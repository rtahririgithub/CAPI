package com.telus.cmb.utility.activitylogging.svc;

import com.telus.eas.framework.info.TestPointResultInfo;

public interface ActivityLoggingServiceTestPoint {
	TestPointResultInfo testRequestPersistenceService();
	TestPointResultInfo testReferenceDataProvider();
	String getVersion();
}
