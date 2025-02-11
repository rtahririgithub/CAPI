package com.telus.cmb.account.lifecyclemanager.svc;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface AccountLifecycleManagerTestPoint {
	TestPointResultInfo testCodsDataSource();
	String openSession(String userId, String password, String applicationId) throws ApplicationException;
	TestPointResultInfo testPrepaidWirelessCustomerOrderService();
	TestPointResultInfo testPrepaidSubscriberService();
	TestPointResultInfo testSubscriptionBalanceMgmtService();
	TestPointResultInfo testSubscriptionManagementService();
	String getVersion();
}