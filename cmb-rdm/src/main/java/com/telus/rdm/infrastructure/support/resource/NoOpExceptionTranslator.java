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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author x113300
 *
 */
public class NoOpExceptionTranslator implements ResourceExceptionTranslator {

	private static final Logger logger = LoggerFactory.getLogger(NoOpExceptionTranslator.class);

	/* (non-Javadoc)
	 * @see com.telus.rdm.infrastructure.support.resource.ResourceExceptionTranslator#translate(java.lang.Exception)
	 */
	@Override
	public ResourceAccessException translate(Exception exception) {
		
		logger.error("Error: " + exception);
		
		return null;
	}

}
