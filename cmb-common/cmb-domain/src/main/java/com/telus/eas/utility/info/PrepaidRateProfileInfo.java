/*
 * Created on 25-Jun-2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.telus.eas.utility.info;

import java.util.ArrayList;

import com.telus.api.reference.PrepaidRateProfile;

/**
 * @author x119734
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PrepaidRateProfileInfo implements PrepaidRateProfile {

	private int rateId;
	private double rate;
	private String rateTypeId;
	private String countryCode;
	private ArrayList applicationIds = new ArrayList();
	private String code;
	private String description;
	private String descriptionFrench;
	
	
	/**
	 * @return Returns the applicationIds.
	 */
	public String[] getApplicationIds() {
			return (String[])applicationIds.toArray(new String[applicationIds.size()]);
	}
	
	public void addApplicationIds(String applicationId) {
		this.applicationIds.add(applicationId);
	}
	
	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return Returns the countryCode.
	 */
	public String getCountryCode() {
		return countryCode;
	}
	/**
	 * @param countryCode The countryCode to set.
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the descriptionFrench.
	 */
	public String getDescriptionFrench() {
		return descriptionFrench;
	}
	/**
	 * @param descriptionFrench The descriptionFrench to set.
	 */
	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}
	/**
	 * @return Returns the rate.
	 */
	public double getRate() {
		return rate;
	}
	/**
	 * @param rate The rate to set.
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}
	/**
	 * @return Returns the rateId.
	 */
	public int getRateId() {
		return rateId;
	}
	/**
	 * @param rateId The rateId to set.
	 */
	public void setRateId(int rateId) {
		this.rateId = rateId;
	}
	/**
	 * @return Returns the rateTypeId.
	 */
	public String getRateTypeId() {
		return rateTypeId;
	}
	/**
	 * @param rateTypeId The rateTypeId to set.
	 */
	public void setRateTypeId(String rateTypeId) {
		this.rateTypeId = rateTypeId;
	}
}
