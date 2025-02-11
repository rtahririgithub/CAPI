package com.telus.cmb.common.shakedown;

import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.telus.cmnb.armx.agent.shakedown.ShakedownModule;
import com.telus.cmnb.armx.agent.shakedown.ShakedownResult;

public interface EjbShakedown extends ShakedownModule {
	static final String REFERENCE_DATA_FACADE_TESTPOINT = "ReferenceDataFacade#com.telus.cmb.reference.svc.ReferenceDataFacadeTestPoint";
	static final String REFERENCE_DATA_HELPER_TESTPOINT = "ReferenceDataHelper#com.telus.cmb.reference.svc.ReferenceDataHelperTestPoint";

	static final String SUBSCRIBER_LIFECYCLE_FACADE_TESTPOINT = "SubscriberLifecycleFacade#com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacadeTestPoint";
	static final String SUBSCRIBER_LIFECYCLE_HELPER_TESTPOINT = "SubscriberLifecycleHelper#com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelperTestPoint";
	static final String SUBSCRIBER_LIFECYCLE_MANAGER_TESTPOINT = "SubscriberLifecycleManager#com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManagerTestPoint";    

	static final String ACCOUNT_INFORMATION_HELPER_TESTPOINT  = "AccountInformationHelper#com.telus.cmb.account.informationhelper.svc.AccountInformationHelperTestPoint";
	static final String ACCOUNT_LIFECYCLE_FACADE_TESTPOINT = "AccountLifecycleFacade#com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacadeTestPoint";
	static final String ACCOUNT_LIFECYCLE_MANAGER_TESTPOINT = "AccountLifecycleManager#com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManagerTestPoint";

	static final String PRODUCT_EQUIPMENT_HELPER_TESTPOINT = "ProductEquipmentHelper#com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelperTestPoint";
	static final String PRODUCT_EQUIPMENT_MANAGER_TESTPOINT = "ProductEquipmentManager#com.telus.cmb.productequipment.manager.svc.ProductEquipmentManagerTestPoint";
	static final String PRODUCT_EQUIPMENT_LIFECYCLE_FACADE_TESTPOINT = "ProductEquipmentLifecycleFacade#com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacadeTestPoint";

	static final String CONTACT_EVENT_MANAGER_TESTPOINT = "ContactEventManager#com.telus.cmb.utility.contacteventmanager.svc.ContactEventManagerTestPoint";
	static final String QUEUE_EVENT_MANAGER_TESTPOINT = "QueueEventManager#com.telus.cmb.utility.queueevent.svc.QueueEventManagerTestPoint";
	static final String CONFIGURATION_MANAGER_TESTPOINT = "ConfigurationManager#com.telus.cmb.utility.configurationmanager.svc.ConfigurationManagerTestPoint";
	static final String ACTIVITY_LOGGING_TESTPOINT = "ActivityLoggingService#com.telus.cmb.utility.activitylogging.svc.ActivityLoggingServiceTestPoint";
	static final String DEALER_MANAGER_TESTPOINT = "DealerManager#com.telus.cmb.utility.dealermanager.svc.DealerManagerTestPoint";

	static final String KB_ID = "18654";
	static final String KB_CREDENTIAL = "apollo";
	static final String KB_APP = "CAPISHKD";

	void testVersion();
	void testAmdocs();
	void testDataSources();
	void testWebServices();
	void testOtherApi();
	void testPackages();
	void lookupEjb(InitialContext cx) throws NamingException;
	List<ShakedownResult> getResultList();
}
