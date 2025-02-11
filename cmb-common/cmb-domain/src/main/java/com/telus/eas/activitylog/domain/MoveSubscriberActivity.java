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

import java.util.Date;

import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.eas.activitylog.queue.info.ActivityLoggingInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class MoveSubscriberActivity extends ActivityLoggingInfo{
	
	private static final long serialVersionUID = 1L;
	
	private int newBanId = 0;
	
	private String phoneNumber = null;
	
	private String reason = null;
	
	private Date subscriberActivationDate = null;
	
	public MoveSubscriberActivity(ServiceRequestHeader header) {
		super(header);
	}
	
	public String getMessageType() {
		return MESSAGE_TYPE_MOVE_SUBSCRIBER;
	}

	/**
	 * @return the newBanId
	 */
	public int getNewBanId() {
		return newBanId;
	}

	/**
	 * @param newBanId the newBanId to set
	 */
	public void setNewBanId(int newBanId) {
		this.newBanId = newBanId;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return the subscriberActivationDate
	 */
	public Date getSubscriberActivationDate() {
		return subscriberActivationDate;
	}

	/**
	 * @param subscriberActivationDate the subscriberActivationDate to set
	 */
	public void setSubscriberActivationDate(Date subscriberActivationDate) {
		this.subscriberActivationDate = subscriberActivationDate;
	}
	
}
