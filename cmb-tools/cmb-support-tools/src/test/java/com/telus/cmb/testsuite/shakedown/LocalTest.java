package com.telus.cmb.testsuite.shakedown;

import org.junit.Test;

import com.telus.cmb.account.app.AccountInformationHelperShakedown;
import com.telus.cmb.account.app.AccountLifecycleFacadeShakedown;
import com.telus.cmb.account.app.AccountLifecycleManagerShakedown;
import com.telus.cmb.productequipment.app.ProductEquipmentFacadeShakedown;
import com.telus.cmb.productequipment.app.ProductEquipmentHelperShakedown;
import com.telus.cmb.productequipment.app.ProductEquipmentManagerShakedown;
import com.telus.cmb.reference.app.ReferenceDataFacadeShakedown;
import com.telus.cmb.reference.app.ReferenceDataHelperShakedown;
import com.telus.cmb.subscriber.app.SubscriberFacadeShakedown;
import com.telus.cmb.subscriber.app.SubscriberHelperShakedown;
import com.telus.cmb.subscriber.app.SubscriberManagerShakedown;
import com.telus.cmb.utility.app.ConfigurationManagerShakedown;
import com.telus.cmb.utility.app.ContactEventManagerShakedown;
import com.telus.cmb.utility.app.DealerManagerShakedown;
import com.telus.cmb.utility.app.QueueEventManagerShakedown;

public class LocalTest extends EnvBaseTest {
	private static final String LOCALHOST = "t3://localhost:7001";
	
	@Test
	public void runTest() throws Exception {
		testAll();
	}
	
	@Override
	@Test
	public void testAccountInformationHelper() throws Exception {
		testEjb("AccountInformationHelper", LOCALHOST, null, new AccountInformationHelperShakedown());
	}

	@Override
	@Test
	public void testAccountLifecycleManager() throws Exception {
		testEjb("AccountLifecycleManager", LOCALHOST, null, new AccountLifecycleManagerShakedown());
	}

	@Override
	@Test
	public void testAccountLifecycleFacade() throws Exception {
		testEjb("AccountLifecycleFacade", LOCALHOST, null, new AccountLifecycleFacadeShakedown());
	}

	@Override
	@Test
	public void testProductEquipmentHelper() throws Exception {
		testEjb("ProductEquipmentHelper", LOCALHOST, null, new ProductEquipmentHelperShakedown());
	}

	@Override
	@Test
	public void testProductEquipmentLifecycleFacade() throws Exception {
		testEjb("ProductEquipmentLifecycleFacade", LOCALHOST, null, new ProductEquipmentFacadeShakedown());	}

	@Override
	@Test
	public void testProductEquipmentManager() throws Exception {
		testEjb("ProductEquipmentManager", LOCALHOST, null, new ProductEquipmentManagerShakedown());
	}

	@Override
	@Test
	public void testReferenceDataFacade() throws Exception {
		testEjb("ReferenceDataFacade", LOCALHOST, null, new ReferenceDataFacadeShakedown());
	}

	@Override
	@Test
	public void testReferenceDataHelper() throws Exception {
		testEjb("ReferenceDataHelper", LOCALHOST, null, new ReferenceDataHelperShakedown());
	}

	@Override
	@Test
	public void testSubscriberLifecycleFacade() throws Exception {
		testEjb("SubscriberLifecycleFacade", LOCALHOST, null, new SubscriberFacadeShakedown());
	}

	@Override
	@Test
	public void testSubscriberLifecycleHelper() throws Exception {
		testEjb("SubscriberLifecycleHelper", LOCALHOST, null, new SubscriberHelperShakedown());
	}

	@Override
	@Test
	public void testSubscriberLifecycleManager() throws Exception {
		testEjb("SubscriberLifecycleManager", LOCALHOST, null, new SubscriberManagerShakedown());
	}

	@Override
	@Test
	public void testQueueEventManagerEJB() throws Exception {
		testEjb("QueueEventManagerEJB", LOCALHOST, null, new QueueEventManagerShakedown());
	}

	@Override
	@Test
	public void testConfigurationManagerEJB() throws Exception {
		testEjb("ConfigurationManagerEJB", LOCALHOST, null, new ConfigurationManagerShakedown());
	}

	@Override
	@Test
	public void testContactEventManagerEJB() throws Exception {
		testEjb("ContactEventManagerEJB", LOCALHOST, null, new ContactEventManagerShakedown());
	}

	@Override
	@Test
	public void testDealerManagerEJB() throws Exception {
		testEjb("DealerManagerEJB", LOCALHOST, null, new DealerManagerShakedown());
	}


	
	
}
