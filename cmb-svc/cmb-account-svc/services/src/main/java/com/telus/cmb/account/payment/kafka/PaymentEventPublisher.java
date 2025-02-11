package com.telus.cmb.account.payment.kafka;

import java.util.Date;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.transaction.info.AuditInfo;

public interface PaymentEventPublisher {

	public void publishPaymentMethodChange(AccountInfo accountInfo,
			PaymentMethodInfo paymentMethodInfo, AuditInfo auditInfo,
			Date transactionDate, boolean notificationSuppressionInd);

	public void publishMakePayment(AccountInfo accountInfo,
			PaymentInfo paymentInfo, AuditInfo auditInfo, Date transactionDate,
			boolean notificationSuppressionInd);
	


}