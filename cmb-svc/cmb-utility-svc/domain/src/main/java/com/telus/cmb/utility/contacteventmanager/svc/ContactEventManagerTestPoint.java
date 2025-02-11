package com.telus.cmb.utility.contacteventmanager.svc;

import com.telus.eas.framework.info.TestPointResultInfo;

public interface ContactEventManagerTestPoint {
	TestPointResultInfo	testCodsDataSource();
	TestPointResultInfo testConeDataSource();
	String getVersion();
}
