package com.telus.cmb.monitoring.svc.domain;

import java.util.Date;

public class MonitoringDataPointInfo implements IMonitoringDataPointInfo {
	
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
	
	public MonitoringDataPointInfo() {
		
	}

	public MonitoringDataPointInfo(String hostName, String applicationId, String methodSignature,
			String systemId, Date startTime, Date endTime, int GMTOffset, long totalResponseTime, int executionCount) {
		this.hostName = hostName;
		this.applicationId = applicationId;
		this.methodSignature = methodSignature;
		this.systemId = systemId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.GMTOffset = GMTOffset;
		this.totalResponseTime = totalResponseTime;
		this.executionCount = executionCount;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#getHostName()
	 */
	
	public String getHostName() {
		return hostName;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#getApplicationId()
	 */
	
	public String getApplicationId() {
		return applicationId;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#getLongMethodSignature()
	 */
	
	public String getMethodSignature() {
		return this.methodSignature;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#getStartTime()
	 */
	
	public Date getStartTime() {
		return startTime;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#getEndTime()
	 */
	
	public Date getEndTime() {
		return endTime;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#getTotalResponseTime()
	 */
	
	public long getTotalResponseTime() {
		return totalResponseTime;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#getExecutionCount()
	 */
	
	public int getExecutionCount() {
		return executionCount;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#toString()
	 */
	
	public String toString() {
		return 
		this.methodSignature + "\n" +
		this.hostName + "\t" + this.applicationId + "\t" 
			+ "\t" + this.startTime + "\t" + this.endTime + "\t" + this.GMTOffset + 
			"\t" + this.totalResponseTime + "\t" + this.executionCount + "\t";
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#getGMTOffset()
	 */
	
	public int getGMTOffset() {
		return this.GMTOffset;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#getSystemId()
	 */
	
	public String getSystemId() {
		return this.systemId;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#setHostName(java.lang.String)
	 */
	
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#setApplicationId(java.lang.String)
	 */
	
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#setLongMethodSignature(java.lang.String)
	 */
	
	public void setMethodSignature(String longMethodSignature) {
		this.methodSignature = longMethodSignature;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#setSystemId(java.lang.String)
	 */
	
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#setStartTime(java.util.Date)
	 */
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#setEndTime(java.util.Date)
	 */
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#setGMTOffset(int)
	 */
	public void setGMTOffset(int gMTOffset) {
		GMTOffset = gMTOffset;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#setTotalResponseTime(long)
	 */
	public void setTotalResponseTime(long totalResponseTime) {
		this.totalResponseTime = totalResponseTime;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.monitoring.svc.domain.IMonitoringDataPoint2#setExecutionCount(int)
	 */
	public void setExecutionCount(int executionCount) {
		this.executionCount = executionCount;
	}
}
