package com.telus.api.account;

/**
 * <CODE>LightWeightSubscriber</CODE> represents the minimum information of subscriber.
 * This class is specifically designed to be a return type when doing search subscribers for large BAN
 * 
 * @see AccountSummary#getLightWeightSubscribers(int maximum, boolean includeCancelled)
 */
public interface LightWeightSubscriber {
	/**
	 * @return int
	 */
	int getBanId();
	String getSubscriberId();
	char getStatus();
	
	String getPhoneNumber();
	String getFirstName();
	String getLastName();
	String getProductType();
	
	boolean hasCDMAEquipment();
	boolean hasHSPAEquipment();
	
	/**
	 * Returns the subscription id of the subscriber from KB
	 * 
	 * @return SUBSCRIBER table, EXTERNAL_ID field
	 */
	long getSubscriptionId();
}
