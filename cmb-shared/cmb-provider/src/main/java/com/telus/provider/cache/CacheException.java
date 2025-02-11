/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.provider.cache;

/**
 * @author Pavel Simonovsky
 *
 */
public class CacheException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public CacheException() {
	}

	/**
	 * @param message
	 */
	public CacheException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CacheException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CacheException(String message, Throwable cause) {
		super(message, cause);
	}

}
