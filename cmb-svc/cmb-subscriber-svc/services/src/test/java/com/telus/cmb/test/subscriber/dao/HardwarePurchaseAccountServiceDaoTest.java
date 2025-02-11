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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.subscriber.lifecyclefacade.dao.HardwarePurchaseAccountServiceDao;
import com.telus.eas.hpa.info.RewardAccountInfo;

/**
 * @author R. Fong
 *
 */
@ContextConfiguration(locations="classpath:com/telus/cmb/test/subscriber/dao/test-context-dao.xml")
public class HardwarePurchaseAccountServiceDaoTest extends AbstractTestNGSpringContextTests {

	static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		
	}
	
	@Autowired
	private HardwarePurchaseAccountServiceDao dao;
	
	@Test
	public void getRewardAccount() throws Exception {
		
		try {
			List<RewardAccountInfo> rewardAccountList = dao.getRewardAccount(70737652, "5141654916", 8371054);
			for (RewardAccountInfo info : rewardAccountList) {
				System.out.println(info);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}