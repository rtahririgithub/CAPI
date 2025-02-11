package com.telus.cmb.common.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.EnumUtils;
import com.telus.cmb.common.kafka.KafkaEventType;
import com.telus.cmb.framework.config.ConfigurationManager;
import com.telus.cmb.framework.config.ConfigurationManagerFactory;
import com.telus.eas.framework.config.ValueParser;
import com.telus.eas.framework.config.ValueParserFactory;

public abstract class BaseAppConfiguration {
	
	private static final ValueParser stringValueParser = ValueParserFactory.getStringValueParser();
	private static final long DEFAULT_IDLE_TIME = 12 * 60 * 60 * 1000; //12 HOURS
	private static final long DEFAULT_EVICTION_RATE = 1 * 60 * 60 * 1000; //check sessions every hour
	
	protected static ConfigurationManager getConfigurationManager() {
		return ConfigurationManagerFactory.getInstance();
	}
	
	public static String getAmdocsUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.AMDOCS_URL);
	}
	
	public static String getUamsUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.UAMS_URL);
	}
	
	public static long getAmdocsSessionIdle() {
		return getConfigurationManager().getLongValue(LdapKeys.AMDOCS_SESSION_IDLE, DEFAULT_IDLE_TIME);
	}
	
	public static long getAmdocsSessionEvictionRate() {
		return getConfigurationManager().getLongValue(LdapKeys.AMDOCS_SESSION_EVICTION_RATE, DEFAULT_EVICTION_RATE);
	}
	
	public static String getAccountInformationHelperUrl() {
		return getConfigurationManager().getStringValue("AccountInformationHelperUrl");
	}
	
	public static String getAccountLifecycleFacadeUrl() {
		return getConfigurationManager().getStringValue("AccountLifecycleFacadeUrl");
	}
	
	public static String getAccountLifecycleManagerUrl() {
		return getConfigurationManager().getStringValue("AccountLifecycleManagerUrl");
	}
	
	public static String getProductEquipmentHelperUrl() {
		return getConfigurationManager().getStringValue("ProductEquipmentHelperUrl");
	}
	
	public static String getProductEquipmentLifecycleFacadeUrl() {
		return getConfigurationManager().getStringValue("ProductEquipmentLifecycleFacadeUrl");
	}
	
	public static String getProductEquipmentManagerUrl() {
		return getConfigurationManager().getStringValue("ProductEquipmentManagerUrl");
	}
	
	public static String getReferenceDataFacadeUrl() {
		return getConfigurationManager().getStringValue("ReferenceDataFacadeUrl");
	}
	
	public static String getReferenceDataHelperUrl() {
		return getConfigurationManager().getStringValue("ReferenceDataHelperUrl");
	}

	public static String getSubscriberLifecycleFacadeUrl() {
		return getConfigurationManager().getStringValue("SubscriberLifecycleFacadeUrl");
	}
	
	public static String getSubscriberLifecycleHelperUrl() {
		return getConfigurationManager().getStringValue("SubscriberLifecycleHelperUrl");
	}
	
	public static String getSubscriberLifecycleManagerUrl() {
		return getConfigurationManager().getStringValue("SubscriberLifecycleManagerUrl");
	}
	
	public static String getConfigurationManagerEJBUrl() {
		return getConfigurationManager().getStringValue("ConfigurationManagerEjbUrl");
	}
	
	public static String getContactEventManagerEJBUrl() {
		return getConfigurationManager().getStringValue("ContactEventManagerEjbUrl");
	}
	
	public static String getDealerManagerEJBUrl() {
		return getConfigurationManager().getStringValue("DealerManagerEjbUrl");
	}
	
	public static String getQueueEventManagerEJBUrl() {
		return getConfigurationManager().getStringValue("QueueEventManagerEjbUrl");
	}
	
	
	// This is not a roll back flag, shall stay. In staging environment, this flag is set to true, so that we don't set notification to real account's email.
	public static boolean isCDRNotificationUseTestingEmailAddress() {
		return getConfigurationManager().getBooleanValue(LdapKeys.CDR_NOTIFICATION_USE_TESTING_EMAIL, false);
	}
	
	public static boolean isInCDRNotificationTestingEmailList(String email) {
		
		List<String> testingEmailList = getConfigurationManager().getStringValues(LdapKeys.CDR_NOTIFICATION_TESTING_EMAIL_LIST, ",", null);
		if (email != null) {
			email = email.trim();
			for (String emailEntry : testingEmailList) {
				if (emailEntry.equalsIgnoreCase(email)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean isKafkaEventPublisherEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.KAFKA_EVENT_PUBLISHER_ENABLED, false);
	}
	
	public static String getKafkaCredential() {
		return getConfigurationManager().getStringValue(LdapKeys.KAFKA_CREDENTIAL);
	}

	public static List<KafkaEventType> getKafkaEnabledAccountEventTypes() {
		List<KafkaEventType> result = new ArrayList<KafkaEventType>();
		for (String eventType : getConfigurationManager().getStringValues(LdapKeys.KAFKA_ENABLED_EVENT_TYPES)) {
			if(EnumUtils.isValidEnum(KafkaEventType.class, eventType)){
				result.add(KafkaEventType.valueOf(eventType));
			}			
		}
		return result;
	}
	
	public static List<KafkaEventType> getKafkaEnabledSubscriberEventTypes() {
		List<KafkaEventType> result = new ArrayList<KafkaEventType>();
		for (String eventType : getConfigurationManager().getStringValues(LdapKeys.KAFKA_ENABLED_SUBSCRIBER_EVENT_TYPES)) {
			if (EnumUtils.isValidEnum(KafkaEventType.class, eventType)) {
				result.add(KafkaEventType.valueOf(eventType));
			}
		}
		return result;
	}
	
	public static int getQueryTimeoutInSeconds() {
		return getConfigurationManager().getIntegerValue(LdapKeys.QUERY_TIMEOUT_IN_SECONDS, 0);
	}
	
	public static String getStringByKey(String key) {
		return getConfigurationManager().getStringValue(key);
	}
	
	public static String getEnvironment() {
		return getConfigurationManager().getStringValue(LdapKeys.ENVIRONMENT);
	}
}