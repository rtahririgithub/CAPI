/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.endpoint;

import java.beans.PropertyDescriptor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.bea.common.security.jdkutils.WeaverUtil.Writer;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.cmb.endpoint.EndpointException;
import com.telus.cmb.framework.validation.ValidationError;
import com.telus.cmb.framework.validation.ValidationGroup;
import com.telus.cmb.framework.validation.ValidationResult;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.cmbresponsecommontypes_v1.ContextMap;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.cmbresponsecommontypes_v1.ContextMapList;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.cmbresponsecommontypes_v1.ServiceResponseMessage;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.cmbresponsecommontypes_v1.SubSystemError;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.cmbresponsecommontypes_v1.SubSystemErrorList;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Message;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType;

/**
 * @author Pavel Simonovsky
 *
 */
public class EndpointResponseBuilderImpl implements EndpointResponseBuilder {
	
	private static final Logger logger = LoggerFactory.getLogger(EndpointResponseBuilderImpl.class);
	
	public static final String MSG_TYPE_SUCCESS = "Success";
	public static final String MSG_TYPE_FAILURE = "Failure";
	

	/**
	 * 
	 */
	public EndpointResponseBuilderImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.endpoint.provider.EndpointResponseBuilder#buildResponse(java.lang.Object, com.telus.cmb.endpoint.provider.EndpointOperationContext)
	 */
	@Override
	public Object buildResponse(EndpointOperationContext context) throws Exception {

	    checkHttpAuthorizationError(context);
		EndpointException exception = createException(context);
		if (exception != null) {
			throw exception;
		}
		return createResponseMessage(context);
	}
	
	private void checkHttpAuthorizationError(EndpointOperationContext context) {
	    
	    Throwable cause = context.getException();
	    if (cause instanceof EndpointSystemException)  {
	        EndpointSystemException endpointException = (EndpointSystemException)cause;
	        if ( ErrorCodes.CMB_HTTP_ERROR_UNAUTHORIZED.equals( endpointException.getErrorCode() ) ) {
	            throw new HttpServerErrorException( HttpStatus.UNAUTHORIZED, endpointException.getLocalizedMessage() );
	        }
	        
	    }
	    

//	    Throwable cause = context.getException();

//	    if ((cause != null) && (cause instanceof EndpointSystemException))  {
//	        EndpointSystemException endpointException = (EndpointSystemException)cause;
//	        if ( ErrorCodes.CMB_HTTP_ERROR_UNAUTHORIZED.equals( endpointException.getErrorCode() ) ) {
//	            if ( endpointException.getCause() instanceof HttpServerErrorException ) {
//	                HttpServerErrorException httpException = (HttpServerErrorException) endpointException.getCause();
//	                if ( httpException.getStatusCode().equals( HttpStatus.UNAUTHORIZED ) ) {
//	                    throw httpException;
//	                }
//	                
////	                throw endpointException;
//	            }
//	        }
//	    }
	}

	protected Object createResponseMessage(EndpointOperationContext context) {
		
		ServiceResponseMessage message = (ServiceResponseMessage) context.getResponseMessage();

		message.setStartTime(context.getStartTime());
		message.setDateTimeStamp(context.getStartTime());
		message.setTransactionId(context.getTransactionId());
		message.setMessageType(MSG_TYPE_SUCCESS);

		Throwable exception = context.getException();
		if (exception != null) {
			
			String error = String.format("%s:%s() error: %s", context.getServiceName(), context.getOperationName(), exception.getMessage());
			logger.error(error, exception);
			
			message.setMessageType(MSG_TYPE_FAILURE);
			message.setErrorCode(context.getAnnotation().errorCode());
			addMessage(context.getAnnotation().errorMessage(), message);
			
			StringWriter stringWriter = new StringWriter(1024);
			PrintWriter writer = new PrintWriter(stringWriter);
			exception.printStackTrace(writer);
			
			addMessage("Cause: " + stringWriter.toString(), message);
			
			writer.close();
			
			if (exception instanceof ApplicationException) {
				ApplicationException aex = (ApplicationException) exception;
				
				SubSystemErrorList subSystemErrors = new SubSystemErrorList();
				populateSubSystemErrorList (subSystemErrors, aex);			
				message.setSubSystemErrorList(subSystemErrors);
			}
		}
		
		ValidationResult validationResult = context.getValidationResult();
		if (validationResult.hasErrors()) {
			
			String format = "[%s] Error: [%s] - [%s]";
			
			EndpointOperation annotation = context.getAnnotation();
			
			message.setMessageType(MSG_TYPE_FAILURE);
			message.setErrorCode(annotation.errorCode());
			
			for (ValidationGroup group : validationResult.getErrorGroups()) {
				for (ValidationError error : group.getErrors()) {
					addMessage(String.format(format, group.getName(), error.getCode(), error.getMessage()), message);
				}
			}
		}
		
		if (StringUtils.equals(message.getMessageType(), MSG_TYPE_FAILURE)) {
			createRuntimeInfo(message);
			createContextInfo(message, context);
		}
		
		return context.getResponse();
	}
	
