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

import java.net.SocketException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;

public class SoaDefaultRestExceptionHandler implements SoaRestExceptionHandler {

	private static final Log logger = LogFactory.getLog(SoaDefaultRestExceptionHandler.class);

	@Override
	public <T> void handleRestException(RestClientException exception, String componentCode) throws ApplicationException {

		if (exception instanceof HttpClientErrorException) {
			HttpClientErrorException clientException = (HttpClientErrorException) exception;
			logger.error("Rest client exception: code[" + clientException.getStatusCode() + "], message[" + clientException.getResponseBodyAsString() + "]");			
			throw new ApplicationException(SystemCodes.SOA_SPRING, getErrorCode(clientException.getStatusCode()), clientException.getMessage(), StringUtils.EMPTY, exception.getCause());
		} else if (exception instanceof HttpClientErrorException) {
			HttpServerErrorException serverException = (HttpServerErrorException) exception;
			logger.error("Rest system exception: code[" + serverException.getStatusCode() + "], message[" + serverException.getResponseBodyAsString() + "]");
			throw new ApplicationException(SystemCodes.SOA_SPRING, getErrorCode(serverException.getStatusCode()), serverException.getMessage(), StringUtils.EMPTY, exception.getCause());
		} else if (exception instanceof ResourceAccessException) {
			throw new SystemException (SystemCodes.SOA_SPRING, exception.getMessage(), StringUtils.EMPTY, exception.getCause());
		} else if (exception.getCause() instanceof SocketException) {
			throw new SystemException (SystemCodes.SOA_SPRING, exception.getMessage(), StringUtils.EMPTY, exception.getCause());
		} else {
			logger.error("Unknown Rest exception: " + exception.getMessage());
			throw new ApplicationException(SystemCodes.SOA_SPRING, exception.getMessage(), StringUtils.EMPTY, exception.getCause());
		}
	}

	private String getErrorCode(HttpStatus status) {
		return String.valueOf(status.value()) + " - " + status.name();
	}
	
}