package com.telus.cmb.subscriber.lifecyclehelper.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;

public interface SubscriptionServiceDao {

	TestPointResultInfo test();
	
	public ServiceAgreementInfo[] retrieveFeatures(String phoneNumber) throws ApplicationException;
}
