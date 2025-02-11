package com.telus.cmb.account.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.api.account.AuditHeader;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.transaction.info.AuditInfo;

public interface DeprecatedCreditProfileSvcDao {

	CreditCheckResultInfo checkCredit(AccountInfo accountInfo, 	BusinessCreditIdentityInfo selectedBusinessCreditIdentiy, 
			AuditInfo auditInfo, AuditHeader header,boolean isCreditCheckForBusiness) throws ApplicationException;
	TestPointResultInfo test();
	
}
