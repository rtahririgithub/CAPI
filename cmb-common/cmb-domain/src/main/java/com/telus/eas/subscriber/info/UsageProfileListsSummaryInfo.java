package com.telus.eas.subscriber.info;

import com.telus.api.account.UsageProfileAdditionalChargesList;
import com.telus.api.account.UsageProfileList;
import com.telus.api.account.UsageProfileListsSummary;
import com.telus.eas.framework.info.Info;

/**
 * Title:        UsageProfileListInfo<p>
 * Description:  The UsageProfileListInfo class holds the attributes for a list of usage profiles.<p>
 * Copyright:    Copyright (c) 2004<p>
 * Company:      Telus Mobility Inc<p>
 * @author K. Paramsothy
 * @version 1.0
 */
public class UsageProfileListsSummaryInfo extends Info implements UsageProfileListsSummary {

	static final long serialVersionUID = 1L;

	UsageProfileList usageProfileList;
	UsageProfileAdditionalChargesList usageProfileAdditionalChargesList;
	
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfileListsSummary#getUsageProfileAdditionalChargesList()
	 */
	public UsageProfileAdditionalChargesList getUsageProfileAdditionalChargesList() {
		return usageProfileAdditionalChargesList;
	}
	/* (non-Javadoc)
	 * @see com.telus.api.account.UsageProfileListsSummary#getUsageProfileList()
	 */
	public UsageProfileList getUsageProfileList() {
		return usageProfileList;
	}
	
	/**
	 * @param usageProfileList The usageProfileList to set.
	 */
	public void setUsageProfileList(UsageProfileList usageProfileList) {
		this.usageProfileList = usageProfileList;
	}
	/**
	 * @param usageProfileAdditionalChargesList The usageProfileAdditionalChargesList to set.
	 */
	public void setUsageProfileAdditionalChargesList(UsageProfileAdditionalChargesList usageProfileAdditionalChargesList) {
		this.usageProfileAdditionalChargesList = usageProfileAdditionalChargesList;
	}

	/**
	 * toString method: creates a String representation of the object
	 * @return the String representation
	 * 
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UsageProfileListsSummaryInfo[");
		buffer.append("]");
		return buffer.toString();
	}
}
