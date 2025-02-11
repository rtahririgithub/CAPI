package com.telus.cmb.subscriber.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface LogicalResourceServiceDao {

	TestPointResultInfo test();
	
	void setIMSIStatus(String networkType, String localIMSI, String remoteIMSI, String status) throws ApplicationException;
	
	void changeIMSIs(String phoneNumber, String networkType, String newLocalIMSI, String newRemoteIMSI) throws ApplicationException;
	
	void setTNStatus(String phoneNumber, String networkType, String status) throws ApplicationException;
	
}
