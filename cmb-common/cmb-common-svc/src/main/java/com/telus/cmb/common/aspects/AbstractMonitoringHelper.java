package com.telus.cmb.common.aspects;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.app.ApplicationServiceLocator;
import com.telus.cmb.common.perfmon.MethodInvocationProfiler;
import com.telus.cmb.common.util.ExceptionUtil;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public abstract class AbstractMonitoringHelper {

	protected static final Logger slaFailureLogger = Logger.getLogger("slaFailureLogger");
//	protected static final Logger methodInvocationLogger = LoggerFactory.getLogger("methodInvocationLogger");
	protected static final Map<String, Logger> methodInvocationLoggerMap = new HashMap<String, Logger>();
	protected static final long DEFAULT_SLA = 1000;
	private static final String DEFAULT_METHODINVOCATION_LOGGERNAME = "methodInvocationLogger";
		
	protected abstract Log getLogger();
	
	protected Object invokeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		Logger methodInvocationLogger = null;
		
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String methodFullName = signature.toLongString();
		String className = signature.getDeclaringType().getName();
		Object[] args = joinPoint.getArgs();
		MethodInvocationProfiler profiler = new MethodInvocationProfiler(className, methodFullName);
		LogInvocation logInvocationAnn = signature.getMethod().getAnnotation(LogInvocation.class);
			
		if (logInvocationAnn != null) {
			methodInvocationLogger = getLogInvocationLogger(logInvocationAnn);
		}

		if (getLogger().isDebugEnabled()) {
			getLogger().debug("Started " + methodFullName);
			logBanAndSubscriber(args);
		}
		
		Object result = null;
		try {
			logInvocationStart(methodInvocationLogger, methodFullName, args);
			profiler.start();
			result = joinPoint.proceed();
			profiler.stop();
			logInvocationEnd(methodInvocationLogger, methodFullName, args);

			return result;
		} catch (Throwable t) {

			profiler.stop(t);
			if ( getLogger().isDebugEnabled() == false) {
				logBanAndSubscriber(args);
			}
			logError(methodFullName, t);
			if (ExceptionUtil.isRootCauseAsORA04068(t)) { //try again
				try {
					
					getLogger().info("Retrying " + methodFullName);
					profiler.start();
					result = joinPoint.proceed();
					profiler.stop();
					getLogger().info("Retry successful " + methodFullName);

					return result;
				} catch (Throwable t2) {
					logError("Retry failed " + methodFullName, t2);
					throw t2;
				}
			}

			throw t;
		} finally {

			long duration = profiler.getDuration();
			long sla = getSLA(signature);
			if (duration > sla) {
				logSLAFailure(duration, sla, methodFullName, args);
			} 
			
			if (getLogger().isDebugEnabled()) {
				getLogger().debug("Finished " + methodFullName + ", time = " + duration + " msec.");
			}

			ApplicationServiceLocator.getInstance().getPerformanceMonitor().handleMethodInvocation(profiler);
		}
	}	
	
	protected void logError(String methodFullName, Throwable t) {
		
		if (t instanceof ApplicationException) {
			if (getLogger().isDebugEnabled()) {
				getLogger().debug(methodFullName + " invocation error " + t.getMessage(), t);
			}
		} else {
			getLogger().error(methodFullName + " invocation error " + t.getMessage(), t);
		}
	}
	
	protected void logBanAndSubscriber(Object[] args) {
		for (Object argument : args) {
			if (argument instanceof AccountInfo) {
				int banId = ((AccountInfo) argument).getBanId();
				getLogger().error("Found AccountInfo ban=[" + banId + "]");
			} else if (argument instanceof SubscriberInfo) {
				String subscriberNo = ((SubscriberInfo) argument).getSubscriberId();
				int banId = ((SubscriberInfo) argument).getBanId();
				getLogger().error("Found SubscriberInfo ban=[" + banId + "] subscriberNo=[" + subscriberNo + "]");
			}
		}
	}
	
	protected long getSLA(MethodSignature signature) {
		return getSLA(signature, DEFAULT_SLA);
	}
	
	protected long getSLA(MethodSignature signature, long defaultSLA) {		

		Annotation annotations[] = signature.getMethod().getAnnotations();		
		for (Annotation annotation : annotations) {
			if (annotation instanceof ExpectedSLA) {
				return ((ExpectedSLA) annotation).value();
			}
		}
		
		return defaultSLA;
	}
	
	protected String convertObjectsToString(Object[] objects) {
		
		if (objects != null) {			
			StringBuilder sb = new StringBuilder();
			for (Object object : objects) {
				if (object != null) {
					if (object instanceof String || object instanceof Collection) {
						// For some reason simple style looks weird for String objects
						sb.append(object);
					} else {
						sb.append(new ReflectionToStringBuilder(object, ToStringStyle.SHORT_PREFIX_STYLE).build().replace("\n", ""));
					}					
				} else {
					sb.append("<null>");					
				}
				sb.append(";");
			}			
			return sb.toString();
		}
		
		return "<null>";
	}

	protected void logSLAFailure(long executionTime, long slaTime, String methodFullName, Object[] args) {
		slaFailureLogger.info(executionTime + "|" + slaTime + "|" + methodFullName + "|" + convertObjectsToString(args));
	}
	
	protected void logInvocationStart (Logger methodInvocationLogger, String methodFullName, Object[] args) {
		if (methodInvocationLogger != null) {
			methodInvocationLogger.info("begin " + methodFullName +"|" + convertObjectsToString(args));
		}
	}
	
	protected void logInvocationEnd (Logger methodInvocationLogger, String methodFullName, Object[] args) {
		if (methodInvocationLogger != null) {
			methodInvocationLogger.info("end " + methodFullName +"|" + convertObjectsToString(args));
		}
	}
	
	private Logger getLogInvocationLogger(LogInvocation logInvocationAnn) {
		Logger methodInvocationLogger = null;
		if (logInvocationAnn != null) {
			String loggerName = logInvocationAnn.loggerName();
			if (loggerName == null || loggerName.trim().isEmpty()) {
				loggerName = DEFAULT_METHODINVOCATION_LOGGERNAME;
			}
			methodInvocationLogger = methodInvocationLoggerMap.get(loggerName);

			if (methodInvocationLogger == null) {
				methodInvocationLogger = Logger.getLogger(loggerName);
				methodInvocationLoggerMap.put(loggerName, methodInvocationLogger);
			}
		}

		return methodInvocationLogger;
	}
	
}
