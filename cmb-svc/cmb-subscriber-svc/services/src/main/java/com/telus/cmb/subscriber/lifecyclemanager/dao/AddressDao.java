package com.telus.cmb.subscriber.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.AddressInfo;

public interface AddressDao {

	/**
	 * Updates subscriber's address
	 *
	 * @param ban
	 * @param subscriber
	 * @param productType
	 * @param addressInfo
	 * @throws ApplicationException
	 */
	void updateAddress(int ban, String subscriber, String productType,AddressInfo addressInfo, String sessionId) throws ApplicationException;
	
	
	}
