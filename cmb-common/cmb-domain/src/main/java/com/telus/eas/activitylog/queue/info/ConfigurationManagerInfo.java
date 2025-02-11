package com.telus.eas.activitylog.queue.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;



public abstract class ConfigurationManagerInfo extends Info {

	private static final long serialVersionUID = 1L;
	
	public static final String MESSAGE_TYPE_ACCOUNT_STATUS_CHANGE   = "accountStatusChange";
    public static final String MESSAGE_TYPE_ADDRESS_CHANGE          = "addressChange";
    public static final String MESSAGE_TYPE_BILL_PAYMENT            = "billPayment";
    public static final String MESSAGE_TYPE_EQUIPMENT_CHANGE        = "equipmentChange";
    public static final String MESSAGE_TYPE_PAYMENT_METHOD_CHANGE   = "paymentMethodChange";
    public static final String MESSAGE_TYPE_PHONE_NUMBER_CHANGE     = "phoneNumberChange";
    public static final String MESSAGE_TYPE_PREPAID_TOPUP           = "prepaidTopup";
    public static final String MESSAGE_TYPE_PRICE_PLAN_CHANGE       = "priceplanChange";
    public static final String MESSAGE_TYPE_SERVICE_CHANGE          = "serviceChange";
    public static final String MESSAGE_TYPE_SUBSCRIBER_CHANGE       = "subscriberChange";
    public static final String MESSAGE_TYPE_SUBSCRIBER_CHARGE       = "subscriberCharge";
    public static final String MESSAGE_TYPE_ROLE_CHANGE             = "roleChange";

	private long transactionId;
	
	private Date transactionDate;
	
	public abstract String getMessageType() ;

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

   
	
}