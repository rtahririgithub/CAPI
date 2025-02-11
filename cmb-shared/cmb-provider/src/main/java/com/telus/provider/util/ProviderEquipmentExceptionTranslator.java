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
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.InvalidSerialNumberException;
import com.telus.api.account.UnknownPhoneNumberException;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.api.account.UnknownSerialNumberPrefixException;
import com.telus.api.equipment.MasterLockException;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.exception.TelusSystemException;

/**
 * @author Rad Andric
 *
 */
public class ProviderEquipmentExceptionTranslator implements TelusExceptionTranslator {

	/* (non-Javadoc)
	 * @see com.telus.api.util.TelusExceptionTranslator#translateException(java.lang.Throwable)
	 */
	private String serialORphoneNumber;
	
	public ProviderEquipmentExceptionTranslator(String serialORphoneNumber) {
		this.serialORphoneNumber=serialORphoneNumber;
	}
	
	public TelusAPIException translateException(Throwable throwable) {

		TelusAPIException exception = null;
		
		if (throwable instanceof ApplicationException) {
			exception = handleApplicationException((ApplicationException) throwable);
		} else if (throwable instanceof SystemException) {
			exception = handleSystemException((SystemException) throwable);
		} else if (throwable instanceof TelusException) {
			exception = handleTelusException((TelusException) throwable);
		} else if (throwable instanceof TelusAPIException) {
			exception = ((TelusAPIException) throwable);
		} else {
			exception = new TelusAPIException(throwable);
		}

		return exception;
	}
	
	private TelusAPIException getExceptionForErrorId(String errorId, Throwable cause) {
		if (errorId.equals("VAL10002")) {
			return new UnknownSerialNumberException(cause, serialORphoneNumber, InvalidSerialNumberException.UNKNOWN_EQUIPMENT);
		}else if (cause.getMessage().indexOf("ORA-20310") != -1) {
			return new MasterLockException(cause, MasterLockException.ESN_NOT_FOUND);
		} else if (cause.getMessage().indexOf("ORA-20311") != -1) {
			return new MasterLockException(cause, MasterLockException.USER_ID_NOT_FOUND); 
		} else if (cause.getMessage().indexOf("ORA-20312") != -1) {
			return new MasterLockException(cause, MasterLockException.MAX_REQUESTS_EXCEEDED); 
		} else if (errorId.equals("VAL10007")) {
			return new UnknownSerialNumberPrefixException(cause, serialORphoneNumber);
		}else if (errorId.equals("VAL20038")) {
			return new InvalidSerialNumberException(cause,serialORphoneNumber,InvalidSerialNumberException.EQUIPMENT_NOT_ALLOWED);
		}else if (errorId.equals("VAL20001")) {
			return new UnknownPhoneNumberException(cause, serialORphoneNumber);
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