	private void addMessage(String message, ServiceResponseMessage response) {
		Message msg = new Message();
		msg.setMessage(message);
		response.getMessageList().add(msg);
	}
	
	protected EndpointException createException(EndpointOperationContext context) {

		EndpointException endpointException = null;
		
		Throwable cause = context.getException();
		
		if (cause instanceof EndpointSystemException) {
			
			FaultExceptionDetailsType faultInfo = new FaultExceptionDetailsType();

			faultInfo.setMessageId(context.getTransactionId());
			faultInfo.setErrorCode(context.getAnnotation().errorCode());
			
			EndpointOperation annotation = context.getAnnotation();
			
			String exceptionMessage = String.format("Error [%s] - [%s]", annotation.errorCode(), annotation.errorMessage());
			
			StringWriter stringWriter = new StringWriter(1024);
			PrintWriter writer = new PrintWriter(stringWriter);
			
			writer.println("*** Timestamp: ["+ context.getStartTime() + "] ***");
			writer.println("*** Service: [" + context.getServiceName() + "] ***");
			writer.println("*** Operation: [" + context.getOperationName() + "] ***");
			writer.println("*** Error code: [" + context.getAnnotation().errorCode() + "] ***");
			writer.println("*** Cause error message: [" + context.getException().getMessage() + "] ***");

			writer.println("*** Input parameters: ***");

			String[] parameterNames = context.getParameterNames();
			Object[] parameterValues = context.getParameterValues();
			
			for (int idx = 0; idx < parameterNames.length; idx++) {
				writer.print(" - [" + idx + "][" + parameterNames[idx] + "]: ");
				writer.println(parameterValues[idx]);
			}
			
			writer.println("*** Stack trace: [");
			context.getException().printStackTrace(writer);
			writer.println("] ***");
			
			faultInfo.setErrorMessage(stringWriter.toString());
			
			endpointException = new EndpointException(exceptionMessage, faultInfo);
		}
		
		return endpointException;
	}
	
	protected ServiceResponseMessage createContextInfo(ServiceResponseMessage message, EndpointOperationContext context) {
		
		String[] parameterNames = context.getParameterNames();
		Object[] parameterValues = context.getParameterValues();

		ContextMapList contextMapList = new ContextMapList();
		
		// check for parameter wrapper
		
		if (parameterValues != null && parameterValues.length == 1 && parameterValues[0] != null) {
			Object params = parameterValues[0];
			XmlType xmlTypeAnnotation = params.getClass().getAnnotation(XmlType.class);
			if (xmlTypeAnnotation != null) {
				if (StringUtils.isEmpty(xmlTypeAnnotation.name())) {
					for (PropertyDescriptor descriptor : BeanUtils.getPropertyDescriptors(params.getClass())) {
						if (descriptor.getName().equals("class")) {
							continue;
						}
						ContextMap pair = new ContextMap();
						pair.setName(descriptor.getName());
						Method getter = descriptor.getReadMethod();
						try {
							Object value = getter.invoke(params, new Object[0]); 
							pair.setValue(value == null ? "null" : value.toString());
						} catch (Exception e) {
							pair.setValue("Error invoking getter " + getter + ": " + e.getMessage());
						}
						contextMapList.getContextMap().add(pair);
					}
				}
			}
		} else {
			
			// use unwrapped parameters
			
			for (int idx = 0; idx < parameterNames.length; idx++) {
				
				ContextMap pair = new ContextMap();
				pair.setName(parameterNames[idx]);
				Object value = parameterValues[idx]; 
				pair.setValue(value == null ? "null" : value.toString());
				
				contextMapList.getContextMap().add(pair);
			}
		}
		
		message.setContextMapList(contextMapList);
		
		return message;
	}
	
	protected ServiceResponseMessage createRuntimeInfo(ServiceResponseMessage message) {
		
		message.setRuntimeInfo("Runtime info...");
		
		return message;
	}
	
	protected void populateSubSystemErrorList(SubSystemErrorList subSystemErrorList, ApplicationException aex) {
		if (aex != null) {
			populateSubSystemErrorList(subSystemErrorList, aex.getNestedException());
			
			SubSystemError subSystemError = new SubSystemError();
			subSystemError.setId(aex.getErrorCode());
			subSystemError.setMessage(aex.getErrorMessage());

			subSystemErrorList.getSubSystemError().add(subSystemError);
		}

	}

}
