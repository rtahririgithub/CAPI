package com.telus.cmb.subscriber.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.cmb.subscriber.domain.CommunicationSuiteRepairData;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface CommSuiteMgmtRestSvcDao {
	void repairCommunicationSuite(CommunicationSuiteRepairData repairData, String sessionId) throws ApplicationException;

	TestPointResultInfo test();
}
