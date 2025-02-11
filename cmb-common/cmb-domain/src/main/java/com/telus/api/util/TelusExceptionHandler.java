/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.api.util;

import com.telus.api.TelusAPIException;
import com.telus.eas.framework.exception.TelusException;

/**
 * @author Pavel Simonovsky
 *
 */
public class TelusExceptionHandler {

	private TelusExceptionTranslator defaultExceptionTranslator;
	
	public TelusExceptionHandler(TelusExceptionTranslator exceptionTranslator) {
		this.defaultExceptionTranslator = exceptionTranslator;
	}
	
	public void handleException(Throwable throwable) throws TelusAPIException {
		handleException(throwable, defaultExceptionTranslator);
	}

	public void handleException(Throwable throwable, TelusExceptionTranslator customExceptionTranslator) throws TelusAPIException {
		
		if ( throwable instanceof TelusException ) {
			//override id field to avoid NullPointerException in TelusExceptionTranslator
			TelusException telusEx = (TelusException ) throwable;
			if (telusEx.id==null) telusEx.id = "";
		}
		
		TelusAPIException exception = customExceptionTranslator.translateException(throwable);
		
		if (exception == null && customExceptionTranslator != defaultExceptionTranslator) {
			exception = defaultExceptionTranslator.translateException(throwable);
		}
		
		if (exception != null) {
			throw exception;
		}
	}
	
}
