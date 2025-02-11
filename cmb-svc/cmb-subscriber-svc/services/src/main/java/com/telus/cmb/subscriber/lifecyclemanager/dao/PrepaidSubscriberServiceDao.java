package com.telus.cmb.subscriber.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.PrepaidSubscriberInfo;

public interface PrepaidSubscriberServiceDao {
	TestPointResultInfo test();
	public void updatePrepaidSubscriber(PrepaidSubscriberInfo prepaidSubscriberInfo) throws ApplicationException;

}
