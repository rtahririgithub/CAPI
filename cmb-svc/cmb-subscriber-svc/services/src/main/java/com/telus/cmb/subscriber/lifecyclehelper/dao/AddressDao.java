package com.telus.cmb.subscriber.lifecyclehelper.dao;

import com.telus.eas.account.info.AddressInfo;

public interface AddressDao {
	/**
	 * Retrieves AddressInfo Details for the given BAN and subscriber.
	 * 
	 * @param Integer 	BAN
	 * @param String	subscriber
	 * @return
	 */
	AddressInfo retrieveSubscriberAddress(int ban, String subscriber);

}
