package com.telus.cmb.common.eligibility.rules;

import com.telus.eas.framework.eligibility.rules.EvaluationResultRule;

public class CallingCircleEligibilityEvaluationResult extends EvaluationResultRule {
	
	private boolean commitmentRulesInd = false;
	private boolean carryOverInd = false;
	private int maxAllowedChangesPerPeriod = 1;
	private int carryOverPeriod = 30;

	public boolean isCommitmentRulesInd() {
		return commitmentRulesInd;
	}

	public void setCommitmentRulesInd(Boolean commitmentRulesInd) {
		this.commitmentRulesInd = commitmentRulesInd;
	}

	public boolean isCarryOverInd() {
		return carryOverInd;
	}

	public void setCarryOverInd(Boolean carryOverInd) {
		this.carryOverInd = carryOverInd;
	}

	public int getMaxAllowedChangesPerPeriod() {
		return maxAllowedChangesPerPeriod;
	}

	public void setMaxAllowedChangesPerPeriod(Integer maxAllowedChangesPerPeriod) {
		this.maxAllowedChangesPerPeriod = maxAllowedChangesPerPeriod;
	}

	public int getCarryOverPeriod() {
		return carryOverPeriod;
	}

	public void setCarryOverPeriod(Integer carryOverPeriod) {
		this.carryOverPeriod = carryOverPeriod;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer("CallingCircleEligibilityEvaluationResult:[\n");

		buffer.append("    commitmentRulesInd = [").append(commitmentRulesInd).append("]\n");
		buffer.append("    carryOverInd = [").append(carryOverInd).append("]\n");
		buffer.append("    maxAllowedChangesPerPeriod = [").append(maxAllowedChangesPerPeriod).append("]\n");
		buffer.append("    carryOverPeriod = [").append(carryOverPeriod).append("]\n");
		buffer.append("]");

		return buffer.toString();

	}

	@Override
	public Object getEligibilityCheckResult() {
		return this;
	}
}
