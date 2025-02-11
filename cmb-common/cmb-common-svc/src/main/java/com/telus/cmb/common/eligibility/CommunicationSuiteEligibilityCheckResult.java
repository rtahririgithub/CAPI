package com.telus.cmb.common.eligibility;

import com.telus.eas.framework.eligibility.rules.EvaluationResultRule;

public class CommunicationSuiteEligibilityCheckResult extends EvaluationResultRule {

	Boolean communicationSuiteEligible;

	public void setCommunicationSuiteEligible(Boolean communicationSuiteEligible) {
		this.communicationSuiteEligible = communicationSuiteEligible;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer("Result: ");
		buffer.append("communicationSuiteEligible=[").append(communicationSuiteEligible.toString()).append("]; ");
		return buffer.toString();
	}

	@Override
	public Object getEligibilityCheckResult() {
		return communicationSuiteEligible;
	}
}
