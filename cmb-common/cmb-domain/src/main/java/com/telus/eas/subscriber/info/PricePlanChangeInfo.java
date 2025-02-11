package com.telus.eas.subscriber.info;

public class PricePlanChangeInfo extends BaseServiceChangeInfo {
	FeatureChangeInfo[] featureChangeInfoList;
	String serviceType;
	
	
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
	
	
}
