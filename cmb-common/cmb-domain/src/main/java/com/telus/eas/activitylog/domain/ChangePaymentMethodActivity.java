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
public class ChangePaymentMethodActivity extends ActivityLoggingInfo {

	private static final long serialVersionUID = 1L;

	private String paymentMethodCode = null;

	public ChangePaymentMethodActivity(ServiceRequestHeader serviceRequestHeader) {
		super(serviceRequestHeader);
	}
	
	public String getMessageType() {
		return MESSAGE_TYPE_CHANGE_PAYMENT_METHOD;
	}

	/**
	 * @return the paymentMethodCode
	 */
	public String getPaymentMethodCode() {
		return paymentMethodCode;
	}

	/**
	 * @param paymentMethodCode the paymentMethodCode to set
	 */
	public void setPaymentMethodCode(String paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
	}

}
