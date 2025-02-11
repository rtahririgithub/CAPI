package com.telus.provider.monitoring.dao;

import com.telus.provider.monitoring.domain.IMonitoringDataPointManager;
import com.telus.provider.monitoring.domain.MonitoringDataPointManager;

public class DaoFactory {
	
	public static IMonitoringSleepIntervalDao getMonitoringSleepIntevalDao() {
		return new MonitoringSleepIntervalDaoLdapImpl();
	}

	public static IMonitoringDataPointManager getMonitoringDataPointManager() {
		return new MonitoringDataPointManager();
	}
	
	public static IMonitoringDataPointWriteDao getMonitoringDataPointWriteDao() {
		return new MonitoringDataPointWriteDaoEjbImpl();
	}

	public static IMonitoringDataPointHoursToKeepDao getMonitoringDataPointHoursToKeepDao() {
		return new MonitoringDataPointHoursToKeepInMemoryImpl();
	}
}
