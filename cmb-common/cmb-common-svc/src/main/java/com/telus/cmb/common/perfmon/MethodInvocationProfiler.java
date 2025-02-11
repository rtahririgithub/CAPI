/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.perfmon;

import java.lang.reflect.Method;

/**
 * @author Pavel Simonovsky
 *
 */
public class MethodInvocationProfiler {
	
	public static final long UNDEFINED_DURATION = -1;
	
	private String className;
	
	private String methodName;
	
	private long startTime;
	
	private long stopTime;
	
	private Throwable error;
	
	public MethodInvocationProfiler(String className, String methodName) {
		this.className = className;
		this.methodName = methodName;
	}

	public MethodInvocationProfiler(Class<?> clazz, Method method) {
		this(clazz.getName(), method.getName());
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	public void stop(Throwable error) {
		stopTime = System.currentTimeMillis();
		this.error = error;
	}

	public void stop() {
		stop(null);
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @return the error
	 */
	public Throwable getError() {
		return error;
	}
	
	public long getDuration() {
		return isCompleted() ? stopTime - startTime : UNDEFINED_DURATION; 
	}
	
	public boolean isCompleted() {
		return startTime != 0 && stopTime != 0;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @return the stopTime
	 */
	public long getStopTime() {
		return stopTime;
	}
	
}
