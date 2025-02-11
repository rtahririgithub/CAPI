package com.telus.provider.eligibility.interservice.impl.rules;

import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;
import com.telus.eas.framework.eligibility.rules.ReferenceComparisonEvaluationRule;
import com.telus.provider.eligibility.interservice.InternationalServiceEligibilityCheckCriteria;

public class TenureEvaluationRule extends ReferenceComparisonEvaluationRule {

	public void setLimit(Integer limit) {
		setReferenceValue(limit);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.provider.eligibility.interservice.impl.rules.ReferenceComparisonEvaluationRule#getEvaluationValue(com.telus.provider.eligibility.interservice.InternationalServiceEligibilityCheckCriteria)
	 */
	protected Object getEvaluationValue(EligibilityCheckCriteria criteria) {
		return new Integer(((InternationalServiceEligibilityCheckCriteria)criteria).getTenure());
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Tenure reference value: [" + getOperation() + " then " + getReferenceValue();
	}	

}
