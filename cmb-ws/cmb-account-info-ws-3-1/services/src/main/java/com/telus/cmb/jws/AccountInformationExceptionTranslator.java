/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorIds;
import com.telus.eas.framework.exception.TelusApplicationException;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.exception.TelusSystemException;
import com.telus.eas.framework.exception.TelusValidationException;

/**
 * @author Dimitry Siganevich
 *
 */
public class AccountInformationExceptionTranslator extends DefaultExceptionTranslator {

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.DefaultExceptionTranslator#translateException(java.lang.Throwable, com.telus.cmb.jws.ExceptionTranslationContext)
	 */
	@Override
	public Exception translateException(Throwable cause, ExceptionTranslationContext context) {

		if (cause instanceof TelusSystemException) {
			String id = ((TelusSystemException) cause).id;
			
			if (id.equals("SYS00003")) {
				return createServiceException(cause, ServiceErrorCodes.ERROR_DESC_THROWABLE, id, context);
			}
			if (id.equals("SYS00008")) {
				return createServiceException(cause, ServiceErrorCodes.ERROR_DESC_DATABASE, id, context);
			}
			if (id.equals("SYS00015")) {
				return createServiceException(cause, ServiceErrorCodes.ERROR_DESC_TUXEDO_SERVICE, id, context);
			}
			
		}

		if (cause instanceof TelusValidationException) {
			String id = ((TelusValidationException) cause).id;
			
			if (id != null && id.startsWith("VAL") == false) { //to distinguish validation exception from others
				id = "VAL" + id; 
			}
			return createPolicyException (cause, ServiceErrorCodes.ERROR_DESC_APILINK_VALIDATION, id, context);
			
		}
		if (cause instanceof TelusApplicationException ){
			String id  = ((TelusApplicationException) cause).id;
			if (id.equals("APP10001"))
				return createPolicyException(cause, ServiceErrorCodes.ERROR_DESC_ECA, id, context);
		}

		
		if (cause instanceof TelusException) {
			String id = ((TelusException) cause).id;
			
			if (id.equals("APP10004")) {
				return createPolicyException(cause, ServiceErrorCodes.ERROR_DESC_INPUT, id, context);
			}
		}
		
		return super.translateException(cause, context);
	}

}
