package com.telus.cmb.utility.queueevent.svc;

import com.telus.eas.framework.info.TestPointResultInfo;

public interface QueueEventManagerTestPoint {
	TestPointResultInfo testCCEventsDataSource();
	TestPointResultInfo getccEventPkgVersion();
	String getVersion();
}
