package com.telus.eas.framework.eligibility;

public interface EligibilityCheckStrategy {
	public Object evaluate(EligibilityCheckCriteria criteria);
}
