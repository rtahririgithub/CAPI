/*
 *  Copyright (c) 2013 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.dao.soa.spring;

/*
 * Any WS client that extends this base code must support SOA's Exceptions v3.0 schema. This schema 
 * no longer supports PolicyExceptions. These types of exceptions are now handled as response messages
 * that we must now parse for errors. This error parsing is handled by the SoaResponseHandler interface.
 * Exception handling for ServiceExceptions is unchanged, and inherited from SoaBaseSvcClient.
 *  
 */
public abstract class SoaBaseSvcClientV2 extends SoaBaseSvcClient {
	
	private SoaResponseHandler responseHandler;
	
	public SoaBaseSvcClientV2() {
		this(null, null);
	}

	public SoaBaseSvcClientV2(SoaExceptionHandler exceptionHandler, SoaResponseHandler responseHandler) {
		this.setExceptionHandler(exceptionHandler == null ? new SoaDefaultErrorHandler() : exceptionHandler);
		this.setResponseHandler(responseHandler == null ? new SoaDefaultResponseHandler(this.getClass().getName()) : responseHandler);
	}
	
	public SoaResponseHandler getResponseHandler() {
		return responseHandler;
	}

	public void setResponseHandler(SoaResponseHandler responseHandler) {
		this.responseHandler = responseHandler;
	}
	
}