/*
 * $Id$
 *
 * Copyright Telus.  All rights reserved.  Proprietary and Confidential.
 * @author
 */
package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.account.MaxVoipLine;
import com.telus.eas.framework.info.Info;

public class MaxVoipLineInfo extends Info implements MaxVoipLine {

	private static final long serialVersionUID = 1L;
	
	private Date effectiveStartDate;
	private Date effectiveEndDate;
	private long subscriptionId;
	private int ban;
	private int maxVoipLines;
	private Date createDate;
	private String createUser;
	private Date lastUpdateDate;
	private String lastUpdateUser;

	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	public long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public int getBan() {
		return ban;
	}

	public void setBan(int ban) {
		this.ban = ban;
	}

	public int getMaxVoipLines() {
		return maxVoipLines;
	}

	public void setMaxVoipLines(int maxVoipLines) {
		this.maxVoipLines = maxVoipLines;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer(128);

		s.append("MaxVoipLineInfo:[\n");
		s.append("    effectiveStartDate=[").append(effectiveStartDate)
				.append("]\n");
		s.append("    effectiveEndDate=[").append(effectiveEndDate)
				.append("]\n");
		s.append("    subscriptionId=[").append(subscriptionId).append("]\n");
		s.append("    ban=[").append(ban).append("]\n");
		s.append("    maxVoipLines=[").append(maxVoipLines).append("]\n");
		s.append("    createDate=[").append(createDate).append("]\n");
		s.append("    createUser=[").append(createUser).append("]\n");
		s.append("    lastUpdateDate=[").append(lastUpdateDate).append("]\n");
		s.append("    lastUpdateUser=[").append(lastUpdateUser).append("]\n");
		s.append("super=" + super.toString());

		return s.toString();
	}
	
}