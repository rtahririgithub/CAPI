package com.telus.cmb.subscriber.lifecyclehelper.svc;

import com.telus.eas.framework.info.TestPointResultInfo;

public interface SubscriberLifecycleHelperTestPoint {
	TestPointResultInfo testKnowbilityDataSource();
	TestPointResultInfo testEcpcsDataSource();
	TestPointResultInfo testCodsDataSource();
	TestPointResultInfo testServDataSource();
	TestPointResultInfo testDistDataSource();
	TestPointResultInfo testEasDataSource();
	TestPointResultInfo testConeDataSource();
	TestPointResultInfo getSubscriberPrefPkgVersion();
	TestPointResultInfo getSubscriberPkgVersion();
	TestPointResultInfo getSubRetrievalPkgVersion();
	TestPointResultInfo getSubAttrbRetrievalpkgVersion();
	TestPointResultInfo getHistoryUtilityPkgVersion();
	TestPointResultInfo getMemoUtilityPkgVersion();
	TestPointResultInfo getFleetUtilityPkgVersion();
	String getVersion();
	TestPointResultInfo testSubscriptionService();
}
