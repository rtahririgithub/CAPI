/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

import com.telus.cmnb.armx.agent.ApplicationMonitoringAgent;
import com.telus.cmnb.armx.agent.ApplicationMonitoringAgentFactory;
import com.telus.framework.config.ConfigContext;

/**
 * @author Pavel Simonovsky
 *
 */
public class ApplicationContextListener implements ApplicationListener<ApplicationContextEvent> {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationContextListener.class);
	
	private ApplicationMonitoringAgent monitoringAgent;
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		
		if (event instanceof ContextClosedEvent || event instanceof ContextStoppedEvent) {
			handleContextStop();
		} else if (event instanceof ContextRefreshedEvent || event instanceof ContextStartedEvent) {
			handleContextStart();
		} else {
			logger.warn("Unknown context event: [{}]", event);
		}
	}

	protected void handleContextStart() {

		String applicationId = getApplicationId();
		String applicationVersion = getApplicationVersion();
		
		ApplicationServiceLocator serviceLocator = ApplicationServiceLocator.getInstance();
		serviceLocator.setApplicationName(applicationId);
		
		if (monitoringAgent == null) {
			monitoringAgent = ApplicationMonitoringAgentFactory.createApplicationMonitoringAgent();
			monitoringAgent.addModuleVersion(applicationId, applicationVersion);
		}
		
		monitoringAgent.start();
		
		logger.info("Application [{}, version {}] started successfully", applicationId, applicationVersion);
	}
	
	protected void handleContextStop() {
		
		String applicationId = getApplicationId();
		String applicationVersion = getApplicationVersion();

		if (monitoringAgent != null) {
			monitoringAgent.stop();
		}

		logger.info("Application [{}, version {}] stopped successfully", applicationId, applicationVersion);
	}
	
	private String getApplicationVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}
	
	private String getApplicationId() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(ConfigContext.getApplicationId()).append("-");
		buffer.append(ConfigContext.getApplicationVersion());
		
		return buffer.toString();
	}
	
}
