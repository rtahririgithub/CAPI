package com.telus.cmb.account.lifecyclefacade.dao.impl;

import java.text.SimpleDateFormat;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.transaction.info.AuditInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-lifecyclefacade-test.xml"})

public class CreditProfileSvcDaoImpIntTest {

	@Autowired
	private CreditProfileSvcV30DaoImpl creditProfileSvcDao;
	
	@BeforeClass
	public static void beforeClass() {	
		//System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration"); 
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory"); 		
}		
	@Test
	public void testCheckCredit() throws Throwable {
		PostpaidConsumerAccountInfo accountInfo = PostpaidConsumerAccountInfo.newPCSInstance();
		accountInfo.setBanId(27527794);
		accountInfo.setAccountType('I');
		accountInfo.setAccountSubType('R');
		accountInfo.setAccountCategory("R");
		accountInfo.setCustomerId(27527794);
		accountInfo.setBrandId(1);
		accountInfo.setBanSegment("TCSQ");
		accountInfo.setBanSubSegment("OTHR");
		accountInfo.setStatus('T');
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		accountInfo.setStatusDate(dateFormat.parse("2014-04-11"));
		accountInfo.setStatusActivityCode("TEN");
		accountInfo.setStatusActivityReasonCode("NB");
		accountInfo.setCreateDate(dateFormat.parse("2014-04-11"));
		accountInfo.setBillCycle(43);
		accountInfo.setBillCycleCloseDay(13);
		AddressInfo addressInfo=new AddressInfo();
		addressInfo.setAddressType("C");
		addressInfo.setPrimaryLine("1102 ALAINBOURG RUE");
		addressInfo.setCity("SAINT-JEAN-CHRYSOSTOME");
		addressInfo.setProvince("PQ");
		addressInfo.setPostalCode("G6Z3A1");
		addressInfo.setCountry("CAN");
		addressInfo.setCivicNo("1102");
		addressInfo.setStreetNumber("1102");
		addressInfo.setStreetName("ALAINBOURG");
		addressInfo.setStreetType("RUE");		
		accountInfo.setAddress0(addressInfo);
		accountInfo.setHomeProvince("PQ");
		accountInfo.setHomePhone("4183040510");
		accountInfo.setPin("1969");
		accountInfo.setEmail("coulombejosee@hotmail­.com");
		accountInfo.setLanguage("FR");
		accountInfo.setDealerCode("1100000719");
		accountInfo.setSalesRepCode("7WJV");
		accountInfo.setClientConsentIndicatorCodes(new String[]{"2"});
		accountInfo.getContactName().setTitle("MRS.");
		accountInfo.getContactName().setFirstName("JOSEE");
		accountInfo.getContactName().setLastName("COULOMBE");
		accountInfo.getContactName().setNameFormat("P");
		//accountInfo.setFullName("JOSEE COULOMBE");
		BusinessCreditIdentityInfo selectedBusinessCreditIdentiy = new BusinessCreditIdentityInfo();
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setUserId("arobitaille10");
		auditInfo.setUserTypeCode("4");
		auditInfo.setSalesRepId("7WJV");
		auditInfo.setChannelOrgId("03138");
		auditInfo.setOriginatorAppId("8473");
		auditInfo.setTimestamp(dateFormat.parse("2014-04-11"));
		AuditHeaderInfo headerInfo = new AuditHeaderInfo();
		headerInfo.setCustomerId("arobitaille10,0");
		headerInfo.setUserIPAddress("206.162.187.5:192.168.156.231");
		headerInfo.appendAppInfo("arobitaille10,0",8473,"206.162.187.5:192.168.156.231");
		boolean isCreditCheckForBusiness = false;
		creditProfileSvcDao.checkCredit(accountInfo, selectedBusinessCreditIdentiy, auditInfo, headerInfo, isCreditCheckForBusiness);
		//System.out.println(creditProfileSvcDao.ping());
	}

}
