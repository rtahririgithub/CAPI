package com.telus.eas.subscriber.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telus.api.account.ActivationOption;
import com.telus.api.account.ServicesValidation;
import com.telus.api.portability.PortInEligibility;
import com.telus.eas.account.info.ActivationTopUpPaymentArrangementInfo;
import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.message.info.ApplicationMessageInfo;

/**
 * @Author Brandon Wen, R. Fong
 */
public class ActivationChangeInfo extends BaseChangeInfo {

	private static final long serialVersionUID = -1264134451607913079L;

	private boolean dealerHasDeposit;
	private String activationFeeChargeCode;
	private boolean waiveSearchFee;
	private String memoText;
	private String primaryEquipmentSerialNumber;
	private String primaryEquipmentType;
	private String associatedHandsetSerialNumber;
	private String associatedHandsetType;
	private PortInEligibility portInEligibility;
	private ServicesValidation serviceValidation;
	private ActivationOption activationOption;
	private String subscriptionRoleCode;
	
	private ActivationTopUpPaymentArrangementInfo topUpPaymentArrangement;

	private SubscriberInfo newSubscriberInfo;
	// List of application level warning messages. Example of application level warnings is a list of warnings raised during equipment swap.
	private List<ApplicationMessageInfo> applicationMessageList = new ArrayList<ApplicationMessageInfo>();
	// List of sub-system exceptions that were ignored by business logic.
	private List<WarningFaultInfo> systemWarningList = new ArrayList<WarningFaultInfo>();
	
	public boolean isDealerHasDeposit() {
		return dealerHasDeposit;
	}

	public void setDealerHasDeposit(boolean dealerHasDeposit) {
		this.dealerHasDeposit = dealerHasDeposit;
	}

	public String getMemoText() {
		return memoText;
	}

	public void setMemoText(String memoText) {
		this.memoText = memoText;
	}

	public PortInEligibility getPortInEligibility() {
		return portInEligibility;
	}

	public void setPortInEligibility(PortInEligibility portInEligibility) {
		this.portInEligibility = portInEligibility;
	}

	public ServicesValidation getServiceValidation() {
		return serviceValidation;
	}

	public void setServiceValidation(ServicesValidation serviceValidation) {
		this.serviceValidation = serviceValidation;
	}

	public ActivationOption getActivationOption() {
		return activationOption;
	}

	public void setActivationOption(ActivationOption activationOption) {
		this.activationOption = activationOption;
	}

	public String getPrimaryEquipmentSerialNumber() {
		return primaryEquipmentSerialNumber;
	}

	public void setPrimaryEquipmentSerialNumber(String primaryEquipmentSerialNumber) {
		this.primaryEquipmentSerialNumber = primaryEquipmentSerialNumber;
	}

	public String getPrimaryEquipmentType() {
		return primaryEquipmentType;
	}

	public void setPrimaryEquipmentType(String primaryEquipmentType) {
		this.primaryEquipmentType = primaryEquipmentType;
	}

	public String getAssociatedHandsetSerialNumber() {
		return associatedHandsetSerialNumber;
	}

	public void setAssociatedHandsetSerialNumber(String associatedHandsetSerialNumber) {
		this.associatedHandsetSerialNumber = associatedHandsetSerialNumber;
	}

	public String getAssociatedHandsetType() {
		return associatedHandsetType;
	}

	public void setAssociatedHandsetType(String associatedHandsetType) {
		this.associatedHandsetType = associatedHandsetType;
	}

	public String getActivationFeeChargeCode() {
		return activationFeeChargeCode;
	}

	public void setActivationFeeChargeCode(String activationFeeChargeCode) {
		this.activationFeeChargeCode = activationFeeChargeCode;
	}

	public boolean isWaiveSearchFee() {
		return waiveSearchFee;
	}

	public void setWaiveSearchFee(boolean waiveSearchFee) {
		this.waiveSearchFee = waiveSearchFee;
	}

	public String getSubscriptionRoleCode() {
		return subscriptionRoleCode;
	}

	public void setSubscriptionRoleCode(String subscriptionRoleCode) {
		this.subscriptionRoleCode = subscriptionRoleCode;
	}

	public SubscriberInfo getNewSubscriberInfo() {
		return newSubscriberInfo;
	}

	public void setNewSubscriberInfo(SubscriberInfo newSubscriberInfo) {
		this.newSubscriberInfo = newSubscriberInfo;
	}

	public ActivationTopUpPaymentArrangementInfo getTopUpPaymentArrangement() {
		return topUpPaymentArrangement;
	}

	public void setTopUpPaymentArrangement(
			ActivationTopUpPaymentArrangementInfo topUpPaymentArrangement) {
		this.topUpPaymentArrangement = topUpPaymentArrangement;
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

}