package com.telus.cmb.account.lifecyclefacade.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.RemoteAccessException;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.common.dao.soa.spring.SoaDefaultErrorHandler;
import com.telus.cmb.common.dao.soa.spring.SoaExceptionContext;
import com.telus.cmb.wsclient.PolicyException_v1;
import com.telus.cmb.wsclient.ServiceException_v1;

public class CardPaymentServiceSoaExceptionHandler extends SoaDefaultErrorHandler {

	private static final Log LOGGER = LogFactory.getLog(CardPaymentServiceSoaExceptionHandler.class);
	
	@Override
	public void handleException(Throwable cause, SoaExceptionContext translationContext) throws ApplicationException {
		if (cause instanceof PolicyException_v1) {
			ApplicationException ae = getApplicationException((PolicyException_v1)cause);
			LOGGER.debug(getErrorMsg(ae), ae);
			throw ae;
		} else if (cause instanceof ServiceException_v1) {
			SystemException se = getSystemException((ServiceException_v1)cause);
			LOGGER.error(getErrorMsg(se), se);
			throw se;
		} else if (cause instanceof SystemException) {
			LOGGER.error(getErrorMsg((SystemException)cause), cause);
			throw (SystemException)cause;
		} else if (cause instanceof ApplicationException) {
			LOGGER.debug(getErrorMsg((ApplicationException)cause), cause);
			throw (ApplicationException)cause;
		} else if (cause instanceof RemoteAccessException) {
			SystemException se = getSystemException(cause);
			LOGGER.error(getErrorMsg(se), se);
			throw se;
		} else {
			LOGGER.error(cause.getMessage(), cause);
			SystemException se = getUnknownException(cause);
			throw se;
		}
		
	}

	@Override
	protected ApplicationException getApplicationException(PolicyException_v1 cause) {
		return new ApplicationException(SystemCodes.WPS, 
				getErrorCode(cause), 
				getErrorMsg(cause), 
				"", 
				cause);
	}

	@Override
	protected SystemException getSystemException(ServiceException_v1 cause) {
		String contactMessage = "Please contact the WPS team for support.";
		String errorCode = getErrorCode(cause);
		String errorMessage = getErrorMsg(cause);
		String errorId = getErrorMsgId(cause);

		if (isErrorCodeFromAvalon(errorCode)) {
			contactMessage = "This error code with \"AV\" prefix is an error thrown from Avalon. Please contact the Avalon team for support.";
		}

		return new SystemException(SystemCodes.CMB_ALF_EJB, errorCode + "(" + errorId + ")", "ServiceException was received from WPS service (errorCode=" + errorCode
				+ ", message=" + errorMessage + "). " + contactMessage, "", cause);
	}
	
	private boolean isErrorCodeFromAvalon(String errorCode) {
		if (errorCode != null && errorCode.startsWith("AV")) {
			return true;
		} else {
			return false;
		}
	}




	

}
