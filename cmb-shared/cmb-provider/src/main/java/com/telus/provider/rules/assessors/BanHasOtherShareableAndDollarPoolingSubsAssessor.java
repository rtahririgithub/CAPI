package com.telus.provider.rules.assessors;

import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.PCSAccount;
import com.telus.api.account.PostpaidAccount;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.Subscriber;
import com.telus.provider.rules.ConditionAssessor;
import com.telus.provider.rules.ConditionResult;
import com.telus.provider.rules.RuleConstants;
import com.telus.provider.rules.WorkingMemory;
import com.telus.provider.rules.WorkingMemoryElement;

public class BanHasOtherShareableAndDollarPoolingSubsAssessor extends ConditionAssessor {

	public BanHasOtherShareableAndDollarPoolingSubsAssessor(String conditionId) {
		super(conditionId);
	}

	public ConditionResult evaluate(WorkingMemory workingMemory) throws TelusAPIException {		

		// get the attributes out of working memory required for the evaluation
		WorkingMemoryElement includeReservedElement = (WorkingMemoryElement)workingMemory.getAttribute(
				RuleConstants.ATTRIBUTE_INCLUDE_RESERVED_SUBSCRIBERS);
		WorkingMemoryElement accountElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_ACCOUNT);
		WorkingMemoryElement subscriberElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_SUBSCRIBER);
		WorkingMemoryElement subContextShareableOrDollarPoolingSubsElement = 
			(WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_SUBCONTEXT_SHAREABLE_OR_DOLLAR_POOLING_SUBS);

		// check if any of the working memory attributes have changed
		ConditionResult oldResult = (ConditionResult)workingMemory.getFact(conditionId);
		if (oldResult != null && !includeReservedElement.isModified() && !accountElement.isModified() && !subscriberElement.isModified()) {
			// if not, then simply return the result already in the working memory
			return oldResult;
		}

		// otherwise, perform the evaluation again or for the first time
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
			// check the subscriber context element first to see if there are any other shareable or dollar pooling subscribers
			if (subContextShareableOrDollarPoolingSubsElement != null) {
				List subContextShareableOrDollarPoolingSubs = (List)subContextShareableOrDollarPoolingSubsElement.getElement();
				if ((subContextShareableOrDollarPoolingSubs != null) 
						&& ((subContextShareableOrDollarPoolingSubs.size() > 0 
								&& !subContextShareableOrDollarPoolingSubs.contains(subscriber.getSubscriberId()))
						|| (subContextShareableOrDollarPoolingSubs.size() > 1 
								&& subContextShareableOrDollarPoolingSubs.contains(subscriber.getSubscriberId())))) {
					// we don't need to check any further, we have other subscribers
					result = new ConditionResult(RuleConstants.CONDITION_TYPE_BOOLEAN, Boolean.toString(true));
					
				} else {
					// otherwise, check the subscribers on the account
					result = new ConditionResult(RuleConstants.CONDITION_TYPE_BOOLEAN, 
							Boolean.toString(getAccountShareableAndDollarPoolingStatus(account, subscriber, includeReserved.booleanValue())));
				}
				
			} else {				
				result = new ConditionResult(RuleConstants.CONDITION_TYPE_BOOLEAN, 
						Boolean.toString(getAccountShareableAndDollarPoolingStatus(account, subscriber, includeReserved.booleanValue())));
			}
		}
		
		// add the result to the working memory
		workingMemory.putFact(conditionId, result);

		return result;
	}

	private boolean getAccountShareableAndDollarPoolingStatus(Account account, Subscriber subscriber, boolean includeReserved) 
	throws TelusAPIException {

		// if the account is not a postpaid account, then it can't be shareable or dollar pooling - return false 
		if (!account.isPostpaid())
			return false;
		
		// get the dollar pooling subscriber counts
		PostpaidAccount postpaidAccount = (PostpaidAccount)account;
		PricePlanSubscriberCount[] dollarPoolingCounts = postpaidAccount.getDollarPoolingPricePlanSubscriberCount(false);
		
		// get the subscriber ID - if the subscriber is null, use an empty string
		String subscriberId = subscriber != null ? subscriber.getSubscriberId() : "";
		
		boolean isShareableOrDollarPooling = checkSubscriberCounts(dollarPoolingCounts, account, subscriberId, includeReserved);

		// if no one else on the account is dollar pooling, check if anyone is shareable
		if (!isShareableOrDollarPooling) {
			
			// if the account is not a PCS account, then it can't be shareable - return false 
			if (!account.isPCS())
				return false;

			// get the shareable subscriber counts
			PCSAccount pcsAccount = (PCSAccount)account;
			PricePlanSubscriberCount[] shareablePoolingCounts = pcsAccount.getShareablePricePlanSubscriberCount(false);
			isShareableOrDollarPooling = checkSubscriberCounts(shareablePoolingCounts, account, subscriberId, includeReserved);
		}
		
		return isShareableOrDollarPooling;
	}

	private boolean checkSubscriberCounts(PricePlanSubscriberCount[] subscriberCounts, Account account, String subscriberId, 
			boolean includeReserved) throws TelusAPIException {
		
		// loop through all subscribers
		for (int i = 0; i < (subscriberCounts != null ? subscriberCounts.length : 0); i++) {

			// loop through all active subscribers - if there's at least one other active subscriber, return true
			String[] activeSubs;
			if (includeReserved)
				activeSubs = subscriberCounts[i].getActiveAndReservedSubscribers();
			else
				activeSubs = subscriberCounts[i].getActiveSubscribers();
			
			for (int j = 0; j < (activeSubs != null ? activeSubs.length : 0); j++) {
				if (activeSubs[j] != null && activeSubs[j].trim().length() > 0 && !(activeSubs[j].equals(subscriberId)))
					return true;
			}

			// loop through all suspended subscribers - if there's at least one other subscriber that is 
			// suspended for a valid reason, return true
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
						return true;
				}
			}
		}
		
		// otherwise, return false
		return false;
	}
}