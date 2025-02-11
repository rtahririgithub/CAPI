package com.telus.cmb.monitoring.svc.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.telus.cmb.monitoring.svc.domain.IMonitoringDataPointInfo;

public class MonitoringDataDaoLog4jImpl implements IMonitoringDataDao{

	private static final Log logger = LogFactory.getLog(MonitoringDataDaoLog4jImpl.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final String DELIMITER = "|";
	
	@Override
	public void write(IMonitoringDataPointInfo[] dataPoints) throws MonitoringDataDaoWriteException {	
		Logger root = Logger.getRootLogger();
		if (!root.getAllAppenders().hasMoreElements()) {
			// 	No appenders means log4j is not initialized!
			throw new MonitoringDataDaoWriteLog4jNoRootLoggerException();
		}
		
		for (IMonitoringDataPointInfo datum : dataPoints) {
			if (datum != null) {
				long avgResponseTime = 0;
				if (datum.getExecutionCount() > 0) {
					avgResponseTime = datum.getTotalResponseTime() / datum.getExecutionCount();
				}
				String logLine = sdf.format(new Date()) + DELIMITER + sdf.format(datum.getStartTime()) + DELIMITER 
				+ sdf.format(datum.getEndTime()) + DELIMITER + datum.getGMTOffset() +
				DELIMITER + avgResponseTime + DELIMITER + datum.getExecutionCount() + DELIMITER + 
				datum.getHostName() + DELIMITER + datum.getApplicationId() + DELIMITER + datum.getMethodSignature();

				logger.info(logLine);
			}
		}		
	}
}
