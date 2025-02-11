package com.telus.cmb.common.kafka;

public class PublisherConstants {
	
	//Event Meta Data Fields
	
	public static final String BAN = "billingAccountNumber";
	
	@Deprecated
	public static final String ORIGINATOR_APPLICATION_ID = "originatorApplicationId"; //should use eventTriggeredByKBAppId
	public static final String ORIGINATING_APPLICATION_ID = "originatingApplicationId";
	
	@Deprecated
	public static final String PUBLISHER_CMDB_ID = "publisherCmdbId"; //should use originatingApplicationId

	public static final String EVENT_NAME = "eventName";
	public static final String TRIGGERED_BY_KBID = "eventTriggeredByKBID";
	public static final String TRIGGERED_BY_KB_APP_ID = "eventTriggeredByKBAppId";

	public static final String BRAND = "brand";
	public static final String ACCOUNT_TYPE = "accountType";

	public static final String ACCOUNT_SUB_TYPE = "accountSubType";
	public static final String SUBSCRIPTION_ID = "subscriberId";
	public static final String SUBSCRIBER_ID = "subscriptionId";
	public static final String MARKET_PROVINCE = "marketProvince";
	
	public static final String NOTIFICATION_SUPPRESSION_IND = "notificationSuppressionInd";
	public static final String VERSION = "version";


	
}
