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

import com.telus.api.servicerequest.ServiceRequestHeader;

/**
 * @author Pavel Simonovsky
 *
 */
public abstract class AbstractAccountActivity extends AbstractLoggableActivity {

	private static final long serialVersionUID = 1L;
	
	private int banId = 0;

	public AbstractAccountActivity(ServiceRequestHeader serviceRequestHeader) {
		super(serviceRequestHeader);
	}

	/**
	 * @return the banId
	 */
	public int getBanId() {
		return banId;
	}

	/**
	 * @param banId the banId to set
	 */
	public void setBanId(int banId) {
		this.banId = banId;
	}
	
}
