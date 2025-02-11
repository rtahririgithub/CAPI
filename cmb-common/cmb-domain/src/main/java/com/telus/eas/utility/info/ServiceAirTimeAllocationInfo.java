package com.telus.eas.utility.info;

import java.util.Date;

import com.telus.api.reference.FeatureAirTimeAllocation;
import com.telus.api.reference.ServiceAirTimeAllocation;
import com.telus.eas.framework.info.Info;

public class ServiceAirTimeAllocationInfo extends Info implements ServiceAirTimeAllocation {

	private static final long serialVersionUID = 1L;

	private String serviceCode;
	private String description;
	private String descriptionFrench;
	private String marketingDescription;
	private String marketingDescriptionFrench;
	private String productType;
	private String serviceType;
	private String billCycleTreatmentCode;
	private String coverageType;
	private Date effectiveDate;
	private Date expirationDate;
	private FeatureAirTimeAllocationInfo[] featureAirTimeAllocations;
	private boolean containPeriodBasedFeature;
	private transient ServiceAirTimeAllocationInfo followEffectiveSoc;
	private boolean sequentiallyBoundServiceAttached;
	private boolean isValidSOC = true;
	private String errorMessage;
	private String errorCode;
	private String locale;
	private Date timeStamp;

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean isValidSOC() {
		return isValidSOC;
	}

	public void setValidSOC(boolean isValidSOC) {
		this.isValidSOC = isValidSOC;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	// this attribute is used to tell the relationship of two SOC, solely for
	// testing purpose.
	private String leadingServiceCode;

	public String getDescription() {
		return description;
	}

	public String getDescriptionFrench() {
		return descriptionFrench;
	}

	public String getMarketingDescription() {
		return marketingDescription;
	}

	public void setMarketingDescription(String marketingDescription) {
		this.marketingDescription = marketingDescription;
	}

	public String getMarketingDescriptionFrench() {
		return marketingDescriptionFrench;
	}

	public void setMarketingDescriptionFrench(String marketingDescriptionFrench) {
		this.marketingDescriptionFrench = marketingDescriptionFrench;
	}

	public String getCode() {
		return serviceCode;
	}

	public String getProductType() {
		return productType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public String getBillCycleTreatmentCode() {
		return billCycleTreatmentCode;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public Date getExpriationDate() {
		return expirationDate;
	}

	public FeatureAirTimeAllocation[] getFeatureAirTimeAllocations() {
		return getFeatureAirTimeAllocations0();
	}

	public FeatureAirTimeAllocationInfo[] getFeatureAirTimeAllocations0() {
		return featureAirTimeAllocations;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public void setBillCycleTreatmentCode(String billCycleTreatmentCode) {
		this.billCycleTreatmentCode = billCycleTreatmentCode;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public void setFeatureAirTimeAllocations(FeatureAirTimeAllocationInfo[] featureAirTimeAllocations) {
		this.featureAirTimeAllocations = featureAirTimeAllocations;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getCoverageType() {
		return coverageType;
	}

	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}

	public boolean isContainPeriodBasedFeature() {
		return containPeriodBasedFeature;
	}

	public void setContainPeriodBasedFeature(boolean containPeriodBasedFeature) {
		this.containPeriodBasedFeature = containPeriodBasedFeature;
	}

	public ServiceAirTimeAllocationInfo getFollowEffectiveSoc() {
		return followEffectiveSoc;
	}

	public void setFollowEffectiveSoc(ServiceAirTimeAllocationInfo followEffectiveSoc) {
		this.followEffectiveSoc = followEffectiveSoc;
	}

	public boolean hasSequentiallyBoundService() {
		return sequentiallyBoundServiceAttached;
	}

	public void setSequentiallyBoundServiceAttached(boolean sequentiallyBoundServiceAttached) {
		this.sequentiallyBoundServiceAttached = sequentiallyBoundServiceAttached;
	}

	public String getLeadingServiceCode() {
		return leadingServiceCode;
	}

	public String setLeadingServiceCode(String code) {
		return leadingServiceCode = code;
	}
}
