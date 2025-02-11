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

import com.telus.api.ApplicationException;


public class SubscriberManagementExceptionTranslator extends DefaultExceptionTranslator {
	
	
	@Override
	public Exception translateException(Throwable cause, ExceptionTranslationContext context) {
		
		if (cause instanceof ApplicationException) {
			String id  = ((ApplicationException) cause).getErrorCode();
			if (id.equals("400002"))
				return createPolicyException(cause, ServiceErrorCodes.ERROR_DESC_INCOMPATIBLE_PRICEPLAN_ACCOUNT, ServiceErrorCodes.ERROR_INCOMPATIBLE_PRICEPLAN_ACCOUNT, context);
			
			if (id.equals("400003"))
				return createPolicyException(cause, ServiceErrorCodes.ERROR_DESC_INCOMPATIBLE_SERVICE_ACCOUNT, ServiceErrorCodes.ERROR_INCOMPATIBLE_SERVICE_ACCOUNT, context);
		}
		
		return super.translateException(cause, context);
	}
	

}
