package com.telus.cmb.monitoring.svc.dao;

import com.telus.cmb.monitoring.svc.domain.IMonitoringDataPointInfo;

public interface IMonitoringDataDao {
	
	void write(IMonitoringDataPointInfo[] dataPoints) throws MonitoringDataDaoWriteException;

}
