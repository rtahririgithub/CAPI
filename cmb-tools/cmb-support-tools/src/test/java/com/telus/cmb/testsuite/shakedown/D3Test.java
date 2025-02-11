package com.telus.cmb.testsuite.shakedown;

import org.junit.Test;

import com.telus.cmb.shakedown.utilities.Constants;

public class D3Test extends EnvBaseTest {

	
	static {
		String ldapUrl = "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration";
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", ldapUrl);
	}
	
	@Test
	public void runTest() throws Exception {
		testAll();
	}
	
	
	@Test
	@Override
	public void testAccountInformationHelper() throws Exception {
		super.testAccountInformationHelper();
	}


	@Test
	@Override
	public void testAccountLifecycleManager() throws Exception {
		super.testAccountLifecycleManager();
	}


	@Test
	@Override
	public void testAccountLifecycleFacade() throws Exception {
		super.testAccountLifecycleFacade();
	}


	@Test
	@Override
	public void testProductEquipmentHelper() throws Exception {

		super.testProductEquipmentHelper();
	}

	@Test
	@Override
	public void testProductEquipmentLifecycleFacade() throws Exception {
		super.testProductEquipmentLifecycleFacade();
	}


	@Test
	@Override
	public void testProductEquipmentManager() throws Exception {
		super.testProductEquipmentManager();
	}

	@Test
	@Override
	public void testReferenceDataHelper() throws Exception {
		super.testReferenceDataHelper();
	}
	
	@Test
	@Override
	public void testReferenceDataFacade() throws Exception {
		super.testReferenceDataFacade();
	}

	@Test
	@Override
	public void testSubscriberLifecycleFacade() throws Exception {
		super.testSubscriberLifecycleFacade();
	}

	@Test
	@Override
	public void testSubscriberLifecycleHelper() throws Exception {
		super.testSubscriberLifecycleHelper();
	}

	@Test
	@Override
	public void testSubscriberLifecycleManager() throws Exception {
		super.testSubscriberLifecycleManager();
	}
	
	@Test
	@Override
	public void testConfigurationManagerEJB() throws Exception {
		super.testConfigurationManagerEJB();
	}
	
	@Test
	@Override
	public void testContactEventManagerEJB() throws Exception {
		super.testContactEventManagerEJB();
	}

	@Test
	@Override
	public void testDealerManagerEJB() throws Exception {
		super.testDealerManagerEJB();
	}

	@Test
	@Override
	public void testQueueEventManagerEJB() throws Exception {
		super.testQueueEventManagerEJB();
	}

	@Test
	@Override
	public void testAllAccountEJBs() throws Exception {
		super.testAllAccountEJBs();
	}

	@Test
	@Override
	public void testAllProductEJBs() throws Exception {
		super.testAllProductEJBs();
	}

	@Test
	@Override
	public void testAllReferenceEJBs() throws Exception {
		super.testAllReferenceEJBs();
	}

	@Test
	@Override
	public void testAllSubscriberEJBs() throws Exception {
		super.testAllSubscriberEJBs();
	}

	@Test
	@Override
	public void testAllUtilityEJBs() throws Exception {
		super.testAllUtilityEJBs();
	}

	@Override
	protected String[] getClusters(String group, String[] defaultClusters) {
		if (Constants.GROUP_ACCOUNT_EJB.equals(group) || Constants.GROUP_SUBSCRIBER_EJB.equals(group) || Constants.GROUP_PRODUCT_EJB.equals(group))
			return new String[] {"CustomerInformationManagement2", "CustomerInformationManagementBatch"};
		else 
			return defaultClusters;
	}
	
}
