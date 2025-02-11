package com.telus.cmb.account.informationhelper.dao;

import com.telus.api.ApplicationException;
import com.telus.api.account.AuditHeader;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface PrepaidWirelessCustomerOrderServiceDao {
	
	public String validatePayAndTalkSubscriberActivation(String applicationId,
			String userId,
			PrepaidConsumerAccountInfo prepaidConsumerAccountInfo,
			AuditHeader auditHeader) throws ApplicationException;
	
	public double getPrepaidActivationCredit(String applicationId,
			PrepaidConsumerAccountInfo pPrepaidConsumerAccountInfo) throws ApplicationException;
	
	public double getPrepaidActivationCredit(String customerID, 
			String subscriptionID, 
			String equipmentSerialNumber, 
			String activationType, 
			String activationSource, 
			String activationCode, 
			String imei) throws ApplicationException;
	
	public TestPointResultInfo test();

}
