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
public class ChangeAccountTypeActivity extends ActivityLoggingInfo {

	private static final long serialVersionUID = 1L;

	private char oldAccountType;
	
	private char newAccountType;
	
	private char oldAccountSubType;
	
	private char newAccountSubType;

	public ChangeAccountTypeActivity(ServiceRequestHeader header) {
		super(header);
	}

	/**
	 * @return the oldAccountType
	 */
	public char getOldAccountType() {
		return oldAccountType;
	}

	/**
	 * @param oldAccountType the oldAccountType to set
	 */
	public void setOldAccountType(char oldAccountType) {
		this.oldAccountType = oldAccountType;
	}

	/**
	 * @return the newAccountType
	 */
	public char getNewAccountType() {
		return newAccountType;
	}

	/**
	 * @param newAccountType the newAccountType to set
	 */
	public void setNewAccountType(char newAccountType) {
		this.newAccountType = newAccountType;
	}

	/**
	 * @return the oldAccountSubType
	 */
	public char getOldAccountSubType() {
		return oldAccountSubType;
	}

	/**
	 * @param oldAccountSubType the oldAccountSubType to set
	 */
	public void setOldAccountSubType(char oldAccountSubType) {
		this.oldAccountSubType = oldAccountSubType;
	}

	/**
	 * @return the newAccountSubType
	 */
	public char getNewAccountSubType() {
		return newAccountSubType;
	}

	/**
	 * @param newAccountSubType the newAccountSubType to set
	 */
	public void setNewAccountSubType(char newAccountSubType) {
		this.newAccountSubType = newAccountSubType;
	}
	
	public String getMessageType() {
		return MESSAGE_TYPE_CHANGE_ACCOUNT_TYPE;
	}
	
}
