/*
 *  Copyright (c) 2013 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.dao.soa.spring;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.RemoteAccessException;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.wsclient.PolicyException_v1;
import com.telus.cmb.wsclient.ServiceException_v1;
import com.telus.cmb.wsclient.ServiceException_v3;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyFaultInfo;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceFaultInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType;

public class SoaDefaultErrorHandler implements SoaExceptionHandler {
	
	private static final Log LOGGER = LogFactory.getLog(SoaDefaultErrorHandler.class);

	@Override
	public void handleException(Throwable cause, SoaExceptionContext context) throws ApplicationException {
		/*
		 * TODO: context is not in used yet
		 */
		if (cause instanceof PolicyException_v1) {
			ApplicationException ae = getApplicationException((PolicyException_v1)cause);
			LOGGER.debug(getErrorMsg(ae), ae);
			throw ae;
		} else if (cause instanceof ServiceException_v1) {
			SystemException se = getSystemException((ServiceException_v1)cause);
			LOGGER.error(getErrorMsg(se), se);
			throw se;
		} else if (cause instanceof ServiceException_v3) {
			SystemException se = getSystemException((ServiceException_v3)cause);
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
	
	protected ApplicationException getApplicationException(PolicyException_v1 cause) {
		return new ApplicationException(SystemCodes.SOA_SPRING, 
				getErrorCode(cause), 
				cause.getClass().getCanonicalName() + " " + getErrorMsgId(cause) + " " + getErrorMsg(cause), 
				StringUtils.EMPTY, 
				cause);
	}

	protected SystemException getSystemException(ServiceException_v1 cause) {
		return new SystemException(SystemCodes.SOA_SPRING, 
				getErrorCode(cause), 
				cause.getClass().getCanonicalName() + " " + getErrorMsgId(cause) + " " + getErrorMsg(cause), 
				StringUtils.EMPTY, 
				cause);
	}

	protected SystemException getSystemException(ServiceException_v3 cause) {
		return new SystemException(SystemCodes.SOA_SPRING, 
				getErrorCode(cause), 
				cause.getClass().getCanonicalName() + " " + getErrorMsgId(cause) + " " + getErrorMsg(cause), 
				StringUtils.EMPTY, 
				cause);
	}
	
	protected SystemException getSystemException(Throwable cause) {
		return new SystemException(SystemCodes.SOA_SPRING, 
				cause.getClass().getCanonicalName() + " " + getErrorMsg(cause), 
				StringUtils.EMPTY, 
				cause);
	}
	
	protected SystemException getUnknownException(Throwable cause) {
		return new SystemException(SystemCodes.SOA_SPRING, 
				"ERROR_UNKNOWN" + " " + cause.getMessage(), 
				StringUtils.EMPTY, 
				cause);
	}	
	
	protected String getErrorMsg(Throwable cause) {
		String errorMsg = cause.getMessage();
		return errorMsg;
	}
	
	protected String getErrorMsg(PolicyException_v1 cause) {
		String errorMsg = cause.getMessage();
		PolicyFaultInfo faultInfo = cause.getFaultInfo();
		if (faultInfo != null && StringUtils.isNotEmpty(faultInfo.getErrorMessage())) {
			errorMsg = faultInfo.getErrorMessage();
			if (faultInfo.getVariables() != null && faultInfo.getVariables().size() > 0) {
				errorMsg += getErrorVariables(faultInfo.getVariables());
			}
		}
		return errorMsg;
	}
	
	protected String getErrorMsg(ServiceException_v1 cause) {
		String errorMsg = cause.getMessage();
		ServiceFaultInfo faultInfo = cause.getFaultInfo();
		if (faultInfo != null && StringUtils.isNotEmpty(faultInfo.getErrorMessage())) {
			errorMsg = faultInfo.getErrorMessage();
			if (faultInfo.getVariables() != null && faultInfo.getVariables().size() > 0) {
				errorMsg += getErrorVariables(faultInfo.getVariables());
			}
		}
		return errorMsg;
	}	
	
	protected String getErrorMsg(ServiceException_v3 cause) {
		String errorMsg = cause.getMessage();
		FaultExceptionDetailsType faultInfo = cause.getFaultInfo();
		if (faultInfo != null && StringUtils.isNotEmpty(faultInfo.getErrorMessage())) {
			errorMsg = faultInfo.getErrorMessage();
			if (faultInfo.getVariables() != null && faultInfo.getVariables().size() > 0) {
				errorMsg += getErrorVariables(faultInfo.getVariables());
			}
		}
		return errorMsg;
	}
	
	protected String getErrorVariables(List<String> variables) {
		String result = StringUtils.EMPTY;
		for (String variable:variables) {
			if (variable != null && StringUtils.isNotBlank(variable.trim())) {
				result += "|" + variable.trim();
			}
		}
		if (StringUtils.isNotBlank(result)) {
			result = "[Variables: " + result + "]";
		}
		return result;
	}
	
	protected String getErrorMsg(SystemException cause) {
		String errorMsg = cause.getErrorMessage();
		return errorMsg;
	}
	
	protected String getErrorMsg(ApplicationException cause) {
		String errorMsg = cause.getErrorMessage();
		return errorMsg;
	}
	
	protected String getErrorCode(PolicyException_v1 cause) {
		String errorCode = StringUtils.EMPTY;
		PolicyFaultInfo faultInfo = cause.getFaultInfo();
		if (faultInfo != null && StringUtils.isNotEmpty(faultInfo.getErrorCode()))
			errorCode = faultInfo.getErrorCode();
		return errorCode;
	}
	
	protected String getErrorCode(ServiceException_v1 cause) {
		String errorCode = StringUtils.EMPTY;
		ServiceFaultInfo faultInfo = cause.getFaultInfo();
		if (faultInfo != null && StringUtils.isNotEmpty(faultInfo.getErrorCode()))
			errorCode = faultInfo.getErrorCode();
		return errorCode;
	}
	
	protected String getErrorCode(ServiceException_v3 cause) {
		String errorCode = StringUtils.EMPTY;
		FaultExceptionDetailsType faultInfo = cause.getFaultInfo();
		if (faultInfo != null && StringUtils.isNotEmpty(faultInfo.getErrorCode()))
			errorCode = faultInfo.getErrorCode();
		return errorCode;
	}
	
	protected String getErrorMsgId(PolicyException_v1 cause) {
		String errorId = StringUtils.EMPTY;
		PolicyFaultInfo faultInfo = cause.getFaultInfo();
		if (faultInfo != null && StringUtils.isNotEmpty(faultInfo.getMessageId()))
			errorId = faultInfo.getMessageId();
		return errorId;
	}

	protected String getErrorMsgId(ServiceException_v1 cause) {
		String errorId = StringUtils.EMPTY;
		ServiceFaultInfo faultInfo = cause.getFaultInfo();
		if (faultInfo != null && StringUtils.isNotEmpty(faultInfo.getMessageId()))
			errorId = faultInfo.getMessageId();
		return errorId;
	}

	protected String getErrorMsgId(ServiceException_v3 cause) {
		String errorId = StringUtils.EMPTY;
		FaultExceptionDetailsType faultInfo = cause.getFaultInfo();
		if (faultInfo != null && StringUtils.isNotEmpty(faultInfo.getMessageId()))
			errorId = faultInfo.getMessageId();
		return errorId;
	}

}