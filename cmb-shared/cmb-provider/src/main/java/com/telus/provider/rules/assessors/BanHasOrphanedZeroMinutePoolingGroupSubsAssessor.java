package com.telus.provider.rules.assessors;

import java.util.HashSet;
import java.util.Set;

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

public class BanHasOrphanedZeroMinutePoolingGroupSubsAssessor extends ConditionAssessor {

	public BanHasOrphanedZeroMinutePoolingGroupSubsAssessor(String conditionId) {
		super(conditionId);
	}

	public ConditionResult evaluate(WorkingMemory workingMemory) throws TelusAPIException {		

		// get the attributes out of working memory required for the evaluation
		WorkingMemoryElement poolingGroupIdElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_POOLING_GROUP_ID);
		WorkingMemoryElement accountElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_ACCOUNT);
		WorkingMemoryElement subscriberElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_SUBSCRIBER);

		// check if any of the working memory attributes have changed
		ConditionResult oldResult = (ConditionResult)workingMemory.getFact(conditionId);
		if (oldResult != null && !poolingGroupIdElement.isModified() && !accountElement.isModified() && !subscriberElement.isModified()) {
			// if not, then simply return the result already in the working memory
			return oldResult;
		}

		// otherwise, perform the evaluation again or for the first time
		Integer poolingGroupId = (Integer)poolingGroupIdElement.getElement();
		Account account = (Account)accountElement.getElement();
		Subscriber subscriber = (Subscriber)subscriberElement.getElement();

		// retrieve the helper assessor class, or create it if required
		ConditionAssessor banSizeThresholdAssessor = getHelperAssessor(BanSizeThresholdAssessor.class.getName(), workingMemory);
		ConditionResult result = banSizeThresholdAssessor.evaluate(workingMemory);
		
		// if the BAN size exceeds the threshold, return the default condition result
		if (result.getTextResult().equals(RuleConstants.CONDITION_VALUE_BOOLEAN_TRUE))
			result = ConditionResult.DEFAULT;
		else
			result = new ConditionResult(RuleConstants.CONDITION_TYPE_BOOLEAN, 
					Boolean.toString(getOrphanedPoolingStatus(poolingGroupId.intValue(), account, subscriber)));
		
		// add the result to the working memory
		workingMemory.putFact(conditionId, result);

		return result;
	}

	private boolean getOrphanedPoolingStatus(int poolingGroupId, Account account, Subscriber subscriber) throws TelusAPIException {

		// if the account is not a postpaid account, then it isn't capable of pooling - return false 
		if (!account.isPostpaid())
			return false;

		// get the pooling counts
		PostpaidAccount postpaidAccount = (PostpaidAccount)account;
		PoolingPricePlanSubscriberCount poolingCounts = postpaidAccount.getPoolingEnabledPricePlanSubscriberCount(poolingGroupId, false);
		PoolingPricePlanSubscriberCount zeroPoolingCounts = postpaidAccount.getZeroMinutePoolingEnabledPricePlanSubscriberCount(poolingGroupId, false);
		
		// check if there is anything returned for pooling subscribers
		PricePlanSubscriberCount[] subscriberCounts = null;
		if (poolingCounts != null) {
			subscriberCounts = poolingCounts.getPricePlanSubscriberCount();
		}
		
		// check if there is anything returned for zero-minute pooling subscribers
		PricePlanSubscriberCount[] zeroMinuteSubscriberCounts = null;
		if (zeroPoolingCounts != null) {
			zeroMinuteSubscriberCounts = zeroPoolingCounts.getPricePlanSubscriberCount();
		}
		
		// if either subscriber counts are null, then there are no orphaned zero-minute pooling subscribers
		if (subscriberCounts == null || zeroMinuteSubscriberCounts == null)
			return false;
		
		// get the subscriber ID - if the subscriber is null, use an empty string
		String subscriberId = subscriber != null ? subscriber.getSubscriberId() : "";
		
		// get the set of all pooling subscribers
		Set poolingSubs = getSubscriberSet(subscriberCounts, account, subscriberId);
		
		// get the set of zero-minute pooling subscribers
		Set zeroMinuteSubs = getSubscriberSet(zeroMinuteSubscriberCounts, account, subscriberId);
		
		// compare the sets
		return (zeroMinuteSubs.size() > 0 && (zeroMinuteSubs.size() == poolingSubs.size()));
	}

	private Set getSubscriberSet(PricePlanSubscriberCount[] subscriberCounts, Account account, String subscriberId) 
	throws TelusAPIException {
	
		Set subs = new HashSet();
		
		// loop through all subscribers
		for (int i = 0; i < (subscriberCounts != null ? subscriberCounts.length : 0); i++) {
			
			// loop through all active subscribers - if there's at least one other active subscriber, add them to the set
			String[] activeSubs = subscriberCounts[i].getActiveSubscribers();			
			for (int j = 0; j < (activeSubs != null ? activeSubs.length : 0); j++) {
				if (activeSubs[j] != null && activeSubs[j].trim().length() > 0 && !(activeSubs[j].equals(subscriberId)))
					subs.add(activeSubs[j]);
			}

			// loop through all suspended subscribers - if there's at least one other subscriber that is 
			// suspended for a valid reason, add them to the set
			String[] suspendedSubs = subscriberCounts[i].getSuspendedSubscribers();
			for (int k = 0; k < (suspendedSubs != null ? suspendedSubs.length : 0); k++) {
				if (suspendedSubs[k] != null && suspendedSubs[k].trim().length() > 0 && !(suspendedSubs[k].equals(subscriberId))) {
					// unfortunately, we have to retrieve the subscriber to get their last activity reason code
					// let's hope this number is small
					Subscriber sub = account.getSubscriber(suspendedSubs[k]);
					// only recognize suspended subscribers with the following criteria
					if (sub != null	&& (sub.getActivityReasonCode().equalsIgnoreCase(RuleConstants.ACTIVITY_REASON_SUSPEND_LOST) 
							|| sub.getActivityReasonCode().equalsIgnoreCase(RuleConstants.ACTIVITY_REASON_SUSPEND_STOLEN) 
							|| sub.getActivityReasonCode().equalsIgnoreCase(RuleConstants.ACTIVITY_REASON_SUSPEND_VACATION)))
						subs.add(suspendedSubs[k]);
				}
			}
		}

		return subs;
	}
}