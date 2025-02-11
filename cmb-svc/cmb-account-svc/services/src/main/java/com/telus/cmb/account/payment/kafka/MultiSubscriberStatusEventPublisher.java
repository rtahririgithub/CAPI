package com.telus.cmb.account.payment.kafka;

import java.util.Date;
import java.util.List;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.transaction.info.AuditInfo;

public interface MultiSubscriberStatusEventPublisher {

	void publishMultiSubscriberCancel(AccountInfo accountInfo,List<String> phoneNumbers, List<String> waiveReasonCodeList,
			Date activityDate, String activityReasonCode,String depositReturnMethod, String userMemoText,
			AuditInfo auditInfo, Date transactionDate,boolean activityDueToPrimaryCancelInd,boolean notificationSuppressionInd);
}
