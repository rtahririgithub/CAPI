package com.telus.cmb.subscriber.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface OcssamServiceDao {

	public void updateAccountPurchaseAmount(final int ban, final String subscriberId, final String phoneNumber, final double domesticAmount, final double roamingAmount,
			final String actionCode, final String originatingApplicationId) throws ApplicationException;

	TestPointResultInfo test();

}