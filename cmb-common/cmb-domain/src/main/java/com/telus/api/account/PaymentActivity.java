/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Date;

public interface PaymentActivity {
	
	public static final String ACTIVITY_PAYMENT = "PYM";
	public static final String ACTIVITY_REFUND = "RFN";
	public static final String ACTIVITY_TRANSFER_OUT = "FNTF";
	public static final String ACTIVITY_TRANSFER_IN = "FNTT";
	public static final String ACTIVITY_PAYMENT_BACKOUT= "BCK";
	public static final String ACTIVITY_REFUND_BACKOUT= "RFNR";
	
	public static final String REFUND_REASON_CREDIT_CARD_REFUND="CRDR";
	
	String getCreditCardAuthorizationCode();
	
	String getActivityCode();
	
	String getActivityReasonCode();
	
	Date getDate();
	
	double getAmount();
	
	Date getBillDate();
	
	int getFundTransferBanId();
	
	boolean isDeclined();
	
	String getExceptionReasonCode();
	
	String getKnowbilityOperatorID();
	
	boolean displayOnBill();
	
}
