package com.telus.cmb.account.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface BillingAccountDataMgmtDao {
	void insertBillingAccount(AccountInfo accountInfo, String customerID, String userId, String appId) throws ApplicationException;
	void updateBillingAccount(AccountInfo accountInfo, String userId, String appId) throws ApplicationException;
	void insertCustomerWithBillingAccount(AccountInfo accountInfo, String userId, String appId) throws ApplicationException;
	void updatePayChannel(AccountInfo accountInfo, String userId, String appId) throws ApplicationException;
	void updateBillingAccountStatus(int billingAccountNumber, String status, String userId, String appId) throws ApplicationException;
	void updateBillingCycle(int billingAccountNumber, String newBillCycle, String userId, String appId) throws ApplicationException;
	TestPointResultInfo test();
}
