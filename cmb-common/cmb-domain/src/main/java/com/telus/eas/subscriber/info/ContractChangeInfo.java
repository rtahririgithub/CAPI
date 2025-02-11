package com.telus.eas.subscriber.info;

import java.util.List;

import com.telus.eas.account.info.PricePlanValidationInfo;

public class ContractChangeInfo extends BaseChangeInfo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7839947939076439986L;
	private SubscriberContractInfo newSubscriberContractInfo;
	private EquipmentChangeRequestInfo equipmentChangeRequestInfo;
	private List applicationWarningList;
	private List systemWarningList;
	private boolean pricePlanChangeInd = false;
	private boolean contractRenewalInd = false;
	private boolean activationInd = false;
	private int contractTerm;
	private ServiceChangeInfo[] serviceChangeInfoList;
	private PricePlanChangeInfo pricePlanChangeInfo;
	private CommitmentInfo newCommitmentInfo;
	private ServiceAgreementInfo newContractService;
	private String dealerCode;
	private String salesRepCode;
	private String[] multiRingPhoneNumberList;
	private PricePlanValidationInfo pricePlanValidatioInfo;
	private boolean migrationInd;

	public ContractChangeInfo() {
	}

	public SubscriberContractInfo getNewSubscriberContractInfo() {
		return newSubscriberContractInfo;
	}

	public void setNewSubscriberContractInfo(SubscriberContractInfo newSubscriberContractInfo) {
		this.newSubscriberContractInfo = newSubscriberContractInfo;
	}

	public EquipmentChangeRequestInfo getEquipmentChangeRequestInfo() {
		return equipmentChangeRequestInfo;
	}

	public void setEquipmentChangeRequestInfo(EquipmentChangeRequestInfo equipmentChangeRequestInfo) {
		this.equipmentChangeRequestInfo = equipmentChangeRequestInfo;
	}

	public List getApplicationWarningList() {
		return applicationWarningList;
	}

	public void setApplicationWarningList(List applicationWarningList) {
		this.applicationWarningList = applicationWarningList;
	}

	public List getSystemWarningList() {
		return systemWarningList;
	}

	public void setSystemWarningList(List systemWarningList) {
		this.systemWarningList = systemWarningList;
	}

	public boolean isPricePlanChangeInd() {
		return pricePlanChangeInd;
	}

	public void setPricePlanChangeInd(boolean pricePlanChangeInd) {
		this.pricePlanChangeInd = pricePlanChangeInd;
	}

	public boolean isContractRenewalInd() {
		return contractRenewalInd;
	}

	public void setContractRenewalInd(boolean contractRenewalInd) {
		this.contractRenewalInd = contractRenewalInd;
	}

	public boolean isActivationInd() {
		return activationInd;
	}

	public void setActivationInd(boolean activationInd) {
		this.activationInd = activationInd;
	}

	public int getContractTerm() {
		return contractTerm;
	}

	public void setContractTerm(int contractTerm) {
		this.contractTerm = contractTerm;
	}

	public ServiceChangeInfo[] getServiceChangeInfoList() {
		return serviceChangeInfoList;
	}

	public void setServiceChangeInfoList(ServiceChangeInfo[] serviceChangeInfoList) {
		this.serviceChangeInfoList = serviceChangeInfoList;
	}

	public PricePlanChangeInfo getPricePlanChangeInfo() {
		return pricePlanChangeInfo;
	}

	public void setPricePlanChangeInfo(PricePlanChangeInfo pricePlanChangeInfo) {
		this.pricePlanChangeInfo = pricePlanChangeInfo;
	}

	public ServiceAgreementInfo getNewContractService() {
		return newContractService;
	}

	public void setNewContractService(ServiceAgreementInfo newContractService) {
		this.newContractService = newContractService;
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

	/**
	 * @return the newCommitmentInfo
	 */
	public CommitmentInfo getNewCommitmentInfo() {
		return newCommitmentInfo;
	}

	/**
	 * @param newCommitmentInfo the newCommitmentInfo to set
	 */
	public void setNewCommitmentInfo(CommitmentInfo newCommitmentInfo) {
		this.newCommitmentInfo = newCommitmentInfo;
	}

	/**
	 * @return the multiRingPhoneNumberList
	 */
	public String[] getMultiRingPhoneNumberList() {
		return multiRingPhoneNumberList;
	}

	/**
	 * @param multiRingPhoneNumberList the multiRingPhoneNumberList to set
	 */
	public void setMultiRingPhoneNumberList(String[] multiRingPhoneNumberList) {
		this.multiRingPhoneNumberList = multiRingPhoneNumberList;
	}

	/**
	 * @return the priceplanValidatioInfo
	 */
	public PricePlanValidationInfo getPricePlanValidatioInfo() {
		return pricePlanValidatioInfo;
	}

	/**
	 * @param priceplanValidatioInfo the priceplanValidatioInfo to set
	 */
	public void setPricePlanValidatioInfo(PricePlanValidationInfo priceplanValidatioInfo) {
		this.pricePlanValidatioInfo = priceplanValidatioInfo;
	}

	public boolean isMigrationInd() {
		return migrationInd;
	}

	public void setMigrationInd(boolean migrationInd) {
		this.migrationInd = migrationInd;
	}

	
	

}
