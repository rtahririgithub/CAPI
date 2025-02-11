package com.telus.provider.monitoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.telus.provider.monitoring.dao.IMonitoringDataPointWriteDao;
import com.telus.provider.monitoring.dao.IMonitoringSleepIntervalDao;
import com.telus.provider.monitoring.dao.MonitoringDataPointWriteDaoException;
import com.telus.provider.monitoring.domain.IMonitoringDataPoint;

public abstract class AMethodInvocationManagerTest extends TestCase{

	protected static class MonitoringDataPointWriteTestDao implements IMonitoringDataPointWriteDao {
		private List writeNumberCount = new ArrayList();
		
		
		public void write(Collection data) {
			writeNumberCount.add(new Integer(data.size()));
			Iterator datumIterator = data.iterator();
			while(datumIterator.hasNext()) {
				IMonitoringDataPoint datum = (IMonitoringDataPoint) datumIterator.next();
				System.err.println (datum);		
			}
			
		}
		
		public List getWriteNumberCount() {
			return this.writeNumberCount;
		}
		
		public void resetWriteNumberCount() {
			this.writeNumberCount = new ArrayList();
		}
		
	}
	
	protected static class MonitoringDataPointWriteTestThrowsExeptionDao implements IMonitoringDataPointWriteDao {

		public void write(Collection data) throws MonitoringDataPointWriteDaoException {
			try {
				throw new Exception();
			} catch (Exception e) {
				throw new MonitoringDataPointWriteDaoException(e);
			}
		}
	}
	
	protected static class MonitoringDataPointSleepTestDao implements IMonitoringSleepIntervalDao {		
		int sleepInterval = 5;
		
		public MonitoringDataPointSleepTestDao(int sleepInterval) {
			this.sleepInterval = sleepInterval;
		}
		
		public MonitoringDataPointSleepTestDao() {
			
		}
		
		public int getSleepInterval() {
			return sleepInterval;
		}
		
	}
}
