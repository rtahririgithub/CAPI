package com.telus.cmb.account.payment.kafka;

import java.util.Date;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.FollowUpUpdateInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.transaction.info.AuditInfo;

public interface CreditEventPublisher {

	public void publishCreateCredit(AccountInfo accountInfo,CreditInfo creditInfo, AuditInfo auditInfo, Date transactionDate,boolean notificationSuppressionInd);

	public void publishCreditForChargeAdj(AccountInfo accountInfo,CreditInfo creditInfo, ChargeInfo chargeInfo, AuditInfo auditInfo,Date transactionDate, boolean notificationSuppressionInd);

	public void publishFollowUpApprovalCredit(AccountInfo accountInfo,CreditInfo creditInfo, FollowUpUpdateInfo followUpUpdateInfo,AuditInfo auditInfo, Date transactionDate,boolean notificationSuppressionInd);

}