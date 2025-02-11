package com.telus.eas.subscriber.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.ActivationOptionInfo;
import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.message.info.ApplicationMessageInfo;

/**
 * @Author Brandon Wen
 */

public class MigrationChangeInfo extends BaseChangeInfo {

	private static final long serialVersionUID = 4991688023441589915L;

	private int newBan;
	private String newEquipmentSerialNumber;
	private String newAssociatedHandsetSerialNumber;
	private String dealerCode;
	private String salesRepCode;
	private String requestorId;
	private String applicationId;
	private String pricePlanCode;
	private String migrationTypeCode;
	private String memoText;
	private AccountInfo newAccountInfo;
	private SubscriberInfo newSubscriberInfo;
	private SubscriberContractInfo newSubscriberContractInfo;
	private ActivationOptionInfo activationOptionInfo;

	// List of application level warning messages.  Example of application level warnings is a list of warnings raised during equipment swap.
	private List<ApplicationMessageInfo> applicationMessageList = new ArrayList<ApplicationMessageInfo>(); 
	
	// List of sub-system exceptions that were ignored by business logic.
	private List<WarningFaultInfo> systemWarningList = new ArrayList<WarningFaultInfo>(); 

	public int getNewBan() {
		return newBan;
	}

	public void setNewBan(int newBan) {
		this.newBan = newBan;
	}

	public String getNewEquipmentSerialNumber() {
		return newEquipmentSerialNumber;
	}

	public void setNewEquipmentSerialNumber(String newEquipmentSerialNumber) {
		this.newEquipmentSerialNumber = newEquipmentSerialNumber;
	}

	public String getNewAssociatedHandsetSerialNumber() {
		return newAssociatedHandsetSerialNumber;
	}

	public void setNewAssociatedHandsetSerialNumber(String newAssociatedHandsetSerialNumber) {
		this.newAssociatedHandsetSerialNumber = newAssociatedHandsetSerialNumber;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getSalesRepCode() {
		return salesRepCode;
	}

	public void setSalesRepCode(String salesRepCode) {
		this.salesRepCode = salesRepCode;
	}

	public String getRequestorId() {
		return requestorId;
	}

	public void setRequestorId(String requestorId) {
		this.requestorId = requestorId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getPricePlanCode() {
		return pricePlanCode;
	}

	public void setPricePlanCode(String pricePlanCode) {
		this.pricePlanCode = pricePlanCode;
	}

	public String getMigrationTypeCode() {
		return migrationTypeCode;
	}

	public void setMigrationTypeCode(String migrationTypeCode) {
		this.migrationTypeCode = migrationTypeCode;
	}

	public String getMemoText() {
		return memoText;
	}

	public void setMemoText(String memoText) {
		this.memoText = memoText;
	}

	public AccountInfo getNewAccountInfo() {
		return newAccountInfo;
	}

	public void setNewAccountInfo(AccountInfo newAccountInfo) {
		this.newAccountInfo = newAccountInfo;
	}

	public SubscriberInfo getNewSubscriberInfo() {
		return newSubscriberInfo;
	}

	public void setNewSubscriberInfo(SubscriberInfo newSubscriberInfo) {
		this.newSubscriberInfo = newSubscriberInfo;
	}

	public SubscriberContractInfo getNewSubscriberContractInfo() {
		return newSubscriberContractInfo;
	}

	public void setNewSubscriberContractInfo(SubscriberContractInfo newSubscriberContractInfo) {
		this.newSubscriberContractInfo = newSubscriberContractInfo;
	}

	public List<ApplicationMessageInfo> getApplicationMessageList() {
		return applicationMessageList;
	}

	public void setApplicationMessageList(List<ApplicationMessageInfo> applicationMessageList) {
		this.applicationMessageList = applicationMessageList;
	}

	public void addApplicationMessage(ApplicationMessageInfo applicationMessage) {
		if (applicationMessage != null) {
			applicationMessageList.add(applicationMessage);
		}
	}

	public void addApplicationMessageList(ApplicationMessageInfo[] applicationMessages) {
		this.applicationMessageList.addAll(Arrays.asList(applicationMessages));
	}

	public List<WarningFaultInfo> getSystemWarningList() {
		return systemWarningList;
	}

	public void setSystemWarningList(List<WarningFaultInfo> systemWarningList) {
		this.systemWarningList = systemWarningList;
	}

	public void addSystemWarning(WarningFaultInfo systemWarning) {
		if (systemWarning != null) {
			systemWarningList.add(systemWarning);
		}
	}

	public void addSystemWarningList(WarningFaultInfo[] systemWarnings) {
		this.systemWarningList.addAll(Arrays.asList(systemWarnings));
	}

	public ActivationOptionInfo getActivationOptionInfo() {
		return activationOptionInfo;
	}

	public void setActivationOptionInfo(ActivationOptionInfo activationOptionInfo) {
		this.activationOptionInfo = activationOptionInfo;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("BaseChangeInfo[\n");
		sb.append("\tnewBan=[").append(newBan).append("]\n");
		sb.append("\tapplicationId=[").append(applicationId).append("]\n");
		sb.append("\tmigrationTypeCode=[").append(migrationTypeCode).append("]\n");
		sb.append("\tpricePlanCode=[").append(pricePlanCode).append("]\n");
		sb.append("\trequestorId=[").append(requestorId).append("]\n");
		sb.append("\tnewEquipmentSerialNumber=[").append(newEquipmentSerialNumber).append("]\n");
		sb.append("\tnewAssociatedHandsetSerialNumber=[").append(newAssociatedHandsetSerialNumber).append("]\n");
		sb.append("]");
		return sb.toString();
	}
}
