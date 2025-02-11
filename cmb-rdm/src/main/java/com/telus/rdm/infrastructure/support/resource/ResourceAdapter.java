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
public abstract class ResourceAdapter {

	private ResourceExceptionTranslator exceptionTranslator = new ResourceExceptionTranslatorImpl();
	
	public void setExceptionTranslator(ResourceExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}
	
	protected ResourceExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}
	
	protected <T> T execute(ResourceExecutionCallback<T> callback) throws ResourceAccessException {
		try {
			
			return callback.doInCallback();
			
		} catch (Exception e) {
			handleException(e);
		}
		return null;
	}

	protected void invoke(ResourceInvocationCallback callback) throws ResourceAccessException {
		try {

			callback.doInCallback();
			
		} catch (Exception e) {
			handleException(e);
		}
	}
	
	private void handleException(Exception e) throws ResourceAccessException {
		ResourceAccessException raex = getExceptionTranslator().translate(e);
		if (raex != null) {
			throw raex;
		}
	}
}
