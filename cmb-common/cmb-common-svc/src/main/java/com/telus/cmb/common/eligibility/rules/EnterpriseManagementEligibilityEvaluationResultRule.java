package com.telus.cmb.common.eligibility.rules;

import com.telus.eas.framework.eligibility.rules.EvaluationResultRule;

public class EnterpriseManagementEligibilityEvaluationResultRule extends EvaluationResultRule {
	Boolean enterpriseManagedData;
	
	public void setEnterpriseManagedData(Boolean enterpriseManagedData) {
		this.enterpriseManagedData = enterpriseManagedData;
	}
	

	@Override
	public Object getEligibilityCheckResult() {
		return enterpriseManagedData;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer("Result: ");
		buffer.append("enterpriseManagedData=[").append(enterpriseManagedData.toString()).append("]; ");
		return buffer.toString();
	}

}
