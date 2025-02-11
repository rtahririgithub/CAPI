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

import com.telus.api.ApplicationException;

/**
 * @author Pavel Simonovsky
 *
 */
public class DefaultResourceExceptionTranslator implements ResourceExceptionTranslator {

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.framework.resource.ResourceExceptionTranslator#translate(com.telus.cmb.framework.resource.ResourceAccessContext, java.lang.Exception)
	 */
	@Override
	public ApplicationException translate(ResourceAccessContext context, Exception e) {
		
		if (e instanceof ApplicationException) {
			return (ApplicationException) e;
		}
		
		String errorMessage = String.format("[%s] Error accessing system [%s], component [%s] - %s", context.getComponentCode(), context.getTargetSystemCode(), context.getTargetComponentCode(), e.getMessage());

		ApplicationException aex = new ApplicationException(context.getTargetSystemCode(), context.getErrorCode(), errorMessage, errorMessage, e);
		
		return aex;
	}
}
