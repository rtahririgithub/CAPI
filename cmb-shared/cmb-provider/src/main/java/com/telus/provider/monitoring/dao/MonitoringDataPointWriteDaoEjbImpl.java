package com.telus.provider.monitoring.dao;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import com.telus.cmb.monitoring.svc.domain.IMonitoringDataPointInfo;
import com.telus.cmb.monitoring.svc.domain.MonitoringDataPointInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.monitoring.domain.IMonitoringDataPoint;
import com.telus.eas.framework.exception.TelusException;


public class MonitoringDataPointWriteDaoEjbImpl implements
		IMonitoringDataPointWriteDao {

	public void write(Collection data)
			throws MonitoringDataPointWriteDaoException {
		Collection logData = new HashSet();
		
		Iterator datumIterator = data.iterator();
		
		while (datumIterator.hasNext()) {
			logData.add(convert((IMonitoringDataPoint)datumIterator.next()));			
		}
		
		try {
			TMProvider.getMonitoringFacadeEJB().log((IMonitoringDataPointInfo [])
					logData.toArray(new IMonitoringDataPointInfo[logData.size()]));
		} catch (Throwable t) { 
			throw new MonitoringDataPointWriteDaoException(t);
		}
	}

	private  IMonitoringDataPointInfo convert(
			IMonitoringDataPoint input) {
		 IMonitoringDataPointInfo output = new  MonitoringDataPointInfo();
		output.setApplicationId(input.getApplicationId());
		output.setEndTime(input.getEndTime());
		output.setExecutionCount(input.getExecutionCount());
		output.setGMTOffset(input.getGMTOffset());
		output.setHostName(input.getHostName());
		output.setMethodSignature(input.getMethodSignature());
		output.setStartTime(input.getStartTime());
		output.setSystemId(input.getSystemId());
		output.setTotalResponseTime(input.getTotalResponseTime());
		
		return output;
	}

}
