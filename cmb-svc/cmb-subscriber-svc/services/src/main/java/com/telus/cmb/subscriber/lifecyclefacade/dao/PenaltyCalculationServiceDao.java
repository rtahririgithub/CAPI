package com.telus.cmb.subscriber.lifecyclefacade.dao;

import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.SubscriptionMSCResultInfo;
import com.telus.eas.subscriber.info.SubscriptionMSCSpendingInfo;

public interface PenaltyCalculationServiceDao {

	TestPointResultInfo test();	
	
	SubscriptionMSCResultInfo validateSubscriptionMSCList(final List<SubscriptionMSCSpendingInfo> subscriptionMSCSpendingList)  
			throws ApplicationException;	
	
}
