package com.telus.cmb.common.svc;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemException;
import com.telus.cmb.common.perfmon.MethodInvocationProfiler;

public abstract class NewSvcInvocationInterceptor extends SvcInvocationInterceptor{
	
	public abstract String getSystemCode();
	
	private static final Log LOGGER = LogFactory.getLog(NewSvcInvocationInterceptor.class);
	
	@AroundInvoke
	@Override
	public Object handleMethodInvocation(InvocationContext invocationContext) throws Exception {
		
		Method targetMethod = invocationContext.getMethod();
		Class<?> targetClass = targetMethod.getDeclaringClass();
		
		Object[] params = invocationContext.getParameters();
		
		
		String methodFullName = targetClass.getName() + "." + targetMethod.getName() + "(" + params.length + " args)...";
		
		MethodInvocationProfiler profiler = new MethodInvocationProfiler(targetClass.getName(), targetMethod.toGenericString());
		
		LOGGER.debug("Started " + methodFullName);
		
		Object result = null;

		try {
			
			profiler.start();
			result = invocationContext.proceed();
			profiler.stop();
		
			return result;
			
		} catch (ApplicationException e) {			
			profiler.stop(e);
			LOGGER.debug(methodFullName + " ApplicationException invocation error " + e.getMessage(), e);  //ApplicationException is checked error and should not be logged as an error
			if (e.getCause() == null) {
				//Aug 24, 2011
				//this is a workaround to include the stack trace within the EJB/DAO layer. When ApplicationException is thrown to the client
				//without a cause. The stack trace elements are lost and therefore we need to recreate the ApplicationException and set the original
				//as a cause explicitly.
				ApplicationException ae = new ApplicationException (e.getSystemCode(), e.getErrorCode(), e.getErrorMessage(), e.getErrorMessageFr(), e.getNestedException());
				ae.initCause(e);
				throw ae;
			}else {
				throw e;
			}
		} catch (SystemException se) {
			profiler.stop(se);
			LOGGER.error(methodFullName + " SystemException invocation error " + se.getMessage(), se);
			throw se;
		} catch (Throwable t) {
			profiler.stop(t);
			LOGGER.error(methodFullName + " invocation error " + t.getMessage(), t);
			ByteArrayOutputStream os = new ByteArrayOutputStream(16 * 1024);
			PrintStream ps = new PrintStream(os);
			t.printStackTrace(ps);
			
			/** SystemException is RuntimeException and it's always wrapped with RemoteException or EJBException. 
			 * The printStackTrace() method in SystemException therefore has no use on printing the stack trace that's thrown to this point.
			 * By getting the stack trace and set as the messagge in SystemException's constructor. This detail can be preserved when
			 * printing the stack trace.
			 * 
			 * We cannot just do SystemException.initCause(t) because Throwable may contain exception classes that consumer does not have the
			 * library in classpath. For example, it may contain chain of Spring framework exception while the client does not have any
			 * Spring library to interpret a Spring exception class.
			 * */
			SystemException nestedException = new SystemException (getSystemCode(), os.toString(), "", t); 
			SystemException se = new SystemException(getSystemCode(), ErrorCodes.GENERIC_THROWABLE_ERROR_CODE, t.getMessage(), "", t);
			se.initCause(nestedException);
			throw se;			
		} finally {
			long duration = profiler.getDuration();
			if (duration > 10000) {
				LOGGER.info("[ALERT] Finished " + methodFullName + ", time = " + duration + " msec.");
			}else if (logger.isDebugEnabled()) {
				LOGGER.debug("Finished " + methodFullName + ", time = " + duration + " msec.");
			}
			getPerformanceMonitor().handleMethodInvocation(profiler);
		}
	}
	
	

}
