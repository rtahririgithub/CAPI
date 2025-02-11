package com.telus.api.account;

/**
 * @author R. Fong
 * @version 1.0, 09-Jul-2008 
 **/
public interface PoolingPricePlanSubscriberCount {

	int getPoolingGroupId();
	PricePlanSubscriberCount[] getPricePlanSubscriberCount();
	
}