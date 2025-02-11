package com.telus.provider.monitoring.dao;


import java.util.Collection;
import java.util.Iterator;

import com.telus.provider.monitoring.domain.IMonitoringDataPoint;
import com.telus.provider.util.Logger;

public class MonitoringDataPointWriteDaoConsoleImpl implements IMonitoringDataPointWriteDao{


	public void write(Collection data) {
		Iterator datumIterator = data.iterator();
		while (datumIterator.hasNext()) {			
			IMonitoringDataPoint datum = (IMonitoringDataPoint)datumIterator.next();
			Logger.debug(datum);		
		}
	}		
}
