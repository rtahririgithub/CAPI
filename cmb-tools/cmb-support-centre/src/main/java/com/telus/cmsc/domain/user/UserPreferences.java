/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.domain.user;

import java.io.Serializable;

/**
 * @author Pavel Simonovsky	
 *
 */
public class UserPreferences implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean filterStandaloneArtifacts = true;

	public boolean isFilterStandaloneArtifacts() {
		return filterStandaloneArtifacts;
	}

	public void setFilterStandaloneArtifacts(boolean filterStandaloneArtifacts) {
		this.filterStandaloneArtifacts = filterStandaloneArtifacts;
	}
	
}
