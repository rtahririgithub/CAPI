package com.telus.cmb.account.informationhelper.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface PrepaidSubscriberServiceDao {
	
	public PrepaidConsumerAccountInfo retrieveAccountInfo(int ban, String phoneNumber) throws ApplicationException;
	public void retrieveAccountInfo(PrepaidConsumerAccountInfo accountInfo, String phoneNumber) throws ApplicationException;
	public AutoTopUpInfo retrieveAutoTopUpInfo(int ban, String phoneNumber) throws ApplicationException;
	public TestPointResultInfo test();

}
