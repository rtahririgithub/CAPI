package com.telus.cmb.subscriber.kafka;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;

public interface ChangePhoneNumberPublisher {

	void publishChangePhoneNumberPortInEvent(AccountInfo accountInfo,SubscriberInfo subscriberInfo, String oldPhoneNumber,
			String portProcessType, String dealerCode, String salesRepCode,AuditInfo auditInfo, 
			boolean notificationSuppressionInd,String sessionId);

	void publishChangePhoneNumberEvent(AccountInfo accountInfo,SubscriberInfo subscriberInfo, String oldPhoneNumber,
			String dealerCode, String salesRepCode, AuditInfo auditInfo,boolean notificationSuppressionInd, String sessionId);
	
}
