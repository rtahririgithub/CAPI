/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.LockReason;
import com.telus.eas.framework.info.Info;

public class LockReasonInfo extends Info implements LockReason {

	static final long serialVersionUID = 1L;
	
	private long lockReasonId;
	private String description;
	private String descriptionFrench;

	public LockReasonInfo() {
		super();
	}
	
	public long getLockReasonId() {
		return lockReasonId;
	}
	
	public String getCode() {
		return String.valueOf(lockReasonId);
	}
	
	public String getDescription() {
		return description;
	}
		
	public String getDescriptionFrench() {
		return descriptionFrench;
	}

	public void setLockReasonId(long id) {
		this.lockReasonId = id;
	}
	
	public void setDescription(String newDescription) {
		this.description = newDescription;
	}	
	
	public void setDescriptionFrench(String newDescriptionFrench) {
		this.descriptionFrench = newDescriptionFrench;
	}

	public String toString() {
		StringBuffer s = new StringBuffer();
		
		s.append("LockReason:{\n");
		s.append("    LockReasonId=[").append(getLockReasonId()).append("]\n");
		s.append("    code=[").append(getCode()).append("]\n");
		s.append("    description=[").append(getDescription()).append("]\n");
		s.append("    descriptionFrench=[").append(getDescriptionFrench()).append("]\n");
		s.append("}");
		
		return s.toString();
	}
}