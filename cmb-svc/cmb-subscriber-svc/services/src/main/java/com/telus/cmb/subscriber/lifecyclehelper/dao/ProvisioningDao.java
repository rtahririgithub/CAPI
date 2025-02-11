package com.telus.cmb.subscriber.lifecyclehelper.dao;

import java.util.Date;
import java.util.List;

import com.telus.eas.subscriber.info.ProvisioningTransactionInfo;

public interface ProvisioningDao {
	/**
	 * Returns List of ProvisioningTransactionInfo
	 * 
	 * @param int			customerID
	 * @param String		subscriberID
	 * @param Date			from
	 * @param Date			toDate
	 * @return
	 */
	List<ProvisioningTransactionInfo> retrieveProvisioningTransactions(int customerID,String subscriberID,Date from,Date toDate); 
	/**
	 * Retrieves Provisioning Status using ban and Phone Number
	 * 
	 * @param Integer 	ban
	 * @param String	phoneNumber
	 * @return	String	Status
	 */
	String retrieveSubscriberProvisioningStatus(int ban, String phoneNumber);
}
