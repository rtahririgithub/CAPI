package com.telus.eas.subscriber.info;

import com.telus.api.account.DurationServiceCommitmentAttributeData;


public class ServiceChangeInfo extends BaseServiceChangeInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -578246806127466735L;
	
	private ServiceAgreementInfo newServiceAgreementInfo;
	private FeatureChangeInfo[] featureChangeInfoList;
	private String serviceType;
	private PrepaidServicePropertyInfo prepaidServicePropertyInfo;
	private DurationServiceCommitmentAttributeData durationCommitmentData;
	private boolean durationService;

	public ServiceAgreementInfo getNewServiceAgreementInfo() {
		return newServiceAgreementInfo;
	}
	public void setNewServiceAgreementInfo(ServiceAgreementInfo newServiceAgreementInfo) {
		this.newServiceAgreementInfo = newServiceAgreementInfo;
	}
	public FeatureChangeInfo[] getFeatureChangeInfoList() {
		return featureChangeInfoList;
	}
	public void setFeatureChangeInfoList(FeatureChangeInfo[] featureChangeInfoList) {
		this.featureChangeInfoList = featureChangeInfoList;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public PrepaidServicePropertyInfo getPrepaidServicePropertyInfo() {
		return prepaidServicePropertyInfo;
	}
	public void setPrepaidServicePropertyInfo(PrepaidServicePropertyInfo prepaidServicePropertyInfo) {
		this.prepaidServicePropertyInfo = prepaidServicePropertyInfo;
	}
	public DurationServiceCommitmentAttributeData getDurationCommitmentData() {
		return durationCommitmentData;
	}
	public void setDurationCommitmentData(
			DurationServiceCommitmentAttributeData durationCommitmentData) {
		this.durationCommitmentData = durationCommitmentData;
	}
	public boolean isDurationService() {
		return durationService;
	}
	public void setDurationService(boolean durationService) {
		this.durationService = durationService;
	}
	
	
}
