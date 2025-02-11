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
public class NoOpResourceExceptionTranslator implements ResourceExceptionTranslator {

	private static final Logger logger = LoggerFactory.getLogger(NoOpResourceExceptionTranslator.class);
	

	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.resource.ResourceExceptionTranslator#translate(com.telus.cmb.framework.resource.ResourceAccessContext, java.lang.Exception)
	 */
	@Override
	public ApplicationException translate(ResourceAccessContext context, Exception e) {
		
		String message = String.format("[%s] Error accessing system [%s], component [%s] - %s", context.getComponentCode(), context.getTargetSystemCode(), context.getTargetComponentCode(), e.getMessage());
		
		logger.error(message);
		
		return null;
	}

}
