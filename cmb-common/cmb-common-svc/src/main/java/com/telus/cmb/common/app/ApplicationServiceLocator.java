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

import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

import com.telus.cmb.common.logging.LoggingManager;
import com.telus.cmb.common.logging.LoggingManagerMBeanImpl;
import com.telus.cmb.common.perfmon.PerformanceMonitor;
import com.telus.cmb.common.perfmon.PerformanceMonitorMBeanImpl;
import com.telus.cmb.common.perfmon.PerformanceReportWriter;
import com.telus.cmb.common.perfmon.PerformanceStatisticsMonitor;
import com.telus.cmb.common.util.MethodInvocationJob;

/**
 * @author Pavel Simonovsky
 *
 */
public class ApplicationServiceLocator {
	
	private static final Log logger = LogFactory.getLog(ApplicationServiceLocator.class);

	private static ApplicationServiceLocator instance;
	
	private String applicationName = "default-application";
	
	private Scheduler scheduler;
	
	private PerformanceMonitor performanceMonitor;
	
	private ApplicationRuntimeHelper applicationRuntimeHelper;
	
	private LoggingManager loggingManager;
	
	private static String hostName ;
	
	private boolean perfMonJobScheduled = false;
	
	private Date lastPerfMonJobScheduleAttempt = null;
	
	private ApplicationServiceLocator() {
	}
	
	
	public static ApplicationServiceLocator getInstance() {
		if (instance == null) {
			instance = new ApplicationServiceLocator();
		}
		return instance;
	}
	
	public void dispose() {
		getApplicationRuntimeHelper().unregisterAllMBeans();
		
		try {
			getScheduler().shutdown();
		} catch (Exception e) {
			logger.error("Error shutting down application scheduler: " + e.getMessage(), e);
		}
		
		instance = null;
	}
	
	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
		getLoggingManager();
	}

	/**
	 * Returns a configured instance of application scheduler
	 * 
	 * @return {@link Scheduler}
	 */
	public Scheduler getScheduler() {
		if (scheduler == null) {
			
			Properties props = new Properties();
			
			props.put("org.quartz.scheduler.instanceName", "ApplicationScheduler");
			props.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
			props.put("org.quartz.threadPool.threadCount", "1");
			
			try {

				SchedulerFactory factory = new StdSchedulerFactory(props);
				scheduler = factory.getScheduler();
				
			} catch (Exception e) {
				throw new RuntimeException("Error configuring application scheduler", e);
			}
		}
		return scheduler; 
	}
	
	/**
	 * Returns a configured instance of performance monitor
	 * 
	 * @return {@link PerformanceMonitor}
	 */
	public PerformanceMonitor getPerformanceMonitor() {
		if (performanceMonitor == null) {
			
			performanceMonitor = new PerformanceStatisticsMonitor();
			
			schedulePerformanceMonitorJob(performanceMonitor);
			
			// register logging manager with MBean server
			try {
				String name = "com.telus.cmb.management:Name=" + getApplicationName() + ", Type=Performance";
				PerformanceMonitorMBeanImpl performanceMonitorMBean = new PerformanceMonitorMBeanImpl();
				getApplicationRuntimeHelper().registerMBean(performanceMonitorMBean, name);
				
			} catch (Exception e) {
				logger.error("Error registering Logging Manager MBean: " + e.getMessage());
			}
			
		}else {
			schedulePerformanceMonitorJob(performanceMonitor);
		}
		return performanceMonitor;
	}
	
	private void schedulePerformanceMonitorJob(PerformanceMonitor perfMon) {
		if (isSchedulePerfMonJobNow()) {
			perfMonJobScheduled = true;
			PerformanceReportWriter reportWriter = new PerformanceReportWriter();
			reportWriter.setMonitor(perfMon);
			reportWriter.setDirectoryName(getLoggingManager().getLoggingRoot());
			reportWriter.setBaseName("perfstats_" + getHostName() + "_");
			reportWriter.setExtension("txt");
			reportWriter.setMaxHistoryFiles(30);
			reportWriter.setTimestampPattern("-ddMM-HHmmss");

			JobDetail jobDetail = new JobDetail("PerformanceReportGenerationJob", MethodInvocationJob.class);

			jobDetail.getJobDataMap().put(MethodInvocationJob.JOB_TARGET_OBJECT, reportWriter);
			jobDetail.getJobDataMap().put(MethodInvocationJob.JOB_TARGET_METHOD, "writeReport");

			Trigger trigger = TriggerUtils.makeHourlyTrigger("PerformanceReportGenerationTrigger");
			trigger.setStartTime(TriggerUtils.getEvenHourDate(new Date()));

			// Trigger trigger = TriggerUtils.makeMinutelyTrigger(5);
			// trigger.setName("PerformanceReportGenerationTrigger");
			// trigger.setStartTime(TriggerUtils.getEvenMinuteDate(new Date()));

			try {
				getScheduler().scheduleJob(jobDetail, trigger);
			} catch (Exception e) {
				logger.error("Error scheduling performance monitor generation job", e);
				perfMonJobScheduled = false;
			}
		}
	}
	
	/**
	 * Returns true if performance monitor job wasn't scheduled yet. There's also a 60 minutes interval for retry.
	 * @return
	 */
	private boolean isSchedulePerfMonJobNow() {
		if (perfMonJobScheduled == false) {
			if (lastPerfMonJobScheduleAttempt == null || 
				(new Date().getTime() - lastPerfMonJobScheduleAttempt.getTime()) >= 1000 * 60 * 60) { 
				lastPerfMonJobScheduleAttempt = new Date(); 
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns a configured instance of application runtime helper
	 * 
	 * @return
	 */
	public ApplicationRuntimeHelper getApplicationRuntimeHelper() {
		if (applicationRuntimeHelper == null) {
			applicationRuntimeHelper = new ApplicationRuntimeHelper(applicationName, "devmonitor", "monitor1");
		}
		return applicationRuntimeHelper;
	}
	
	public LoggingManager getLoggingManager() {
		if (loggingManager == null) {
			
			loggingManager = new LoggingManager();
			loggingManager.configure(applicationName);
			
			// register logging manager with MBean server
			
			try {
				String name = "com.telus.cmb.management:Name=" + getApplicationName() + ", Type=Logging";
				LoggingManagerMBeanImpl loggingManagerMBean = new LoggingManagerMBeanImpl();
				getApplicationRuntimeHelper().registerMBean(loggingManagerMBean, name);
				
			} catch (Exception e) {
				logger.error("Error registering Logging Manager MBean: " + e.getMessage());
			}
		}
		return loggingManager;
	}
	
	public static String getHostName(){
		try{
			if(hostName == null){
				hostName =  java.net.InetAddress.getLocalHost().getHostName();
			}	
			return hostName;
		}catch(Exception ex){
			return "";
		}

	}
}
