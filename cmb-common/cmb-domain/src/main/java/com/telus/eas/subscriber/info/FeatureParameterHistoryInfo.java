package com.telus.eas.subscriber.info;

import java.util.Date;

import com.telus.api.account.FeatureParameterHistory;
import com.telus.eas.framework.info.Info;

public class FeatureParameterHistoryInfo extends Info implements FeatureParameterHistory {
	private static final long serialVersionUID = 1L;
	private String serviceCode;
	private String featureCode;
	private String parameterName;
	private String parameterValue;
	private Date effectiveDate;
	private Date expirationDate;
	private String knowbilityOperatorID;
	private String applicationID;
	private Date creationDate;
	private Date updateDate;
	private String serviceSequenceNo;
	private String serviceVersionNo;
	private String featureSequenceNo;
	
	public String getApplicationID() {
		return applicationID;
	}
	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getFeatureCode() {
		return featureCode;
	}
	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}
	public String getKnowbilityOperatorID() {
		return knowbilityOperatorID;
	}
	public void setKnowbilityOperatorID(String knowbilityOperatorID) {
		this.knowbilityOperatorID = knowbilityOperatorID;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getParameterValue() {
		return parameterValue;
	}
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String socCode) {
		this.serviceCode = socCode;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public String getServiceSequenceNo() {
		return serviceSequenceNo;
	}
	public void setServiceSequenceNo(String serviceSequenceNo) {
		this.serviceSequenceNo = serviceSequenceNo;
	}
	public String getServiceVersionNo() {
		return serviceVersionNo;
	}
	public void setServiceVersionNo(String serviceVersionNo) {
		this.serviceVersionNo = serviceVersionNo;
	}
	public String getFeatureSequenceNo() {
		return featureSequenceNo;
	}
	public void setFeatureSequenceNo(String featureSequenceNo) {
		this.featureSequenceNo = featureSequenceNo;
	}
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("FeatureParameterHistoryInfo [serviceCode=")
				.append(serviceCode).append(", featureCode=")
				.append(featureCode).append(", serviceVersionNo=")
				.append(serviceVersionNo).append(", serviceSequenceNo=")
				.append(serviceSequenceNo).append(", featureSequenceNo=")
				.append(featureSequenceNo).append(", parameterValue=")
				.append(parameterValue).append(", effectiveDate=")
				.append(effectiveDate).append(", expirationDate=")
				.append(expirationDate).append(", creationDate=")
				.append(creationDate).append(", updateDate=")
				.append(updateDate).append("]");
		return buffer.toString();
	}
	
}
