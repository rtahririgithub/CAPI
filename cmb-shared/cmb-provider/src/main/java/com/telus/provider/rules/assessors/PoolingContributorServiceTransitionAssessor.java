package com.telus.provider.rules.assessors;

import java.util.Arrays;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Contract;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.PoolingGroup;
import com.telus.api.reference.Service;
import com.telus.provider.rules.ConditionAssessor;
import com.telus.provider.rules.ConditionResult;
import com.telus.provider.rules.RuleConstants;
import com.telus.provider.rules.WorkingMemory;
import com.telus.provider.rules.WorkingMemoryElement;

public class PoolingContributorServiceTransitionAssessor extends ConditionAssessor {

	public PoolingContributorServiceTransitionAssessor(String conditionId) {
		super(conditionId);
	}

	public ConditionResult evaluate(WorkingMemory workingMemory) throws TelusAPIException {		

		// get the attributes out of working memory required for the evaluation
		WorkingMemoryElement subscriberElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_SUBSCRIBER);
		WorkingMemoryElement newContractElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_CONTRACT);
		WorkingMemoryElement serviceElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_SERVICE);
		WorkingMemoryElement poolingContributorServicesElement = 
			(WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_MINUTE_POOLING_CONTRIBUTOR_SERVICES);
		WorkingMemoryElement poolingGroupsElement = (WorkingMemoryElement)workingMemory.getAttribute(
				RuleConstants.ATTRIBUTE_POOLING_GROUPS);
		
		// check if any of the working memory attributes have changed
		ConditionResult oldResult = (ConditionResult)workingMemory.getFact(conditionId);
		if (oldResult != null && !subscriberElement.isModified() && !newContractElement.isModified() && !serviceElement.isModified()) {
			// if not, then simply return the result already in the working memory
			return oldResult;
		}
		
		// otherwise, perform the evaluation again or for the first time
		Subscriber subscriber = (Subscriber)subscriberElement.getElement();
		Contract newContract = (Contract)newContractElement.getElement();
		Service service = (Service)serviceElement.getElement();
		Service[] poolingContributorServices = (Service[])poolingContributorServicesElement.getElement();
		PoolingGroup[] poolingGroups = (PoolingGroup[])poolingGroupsElement.getElement();

		ConditionResult result;
		// if the new contract is null, use the subscriber's old contract
		if (newContract == null) {
			result = new ConditionResult(RuleConstants.CONDITION_TYPE_BOOLEAN, Boolean.toString(
					isPoolingContributorServiceTransition(subscriber.getContract(), service, poolingContributorServices, poolingGroups)));
			
		} else {
			result = new ConditionResult(RuleConstants.CONDITION_TYPE_BOOLEAN, Boolean.toString(
					isPoolingContributorServiceTransition(newContract, service, poolingContributorServices, poolingGroups)));
		}

		// add the result to the working memory
		workingMemory.putFact(conditionId, result);
		
		return result;
	}
	
	private boolean isPoolingContributorServiceTransition(Contract contract, Service service, Service[] poolingContributorServices, 
			PoolingGroup[] poolingGroups) throws TelusAPIException {

		// initialize isPoolingContributorServiceTransition to false
		boolean isPoolingContributorServiceTransition = false;
		
		// first, is the price plan pooling capable?
		if (contract.getPricePlan().isMinutePoolingCapable() && isServiceInList(service, poolingContributorServices)) {
			// cycle through all pooling groups and check the coverage type
			for (int i = 0; i < poolingGroups.length; i++) {
				if (contract.isPoolingEnabled(poolingGroups[i].getPoolingGroupId()) && compareCoverageTypes(poolingGroups[i], service)) {
					// if the coverage types match, then this service contributes minutes to this pool
					isPoolingContributorServiceTransition = true;
					break;
				}				
			}
		}

		return isPoolingContributorServiceTransition;
	}
	
	
	// Checks if a given service is in the provided array of services.
	private boolean isServiceInList(Service service, Service[] services) {		
		// loop through the services array and compare with the given service
		for (int i = 0; i < services.length; i++) {
			if (services[i].getCode().equals(service.getCode()))
				return true;
		}
		
		return false;
	}
	
	// Compare the coverage types of the current pooling group and service.
	private boolean compareCoverageTypes(PoolingGroup poolingGroup, Service service) {		
		// long distance coverage types are 'O' and 'G' - with respect to pooling groups, these values are interchangeable
		List longDistanceCoverageTypes = Arrays.asList(PoolingGroup.longDistanceCoverageTypes);
		if (longDistanceCoverageTypes.contains(poolingGroup.getCoverageType()) && 
				longDistanceCoverageTypes.contains(service.getCoverageType()))
			return true;

		if (poolingGroup.getCoverageType().equalsIgnoreCase(service.getCoverageType()))
			return true;
		
		return false;
	}
	
}