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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.telus.api.ApplicationException;

public class SoaDefaultRestResponseHandler implements SoaRestResponseHandler {

	@Override
	public <T> T handleResponse(ResponseEntity<T> response, String componentCode) throws ApplicationException {
		if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
			return response.getBody();
		}
		return null;
	}

}