package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.prepaid.PrepaidSubscriberServiceClient;
import com.telus.cmb.common.prepaid.PrepaidSubscriberServiceMapper;
import com.telus.cmb.subscriber.lifecyclemanager.dao.PrepaidSubscriberServiceDao;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.PrepaidSubscriberInfo;

public class PrepaidSubscriberServiceDaoImpl implements PrepaidSubscriberServiceDao{
	
	@Autowired
	PrepaidSubscriberServiceClient prepaidSubscriberServiceClient;
	
	@Override
	public void updatePrepaidSubscriber(final PrepaidSubscriberInfo prepaidSubscriberInfo) throws ApplicationException{
		prepaidSubscriberServiceClient.updateSubscriber(PrepaidSubscriberServiceMapper.mapUpdateSubscriberToPrepaidSchema(prepaidSubscriberInfo));
	}
	
	@Override
	public TestPointResultInfo test() {
		return prepaidSubscriberServiceClient.test();
	}
}
