/*
 *  Copyright (c) 2013 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.dao.soa.spring.rest;

import org.springframework.web.client.RestClientException;

import com.telus.api.ApplicationException;

public interface SoaRestExceptionHandler {

	/*
	 * SoaDefaultRestExceptionHandler is available for default error handling.
	 * If a customized error handler is necessary, it should extend the SoaDefaultExceptionHandler,
	 * and call super.handleException method at the end of the handleException method of the customized error handler class.
	 */
	public <T> void handleRestException(RestClientException exception, String componentCode) throws ApplicationException;
}
