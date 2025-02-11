package com.telus.eas.utility.info;

import com.telus.api.reference.FeatureAirTimeAllocation;
import com.telus.api.reference.ServiceFeatureClassification;

public class FeatureAirTimeAllocationInfo implements FeatureAirTimeAllocation {

	private static final long serialVersionUID = 1L;
	private String code;
	private String description;
	private String descriptionFrench;
	private ServiceFeatureClassificationInfo classification = new ServiceFeatureClassificationInfo();
	private String featureGroup;
	private boolean isPeriodBased;
	private String inclusiveQuantityType;
	
	private ServicePeriodInfo periodInfo = new ServicePeriodInfo();
	
	private double fromQuantity;
	private double toQuantity;
	private String usageChargeDependCode;
	private int tierLevelCode;
	private boolean pooling;
	private boolean sharing;
	private boolean minimumCommitmentRequired;
	private String callingDirection;
	private String parameterType;
	private String parameterName;
	private String parameterValue;
	private String freeMinuteType;
	private String callingRouteType;
	private double freeInlcudedMinutes;
	private boolean isUsageCharge;
	private double usageCharge;
	private boolean isRecurringCharge;
	private double recurringCharge;
	private boolean isOneTimeCharge;
	private double oneTimeCharge;
	private boolean switchActionRequired;
	private String switchCode;
	private String switchParameter;
	private boolean parameterRequired;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescriptionFrench() {
		return descriptionFrench;
	}
	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}
	public ServiceFeatureClassification getClassification() {
		return classification;
	}
	public ServiceFeatureClassificationInfo getClassification0() {
		return classification;
	}
	public void setClassification(ServiceFeatureClassificationInfo classification) {
		if ( classification!=null) {
			this.classification = classification;
		}
	}
	public String getFeatureGroup() {
		return featureGroup;
	}
	public void setFeatureGroup(String featureGroup) {
		this.featureGroup = featureGroup;
	}
	public String getInclusiveQuantityType() {
		return inclusiveQuantityType;
	}
	public void setInclusiveQuantityType(String inclusiveQuantityType) {
		this.inclusiveQuantityType = inclusiveQuantityType;
	}
	public String getPeriodName() {
		return periodInfo.getPeriodName();
	}
	public void setPeriodName(String periodName) {
		this.periodInfo.setPeriodName(periodName);
	}
	public String getPeriodValueCode() {
		return periodInfo.getCode();
	}
	public void setPeriodValueCode(String periodValueCode) {
		this.periodInfo.setCode(periodValueCode);
	}
	public void setPeriod(ServicePeriodInfo period) {
		this.periodInfo=period;
	}
	public ServicePeriodInfo getPeriod() {
		return periodInfo;
	}
	public String getUsageChargeDependCode() {
		return usageChargeDependCode;
	}
	public void setUsageChargeDependCode(String usageChargeDependCode) {
		this.usageChargeDependCode = usageChargeDependCode;
	}
	public int getTierLevelCode() {
		return tierLevelCode;
	}
	public void setTierLevelCode(int tierLevelCode) {
		this.tierLevelCode = tierLevelCode;
	}
	public boolean isPooling() {
		return pooling;
	}
	public void setPooling(boolean pooling) {
		this.pooling = pooling;
	}
	public boolean isSharing() {
		return sharing;
	}
	public void setSharing(boolean sharing) {
		this.sharing = sharing;
	}
	public boolean isMinimumCommitmentRequired() {
		return minimumCommitmentRequired;
	}
	public void setMinimumCommitmentRequired(boolean minimumCommitmentRequired) {
		this.minimumCommitmentRequired = minimumCommitmentRequired;
	}
	public String getCallingDirection() {
		return callingDirection;
	}
	public void setCallingDirection(String callingDirection) {
		this.callingDirection = callingDirection;
	}
	public String getParameterType() {
		return parameterType;
	}
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
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
	public String getFreeMinuteType() {
		return freeMinuteType;
	}
	public void setFreeMinuteType(String freeMinuteType) {
		this.freeMinuteType = freeMinuteType;
	}
	public String getCallingRouteType() {
		return callingRouteType;
	}
	public void setCallingRouteType(String callingRouteType) {
		this.callingRouteType = callingRouteType;
	}
	public double getFreeInlcudedMinutes() {
		return freeInlcudedMinutes;
	}
	public void setFreeInlcudedMinutes(double freeInlcudedMinutes) {
		this.freeInlcudedMinutes = freeInlcudedMinutes;
	}
	public boolean isUsageCharge() {
		return isUsageCharge;
	}
	public void setUsageCharge(boolean isUsageCharge) {
		this.isUsageCharge = isUsageCharge;
	}
	public double getUsageCharge() {
		return usageCharge;
	}
	public void setUsageCharge(double usageCharge) {
		this.usageCharge = usageCharge;
	}
	public boolean isRecurringCharge() {
		return isRecurringCharge;
	}
	public void setRecurringCharge(boolean isRecurringCharge) {
		this.isRecurringCharge = isRecurringCharge;
	}
	public double getRecurringCharge() {
		return recurringCharge;
	}
	public void setRecurringCharge(double recurringCharge) {
		this.recurringCharge = recurringCharge;
	}
	public boolean isOneTimeCharge() {
		return isOneTimeCharge;
	}
	public void setOneTimeCharge(boolean isOneTimeCharge) {
		this.isOneTimeCharge = isOneTimeCharge;
	}
	public double getOneTimeCharge() {
		return oneTimeCharge;
	}
	public void setOneTimeCharge(double oneTimeCharge) {
		this.oneTimeCharge = oneTimeCharge;
	}
	public boolean isSwitchActionRequired() {
		return switchActionRequired;
	}
	public void setSwitchActionRequired(boolean switchActionRequired) {
		this.switchActionRequired = switchActionRequired;
	}
	public String getSwitchCode() {
		return switchCode;
	}
	public void setSwitchCode(String switchCode) {
		this.switchCode = switchCode;
	}
	public String getSwitchParameter() {
		return switchParameter;
	}
	public void setSwitchParameter(String switchParameter) {
		this.switchParameter = switchParameter;
	}
	public double getFromQuantity() {
		return fromQuantity;
	}
	public void setFromQuantity(double fromQuantity) {
		this.fromQuantity = fromQuantity;
	}
	public double getToQuantity() {
		return toQuantity;
	}
	public void setToQuantity(double toQuantity) {
		this.toQuantity = toQuantity;
	}
	public boolean isPeriodBased() {
		return isPeriodBased;
	}
	public void setPeriodBased(boolean isPeriodBased) {
		this.isPeriodBased = isPeriodBased;
	}
	public boolean isParameterRequired() {
		return parameterRequired;
	}
	public void setParameterRequired(boolean parameterRequired) {
		this.parameterRequired = parameterRequired;
	}
}
