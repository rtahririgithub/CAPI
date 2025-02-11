package com.telus.provider.rules.assessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Contract;
import com.telus.api.account.Subscriber;
import com.telus.api.rules.Context;
import com.telus.provider.account.TMSubscriber;
import com.telus.provider.rules.ConditionAssessor;
import com.telus.provider.rules.ConditionResult;
import com.telus.provider.rules.RuleConstants;
import com.telus.provider.rules.RuleTemplate;
import com.telus.provider.rules.WorkingMemory;
import com.telus.provider.rules.WorkingMemoryElement;

public class PoolingGroupTransitionAssessor extends ConditionAssessor {
	
	public PoolingGroupTransitionAssessor(String conditionId) {
		super(conditionId);
	}

	public ConditionResult evaluate(WorkingMemory workingMemory) throws TelusAPIException {		
		
		// get the attributes out of working memory required for the evaluation
		WorkingMemoryElement poolingGroupIdElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_POOLING_GROUP_ID);
		WorkingMemoryElement transactionTypeElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_TRANSACTION_TYPE);
		WorkingMemoryElement subscriberElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_SUBSCRIBER);
		WorkingMemoryElement newContractElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_CONTRACT);
		WorkingMemoryElement oldContractElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_OLD_CONTRACT);
		WorkingMemoryElement templateElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_RULE_TEMPLATE);
		WorkingMemoryElement subContextPoolingSubsElement = 
			(WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_SUBCONTEXT_POOLING_SUBS);
		
		// check if any of the working memory attributes have changed
		ConditionResult oldResult = (ConditionResult)workingMemory.getFact(conditionId);
		if (oldResult != null && !subscriberElement.isModified() && !newContractElement.isModified() && !poolingGroupIdElement.isModified() 
				&& !transactionTypeElement.isModified() && !templateElement.isModified()) {
			// if not, then simply return the result already in the working memory
			return oldResult;
		}
		
		// otherwise, perform the evaluation again or for the first time
		Integer poolingGroupId = (Integer)poolingGroupIdElement.getElement();
		Integer transactionType = (Integer)transactionTypeElement.getElement();	
		Subscriber subscriber = (Subscriber)subscriberElement.getElement();
		Contract newContract = (Contract)newContractElement.getElement();
		RuleTemplate template = (RuleTemplate)templateElement.getElement();
	
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
			result = new ConditionResult(RuleConstants.CONDITION_TYPE_TEXT, getPoolingChangeTransition(transactionType.intValue(),
					poolingGroupId.intValue(), template.getType(), subscriber, oldContract, newContract));
			
		} else {
			result = new ConditionResult(RuleConstants.CONDITION_TYPE_TEXT, getPoolingChangeTransition(transactionType.intValue(),
				poolingGroupId.intValue(), template.getType(), subscriber, subscriber.getContract(), newContract));
		}
		
		// update the list of pooling subscribers from the subscriber context if the result is non-pooling to pooling or pooling to pooling
		if (result.getTextResult().equals(RuleConstants.TRANSITION_NONPOOLING_TO_POOLING) 
				|| result.getTextResult().equals(RuleConstants.TRANSITION_POOLING_TO_POOLING)) {
			
			// get the HashMap of pooling subscribers in the subscriber context
			// if we haven't created the subscriber context working memory element yet, do it here
			Map subContextPoolingSubs;
			if (subContextPoolingSubsElement == null) {
				subContextPoolingSubs = new HashMap();
				subContextPoolingSubsElement = new WorkingMemoryElement("Map", subContextPoolingSubs, false);
				workingMemory.putAttribute(RuleConstants.ATTRIBUTE_SUBCONTEXT_POOLING_SUBS, subContextPoolingSubsElement);

			} else {
				subContextPoolingSubs = (HashMap)subContextPoolingSubsElement.getElement();
			}
			// get the list of subscribers transitioning to pooling for the given pooling group
			// if we haven't created the list yet, do it here
			List poolingSubsList = (List)subContextPoolingSubs.get(poolingGroupId);
			if (poolingSubsList == null) {
				poolingSubsList = new ArrayList();
			}
			// add the current subscriber to the list and put everything back into the working memory
			poolingSubsList.add(subscriber.getSubscriberId());
			subContextPoolingSubs.put(poolingGroupId, poolingSubsList);
			subContextPoolingSubsElement.setElement(subContextPoolingSubs);
			workingMemory.putAttribute(RuleConstants.ATTRIBUTE_SUBCONTEXT_POOLING_SUBS, subContextPoolingSubsElement);
		}
		
		// add the result to the working memory
		workingMemory.putFact(conditionId, result);
		
		return result;
	}
	
	private String getPoolingChangeTransition(int transactionType, int poolingGroupId, int ruleType, Subscriber subscriber, 
			Contract contract, Contract newContract) throws TelusAPIException {
		
		switch (transactionType) {

		case Context.TRANSACTION_TYPE_ACTIVATION:
			// for activations ignore the new contract
			if (contract.isPoolingEnabled(poolingGroupId))
				// we are adding the pooling group - the transition is non-pooling to pooling
				return RuleConstants.TRANSITION_NONPOOLING_TO_POOLING;
			else
				// otherwise, the transition is non-pooling to non-pooling
				return RuleConstants.TRANSITION_NONPOOLING_TO_NONPOOLING;
				
		case Context.TRANSACTION_TYPE_SERVICE_CHANGE:
			// compare the pooling state of the old contract and the new contract				
			if (contract.isPoolingEnabled(poolingGroupId) && !newContract.isPoolingEnabled(poolingGroupId))
				// we are removing the pooling group - the transition is pooling to non-pooling
				return RuleConstants.TRANSITION_POOLING_TO_NONPOOLING;			
			else if (!contract.isPoolingEnabled(poolingGroupId) && newContract.isPoolingEnabled(poolingGroupId))
				// we are adding the pooling group - the transition is non-pooling to pooling
				return RuleConstants.TRANSITION_NONPOOLING_TO_POOLING;
			else if (contract.isPoolingEnabled(poolingGroupId) && newContract.isPoolingEnabled(poolingGroupId))
				// we are both adding / removing the pooling group - the transition is pooling to pooling
				return RuleConstants.TRANSITION_POOLING_TO_POOLING;		
			else
				// otherwise, the transition is non-pooling to non-pooling
				return RuleConstants.TRANSITION_NONPOOLING_TO_NONPOOLING;
			
		case Context.TRANSACTION_TYPE_SUSPEND_CANCEL:		
			// for suspend / cancel ignore the new contract
			if (contract.isPoolingEnabled(poolingGroupId))
				// we are removing the pooling group - the transition is pooling to non-pooling
				return RuleConstants.TRANSITION_POOLING_TO_NONPOOLING;
			else
				// otherwise, the transition is non-pooling to non-pooling
				return RuleConstants.TRANSITION_NONPOOLING_TO_NONPOOLING;
			
		case Context.TRANSACTION_TYPE_RESTORE_RESUME:
			// for resumption ignore the new contract
			if (contract.isPoolingEnabled(poolingGroupId))
				// we are adding the pooling group - the transition is non-pooling to pooling
				return RuleConstants.TRANSITION_NONPOOLING_TO_POOLING;				
			else
				// otherwise, the transition is non-pooling to non-pooling
				return RuleConstants.TRANSITION_NONPOOLING_TO_NONPOOLING;
				
		case Context.TRANSACTION_TYPE_MOVE:
			// for moves we need to return the transition for both ends of the transaction
			switch (ruleType) {
			// if the this is an add service or conflicting service rule, then the transition is relative to the target account
			case RuleConstants.RULE_TYPE_MP_CONFLICTING_SERVICE:
			// let the case statement fall through
			case RuleConstants.RULE_TYPE_MP_ADD_SERVICE:
				if (contract.isPoolingEnabled(poolingGroupId))
					// we are adding the pooling group - the transition is non-pooling to pooling
					return RuleConstants.TRANSITION_NONPOOLING_TO_POOLING;	
				else
					// otherwise, the transition is non-pooling to non-pooling
					return RuleConstants.TRANSITION_NONPOOLING_TO_NONPOOLING;
			
			// for everything else (i.e., remove service rule or bill cycle rule), the transition is relative to the source account
			default:				
				if (contract.isPoolingEnabled(poolingGroupId))
					// we are removing the pooling group - the transition is pooling to non-pooling
					return RuleConstants.TRANSITION_POOLING_TO_NONPOOLING;				
				else
					// otherwise, the transition is non-pooling to non-pooling
					return RuleConstants.TRANSITION_NONPOOLING_TO_NONPOOLING;
			}

		case Context.TRANSACTION_TYPE_INITIATE_TOWN:
			// for initiate TOWN, we only need to consider remove service rules and the transition is relative to the source account	
			if (contract.isPoolingEnabled(poolingGroupId))
				// we are removing the pooling group - the transition is pooling to non-pooling
				return RuleConstants.TRANSITION_POOLING_TO_NONPOOLING;				
			else
				// otherwise, the transition is non-pooling to non-pooling
				return RuleConstants.TRANSITION_NONPOOLING_TO_NONPOOLING;

		case Context.TRANSACTION_TYPE_COMPLETE_TOWN:
			// for complete TOWN, we only need to consider add service or conflicting service rules and the transition is 
			// relative to the target account	
			if (contract.isPoolingEnabled(poolingGroupId))
				// we are adding the pooling group - the transition is non-pooling to pooling
				return RuleConstants.TRANSITION_NONPOOLING_TO_POOLING;	
			else
				// otherwise, the transition is non-pooling to non-pooling
				return RuleConstants.TRANSITION_NONPOOLING_TO_NONPOOLING;
			
		case Context.TRANSACTION_TYPE_MIGRATE:
			// for migrations we need to return the transition for both ends of the transaction, but this also involves a new contract
			switch (ruleType) {
			// if the this is a conflicting service rule, then the transition is relative to the target account
			case RuleConstants.RULE_TYPE_MP_CONFLICTING_SERVICE:
				// check the pooling state of the new contract		
				if (newContract.isPoolingEnabled(poolingGroupId))
					// we are adding the pooling group - the transition is non-pooling to pooling
					return RuleConstants.TRANSITION_NONPOOLING_TO_POOLING;	
				else
					// otherwise, the transition is non-pooling to non-pooling
					return RuleConstants.TRANSITION_NONPOOLING_TO_NONPOOLING;
			// if the this is an add service rule, then the transition is relative to the target account
			case RuleConstants.RULE_TYPE_MP_ADD_SERVICE:
				// compare the pooling state of the old contract and the new contract				
				if (contract.isPoolingEnabled(poolingGroupId) && !newContract.isPoolingEnabled(poolingGroupId))
					// we are removing the pooling group - the transition is pooling to non-pooling
					return RuleConstants.TRANSITION_POOLING_TO_NONPOOLING;			
				else if (!contract.isPoolingEnabled(poolingGroupId) && newContract.isPoolingEnabled(poolingGroupId))
					// we are adding the pooling group - the transition is non-pooling to pooling
					return RuleConstants.TRANSITION_NONPOOLING_TO_POOLING;
				else if (contract.isPoolingEnabled(poolingGroupId) && newContract.isPoolingEnabled(poolingGroupId))
					// we are both adding / removing the pooling group - the transition is pooling to pooling
					return RuleConstants.TRANSITION_POOLING_TO_POOLING;
				else
					// otherwise, the transition is non-pooling to non-pooling
					return RuleConstants.TRANSITION_NONPOOLING_TO_NONPOOLING;
			
			// for everything else (i.e., remove service rule), the transition is relative to the source account
			default:		
				if (contract.isPoolingEnabled(poolingGroupId))
					// we are removing the pooling group - the transition is pooling to non-pooling
					return RuleConstants.TRANSITION_POOLING_TO_NONPOOLING;				
				else
					// otherwise, the transition is non-pooling to non-pooling
					return RuleConstants.TRANSITION_NONPOOLING_TO_NONPOOLING;
			}			
			
		default:
			// if we've reached the default, then simply return non-pooling to non-pooling
			return RuleConstants.TRANSITION_NONPOOLING_TO_NONPOOLING;
		}
	}

}