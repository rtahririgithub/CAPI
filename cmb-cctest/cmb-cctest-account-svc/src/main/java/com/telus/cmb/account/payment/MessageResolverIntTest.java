package com.telus.cmb.account.payment;


import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.cmb.account.lifecyclefacade.dao.CconDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-dao-lifecyclemanager.xml", 
		"classpath:application-context-datasources-informationhelper-testing-d3.xml"})
public class MessageResolverIntTest {

	@Autowired
	CconDao cconDao;	
	MessageResolver msgResolver;
	
	@Before
	public void setUp() throws Exception {
		msgResolver = new MessageResolver(cconDao);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * This test verifies that the error code added for the PCI Wrap up Release
	 * exists 
	 */
	@Test
	public void testPCICleanUp() {
		String PCICleanUpErrorCode = "AV100051";
		
		String msgId = msgResolver.mapMessageId(PCICleanUpErrorCode);
		
		String applicationId = "SMARTDESKTOP";
		String businessRole = "";
		CCMessage messages = msgResolver.resolveMessage( applicationId , businessRole, msgId, "no default" );
		
		assertEquals("Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date.   If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.", 
				messages.getEnglishMessage());
		assertEquals("Nous sommes désolés de ne pouvoir traiter votre paiement par carte de crédit. Veuillez vérifier votre numéro de carte de crédit et la date d'expiration. Si vous éprouvez toujours des difficultés, veuillez communiquer avec le Service à la clientèle en composant le 611 à partir de votre téléphone.",
				messages.getFrenchMessage());
		assertEquals("Transaction failure AV100051. Credit card expiry date is in the past.  A valid expiry date must be entered.",
				messages.getKbMemoMessage());
		
		businessRole = "CLIENT";
		messages = msgResolver.resolveMessage( applicationId , businessRole, msgId, "no default" );
		assertEquals("Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date.   If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.", 
				messages.getEnglishMessage());
		assertEquals("Nous sommes désolés de ne pouvoir traiter votre paiement par carte de crédit. Veuillez vérifier votre numéro de carte de crédit et la date d'expiration. Si vous éprouvez toujours des difficultés, veuillez communiquer avec le Service à la clientèle en composant le 611 à partir de votre téléphone.",
				messages.getFrenchMessage());
		assertEquals("Transaction failure AV100051. Credit card expiry date is in the past.  A valid expiry date must be entered.",
				messages.getKbMemoMessage());
		
		businessRole = "AGENT";
		messages = msgResolver.resolveMessage( applicationId , businessRole, msgId, "no default" );
		assertEquals("Sorry, we cannot complete the credit card payment. Please check the credit card number and expiration date.", 
				messages.getEnglishMessage());
		assertEquals("Nous sommes désolés de ne pouvoir traiter votre paiement par carte de crédit. Veuillez vérifier votre numéro de carte de crédit et la date d'expiration.",
				messages.getFrenchMessage());
		assertEquals("Transaction failure AV100051. Credit card expiry date is in the past.  A valid expiry date must be entered.",
				messages.getKbMemoMessage());
		
	}
}
