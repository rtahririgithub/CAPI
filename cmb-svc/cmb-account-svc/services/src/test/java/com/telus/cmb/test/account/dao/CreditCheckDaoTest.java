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

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.account.informationhelper.dao.CreditCheckDao;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;

/**
 * @author R. Fong
 *
 */
@ContextConfiguration(locations="classpath:com/telus/cmb/test/account/dao/test-context-dao.xml")
public class CreditCheckDaoTest extends AbstractTestNGSpringContextTests {

	static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		
	}
	
	@Autowired
	private CreditCheckDao dao;
	
	@Test
	public void retrieveDepositsByBan() throws Exception {

		CreditCheckResultDepositInfo[] deposits = dao.retrieveDepositsByBan(70784088); // 70771721 70775239 70784088
		if (ArrayUtils.isNotEmpty(deposits)) {
			for (CreditCheckResultDepositInfo info : deposits) {
				System.out.println(info.toString());
			}
		}
	}
	
	@Test
	public void retrieveLastCreditCheckResultByBan() throws Exception {

		CreditCheckResultInfo info = dao.retrieveLastCreditCheckResultByBan(70788464, "C"); // 70771721 70775239 70784088 70780702 70788418 70788464
		System.out.println(info.toString());		
		System.out.println(info.getLastCreditCheckPersonalnformation().toString());
	}

}