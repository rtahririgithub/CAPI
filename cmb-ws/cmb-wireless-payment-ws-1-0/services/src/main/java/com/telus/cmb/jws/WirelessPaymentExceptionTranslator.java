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
import com.telus.api.SystemCodes;
import com.telus.eas.framework.exception.BankDeclinedTransactionException;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyFaultInfo;
import com.telus.eas.framework.exception.TelusCreditCardException;
import com.telus.eas.framework.exception.TelusSystemException;
import com.telus.eas.framework.exception.TelusValidationException;
import com.telus.eas.framework.exception.WpsPolicyException;
import com.telus.eas.framework.exception.WpsUnmappedPolicyException;

/**
 * @author R. Fong
 * 
 */
public class WirelessPaymentExceptionTranslator extends DefaultExceptionTranslator {

	@Override
	public Exception translateException(Throwable cause, ExceptionTranslationContext context) {

		// Virtually all handled exceptions thrown from the underlying EJBs are now ApplicationExceptions. Do we really need to handle all of the below exception types?
		// TODO clean up the exception translation code.
		
		if (cause instanceof TelusSystemException) {
			String id = ((TelusSystemException) cause).id;
			if (id.equals("SYS00003")) {
				return createServiceException(cause, ServiceErrorCodes.ERROR_DESC_THROWABLE, id, context);
			}
			if (id.equals("SYS00004")) {
				return createServiceException(cause, ServiceErrorCodes.ERROR_DESC_SOAP_FAULT, id, context);
			}
			if (id.equals("SYS00015")) {
				return createServiceException(cause, ServiceErrorCodes.ERROR_DESC_TUXEDO_SERVICE, id, context);
			}
			if (id.equals("SYS00008")) {
				return createServiceException(cause, ServiceErrorCodes.ERROR_DESC_DATABASE, id, context);
			}
		}
		if (cause instanceof TelusValidationException) {
			String id = ((TelusValidationException) cause).id;
			return createPolicyException(cause, ServiceErrorCodes.ERROR_DESC_APILINK_VALIDATION, id, context);
		}
		if (cause instanceof BankDeclinedTransactionException) {
			BankDeclinedTransactionException bdtEx = (BankDeclinedTransactionException) cause;
			return createPolicyException(cause, bdtEx.getBankResponseMessage(), "GP_" + bdtEx.getBankResponseCode(), context);
		}
		if (cause instanceof WpsPolicyException) {
			WpsPolicyException wpsPolicyException = (WpsPolicyException) cause;
			String id = wpsPolicyException.id;
			return createPolicyException(cause, cause.getMessage(), id, context);
		}
		if (cause instanceof WpsUnmappedPolicyException) {
			WpsUnmappedPolicyException wupEx = (WpsUnmappedPolicyException) cause;
			String id = wupEx.id;
			return createPolicyException(cause, cause.getMessage(), id, context);
		}
		if (cause instanceof TelusCreditCardException) {
			String id = ((TelusCreditCardException) cause).id;
			return createPolicyException(cause, ServiceErrorCodes.ERROR_DESC_CC_TX_FAILED, id, context);
		}
		if (cause instanceof ApplicationException) {
			ApplicationException nestedApplicationException = ((ApplicationException) cause).getNestedExceptionBySystemCode(SystemCodes.WPS_GP);
			if (nestedApplicationException != null) {
				// Create a PolicyException specifically for negative responses from the Global Payments System
				return createPolicyException(cause, nestedApplicationException.getErrorMessage(), "GP_" + nestedApplicationException.getErrorCode(), context);
			}
		}

		return super.translateException(cause, context);
	}

	protected PolicyException createPolicyException(Throwable cause, String errorMessageSummary, String errorMessageId, ExceptionTranslationContext context) {
		
		if (cause instanceof ApplicationException) {
			
			PolicyFaultInfo faultInfo = new PolicyFaultInfo();
			String errorMessage, messageId;
			
			ApplicationException ae = (ApplicationException) cause;
			ApplicationException nestedApplicationException = processNestedException(ae);
			if (nestedApplicationException != null) {
				// Override messageId with the sub-system error code
				messageId = nestedApplicationException.getErrorCode();
				errorMessage = createErrorMessageWithNestedExceptionDetails(cause, context, nestedApplicationException);
			} else {
				messageId = errorMessageId;
				errorMessage = createErrorMessageDetails(cause, context);
			}

			faultInfo.setErrorCode(context.getErrorCode());
			faultInfo.setErrorMessage(errorMessage);
			faultInfo.setMessageId(messageId);

			return new PolicyException(errorMessageSummary, faultInfo, cause);			
		}
		
		return super.createPolicyException(cause, errorMessageSummary, errorMessageId, context);
	}

}