package com.telus.cmb.subscriber.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.subscriber.info.SubscriberInfo;

public interface DepositDao {

	
	 void createDeposit(SubscriberInfo pSubscriberInfo, double amount, String memoText, String sessionId) throws ApplicationException;
}
