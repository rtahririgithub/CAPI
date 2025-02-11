/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.subscriber.lifecyclehelper.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Pavel Simonovsky
 *
 */
public class PhoneDirectoryEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date effectiveDate;
	
	private String phoneNumber;
	
	private String nickName;
	
	private boolean existingEntry;

	
	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
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
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the existingEntry
	 */
	public boolean isExistingEntry() {
		return existingEntry;
	}

	/**
	 * @param existingEntry the existingEntry to set
	 */
	public void setExistingEntry(boolean existingEntry) {
		this.existingEntry = existingEntry;
	}
	
	
}
