package com.telus.eas.framework.eligibility.rules;

import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;
import com.telus.eas.framework.eligibility.EvaluationResult;

public abstract class ConditionalEvaluationRule extends EvaluationRule {
	
	protected abstract boolean matchCondition(EligibilityCheckCriteria criteria);

	/*
	 * (non-Javadoc)
	 * @see com.telus.provider.eligibility.interservice.impl.rules.EvaluationRule#evaluate(com.telus.provider.eligibility.interservice.InternationalServiceEligibilityCheckCriteria)
	 */
	public EvaluationResult evaluate(EligibilityCheckCriteria criteria) {
		return matchCondition(criteria) ? super.evaluate(criteria) : null;
	}
	
}
