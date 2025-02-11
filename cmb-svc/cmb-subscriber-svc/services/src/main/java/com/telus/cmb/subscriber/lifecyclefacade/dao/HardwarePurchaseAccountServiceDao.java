package com.telus.cmb.subscriber.lifecyclefacade.dao;

import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.hpa.info.RewardAccountInfo;

public interface HardwarePurchaseAccountServiceDao {

	public void closeRewardAccount(int billingAccountNumber, String phoneNumber, long subscriptionId) throws ApplicationException;

	public void restoreRewardAccount(int billingAccountNumber, String phoneNumber, long subscriptionId) throws ApplicationException;
	
	public List<RewardAccountInfo> getRewardAccount(int billingAccountNumber, String phoneNumber, long subscriptionId) throws ApplicationException;

	TestPointResultInfo test();

}