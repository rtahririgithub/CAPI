package com.telus.eas.subscriber.info;

import com.telus.api.account.UsageProfileAdditionalCharges;
import com.telus.eas.framework.info.Info;

/**
 * Title:        UsageProfileInfo<p>
 * Description:  The UsageProfileInfo class holds the attributes for usage profiles.<p>
 * Copyright:    Copyright (c) 2004<p>
 * Company:      Telus Mobility Inc<p>
 * @author K. Paramsothy
 * @version 1.0
 */
public class UsageProfileAdditionalChargesInfo extends Info implements UsageProfileAdditionalCharges {

	static final long serialVersionUID = 1L;

	private String airtimeFeatureCode;
	private String airtimeFeatureDescription;
	private String secondaryFeatureCode;
	private String secondaryFeatureDescription;
	private String direction;
	private boolean isExtendedHomeArea;
	private int numberOfCalls;
	private double subscriberMinutes;
	private double chargeAmount;
	
	/**
	 * @return Returns the secondaryFeatureCode.
	 */
	public String getSecondaryFeatureCode() {
		return secondaryFeatureCode;
	}
	/**
	 * @param secondaryFeatureCode The secondaryFeatureCode to set.
	 */
	public void setSecondaryFeatureCode(String secondaryFeatureCode) {
		this.secondaryFeatureCode = secondaryFeatureCode;
	}
	/**
	 * @return Returns the secondaryFeatureDescription.
	 */
	public String getSecondaryFeatureDescription() {
		return secondaryFeatureDescription;
	}
	/**
	 * @param secondaryFeatureDescription The secondaryFeatureDescription to set.
	 */
	public void setSecondaryFeatureDescription(
			String secondaryFeatureDescription) {
		this.secondaryFeatureDescription = secondaryFeatureDescription;
	}
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
	 * @see com.telus.api.account.UsageProfile#getNumberOfCalls()
	 */
	public int getNumberOfCalls() {
		return numberOfCalls;
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
	 * @param subscriberMinutes The subscriberMinutes to set.
	 */
	public void setTotalAirtime(double subscriberMinutes) {
		this.subscriberMinutes = subscriberMinutes;
	} 
	
	/**
	 * toString method: creates a String representation of the object
	 * @return the String representation
	 * 
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UsageProfileAdditionalChargesInfo[");
		buffer.append("airtimeFeatureCode = ").append(airtimeFeatureCode);
		buffer.append(", airtimeFeatureDescription = ").append(
				airtimeFeatureDescription);
		buffer.append(", chargeAmount = ").append(chargeAmount);
		buffer.append(", direction = ").append(direction);
		buffer.append(", isExtendedHomeArea = ").append(isExtendedHomeArea);
		buffer.append(", numberOfCalls = ").append(numberOfCalls);
		buffer.append(", secondaryFeatureCode = ").append(secondaryFeatureCode);
		buffer.append(", secondaryFeatureDescription = ").append(
				secondaryFeatureDescription);
		buffer.append(", subscriberMinutes = ").append(subscriberMinutes);
		buffer.append("]");
		return buffer.toString();
	}}
