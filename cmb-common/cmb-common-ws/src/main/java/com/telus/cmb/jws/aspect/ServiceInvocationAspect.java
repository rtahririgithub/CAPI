/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import com.telus.cmb.common.app.ApplicationServiceLocator;
import com.telus.cmb.common.perfmon.MethodInvocationProfiler;
import com.telus.cmb.jws.BaseService;
import com.telus.cmb.jws.ExceptionTranslationContext;
import com.telus.cmb.jws.ExceptionTranslator;
import com.telus.cmb.jws.ServiceBusinessOperation;
import com.telus.cmb.jws.ServiceNestedException;

/**
 * @author Pavel Simonovsky
 *
 */

@Aspect
public class ServiceInvocationAspect {
	
	private static final Log logger = LogFactory.getLog(ServiceInvocationAspect.class);

	/**
	 * The aroundServiceMethod advice method will be called for the target methods matches following
	 * join point declaration:
	 * 
	 *  1) public method with the @ServiceBusinessOperation annotation, 
	 *  2) declared in a type with the @WebService annotation, (M.Liao: WebService annotation is no longer a 
	 *     constraint for this join point: we start using class hierarchy to support service minor version evolution: one base service
	 *     class, who does not have WebService annotation, always implements the latest version of WDSL, each minor version just extends
	 *     base service class, and add WebService annotation. )  
	 *  3) and in a package  beginning with the com.telus.cmb.jws prefix.
	 *  
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("execution (@com.telus.cmb.jws.ServiceBusinessOperation * (com.telus.cmb.jws.*).*(..))")
	public Object aroundServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		
		String methodFullName = signature.toLongString();
		
		String className = signature.getDeclaringType().getName();
		String methodName = signature.getName();
		
		MethodInvocationProfiler profiler = new MethodInvocationProfiler(className, methodFullName);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Started " + methodFullName);
		}
		
		Object result = null;
		
		try {

			profiler.start();
			result = joinPoint.proceed();
			profiler.stop();
			
			return result;
			
		} catch (Throwable throwable) {

			Throwable cause = throwable;
			
			if (throwable instanceof ServiceNestedException) {
				cause = throwable.getCause();
			}
			
			profiler.stop(cause);
			
			BaseService service = (BaseService) joinPoint.getTarget();
			
			ServiceBusinessOperation methodAnnotation = signature.getMethod().getAnnotation(ServiceBusinessOperation.class);
			
			ExceptionTranslationContext translationContext = new ExceptionTranslationContext();
			
			translationContext.setErrorCode(methodAnnotation.errorCode());
			translationContext.setErrorMessage(methodAnnotation.errorMessage());
			translationContext.setFieldMask(methodAnnotation.fieldMask());
			translationContext.setParameterNames(signature.getParameterNames());
			translationContext.setParameterValues(joinPoint.getArgs());
			translationContext.setServiceName(className);
			translationContext.setOperationName(methodName);
			
			ExceptionTranslator translator = service.getExceptionTranslator();
			Exception exception = translator.translateException(cause, translationContext);
			
			if (exception instanceof com.telus.cmb.jws.PolicyException) {
				if (logger.isDebugEnabled()) {
					logger.debug("Service operation error:", exception); //there is no need to log PolicyException in production
				}
			}else {
				logger.error("Service operation error:", exception);
			}
			
			if (exception != null) {
				throw exception;
			} else {
				throw new RuntimeException("ExceptionTranslator implementetation error: translateException() method cannot return null.");
			}
			
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("Finished " + methodFullName + ", time = " + profiler.getDuration() + " msec.");
			}
			ApplicationServiceLocator.getInstance().getPerformanceMonitor().handleMethodInvocation(profiler);
		}
	}
	
}
