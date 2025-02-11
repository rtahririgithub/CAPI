package com.telus.cmb.subscriber.kafka;

import java.util.Date;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;

public interface SubscriberEventPublisher {
	
	 void publishSubscriberActivationEvent(AccountInfo accountInfo,SubscriberInfo subscriberInfo,
			SubscriberContractInfo newContractInfo, boolean isPortedIn,String portType, AuditInfo auditInfo, boolean notificationSuppressionInd);

	void publishSubscriberMoveEvent(AccountInfo accountInfo,SubscriberInfo subscriberInfo, int targetBan, Date activityDate,String activityReasonCode,
			boolean transferOwnership,String userMemoText, String dealerCode, String salesRepCode,AuditInfo auditInfo,boolean notificationSuppressionInd);


	void publishSubscriberCancelEvent(AccountInfo accountInfo,SubscriberInfo subscriberInfo, Date activityDate,
			String activityReasonCode, String depositReturnMethod,String waiveReason, String userMemoText,
			boolean portOutActivityInd,AuditInfo auditInfo,boolean activityDueToPrimaryCancelInd,boolean notificationSuppressionInd);
	
	void publishSubscriberCancelPortOutEvent(AccountInfo accountInfo,SubscriberInfo subscriberInfo, Date activityDate,
			String activityReasonCode, boolean portOutActivityInd, boolean interBrandPortOutInd,AuditInfo auditInfo,boolean activityDueToPrimaryCancelInd,boolean notificationSuppressionInd);
	
}

