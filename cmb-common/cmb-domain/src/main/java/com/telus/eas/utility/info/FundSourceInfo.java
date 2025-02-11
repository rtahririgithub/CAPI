/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.FundSource;
import com.telus.eas.framework.info.Info;

/**
 * @author Pavel Simonovsky
 *
 */
public class FundSourceInfo extends Info implements FundSource {
	
	private static final long serialVersionUID = 1L;

	private int fundSourceType = FUND_SOURCE_NOT_DEFINED;
	
	private String defaultIndicator;

	
	/**
	 * @param fundSourceType the fundSourceType to set
	 */
	public void setFundSourceType(int fundSourceType) {
		this.fundSourceType = fundSourceType;
	}

	/**
	 * @param defaultIndicator the defaultIndicator to set
	 */
	public void setDefaultIndicator(String defaultIndicator) {
		this.defaultIndicator = defaultIndicator;
	}

	/* (non-Javadoc)
	 * @see com.telus.api.reference.FundSource#getDefaultIndicator()
	 */
	public String getDefaultIndicator() {
		return defaultIndicator;
	}

	/* (non-Javadoc)
	 * @see com.telus.api.reference.FundSource#getFundSourceType()
	 */
	public int getFundSourceType() {
		return fundSourceType;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer("FundSourceInfo: [");
		buffer.append("fundSourceType = ").append(fundSourceType).append(", ");
		buffer.append("defaultIndicator = ").append(defaultIndicator);
		buffer.append("]");
		return buffer.toString();
	}
}
