package com.telus.cmb.account.payment.kafka;

import java.util.Date;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.transaction.info.AuditInfo;

public interface CreditCheckResultEventPublisher {

	void publishNewCreditCheckResult(AccountInfo accountInfo,
			CreditCheckResultInfo creditCheckResultInfo, AuditInfo auditInfo,
			Date transactionDate, boolean notificationSuppressionInd);

	void publishModifiedCreditCheckResult(AccountInfo accountInfo,
			CreditCheckResultInfo creditCheckResultInfo, AuditInfo auditInfo,
			Date transactionDate, boolean notificationSuppressionInd);

}