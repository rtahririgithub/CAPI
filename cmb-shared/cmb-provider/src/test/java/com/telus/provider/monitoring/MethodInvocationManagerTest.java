package com.telus.provider.monitoring;

import junit.framework.Assert;

import com.telus.api.BaseTest;
import com.telus.api.ClientAPI;
import com.telus.api.account.Account;
import com.telus.api.account.AccountManager;
import com.telus.api.account.Subscriber;

public class MethodInvocationManagerTest extends AMethodInvocationManagerTest{
	

	private AccountManager accountManager;
	private ClientAPI api = null;
	private MonitoringDataPointWriteTestDao writeDao;
	private MonitoringDataPointSleepTestDao sleepDao = new MonitoringDataPointSleepTestDao();
	
	String phoneNumber;
	
	static {
		BaseTest.setupD3();
	}
	
	public void setUp() throws Exception {	
		
		if (api == null) {
			api = ClientAPI.getInstance("18654", "apollo", "OLN");
			ClientAPI.Provider provider = api.getProvider();
			accountManager = provider.getAccountManager();
		}				
				
		if (System.getProperty("com.telus.provider.providerURL").equals("pt168")) {
			phoneNumber = "4161655710";
			
		} else {
			phoneNumber = "9057160345";
		}
		
		
		writeDao = new MonitoringDataPointWriteTestDao();
		MethodInvocationManager.getInstance(writeDao, sleepDao, null, null);
	}

	public void testWriteRemovesDataEntry() throws Exception {
		
		MethodInvocationManager.getInstance().setDataPointEndTime();					
		MethodInvocationManager.getInstance().write();								// 0
		
		Subscriber subscriber = accountManager.findSubscriberByPhoneNumber(phoneNumber);
		MethodInvocationManager.getInstance().setDataPointEndTime();
		MethodInvocationManager.getInstance().write();								// 1
		
		MethodInvocationManager.getInstance().write();								// 0
		MethodInvocationManager.getInstance().write();								// 0
		
		// Should be 3
		subscriber.getDiscounts();
		subscriber.getDiscounts();
		subscriber.getDiscounts();
		subscriber.getAddress();		// Not included in count.
		subscriber.getActivityCode(); // Not included in count.
		Account account = accountManager.findAccountByPhoneNumber(phoneNumber);		
		account.getAddress();			// Not included in count.
		MethodInvocationManager.getInstance().setDataPointEndTime();
		MethodInvocationManager.getInstance().write();								// 2
				
		
		System.out.println (writeDao.getWriteNumberCount());
		
		assertEquals(new Integer(0), writeDao.getWriteNumberCount().get(0));
		assertEquals(new Integer(1), writeDao.getWriteNumberCount().get(1));
		assertEquals(new Integer(0), writeDao.getWriteNumberCount().get(2));
		assertEquals(new Integer(0), writeDao.getWriteNumberCount().get(3));
		assertEquals(new Integer(2), writeDao.getWriteNumberCount().get(4));	
		
		writeDao.resetWriteNumberCount();
	}
	
	
	public void testWriteNoneWhenEndTimeNotSet() throws Exception {		
		writeDao = new MonitoringDataPointWriteTestDao();
		MethodInvocationManager.getInstance().setWriteDao(writeDao);
		
		accountManager.findSubscriberByPhoneNumber(phoneNumber);
		MethodInvocationManager.getInstance().write();								// 0
				
		MethodInvocationManager.getInstance().setDataPointEndTime();
		MethodInvocationManager.getInstance().write();								// 1
		
		
		assertEquals(new Integer(0), writeDao.getWriteNumberCount().get(0));	
		assertEquals(new Integer(1), writeDao.getWriteNumberCount().get(1));	
	}
	
	public void testHandleExceptionGracefully() {		
		MethodInvocationManager.getInstance().setWriteDao(new MonitoringDataPointWriteTestThrowsExeptionDao());
		MethodInvocationManager.getInstance().write();
	}
	
	public void testWriteFail() throws Exception {
		accountManager.findSubscriberByPhoneNumber(phoneNumber);
		MethodInvocationManager.getInstance().setDataPointEndTime();
		MethodInvocationManager.getInstance().setWriteDao(new MonitoringDataPointWriteTestThrowsExeptionDao());
		MethodInvocationManager.getInstance().write();								// 0 Throws exception
		accountManager.findSubscriberByPhoneNumber(phoneNumber);
		MethodInvocationManager.getInstance().setDataPointEndTime();
		MethodInvocationManager.getInstance().setWriteDao(writeDao);
		MethodInvocationManager.getInstance().write();								// 1
		
		assertEquals(new Integer(2), writeDao.getWriteNumberCount().get(0));			
	}
	
	public void testExceptionPropogation() throws Throwable {
		try {
			accountManager.findSubscriberByPhoneNumber("");
			fail("Exception expected");
		} catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) {
				throw t;
			} else {			
				t.printStackTrace();
			}
			
		}
	}
	public void testExceptionStillLogsEntry() throws Throwable {
		try {
			accountManager.findSubscriberByPhoneNumber("");
			fail("Exception expected");
		} catch (Throwable t) {
			if (t instanceof junit.framework.AssertionFailedError) {
				throw t;
			} else {			
				t.printStackTrace();
				writeDao = new MonitoringDataPointWriteTestDao();
				MethodInvocationManager.getInstance().setWriteDao(writeDao);
				MethodInvocationManager.getInstance().setDataPointEndTime();
				MethodInvocationManager.getInstance().write();
				assertEquals(new Integer(1), writeDao.getWriteNumberCount().get(0));
			}
			
		}
	}
}
