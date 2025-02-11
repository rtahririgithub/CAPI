package com.telus.cmb.account.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface SubscriptionBalanceMgmtServiceDao {
	
	public String adjustBalance(String phoneNumber, double amount, String reasonCode, String transactionId,String userId) throws ApplicationException;
	public String credit(CreditInfo pCreditInfo, String userId) throws ApplicationException;
	public String credit(String phoneNumber, double amount, String reasonCode, String transactionId,String userId) throws ApplicationException;
	public String charge(ChargeInfo pCreditInfo,String userId) throws ApplicationException;
	public String charge(String phoneNumber, double amount, String reasonCode, String transactionId, String userId) throws ApplicationException;
	public TestPointResultInfo test();

}
