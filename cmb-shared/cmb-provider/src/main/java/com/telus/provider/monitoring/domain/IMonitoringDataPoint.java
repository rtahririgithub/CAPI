package com.telus.provider.monitoring.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author t859517 Hilton Poon
 *
 */
public interface IMonitoringDataPoint extends Serializable{
	
	/**
	 * The dataPointKey to be used in a hashmap. This is a String created by catting the superkey attributes.
	 * 
	 * Superkey attributes catted in the following order:
	 * - HostName
	 * - ServerNode
	 * - ApplicationId
	 * - KnowbilityUserId
	 * - MethodSignature
	 * 
	 * @return The Superkey of this datapoint catted together.
	 */
	String getDataPointKey();
	
	/**
	 * The hostname of the server that the clientAPI provider instance was executed on.
	 * @return The hostname of the server that the clientAPI provider instance was executed on..
	 */
	String getHostName();
	
	/**
	 * The application passed to the provide when it was initialised.
	 * @return The application passed to the provide when it was initialised.
	 */
	String getApplicationId();
	
	/**
 	 * The full method signature of the method that was executed.
	 * @return The full method signature of the method that was executed.
	 */
	String getMethodSignature();
	
	/**
	 * The start time of the data point.
	 * @return The start time of the data point.
	 */
	Date getStartTime();
	
	/**
	 * The end time of the data point.
	 * @return The end time of the data point.
	 */
	Date getEndTime();
	
	/**
	 * The total response time for the number of executions counted between the start time and end time.
	 * 
	 * Time in ms.
	 * @return The total response time.
	 */
	long getTotalResponseTime();
	
	/**
	 * The number of times the method was executed in the time interval between the start time and end time.
	 * @return The number of times executed.
	 */
	int getExecutionCount();
	
	/**
	 * Sets the endTime of this dataPoint;
	 * @param endTime The endTime;
	 */
	void setEndTime(Date endTime);
	
	/**
	 * Sets the total response time in ms.
	 * @param totalResponseTime The total response time.
	 */
	void setTotalResponseTime (long totalResponseTime);
	
	/**
	 * Sets the execution count.
	 * @param executionCount The execution count.
	 */
	void setExecutionCount (int executionCount);
	
	/**
	 * Returns the GMT Offset of the startTime and endTime(); 
	 * @return The GMT Offset.
	 */
	int getGMTOffset();
	
	/**
	 * Returns the systemId of this monitoringData point.
	 * @return The System Id.
	 */
	String getSystemId();
	
}
