package com.telus.provider.rules.assessors;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.InvalidBillCycleChangeException;
import com.telus.api.account.PostpaidAccount;
import com.telus.provider.rules.ConditionAssessor;
import com.telus.provider.rules.ConditionResult;
import com.telus.provider.rules.RuleConstants;
import com.telus.provider.rules.WorkingMemory;
import com.telus.provider.rules.WorkingMemoryElement;

public class BillCycleRunAssessor extends ConditionAssessor {

	public BillCycleRunAssessor(String conditionId) {
		super(conditionId);
	}

	public ConditionResult evaluate(WorkingMemory workingMemory) throws TelusAPIException {		

		// get the attributes out of working memory required for the evaluation
		WorkingMemoryElement accountElement = (WorkingMemoryElement)workingMemory.getAttribute(RuleConstants.ATTRIBUTE_ACCOUNT);
		
		// check if any of the working memory attributes have changed
		ConditionResult oldResult = (ConditionResult)workingMemory.getFact(conditionId);
		if (oldResult != null && !accountElement.isModified()) {
			// if not, then simply return the result already in the working memory
			return oldResult;
		}
		
		// otherwise, perform the evaluation again or for the first time
		Account account = (Account)accountElement.getElement();

		boolean billCycleRun = true;
		if (account == null || !(account instanceof PostpaidAccount)) {
			billCycleRun = false;
		} else {
			try {
				((PostpaidAccount)account).testCycleChange();
			} catch (InvalidBillCycleChangeException ibcce) {
				billCycleRun = false;
			}
		}
		
		ConditionResult result = new ConditionResult(RuleConstants.CONDITION_TYPE_BOOLEAN, Boolean.toString(billCycleRun));
		
		// add the result to the working memory
		workingMemory.putFact(conditionId, result);

		return result;
	}

}
