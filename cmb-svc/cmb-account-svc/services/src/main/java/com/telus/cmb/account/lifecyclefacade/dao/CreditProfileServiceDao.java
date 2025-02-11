package com.telus.cmb.account.lifecyclefacade.dao;

import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.api.account.AuditHeader;
import com.telus.eas.account.credit.info.MatchedAccountInfo;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.HCDclpActivationOptionDetailsInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.transaction.info.AuditInfo;

public interface CreditProfileServiceDao {

	CreditCheckResultInfo checkCredit(AccountInfo accountInfo, 	BusinessCreditIdentityInfo selectedBusinessCreditIdentiy, AuditInfo auditInfo, AuditHeader header,boolean isCreditCheckForBusiness) throws ApplicationException;
	CreditCheckResultInfo performSubscriberEligibilityCheck(AccountInfo accountInfo, int subscriberCount, double thresholdAmount, AuditInfo auditInfo) throws ApplicationException;
	HCDclpActivationOptionDetailsInfo getCLPActivationOptionsDetail(AccountInfo accountInfo) throws ApplicationException;
	List<BusinessCreditIdentityInfo> retrieveCreditEvaluationBusinessList(PostpaidBusinessRegularAccountInfo accountInfo, AuditInfo auditInfo) throws ApplicationException;
	List<MatchedAccountInfo> findAccountsByCustomerProfile(AccountInfo accountInfo) throws ApplicationException;
	TestPointResultInfo test();
	
}