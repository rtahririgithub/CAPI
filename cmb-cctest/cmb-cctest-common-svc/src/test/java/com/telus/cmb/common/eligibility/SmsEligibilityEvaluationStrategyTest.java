package com.telus.cmb.common.eligibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.IOException;

import mockit.Mock;
import mockit.MockUp;

import org.junit.Before;
import org.junit.Test;

import com.telus.api.account.SmsEligibilityCheckResult;
import com.telus.eas.eligibility.info.SmsEligibilityCheckResultInfo;

public class SmsEligibilityEvaluationStrategyTest {
	
	private static String xmlString = null;
	
	@Test
	public void testActivationForMemoTypeACPO() throws Exception {
		SmsEligibilityCheckCriteria criteria = new SmsEligibilityCheckCriteria();
		criteria.setProductType("C");
		criteria.setEquipmentType("P");
		criteria.setProcessType("Activation");
		criteria.setAccountCombinedType("IR");
		criteria.setBrandId(1);

		SmsEligibilityCheckResultInfo result = (SmsEligibilityCheckResultInfo)SmsEligibilityEvaluationStrategy.getInstance().evaluate(criteria);
		assertNotNull(result);
		assertTrue(result.getSendSMS());
		assertEquals(result.getSmsTemplate(), "248");
		assertEquals(result.getContactEventTypeId(), "129");
		assertEquals(result.getMemoType(), "ACPO");
		assertEquals(result.getMemoText(), "L&R Campaign - client is new and received SMS to visit mobility.telus.com/gettingstarted for getting started help.");
		assertEquals(Double.valueOf(result.getMessageDelay()), new Double(3600));
	}
	
	@Test
	public void testActivationForMemoTypeACKA() throws Exception {
		SmsEligibilityCheckCriteria criteria = new SmsEligibilityCheckCriteria();	
		criteria.setProductType("C");
		criteria.setEquipmentType("D");
		criteria.setProcessType("Activation");
		criteria.setAccountCombinedType("BP");
		criteria.setBrandId(3);
		SmsEligibilityCheckResultInfo result = (SmsEligibilityCheckResultInfo)SmsEligibilityEvaluationStrategy.getInstance().evaluate(criteria);
		assertTrue(result.getSendSMS());
		assertEquals(result.getSmsTemplate(), "250");
		assertEquals(result.getContactEventTypeId(), "129");
		assertEquals(result.getMemoType(), "ACKA");
		assertEquals(result.getMemoText(), "Koodo Campaign - client is new and received SMS to visit koodomobile.com/help for device help.");
		assertEquals(Double.valueOf(result.getMessageDelay()), new Double(3600));
	}
	@Test
	public void testActivationForMemoTypeACID() throws Exception {
		SmsEligibilityCheckCriteria criteria = new SmsEligibilityCheckCriteria();	
		criteria.setProductType("C");
		criteria.setEquipmentType("3");
		criteria.setProcessType("Activation");
		criteria.setAccountCombinedType("BP");
		criteria.setBrandId(1);
		SmsEligibilityCheckResultInfo result = (SmsEligibilityCheckResultInfo)SmsEligibilityEvaluationStrategy.getInstance().evaluate(criteria);
		assertTrue(result.getSendSMS());
		assertEquals(result.getSmsTemplate(), "249");
		assertEquals(result.getContactEventTypeId(), "129");
		assertEquals(result.getMemoType(), "ACID");
		assertEquals(result.getMemoText(), "L&R Campaign - client is new and received SMS to visit mobility.telus.com/gettingstarted for getting started help.");
		assertEquals(Double.valueOf(result.getMessageDelay()), new Double(3600));
	}
	
	@Test
	public void testActivationForMemoTypeACPP() throws Exception {
		SmsEligibilityCheckCriteria criteria = new SmsEligibilityCheckCriteria();	
		criteria.setProductType("C");
		criteria.setEquipmentType("D");
		criteria.setProcessType("Activation");
		criteria.setAccountCombinedType("IB");
		criteria.setBrandId(1);
		SmsEligibilityCheckResultInfo result = (SmsEligibilityCheckResultInfo)SmsEligibilityEvaluationStrategy.getInstance().evaluate(criteria);
		assertTrue(result.getSendSMS());
		assertEquals(result.getSmsTemplate(), "247");
		assertEquals(result.getContactEventTypeId(), "129");
		assertEquals(result.getMemoType(), "ACPP");
		assertEquals(result.getMemoText(), "L&R Campaign - client is new and received SMS to visit mobility.telus.com/gettingstarted for getting started help.");
		assertEquals(Double.valueOf(result.getMessageDelay()), new Double(3600));
	}

