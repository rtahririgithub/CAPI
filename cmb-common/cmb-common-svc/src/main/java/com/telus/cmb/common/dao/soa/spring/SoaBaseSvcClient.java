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

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;

public abstract class SoaBaseSvcClient {
	private static final Log LOGGER = LogFactory.getLog(SoaBaseSvcClient.class);
	
	private SoaExceptionHandler exceptionHandler;
	
	public SoaBaseSvcClient() {
		this(null);
	}

	public SoaBaseSvcClient(SoaExceptionHandler exceptionHandler) {
		this.setExceptionHandler(exceptionHandler == null ? new SoaDefaultErrorHandler() : exceptionHandler);
	}
	
	/*
	 * Any WS client that extends this base code must map the exception to the following classes:
	 *  - com.telus.cmb.wsclient.ServiceException
	 *  - com.telus.cmb.wsclient.PolicyException
	 *  
	 * A customized SoaExceptionHandler should be provided for the exception handling if
	 *  - Special error handling is necessary, e.g. adding customized error messages or error handling base on the exception.
	 *  - Exception(s) is not under package com.telus.cmb.wsclient.*
	 */
	protected <T> T execute(SoaCallback<T> callback) throws ApplicationException {
		//TODO the context is not in used yet, need to implement the @Aspect, refer to ServiceInvocationAspect
		SoaExceptionContext context = new SoaExceptionContext();
		try {
			
			return callback.doCallback();
			
		} catch (Throwable e) {
			LOGGER.error(e);
			getExceptionHandler().handleException(e, context);
		}
		
		return null;
	}
	
	abstract public String ping() throws ApplicationException;

	public TestPointResultInfo test() {
		final TestPointResultInfo resultInfo = new TestPointResultInfo();
		resultInfo.setTimestamp(new Date());
		try {
			String pingResult = ping();
			resultInfo.setResultDetail(pingResult);
			resultInfo.setPass(true);
		} catch (Throwable t) {
			LOGGER.error(t.getMessage(), t);
			resultInfo.setExceptionDetail(t);
			resultInfo.setPass(false);
		}
		return resultInfo;
	}

	public SoaExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(SoaExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}
	
}
