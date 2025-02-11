/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.api.preferences;

import java.io.Serializable;

/**
 * @author Pavel Simonovsky
 *
 */
public class PreferenceProfileIdentity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final int ROLE_MEMBER = 0;

	public static final int ROLE_OWNER = 1;
	
	private String ban;
	
	private String subscriptionId;
	
	private int role;

	/**
	 * @return the ban
	 */
	public String getBan() {
		return ban;
	}

	/**
	 * @param ban the ban to set
	 */
	public void setBan(String ban) {
		this.ban = ban;
	}

	/**
	 * @return the subscriptionId
	 */
	public String getSubscriptionId() {
		return subscriptionId;
	}

	/**
	 * @param subscriptionId the subscriptionId to set
	 */
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	/**
	 * @return the role
	 */
	public int getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(int role) {
		this.role = role;
	}
	
}
