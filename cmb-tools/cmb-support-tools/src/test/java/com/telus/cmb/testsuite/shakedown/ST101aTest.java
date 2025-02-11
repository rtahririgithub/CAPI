package com.telus.cmb.testsuite.shakedown;

import java.util.List;

import org.junit.Test;

import com.telus.cmb.shakedown.info.ComponentInfo;
import com.telus.cmb.shakedown.utilities.AppConfiguration;
import com.telus.cmb.shakedown.utilities.ComponentManager;
import com.telus.cmb.shakedown.utilities.Constants;

public class ST101aTest extends EnvBaseTest {

	static {
		String ldapUrl = "ldaps://ldapread-st101a.tmi.telus.com:636/cn=s_81,o=telusconfiguration_a";
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", ldapUrl);
        System.setProperty("useLdapUrl", "true");
        System.setProperty("com.telusmobility.config.java.naming.security.authentication", "simple");
        System.setProperty("com.telusmobility.config.java.naming.security.principal", "uid=telusAdmin,ou=administrators,o=telusconfiguration_a");
        System.setProperty("com.telusmobility.config.java.naming.security.credentials", "TelusAdminST101A");
	}
	
	@Test
	public void test1() throws Exception {
		List<ComponentInfo> list2 = ComponentManager.getComponentInfoList(Constants.CATEGORY_EJB);
		System.out.println(list2 == null ? "NULL" : list2.size());
		for (ComponentInfo componentInfo : list2) {
			System.out.println(componentInfo.getName() + " : " +
			AppConfiguration.getStringValue(componentInfo.getUrlKey()));
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
	public void testProductEquipmentLifecycleFacade() throws Exception {
		super.testProductEquipmentLifecycleFacade();
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
		if (Constants.GROUP_ACCOUNT_EJB.equals(group) || Constants.GROUP_SUBSCRIBER_EJB.equals(group) || Constants.GROUP_PRODUCT_EJB.equals(group))
			return new String[] {"CustomerInformationManagement2", "CustomerInformationManagementBatch"};
		else 
			return defaultClusters;
	}
	
}
