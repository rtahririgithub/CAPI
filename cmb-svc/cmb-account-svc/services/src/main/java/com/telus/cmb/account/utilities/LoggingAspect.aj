package com.telus.cmb.account.utilities;



import java.util.Date;

import org.apache.log4j.Logger;

import com.telus.cmb.common.logging.ExceptionParamLogger;


public aspect LoggingAspect {
	
//	Object around() : allScope() {			
//		Logger logger = Logger.getLogger(thisJoinPoint.getSourceLocation().getWithinType());
//		String signatureLong = thisJoinPoint.getSignature().toLongString();
//		
//		Object returnValue = null;
//		
//		Date startTime = new Date();
//		logger.debug("Start: " + signatureLong);	
//		try {
//			
//			returnValue = proceed();
//		} finally {
//			Date endTime = new Date();
//
//
//			long executionTime = endTime.getTime() - startTime.getTime();
//			logger.debug("Stop: " + executionTime + " ms - " + signatureLong);
//		}
//		
//		return returnValue;
//	}
//	
//	pointcut allScope():
//		execution(* com.telus.cmb.account.lifecyclefacade.svc.*.* (..)) ||
//		execution(* com.telus.cmb.dependency.*.* (..)) ||
//		execution(* com.telus.cmb.account.informationhelper.dao.*.* (..)) || 
//		execution(* com.telus.cmb.account.informationhelper.svc.*.* (..));
	
}
