package com.telus.cmb.monitoring.svc.domain;

import java.io.Serializable;
import java.util.Date;

public interface IMonitoringDataPointInfo extends Serializable {

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
	 * Returns the GMT Offset of the startTime and endTime(); 
	 * @return The GMT Offset.
	 */
	int getGMTOffset();
	
	/**
	 * Returns the systemId of this monitoringData point.
	 * @return The System Id.
	 */
	String getSystemId();

	void setHostName(String hostName);

	void setApplicationId(String applicationId);

	void setMethodSignature(String methodSignature);

	void setSystemId(String systemId);

	void setStartTime(Date startTime);

	void setEndTime(Date endTime);

	void setGMTOffset(int gMTOffset);

	void setTotalResponseTime(long totalResponseTime);

	void setExecutionCount(int executionCount);

}