	@Test
	public void testMigrationForMemoTypeMPID() throws Exception {
		SmsEligibilityCheckCriteria criteria = new SmsEligibilityCheckCriteria();
		criteria.setProductType("C");
		criteria.setEquipmentType("3");
		criteria.setProcessType("Migration");
		criteria.setAccountCombinedType("BP");
		criteria.setBrandId(1);

		SmsEligibilityCheckResultInfo result = (SmsEligibilityCheckResultInfo)SmsEligibilityEvaluationStrategy.getInstance().evaluate(criteria);
		assertNotNull(result);
		assertTrue(result.getSendSMS());
		assertEquals(result.getSmsTemplate(), "253");
		assertEquals(result.getContactEventTypeId(), "130");
		assertEquals(result.getMemoType(), "MPID");
		assertEquals(result.getMemoText(), "L&R Campaign - client just renewed or migrated and received SMS to visit mobility.telus.com/gettingstarted for device help.");
		assertEquals(Double.valueOf(result.getMessageDelay()), new Double(3600));
	}
	@Test
	public void testMigrationForMemoTypeMIPT() throws Exception {
		SmsEligibilityCheckCriteria criteria = new SmsEligibilityCheckCriteria();
		criteria.setProductType("C");
		criteria.setEquipmentType("A");
		criteria.setProcessType("Migration");
		criteria.setAccountCombinedType("CI");
		criteria.setBrandId(1);

		SmsEligibilityCheckResultInfo result = (SmsEligibilityCheckResultInfo)SmsEligibilityEvaluationStrategy.getInstance().evaluate(criteria);
		assertNotNull(result);
		assertTrue(result.getSendSMS());
		assertEquals(result.getSmsTemplate(), "252");
		assertEquals(result.getContactEventTypeId(), "130");
		assertEquals(result.getMemoType(), "MIPT");
		assertEquals(result.getMemoText(), "L&R Campaign - client just renewed or migrated and received SMS to visit mobility.telus.com/gettingstarted for device help.");
		assertEquals(Double.valueOf(result.getMessageDelay()), new Double(3600));
	}
	@Test
	public void testMigrationForMemoTypeMIPP() throws Exception {
		SmsEligibilityCheckCriteria criteria = new SmsEligibilityCheckCriteria();
		criteria.setProductType("C");
		criteria.setEquipmentType("Z");
		criteria.setProcessType("Migration");
		criteria.setAccountCombinedType("IQ");
		criteria.setBrandId(1);

		SmsEligibilityCheckResultInfo result = (SmsEligibilityCheckResultInfo)SmsEligibilityEvaluationStrategy.getInstance().evaluate(criteria);
		assertNotNull(result);
		assertTrue(result.getSendSMS());
		assertEquals(result.getSmsTemplate(), "251");
		assertEquals(result.getContactEventTypeId(), "130");
		assertEquals(result.getMemoType(), "MIPP");
		assertEquals(result.getMemoText(), "L&R Campaign - client just renewed or migrated and received SMS to visit mobility.telus.com/gettingstarted for device help.");
		assertEquals(Double.valueOf(result.getMessageDelay()), new Double(3600));
	}

