package com.telus.eas.subscriber.info;

import com.telus.api.account.UsageProfile;
import com.telus.api.account.UsageProfileList;
import com.telus.eas.framework.info.Info;

import java.util.Arrays;
/**
 * Title:        UsageProfileListInfo<p>
 * Description:  The UsageProfileListInfo class holds the attributes for a list of usage profiles.<p>
 * Copyright:    Copyright (c) 2004<p>
 * Company:      Telus Mobility Inc<p>
 * @author K. Paramsothy
 * @version 1.0
 */
public class UsageProfileListInfo extends Info implements UsageProfileList {

	static final long serialVersionUID = 1L;

	private UsageProfile[] usageProfiles;
		
	public UsageProfile[] getUsageProfiles() {
		return usageProfiles;
	}

	public void setUsageProfiles(UsageProfile[] usageProfiles) {
		this.usageProfiles = usageProfiles;
	}
	
	/**
	 * toString method: creates a String representation of the object
	 * @return the String representation
	 * 
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UsageProfileListInfo[");
		if (usageProfiles == null) {
			buffer.append("usageProfiles = ").append("null");
		} else {
			buffer.append("usageProfiles = ").append(
					Arrays.asList(usageProfiles).toString());
		}
		buffer.append("]");
		return buffer.toString();
	}
}
