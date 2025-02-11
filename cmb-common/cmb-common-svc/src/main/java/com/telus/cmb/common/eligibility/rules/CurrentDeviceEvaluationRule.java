package com.telus.cmb.common.eligibility.rules;

import com.telus.cmb.common.eligibility.EsimDeviceSwapEligibilityCheckCriteria;
import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;
import com.telus.eas.framework.eligibility.rules.TokenSetEvaluationRule;

public class CurrentDeviceEvaluationRule extends TokenSetEvaluationRule {
	
	public void setCurrentDeviceType(String currentDeviceType) {
		setTokens(currentDeviceType);
	}
	
	protected String getTargetToken(EligibilityCheckCriteria criteria) {
		return ((EsimDeviceSwapEligibilityCheckCriteria )criteria).getCurrentDeviceType();
	}

	public String toString() {
		return "CurrentDeviceType reference value: " + tokens + "";
	}
}
