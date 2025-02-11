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
public class ChangePhoneNumberActivity extends ActivityLoggingInfo{

	private static final long serialVersionUID = 1L;
	
	private String newSubscriberId = null;
	
	private String oldPhoneNumber = null;
	
	private String newPhoneNumber = null;
	
	public ChangePhoneNumberActivity(ServiceRequestHeader header) {
		super(header);
	}
	
	public String getMessageType() {
		return MESSAGE_TYPE_CHANGE_PHONE_NUMBER;
	}

	/**
	 * @return the newSubscriberId
	 */
	public String getNewSubscriberId() {
		return newSubscriberId;
	}

	/**
	 * @param newSubscriberId the newSubscriberId to set
	 */
	public void setNewSubscriberId(String newSubscriberId) {
		this.newSubscriberId = newSubscriberId;
	}

	/**
	 * @return the oldPhoneNumber
	 */
	public String getOldPhoneNumber() {
		return oldPhoneNumber;
	}

	/**
	 * @param oldPhoneNumber the oldPhoneNumber to set
	 */
	public void setOldPhoneNumber(String oldPhoneNumber) {
		this.oldPhoneNumber = oldPhoneNumber;
	}

	/**
	 * @return the newPhoneNumber
	 */
	public String getNewPhoneNumber() {
		return newPhoneNumber;
	}

	/**
	 * @param newPhoneNumber the newPhoneNumber to set
	 */
	public void setNewPhoneNumber(String newPhoneNumber) {
		this.newPhoneNumber = newPhoneNumber;
	}
	
}
