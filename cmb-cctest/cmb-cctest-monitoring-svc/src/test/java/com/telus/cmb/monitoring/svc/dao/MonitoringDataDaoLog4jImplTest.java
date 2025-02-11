package com.telus.cmb.monitoring.svc.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.Before;
import org.junit.Test;

import com.telus.cmb.monitoring.svc.domain.IMonitoringDataPointInfo;
import com.telus.cmb.monitoring.svc.domain.MonitoringDataPointInfo;

public class MonitoringDataDaoLog4jImplTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testWrite() throws MonitoringDataDaoWriteException {
		MonitoringDataDaoLog4jImpl dao = new MonitoringDataDaoLog4jImpl();
		
		Collection<IMonitoringDataPointInfo> dataPoints = new ArrayList<IMonitoringDataPointInfo>();
		
		dataPoints.add(new MonitoringDataPointInfo("l013430", "SmartDesktop", "com.telus.blahblahblah", 
				"clientAPI", new Date(), new Date(), -4, 10000, 3));		
		
		Logger root = Logger.getRootLogger();
		root.removeAllAppenders();
		
		try {
			dao.write(dataPoints.toArray(new IMonitoringDataPointInfo[dataPoints.size()]));
			fail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		root.addAppender(new ConsoleAppender(new PatternLayout("%m%n")));
		
		try {
			dao.write(dataPoints.toArray(new IMonitoringDataPointInfo[dataPoints.size()]));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
