package com.telus.cmb.reference.utilities;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.jdbc.UncategorizedSQLException;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.aspects.AbstractMonitoringHelper;

@Aspect
public class DaoMonitoringAspect extends AbstractMonitoringHelper {

	private static final Log logger = LogFactory.getLog(DaoMonitoringAspect.class);

	@Pointcut("execution(* com.telus.cmb.reference.dao.*.*(..))")
	public void referenceEJBDaos() {
	}

	@Pointcut("execution(* com.telus.cmb.common.dao.BaseJdbcDao+.*(..))")
	public void baseJdbcDaos() {
	}

	@Pointcut("execution(* com.telus.cmb.common.dao.BaseSvcClientDao+.*(..))")
	public void baseSvcClientDaos() {
	}

	// TODO: When the aspects in common-svc are sorted out, we can combine all the DaoMonitoring Aspects into one
	@Around("baseJdbcDaos() || baseSvcClientDaos() || referenceEJBDaos()")
	public Object aroundJdbcMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		return invokeMethod(joinPoint);
	}

	@Override
	protected Log getLogger() {
		return logger;
	}

	@Override
	protected void logError(String methodFullName, Throwable t) {

		if (t instanceof UncategorizedSQLException) {
			SQLException sqlException = ((UncategorizedSQLException) t).getSQLException();
			if (sqlException.getErrorCode() == 20201) { // do not log complete stack trace for price plan not found error
				return;
			}
		}

		if (t instanceof ApplicationException) {
			if (logger.isDebugEnabled()) {
				logger.debug(methodFullName + " invocation error " + t.getMessage(), t);
			}
		} else {
			logger.error(methodFullName + " invocation error " + t.getMessage(), t);
		}
	}

}
