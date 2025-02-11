package com.telus.provider.monitoring.dao;

import junit.framework.TestCase;

public class MonitoringSleepIntervalDaoLdapImplTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testGetSleepIntervalDefault() {
		MonitoringSleepIntervalDaoLdapImpl dao = new MonitoringSleepIntervalDaoLdapImpl();
		assertEquals(1000*60*30, dao.getSleepInterval());
	}

}
