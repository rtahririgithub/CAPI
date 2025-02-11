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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

import weblogic.application.ApplicationLifecycleEvent;

import com.telus.cmb.common.app.ApplicationInitializer;
import com.telus.cmb.common.app.ApplicationServiceLocator;

/**
 * @author Pavel Simonovsky
 *
 */
public class ReferenceApplicationInitializer extends ApplicationInitializer {

	private static final Log logger = LogFactory.getLog(ReferenceApplicationInitializer.class);

	@Override
	public void preStart(ApplicationLifecycleEvent evt) {
		super.preStart(evt);
		monitoringAgent.addShakedown("ReferenceDataHelper", new ReferenceDataHelperShakedown()); 
		monitoringAgent.addShakedown("ReferenceDataFacade", new ReferenceDataFacadeShakedown()); 
	}
	
	@Override
	public void postStart(ApplicationLifecycleEvent evt) {
		super.postStart(evt);
		
		JobDetail jobDetail = new JobDetail("ReferenceDataWarmUpJob", ReferenceDataWarmUpJob.class);
		Trigger trigger = TriggerUtils.makeMinutelyTrigger("ReferenceDataWarmUpTrigger");
		
		try {

			ApplicationServiceLocator.getInstance().getScheduler().scheduleJob(jobDetail, trigger);
			
		} catch (Exception e) {
			logger.error("Error scheduling reference data warmup job: " + e.getMessage(), e);
		}
	}
}
