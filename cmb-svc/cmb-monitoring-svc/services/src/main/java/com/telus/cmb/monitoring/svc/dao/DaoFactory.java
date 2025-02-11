package com.telus.cmb.monitoring.svc.dao;

public class DaoFactory {

	public static IMonitoringDataDao getMonitoringDataDao() {
		return new MonitoringDataDaoLog4jImpl();
	}
}
