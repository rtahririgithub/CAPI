package com.telus.provider.monitoring.domain;

import java.util.Date;

public class MonitoringDataPointManager implements IMonitoringDataPointManager{

	public void addDataPoint(IMonitoringDataPoint dataPoint, long responseTime) throws MonitoringDataPointManagerEndTimeException {
		
		if (dataPoint.getEndTime() != null) {
			throw new MonitoringDataPointManagerEndTimeException();
		}		
		dataPoint.setExecutionCount( dataPoint.getExecutionCount() + 1);
		dataPoint.setTotalResponseTime(dataPoint.getTotalResponseTime() + responseTime);		
	}

	public void setEndTime(IMonitoringDataPoint dataPoint, Date endTime)
			throws MonitoringDataPointManagerEndTimeException {
		if (dataPoint.getEndTime() != null) {
			throw new MonitoringDataPointManagerEndTimeException();
		}		
		dataPoint.setEndTime(endTime);		
	}

}
