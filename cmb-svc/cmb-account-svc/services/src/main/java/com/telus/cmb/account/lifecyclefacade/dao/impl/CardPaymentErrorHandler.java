/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.account.lifecyclefacade.dao.impl;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaDefaultErrorHandler;
import com.telus.cmb.common.dao.soa.spring.SoaExceptionContext;

/**
 * @author Pavel Simonovsky
 *
 */
public class CardPaymentErrorHandler extends SoaDefaultErrorHandler {


	@Override
	public void handleException(Throwable cause, SoaExceptionContext context) throws ApplicationException {
		
		
		
		super.handleException(cause, context);
	}
}
