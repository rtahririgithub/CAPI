package com.telus.eas.framework.eligibility.rules;

import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;

public class AccountSubTypeEvaluationRule extends TokenSetEvaluationRule {

	public void setAccountSubType(String accountSubType) {
		setTokens(accountSubType);
	}
	
	protected String getTargetToken(EligibilityCheckCriteria criteria) {
		return criteria.getAccountSubType();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "AccountSubtype reference values: " + tokens + "";
	}

}
