package com.telus.provider.monitoring.dao;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import com.telus.api.BaseTest;
import com.telus.provider.monitoring.domain.MonitoringDataPoint;
import com.telus.provider.monitoring.domain.MonitoringDataPointManager;
import com.telus.provider.monitoring.domain.MonitoringDataPointManagerEndTimeException;

public class MonitoringDataPointWriteDaoEjbImplTest extends BaseTest {
	static {
		setupD3();
	}
	
	public MonitoringDataPointWriteDaoEjbImplTest(String name) throws Throwable {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testWrite() throws MonitoringDataPointManagerEndTimeException, MonitoringDataPointWriteDaoException {
		
		System.setProperty("com.telus.provider.providerURL", "t3://um-generalutilities-dv103.tmi.telus.com:20152");
		
		MonitoringDataPointWriteDaoEjbImpl dao = new MonitoringDataPointWriteDaoEjbImpl();
		
		MonitoringDataPoint dataPoint = new MonitoringDataPoint("host", "test", "com.telus.blah", "clientAPI", new Date());
		MonitoringDataPointManager manager = new MonitoringDataPointManager();
		manager.addDataPoint(dataPoint, 10000L);
		manager.setEndTime(dataPoint, new Date());
		
		Collection data = new HashSet();
		data.add(dataPoint);		
		
		try {
			dao.write(data);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
