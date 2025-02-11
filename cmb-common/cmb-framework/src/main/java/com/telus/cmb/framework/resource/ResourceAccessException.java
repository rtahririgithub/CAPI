/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.resource;



/**
 * @author Pavel Simonovsky
 *
 */
public class ResourceAccessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceAccessException(String msg) {
		super(msg);
	}

	public ResourceAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
