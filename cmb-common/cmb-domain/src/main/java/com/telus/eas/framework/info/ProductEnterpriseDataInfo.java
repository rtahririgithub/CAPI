package com.telus.eas.framework.info;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public class ProductEnterpriseDataInfo extends Info {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4418608616954464641L;
	public static final String PROCESS_TYPE_SUBSCRIBER_CANCELLATION = "SubscriberCancellation";
	public static final String PROCESS_TYPE_SUBSCRIBER_SUSPENSION = "SubscriberSuspension";
	public static final String PROCESS_TYPE_SUBSCRIBER_RESTORE_SUSPENDED = "SubscriberRestoreSuspended";
	public static final String PROCESS_TYPE_SUBSCRIBER_RESTORE_CANCELLED = "SubscriberResumeCancelled";
	public static final String PROCESS_TYPE_SUBSCRIBER_RESOURCE_UPDATE = "SubscriberResourceUpdate";
	public static final String PROCESS_TYPE_SUBSCRIBER_ACTIVATION = "SubscriberActivation";
	public static final String PROCESS_TYPE_SUBSCRIBER_MIGRATION = "SubscriberMigration";
	public static final String PROCESS_TYPE_SUBSCRIBER_MOVE = "SubscriberMove";
	public static final String PROCESS_TYPE_SUBSCRIBER_UPDATE = "SubscriberUpdate";
	
	public static final String MESSAGE_TYPE_INSERT_PRODUCT_INSTANCE = "insertProductInstance";
	public static final String MESSAGE_TYPE_UPDATE_PRODUCT_INSTANCE = "updateProductInstance";
	public static final String MESSAGE_TYPE_MANAGE_PRODUCT_PARAMETERS = "manageProductParameters";
	public static final String MESSAGE_TYPE_MANAGE_PRODUCT_RESOURCES = "manageProductResources";
	
	
	private String messageType;
	private int billingAccountNumber;
	private String subscriberId;
	private String phoneNumber;
	private AccountInfo accountInfo = null;
	private SubscriberInfo subscriberInfo = null;
	private	EquipmentInfo equipmentInfo = null;
	private	SubscriberContractInfo subscriberContractInfo = null;
	private String processType;
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
	 * @return the subscriberId
	 */
	public String getSubscriberId() {
		return subscriberId;
	}
	/**
	 * @param subscriberId the subscriberId to set
	 */
	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public AccountInfo getAccountInfo() {
		return accountInfo;
	}
	public void setAccountInfo(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the subscriberInfo
	 */
	public SubscriberInfo getSubscriberInfo() {
		return subscriberInfo;
	}
	/**
	 * @param subscriberInfo the subscriberInfo to set
	 */
	public void setSubscriberInfo(SubscriberInfo subscriberInfo) {
		this.subscriberInfo = subscriberInfo;
	}
	/**
	 * @return the equipmentInfo
	 */
	public EquipmentInfo getEquipmentInfo() {
		return equipmentInfo;
	}
	/**
	 * @param equipmentInfo the equipmentInfo to set
	 */
	public void setEquipmentInfo(EquipmentInfo equipmentInfo) {
		this.equipmentInfo = equipmentInfo;
	}
	/**
	 * @return the subscriberContractInfo
	 */
	public SubscriberContractInfo getSubscriberContractInfo() {
		return subscriberContractInfo;
	}
	/**
	 * @param subscriberContractInfo the subscriberContractInfo to set
	 */
	public void setSubscriberContractInfo(SubscriberContractInfo subscriberContractInfo) {
		this.subscriberContractInfo = subscriberContractInfo;
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
	public String toString() {
		return "ProductEnterpriseDataInfo [messageType=" + messageType + ", billingAccountNumber=" + billingAccountNumber + ", subscriberId=" + subscriberId + ", phoneNumber=" + phoneNumber
				+ ", subscriberInfo=" + subscriberInfo + ", equipmentInfo=" + equipmentInfo + ", subscriberContractInfo=" + subscriberContractInfo + ", processType=" + processType + "]";
	}
	
	

}
