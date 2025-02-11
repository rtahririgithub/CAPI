package com.telus.cmb.utility.dealermanager.svc;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface DealerManagerTestPoint {
	TestPointResultInfo testDistDataSource();
	String openSession(String userId, String password, String applicationId) throws ApplicationException;
	String getVersion();
}
