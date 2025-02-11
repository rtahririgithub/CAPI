package com.telus.eas.framework.eligibility.rules;

import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;

public class AccountTypeEvaluationRule extends TokenSetEvaluationRule {

	public void setAccountCombinedTypes(String accountCombinedTypes) {
		setTokens(accountCombinedTypes);
	}
	
	
	public void setAccountType(String accountType) {
		setTokens(accountType);
	}
	
	protected String getTargetToken(EligibilityCheckCriteria criteria) {
		return criteria.getAccountCombinedType();
	}

	public String toString() {
		return "Account type reference values: " + tokens + "";
	}

}
