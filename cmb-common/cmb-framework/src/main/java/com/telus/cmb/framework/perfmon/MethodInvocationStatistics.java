/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.perfmon;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author Pavel Simonovsky
 * 
 */
public class MethodInvocationStatistics {

	private String className;

	private String methodName;

	private long firstInvocationTime;

	private long lastInvocationTime;

	private long minimumInvocationTime = Long.MAX_VALUE;

	private long maximumInvocationTime = Long.MIN_VALUE;

	private long totalInvocationTime;

	private long numberOfSuccessfullInvocations;

	private long numberOfUnsuccessfullInvocations;
	
	private Date startTime = null;
	
	private int GMTOffset = 0;
	
	public Date getStartTime() {
		return startTime;
	}

	public int getGMTOffset() {
		return GMTOffset;
	}

	public String getName() {
		return className + "." + methodName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public long getFirstInvocationTime() {
		return firstInvocationTime;
	}

	public long getLastInvocationTime() {
		return lastInvocationTime;
	}

	/**
	 * @return the minimumInvocationTime
	 */
	public long getMinimumInvocationTime() {
		return minimumInvocationTime;
	}

	/**
	 * @return the maximumInvocationTime
	 */
	public long getMaximumInvocationTime() {
		return maximumInvocationTime;
	}

	/**
	 * @return the totalInvocationTime
	 */
	public long getTotalInvocationTime() {
		return totalInvocationTime;
	}

	/**
	 * @return the totalNumberOfInvocations
	 */
	public long getTotalNumberOfInvocations() {
		return numberOfSuccessfullInvocations + numberOfUnsuccessfullInvocations;
	}

	/**
	 * @return the numberOfSuccessfullInvocations
	 */
	public long getNumberOfSuccessfullInvocations() {
		return numberOfSuccessfullInvocations;
	}

	/**
	 * @return the numberOfUnsuccessfullInvocations
	 */
	public long getNumberOfUnsuccessfullInvocations() {
		return numberOfUnsuccessfullInvocations;
	}

	public long getAverageInvocationTime() {
		return totalInvocationTime / getTotalNumberOfInvocations();
	}

	public void add(long startTime, long stopTime, boolean successfull,Date start) {
		this.startTime = start;
		GMTOffset = TimeZone.getDefault().getOffset(this.startTime.getTime())/1000/60/60;
		firstInvocationTime = firstInvocationTime == 0 ? startTime : firstInvocationTime;
		lastInvocationTime = startTime;
		long duration = stopTime - startTime;
		minimumInvocationTime = duration < minimumInvocationTime  ? duration : minimumInvocationTime;
		maximumInvocationTime = duration > maximumInvocationTime  ? duration : maximumInvocationTime;

		if (successfull) {
			numberOfSuccessfullInvocations++;
		} else {
			numberOfUnsuccessfullInvocations++;
		}

		totalInvocationTime += duration;
	}

}
