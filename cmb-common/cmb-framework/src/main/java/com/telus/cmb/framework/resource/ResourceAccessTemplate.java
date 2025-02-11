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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telus.api.ApplicationException;

/**
 * @author Pavel Simonovsky
 *
 */
public class ResourceAccessTemplate implements ResourceExceptionTranslator {
	
	private static final Logger logger = LoggerFactory.getLogger(ResourceAccessTemplate.class);

	private ResourceExceptionTranslator exceptionTranslator = new DefaultResourceExceptionTranslator();
	

	protected ResourceExceptionTranslator getExceptionTranslator() {
		return exceptionTranslator;
	}

	protected void setExceptionTranslator(ResourceExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator;
	}

	protected <T> T execute(ResourceExecutionCallback<T> callback, String errorCode, String componentCode, String targetComponentCode, String targetSystemCode) throws ApplicationException {
		return execute(callback, new ResourceAccessContext(errorCode, componentCode, targetSystemCode, targetComponentCode));
	}
	
	protected <T> T execute(ResourceExecutionCallback<T> callback, ResourceAccessContext context) throws ApplicationException {
	
		logger.debug("Executing {} using context {}", callback, context);
		
		T result = null;
		
		try {
			
			result = callback.doInCallback();
			
		} catch (Exception e) {
			
			ApplicationException translatedException = translate(context, e);
			if (translatedException != null) {
				throw translatedException;
			}
		}
		
		return result;
	}

	protected void invoke(ResourceInvocationCallback callback, String errorCode, String componentCode, String targetComponentCode, String targetSystemCode) throws ApplicationException {
		invoke(callback, new ResourceAccessContext(errorCode, componentCode, targetSystemCode, targetComponentCode));
	}
	
	protected void invoke(ResourceInvocationCallback callback, ResourceAccessContext context) throws ApplicationException {
		
		logger.debug("Invoking {} using context {}", callback, context);
		
		try {
			
			callback.doInCallback();
			
		} catch (Exception e) {

			ApplicationException translatedException = translate(context, e);
			if (translatedException != null) {
				throw translatedException;
			}
		}
		
	}
	
//	protected void invoke(ResourceInvocationCallback callback) throws ApplicationException {
//		ResourceAccessContext context = new ResourceAccessContext();
//		invoke(callback, context);
//	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.framework.resource.ResourceExceptionTranslator#translate(com.telus.cmb.framework.resource.ResourceAccessContext, java.lang.Exception)
	 */
	@Override
	public ApplicationException translate(ResourceAccessContext context, Exception e) {
		return getExceptionTranslator().translate(context, e);
	}
}
