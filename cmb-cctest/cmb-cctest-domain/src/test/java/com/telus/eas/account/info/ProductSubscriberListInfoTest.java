package com.telus.eas.account.info;

import junit.framework.TestCase;

public class ProductSubscriberListInfoTest extends TestCase {
	
	ProductSubscriberListInfo psli = new ProductSubscriberListInfo();
	SubscriberIdentifierInfo[] si = new SubscriberIdentifierInfo[5];
	String productType;
	
	protected void setUp() throws Exception {
		super.setUp();
		productType = "TEST";
		
		si[0] = new SubscriberIdentifierInfo();
		si[0].setSubscriberId("0");
		si[0].setSubscriptionId(0L);
		si[1] = new SubscriberIdentifierInfo();
		si[1].setSubscriberId("1");
		si[1].setSubscriptionId(1L);
		si[2] = new SubscriberIdentifierInfo();
		si[2].setSubscriberId("2");
		si[2].setSubscriptionId(2L);
		si[3] = new SubscriberIdentifierInfo();
		si[3].setSubscriberId("3");
		si[3].setSubscriptionId(3L);
		si[4] = new SubscriberIdentifierInfo();
		si[4].setSubscriberId("4");
		si[4].setSubscriptionId(4L);		
	}

	public void testProductType() {
		assertNotSame(psli.getProductType(), productType);
		psli.setProductType(productType);
		assertEquals(psli.getProductType(), productType);
	}

	public void testGetActiveSubscribers() {
		assertNull(psli.getActiveSubscribers());
		psli.setActiveSubscriberIdentifiers(si);
		assertEquals(5, psli.getActiveSubscribers().length);
		assertEquals("0", psli.getActiveSubscribers()[0]);
	
		assertNull(psli.getCancelledSubscribers());
		assertNull(psli.getReservedSubscribers());
		assertNull(psli.getSuspendedSubscribers());
	}

	public void testGetCancelledSubscribers() {
		assertNull(psli.getCancelledSubscribers());
		psli.setCancelledSubscriberIdentifiers(si);
		assertEquals(5, psli.getCancelledSubscribers().length);
		assertEquals("1", psli.getCancelledSubscribers()[1]);
		
		assertNull(psli.getActiveSubscribers());
		assertNull(psli.getReservedSubscribers());
		assertNull(psli.getSuspendedSubscribers());
	}

	public void testGetReservedSubscribers() {
		assertNull(psli.getReservedSubscribers());
		psli.setReservedSubscriberIdentifiers(si);
		assertEquals(5, psli.getReservedSubscribers().length);
		assertEquals("3", psli.getReservedSubscribers()[3]);
		
		assertNull(psli.getActiveSubscribers());
		assertNull(psli.getCancelledSubscribers());
		assertNull(psli.getSuspendedSubscribers());
	}

	public void testGetSuspendedSubscribers() {
		assertNull(psli.getSuspendedSubscribers());
		psli.setSuspendedSubscriberIdentifiers(si);
		assertEquals(5, psli.getSuspendedSubscribers().length);
		assertEquals("2", psli.getSuspendedSubscribers()[2]);
		
		assertNull(psli.getActiveSubscribers());
		assertNull(psli.getCancelledSubscribers());
		assertNull(psli.getReservedSubscribers());
	}


	public void testGetActiveSubscribersCount() {
		assertEquals(0, psli.getActiveSubscribersCount());
		psli.setActiveSubscriberIdentifiers(si);
		assertEquals(5, psli.getActiveSubscribersCount());
	}

	public void testGetCancelledSubscribersCount() {
		assertEquals(0, psli.getCancelledSubscribersCount());
		psli.setCancelledSubscriberIdentifiers(si);
		assertEquals(5, psli.getCancelledSubscribersCount());
	}

	public void testGetReservedSubscribersCount() {
		assertEquals(0, psli.getReservedSubscribersCount());
		psli.setReservedSubscriberIdentifiers(si);
		assertEquals(5, psli.getReservedSubscribersCount());
	}

	public void testGetSuspendedSubscribersCount() {
		assertEquals(0, psli.getSuspendedSubscribersCount());
		psli.setSuspendedSubscriberIdentifiers(si);
		assertEquals(5, psli.getSuspendedSubscribersCount());
	}
}
