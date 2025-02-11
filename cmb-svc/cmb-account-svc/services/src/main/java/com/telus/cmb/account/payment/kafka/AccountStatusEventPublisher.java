package com.telus.cmb.account.payment.kafka;

import java.util.Date;
import java.util.List;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.transaction.info.AuditInfo;

public interface AccountStatusEventPublisher {

	void publishAccountCancel(AccountInfo accountInfo,
			List<String> phoneNumbers, Date activityDate,
			String activityReasonCode, String depositReturnMethod,
			String waiveReason, String userMemoText,
			boolean portOutActivityInd, boolean brandPortOutActivityInd,
			AuditInfo auditInfo, Date transactionDate,
			boolean AccountStatusEventPublisher,
			boolean notificationSuppressionInd);

	void publishAccountCancelPortOut(AccountInfo accountInfo,
			List<String> phoneNumbers, Date activityDate,
			String activityReasonCode, boolean portOutActivityInd,
			boolean brandPortOutActivityInd, AuditInfo auditInfo,
			Date transactionDate, boolean AccountStatusEventPublisher,
			boolean notificationSuppressionInd);
}
