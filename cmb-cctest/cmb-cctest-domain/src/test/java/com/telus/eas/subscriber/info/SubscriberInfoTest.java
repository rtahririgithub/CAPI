package com.telus.eas.subscriber.info;

import junit.framework.TestCase;

public class SubscriberInfoTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testCopyFrom() {
		SubscriberInfo si = new SubscriberInfo();		
		assertEquals(0, si.getSubscriptionId());
		
		SubscriberInfo siNew = new SubscriberInfo();
		siNew.setSubscriptionId(12);		
		si.copyFrom(siNew);		
		assertEquals(12, si.getSubscriptionId());
	}

}
