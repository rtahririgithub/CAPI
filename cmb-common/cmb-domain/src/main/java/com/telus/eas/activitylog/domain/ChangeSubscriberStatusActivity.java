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
public class ChangeSubscriberStatusActivity extends ActivityLoggingInfo {

	private static final long serialVersionUID = 1L;
	
	private String phoneNumber = null;
	
	private char oldSubscriberStatus = 0;
	
	private char newSubscriberStatus = 0;
	
	private String reason = null;
	
	private Date subscriberActivationDate = null;

	private Date subscriberDeactivationDate = null;

	public ChangeSubscriberStatusActivity(ServiceRequestHeader header) {
		super(header);
	}
	
	public String getMessageType() {
		return MESSAGE_TYPE_CHANGE_SUBSCRIBER_STATUS;
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
	 * @return the oldSubscriberStatus
	 */
	public char getOldSubscriberStatus() {
		return oldSubscriberStatus;
	}

	/**
	 * @param oldSubscriberStatus the oldSubscriberStatus to set
	 */
	public void setOldSubscriberStatus(char oldSubscriberStatus) {
		this.oldSubscriberStatus = oldSubscriberStatus;
	}

	/**
	 * @return the newSubscriberStatus
	 */
	public char getNewSubscriberStatus() {
		return newSubscriberStatus;
	}

	/**
	 * @param newSubscriberStatus the newSubscriberStatus to set
	 */
	public void setNewSubscriberStatus(char newSubscriberStatus) {
		this.newSubscriberStatus = newSubscriberStatus;
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

	/**
	 * @return the subscriberDeactivationDate
	 */
	public Date getSubscriberDeactivationDate() {
		return subscriberDeactivationDate;
	}

	/**
	 * @param subscriberDeactivationDate the subscriberDeactivationDate to set
	 */
	public void setSubscriberDeactivationDate(Date subscriberDeactivationDate) {
		this.subscriberDeactivationDate = subscriberDeactivationDate;
	}
	
}
