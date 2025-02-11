package com.telus.cmb.common.eligibility.rules;

import com.telus.cmb.common.eligibility.EsimDeviceSwapEligibilityCheckCriteria;
import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;
import com.telus.eas.framework.eligibility.rules.TokenSetEvaluationRule;

public class CurrentSimEvaluationRule extends TokenSetEvaluationRule {
	
	public void setSimType(String simType) {
		setTokens(simType);
	}
	
	protected String getTargetToken(EligibilityCheckCriteria criteria) {
		return ((EsimDeviceSwapEligibilityCheckCriteria )criteria).getSimType();
		//return null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Current SIM type reference value: " + tokens + "";
	}

}
