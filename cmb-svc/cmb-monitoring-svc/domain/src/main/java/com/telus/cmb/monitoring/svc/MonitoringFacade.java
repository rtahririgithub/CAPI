package com.telus.cmb.monitoring.svc;

import com.telus.cmb.monitoring.svc.domain.IMonitoringDataPointInfo;
import com.telus.eas.framework.exception.TelusException;

public interface MonitoringFacade {
	
	void log(IMonitoringDataPointInfo[] monitoringDataPoints) throws TelusException;
}
