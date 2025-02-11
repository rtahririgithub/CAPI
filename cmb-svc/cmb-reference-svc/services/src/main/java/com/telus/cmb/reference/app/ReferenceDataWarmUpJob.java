/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.app;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author Pavel Simonovsky
 *
 */
public class ReferenceDataWarmUpJob implements Job {
	
	/*
	 * (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext jobContext) throws JobExecutionException {

//		logger.debug("Preloading reference data...");
//		
//		try {
//
//			InitialContext initialContext = new InitialContext();
//			Object obj = initialContext.lookup("ReferenceDataFacade#com.telus.cmb.reference.svc.impl.ReferenceDataFacadeHome");
//			
//			
//			logger.info(obj);
//			
//			initialContext.close();
//			
//		} catch (Exception e) {
//			logger.error(e);
//		}
		
		
	}
}
