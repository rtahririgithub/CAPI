package com.telus.provider.monitoring;

import com.telus.api.BaseTest;
import com.telus.api.ClientAPI;
import com.telus.api.account.Account;
import com.telus.api.account.AccountManager;
import com.telus.api.account.Subscriber;
import com.telus.provider.monitoring.dao.IMonitoringSleepIntervalDao;

public class MethodInvocationManagerSchedulerTest extends AMethodInvocationManagerTest{
	static {
		BaseTest.setupD3();
		
	}

	private AccountManager accountManager;
	private ClientAPI api = null;
	private static MonitoringDataPointWriteTestDao writeDao = new MonitoringDataPointWriteTestDao();
	private static MonitoringDataPointSleepTestDao sleepDao = new MonitoringDataPointSleepTestDao();
	

	
	public void setUp() throws Exception {		
		if (api == null) {
			api = ClientAPI.getInstance("18654", "apollo", "OLN");
			ClientAPI.Provider provider = api.getProvider();
			accountManager = provider.getAccountManager();
		}				
		
		MethodInvocationManager.getInstance(writeDao, sleepDao, null);
	}

	public void testWriteRemovesDataEntry() throws Exception {
		setUp();
				
		Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("9057160345");		
		
		subscriber.getDiscounts();
		subscriber.getDiscounts();
		subscriber.getDiscounts();
		subscriber.getAddress();		// Not included in count.
		subscriber.getActivityCode(); // Not included in count.
		Account account = accountManager.findAccountByPhoneNumber("9057160345");		
		account.getAddress();
		
		Thread.sleep(sleepDao.getSleepInterval() * 10);
		
		System.out.println (writeDao.getWriteNumberCount());		
	}
	
	private static class MonitoringDataPointSleepTestDao implements IMonitoringSleepIntervalDao {
		
		public int getSleepInterval() {
			return 5000;
		}
		
	}
	
	

}
