package com.telus.cmb.account.lifecyclefacade.dao.impl;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.api.account.AuditHeader;
import com.telus.cmb.account.lifecyclefacade.dao.CardPaymentServiceDao;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.CreditCardHolderInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-lifecyclefacade-test.xml"})

public class CardPaymentDaoImplIntTest {

	@Autowired
	private CardPaymentServiceDao cardPaymentServiceDao;

	String env = "PT148";
	int banId;
	String phoneNum = "";

	@BeforeClass
	public static void beforeClass() {
	}
	
	private void setupEnv() {
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		//System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
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
	}

	@Test
	public void testCardPaymentService() throws ApplicationException {
		setupEnv();
		TestPointResultInfo result = cardPaymentServiceDao.test();
		System.out.println(result.toString());
		assertEquals(true, result.isPass());
	}
	
	@Test
	public void testprocessTransactionPurchase() throws ApplicationException {
		setupEnv();
		cardPaymentServiceDao.processCreditCard(getTermId(), getCCTxnInfo(), getAuditHeader());
	}
	
	private String getTermId() {
		return "APOLLO_1";//"6680A_1";
	}
	
	private CreditCardTransactionInfo getCCTxnInfo() {
		CreditCardTransactionInfo result = new CreditCardTransactionInfo();
		result.setAmount(100.00);
		result.setTransactionType(CreditCardTransactionInfo.TYPE_CREDIT_CARD_PAYMENT);
		result.setCreditCardInfo(getCC());
		result.setCreditCardHolderInfo(getCCHolder());
		return result;
	}
	
	private CreditCardHolderInfo getCCHolder() {
		CreditCardHolderInfo result = new CreditCardHolderInfo();
		result.setClientID("70699417");//BAN
		result.setlastName("YGHILL");
		result.setPostalCode("M1H3J3");
		result.setAccountType("B");
		result.setAccountSubType("P");
		return result;
	}
	
	private CreditCardInfo getCC() {
		CreditCardInfo cc = new CreditCardInfo();
		cc.setToken("100000000000004025872");//100000000000004119025
		cc.setExpiryMonth(2);
		cc.setExpiryYear(2020);
		cc.setLeadingDisplayDigits("341400");
		cc.setTrailingDisplayDigits("1000");
		return cc;
	}
	
	private AuditHeader getAuditHeader() {
		AuditHeader result = new AuditHeaderInfo();
		result.setUserIPAddress("142.63.44.30");
		result.setCustomerId("70699417");
		result.appendAppInfo("70699417", 12373, "142.63.44.30");
		return result;
	}
	
}
