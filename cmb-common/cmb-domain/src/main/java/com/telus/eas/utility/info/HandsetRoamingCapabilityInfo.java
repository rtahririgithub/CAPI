/*
 * Created on 27-Jan-2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.HandsetRoamingCapability;

/**
 * @author x119734
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HandsetRoamingCapabilityInfo implements HandsetRoamingCapability {

	String handsetMode;
	String roamingType;
	String roamingValue;
	
	/**
	 * @return Returns the handsetMode.
	 */
	public String getHandsetMode() {
		return handsetMode;
	}
	/**
	 * @param handsetMode The handsetMode to set.
	 */
	public void setHandsetMode(String handsetMode) {
		this.handsetMode = handsetMode;
	}
	/**
	 * @return Returns the roamingType.
	 */
	public String getRoamingType() {
		return roamingType;
	}
	/**
	 * @param roamingType The roamingType to set.
	 */
	public void setRoamingType(String roamingType) {
		this.roamingType = roamingType;
	}
	/**
	 * @return Returns the roamingValue.
	 */
	public String getRoamingValue() {
		return roamingValue;
	}
	/**
	 * @param roamingValue The roamingValue to set.
	 */
	public void setRoamingValue(String roamingValue) {
		this.roamingValue = roamingValue;
	}
	public String getDescription() {
		return roamingType + " - " + roamingValue;
	}

	/* (non-Javadoc)
	 * @see com.telus.api.reference.Reference#getDescriptionFrench()
	 */
	public String getDescriptionFrench() {
		return roamingType + " - " + roamingValue;
	}

	/* (non-Javadoc)
	 * @see com.telus.api.reference.Reference#getCode()
	 */
	public String getCode() {
		return handsetMode;
	}

}
