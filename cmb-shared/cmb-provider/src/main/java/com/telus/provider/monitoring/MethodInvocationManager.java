package com.telus.provider.monitoring;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.telus.provider.monitoring.dao.DaoFactory;
import com.telus.provider.monitoring.dao.IMonitoringDataPointHoursToKeepDao;
import com.telus.provider.monitoring.dao.IMonitoringDataPointWriteDao;
import com.telus.provider.monitoring.dao.IMonitoringSleepIntervalDao;
import com.telus.provider.monitoring.dao.MonitoringDataPointWriteDaoException;
import com.telus.provider.monitoring.domain.IMonitoringDataPoint;
import com.telus.provider.monitoring.domain.IMonitoringDataPointManager;
import com.telus.provider.monitoring.domain.MonitoringDataPoint;
import com.telus.provider.monitoring.scheduling.MonitoringEndTimeJob;
import com.telus.provider.monitoring.scheduling.MonitoringWriteJob;
import com.telus.provider.scheduling.IScheduler;
import com.telus.provider.scheduling.NowTrigger;
import com.telus.provider.scheduling.OnTheHourTrigger;
import com.telus.provider.scheduling.Scheduler;
import com.telus.provider.util.Logger;

public class MethodInvocationManager implements Serializable, IMethodInvocationManager {
	
	private static String SYSTEM_ID = "clientAPI";
	
	private static final long serialVersionUID = 1L;
	
	private static String hostName = "";
	
	private IMonitoringDataPointWriteDao writeDao = null;
	private IMonitoringDataPointManager dataManager = null;
	private IMonitoringDataPointHoursToKeepDao hoursToKeepDao = DaoFactory.getMonitoringDataPointHoursToKeepDao();
	
	/* (non-Javadoc)
	 * @see com.telus.provider.monitoring.IMethodInvocationManager#setHoursToKeepDao(com.telus.provider.monitoring.dao.IMonitoringDataPointHoursToKeepDao)
	 */
	public void setHoursToKeepDao(IMonitoringDataPointHoursToKeepDao hoursToKeepDao) {
		this.hoursToKeepDao = hoursToKeepDao;
	}

	private static Date reportingPeriodStartTime = new Date();
	
	private Map dataMap = Collections.synchronizedMap(new HashMap());
	private List writeData = Collections.synchronizedList(new ArrayList());	

	private Calendar firstFailDate = null;

	private static IMethodInvocationManager instance;

