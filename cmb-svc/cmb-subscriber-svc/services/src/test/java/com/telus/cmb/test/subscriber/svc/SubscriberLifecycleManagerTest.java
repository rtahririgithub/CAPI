/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.test.subscriber.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;

/**
 * @author Pavel Simonovsky
 *
 */

@Test
@ContextConfiguration(locations="classpath:application-context-test.xml")
@ActiveProfiles("standalone")
public class SubscriberLifecycleManagerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private SubscriberLifecycleManager manager;
	
	@Test
	public void getAirtimeRate() throws Exception {
		double rate = manager.getAirtimeRate(8, "", "");
		System.out.println(rate);
	}
}
