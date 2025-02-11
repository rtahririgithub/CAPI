package com.telus.cmb.subscriber.lifecyclefacade.dao;

import java.rmi.RemoteException;

import com.telus.eas.framework.info.TestPointResultInfo;
import com.telusmobility.rcm.common.exception.RcmException;
import com.telusmobility.rcm.common.exception.RcmSystemException;

public interface MinMdnServiceDao {

	TestPointResultInfo test();
	
	String retrieveMin(String phoneNumber, String networkType, String application)  throws RemoteException, RcmException, RcmSystemException;
}