	@Test
	public void testRenewalForMemoTypeKDRE() throws Exception {
		SmsEligibilityCheckCriteria criteria = new SmsEligibilityCheckCriteria();
		criteria.setProductType("C");
		criteria.setEquipmentType("A");
		criteria.setProcessType("Renewal");
		criteria.setAccountCombinedType("CI");
		criteria.setBrandId(3);

		SmsEligibilityCheckResultInfo result = (SmsEligibilityCheckResultInfo)SmsEligibilityEvaluationStrategy.getInstance().evaluate(criteria);
		assertNotNull(result);
		assertTrue(result.getSendSMS());
		assertEquals(result.getSmsTemplate(), "254");
		assertEquals(result.getContactEventTypeId(), "132");
		assertEquals(result.getMemoType(), "KDRE");
//		assertEquals(result.getMemoText(), "");
		assertEquals(Double.valueOf(result.getMessageDelay()), new Double(30));
	}
	@Test
	public void testRenewalForMemoTypeRNPT() throws Exception {
		SmsEligibilityCheckCriteria criteria = new SmsEligibilityCheckCriteria();
		criteria.setProductType("C");
		criteria.setEquipmentType("A");
		criteria.setProcessType("Renewal");
		criteria.setAccountCombinedType("CI");
		criteria.setBrandId(1);

		SmsEligibilityCheckResultInfo result = (SmsEligibilityCheckResultInfo)SmsEligibilityEvaluationStrategy.getInstance().evaluate(criteria);
		assertNotNull(result);
		assertTrue(result.getSendSMS());
		assertEquals(result.getSmsTemplate(), "252");
		assertEquals(result.getContactEventTypeId(), "131");
		assertEquals(result.getMemoType(), "RNPT");
		assertEquals(result.getMemoText(), "L&R Campaign - client just renewed or migrated and received SMS to visit mobility.telus.com/gettingstarted for device help.");
		assertEquals(Double.valueOf(result.getMessageDelay()), new Double(30));
	}
	@Test
	public void testRenewalFormemoTypeRNID() throws Exception {
		SmsEligibilityCheckCriteria criteria = new SmsEligibilityCheckCriteria();
		criteria.setProductType("C");
		criteria.setEquipmentType("3");
		criteria.setProcessType("Renewal");
		criteria.setAccountCombinedType("BP");
		criteria.setBrandId(1);

		SmsEligibilityCheckResultInfo result = (SmsEligibilityCheckResultInfo)SmsEligibilityEvaluationStrategy.getInstance().evaluate(criteria);
		assertNotNull(result);
		assertTrue(result.getSendSMS());
		assertEquals(result.getSmsTemplate(), "253");
		assertEquals(result.getContactEventTypeId(), "131");
		assertEquals(result.getMemoType(), "RNID");
		assertEquals(result.getMemoText(), "L&R Campaign - client just renewed or migrated and received SMS to visit mobility.telus.com/gettingstarted for device help.");
		assertEquals(Double.valueOf(result.getMessageDelay()), new Double(30));
	}
	
	@Test
	public void testUnsupportProductType() throws Exception {
		SmsEligibilityCheckCriteria criteria = new SmsEligibilityCheckCriteria();
		criteria.setProductType("P");

		SmsEligibilityCheckResultInfo result = (SmsEligibilityCheckResultInfo)SmsEligibilityEvaluationStrategy.getInstance().evaluate(criteria);
		assertNotNull(result);
		assertFalse(result.getSendSMS());
	}
	
	@Before
	public void setup_ldap_d3() {
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
	    System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
	 //System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");

	  //System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
	 // System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");
	 // System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-s.tmi.telus.com:1589/cn=s_81,o=telusconfiguration");
	  //System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");

	    
	}

	public void setup_mockup() {
		new MockUp<SmsEligibilityEvaluationStrategy> ()
		{
			@SuppressWarnings("unused")
			@Mock
			String getXmlStringFromLdap() {
				return getXmlStringFromFile();
			};
		};
	}
	
	private static String getXmlStringFromFile() {
		if (xmlString == null) {
			StringBuffer sb = new StringBuffer();
			BufferedInputStream bis = null;
			try {
				bis = new BufferedInputStream(ClassLoader.getSystemResourceAsStream("com/telus/cmb/common/eligibility/SMSEligibilityRules.xml"));
				byte[] bytes = new byte[1024];
				int count = 0;
				while ((count = bis.read(bytes)) != -1) {
					sb.append(new String(bytes, 0, count));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bis != null)
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
			xmlString = sb.toString();
			System.out.println("==== SMSRule Begin ====\n" + xmlString + "\n==== SMSRule End ====");
		}
		return xmlString;
	}
}
