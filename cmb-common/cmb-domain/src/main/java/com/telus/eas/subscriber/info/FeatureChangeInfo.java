package com.telus.eas.subscriber.info;


public class FeatureChangeInfo extends BaseServiceChangeInfo {
	String featureParameter;
	String[] callingCirclePhoneNumberList;
	
	ServiceFeatureInfo newServiceFeatureInfo;

	public String getFeatureParameter() {
		return featureParameter;
	}

	public void setFeatureParameter(String featureParameter) {
		this.featureParameter = featureParameter;
	}

	/**
	 * @return the newServiceFeatureInfo
	 */
	public ServiceFeatureInfo getNewServiceFeatureInfo() {
		return newServiceFeatureInfo;
	}

	/**
	 * @param newServiceFeatureInfo the newServiceFeatureInfo to set
	 */
	public void setNewServiceFeatureInfo(ServiceFeatureInfo newServiceFeatureInfo) {
		this.newServiceFeatureInfo = newServiceFeatureInfo;
	}

	public String[] getCallingCirclePhoneNumberList() {
		return callingCirclePhoneNumberList;
	}

	public void setCallingCirclePhoneNumberList(
			String[] callingCirclePhoneNumberList) {
		this.callingCirclePhoneNumberList = callingCirclePhoneNumberList;
	}
	
	
	
}
