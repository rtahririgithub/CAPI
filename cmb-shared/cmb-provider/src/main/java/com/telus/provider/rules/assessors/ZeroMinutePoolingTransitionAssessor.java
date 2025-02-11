package com.telus.provider.rules.assessors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Contract;
import com.telus.api.account.ContractService;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.PoolingGroup;
import com.telus.api.reference.Service;
import com.telus.provider.rules.ConditionAssessor;
import com.telus.provider.rules.ConditionResult;
import com.telus.provider.rules.RuleConstants;
import com.telus.provider.rules.WorkingMemory;
import com.telus.provider.rules.WorkingMemoryElement;

public class ZeroMinutePoolingTransitionAssessor extends ConditionAssessor {
	
	public ZeroMinutePoolingTransitionAssessor(String conditionId) {
		super(conditionId);
	}

	public ConditionResult evaluate(WorkingMemory workingMemory) throws TelusAPIException {		
		
		// get the attributes out of working memory required for the evaluation
		WorkingMemoryElement poolingGroupIdElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_POOLING_GROUP_ID);
		WorkingMemoryElement subscriberElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_SUBSCRIBER);
		WorkingMemoryElement newContractElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_CONTRACT);
		WorkingMemoryElement minutePoolingContributorServicesElement = (WorkingMemoryElement)workingMemory.getAttribute(
				RuleConstants.ATTRIBUTE_MINUTE_POOLING_CONTRIBUTOR_SERVICES);
		WorkingMemoryElement poolingGroupsElement = (WorkingMemoryElement)workingMemory.getAttribute(
				RuleConstants.ATTRIBUTE_POOLING_GROUPS);
		
		// check if any of the working memory attributes have changed
		ConditionResult oldResult = (ConditionResult)workingMemory.getFact(conditionId);
		if (oldResult != null && !subscriberElement.isModified() && !newContractElement.isModified() && !poolingGroupIdElement.isModified() 
				&& !minutePoolingContributorServicesElement.isModified()) {
			// if not, then simply return the result already in the working memory
			return oldResult;
		}
		
		// otherwise, perform the evaluation again or for the first time
		Integer poolingGroupId = (Integer)poolingGroupIdElement.getElement();
		Subscriber subscriber = (Subscriber)subscriberElement.getElement();
		Contract newContract = (Contract)newContractElement.getElement();
		Service[] services = (Service[])minutePoolingContributorServicesElement.getElement();
		PoolingGroup[] poolingGroups = (PoolingGroup[])poolingGroupsElement.getElement();
		
		ConditionResult result;
		// if the new contract is null, use the subscriber's old contract
		if (newContract == null) {
			result = new ConditionResult(RuleConstants.CONDITION_TYPE_BOOLEAN, Boolean.toString(
					getZeroMinutePoolingChangeTransition(poolingGroupId.intValue(), subscriber.getContract(), services, poolingGroups)));
			
		} else {
			result = new ConditionResult(RuleConstants.CONDITION_TYPE_BOOLEAN, Boolean.toString(
					getZeroMinutePoolingChangeTransition(poolingGroupId.intValue(), newContract, services, poolingGroups)));
		}
		
		// add the result to the working memory
		workingMemory.putFact(conditionId, result);
		
		return result;
	}
	
	private boolean getZeroMinutePoolingChangeTransition(int poolingGroupId, Contract contract,	Service[] minutePoolingContributorServices, 
			PoolingGroup[] poolingGroups) throws TelusAPIException {

		// initialize isZeroMinutePooling to false
		boolean isZeroMinutePooling = false;
		
		// first, is the price plan pooling capable?
		if (contract.getPricePlan().isMinutePoolingCapable()) {
			// check if the contract is pooling and if there are any included combined minutes
			if (contract.isPoolingEnabled(poolingGroupId) && contract.getPricePlan().isZeroIncludedMinutes()) {
				// if the contract is pooling and there are no included combined minutes, then (so far) the contract
				// is zero-minute pooling
				isZeroMinutePooling = true;		
				
				// check if any pooling contributor services are optional services
				List contributorList = new ArrayList();
				ContractService[] services = contract.getOptionalServices();
				for (int i = 0; i < services.length; i++) {
					for (int j = 0; j < minutePoolingContributorServices.length; j++) {
						if (services[i].getCode().equalsIgnoreCase(minutePoolingContributorServices[j].getCode())) {
							contributorList.add(services[i]);
						}
					}
				}
	
				// get the coverage type for the pooling group
				String poolingCoverageType = "";
				for (int k = 0; k < poolingGroups.length; k++) {
					if (poolingGroups[k].getPoolingGroupId() == poolingGroupId) {
						poolingCoverageType = poolingGroups[k].getCoverageType();
						break;
					}
				}
				
				// iterate through the list of services and see if anything matches the pooling coverage type
				Iterator iterator = contributorList.iterator();
				while (iterator.hasNext()) {
					ContractService service = (ContractService)iterator.next();
					if (service.getService().getCoverageType().equalsIgnoreCase(poolingCoverageType)) {
						// if the coverage types match, then this service contributes minutes to this pool and
						// the contract is not zero-minute pooling
						isZeroMinutePooling = false;
						break;
					}
				}
			}
		}

		return isZeroMinutePooling;
	}
	
}