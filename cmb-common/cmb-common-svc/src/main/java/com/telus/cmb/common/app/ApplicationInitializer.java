/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import weblogic.application.ApplicationException;
import weblogic.application.ApplicationLifecycleEvent;
import weblogic.application.ApplicationLifecycleListener;

import com.telus.cmnb.armx.agent.ApplicationMonitoringAgent;
import com.telus.cmnb.armx.agent.ApplicationMonitoringAgentFactory;
import com.telus.framework.config.ConfigContext;


/**
 * @author Pavel Simonovsky
 *
 */
public class ApplicationInitializer extends ApplicationLifecycleListener {

	private static final Log logger = LogFactory.getLog(ApplicationInitializer.class);
	
	protected ApplicationMonitoringAgent monitoringAgent;
	
	private ApplicationServiceLocator serviceLocator = ApplicationServiceLocator.getInstance();
	
	/*
	 * (non-Javadoc)
	 * @see weblogic.application.ApplicationLifecycleListener#preStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	public void preStart(ApplicationLifecycleEvent evt) {
		String applicationId = evt.getApplicationContext().getApplicationId();

		serviceLocator.setApplicationName(applicationId);
		
		logger.info("Preparing application to start [" + applicationId + ", version(" + getVersion() + ")]...");
		
		if (monitoringAgent == null) {
			monitoringAgent = ApplicationMonitoringAgentFactory.createApplicationMonitoringAgent();
			monitoringAgent.addModuleVersion(applicationId, getVersion());
		}

		logger.info("Application state [" + evt.getApplicationContext().getApplicationId() + "] changed to STARTING.");
	}
	
	/*
	 * (non-Javadoc)
	 * @see weblogic.application.ApplicationLifecycleListener#postStart(weblogic.application.ApplicationLifecycleEvent)
	 */
	public void postStart(ApplicationLifecycleEvent evt) {
		try {
			serviceLocator.getScheduler().start();
			monitoringAgent.start();
		} catch (Exception e) {
			logger.error("Error starting application: " + e.getMessage(), e);
			throw new RuntimeException("Error starting application ", e);
		}
		logger.info("Application state [" + evt.getApplicationContext().getApplicationId() + "] changed to STARTED.");
	}	
	
	@Override
	public void preStop(ApplicationLifecycleEvent evt) throws ApplicationException {
		try {
			monitoringAgent.stop();
			serviceLocator.getScheduler().standby();
		} catch (Exception e) {
			logger.error("Error pausing scheduler: " + e.getMessage(), e);
		}
		logger.info("Application state [" + evt.getApplicationContext().getApplicationId() + "] changed to STANDBY.");

	}
	
	@Override
	public void postStop(ApplicationLifecycleEvent evt) {
		serviceLocator.dispose();
		logger.info("Application state [" + evt.getApplicationContext().getApplicationId() + "] changed to STOPPED.");
	}
	
	private String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}

}
