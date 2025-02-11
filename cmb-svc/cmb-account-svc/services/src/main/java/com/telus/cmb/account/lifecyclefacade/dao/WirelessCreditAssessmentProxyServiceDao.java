package com.telus.cmb.account.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.api.account.AuditHeader;
import com.telus.eas.account.credit.info.CreditAssessmentInfo;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.transaction.info.AuditInfo;

public interface WirelessCreditAssessmentProxyServiceDao {

	CreditAssessmentInfo overrideCreditWorthiness(final int ban, final String creditProgram, final String creditClass, final double creditLimit, final CreditCheckResultDepositInfo[] depositInfoArray,
			final AuditInfo auditInfo, final AuditHeader auditHeader) throws ApplicationException;

	CreditAssessmentInfo performCreditCheck(final AccountInfo accountInfo, final AuditInfo auditInfo, final AuditHeader auditHeader) throws ApplicationException;

	CreditAssessmentInfo performManualCreditCheck(final AccountInfo accountInfo, final AuditInfo auditInfo, final AuditHeader auditHeader) throws ApplicationException;

	TestPointResultInfo test();

}