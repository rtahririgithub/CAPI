package com.telus.cmb.common.eligibility;

import org.junit.Before;
import org.junit.Test;

import com.telus.eas.eligibility.info.SmsEligibilityCheckResultInfo;


public class EligibilityUtilitiesTest {

	@Before
	public void setup_ldap_d3() {
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
	}
	
	@Test
	public void testSmsEligibility() throws Exception {
		SmsEligibilityCheckCriteria criteria = new SmsEligibilityCheckCriteria();
		criteria.setProductType("C");
		criteria.setEquipmentType("A");
		criteria.setProcessType("Renewal");
		criteria.setAccountCombinedType("CI");
		criteria.setBrandId(3);
		SmsEligibilityCheckResultInfo result = SmsEligibilityEvaluationStrategy.getInstance().checkEligibility(criteria);
		System.out.println(result);
	}
	
	@Test
	public void testDataSyncEligibility() throws Exception {
		EnterpriseManagementEligibilityCheckCriteria criteria = new EnterpriseManagementEligibilityCheckCriteria();
		criteria.setProductType("C");
		criteria.setProcessType("AccountActivation");
		criteria.setAccountCombinedType("I1");
		criteria.setBrandId(1);
		Boolean result = EnterpriseManagementEligibilityEvaluationStrategy.getInstance().checkEligibility(criteria);
		System.out.println(result);
	}
}
