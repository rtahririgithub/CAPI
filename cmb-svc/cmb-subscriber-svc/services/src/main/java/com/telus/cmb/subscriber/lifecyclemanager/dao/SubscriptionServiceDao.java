package com.telus.cmb.subscriber.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;

public interface SubscriptionServiceDao {
	
	public void updateFeatureForPrepaidSubscriber(String phoneNumber, ServiceAgreementInfo serviceAgreementInfo, 
			ServiceAgreementInfo existingServiceAgreementInfo) throws ApplicationException;

	public void deactivateFeatureForPrepaidSubscriber(String phoneNumber, ServiceAgreementInfo serviceAgreementInfo) throws ApplicationException;
	
	public void updateCallingCircleParameters(String applicationId,String userId, String phoneNumber,ServiceAgreementInfo serviceAgreement, byte action)throws ApplicationException ;
	
	TestPointResultInfo test();

}
