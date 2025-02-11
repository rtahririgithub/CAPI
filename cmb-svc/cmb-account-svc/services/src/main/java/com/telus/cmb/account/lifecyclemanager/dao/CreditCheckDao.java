package com.telus.cmb.account.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;

public interface CreditCheckDao {
	
	void saveCreditCheckInfo(AccountInfo pAccountInfo, CreditCheckResultInfo pCreditCheckResultInfo,
			String pCreditParamType, ConsumerNameInfo pConsumerNameInfo,
			AddressInfo pAddressInfo, String sessionId) throws ApplicationException ;
	
	void saveCreditCheckInfoForBusiness(AccountInfo pAccountInfo, BusinessCreditIdentityInfo[] listOfBusinesses,
			BusinessCreditIdentityInfo selectedBusiness,CreditCheckResultInfo pCreditCheckResultInfo,
			String pCreditParamType, String sessionId) throws ApplicationException ;

    void updateCreditCheckResult(int pBan, String pCreditClass,
            CreditCheckResultDepositInfo[] pCreditCheckResultDepositInfo,
            String pDepositChangedReasonCode, String pDepositChangeText, 
            String sessionId) throws ApplicationException;
    
    void updateCreditProfile(int ban, String newCreditClass, double newCreditLimit, String memoText,
			String sessionId) throws ApplicationException;
    
    void updateCreditClass(int ban, String newCreditClass, String memoText,String sessionId)throws ApplicationException;
    
    CreditCheckResultInfo retrieveAmdocsCreditCheckResult(int ban, String sessionId) throws ApplicationException;

}
