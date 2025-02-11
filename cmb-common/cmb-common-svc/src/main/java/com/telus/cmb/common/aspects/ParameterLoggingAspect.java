package com.telus.cmb.common.aspects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.telus.cmb.common.logging.ExceptionParamLogger;


@Aspect
public class ParameterLoggingAspect {

	@Pointcut("execution(* com.telus.cmb.account..*(..)) " +
			"|| execution(* com.telus.cmb.subscriber..*(..)) " +
			"|| execution(* com.telus.cmb.productequipment..*(..)) " +
			"|| execution(* com.telus.cmb.reference..*(..)) " +
			"|| execution(* com.telus.cmb.utility..*(..))")
	public void allEJBs() {}


	@AfterThrowing(value="allEJBs()", throwing="t")
	public void afterThrowing(JoinPoint thisJoinPoint, Throwable t) {
		Log logger = LogFactory.getLog(thisJoinPoint.getSourceLocation().getWithinType());
		ExceptionParamLogger.logError(logger, thisJoinPoint, t);
	}
}
