package com.telus.eas.framework.eligibility.rules;

import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;

public class BrandTokenSetEvaluationRule extends TokenSetEvaluationRule {

	
	public void setBrandIds(String brandIds) {
		setTokens(brandIds);
	}

	protected String getTargetToken(EligibilityCheckCriteria criteria) {
		return Integer.toString( criteria.getBrandId() );
	}
	

	
}
