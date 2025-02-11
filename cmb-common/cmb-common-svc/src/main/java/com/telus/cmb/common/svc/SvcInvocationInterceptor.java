/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.svc;

import java.lang.reflect.Method;
import java.sql.SQLException;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.cmb.common.app.ApplicationServiceLocator;
import com.telus.cmb.common.perfmon.MethodInvocationProfiler;
import com.telus.cmb.common.perfmon.PerformanceMonitor;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.exception.TelusSystemException;
import com.telus.eas.framework.info.ExceptionInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class SvcInvocationInterceptor {

	protected static final Log logger = LogFactory.getLog(SvcInvocationInterceptor.class);

	private PerformanceMonitor performanceMonitor;

	protected PerformanceMonitor getPerformanceMonitor() {
		if (performanceMonitor == null) {
			performanceMonitor = ApplicationServiceLocator.getInstance().getPerformanceMonitor();
		}
		return performanceMonitor;
	}
	
	@AroundInvoke
	public Object handleMethodInvocation(InvocationContext invocationContext) throws Exception {
		
		Method targetMethod = invocationContext.getMethod();
		Class<?> targetClass = targetMethod.getDeclaringClass();
		
		Object[] params = invocationContext.getParameters();
		
		String methodFullName = targetClass.getName() + "." + targetMethod.getName() + "(" + params.length + " args)...";
		
		MethodInvocationProfiler profiler = new MethodInvocationProfiler(targetClass.getName(), targetMethod.toGenericString());
		
		logger.debug("Started " + methodFullName);
		
		Object result = null;

		try {
			
			profiler.start();
			result = invocationContext.proceed();
			profiler.stop();
		
			return result;
			
		} catch (Throwable t) {
			profiler.stop(t);
			logError (methodFullName, t);
			
			throw translateException( t );
			
		} finally {
			long duration = profiler.getDuration();
			if (duration > 10000) {
				logger.info("[ALERT] Finished " + methodFullName + ", time = " + duration + " msec.");
			}else if (logger.isDebugEnabled()) {
				logger.debug("Finished " + methodFullName + ", time = " + duration + " msec.");
			}
			getPerformanceMonitor().handleMethodInvocation(profiler);
		}
	}
	
	protected void logError (String methodFullName, Throwable t) {
		if (t instanceof DataAccessException) {
			Throwable cause = ((DataAccessException) t).getMostSpecificCause();
			if (cause instanceof SQLException) {
				SQLException sqlex = (SQLException) cause;
				if (sqlex.getErrorCode() == 20201) { //do not log complete stack trace for price plan not found error
					return;
				}
			}
		}else if (t instanceof ApplicationException) {
			return;
		}
		
		logger.error(methodFullName + " invocation error " + t.getMessage(), t);
	}
	
	protected Exception translateException(Throwable t) {
		
		if (t instanceof TelusException ) {
			return (TelusException) t;
		}
		else if (t instanceof ApplicationException ) {
			return (Exception) t;
		}
		else if (t instanceof SystemException ) {
			return (Exception) t;
		}
		else if ( t instanceof DataAccessException) {
			return translateDataAccessException( (DataAccessException) t );
		}
		else {
			return translateThrowable( t );
		}
	}

	protected Exception translateThrowable( Throwable t) {
		ExceptionInfo exceptionInfo = new ExceptionInfo("SYS00003", 
				t.getMessage() == null ? "Method invocation error (" + t.toString() + ") - see stack trace for details" : t.getMessage(), 
				t);
		return new TelusSystemException(exceptionInfo);
	}
	
	protected Exception translateDataAccessException( DataAccessException ex) {
		
		Throwable cause = ex.getMostSpecificCause();
		
		if (cause instanceof SQLException) {
			
			SQLException sqlex = (SQLException) cause;
			return new TelusException (String.valueOf(sqlex.getErrorCode()), sqlex.getMessage(), sqlex);
			
		} else {
			return translateThrowable( cause );
		}
	}
	
}
