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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.common.app.ApplicationRuntimeHelper;
import com.telus.cmb.common.app.ApplicationServiceLocator;
import com.telus.eas.framework.exception.WpsPolicyException;
import com.telus.eas.framework.exception.WpsServiceException;
import com.telus.eas.framework.exception.WpsUnmappedPolicyException;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyFaultInfo;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceFaultInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class DefaultExceptionTranslator implements ExceptionTranslator {

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.ExceptionTranslator#translateException(java.lang.Throwable, com.telus.cmb.jws.ExceptionTranslationContext)
	 */
	@Override
	public Exception translateException(Throwable cause, ExceptionTranslationContext context) {

		if (cause instanceof PolicyException) {
			return createPolicyException(cause, cause.getMessage(), ((PolicyException) cause).getFaultInfo().getErrorCode(), context);
		}
		
		if (cause instanceof ServiceException) {
			return createServiceException(cause, cause.getMessage(), ((ServiceException) cause).getFaultInfo().getErrorCode(), context);
		}
		
		if (cause instanceof ServicePolicyException) {
			return createPolicyException(cause, cause.getMessage(), ((ServicePolicyException) cause).getErrorCode(), context);
		}
		
		if (cause instanceof ServiceSystemException) {
			return createServiceException(cause, cause.getMessage(), ((ServiceSystemException) cause).getErrorCode(), context);
		}
		
		if (cause instanceof SystemException) {
			return createServiceException(cause, ((SystemException) cause).getErrorMessage(), ((SystemException) cause).getErrorCode(), context);
		}
		
		if (cause instanceof ApplicationException) {
			return createPolicyException(cause, ((ApplicationException) cause).getErrorMessage(), ((ApplicationException) cause).getErrorCode(), context);
		}
		
		String errorMessageSummary = context.getErrorMessage();
		if (errorMessageSummary == null || errorMessageSummary.equals("")){
			errorMessageSummary = cause.getMessage();
		}
		return createServiceException(cause,errorMessageSummary , ServiceErrorCodes.ERROR_UNKNOWN, context);
	}
	
	protected String createErrorMessageDetails(Throwable cause, ExceptionTranslationContext context) {
		if (cause instanceof ApplicationException) {
			return createErrorMessageDetails(cause, context, context.getErrorCode(), ((ApplicationException) cause).getErrorMessage(), ((ApplicationException) cause).getErrorMessageFr());	
		}else if (cause instanceof SystemException) {
			return createErrorMessageDetails(cause, context, context.getErrorCode(), ((SystemException) cause).getErrorMessage(), "");	
		}else {
			return createErrorMessageDetails(cause, context, context.getErrorCode(), cause.getMessage(),"");
		}
	}
	
	protected String createErrorMessageDetails(Throwable cause, ExceptionTranslationContext context, String errorCode, String errorMessage, String errorMessageFrench) {
		StringWriter stringWriter = new StringWriter(1024);
		PrintWriter writer = new PrintWriter(stringWriter);
		
		writer.println("Error details:");
		String hostName = "";
		try {
			hostName = "/" + InetAddress.getLocalHost().getCanonicalHostName();
		}catch (UnknownHostException e) {
			
		}
		ApplicationRuntimeHelper appRuntimeHelper = ApplicationServiceLocator.getInstance().getApplicationRuntimeHelper();
		writer.println("*** Timestamp: ["+ (new Date()).toString() + "] ***");
		writer.println("*** Runtime info: ["+appRuntimeHelper.getDomainName()+"/"+appRuntimeHelper.getNodeName()+"/"+appRuntimeHelper.getApplicationName()+hostName+"]");
		writer.println("*** Service: [" + context.getServiceName() + "] ***");
		writer.println("*** Operation: [" + context.getOperationName() + "] ***");
		writer.println("*** Error code: [" + errorCode + "] ***");
		writer.println("*** Cause error message: [" + errorMessage + "] ***");
		if (errorMessageFrench!=null && !errorMessageFrench.isEmpty()){
			writer.println("*** Cause error message (French): [" + errorMessageFrench + "] ***");
		}
		writer.println("*** Input parameters: ***");

		String[] parameterNames = context.getParameterNames();
		Object[] parameterValues = context.getParameterValues();
		
		for (int idx = 0; idx < parameterNames.length; idx++) {
			writer.print(" - [" + idx + "][" + parameterNames[idx] + "]: ");
			writer.println(toString(parameterValues[idx], context.getFieldMask()));
		}
		
		writer.println("*** Stack trace: [");
		cause.printStackTrace(writer);
		writer.println("] ***");
			
		return stringWriter.toString();
	}
	
	
	@SuppressWarnings("unchecked")
	protected String createErrorMessageDetails(Throwable cause, ExceptionTranslationContext context, com.telus.eas.framework.exception.PolicyFaultInfo additionalFaultInfo) {
		String errorMessageDetails = createErrorMessageDetails(cause, context);
		
		if (additionalFaultInfo != null) {
			StringWriter stringWriter = new StringWriter(1024);
			PrintWriter writer = new PrintWriter(stringWriter);
			
			writer.println ("*** PolicyFaultInfo received from sub-system call: [");
			writer.println ("****** Error code: [" + additionalFaultInfo.getErrorCode() + "] ***");
			writer.println ("****** Error message: [" + additionalFaultInfo.getErrorMessage() + "] ***");
			if (additionalFaultInfo.getVariables() != null && additionalFaultInfo.getVariables().size() > 0 ) {
				writer.println ("****** Variables: ");
				for (String variable : (List<String>)additionalFaultInfo.getVariables()) {
					writer.println ("****** [" + variable + "] ***"); 
				}
			}
			errorMessageDetails += stringWriter.toString();
		}
		return errorMessageDetails;
	}

	@SuppressWarnings("unchecked")
	protected String createErrorMessageDetails(Throwable cause, ExceptionTranslationContext context, com.telus.eas.framework.exception.ServiceFaultInfo additionalFaultInfo) {
		String errorMessageDetails = createErrorMessageDetails(cause, context);
		
		if (additionalFaultInfo != null) {
			StringWriter stringWriter = new StringWriter(1024);
			PrintWriter writer = new PrintWriter(stringWriter);
			
			writer.println ("*** ServiceFaultInfo received from sub-system call: [");
			writer.println ("****** Error code: [" + additionalFaultInfo.getErrorCode() + "] ***");
			writer.println ("****** Error message: [" + additionalFaultInfo.getErrorMessage() + "] ***");
			if (additionalFaultInfo.getVariables() != null && additionalFaultInfo.getVariables().size() > 0 ) {
				writer.println ("****** Variables: ");
				for (String variable : (List<String>)additionalFaultInfo.getVariables()) {
					writer.println ("****** [" + variable + "] ***"); 
				}
			}
			errorMessageDetails += stringWriter.toString();
		}
		return errorMessageDetails;
	}
	
	@SuppressWarnings("unchecked")
	protected String createErrorMessageWithNestedExceptionDetails(Throwable cause, ExceptionTranslationContext context, ApplicationException nestedException) {
		String errorMessageDetails = createErrorMessageDetails(cause, context);
		
		if (nestedException != null) {
			StringWriter stringWriter = new StringWriter(1024);
			PrintWriter writer = new PrintWriter(stringWriter);
			
			writer.println ("*** Nested ApplicationException received from sub-system call: [");
			writer.println ("****** Error code: [" + nestedException.getErrorCode() + "] ***");
			writer.println ("****** Error message: [" + nestedException.getErrorMessage() + "] ***");
			errorMessageDetails += stringWriter.toString();
		}
		return errorMessageDetails;
	}

	@SuppressWarnings("unchecked")
	protected String createErrorMessageWithNestedExceptionDetails(Throwable cause, ExceptionTranslationContext context, SystemException nestedException) {
		String errorMessageDetails = createErrorMessageDetails(cause, context);
		
		if (nestedException != null) {
			StringWriter stringWriter = new StringWriter(1024);
			PrintWriter writer = new PrintWriter(stringWriter);
			
			writer.println ("*** Nested ServiceException received from sub-system call: [");
			writer.println ("****** Error code: [" + nestedException.getErrorCode() + "] ***");
			writer.println ("****** Error message: [" + nestedException.getErrorMessage() + "] ***");
			errorMessageDetails += stringWriter.toString();
		}
		return errorMessageDetails;
	}
	
	protected ServiceException createServiceException(Throwable cause, String errorMessageSummary, String rootCauseMessage, ExceptionTranslationContext context) {
		ServiceFaultInfo faultInfo = new ServiceFaultInfo();
		String errorCode, errorMessage, messageId;
		
		errorCode = context.getErrorCode();
		messageId = rootCauseMessage;
		if (cause instanceof WpsServiceException) {
			WpsServiceException wpsServiceException = (WpsServiceException) cause;
			if (wpsServiceException.getWpsServiceFaultInfo() != null) {			
				messageId = wpsServiceException.getWpsServiceFaultInfo().getErrorCode(); //override messageId with the actual WPS error code
			}
			errorMessage = createErrorMessageDetails(cause, context, wpsServiceException.getWpsServiceFaultInfo());
		}else {
			errorMessage = createErrorMessageDetails(cause, context);
		}

		faultInfo.setErrorCode(errorCode);
		faultInfo.setErrorMessage(errorMessage);
		faultInfo.setMessageId(messageId);

		return new ServiceException(errorMessageSummary, faultInfo, cause);
	}

	protected PolicyException createPolicyException(Throwable cause, String errorMessageSummary, String errorMessageId, ExceptionTranslationContext context) {
		PolicyFaultInfo faultInfo = new PolicyFaultInfo();
		String errorCode, errorMessage, messageId;
		
		errorCode = context.getErrorCode();
		messageId = errorMessageId;
		if (cause instanceof WpsPolicyException) {
			WpsPolicyException wpsPolicyException = (WpsPolicyException) cause;
			if (wpsPolicyException.getWpsPolicyFaultInfo() != null) {			
				messageId = wpsPolicyException.getWpsPolicyFaultInfo().getErrorCode(); //override messageId with the actual WPS error code
			}
			errorMessage = createErrorMessageDetails(cause, context, wpsPolicyException.getWpsPolicyFaultInfo());
		}else if (cause instanceof WpsUnmappedPolicyException) {
			WpsUnmappedPolicyException wupException = (WpsUnmappedPolicyException) cause;
			if (wupException.getWpsPolicyFaultInfo() != null) {			
				messageId = wupException.getWpsPolicyFaultInfo().getErrorCode(); //override messageId with the actual WPS error code
			}
			errorMessage = createErrorMessageDetails(cause, context, wupException.getWpsPolicyFaultInfo());

		}else if (cause instanceof ApplicationException) {
			ApplicationException ae = (ApplicationException) cause;
			ApplicationException nestedApplicationException = processNestedException(ae);
			/* Per Ruxandra's request, we would return the primary error code from ApplicationException. Should this be changed later on, the 
			 * following block may be used   
			 */
//			if (nestedApplicationException != null) {
//				messageId = nestedApplicationException.getErrorCode();         //override messageId with the sub-system error code 
//				errorMessage = createErrorMessageWithNestedExceptionDetails(cause, context, nestedApplicationException);
//			}else {
//				errorMessage = createErrorMessageDetails(cause, context);
//			}
			errorMessage = createErrorMessageDetails(cause, context);
		}else {
			errorMessage = createErrorMessageDetails(cause, context);
		}

		faultInfo.setErrorCode(errorCode);
		faultInfo.setErrorMessage(errorMessage);
		faultInfo.setMessageId(messageId);
		
		return new PolicyException(errorMessageSummary, faultInfo, cause);
	}
	
	/**
	 * Goes through a list of nested exceptions that the WS should handle
	 * Assumption: The subSystemCodes list always appears mutual-exclusively in a nested exception stack.
	 * @param ApplicationException
	 * @return
	 */
	protected ApplicationException processNestedException(ApplicationException cause) {
		String[] subSystemCodes = {SystemCodes.WPS};
		for (String systemCode : subSystemCodes) {
			ApplicationException nestedException = cause.getNestedExceptionBySystemCode(systemCode);
			if (nestedException != null && nestedException != cause) {
				return nestedException;
			}
		}
		
		return null;
	}
	
	private String toString(Object obj, String[] fieldMask) {
		return (obj != null ? obj.toString() : "null");
	}
	
}
