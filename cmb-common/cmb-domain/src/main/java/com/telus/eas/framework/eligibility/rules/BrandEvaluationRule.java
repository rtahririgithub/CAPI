package com.telus.eas.framework.eligibility.rules;

import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;

public class BrandEvaluationRule extends ReferenceComparisonEvaluationRule {

	public BrandEvaluationRule() {
		super(OP_EQUAL);
	}
	
	public void setBrandId(String brandId) {
		setReferenceValue(brandId);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.provider.eligibility.interservice.impl.rules.ReferenceValueEvaluationRule#getEvaluationValue(com.telus.provider.eligibility.interservice.InternationalServiceEligibilityCheckCriteria)
	 */
	protected Object getEvaluationValue(EligibilityCheckCriteria criteria) {
		return Integer.toString(criteria.getBrandId());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Brand reference value: " + getReferenceValue();
	}
}
