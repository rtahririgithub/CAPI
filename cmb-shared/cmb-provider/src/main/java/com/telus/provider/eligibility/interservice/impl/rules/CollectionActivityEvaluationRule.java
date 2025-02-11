package com.telus.provider.eligibility.interservice.impl.rules;

import com.telus.eas.framework.eligibility.EligibilityCheckCriteria;
import com.telus.eas.framework.eligibility.rules.ReferenceComparisonEvaluationRule;
import com.telus.provider.eligibility.interservice.InternationalServiceEligibilityCheckCriteria;

public class CollectionActivityEvaluationRule extends ReferenceComparisonEvaluationRule {
	
	public CollectionActivityEvaluationRule() {
		super(OP_EQUAL);
	}
	
	public void setActivityPresent(Boolean activityPresent) {
		setReferenceValue(activityPresent);
	}

	/* (non-Javadoc)
	 * @see com.telus.provider.eligibility.interservice.impl.rules.ReferenceValueEvaluationRule#getEvaluationValue(com.telus.provider.eligibility.interservice.InternationalServiceEligibilityCheckCriteria)
	 */
	protected Object getEvaluationValue(EligibilityCheckCriteria criteria) {
		return new Boolean(((InternationalServiceEligibilityCheckCriteria)criteria).isCollectionActivityPresent());
	}

}
