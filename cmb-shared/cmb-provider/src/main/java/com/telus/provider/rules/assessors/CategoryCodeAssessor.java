package com.telus.provider.rules.assessors;

import com.telus.api.TelusAPIException;
import com.telus.provider.rules.ConditionAssessor;
import com.telus.provider.rules.ConditionResult;
import com.telus.provider.rules.RuleConstants;
import com.telus.provider.rules.WorkingMemory;
import com.telus.provider.rules.WorkingMemoryElement;

public class CategoryCodeAssessor extends ConditionAssessor {
	
	public CategoryCodeAssessor(String conditionId) {
		super(conditionId);
	}

	public ConditionResult evaluate(WorkingMemory workingMemory) throws TelusAPIException {		
		
		// get the attributes out of working memory required for the evaluation
		WorkingMemoryElement categoryCodeElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_CATEGORY_CODE);
		
		// check if any of the working memory attributes have changed
		ConditionResult oldResult = (ConditionResult)workingMemory.getFact(conditionId);
		if (oldResult != null && !categoryCodeElement.isModified()) {
			// if not, then simply return the result already in the working memory
			return oldResult;
		}
		
		// otherwise, perform the evaluation again or for the first time
		String categoryCode = (String)categoryCodeElement.getElement();

		ConditionResult result = new ConditionResult(RuleConstants.CONDITION_TYPE_TEXT, categoryCode);

		// add the result to the working memory
		workingMemory.putFact(conditionId, result);
		
		return result;
	}

}