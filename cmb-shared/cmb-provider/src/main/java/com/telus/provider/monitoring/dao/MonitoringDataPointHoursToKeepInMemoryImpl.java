package com.telus.provider.monitoring.dao;

public class MonitoringDataPointHoursToKeepInMemoryImpl implements
		IMonitoringDataPointHoursToKeepDao {

	public int getHoursToKeep() {
		return -24;
	}

}
