package com.telus.cmb.account.utilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.telus.cmb.common.aspects.AbstractMonitoringHelper;

@Aspect
public class DaoMonitoringAspect extends AbstractMonitoringHelper {

	private static final Log logger = LogFactory.getLog(DaoMonitoringAspect.class);

	@Pointcut("execution(* com.telus.cmb.account.*.dao.*.*(..))")
	public void accountEJBDaos() {
	}

	@Pointcut("execution(* com.telus.cmb.common.dao.BaseJdbcDao+.*(..))")
	public void baseJdbcDaos() {
	}

	@Pointcut("execution(* com.telus.cmb.common.dao.BaseSvcClientDao+.*(..))")
	public void baseSvcClientDaos() {
	}

	// TODO: When the aspects in common-svc are sorted out, we can combine all the DaoMonitoring Aspects into one
	@Around("baseJdbcDaos() || baseSvcClientDaos() || accountEJBDaos()")
	public Object aroundJdbcMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		return invokeMethod(joinPoint);
	}

	@Override
	protected Log getLogger() {
		return logger;
	}

}
