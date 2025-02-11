package com.telus.cmb.testsuite.shakedown;

import java.util.List;

import org.junit.Test;

import com.telus.cmb.shakedown.info.ServerInfo;
import com.telus.cmb.shakedown.utilities.Constants;
import com.telus.cmb.shakedown.utilities.ServerInfoUtilities;

public class PR101ATest extends EnvBaseTest {

	static {
		String ldapUrl = "ldaps://ldapread-pra.tmi.telus.com:636/cn=prod_81,o=telusconfiguration_a";
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", ldapUrl);
        System.setProperty("useLdapUrl", "true");
        System.setProperty("com.telusmobility.config.java.naming.security.authentication", "simple");
        System.setProperty("com.telusmobility.config.java.naming.security.principal", "uid=telusAdmin,ou=administrators,o=telusconfiguration_a");
        System.setProperty("com.telusmobility.config.java.naming.security.credentials", "EMprod123");
	}
	
	@Test
	public void test1() throws Exception {
		List<ServerInfo> serverList = ServerInfoUtilities.retrieveServerInfoList("t3://cmosr-custinfomgmt2-pr.tmi.telus.com:50022", new String[]{"CustomerInformationManagement3", "CustomerInformationManagementBatch"});
		System.out.println();
		for (ServerInfo serverInfo : serverList) {
			System.out.println(serverInfo);
		}
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
		if (Constants.GROUP_ACCOUNT_EJB.equals(group) || Constants.GROUP_SUBSCRIBER_EJB.equals(group) || Constants.GROUP_PRODUCT_EJB.equals(group)) {
			return new String[] {"CustomerInformationManagement2", "CustomerInformationManagement3", "CustomerInformationManagementBatch"};
		} else if (Constants.GROUP_UTILITY_EJB.equals(group) || Constants.GROUP_REFERENCE_EJB.equals(group)) {
			return new String[] {"GeneralUtilities", "GeneralUtilities2"};
		} else { 
			return defaultClusters;
		}
	}
}
