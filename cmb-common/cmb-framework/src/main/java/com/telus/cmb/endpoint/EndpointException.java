/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.endpoint;

import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType;

/**
 * @author Pavel Simonovsky
 *
 */
public class EndpointException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Java type that goes as soapenv:Fault detail element.
	 * 
	 */
	private FaultExceptionDetailsType faultInfo;

	/**
	 * 
	 * @param message
	 * @param faultInfo
	 */
	public EndpointException(String message, FaultExceptionDetailsType faultInfo) {
		super(message);
		this.faultInfo = faultInfo;
	}

	/**
	 * 
	 * @param message
	 * @param faultInfo
	 * @param cause
	 */
	public EndpointException(String message, FaultExceptionDetailsType faultInfo, Throwable cause) {
		super(message, cause);
		this.faultInfo = faultInfo;
	}

	/**
	 * 
	 * @return returns fault bean:
	 *         com.telus.tmi.xmlschema.xsd.enterprise.basetypes
	 *         .exceptions_v3.FaultExceptionDetailsType
	 */
	public FaultExceptionDetailsType getFaultInfo() {
		return faultInfo;
	}
}
