/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.util;

import org.apache.commons.beanutils.MethodUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Qaurtz Job implementation which delegated job execution to
 * the target object using specified target method.
 * 
 *  Both target object and method name are passed to execute() methos
 *  using {@link JobDataMap} under JOB_TARGET_OBJECT and JOB_TARGET_METHOD keys 
 *  correspondingly.
 * 
 * @author Pavel Simonovsky
 *
 */
public class MethodInvocationJob implements Job {

	public static final String JOB_TARGET_OBJECT = "jobTargetObject";

	public static final String JOB_TARGET_METHOD = "jobTargetMethod";
	
	
	/*
	 * (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext executionContext) throws JobExecutionException {
		
		JobDataMap dataMap = executionContext.getMergedJobDataMap();
		
		Object object = dataMap.get(JOB_TARGET_OBJECT);
		String methodName = dataMap.getString(JOB_TARGET_METHOD);
		
		if (object != null && methodName != null) {
			try {

				MethodUtils.invokeMethod(object, methodName, null);

			} catch (Throwable t) {
				throw new JobExecutionException("Job execution error", t);
			}
		}
	}
	
}
