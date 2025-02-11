package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.telus.cmb.subscriber.lifecyclemanager.dao.PrepaidSubscriberServiceDao;
import com.telus.eas.subscriber.info.PrepaidSubscriberInfo;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-wsclient-prepaid.xml",
		"classpath:application-context-dao-lifecyclemanager.xml",
		"classpath:application-context-datasources-lifecyclehelper-testing-d3.xml",
		"classpath:application-context-common.xml"})
public class PrepaidSubscriberServiceDaoImplTest {
	
	@Autowired
	PrepaidSubscriberServiceDao prepaidSubscriberServiceDao;
	String phoneNumber = "6470000008";
	
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");//D3
		//System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");//PT148
		//System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");//PT168
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory"); 		
	}
	
	// write the test cases to test expiry date and language .
	
	@Test
	public void testUpdateSubscriberRateId() throws Exception {
		long rateId=6;
		try {
			PrepaidSubscriberInfo prepaidSubscriber = new PrepaidSubscriberInfo();
			prepaidSubscriber.setPhoneNumber("4169060049");
			prepaidSubscriber.setRateId(rateId);
			prepaidSubscriberServiceDao.updatePrepaidSubscriber(prepaidSubscriber);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateSubscriberBalanceExpiryDate() throws Exception {
		try {
			PrepaidSubscriberInfo prepaidSubscriber = new PrepaidSubscriberInfo();
			prepaidSubscriber.setPhoneNumber("4169060049");
			prepaidSubscriber.setExpiryDate(new Date());
			prepaidSubscriberServiceDao.updatePrepaidSubscriber(prepaidSubscriber);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	

	@Test
	public void testUpdateSubscriberLanguage() throws Exception {
		//Getting illegalargument exception.
		try {
			PrepaidSubscriberInfo prepaidSubscriber = new PrepaidSubscriberInfo();
			prepaidSubscriber.setPhoneNumber("4169060049");
			prepaidSubscriber.setLanguage("fr");
			prepaidSubscriberServiceDao.updatePrepaidSubscriber(prepaidSubscriber);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	
	
}
