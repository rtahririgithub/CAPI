package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;


import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.cmb.subscriber.lifecyclemanager.dao.PrepaidWirelessCustomerOrderServiceDao;
import com.telus.eas.account.info.ActivationFeaturesPurchaseArrangementInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-lifecyclemanager-prepaidservices-dao-test.xml"})
public class PrepaidWirelessCustomerOrderServiceDaoImplTest {

	
	@Autowired
	//@Qualifier("prepaidWirelessCustomerOrderServiceDao")
	PrepaidWirelessCustomerOrderServiceDao dao;
	
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
		//System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory"); 		
	}		

	@Test
	public void testSaveActivationFeaturesPurchaseArrangement() throws Exception {
		try {
			ActivationFeaturesPurchaseArrangementInfo[] info = new ActivationFeaturesPurchaseArrangementInfo[1];
			ActivationFeaturesPurchaseArrangementInfo infoObj = new ActivationFeaturesPurchaseArrangementInfo();
			infoObj.setFeatureId("511");
			infoObj.setAutoRenewIndicator("true");
			infoObj.setPurchaseFundSource(1);
			infoObj.setAutoRenewFundSource(1);
			info[0]=infoObj;
			SubscriberInfo subinfo=new SubscriberInfo();
			subinfo.setBanId(70679613);
			subinfo.setPhoneNumber("4160701260");
			subinfo.setSerialNumber("8912239900000907628");
			dao.saveActivationFeaturesPurchaseArrangement(subinfo, info, "Palaksha");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
