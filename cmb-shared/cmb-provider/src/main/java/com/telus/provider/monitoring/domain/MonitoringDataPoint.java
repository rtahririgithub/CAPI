package com.telus.provider.monitoring.domain;

import java.util.Date;
import java.util.TimeZone;

public class MonitoringDataPoint implements IMonitoringDataPoint {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5035435935931064185L;
	
	private String hostName = null;
	private String applicationId = null;
	private String methodSignature = null;
	private String systemId = null;
	private Date startTime = null;
	private Date endTime = null;
	private int GMTOffset = 0;
	private long totalResponseTime = 0L;
	private int executionCount = 0;
	
	
	public MonitoringDataPoint(String hostName, String applicationId, String methodSignature, String systemId, Date startTime) {
		this.hostName = hostName;
		this.applicationId = applicationId;
		this.methodSignature = methodSignature;
		this.startTime = startTime;		
		this.systemId = systemId;
		GMTOffset = TimeZone.getDefault().getOffset(startTime.getTime())/1000/60/60;
		
	}

	public String getDataPointKey() {
		return getDataPointKey(this.hostName, this.applicationId, this.methodSignature, this.systemId);
	}
	
	/**
	 * Returns the dataPointKey for a MonitoringData object by composing the given parameters.
	 * @param hostName The hostName.
	 * @param applicationId The applicationId.
	 * @param knowbilityUserId The knowbilityUserId
	 * @param longMethodSignature The longMethodSignature.
	 * @return The dataPointKey.
	 */
	public static String getDataPointKey(String hostName, String applicationId, String longMethodSignature, String systemId) {
		return hostName + applicationId + longMethodSignature + systemId;
	}

	public String getHostName() {
		return hostName;
	}

	public String getApplicationId() {
		return applicationId;
	}
	
	public String getMethodSignature() {
		return this.methodSignature;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public long getTotalResponseTime() {
		return totalResponseTime;
	}

	public int getExecutionCount() {
		return executionCount;
	}

	public void setTotalResponseTime(long totalResponseTime) {
		this.totalResponseTime = totalResponseTime;

	}

	public void setExecutionCount(int executionCount) {
		this.executionCount = executionCount;

	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String toString() {
		return 
		this.methodSignature + "\n" +
		this.hostName + "\t" + this.applicationId + "\t" 
			+ "\t" + this.startTime + "\t" + this.endTime + "\t" + this.GMTOffset + 
			"\t" + this.totalResponseTime + "\t" + this.executionCount + "\t";
	}

	public int getGMTOffset() {
		return this.GMTOffset;
	}

	public String getSystemId() {
		return this.systemId;
	}
}
