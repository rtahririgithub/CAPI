package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.api.ApplicationException;
import com.telus.api.account.AuditHeader;
import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.cmb.account.informationhelper.dao.PrepaidSubscriberServiceDao;
import com.telus.cmb.account.informationhelper.dao.PrepaidWirelessCustomerOrderServiceDao;
import com.telus.eas.account.info.ActivationTopUpInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

@ContextConfiguration(locations = {"classpath:application-context-informationhelper-test.xml",
		"classpath:application-context-wsclint-prepaid.xml"})

public class PrepaidDaoImplIntTest extends BaseInformationHelperIntTest{

//	@Autowired
//	PrepaidDaoImpl dao;
	@Autowired
	Integer prepaidBAN;
	@Autowired
	String prepaidSub;
	@Autowired
	PrepaidConsumerAccountInfo prepaidConsumerAccountInfo;
	@Autowired
	ActivationTopUpInfo activationTopUpInfo;
	@Autowired
	private PrepaidWirelessCustomerOrderServiceDao pwcosDao;
	@Autowired
	private PrepaidSubscriberServiceDao pssDao;

	String env = "PT148";
	int banId;
	String phoneNum = "";
	String appId = "sserve";

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
			banId = 70706728;
			phoneNum = "4160701933";
		} else if (env.equals("D3")) {
			System.setProperty("com.telusmobility.config.java.naming.provider.url",	"ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
			banId = 70104002;
			phoneNum = "5198060070";
		}
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
	}
	
	/**
	
	@Test
	@Deprecated
	public void testShakedownLib() throws ApplicationException {
		setupEnv();
		TestPointResultInfo result = dao.test();
		System.out.println(result.toString());
	}
	
	
	@Test
	@Deprecated
	public void testRetrieveAccountInfoIntString() throws ApplicationException{
		setupEnv();
		PrepaidConsumerAccountInfo accountInfo = dao.retrieveAccountInfo(banId, phoneNum);
		System.out.println("accountInfo:" + accountInfo.toString());
		assertEquals(banId, accountInfo.getBanId());
		assertNotNull(accountInfo.getBalance());
		assertEquals(0, accountInfo.getBalance(), 0);
	}

	@Test
	@Deprecated
	public void testRetrieveAccountInfoPrepaidConsumerAccountInfoString() throws ApplicationException{
		PrepaidConsumerAccountInfo ai = new PrepaidConsumerAccountInfo();
		ai.setBanId(banId);

		dao.retrieveAccountInfo(ai, phoneNum);
		assertEquals(banId, ai.getBanId());			
		assertNotNull(ai.getBalance());
		assertEquals(0, ai.getBalance(), 0);
	}

	/**
	 * This method is not well tested due to dependency on prepaid system
	 * @throws Exception
	 */
/**	@Test
	@Deprecated
	public void testGetPrepaidActivationCredit_Deprecated() throws Exception {
		String applicationId = "notImportantAppId";
		String pUserId = "notEvenUsed";
		PrepaidConsumerAccountInfo pPrepaidConsumerAccountInfo = new PrepaidConsumerAccountInfo();
		double prepaidActivationCredit = dao.getPrepaidActivationCredit(applicationId, pUserId, pPrepaidConsumerAccountInfo);
		assertEquals(0.0, prepaidActivationCredit, 0.0);
	}

	@Test
	@Deprecated
	public void testvalidatePayAndTalkSubscriberActivation_Deprecated() throws ApplicationException{
		String applicationId = "notImportantAppId";
		AuditHeader auditHeader = new AuditHeaderInfo();
		String userId = "92199";
		dao.validatePayAndTalkSubscriberActivation(applicationId, userId, prepaidConsumerAccountInfo, auditHeader);
	}

	**/
	
	
	
	
	@Test
	public void testShakedownWS() throws ApplicationException {
		testPWCOSWS();
		testPSSWS();
	}

	@Test
	public void testPWCOSWS() throws ApplicationException {
		setupEnv();
		TestPointResultInfo result = pwcosDao.test();
		System.out.println(result.toString());
		assertEquals(true, result.isPass());
		assertEquals("Success", result.getResultDetail());
	}
	
	@Test
	public void testPSSWS() throws ApplicationException {
		setupEnv();
		TestPointResultInfo result = pssDao.test();
		System.out.println(result.toString());
		assertEquals(true, result.isPass());
		assertEquals("3.0", result.getResultDetail());
	}
	
	
	// PrepaidSubscriberServiceDao
	@Test
	public void testGetdetails() throws ApplicationException {
		setupEnv();
		banId = 70696700;
		phoneNum = "4161743905";
		PrepaidConsumerAccountInfo account = pssDao.retrieveAccountInfo(banId, phoneNum);
		System.out.println(account);
	}
	
	@Test
	public void testRetrieveAutoTopUpInfo() throws ApplicationException {
		setupEnv();
		AutoTopUpInfo autoTopup = pssDao.retrieveAutoTopUpInfo(banId, phoneNum);
		System.out.println(autoTopup);
	}

	
	// PrepaidWirelessCustomerOrderServiceDao
	@Test
	public void testValidatePayAndTalkSubscriberActivation() throws ApplicationException {
		setupEnv();
		AuditHeader auditHeader = new AuditHeaderInfo();
		String userId = "92199";
		PrepaidConsumerAccountInfo prepaidConsumerAccountInfo = pssDao.retrieveAccountInfo(banId, phoneNum);
		//TODO fix data issue
		pwcosDao.validatePayAndTalkSubscriberActivation(appId, userId, prepaidConsumerAccountInfo, auditHeader);
	}
	
	@Test
	public void testGetPrepaidActivationCredit() throws ApplicationException {
		setupEnv();
		PrepaidConsumerAccountInfo prepaidConsumerAccountInfo = pssDao.retrieveAccountInfo(banId, phoneNum);
		double credit = pwcosDao.getPrepaidActivationCredit(appId, prepaidConsumerAccountInfo);
		System.out.println("Creatit of banId " + banId + " and phone " + phoneNum + " is: " + credit);
	}
	
	@Test
	public void testTetPrepaidActivationCredit() throws ApplicationException {
		//TODO
	}
	
}
