package com.telus.cmb.subscriber.lifecyclemanager.svc;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface SubscriberLifecycleManagerTestPoint {
	TestPointResultInfo testCodsDataSource();
	String openSession(String userId, String password, String applicationId) throws ApplicationException;
	TestPointResultInfo testPrepaidSubscriberService();
	TestPointResultInfo testPrepaidWirelessCustomerOrderService();
    TestPointResultInfo testOrderService();
    TestPointResultInfo testSubscriptionService();
	String getVersion();
}
