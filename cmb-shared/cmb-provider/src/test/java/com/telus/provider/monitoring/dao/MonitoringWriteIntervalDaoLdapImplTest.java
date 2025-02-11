package com.telus.provider.monitoring.dao;

import junit.framework.TestCase;

public class MonitoringWriteIntervalDaoLdapImplTest extends TestCase{
	

	public void setUp() throws Exception {				
	}

	public void testGetWriteInterval() throws Exception {
		MonitoringSleepIntervalDaoLdapImpl dao = new MonitoringSleepIntervalDaoLdapImpl();
		
		System.out.println(dao.getSleepInterval());
	}

}
