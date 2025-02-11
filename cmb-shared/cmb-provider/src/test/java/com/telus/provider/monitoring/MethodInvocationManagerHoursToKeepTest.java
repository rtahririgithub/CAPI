package com.telus.provider.monitoring;

import com.telus.api.BaseTest;
import com.telus.api.BrandNotSupportedException;
import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;
import com.telus.api.account.AccountManager;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.provider.monitoring.dao.IMonitoringDataPointHoursToKeepDao;

public class MethodInvocationManagerHoursToKeepTest extends
		AMethodInvocationManagerTest {
	private AccountManager accountManager;
	private ClientAPI api = null;
	private MonitoringDataPointWriteTestDao writeDao = new MonitoringDataPointWriteTestDao();
	private MonitoringDataPointWriteTestThrowsExeptionDao exceptionWriteDao = new MonitoringDataPointWriteTestThrowsExeptionDao();
	
	static {
		BaseTest.setupD3();
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		if (api == null) {
			api = ClientAPI.getInstance("18654", "apollo", "D3_DEV_TEST");
			ClientAPI.Provider provider = api.getProvider();
			accountManager = provider.getAccountManager();
		}				
	}
	
	public void testWriteHoursToKeep() throws UnknownSubscriberException, BrandNotSupportedException, TelusAPIException {				
		MethodInvocationManager.getInstance(writeDao, new MonitoringDataPointSleepTestDao(1000), null, null);		
		
		Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("9057160345");
		MethodInvocationManager.getInstance().setDataPointEndTime();
		MethodInvocationManager.getInstance().write();								// 1
		
		assertEquals(new Integer(1), writeDao.getWriteNumberCount().get(0));
						
		MethodInvocationManager.getInstance().setWriteDao(exceptionWriteDao);
		subscriber = accountManager.findSubscriberByPhoneNumber("9057160345");
		MethodInvocationManager.getInstance().setDataPointEndTime();
		MethodInvocationManager.getInstance().write();								// 0 ExceptionWriteDAO
		
		MethodInvocationManager.getInstance().setWriteDao(writeDao);
		MethodInvocationManager.getInstance().write();								// 1	
		assertEquals(new Integer(1), writeDao.getWriteNumberCount().get(1));
				
		MethodInvocationManager.getInstance().setHoursToKeepDao(new MonitoringDataPointHoursToKeepTestImpl());
		MethodInvocationManager.getInstance().setWriteDao(exceptionWriteDao);
		subscriber = accountManager.findSubscriberByPhoneNumber("9057160345");
		MethodInvocationManager.getInstance().setDataPointEndTime();
		MethodInvocationManager.getInstance().write();								// 0 ExceptionWriteDAO
		
		MethodInvocationManager.getInstance().setWriteDao(writeDao);
		MethodInvocationManager.getInstance().write();								// 0 - Should be called due to stale record
		assertEquals(new Integer(0), writeDao.getWriteNumberCount().get(2));
		
	}
	
	private class MonitoringDataPointHoursToKeepTestImpl implements IMonitoringDataPointHoursToKeepDao {

		public int getHoursToKeep() {
			return 1;
		}
		
	}

}
