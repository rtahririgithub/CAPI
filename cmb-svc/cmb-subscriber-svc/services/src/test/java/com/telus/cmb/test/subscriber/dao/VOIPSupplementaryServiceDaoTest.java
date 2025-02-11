/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.test.subscriber.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.common.dao.provisioning.VOIPSupplementaryServiceDao;
import com.telus.eas.account.info.VOIPAccountInfo;

/**
 * @author R. Fong
 *
 */
@ContextConfiguration(locations="classpath:com/telus/cmb/test/subscriber/dao/test-context-dao.xml")
public class VOIPSupplementaryServiceDaoTest extends AbstractTestNGSpringContextTests {

	static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		
	}
	
	@Autowired
	private VOIPSupplementaryServiceDao dao;
	
	@Test
	public void getAccountInfo() throws Exception {
		
		try {
			VOIPAccountInfo voipAccountInfo = dao.getVOIPAccountInfo(123);
			System.out.println(voipAccountInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getPrimaryStarterSeatSubscriptionId() throws Exception {
		
		try {
			String subscriptionId = dao.getPrimaryStarterSeatSubscriptionId(70754459);
			System.out.println(subscriptionId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}