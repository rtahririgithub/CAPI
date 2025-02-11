/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.domain.identity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel Simonovsky	
 *
 */
public class ApplicationIdentity {

	private String applicationCode;
	
	private String applicationKey;
	
	private String description;
	
	private List<ApplicationIdentityEntry> entries = new ArrayList<ApplicationIdentityEntry>();

	public String getApplicationCode() {
		return applicationCode;
	}

	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}

	public String getApplicationKey() {
		return applicationKey;
	}

	public void setApplicationKey(String applicationKey) {
		this.applicationKey = applicationKey;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ApplicationIdentityEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ApplicationIdentityEntry> entries) {
		this.entries = entries;
	}
	
	public void addEntry(ApplicationIdentityEntry entry) {
		entries.add(entry);
	}
	
}
