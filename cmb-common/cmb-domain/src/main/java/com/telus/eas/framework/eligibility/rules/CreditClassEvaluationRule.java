package com.telus.eas.framework.eligibility.rules;

import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;

public class CreditClassEvaluationRule extends ReferenceComparisonEvaluationRule {

	public CreditClassEvaluationRule() {
		super(OP_EQUAL);
	}
	
	public void setCreditClass(String creditClass) {
		setReferenceValue(creditClass);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.provider.eligibility.interservice.impl.rules.ReferenceValueEvaluationRule#getEvaluationValue(com.telus.provider.eligibility.interservice.InternationalServiceEligibilityCheckCriteria)
	 */
	protected Object getEvaluationValue(EligibilityCheckCriteria criteria) {
		return criteria.getCreditClass();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Credit class reference value: " + getReferenceValue();
	}
}
