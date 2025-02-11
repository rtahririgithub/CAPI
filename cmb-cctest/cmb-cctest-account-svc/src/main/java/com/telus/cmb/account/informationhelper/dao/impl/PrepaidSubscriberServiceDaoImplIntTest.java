package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.cmb.account.informationhelper.dao.PrepaidSubscriberServiceDao;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

@ContextConfiguration(locations = {"classpath:application-context-informationhelper-test.xml",
"classpath:application-context-wsclint-prepaid.xml"})

public class PrepaidSubscriberServiceDaoImplIntTest extends BaseInformationHelperIntTest {

	@Autowired
	private PrepaidSubscriberServiceDao pssDao;

	String env = "D3";
	int banId;
	String phoneNum = "";

	@BeforeClass
	public static void beforeClass() {
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
	public void testPWCOSWS() throws ApplicationException {
		setupEnv();
		TestPointResultInfo result = pssDao.test();
		System.out.println(result.toString());
		assertEquals(true, result.isPass());
		assertEquals("3.0", result.getResultDetail());
	}
	
	@Test
	public void testGetdetails() throws ApplicationException {
		setupEnv();
		PrepaidConsumerAccountInfo account = pssDao.retrieveAccountInfo(banId, phoneNum);
		System.out.println(account);
	}
	
	@Test
	public void testRetrieveAutoTopUpInfo() throws ApplicationException {
		setupEnv();
		AutoTopUpInfo autoTopup = pssDao.retrieveAutoTopUpInfo(banId, phoneNum);
		System.out.println(autoTopup);
	}
}
