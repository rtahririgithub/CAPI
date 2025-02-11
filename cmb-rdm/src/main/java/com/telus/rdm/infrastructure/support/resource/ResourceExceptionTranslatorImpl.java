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
public class ResourceExceptionTranslatorImpl implements ResourceExceptionTranslator {


	/* (non-Javadoc)
	 * @see com.telus.cmb.rdm.infrastructure.remoting.support.ResourceExceptionTranslator#translate(java.lang.Exception)
	 */
	@Override
	public ResourceAccessException translate(Exception exception) {
		return new ResourceAccessException("Error accessing remote resource: " + exception.getMessage(), exception);
	}

}
