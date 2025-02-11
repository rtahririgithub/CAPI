package com.telus.eas.subscriber.info;

import com.telus.api.account.UsageProfilePeriodDetail;
import com.telus.eas.framework.info.Info;

/**
 * Title:        UsageProfilePeriodDetailInfo<p>
 * Description:  The UsageProfilePeriodDetailInfo class holds the attributes for usage period details.<p>
 * Copyright:    Copyright (c) 2004<p>
 * Company:      Telus Mobility Inc<p>
 * @author K. Paramsothy
 * @version 1.0
 */
public class UsageProfilePeriodDetailInfo extends Info implements UsageProfilePeriodDetail {

	static final long serialVersionUID = 1L;

	private String periodDescription;
	private int totalCallsInPeriod;
	private double totalMinutesInPeriod;
	private double totalChargeAmountInPeriod;
	private double allowedIncludedMinutesInPeriod;
	private double usedIncludedMinutesInPeriod;
	
	/**
	 * @return Returns the allowedIncludedMinutesInPeriod.
	 */
	public double getAllowedIncludedMinutesInPeriod() {
		return allowedIncludedMinutesInPeriod;
	}
	/**
	 * @param allowedIncludedMinutesInPeriod The allowedIncludedMinutesInPeriod to set.
	 */
	public void setAllowedIncludedMinutesInPeriod(
			double allowedIncludedMinutesInPeriod) {
		this.allowedIncludedMinutesInPeriod = allowedIncludedMinutesInPeriod;
	}
	/**
	 * @return Returns the periodDescription.
	 */
	public String getPeriodDescription() {
		return periodDescription;
	}
	/**
	 * @param periodDescription The periodDescription to set.
	 */
	public void setPeriodDescription(String periodDescription) {
		this.periodDescription = periodDescription;
	}
	/**
	 * @return Returns the totalCallsInPeriod.
	 */
	public int getTotalCallsInPeriod() {
		return totalCallsInPeriod;
	}
	/**
	 * @param totalCallsInPeriod The totalCallsInPeriod to set.
	 */
	public void setTotalCallsInPeriod(int totalCallsInPeriod) {
		this.totalCallsInPeriod = totalCallsInPeriod;
	}
	/**
	 * @return Returns the totalChargeAmountInPeriod.
	 */
	public double getTotalChargeAmountInPeriod() {
		return totalChargeAmountInPeriod;
	}
	/**
	 * @param totalChargeAmountInPeriod The totalChargeAmountInPeriod to set.
	 */
	public void setTotalChargeAmountInPeriod(double totalChargeAmountInPeriod) {
		this.totalChargeAmountInPeriod = totalChargeAmountInPeriod;
	}
	/**
	 * @return Returns the totalMinutesInPeriod.
	 */
	public double getTotalMinutesInPeriod() {
		return totalMinutesInPeriod;
	}
	/**
	 * @param totalMinutesInPeriod The totalMinutesInPeriod to set.
	 */
	public void setTotalMinutesInPeriod(double totalMinutesInPeriod) {
		this.totalMinutesInPeriod = totalMinutesInPeriod;
	}
	/**
	 * @return Returns the usedIncludedMinutesInPeriod.
	 */
	public double getUsedIncludedMinutesInPeriod() {
		return usedIncludedMinutesInPeriod;
	}
	/**
	 * @param usedIncludedMinutesInPeriod The usedIncludedMinutesInPeriod to set.
	 */
	public void setUsedIncludedMinutesInPeriod(
			double usedIncludedMinutesInPeriod) {
		this.usedIncludedMinutesInPeriod = usedIncludedMinutesInPeriod;
	}
	
	/**
	 * toString method: creates a String representation of the object
	 * @return the String representation
	 * 
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UsageProfilePeriodDetailInfo[");
		buffer.append("allowedIncludedMinutesInPeriod = ").append(
				allowedIncludedMinutesInPeriod);
		buffer.append(", periodDescription = ").append(periodDescription);
		buffer.append(", totalCallsInPeriod = ").append(totalCallsInPeriod);
		buffer.append(", totalChargeAmountInPeriod = ").append(
				totalChargeAmountInPeriod);
		buffer.append(", totalMinutesInPeriod = ").append(totalMinutesInPeriod);
		buffer.append(", usedIncludedMinutesInPeriod = ").append(
				usedIncludedMinutesInPeriod);
		buffer.append("]");
		return buffer.toString();
	}
}
