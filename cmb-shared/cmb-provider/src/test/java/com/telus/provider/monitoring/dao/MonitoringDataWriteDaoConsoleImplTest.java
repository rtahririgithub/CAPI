package com.telus.provider.monitoring.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.telus.api.BaseTest;
import com.telus.provider.monitoring.domain.MonitoringDataPoint;

public class MonitoringDataWriteDaoConsoleImplTest extends BaseTest{
	static {
		setupD3();
	}
	public MonitoringDataWriteDaoConsoleImplTest(String name) throws Throwable {
		super(name);
	}

	public void setUp() throws Exception {
		
	}
	
	public void testWriteEndTimeNull() throws Exception {
		setUp();
		Set data = new HashSet();
		
		data.add(new MonitoringDataPoint("sp20099", "MAIP", "com.telus.blahblahblah", "ClientAPI", new Date()));
		data.add(new MonitoringDataPoint("sp200910", "MAIP", "com.telus.blahblahblah", "ClientAPI", new Date()));		
		
		MonitoringDataPointWriteDaoConsoleImpl dao = new MonitoringDataPointWriteDaoConsoleImpl();
		
		dao.write(data);
			
	}

}
