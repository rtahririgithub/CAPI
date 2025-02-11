package com.telus.provider.rules.assessors;

import java.util.List;
import java.util.Map;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.PoolingPricePlanSubscriberCount;
import com.telus.api.account.PostpaidAccount;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.Subscriber;
import com.telus.provider.rules.ConditionAssessor;
import com.telus.provider.rules.ConditionResult;
import com.telus.provider.rules.RuleConstants;
import com.telus.provider.rules.WorkingMemory;
import com.telus.provider.rules.WorkingMemoryElement;

public class BanHasOtherMinutePoolingSubsAssessor extends ConditionAssessor {

	public BanHasOtherMinutePoolingSubsAssessor(String conditionId) {
		super(conditionId);
	}

	public ConditionResult evaluate(WorkingMemory workingMemory) throws TelusAPIException {		

		// get the attributes out of working memory required for the evaluation
		WorkingMemoryElement poolingGroupIdElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_POOLING_GROUP_ID);
		WorkingMemoryElement includeReservedElement = (WorkingMemoryElement)workingMemory.getAttribute(
				RuleConstants.ATTRIBUTE_INCLUDE_RESERVED_SUBSCRIBERS);
		WorkingMemoryElement accountElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_ACCOUNT);
		WorkingMemoryElement subscriberElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_SUBSCRIBER);
		WorkingMemoryElement subContextPoolingSubsElement = 
			(WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_SUBCONTEXT_POOLING_SUBS);

		// check if any of the working memory attributes have changed
		ConditionResult oldResult = (ConditionResult)workingMemory.getFact(conditionId);
		if (oldResult != null && !poolingGroupIdElement.isModified() && !includeReservedElement.isModified() && !accountElement.isModified() 
				&& !subscriberElement.isModified()) {
			// if not, then simply return the result already in the working memory
			return oldResult;
		}

		// otherwise, perform the evaluation again or for the first time
		Integer poolingGroupId = (Integer)poolingGroupIdElement.getElement();
		Boolean includeReserved = (Boolean)includeReservedElement.getElement();
		Account account = (Account)accountElement.getElement();
		Subscriber subscriber = (Subscriber)subscriberElement.getElement();

		// retrieve the helper assessor class, or create it if required
		ConditionAssessor banSizeThresholdAssessor = getHelperAssessor(BanSizeThresholdAssessor.class.getName(), workingMemory);
		ConditionResult result = banSizeThresholdAssessor.evaluate(workingMemory);
		
		// if the BAN size exceeds the threshold, return the default condition result
		if (result.getTextResult().equals(RuleConstants.CONDITION_VALUE_BOOLEAN_TRUE)) {
			result = ConditionResult.DEFAULT;
			
		} else {
			// check the subscriber context element first to see if there are any other pooling subscribers
			if (subContextPoolingSubsElement != null) {
				Map subContextPoolingSubs = (Map)subContextPoolingSubsElement.getElement();
				List poolingSubsList = (List)subContextPoolingSubs.get(poolingGroupId);
				if ((poolingSubsList != null) 
						&& ((poolingSubsList.size() > 0 && !poolingSubsList.contains(subscriber.getSubscriberId())) 
								|| (poolingSubsList.size() > 1 && poolingSubsList.contains(subscriber.getSubscriberId())))) {
						// we don't need to check any further, we have other pooling subscribers
					result = new ConditionResult(RuleConstants.CONDITION_TYPE_BOOLEAN, Boolean.toString(true));

				} else {
					// otherwise, check the subscribers on the account
					result = new ConditionResult(RuleConstants.CONDITION_TYPE_BOOLEAN, 
							Boolean.toString(getAccountPoolingStatus(poolingGroupId.intValue(), account, subscriber, 
									includeReserved.booleanValue())));
				}

			} else {
				result = new ConditionResult(RuleConstants.CONDITION_TYPE_BOOLEAN, 
					Boolean.toString(getAccountPoolingStatus(poolingGroupId.intValue(), account, subscriber, includeReserved.booleanValue())));
			}
		}

		// add the result to the working memory
		workingMemory.putFact(conditionId, result);

		return result;
	}

	private boolean getAccountPoolingStatus(int poolingGroupId, Account account, Subscriber subscriber, boolean includeReserved) 
	throws TelusAPIException {

		// if the account is not a postpaid account, then it isn't capable of pooling - return false 
		if (!account.isPostpaid())
			return false;

		// get the pooling subscriber counts
		PostpaidAccount postpaidAccount = (PostpaidAccount)account;
		PoolingPricePlanSubscriberCount[] poolingCounts = postpaidAccount.getPoolingEnabledPricePlanSubscriberCount(false);

		// get the subscriber ID - if the subscriber is null, use an empty string
		String subscriberId = subscriber != null ? subscriber.getSubscriberId() : "";

		// loop through all pools
		for (int i = 0; i < (poolingCounts != null ? poolingCounts.length : 0); i++) {

			// get the subscriber counts for the current pool
			PricePlanSubscriberCount[] subscriberCounts = poolingCounts[i].getPricePlanSubscriberCount();

			// loop through all subscribers
			for (int j = 0; j < (subscriberCounts != null ? subscriberCounts.length : 0); j++) {

				// loop through all active and / or reserved subscribers - if there's at least one other pooling subscriber, return true
				String[] activeSubs;
				if (includeReserved)
					activeSubs = subscriberCounts[j].getActiveAndReservedSubscribers();
				else
					activeSubs = subscriberCounts[j].getActiveSubscribers();
				
				for (int k = 0; k < (activeSubs != null ? activeSubs.length : 0); k++) {
					if (activeSubs[k] != null && activeSubs[k].trim().length() > 0 && !(activeSubs[k].equals(subscriberId)))
						return true;
				}

				// loop through all suspended subscribers - if there's at least one other pooling subscriber that is 
				// suspended for a valid reason, return true
				String[] suspendedSubs = subscriberCounts[j].getSuspendedSubscribers();
				for (int l = 0; l < (suspendedSubs != null ? suspendedSubs.length : 0); l++) {
					if (suspendedSubs[l] != null && suspendedSubs[l].trim().length() > 0 && !(suspendedSubs[l].equals(subscriberId))) {
						// unfortunately, we have to retrieve the subscriber to get their last activity reason code
						// let's hope this number is small
						Subscriber sub = account.getSubscriber(suspendedSubs[l]);
						// only recognize suspended subscribers with the following criteria
						if (sub != null	&& (sub.getActivityReasonCode().equalsIgnoreCase(RuleConstants.ACTIVITY_REASON_SUSPEND_LOST) 
								|| sub.getActivityReasonCode().equalsIgnoreCase(RuleConstants.ACTIVITY_REASON_SUSPEND_STOLEN) 
								|| sub.getActivityReasonCode().equalsIgnoreCase(RuleConstants.ACTIVITY_REASON_SUSPEND_VACATION)))
							return true;
					}
				}
			}
		}

		// otherwise, return false
		return false;
	}

}