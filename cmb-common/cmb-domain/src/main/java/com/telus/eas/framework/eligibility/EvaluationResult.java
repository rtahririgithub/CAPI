package com.telus.eas.framework.eligibility;

import com.telus.eas.framework.eligibility.rules.EvaluationRule;

public interface EvaluationResult {

	public Object getEligibilityCheckResult();
	
	public EvaluationRule [] getEvaluationPath();

}
