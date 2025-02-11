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
import com.telus.api.DuplicateObjectException;
import com.telus.api.ErrorCodes;
import com.telus.api.LimitExceededException;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.TooManyObjectsException;
import com.telus.api.account.UnknownBANException;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.exception.TelusSystemException;
import com.telus.api.portability.PortOutEligibilityException;
import com.telus.api.portability.InterBrandPortRequestException;

/**
 * @author Pavel Simonovsky
 *
 */
public class ProviderDefaultExceptionTranslator implements TelusExceptionTranslator {

	/* (non-Javadoc)
	 * @see com.telus.api.util.TelusExceptionTranslator#translateException(java.lang.Throwable)
	 */
	public TelusAPIException translateException(Throwable throwable) {

		TelusAPIException exception = null;
		
		if (throwable instanceof ApplicationException) {
			exception = handleApplicationException((ApplicationException) throwable);
		} else if (throwable instanceof SystemException) {
			exception = handleSystemException((SystemException) throwable);
		} else if (throwable instanceof PortOutEligibilityException) {
			return (PortOutEligibilityException) throwable;
		} 
		  else if (throwable instanceof InterBrandPortRequestException) {
			return (InterBrandPortRequestException) throwable;
		}
		   else if (throwable instanceof TelusException) {
			exception = handleTelusException((TelusException) throwable);
		} else if (throwable instanceof TelusAPIException) {
			exception = ((TelusAPIException) throwable);
		} else {
			exception = new TelusAPIException(throwable);
		}

		return exception;
	}
	
	protected TelusAPIException getExceptionForErrorId(String errorId, Throwable cause) {
		if (errorId.equals("APP10004")) {
			return new UnknownBANException(cause);
		} else if (errorId.equals("APP20002")) {
			return new UnknownSubscriberException(cause);
		} else if (errorId.equals("1112250")){
			return new LimitExceededException(cause);
		}else if (errorId.equals("VAL10014")) {
  			return new DuplicateObjectException("Duplicate Object Name", cause);
  		}else if(errorId.equals("VAL10015")){
  			return new TooManyObjectsException("Too many Talk Group", cause);
  		}
  		else if (errorId.equals("APP10011")){
  			return new TelusAPIException("Memo Not Found", cause);
   		}
  		else if (errorId.equals("APP10006")){
  			return new TelusAPIException ("Credit Check Memo Not Found", cause);
  		}
  		else if (errorId.equals(ErrorCodes.SUBSCRIBER_QUERY_LIMIT_EXCEEDED)) {
			return new LimitExceededException(cause);
		}
		return null;
	}

	protected TelusAPIException handleTelusException(TelusException exception) {
		TelusAPIException result = getExceptionForErrorId(exception.id, exception);
		return result == null ? new TelusAPIException(exception) : result;
	}
	
	protected TelusAPIException handleApplicationException(ApplicationException exception) {
		TelusAPIException result = getExceptionForErrorId(exception.getErrorCode(), exception);
		return result == null ? new TelusAPIException(exception) : result;
	}

	protected TelusAPIException handleSystemException(SystemException exception) {
		TelusSystemException result = new TelusSystemException(exception.getErrorCode(), exception.getErrorMessage(), exception);
		return result;
	}
	
}
