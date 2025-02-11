/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.api.account;

/**
 * @author Pavel Simonovsky
 *
 */
public interface InternationalServiceEligibilityCheckResult {

	/**
	 * @return the depositAmount
	 */
	public abstract double getDepositAmount();

	/**
	 * @return the callingEligibility
	 */
	public abstract boolean isEligibleForInternationalDialing();

	/**
	 * @return the roamingEligibility
	 */
	public abstract boolean isEligibleForInternationalRoaming();

}