package com.telus.cmb.jws;

import com.telus.cmb.jws.identity.ClientIdentityProvider;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyFaultInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Message;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.ResponseMessage;

public abstract class BaseServiceV2 extends BaseService {
	
	public static final String ERROR_MSG_TYPE_ERROR = "ERROR";
	public static final String ERROR_MSG_TYPE_VALIDATION = "VALIDATION";

	public BaseServiceV2() {
		this(null, null);
	}

	public BaseServiceV2(ClientIdentityProvider clientIdentityProvider) {
		super(clientIdentityProvider);
	}

	public BaseServiceV2(ExceptionTranslator exceptionTranslator) {
		super(exceptionTranslator);
	}

	public BaseServiceV2(ExceptionTranslator exceptionTranslator, ClientIdentityProvider clientIdentityProvider) {
		super(exceptionTranslator, clientIdentityProvider);
	}

	@Override
	protected <T> T execute(ServiceInvocationCallback<T> callback) throws ServiceException {
		
		ServiceInvocationContext serviceInvocationContext = new ServiceInvocationContext(this);
		
		try {
			// Check if there is a Schema Validation Exception from the Service Schema Validator
			Exception exception = (Exception) serviceContext.getMessageContext().get(ServiceSchemaValidator.ERROR);
			if (exception != null) {
				serviceInvocationContext.setSchemaValidationException(exception);
			}
			
			return callback.doInInvocationCallback(serviceInvocationContext);

		} catch (Throwable throwable) {
			// the runtime exception will be intercepted by service invocation advice
			// and passed to exception translator for further processing.
			throw new ServiceNestedException(throwable);
		} finally {
			serviceInvocationContext.dispose();
		}
	}
	
	// Due to new SOA guidelines, PolicyException is no longer supported. As of July 2014, all new major-version web service releases
	// (e.g., SMS v3.0) must support only ServiceException and all other exceptions are to be mapped to the response as an error code
	// and message. This version of the execute method catches and translates all Throwables and takes any resulting PolicyExceptions
	// and maps them to the supplied ResponseMessage and returns the service invocation as normal. Anything else is thrown as a 
	// ServiceNestedException.
	// Note: this only catches PolicyExceptions thrown within the ServiceInvocationContext. This is only a temporary solution, implemented
	// in conjunction with other workarounds (see SMS 3.0 createExceptionTranslationContext method).
	// TODO Implement a long-term solution (likely in a new version of the ServiceBusinessOperation annotation) to elegantly handle PolicyExceptions.
	protected <T extends ResponseMessage> T execute(ServiceInvocationCallback<T> callback, T response, ExceptionTranslationContext exceptionContext) throws ServiceException {

		ServiceInvocationContext serviceInvocationContext = new ServiceInvocationContext(this);

		try {
			// Check if there is a Schema Validation Exception from the Service Schema Validator
			Exception exception = (Exception) serviceContext.getMessageContext().get(ServiceSchemaValidator.ERROR);
			if (exception != null) {
				serviceInvocationContext.setSchemaValidationException(exception);
			}

			return callback.doInInvocationCallback(serviceInvocationContext);

		} catch (Throwable throwable) {
			// This is where we catch and map PolicyExceptions. We need to translate the throwable first, though.
			Exception exception = getExceptionTranslator().translateException(throwable, exceptionContext);
			if (exception instanceof PolicyException) {
				// Map all translated PolicyException attributes and return the response.
				PolicyException pe = (PolicyException) exception;
				Message message = new Message();
				message.setMessage(pe.getFaultInfo().getErrorMessage());
				// Since we only have one field for error code, concatenate the error code and error message ID.
				response.setErrorCode(pe.getFaultInfo().getErrorCode() + "." + pe.getFaultInfo().getMessageId());
				response.setMessageType(ERROR_MSG_TYPE_ERROR);
				response.getMessageList().add(message);

				return response;
			}	
			
			// The runtime exception will be intercepted by service invocation advice and passed to exception translator for further processing.
			throw new ServiceNestedException(throwable);
		} finally {
			serviceInvocationContext.dispose();
		}
	}
	
	// Note: changed visibility of this method from protected to public to resolve exceptions when called from within a ServiceInvocationCallback
	// context. Methods which throw PolicyExceptions must now be called from within a ServiceInvocationCallback context in order to be mapped
	// to the new SOA Exceptions v3.0 ResponseMessage schema, otherwise they will be converted to ServiceExceptions or (even worse) SOAP faults.
	// TODO Implement a long-term solution (likely in a new version of the ServiceBusinessOperation annotation) to elegantly handle PolicyExceptions.
	public void assertValid(String paramName, Object paramValue, Object [] possibleValues) throws PolicyException {
		
		if (paramValue == null || (paramValue instanceof String && ((String)paramValue).trim().length() == 0)) {
			PolicyFaultInfo policyFaultInfo = new PolicyFaultInfo();
			policyFaultInfo.setErrorCode("ERROR_0002");
			policyFaultInfo.setErrorMessage(getPolicyErrorMessage(paramName, possibleValues));
			throw new PolicyException("Input parameter, " + paramName + ", validation error", policyFaultInfo);
			
		} else if (paramValue instanceof String) {
			String strValue = ((String) paramValue).trim();
			for (Object possibleValue : possibleValues) {
				if (strValue.equals(possibleValue)) {
					return;
				}
			}
			String errorMessage = getPolicyErrorMessage(paramName, possibleValues);
			PolicyFaultInfo policyFaultInfo = new PolicyFaultInfo();
			policyFaultInfo.setErrorCode("ERROR_0003");
			policyFaultInfo.setErrorMessage(errorMessage);
			throw new PolicyException(errorMessage, policyFaultInfo);
		}
	}

}