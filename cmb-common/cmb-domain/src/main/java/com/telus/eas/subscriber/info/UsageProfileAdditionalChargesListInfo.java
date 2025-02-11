package com.telus.eas.subscriber.info;

import com.telus.api.account.UsageProfileAdditionalChargesList;
import com.telus.api.account.UsageProfileAdditionalCharges;
import com.telus.eas.framework.info.Info;

import java.util.Arrays;
/**
 * Title:        UsageProfileAdditionalChargesListInfo<p>
 * Description:  The UsageProfileAdditionalChargesListInfo class holds the attributes for a list of usage profile additional charges.<p>
 * Copyright:    Copyright (c) 2004<p>
 * Company:      Telus Mobility Inc<p>
 * @author K. Paramsothy
 * @version 1.0
 */
public class UsageProfileAdditionalChargesListInfo extends Info implements UsageProfileAdditionalChargesList {

	static final long serialVersionUID = 1L;

	private UsageProfileAdditionalCharges[] additionalCharges;
		
	public UsageProfileAdditionalCharges[] getUsageProfileAdditionalCharges() {
		return additionalCharges;
	}

	public void setUsageProfileAdditionalCharges(UsageProfileAdditionalCharges[] additionalCharges) {
		this.additionalCharges = additionalCharges;
	}
	
	/**
	 * toString method: creates a String representation of the object
	 * @return the String representation
	 * 
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UsageProfileAdditionalChargesListInfo[");
		if (additionalCharges == null) {
			buffer.append("additionalCharges = ").append("null");
		} else {
			buffer.append("additionalCharges = ").append(
					Arrays.asList(additionalCharges).toString());
		}
		buffer.append("]");
		return buffer.toString();
	}}
