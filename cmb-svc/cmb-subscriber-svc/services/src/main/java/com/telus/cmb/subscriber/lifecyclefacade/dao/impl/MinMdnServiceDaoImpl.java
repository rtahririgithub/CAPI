package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;


import java.rmi.RemoteException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.reference.NetworkType;
import com.telus.cmb.subscriber.lifecyclefacade.dao.MinMdnServiceDao;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telusmobility.rcm.common.ejb.MinMdnService;
import com.telusmobility.rcm.common.exception.RcmException;
import com.telusmobility.rcm.common.exception.RcmSystemException;


public class MinMdnServiceDaoImpl implements MinMdnServiceDao {
	
	private final Logger logger = Logger.getLogger(MinMdnServiceDaoImpl.class);
	
	@Autowired
	private MinMdnService minMdnService;

	private TestPointResultInfo test(String jwsName) {
		final TestPointResultInfo resultInfo = new TestPointResultInfo();
		resultInfo.setTimestamp(new Date());
		resultInfo.setTestPointName(jwsName);
		try {
			String testResult = minMdnService.retrieveMin("1234567890","ClientAPI");
			resultInfo.setResultDetail(testResult);
			resultInfo.setPass(true);
		} catch (Throwable t) {
			if(t.getMessage().contains("No MIN has been associated with this MDN"))
			{
				resultInfo.setResultDetail(" MinMdnService successfully tested for ' retrieveMin ' opeartion");
				resultInfo.setPass(true);
			}
			else {
			resultInfo.setExceptionDetail(t);
			resultInfo.setPass(false);
			}
		}

		return resultInfo;
	}

	@Override
	public TestPointResultInfo test() {
		return test("MinMdn Service");
	}

	@Override
	public String retrieveMin(String phoneNumber, String networkType, String application) throws RemoteException, RcmException, RcmSystemException {
		String min = "";
		if (networkType.equals(NetworkType.NETWORK_TYPE_HSPA)) {
			logger.debug("Returning empty string for HSPA network");
		} else {
			min = minMdnService.retrieveMin(phoneNumber, application);
			logger.debug("MIN :[" + min + "]");
		}
		return min;
	}

}
