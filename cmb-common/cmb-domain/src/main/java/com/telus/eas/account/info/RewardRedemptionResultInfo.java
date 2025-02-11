/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.RewardRedemptionResult;
import com.telus.eas.framework.info.Info;

public class RewardRedemptionResultInfo extends Info implements RewardRedemptionResult {

	static final long serialVersionUID = 1L;

	private boolean isRewardEligible;
	private double rewardEligibilityAmount;
		
	public RewardRedemptionResultInfo(boolean isRewardEligible, double rewardEligibilityAmount) {
		super();
		this.isRewardEligible = isRewardEligible;
		this.rewardEligibilityAmount = rewardEligibilityAmount;
	}

	public boolean isRewardEligible() {
		return isRewardEligible;
	}
	
	public void setIsRewardEligible(boolean isRewardEligible) {
		this.isRewardEligible = isRewardEligible;
	}

	public double getRewardEligibilityAmount() {
		return rewardEligibilityAmount;
	}

	public void setRewardEligibilityAmount(double rewardEligibilityAmount) {
		this.rewardEligibilityAmount = rewardEligibilityAmount;
	}

	public void copyFrom(RewardRedemptionResultInfo info) {
		setIsRewardEligible(info.isRewardEligible());
		setRewardEligibilityAmount(info.getRewardEligibilityAmount());
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer();
		s.append("RewardRedemptionResultInfo:{\n");
		s.append("    isRewardEligible=[").append(isRewardEligible).append("]\n");
		s.append("    rewardEligibilityAmount=[").append(rewardEligibilityAmount).append("]\n");
		s.append("}");

		return s.toString();
	}

}




