package com.telus.cmb.common.eligibility.rules;

import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;
import com.telus.eas.framework.eligibility.rules.ReferenceComparisonEvaluationRule;

public class ProcessTypeEvaluationRule extends ReferenceComparisonEvaluationRule {

	public ProcessTypeEvaluationRule() {
		super(OP_EQUAL);
	}
	
	public void setProcessType(String processType) {
		setReferenceValue(processType);
	}
	
	@Override
	protected Object getEvaluationValue(EligibilityCheckCriteria criteria) {
		return criteria.getProcessType();
	}

	public String toString() {
		return "Process type class reference value: " + getReferenceValue();
	}
}
