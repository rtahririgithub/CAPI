package com.telus.cmb.account.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.api.account.PaymentCard;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface PrepaidWirelessCustomerOrderServiceDao {
	
	public void creditSubscriberMigration(String ban, 
			String esn, 
			String imei, 
			String phoneNumber, 
			String provinceCode, 
			String pin,
			double creditAmt, 
			String rateId, 
			int expiryDays, 
			String source, 
			String user, 
			String activationType, 
			String reasonCode) throws ApplicationException;
	
	public void saveActivationTopUpArrangement(String ban,
			String esn, 
			String phoneNumber, 
			PaymentCard[] paymentCards) throws ApplicationException;
	
	public TestPointResultInfo test();

}
