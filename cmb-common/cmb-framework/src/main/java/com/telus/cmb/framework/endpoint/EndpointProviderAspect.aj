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

import java.util.Date;

import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.cmb.endpoint.EndpointException;
import com.telus.cmb.framework.application.ApplicationServiceLocator;
import com.telus.cmb.framework.identity.ClientIdentity;
import com.telus.cmb.framework.perfmon.MethodInvocationProfiler;


/**
 * @author Pavel Simonovsky
 *
 */
public aspect EndpointProviderAspect {

	private static final Logger logger = LoggerFactory.getLogger(EndpointProviderAspect.class);
	
	pointcut endpointOperation() : execution(@com.telus.cmb.framework.endpoint.EndpointOperation public * *.*(..) throws EndpointException);

	declare soft : ApplicationException : endpointOperation();

	declare soft : TelusAPIException : endpointOperation();

	Object around() throws EndpointException: endpointOperation() {

		MethodSignature signature = (MethodSignature) thisJoinPoint.getSignature();
		
		String methodFullName = signature.toLongString();
		
		String className = signature.getDeclaringType().getName();
		
		EndpointOperationContext context = new EndpointOperationContext();
		context.setAnnotation(signature.getMethod().getAnnotation(EndpointOperation.class));
		context.setParameterNames(signature.getParameterNames());
		context.setParameterValues(thisJoinPoint.getArgs());
		context.setServiceName(className);
		context.setOperationName(signature.getName());

		EndpointProvider endpoint = (EndpointProvider) thisJoinPoint.getTarget();

		ClientIdentity clientIdentity = new ClientIdentity("anonymous", "", "unknown");
		
		try {
			clientIdentity = endpoint.getClientIdentityProvider().getClientIdentity(endpoint.getServiceContext());
		} catch (Exception e) {
		}
		
		context.setClientIdentity(clientIdentity);
		
		Object result = null;
		
		try {
			result = signature.getReturnType().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to instantiate response class", e);
		}
		
		context.setResponse(result);
		
		endpoint.setOperationContext(context);
		
		MethodInvocationProfiler profiler = new MethodInvocationProfiler(className, methodFullName);
		
		logger.debug("Started [{}], transactionId [{}]", methodFullName, context.getTransactionId());
		
		try {
			
			profiler.start();
			result = proceed();
			profiler.stop();
			
		} catch (Exception e) {
			profiler.stop(e);
			context.setException(e);
		} 
		
		ApplicationServiceLocator.getInstance().getPerformanceMonitor().handleMethodInvocation(profiler);
		
		context.setEndTime( new Date());

		try {
			
			result = endpoint.getResponseBuilder().buildResponse(context);
			
		} catch (Exception e) {
		    if ( e instanceof HttpServerErrorException) {
		        throw (HttpServerErrorException)e;
		    } else {
		        throw (EndpointException) e;
		    }
			
		} finally {
			logger.debug("Finished [{}], time = [{}] msec.", methodFullName, profiler.getDuration());
		}
		
		return result;
	}
	
}
