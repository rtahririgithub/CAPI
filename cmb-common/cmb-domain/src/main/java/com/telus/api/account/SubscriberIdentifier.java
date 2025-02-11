package com.telus.api.account;

public interface SubscriberIdentifier {
	/**
	 * 
	 * @return subscriber id from KB, SUBSCRIBER table, SUBSCRIBER_NO field
	 */
	String getSubscriberId();
	
	/**
	 * 
	 * @return subscription id from KB, SUBSCRIBER table, EXTERNAL_ID field
	 */
	long getSubscriptionId();
	

	/**
	 * 
	 * @return seat Type id from KB, SUBSCRIBER table, SEAT_TYPE field
	 */
	String getSeatType();
	/**
	 * 
	 * @return seatGroup id from KB, SUBSCRIBER table, SEAT_GROUP field
	 */
	String getSeatGroup();
	
	
	/**
	 * 
	 * @return Brand id from KB, SUBSCRIBER table, BRAND_ID field
	 */
	int getBrandId();
}
