/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.provider.util;

import com.telus.api.ApplicationException;
import com.telus.api.InvalidPasswordException;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.exception.TelusSystemException;

/**
 * @author Rad Andric
 *
 */
public class ProviderDealerExceptionTranslator implements TelusExceptionTranslator {

	/* (non-Javadoc)
	 * @see com.telus.api.util.TelusExceptionTranslator#translateException(java.lang.Throwable)
	 */
	
	private String additionalInfo = "";
	
	public ProviderDealerExceptionTranslator(String additionalInfo) {
		this.additionalInfo=additionalInfo;
	}	

	public TelusAPIException translateException(Throwable throwable) {

		TelusAPIException exception = null;

		if (throwable instanceof ApplicationException) {
			exception = handleApplicationException((ApplicationException) throwable);
		} else if (throwable instanceof UnknownObjectException) {
			exception = ((UnknownObjectException) throwable);			
		} else if (throwable instanceof SystemException) {
			exception = handleSystemException((SystemException) throwable);
		} else if (throwable instanceof TelusException) {
			exception = handleTelusException((TelusException) throwable);
		} else if (throwable instanceof TelusAPIException) {
			exception = ((TelusAPIException) throwable);
		}else {
			exception = new TelusAPIException(throwable);
		}

		return exception;
	}

	private TelusAPIException getExceptionForErrorId(String errorId, Throwable cause) {
		if (errorId.equals("VAL10012")) {
			return new InvalidPasswordException(cause, additionalInfo);
		}
		else if (errorId.equals("VAL10017")) {
			return new UnknownObjectException("Unknown Dealer", cause, additionalInfo);
		}
		return null;
	}

	private TelusAPIException handleTelusException(TelusException exception) {
		TelusAPIException result = getExceptionForErrorId(exception.id, exception);
		return result == null ? new TelusAPIException(exception) : result;
	}

	private TelusAPIException handleApplicationException(ApplicationException exception) {
		TelusAPIException result = getExceptionForErrorId(exception.getErrorCode(), exception);
		return result == null ? new TelusAPIException(exception) : result;
	}

	private TelusAPIException handleSystemException(SystemException exception) {
		TelusSystemException result = new TelusSystemException(exception.getErrorCode(), exception.getErrorMessage(), exception);
		return result;
	}

}
