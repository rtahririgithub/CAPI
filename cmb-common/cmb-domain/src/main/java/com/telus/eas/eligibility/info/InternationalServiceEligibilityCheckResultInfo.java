/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.eas.eligibility.info;

import com.telus.api.account.InternationalServiceEligibilityCheckResult;
import com.telus.eas.framework.info.Info;

/**
 * @author Pavel Simonovsky
 *
 */
public class InternationalServiceEligibilityCheckResultInfo extends Info implements InternationalServiceEligibilityCheckResult {

	private static final long serialVersionUID = 1L;

	private double depositAmount = 0;
	
	private boolean eligibleForInternationalDialing = false;
	
	private boolean eligibleForInternationalRoaming = false;

	public InternationalServiceEligibilityCheckResultInfo() {
	}
	
	public InternationalServiceEligibilityCheckResultInfo(double depositAmount, boolean eligibleForInternationalDialing, boolean eligibleForInternationalRoaming) {
		this.eligibleForInternationalDialing = eligibleForInternationalDialing;
		this.depositAmount = depositAmount;
		this.eligibleForInternationalRoaming = eligibleForInternationalRoaming;
	}

	/* (non-Javadoc)
	 * @see com.telus.clientapi.domain.InternationalServiceEligibilityEvaluationResult#getDepositAmount()
	 */
	public double getDepositAmount() {
		return depositAmount;
	}

	/**
	 * @param depositAmount the depositAmount to set
	 */
	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
	}

	/**
	 * @return the eligibleForInternationalDialing
	 */
	public boolean isEligibleForInternationalDialing() {
		return eligibleForInternationalDialing;
	}

	/**
	 * @param eligibleForInternationalDialing the eligibleForInternationalDialing to set
	 */
	public void setEligibleForInternationalDialing(boolean eligibleForInternationalDialing) {
		this.eligibleForInternationalDialing = eligibleForInternationalDialing;
	}

	/**
	 * @return the eligibleForInternationalRoaming
	 */
	public boolean isEligibleForInternationalRoaming() {
		return eligibleForInternationalRoaming;
	}

	/**
	 * @param eligibleForInternationalRoaming the eligibleForInternationalRoaming to set
	 */
	public void setEligibleForInternationalRoaming(	boolean eligibleForInternationalRoaming) {
		this.eligibleForInternationalRoaming = eligibleForInternationalRoaming;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer("InternationalServiceEligibilityCheckResultInfo:[\n");

		buffer.append("    depositAmount = [").append(depositAmount).append("]\n");
		buffer.append("    eligibleForInternationalRoaming = [").append(eligibleForInternationalRoaming).append("]\n");
		buffer.append("    eligibleForInternationalDialing = [").append(eligibleForInternationalDialing).append("]\n");
		
		buffer.append("]");
		
		return buffer.toString();
	}
}
