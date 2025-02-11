/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.account.PaymentActivity;
import com.telus.eas.framework.info.Info;

public class PaymentActivityInfo extends Info implements PaymentActivity {

	 static final long serialVersionUID = 1L;
	
	private String activityCode;
	private String activityReasonCode;
	private Date date;
	private double amount;
	private Date billDate;
	private int fundTransferBanId;
	private boolean declined;
	private String exceptionReasonCode;
	private String knowbilityOperatorID;
	private boolean displayOnBill;
	
	private String creditCardAuthorizationCode;
	

    public PaymentActivityInfo() {
    }
    
	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getActivityReasonCode() {
		return activityReasonCode;
	}

	public void setActivityReasonCode(String activityReasonCode) {
		this.activityReasonCode = activityReasonCode;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isDeclined() {
		return declined;
	}

	public void setDeclined(boolean declined) {
		this.declined = declined;
	}

	public boolean displayOnBill() {
		return displayOnBill;
	}

	public void setDisplayOnBill(boolean displayOnBill) {
		this.displayOnBill = displayOnBill;
	}

	public String getExceptionReasonCode() {
		return exceptionReasonCode;
	}

	public void setExceptionReasonCode(String exceptionReasonCode) {
		this.exceptionReasonCode = exceptionReasonCode;
	}

	public int getFundTransferBanId() {
		return fundTransferBanId;
	}

	public void setFundTransferBanId(int fundTransferBanId) {
		this.fundTransferBanId = fundTransferBanId;
	}

	public String getKnowbilityOperatorID() {
		return knowbilityOperatorID;
	}

	public void setKnowbilityOperatorID(String knowbilityOperatorID) {
		this.knowbilityOperatorID = knowbilityOperatorID;
	}

	public String toString() {
        StringBuffer s = new StringBuffer(128);

        s.append("PaymentActivityInfo:[\n");
        s.append("    activityCode=[").append(activityCode).append("]\n");
        s.append("    activityReasonCode=[").append(activityReasonCode).append("]\n");
        s.append("    date=[").append(date).append("]\n");
        s.append("    amount=[").append(amount).append("]\n");
        s.append("    billDate=[").append(billDate).append("]\n");
        s.append("    fundTransferBanId=[").append(fundTransferBanId).append("]\n");
        s.append("    declined=[").append(declined).append("]\n");
        s.append("    exceptionReasonCode=[").append(exceptionReasonCode).append("]\n");
        s.append("    knowbilityOperatorID=[").append(knowbilityOperatorID).append("]\n");
        s.append("    displayOnBill=[").append(displayOnBill).append("]\n");
        s.append("]");

        return s.toString();
    }

	public void setCreditCardAuthorizationCode(String authorizationCode) {
		this.creditCardAuthorizationCode = authorizationCode;
	}

	public String getCreditCardAuthorizationCode() {
		return creditCardAuthorizationCode;
	}
}



