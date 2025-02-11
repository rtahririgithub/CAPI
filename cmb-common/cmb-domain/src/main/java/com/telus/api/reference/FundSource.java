/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.api.reference;

/**
 * @author Pavel Simonovsky
 *
 */
public interface FundSource {
	static final int FUND_SOURCE_BALANCE = 1;
	static final int FUND_SOURCE_CREDIT_CARD = 2;
	static final int FUND_SOURCE_BANK_CARD = 3;
	static final int FUND_SOURCE_NOT_DEFINED = -1;


	int getFundSourceType();
	
	String getDefaultIndicator();
	
}
