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

import com.telus.api.ApplicationException;
import com.telus.api.account.ActivationOptionType;
import com.telus.cmb.account.lifecyclefacade.dao.WirelessCreditAssessmentProxyServiceDao;
import com.telus.eas.account.credit.info.CreditAssessmentInfo;
import com.telus.eas.transaction.info.AuditInfo;

/**
 * @author R. Fong
 *
 */
@ContextConfiguration(locations="classpath:com/telus/cmb/test/account/dao/test-context-dao.xml")
public class WirelessCreditAssessmentProxyServiceDaoTest extends AbstractTestNGSpringContextTests {

	static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		
	}
	
	@Autowired
	private WirelessCreditAssessmentProxyServiceDao dao;
	
	@Test
	public void overrideCreditWorthiness() throws Exception {
		
		try {
			CreditAssessmentInfo info = dao.overrideCreditWorthiness(8, ActivationOptionType.CREDIT_LIMIT, "X", 500, null, createAuditInfo(), null);
			System.out.println(info.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private AuditInfo createAuditInfo() throws ApplicationException {

		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setinternalPopulatedAppId(true);
		auditInfo.setUserId("1234");
		auditInfo.setUserTypeCode("2");
		auditInfo.setChannelOrgId("1234");
		auditInfo.setOriginatorAppId("ClientAPI_EJB");

		return auditInfo;
	}
	
}