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

import java.io.Serializable;
import java.util.Date;

import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.servicerequest.ServiceRequestNote;
import com.telus.api.servicerequest.ServiceRequestParent;
import com.telus.api.util.ToStringBuilder;

/**
 * @author Pavel Simonovsky
 *
 */
public abstract class AbstractLoggableActivity implements Serializable {

	private static final long serialVersionUID = 1L;

	private ServiceRequestHeader serviceRequestHeader = null;
	
	private String dealerCode = null;
	
	private String salesRepCode = null;
	
	private String userId = null;
	
	private Date date = new Date();

	
	public AbstractLoggableActivity(ServiceRequestHeader serviceRequestHeader) {
		this.serviceRequestHeader = serviceRequestHeader;
	}

	/**
	 * @return the serviceRequestHeader
	 */
	public ServiceRequestHeader getServiceRequestHeader() {
		return serviceRequestHeader;
	}

	/**
	 * @param serviceRequestHeader the serviceRequestHeader to set
	 */
	public void setServiceRequestHeader(ServiceRequestHeader serviceRequestHeader) {
		this.serviceRequestHeader = serviceRequestHeader;
	}

	/**
	 * @return the dealerCode
	 */
	public String getDealerCode() {
		return dealerCode;
	}

	/**
	 * @param dealerCode the dealerCode to set
	 */
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	/**
	 * @return the salesRepCode
	 */
	public String getSalesRepCode() {
		return salesRepCode;
	}

	/**
	 * @param salesRepCode the salesRepCode to set
	 */
	public void setSalesRepCode(String salesRepCode) {
		this.salesRepCode = salesRepCode;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLanguageCode() {
		return serviceRequestHeader == null ? null : serviceRequestHeader.getLanguageCode();
	}

	public long getApplicationId() {
		return serviceRequestHeader == null ? 0 : serviceRequestHeader.getApplicationId();
	}

	public String getReferenceNumber() {
		return serviceRequestHeader == null ? null : serviceRequestHeader.getReferenceNumber();
	}

	public ServiceRequestParent getServiceRequestParent() {
		return serviceRequestHeader == null ? null : serviceRequestHeader.getServiceRequestParent();
	}

	public ServiceRequestNote getServiceRequestNote() {
		return serviceRequestHeader == null ? null : serviceRequestHeader.getServiceRequestNote();
	}
	
	public void setActors(String dealerCode, String salesRepCode, String userId) {
		setDealerCode(dealerCode);
		setSalesRepCode(salesRepCode);
		setUserId(userId);
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getName() {
		return this.getClass().getName();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ToStringBuilder.toString(this);
	}
	
}
