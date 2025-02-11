package com.telus.cmb.account.lifecyclemanager.dao.impl;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.dao.SubscriptionManagementServiceDao;
import com.telus.eas.framework.info.TestPointResultInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-lifecyclemanager-test.xml", 
		"classpath:application-context-dao-lifecyclemanager.xml",
		"classpath:application-context-wsclint-prepaid.xml",
		"classpath:application-context-datasources-informationhelper-testing-pt148.xml" 
		})
public class SubscriptionManagementServiceDaoImplIntTest {

	@Autowired
	private SubscriptionManagementServiceDao smsDao;

	String env = "D3";
	int banId;
	String phoneNum = "";

	@BeforeClass
	public static void beforeClass() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");//D3
		//System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");//PT148
		//System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");//PT168
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory"); 		
	}		
	
	private void setupEnv() {
		if (env.equals("PT168")) {
			System.setProperty("com.telusmobility.config.java.naming.provider.url",	"ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");
			banId = 70602349;
			phoneNum = "7781690712";
		} else if (env.equals("PT148")) {
			System.setProperty("com.telusmobility.config.java.naming.provider.url",	"ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
			//TODO update
			banId = 70690824;
			phoneNum = "4160704331";
		} else if (env.equals("D3")) {
			System.setProperty("com.telusmobility.config.java.naming.provider.url",	"ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
			banId = 70104002;
			phoneNum = "5198060070";
		}
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
	}

	@Test
	public void testSMSWS() throws ApplicationException {
		setupEnv();
		TestPointResultInfo result = smsDao.test();
		System.out.println(result.toString());
		assertEquals(true, result.isPass());
		assertEquals("3.0", result.getResultDetail());
	}
	
}
