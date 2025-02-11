package com.telus.eas.framework.eligibility;

import com.telus.eas.framework.eligibility.rules.EvaluationRule;

public interface EvaluationRuleFactory {
	public EvaluationRule createEvaluationRule();

}
