package com.telus.provider.rules.assessors;

import java.util.ArrayList;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Contract;
import com.telus.api.account.Subscriber;
import com.telus.api.rules.Context;
import com.telus.provider.account.TMSubscriber;
import com.telus.provider.rules.ConditionAssessor;
import com.telus.provider.rules.ConditionResult;
import com.telus.provider.rules.RuleConstants;
import com.telus.provider.rules.WorkingMemory;
import com.telus.provider.rules.WorkingMemoryElement;

public class ShareableOrDollarPoolingTransitionAssessor extends ConditionAssessor {
	
	public ShareableOrDollarPoolingTransitionAssessor(String conditionId) {
		super(conditionId);
	}

	public ConditionResult evaluate(WorkingMemory workingMemory) throws TelusAPIException {		
		
		// get the attributes out of working memory required for the evaluation
		WorkingMemoryElement transactionTypeElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_TRANSACTION_TYPE);
		WorkingMemoryElement subscriberElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_SUBSCRIBER);
		WorkingMemoryElement newContractElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_CONTRACT);
		WorkingMemoryElement oldContractElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_OLD_CONTRACT);
		WorkingMemoryElement subContextShareableOrDollarPoolingSubsElement = 
			(WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_SUBCONTEXT_SHAREABLE_OR_DOLLAR_POOLING_SUBS);
		
		// check if any of the working memory attributes have changed
		ConditionResult oldResult = (ConditionResult)workingMemory.getFact(conditionId);
		if (oldResult != null && !subscriberElement.isModified() && !newContractElement.isModified() && !transactionTypeElement.isModified()) {
			// if not, then simply return the result already in the working memory
			return oldResult;
		}
		
		// otherwise, perform the evaluation again or for the first time
		Integer transactionType = (Integer)transactionTypeElement.getElement();	
		Subscriber subscriber = (Subscriber)subscriberElement.getElement();
		Contract newContract = (Contract)newContractElement.getElement();

		ConditionResult result;
		// if this is a service change and the new contract is null, we'll need to retrieve the subscriber's previous contract
		if (transactionType.intValue() == Context.TRANSACTION_TYPE_SERVICE_CHANGE && newContract == null) {
			// the new contract is the one in the subscriber object - it has the contract changes
			newContract = subscriber.getContract();
			// retrieve the subscriber's previous contract from working memory or the database and set it as the old contract
			Contract oldContract;
			if (oldContractElement == null || subscriberElement.isModified()) {
				oldContract = ((TMSubscriber)subscriber).getContract0(true, false);
				workingMemory.putAttribute(RuleConstants.ATTRIBUTE_OLD_CONTRACT, new WorkingMemoryElement("Contract", oldContract, false));
			} else {
				oldContract = (Contract)oldContractElement.getElement();
			}
			result = new ConditionResult(RuleConstants.CONDITION_TYPE_BOOLEAN, 
					Boolean.toString(getShareableOrDollarPoolingChangeTransition(transactionType.intValue(), subscriber, 
							oldContract, newContract)));
			
		} else {			
			result = new ConditionResult(RuleConstants.CONDITION_TYPE_BOOLEAN, 
					Boolean.toString(getShareableOrDollarPoolingChangeTransition(transactionType.intValue(), subscriber, 
							subscriber.getContract(), newContract)));
		}
		
		// update the list of shareable or dollar pooling subscribers from the subscriber context if the result is true
		if (result.getTextResult().equals(RuleConstants.CONDITION_VALUE_BOOLEAN_TRUE)) {			
			// get the list of shareable or dollar pooling subscribers in the subscriber context
			// if we haven't created the subscriber context working memory element yet, do it here
			List subContextShareableOrDollarPoolingSubs;
			if (subContextShareableOrDollarPoolingSubsElement == null) {
				subContextShareableOrDollarPoolingSubs = new ArrayList();
				subContextShareableOrDollarPoolingSubsElement = new WorkingMemoryElement("List", 
						subContextShareableOrDollarPoolingSubs, false);
				workingMemory.putAttribute(RuleConstants.ATTRIBUTE_SUBCONTEXT_SHAREABLE_OR_DOLLAR_POOLING_SUBS, 
						subContextShareableOrDollarPoolingSubsElement);
				
			} else {
				subContextShareableOrDollarPoolingSubs = (List)subContextShareableOrDollarPoolingSubsElement.getElement();
			}
			// add the current subscriber to the list and put everything back into the working memory
			subContextShareableOrDollarPoolingSubs.add(subscriber.getSubscriberId());
			subContextShareableOrDollarPoolingSubsElement.setElement(subContextShareableOrDollarPoolingSubs);
			workingMemory.putAttribute(RuleConstants.ATTRIBUTE_SUBCONTEXT_SHAREABLE_OR_DOLLAR_POOLING_SUBS, 
					subContextShareableOrDollarPoolingSubsElement);
		}
		
		// add the result to the working memory
		workingMemory.putFact(conditionId, result);
		
		return result;
	}
	
	private boolean getShareableOrDollarPoolingChangeTransition(int transactionType, Subscriber subscriber, Contract contract, 
			Contract newContract) throws TelusAPIException {
		
		switch (transactionType) {

		case Context.TRANSACTION_TYPE_RESTORE_RESUME:
			// treat identically to activations - let the case statement fall through
		case Context.TRANSACTION_TYPE_SUSPEND_CANCEL:
			// treat identically to activations - let the case statement fall through
		case Context.TRANSACTION_TYPE_MOVE:
			// treat identically to activations - let the case statement fall through
		case Context.TRANSACTION_TYPE_INITIATE_TOWN:
			// treat identically to activations - let the case statement fall through
		case Context.TRANSACTION_TYPE_COMPLETE_TOWN:			
			// treat identically to activations - let the case statement fall through
		case Context.TRANSACTION_TYPE_ACTIVATION:
			// for activations ignore the new contract
			if (contract.isShareable() || contract.isDollarPooling())
				// we are adding shareable or dollar pooling
				return true;			
			else
				// otherwise, the transition is false
				return false;
			
		case Context.TRANSACTION_TYPE_MIGRATE:
			// treat identically to service changes - let the case statement fall through				
		case Context.TRANSACTION_TYPE_SERVICE_CHANGE:
			// for service changes, ignore the old contract
			if (newContract.isShareable() || newContract.isDollarPooling())
				// we are adding shareable or dollar pooling - return true
				return true;			
			else
				// otherwise, the transition is false
				return false;

		default:
			// if we've reached the default, then simply return false
			return false;
		}
	}
	
}