package com.telus.cmb.subscriber.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;

public interface OrderServiceDao {
	
	TestPointResultInfo test();
	
	void activateFeatureForPrepaidSubscriber(String phoneNumber,
			ServiceAgreementInfo serviceAgreementInfo)throws ApplicationException;

}
