package com.telus.provider.monitoring;

import com.telus.provider.monitoring.dao.IMonitoringDataPointHoursToKeepDao;
import com.telus.provider.monitoring.dao.IMonitoringDataPointWriteDao;

public interface IMethodInvocationManager {

	public abstract void setHoursToKeepDao(
			IMonitoringDataPointHoursToKeepDao hoursToKeepDao);

	/**
	 * Logs the method invocation.
	 * @param provider The TMProvider used for the method invocation.
	 * @param longMethodSignature The full methodSignature.
	 * @param shortMethodSignature The short method signature
	 * @param responseTime The response time for the call of that method.
	 */
	public abstract void logMethodInvocation(String applicationId,
			String longMethodSignature, long responseTime);

	/**
	 * Sets the endTime to all the dataPoints to now.
	 */
	public abstract void setDataPointEndTime();

	/**
	 * Writes the dataPoints using the dao.
	 */
	public abstract void write();

	public abstract void setWriteDao(IMonitoringDataPointWriteDao writeDao);

}