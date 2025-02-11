package com.telus.cmb.subscriber.lifecyclefacade.dao;

import com.telus.api.ApplicationException;

public interface RedkneeSubscriptionMgmtServiceDao {
	public void  updateSubscriptionWithStateTransition(String phoneNumber) throws ApplicationException;
}
