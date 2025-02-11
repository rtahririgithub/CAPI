/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.test.account.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import com.telus.cmb.common.dao.billnotification.BillNotificationManagementDao;
import com.telus.eas.account.info.BillMediumInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

@ContextConfiguration(locations="classpath:com/telus/cmb/test/account/dao/test-context-dao.xml")
public class BillNotificationManagementDaoTest extends AbstractTestNGSpringContextTests {

	static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		
	}
	
	@Autowired
	private BillNotificationManagementDao dao;
	
	@Test
	public void getBillMediumInfo() throws Exception {
		
		try {
			BillMediumInfo info = dao.getBillMediumInfo(70813994);
			System.out.println(info.getBillTypeList());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Test
	public void pingTest() throws Exception {
		
		try {
			TestPointResultInfo info = dao.test();
			System.out.println(info);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}