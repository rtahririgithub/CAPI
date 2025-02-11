package com.telus.cmb.common.eligibility;

import com.telus.eas.framework.eligibility.rules.EvaluationResultRule;

public class EsimDeviceSwapEligibilityCheckResult extends EvaluationResultRule {

	private Boolean isEsimDeviceSwapEligible;
	private String errorCd;
	
	public void setDeviceSwapAllowed(Boolean isEsimDeviceSwapEligible) {
		this.isEsimDeviceSwapEligible = isEsimDeviceSwapEligible;
	}

	public void setErrorCd(String errorCd) {
		this.errorCd = errorCd;
	}
	
	@Override
	public Object getEligibilityCheckResult() {
		return isEsimDeviceSwapEligible;
	}
	
	public String getErrorCd() {
		return errorCd;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("EsimDeviceSwapEligibilityCheckResult: ");
		buffer.append("isEsimDeviceSwapEligible=[").append(isEsimDeviceSwapEligible.toString()).append("]; ");
		return buffer.toString();
	}
}
