/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.infrastructure.support.resource;

/**
 * @author x113300
 *
 */
public class ResourceAccessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errorCode;
	
	/**
	 * @param message
	 */
	public ResourceAccessException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * @param cause
	 */
	public ResourceAccessException(String errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ResourceAccessException(String errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
