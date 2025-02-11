package com.telus.cmb.subscriber.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface CommunicationSuiteMgmtSvcDao {
	void removeFromCommunicationSuite(int ban, String companionPhoneNumber,String primaryphoneNumber) throws ApplicationException;
	TestPointResultInfo test();	
}
