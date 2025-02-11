package com.telus.eas.framework.eligibility.rules;

import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;

public class ProductTypeEvaluationRule extends ReferenceComparisonEvaluationRule {

	public ProductTypeEvaluationRule() {
		super(OP_EQUAL);
	}
	
	public void setProductType(String productType) {
		setReferenceValue(productType);
	}
	
	protected Object getEvaluationValue(EligibilityCheckCriteria criteria) {
		return criteria.getProductType();
	}

	public String toString() {
		return "ProductType class reference value: " + getReferenceValue();
	}
}
