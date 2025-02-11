package com.telus.cmb.account.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface PrepaidSubscriberServiceDao {
	
	public void updateAccountPIN(String phoneNumber, String pin) throws ApplicationException;
	public TestPointResultInfo test();

}
