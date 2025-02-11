package com.telus.cmb.shakedown.utilities;

import com.telus.cmb.common.util.BaseAppConfiguration;


public class AppConfiguration extends BaseAppConfiguration {
	public static final String REFERENCE_DATA_FACADE_TESTPOINT = "ReferenceDataFacade#com.telus.cmb.reference.svc.ReferenceDataFacadeTestPoint";
	public static final String REFERENCE_DATA_HELPER_TESTPOINT = "ReferenceDataHelper#com.telus.cmb.reference.svc.ReferenceDataHelperTestPoint";

	public static final String SUBSCRIBER_LIFECYCLE_FACADE_TESTPOINT = "SubscriberLifecycleFacade#com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacadeTestPoint";
	public static final String SUBSCRIBER_LIFECYCLE_HELPER_TESTPOINT = "SubscriberLifecycleHelper#com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelperTestPoint";
	public static final String SUBSCRIBER_LIFECYCLE_MANAGER_TESTPOINT = "SubscriberLifecycleManager#com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManagerTestPoint";    

	public static final String ACCOUNT_INFORMATION_HELPER_TESTPOINT  = "AccountInformationHelper#com.telus.cmb.account.informationhelper.svc.AccountInformationHelperTestPoint";
	public static final String ACCOUNT_LIFECYCLE_FACADE_TESTPOINT = "AccountLifecycleFacade#com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacadeTestPoint";
	public static final String ACCOUNT_LIFECYCLE_MANAGER_TESTPOINT = "AccountLifecycleManager#com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManagerTestPoint";

	public static final String PRODUCT_EQUIPMENT_HELPER_TESTPOINT = "ProductEquipmentHelper#com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelperTestPoint";
	public static final String PRODUCT_EQUIPMENT_MANAGER_TESTPOINT = "ProductEquipmentManager#com.telus.cmb.productequipment.manager.svc.ProductEquipmentManagerTestPoint";
	public static final String PRODUCT_EQUIPMENT_LIFECYCLE_FACADE_TESTPOINT = "ProductEquipmentLifecycleFacade#com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacadeTestPoint";

	public static final String CONTACT_EVENT_MANAGER_TESTPOINT = "ContactEventManager#com.telus.cmb.utility.contacteventmanager.svc.ContactEventManagerTestPoint";
	public static final String QUEUE_EVENT_MANAGER_TESTPOINT = "QueueEventManager#com.telus.cmb.utility.queueevent.svc.QueueEventManagerTestPoint";
	public static final String CONFIGURATION_MANAGER_TESTPOINT = "ConfigurationManager#com.telus.cmb.utility.configurationmanager.svc.ConfigurationManagerTestPoint";
	public static final String ACTIVITY_LOGGING_TESTPOINT = "ActivityLoggingService#com.telus.cmb.utility.activitylogging.svc.ActivityLoggingServiceTestPoint";
	public static final String DEALER_MANAGER_TESTPOINT = "DealerManager#com.telus.cmb.utility.dealermanager.svc.DealerManagerTestPoint";
	
	public static final String SECURITY_PRINCIPAL = "ejb_user";
	public static final String SECURITY_CREDENTIALS = "ejb_user";
	
	public static final String KB_ID = "18654";
	public static final String KB_CREDENTIAL = "apollo";
	public static final String KB_APP = "CAPISHKD";

	public static String getStringValue(String str) {
		return getConfigurationManager().getStringValue(str);
	}
	
}
