package com.telus.eas.subscriber.info;

import com.telus.api.account.UsageProfile;
import com.telus.api.account.UsageProfilePeriodDetail;
import com.telus.eas.framework.info.Info;

import java.util.Arrays;
/**
 * Title:        UsageProfileInfo<p>
 * Description:  The UsageProfileInfo class holds the attributes for usage profiles.<p>
 * Copyright:    Copyright (c) 2004<p>
 * Company:      Telus Mobility Inc<p>
 * @author K. Paramsothy
 * @version 1.0
 */
public class UsageProfileInfo extends Info implements UsageProfile {

	static final long serialVersionUID = 1L;

	private String airtimeFeatureCode;
	private String airtimeFeatureDescription;
	private String direction;
	private boolean isExtendedHomeArea;
	private String step;
	private int numberOfCalls;
	private double subscriberMinutes;
	private double chargeAmount;
	private double allowedIncomingMinutes;
	private double usedIncomingMinutes;
	private int freeAirCalls;
	private int specialCalls;
	private String freeMinuteType;
	private String unitOfMeasure;
	private int numberOfPeriods;
	private UsageProfilePeriodDetail[] periodDetails;

	
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#getAirtimeFeatureCode()
	 */
	public String getAirtimeFeatureCode() {
		return airtimeFeatureCode;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#getAirtimeFeatureDescription()
	 */
	public String getAirtimeFeatureDescription() {
		return airtimeFeatureDescription;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#getAllowedIncomingMinutes()
	 */
	public double getAllowedIncomingMinutes() {
		return allowedIncomingMinutes;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#getTotalAirtime()
	 */
	public double getTotalAirtime() {
		return subscriberMinutes;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#getChargeAmount()
	 */
	public double getChargeAmount() {
		return chargeAmount;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#getDirection()
	 */
	public String getDirection() {
		return direction;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#getFreeAirCalls()
	 */
	public int getFreeAirCalls() {
		return freeAirCalls;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#getFreeMinuteType()
	 */
	public String getFreeMinuteType() {
		return freeMinuteType;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#getNumberOfCalls()
	 */
	public int getNumberOfCalls() {
		return numberOfCalls;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#getNumberOfPeriods()
	 */
	public int getNumberOfPeriods() {
		return numberOfPeriods;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#getPeriodDetails()
	 */
	public UsageProfilePeriodDetail[] getPeriodDetails() {
		return periodDetails;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#getSpecialCalls()
	 */
	public int getSpecialCalls() {
		return specialCalls;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#getStep()
	 */
	public String getStep() {
		return step;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#getUnitOfMeasure()
	 */
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#getUsedIncomingMinutes()
	 */
	public double getUsedIncomingMinutes() {
		return usedIncomingMinutes;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfile#isExtendedHomeArea()
	 */
	public boolean isExtendedHomeArea() {
		return isExtendedHomeArea;
	}
	/**
	 * @param airtimeFeatureCode The airtimeFeatureCode to set.
	 */
	public void setAirtimeFeatureCode(String airtimeFeatureCode) {
		this.airtimeFeatureCode = airtimeFeatureCode;
	}
	/**
	 * @param airtimeFeatureDescription The airtimeFeatureDescription to set.
	 */
	public void setAirtimeFeatureDescription(String airtimeFeatureDescription) {
		this.airtimeFeatureDescription = airtimeFeatureDescription;
	}
	/**
	 * @param allowedIncomingMinutes The allowedIncomingMinutes to set.
	 */
	public void setAllowedIncomingMinutes(double allowedIncomingMinutes) {
		this.allowedIncomingMinutes = allowedIncomingMinutes;
	}
	/**
	 * @param chargeAmount The chargeAmount to set.
	 */
	public void setChargeAmount(double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	/**
	 * @param direction The direction to set.
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}
	/**
	 * @param freeAirCalls The freeAirCalls to set.
	 */
	public void setFreeAirCalls(int freeAirCalls) {
		this.freeAirCalls = freeAirCalls;
	}
	/**
	 * @param freeMinuteType The freeMinuteType to set.
	 */
	public void setFreeMinuteType(String freeMinuteType) {
		this.freeMinuteType = freeMinuteType;
	}
	/**
	 * @param isExtendedHomeArea The isExtendedHomeArea to set.
	 */
	public void setExtendedHomeArea(boolean isExtendedHomeArea) {
		this.isExtendedHomeArea = isExtendedHomeArea;
	}
	/**
	 * @param numberOfCalls The numberOfCalls to set.
	 */
	public void setNumberOfCalls(int numberOfCalls) {
		this.numberOfCalls = numberOfCalls;
	}
	/**
	 * @param numberOfPeriods The numberOfPeriods to set.
	 */
	public void setNumberOfPeriods(int numberOfPeriods) {
		this.numberOfPeriods = numberOfPeriods;
	}
	/**
	 * @param periodDetails The periodDetails to set.
	 */
	public void setPeriodDetails(UsageProfilePeriodDetail[] periodDetails) {
		this.periodDetails = periodDetails;
	}
	/**
	 * @param specialCalls The specialCalls to set.
	 */
	public void setSpecialCalls(int specialCalls) {
		this.specialCalls = specialCalls;
	}
	/**
	 * @param step The step to set.
	 */
	public void setStep(String step) {
		this.step = step;
	}
	/**
	 * @param subscriberMinutes The subscriberMinutes to set.
	 */
	public void setTotalAirtime(double subscriberMinutes) {
		this.subscriberMinutes = subscriberMinutes;
	}
	/**
	 * @param unitOfMeasure The unitOfMeasure to set.
	 */
	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
	/**
	 * @param usedIncomingMinutes The usedIncomingMinutes to set.
	 */
	public void setUsedIncomingMinutes(double usedIncomingMinutes) {
		this.usedIncomingMinutes = usedIncomingMinutes;
	}
	
	/**
	 * toString method: creates a String representation of the object
	 * @return the String representation
	 * 
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UsageProfileInfo[");
		buffer.append("airtimeFeatureCode = ").append(airtimeFeatureCode);
		buffer.append(", airtimeFeatureDescription = ").append(
				airtimeFeatureDescription);
		buffer.append(", allowedIncomingMinutes = ").append(
				allowedIncomingMinutes);
		buffer.append(", chargeAmount = ").append(chargeAmount);
		buffer.append(", direction = ").append(direction);
		buffer.append(", freeAirCalls = ").append(freeAirCalls);
		buffer.append(", freeMinuteType = ").append(freeMinuteType);
		buffer.append(", isExtendedHomeArea = ").append(isExtendedHomeArea);
		buffer.append(", numberOfCalls = ").append(numberOfCalls);
		buffer.append(", numberOfPeriods = ").append(numberOfPeriods);
		if (periodDetails == null) {
			buffer.append(", periodDetails = ").append("null");
		} else {
			buffer.append(", periodDetails = ").append(
					Arrays.asList(periodDetails).toString());
		}
		buffer.append(", specialCalls = ").append(specialCalls);
		buffer.append(", step = ").append(step);
		buffer.append(", subscriberMinutes = ").append(subscriberMinutes);
		buffer.append(", unitOfMeasure = ").append(unitOfMeasure);
		buffer.append(", usedIncomingMinutes = ").append(usedIncomingMinutes);
		buffer.append("]");
		return buffer.toString();
	}
}
