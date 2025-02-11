/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.eas.activitylog.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.telus.api.util.ToStringBuilder;

/**
 * @author Pavel Simonovsky
 *
 */
public class ActivityLogRecordIdentifier  implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id = null;
	
	private Timestamp timestamp = null;

	public ActivityLogRecordIdentifier(Long id, Timestamp timestamp) {
		this.id = id;
		this.timestamp = timestamp;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the timestamp
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public String toString() {
		return ToStringBuilder.toString(this);
	}
	
}
