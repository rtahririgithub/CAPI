package com.telus.provider.monitoring.dao;

public interface IMonitoringDataPointHoursToKeepDao {

	/**
	 * Returns the number of hours to keep a monitoringDataPoint record in absolute hours.
	 * 
	 * e.g. For one day in the past, return -24.
	 * For 2 hours in the past, return -2.
	 * @return The absolute number of hours to keep the monitoringDataPoint.
	 */
	public int getHoursToKeep();
}
