package com.telus.provider.monitoring.domain;

import java.util.Date;

/**
 * Manager that actions on the IMonitoringData objects.
 * @author t859517 Hilton Poon
 *
 */
public interface IMonitoringDataPointManager {
	
	/**
	 * Adds a dataPoint to the IMonitoringData dataPoint. Method shall update the averageExecutionTime
	 * and the executionTime.
	 * 
	 * Assumes one executionPoint is added.
	 * 
	 * Pre-condition: EndTime is null. 
	 * @param dataPoint The dataPoint we're adding to.
	 * @param responseTime The responseTime of the execution.
	 * @throws MonitoringDataPointManagerEndTimeException Thrown if EndTime is not null.
	 */
	void addDataPoint(IMonitoringDataPoint dataPoint, long responseTime) throws MonitoringDataPointManagerEndTimeException;
	
	/**
	 * Sets the endTime for the dataPoint.
	 * @param dataPoint Sets the endTime;
	 */
	void setEndTime(IMonitoringDataPoint dataPoint, Date endTime) throws MonitoringDataPointManagerEndTimeException;
}
