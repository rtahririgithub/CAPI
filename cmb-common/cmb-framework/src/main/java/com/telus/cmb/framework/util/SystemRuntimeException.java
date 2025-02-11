/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.util;

/**
 * @author Pavel Simonovsky
 *
 */
public class SystemRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	
	public SystemRuntimeException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * @param cause
	 */
	public SystemRuntimeException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SystemRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getErrorCode() {
		return errorCode;
	}
	
}
