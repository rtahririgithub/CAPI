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

import org.springframework.http.ResponseEntity;

import com.telus.api.ApplicationException;

public interface SoaRestResponseHandler {

	/*
	 * SoaDefaultRestResponseHandler is available for default response handling - HTTP 400 will return a BusinessException and HTTP 500 will return a SystemException.
	 * If a customized error handler is necessary, it should extend the SoaDefaultExceptionHandler,
	 * and call super.handleException method at the end of the handleException method of the customized error handler class.
	 */
	public <T> T handleResponse(ResponseEntity<T> response, String componentCode) throws ApplicationException;
}
