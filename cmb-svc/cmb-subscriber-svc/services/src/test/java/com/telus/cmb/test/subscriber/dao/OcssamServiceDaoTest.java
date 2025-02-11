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

import com.telus.cmb.subscriber.lifecyclefacade.dao.OcssamServiceDao;

/**
 * @author R. Fong
 *
 */
@ContextConfiguration(locations="classpath:com/telus/cmb/test/subscriber/dao/test-context-dao.xml")
public class OcssamServiceDaoTest extends AbstractTestNGSpringContextTests {

	static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		
	}
	
	@Autowired
	private OcssamServiceDao dao;
	
	@Test
	public void updateAccountPurchaseAmount() throws Exception {
		
		try {
			int ban = 70787536;
			String subscriberId = "1234567890";
			dao.updateAccountPurchaseAmount(ban, subscriberId, subscriberId, 100, 125, "CHARGE", "11");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}