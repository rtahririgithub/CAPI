package com.telus.cmb.subscriber.kafka;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;

public interface ServiceAgreementEventPublisher {

	public void publishServiceAgreementChangeEvent(AccountInfo accountInfo,SubscriberInfo subscriberInfo,SubscriberContractInfo newContractInfo,SubscriberContractInfo oldContractInfo, 
			EquipmentInfo newPrimaryEquipmentInfo,String dealerCode,String salesRepCode, AuditInfo auditInfo,boolean notificationSuppressionInd);
}