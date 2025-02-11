package com.telus.cmb.endpoint.mapper;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.cmb.jws.DefaultExceptionTranslator;
import com.telus.cmb.jws.ExceptionTranslationContext;
import com.telus.cmb.jws.PolicyException;
import com.telus.cmb.jws.ServiceErrorCodes;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyFaultInfo;

public class SubscriberInformationExceptionMapper extends DefaultExceptionTranslator{
	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.DefaultExceptionTranslator#translateException(java.lang.Throwable, com.telus.cmb.jws.ExceptionTranslationContext)
	 */
	@Override
	public Exception translateException(Throwable cause, ExceptionTranslationContext context) {
		if (cause instanceof ApplicationException) {

			String errorCode = ((ApplicationException)cause).getErrorCode();
			String errorMessage = ((ApplicationException)cause).getErrorMessage();
			if(errorCode.equals(ErrorCodes.SUBSCRIBER_QUERY_LIMIT_EXCEEDED)) {
				return createPolicyException(cause, errorMessage,ServiceErrorCodes.ERROR_MAX_LIMIT_EXCEEDED,context);
			}
		}
		return super.translateException(cause, context);
	}

	@Override
	protected PolicyException createPolicyException(Throwable cause, String errorMessageSummary, String errorMessageId, ExceptionTranslationContext context) {
		PolicyFaultInfo faultInfo = new PolicyFaultInfo();
		String errorCode, errorMessage, messageId;
		
		errorCode = context.getErrorCode();
		messageId = errorMessageId;
		errorMessage = createErrorMessageDetails(cause,context);

		faultInfo.setErrorCode(errorCode);
		faultInfo.setErrorMessage(errorMessage);
		faultInfo.setMessageId(messageId);
		
		return new PolicyException(errorMessageSummary, faultInfo, cause);
	}
}