	private MethodInvocationManager(IMonitoringDataPointWriteDao writeDao, IMonitoringSleepIntervalDao sleepDao, IMonitoringDataPointManager dataManager, IScheduler schedFact) {
		this.writeDao = writeDao;
		this.dataManager = dataManager;		

		// If the schedFactory is null we skip scheduling!
		if (schedFact != null) {
			int sleepInterval = sleepDao.getSleepInterval();
			Logger.debug0("MethodInvocationManager - Sleep Interval: " + sleepInterval);
			schedFact.addJob(new OnTheHourTrigger(sleepInterval), new MonitoringEndTimeJob());
			schedFact.addJob(new NowTrigger(sleepInterval), new MonitoringWriteJob());
			schedFact.start();
		}
		try {
			hostName = java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {

		}
	}
	
	/**
	 * Retrieves a MethodInvocationManager with the default DaoFactory provided daos and a default StdSchedulerFactory from quartz.
	 * Delegates to getInstance(IMonitoringDataPointWriteDao writeDao, IMonitoringSleepIntervalDao sleepDao
			, IMonitoringDataPointManager dataManager)
	 * @return An instance of MethodInvocationManager
	 */
	public static IMethodInvocationManager getInstance() {
		if (instance == null) {
			return getInstance(null, null, null, Scheduler.getInstance());
		} else {
			return instance;
		}
	}
	
	public static IMethodInvocationManager getInstance(IMonitoringDataPointWriteDao writeDao, IMonitoringSleepIntervalDao sleepDao
			, IMonitoringDataPointManager dataManager) {
		if (instance == null) {
			return getInstance(writeDao, sleepDao, dataManager, Scheduler.getInstance());
		} else {
			return instance;
		}
	}
	
	

	
	/**
	 * Retrieves a MethodInvocationManager with the given writeDao, sleepDao and dataManager IF a singleton has not been instantiated already.
	 * 
	 * If a singleton has been instantiated then that instance is returned regardless of what daos you pass.
	 * 
	 * If a singleton has not been instantiated then an instance will be create using the provided daos.
	 * 
	 * If any of the daos are null, we call the DaoFactory to provide a default dao.
	 * @param writeDao The IMonitoringDataPointWriteDao. Can be null.
	 * @param sleepDao The IMonitoringSleepIntervalDao. Can be null.
	 * @param dataManager The IMonitoringDataPointManager Can be null.
	 * @param schedFact The SchedulerFactory you want used. This scheduler can be null, and when it is null we effectively disable scheduling.
	 * @return An instance of the methodInvocationManager.
	 */
	synchronized static IMethodInvocationManager getInstance(IMonitoringDataPointWriteDao writeDao, IMonitoringSleepIntervalDao sleepDao
			, IMonitoringDataPointManager dataManager, IScheduler schedFact ) {
		if (instance == null) {
			if (writeDao == null) {
				writeDao = DaoFactory.getMonitoringDataPointWriteDao();
			}
			if (sleepDao == null) {
				sleepDao = DaoFactory.getMonitoringSleepIntevalDao();
			}
			if (dataManager == null) {
				dataManager = DaoFactory.getMonitoringDataPointManager();
			}
			instance = new MethodInvocationManager(writeDao, sleepDao, dataManager, schedFact);
		}
		
		return instance;
	}


	/* (non-Javadoc)
	 * @see com.telus.provider.monitoring.IMethodInvocationManager#logMethodInvocation(java.lang.String, java.lang.String, long)
	 */
	public void logMethodInvocation(String applicationId, String longMethodSignature, long responseTime) {
		try {			

			String key = MonitoringDataPoint.getDataPointKey(hostName, applicationId, longMethodSignature, SYSTEM_ID);

			synchronized (dataMap) {
				IMonitoringDataPoint data = (IMonitoringDataPoint)dataMap.get(key);

				if (data == null) {
					data = new MonitoringDataPoint(hostName, applicationId, longMethodSignature, SYSTEM_ID, reportingPeriodStartTime);
					dataMap.put(key, data);
				}

				dataManager.addDataPoint(data, responseTime);
			}
		} catch (Throwable t) {
			Logger.debug0(t);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.telus.provider.monitoring.IMethodInvocationManager#setDataPointEndTime()
	 */
	public void setDataPointEndTime() {
		List writeDataTemp = new ArrayList();
		
		try {	
			Date reportingPeriodEndTime = null;
			synchronized (dataMap) {
				reportingPeriodEndTime = new Date();
				reportingPeriodStartTime = reportingPeriodEndTime;
				writeDataTemp.addAll(dataMap.values());
				dataMap = Collections.synchronizedMap(new HashMap());
			}

			Iterator datumIterator = writeDataTemp.iterator();
			while (datumIterator.hasNext()) {
				IMonitoringDataPoint datum = (IMonitoringDataPoint) datumIterator.next();

				dataManager.setEndTime(datum, reportingPeriodEndTime);
			}
			synchronized(writeData) {
				writeData.addAll(writeDataTemp);
			}
			
		} catch (Throwable t) {
			Logger.debug0(t);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.telus.provider.monitoring.IMethodInvocationManager#write()
	 */
	public void write() {	
		try {
			
			// Copy the data to another list to avoid blocking on remote EJB call
			List writeDataCopy = new ArrayList();
			synchronized (writeData) {
				writeDataCopy.addAll(writeData);
				writeData = Collections.synchronizedList(new ArrayList());
			}
			
			try {
				writeDao.write(writeDataCopy);
				firstFailDate = null;
				writeDataCopy = new ArrayList();
			} catch (MonitoringDataPointWriteDaoException e) {
				Logger.debug0(e);				
				if (firstFailDate == null) {
					firstFailDate = Calendar.getInstance();
				}				
			}
			
			
			// if the firstFailDate is not null, then we want to check if we have to
			// clear any records
			if (firstFailDate != null) {
				Calendar freshDate = Calendar.getInstance();
				freshDate.add(Calendar.HOUR, (hoursToKeepDao.getHoursToKeep()));
				// If the first fail date is older than the hours to keep, then we have to iterate
				// through the writeData to clean out the records
				if (firstFailDate.before(freshDate)) {
					ArrayList removeData = new ArrayList();				
					Iterator datumIterator = writeDataCopy.iterator();				
					while (datumIterator.hasNext()) {
						IMonitoringDataPoint datum = (IMonitoringDataPoint)datumIterator.next();

						Calendar datumEndTime = Calendar.getInstance();
						datumEndTime.setTime(datum.getEndTime());
						// If the record is older than the hours to keep, we add to removeData
						if (datumEndTime.before(freshDate)) {						
							removeData.add(datum);
						}					
					}
					// Remove the entries from the writeData.
					Iterator removeDataIterator = removeData.iterator();
					while (removeDataIterator.hasNext()) {
						writeDataCopy.remove(removeDataIterator.next());
					}				
				}
			}
			// In the event of failed write, we need to copy the data back!	
			if (writeDataCopy.size() > 0) {
				synchronized (writeData) {
					writeData.addAll(writeDataCopy);
				}
			}
		} catch (Throwable t) {
			Logger.debug0(t);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.telus.provider.monitoring.IMethodInvocationManager#setWriteDao(com.telus.provider.monitoring.dao.IMonitoringDataPointWriteDao)
	 */
	public void setWriteDao(IMonitoringDataPointWriteDao writeDao) {
		this.writeDao = writeDao;
	}
}
