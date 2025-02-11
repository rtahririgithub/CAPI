/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws;

/**
 * 
 * @author Pavel Simonovsky
 *
 */
public class ServicePolicyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errorCode;
	

	public ServicePolicyException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public ServicePolicyException(String errorCode, String message) {
		this(errorCode, message, null);
	}
	
	public String getErrorCode() {
		return errorCode;
	}

}
