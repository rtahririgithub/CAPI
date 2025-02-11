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
import com.telus.eas.activitylog.queue.info.ActivityLoggingInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class ChangeAccountPinActivity extends ActivityLoggingInfo {

	private static final long serialVersionUID = 1L;
	
	public ChangeAccountPinActivity(ServiceRequestHeader header) {
		super(header);
	}
	
	public String getMessageType() {
		return MESSAGE_TYPE_CHANGE_ACCOUNT_PIN;
	}
	
}
