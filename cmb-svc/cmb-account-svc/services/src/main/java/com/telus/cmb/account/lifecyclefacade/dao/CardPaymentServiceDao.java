package com.telus.cmb.account.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.api.account.AuditHeader;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.utility.info.CreditCardResponseInfo;

public interface CardPaymentServiceDao {
	
	CreditCardResponseInfo processCreditCard(String termId, CreditCardTransactionInfo ccTxnInfo, AuditHeader auditHeader) throws ApplicationException;
	
	TestPointResultInfo test();
	
}
