/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

/**
 * <CODE>RewardRedemptionResult</CODE>
 *
 */
public interface RewardRedemptionResult {
	
	boolean isRewardEligible();
	
	double getRewardEligibilityAmount();

}


