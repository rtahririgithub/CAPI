package com.telus.eas.framework.info;

import com.telus.eas.account.info.AccountInfo;

public class BillingAccountEnterpriseDataInfo extends Info {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9218131191915054064L;
	private String messageType;
	private String dataSyncMethod;
	private int billingAccountNumber;
	private String customerID;
	private short billCycle;
	private short nextBillCycle;
	private String status;
	private String processType;
	private AccountInfo accountInfo = null;
	
	public static final String PROCESS_TYPE_ACCCOUNT_ACTIVATION = "AccountActivation";
	public static final String PROCESS_TYPE_ACCOUNT_UPDATE = "AccountUpdate";
	public static final String PROCESS_TYPE_ACCOUNT_CANCELLATION = "AccountCancellation";
	public static final String PROCESS_TYPE_ACCOUNT_SUSPENSION = "AccountSuspension";
	
	public static final String MESSAGE_TYPE_INSERT_BILLING_ACCOUNT = "insertBillingAccount";
	public static final String MESSAGE_TYPE_UPDATE_BILLING_ACCOUNT = "updateBillingAccount";
	public static final String MESSAGE_TYPE_INSERT_CUSTOMER_WITH_BILLING_ACCOUNT = "insertCustomerWithBillingAccount";
	public static final String MESSAGE_TYPE_UPDATE_PAY_CHANNEL = "updatePayChannel";
	public static final String MESSAGE_TYPE_UPDATE_BILLING_ACCOUNT_STATUS = "updateBillingAccountStatus";
	public static final String MESSAGE_TYPE_UPDATE_BILL_CYCLE = "updateBillCycle";
	
	/**
	 * @return the messageType
	 */
	public String getMessageType() {
		return messageType;
	}
	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	/**
	 * @return the billingAccountNumber
	 */
	public int getBillingAccountNumber() {
		return billingAccountNumber;
	}
	/**
	 * @param billingAccountNumber the billingAccountNumber to set
	 */
	public void setBillingAccountNumber(int billingAccountNumber) {
		this.billingAccountNumber = billingAccountNumber;
	}
	/**
	 * @return the customerID
	 */
	public String getCustomerID() {
		return customerID;
	}
	/**
	 * @param customerID the customerID to set
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	/**
	 * @return the billCycle
	 */
	public short getBillCycle() {
		return billCycle;
	}
	/**
	 * @param billCycle the billCycle to set
	 */
	public void setBillCycle(short billCycle) {
		this.billCycle = billCycle;
	}
	/**
	 * @return the nextBillCycle
	 */
	public short getNextBillCycle() {
		return nextBillCycle;
	}
	/**
	 * @param nextBillCycle the nextBillCycle to set
	 */
	public void setNextBillCycle(short nextBillCycle) {
		this.nextBillCycle = nextBillCycle;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the processType
	 */
	public String getProcessType() {
		return processType;
	}
	/**
	 * @param processType the processType to set
	 */
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	
	/**
	 * @return the accountInfo
	 */
	public AccountInfo getAccountInfo() {
		return accountInfo;
	}
	/**
	 * @param accountInfo the accountInfo to set
	 */
	public void setAccountInfo(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "BillingAccountEnterpriseDataInfo [messageType=" + messageType + ", dataSyncMethod=" + dataSyncMethod + ", billingAccountNumber=" + billingAccountNumber + ", customerID=" + customerID
				+ ", billCycle=" + billCycle + ", nextBillCycle=" + nextBillCycle + ", status=" + status + ", processType=" + processType + "]";
	}
	
	

}
