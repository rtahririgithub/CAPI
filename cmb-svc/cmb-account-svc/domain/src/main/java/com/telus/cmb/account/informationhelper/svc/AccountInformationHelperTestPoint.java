package com.telus.cmb.account.informationhelper.svc;

import com.telus.eas.framework.info.TestPointResultInfo;

public interface AccountInformationHelperTestPoint {
	TestPointResultInfo testKnowbilityDataSource();
	TestPointResultInfo testDistDataSource();
	TestPointResultInfo testEcpcsDataSource();
	TestPointResultInfo testConeDataSource();
	TestPointResultInfo testCodsDataSource();
	TestPointResultInfo testEasDataSource();
	TestPointResultInfo getCreditCheckResultPkgVersion();
	TestPointResultInfo getAccRetrievalPkgVersion();
	TestPointResultInfo getAccAttribRetrievalPkgVersion();
	TestPointResultInfo getRaUtilityPkgVersion();
	TestPointResultInfo getSubscriberPkgVersion();
	TestPointResultInfo getSubscriberCountPkgVersion();
	TestPointResultInfo getMemoUtilityPkgVersion();
	TestPointResultInfo getUsageUtilityPkgVersion();
	TestPointResultInfo getHistoryUtilityPkgVersion();
	TestPointResultInfo getInvoicePkgVersion();
	TestPointResultInfo getPortalNotificationPkgVersion();
	TestPointResultInfo getFleetUtilityPkgVersion();
	TestPointResultInfo getClientEquipmentPkgVersion();
	TestPointResultInfo testPrepaidWirelessCustomerOrderService();
	TestPointResultInfo testPrepaidSubscriberService();
	String getVersion();
}
