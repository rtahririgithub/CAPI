package com.telus.cmb.common.eligibility.rules;

import com.telus.cmb.common.eligibility.EsimDeviceSwapEligibilityCheckCriteria;
import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;
import com.telus.eas.framework.eligibility.rules.TokenSetEvaluationRule;

public class NewDeviceEvaluationRule extends TokenSetEvaluationRule {
	
	public void setNewDeviceType(String newDeviceType) {
		setTokens(newDeviceType);
	}
	
	protected String getTargetToken(EligibilityCheckCriteria criteria) {
		return ((EsimDeviceSwapEligibilityCheckCriteria )criteria).getNewDeviceType();
	}

	public String toString() {
		return "NewDeviceType reference value: " + tokens + "";
	}
}
