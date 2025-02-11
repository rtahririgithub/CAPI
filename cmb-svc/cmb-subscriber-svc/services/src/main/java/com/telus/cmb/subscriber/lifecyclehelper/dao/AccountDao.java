package com.telus.cmb.subscriber.lifecyclehelper.dao;

import com.telus.eas.subscriber.info.SubscriberInfo;

public interface AccountDao {

	/**
	 * Retrieves SubscriberInfo details using the phoneNumber
	 * 
	 * @param String	phoneNumber
	 * @return	SubscriberInfo Object
	 */
	SubscriberInfo retrieveBanForPartiallyReservedSub(String phoneNumber);
	/**
	 * Retrieves BAN with the phone Number as input
	 * 
	 * @param String 		phoneNumber
	 * @return	Integer BAN
	 */
	int retrieveBanIdByPhoneNumber(String phoneNumber);
}
