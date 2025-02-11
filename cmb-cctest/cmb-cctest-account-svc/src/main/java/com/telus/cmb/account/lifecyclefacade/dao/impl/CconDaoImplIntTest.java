package com.telus.cmb.account.lifecyclefacade.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclefacade.dao.CconDao;
import com.telus.cmb.account.payment.CCMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-ldap.xml", "classpath:application-context-dao-lifecyclemanager.xml", 
		"classpath:application-context-datasources-informationhelper-testing-d3.xml"})
public class CconDaoImplIntTest {

	@Autowired
	CconDao cconDao;

	static {
		String url = "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration";
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.provider", "com.telus.provider.config.PropertiesOverridingConfigurationProvider");
		System.setProperty("com.telusmobility.config.propertiesFile", "configuration.properties");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", url);

	}
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadAppMessages() throws ApplicationException {
		Map<String, HashMap<String, CCMessage>> map = cconDao.loadAppMessages();
		Set<String> keys = map.keySet();
		assertEquals(5, keys.size());
	}

	@Test
	public void testLoadErrorCodeMappings() throws ApplicationException {
		Map<String, String> map = cconDao.loadErrorCodeMappings();
		Set<String> keys = map.keySet();
		assertEquals(18, keys.size());
	}
	
	/**
	 * This test verifies that the error code added for the PCI Wrap up Release
	 * exists 
	 * 
	 * @throws ApplicationException
	 */
	@Test
	public void testPCICleanUp() throws ApplicationException {
		String PCICleanUpResponseCode = "104";
		String PCICleanUpErrorCode = "AV100051";
		Map<String, HashMap<String, CCMessage>> appMessagesMap = cconDao.loadAppMessages();
		HashMap<String, CCMessage> valuesMap;
		CCMessage ccMessage;
		
		Map<String, String> errorCodeMap = cconDao.loadErrorCodeMappings();
		String value = errorCodeMap.get(PCICleanUpErrorCode);
		assertEquals(PCICleanUpResponseCode, value);		
		
		valuesMap = appMessagesMap.get("AMDOCS_DEFAULT");
		ccMessage = valuesMap.get(PCICleanUpResponseCode);
		assertEquals("Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date.   If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.", 
				ccMessage.getEnglishMessage());
		assertEquals("Nous sommes désolés de ne pouvoir traiter votre paiement par carte de crédit. Veuillez vérifier votre numéro de carte de crédit et la date d'expiration. Si vous éprouvez toujours des difficultés, veuillez communiquer avec le Service à la clientèle en composant le 611 à partir de votre téléphone.",
				ccMessage.getFrenchMessage());
		assertEquals("Transaction failure AV100051. Credit card expiry date is in the past.  A valid expiry date must be entered.",
				ccMessage.getKbMemoMessage());
		
		valuesMap = appMessagesMap.get("AMDOCS_CLIENT");
		ccMessage = valuesMap.get(PCICleanUpResponseCode);
		assertEquals("Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date.   If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.", 
				ccMessage.getEnglishMessage());
		assertEquals("Nous sommes désolés de ne pouvoir traiter votre paiement par carte de crédit. Veuillez vérifier votre numéro de carte de crédit et la date d'expiration. Si vous éprouvez toujours des difficultés, veuillez communiquer avec le Service à la clientèle en composant le 611 à partir de votre téléphone.",
				ccMessage.getFrenchMessage());
		assertEquals("Transaction failure AV100051. Credit card expiry date is in the past.  A valid expiry date must be entered.",
				ccMessage.getKbMemoMessage());
		
		valuesMap = appMessagesMap.get("AMDOCS_AGENT");
		ccMessage = valuesMap.get(PCICleanUpResponseCode);
		assertEquals("Sorry, we cannot complete the credit card payment. Please check the credit card number and expiration date.", 
				ccMessage.getEnglishMessage());
		assertEquals("Nous sommes désolés de ne pouvoir traiter votre paiement par carte de crédit. Veuillez vérifier votre numéro de carte de crédit et la date d'expiration.",
				ccMessage.getFrenchMessage());
		assertEquals("Transaction failure AV100051. Credit card expiry date is in the past.  A valid expiry date must be entered.",
				ccMessage.getKbMemoMessage());
		
	}

}
