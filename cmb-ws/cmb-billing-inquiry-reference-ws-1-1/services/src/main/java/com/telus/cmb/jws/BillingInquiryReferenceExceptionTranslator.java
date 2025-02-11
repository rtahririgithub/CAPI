/*
 *  Copyright (c) 2010 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws;

import javax.ejb.EJBException;

import com.telus.eas.framework.exception.TelusSystemException;

/**
 * @author Dimitry Siganevich
 *
 */
public class BillingInquiryReferenceExceptionTranslator extends DefaultExceptionTranslator {
	
	
	
	@Override
	public Exception translateException(Throwable cause, ExceptionTranslationContext context) {
		
		if (cause instanceof TelusSystemException) {
			String id = ((TelusSystemException) cause).id;

			if (id.equals("SYS00008")) {
				return createServiceException(cause, ServiceErrorCodes.ERROR_DESC_DATABASE, id, context);
			}
		}
		
		
		if (cause instanceof EJBException) {
			if (cause.getCause() instanceof IllegalArgumentException){
				String id = "IllegalArg001";
				return createPolicyException (cause.getCause(), ServiceErrorCodes.ERROR_DESC_INPUT, id, context);
			}
		}
		return super.translateException(cause, context);
	}
	

}
