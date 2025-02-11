package com.telus.eas.framework.eligibility.interservice.impl.rules;

import com.telus.eas.eligibility.info.InternationalServiceEligibilityCheckResultInfo;
import com.telus.eas.framework.eligibility.rules.EvaluationResultRule;

public class InternationalServiceEvaluationResultRule extends EvaluationResultRule {

	private InternationalServiceEligibilityCheckResultInfo evaluationResult = new InternationalServiceEligibilityCheckResultInfo();
	
	/**
	 * @param depositAmount the depositAmount to set
	 */
	public void setDepositAmount(Double depositAmount) {
		evaluationResult.setDepositAmount(depositAmount.doubleValue());
	}

	/**
	 * @param eligibleForInternationalDialing the eligibleForInternationalDialing to set
	 */
	public void setDialingEligibility(Boolean dialingEligibility) {
		evaluationResult.setEligibleForInternationalDialing(dialingEligibility.booleanValue());
	}

	/**
	 * @param eligibleForInternationalRoaming the eligibleForInternationalRoaming to set
	 */
	public void setRoamingEligibility(Boolean roamingEligibility) {
		evaluationResult.setEligibleForInternationalRoaming(roamingEligibility.booleanValue());
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.provider.eligibility.interservice.impl.rules.EvaluationResult#getInternationalServiceEligibilityCheckResult()
	 */
	public Object getEligibilityCheckResult() {
		return evaluationResult;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Result: depositAmount = [" + evaluationResult.getDepositAmount() + "], eligibleForInternationalRoaming = [" + evaluationResult.isEligibleForInternationalRoaming() + "], eligibleForInternationalDialing = [" + evaluationResult.isEligibleForInternationalDialing() + "]";
	}	

}
