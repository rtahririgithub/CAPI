package com.telus.provider.monitoring.dao;

import com.telus.provider.util.AppConfiguration;

public class MonitoringSleepIntervalDaoLdapImpl implements
		IMonitoringSleepIntervalDao {
	

	public int getSleepInterval() {		
		return AppConfiguration.getClientAgentMonitoringSleepInterval();
	}
	

}
