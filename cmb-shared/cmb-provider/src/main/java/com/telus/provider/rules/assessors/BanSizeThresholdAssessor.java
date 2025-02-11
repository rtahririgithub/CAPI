package com.telus.provider.rules.assessors;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.provider.rules.ConditionAssessor;
import com.telus.provider.rules.ConditionResult;
import com.telus.provider.rules.RuleConstants;
import com.telus.provider.rules.WorkingMemory;
import com.telus.provider.rules.WorkingMemoryElement;

public class BanSizeThresholdAssessor extends ConditionAssessor {

	public BanSizeThresholdAssessor(String conditionId) {
		super(conditionId);
	}
	
	public ConditionResult evaluate(WorkingMemory workingMemory) throws TelusAPIException {		
		
		// get the attributes out of working memory required for the evaluation
		WorkingMemoryElement accountElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_ACCOUNT);
		WorkingMemoryElement banSizeThresholdElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_BAN_SIZE_THRESHOLD);
		
		// check if any of the working memory attributes have changed
		ConditionResult oldResult = (ConditionResult)workingMemory.getFact(conditionId);
		if (oldResult != null && !accountElement.isModified() && !banSizeThresholdElement.isModified()) {
			// if not, then simply return the result already in the working memory
			return oldResult;
		}
		
		// otherwise, perform the evaluation again or for the first time
		Account account = (Account)accountElement.getElement();
		Integer banSizeThreshold = (Integer)banSizeThresholdElement.getElement();
		ConditionResult result = new ConditionResult(RuleConstants.CONDITION_TYPE_BOOLEAN, 
				Boolean.toString(account.getAllSubscriberCount() >= banSizeThreshold.intValue()));
		
		// add the result to the working memory
		workingMemory.putFact(conditionId, result);
		
		return result;
	}

}
