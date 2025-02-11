package com.telus.provider.monitoring.domain;

import java.util.Date;

import junit.framework.TestCase;

public class MonitoringDataManagerTest extends TestCase {
	
	IMonitoringDataPoint dataPoint = null;
	IMonitoringDataPointManager manager = null;
	
	public void setUp() throws Exception {
		 dataPoint = new MonitoringDataPoint("sp20099", "MAIP", "com.telus.blahblahblah", "ClientAPI", new Date());
		
		 manager = new MonitoringDataPointManager();
	}

	
	public void testAddDataPoint() throws Exception {
		setUp();
		manager.addDataPoint(dataPoint, 500);
		
		assertEquals(500, dataPoint.getTotalResponseTime());
		assertEquals(1, dataPoint.getExecutionCount());
	}
	
	
	public void testAddDataPointNotNullEndTime() throws Exception {
		setUp();
		dataPoint.setEndTime(new Date());
		
		try {
			manager.addDataPoint(dataPoint, 500);
			fail("Exception expected.");
		} catch (MonitoringDataPointManagerEndTimeException e) {
			
		}
	}
	
	public void testAddMultipleDataPointsTestAverageCalculation() throws Exception {
		setUp();
		manager.addDataPoint(dataPoint, 500);
		assertEquals(500, dataPoint.getTotalResponseTime());
		assertEquals(1, dataPoint.getExecutionCount());
		
		manager.addDataPoint(dataPoint, 500);
		assertEquals(1000, dataPoint.getTotalResponseTime());
		assertEquals(2, dataPoint.getExecutionCount());
		
		manager.addDataPoint(dataPoint, 1000);
		assertEquals(2000, dataPoint.getTotalResponseTime());
		assertEquals(3, dataPoint.getExecutionCount());
		
		manager.addDataPoint(dataPoint, 0);
		assertEquals(2000, dataPoint.getTotalResponseTime());
		assertEquals(4, dataPoint.getExecutionCount());
		
	}
	
	public void testSetEndTimeNull() throws Exception {
		setUp();
		manager.setEndTime(dataPoint, new Date());
		
		assertNotNull(dataPoint.getEndTime());
	}
	
	public void testSetEndTimeNotNull() throws Exception {
		setUp();
		dataPoint.setEndTime(new Date());
		
		try {
			manager.setEndTime(dataPoint, new Date());
			fail();
		} catch (MonitoringDataPointManagerEndTimeException e) {			
		}
	}

}
