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

import java.util.Map;

import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.telus.cmb.endpoint.EndpointException;
import com.telus.cmb.framework.identity.ClientIdentity;
import com.telus.cmb.framework.identity.ClientIdentityProvider;
import com.telus.cmb.framework.validation.ValidationResult;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingResponse;

/**
 * @author Pavel Simonovsky
 *
 */
public class EndpointProvider extends SpringBeanAutowiringSupport {

	private ThreadLocal<EndpointOperationContext> operationContextHolder = new ThreadLocal<EndpointOperationContext>();

	private EndpointResponseBuilder responseBuilder = new EndpointResponseBuilderImpl();

	private PingResponseBuilder pingResponseBuilder = new PingResponseBuilder();

	@Autowired
	private ClientIdentityProvider clientIdentityProvider;

	@Resource
	protected WebServiceContext serviceContext;

	public void setResponseBuilder(EndpointResponseBuilder responseBuilder) {
		this.responseBuilder = responseBuilder;
	}

	public EndpointResponseBuilder getResponseBuilder() {
		return responseBuilder;
	}

	public void setOperationContext(EndpointOperationContext context) {
		operationContextHolder.set(context);
	}

	public WebServiceContext getServiceContext() {
		return serviceContext;
	}

	public ClientIdentityProvider getClientIdentityProvider() {
		return clientIdentityProvider;
	}

	public EndpointOperationContext getOperationContext() {
		return operationContextHolder.get();
	}

	public void setClientIdentityProvider(ClientIdentityProvider clientIdentityProvider) {
		this.clientIdentityProvider = clientIdentityProvider;
	}

	protected String getSessionId(ClientIdentity identity, Object target) {
        return getOperationContext().getSessionId(identity, target);
    }
	
	protected String getSessionId(Object target) {
		return getOperationContext().getSessionId(target);
	}
	
	protected String getSessionId(String sessionId, ClientIdentity clientIdentity, Object target) {
        return StringUtils.isNotEmpty(sessionId) ? sessionId : getSessionId(clientIdentity, target);
    }

	protected String getSessionId(String sessionId, Object target) {
		return StringUtils.isNotEmpty(sessionId) ? sessionId : getSessionId(target);
	}

	protected ClientIdentity getClientIdentity() {
		return getOperationContext().getClientIdentity();
	}

	protected ValidationResult getValidationResult() {
		return getOperationContext().getValidationResult();
	}

	protected boolean hasErrors() {
		return getValidationResult().hasErrors();
	}

	protected void addError(String code, String message) {
		getValidationResult().addError(code, message);
	}

	@SuppressWarnings("unchecked")
	protected <T> T respond(Object responseMessage) {
		T response = (T) getOperationContext().getResponse();
		EndpointUtils.setValue(response, responseMessage);
		return (T) response;
	}

	public Map<String, Object> enumerateRuntimeResources(Map<String, Object> resources) {
		return resources;
	}

	public PingResponse ping(Ping request) throws EndpointException {
		return pingResponseBuilder.buildResponse(request, this);
	}

	public com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse ping(com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping request) throws EndpointException {
		return pingResponseBuilder.buildResponse(request, this);
	}

}
