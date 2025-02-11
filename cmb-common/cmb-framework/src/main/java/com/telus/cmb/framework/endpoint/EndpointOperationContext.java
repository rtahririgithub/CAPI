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

import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telus.cmb.framework.identity.ClientIdentity;
import com.telus.cmb.framework.validation.ValidationResult;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.ResponseMessage;


/**
 * @author Pavel Simonovsky
 *
 */
public class EndpointOperationContext {
	
	private static final Logger logger = LoggerFactory.getLogger(EndpointOperationContext.class);

	private EndpointOperation annotation;
	
	private ClientIdentity clientIdentity;
	
	private String transactionId;
	
	private Date startTime;
	
	private Date endTime;
	
	private String serviceName;
	
	private String operationName;
	
	private Object[] parameterValues;
	
	private String[] parameterNames;
	
	private ValidationResult validationResult = new ValidationResult();

	private Object response;
	
	private Throwable exception;
	
	private String sessionId;
	
	public EndpointOperationContext() {
		this.transactionId = UUID.randomUUID().toString();
		this.startTime = new Date();
	}
	
	public EndpointOperation getAnnotation() {
		return annotation;
	}

	public void setAnnotation(EndpointOperation annotation) {
		this.annotation = annotation;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public Object[] getParameterValues() {
		return parameterValues;
	}

	public void setParameterValues(Object[] parameterValues) {
		this.parameterValues = parameterValues;
	}

	public String[] getParameterNames() {
		return parameterNames;
	}

	public void setParameterNames(String[] parameterNames) {
		this.parameterNames = parameterNames;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public ValidationResult getValidationResult() {
		return validationResult;
	}
	
	public boolean hasErrors() {
		return validationResult.hasErrors();
	}	
	
	public Object getResponse() {
		return response;
	}
	
	public void setResponse(Object response) {
		this.response = response;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}
	
	/**
	 * @return the clientIdentity
	 */
	public ClientIdentity getClientIdentity() {
		return clientIdentity;
	}

	/**
	 * @param clientIdentity the clientIdentity to set
	 */
	public void setClientIdentity(ClientIdentity clientIdentity) {
		this.clientIdentity = clientIdentity;
	}

	public String getSessionId(Object target) {
		return getSessionId( getClientIdentity(), target );
	}
	
	public String getSessionId(ClientIdentity identity, Object target) {
	    if (sessionId == null) {
            try {
                if (identity == null) {
                    throw new RuntimeException("Client Identity is null");
                } 
                Method method = target.getClass().getMethod("openSession", String.class, String.class, String.class);
                if (method == null) {
                    throw new RuntimeException("Method for client identity is null");
                }
                sessionId = (String) method.invoke(target, identity.getPrincipal(), identity.getCredential(), identity.getApplication());
            } catch (Exception e) {
                String message = "Unable to open sessionId using proxy [" + target + "]: " + ExceptionUtils.getRootCauseMessage(e);
                logger.error(message, e);
                throw new RuntimeException(e);
            }
	    }
	    return sessionId;
	}
	
	public ResponseMessage getResponseMessage() {

		ResponseMessage message = null;
		
		if (response instanceof ResponseMessage) {
			message = (ResponseMessage) response;
		} else {
			message = EndpointUtils.getValue(response, ResponseMessage.class);
			if (message == null) {
				message = EndpointUtils.newPropertyValue(response, ResponseMessage.class);
			}
		}
		return message;
	}
}
