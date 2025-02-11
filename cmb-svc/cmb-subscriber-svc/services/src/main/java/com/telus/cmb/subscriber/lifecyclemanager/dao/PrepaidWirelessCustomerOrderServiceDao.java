package com.telus.cmb.subscriber.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.ActivationFeaturesPurchaseArrangementInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public interface PrepaidWirelessCustomerOrderServiceDao {
	TestPointResultInfo test();
	public void saveActivationFeaturesPurchaseArrangement(SubscriberInfo subscriber,
			ActivationFeaturesPurchaseArrangementInfo[] featuresPurchaseList,String user) throws ApplicationException ;

}
