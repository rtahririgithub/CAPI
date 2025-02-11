package com.telus.cmb.common.aspects;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.telus.cmb.common.util.ExceptionUtil;



/**
 * This aspect is for intercept CallableStatementCallback.doInCallableStatment () and do a retry upon receiving ORA-04069 error
 *
 */
@Aspect
public class JdbcCallableRetryAspect {

	private static final Log logger = LogFactory.getLog(JdbcCallableRetryAspect.class);
	
	@Pointcut("execution(* org.springframework.jdbc.core.CallableStatementCallback.doInCallableStatement(..))")
	public void doInCallableStatement(){}

	@Around("doInCallableStatement()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String methodFullName = signature.toLongString();
				
		try {
			return joinPoint.proceed();
			
		} catch (Throwable t0) {
			if ( ExceptionUtil.isRootCauseAsORA04068(t0)) {
				//in case of ORA-04068 error, we retry the transaction one more time.
				try { 
					logger.info("Encoutered ORA-04068 failure,  retry method: " + methodFullName );
					return joinPoint.proceed();
				} catch (Throwable t1) {
					throw  t1 ;
				}
			} else { 
				//if not ORA-04068, let it error out
				throw  t0 ;
			}
			
		} finally {
		}
	}

}
