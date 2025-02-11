package com.telus.cmb.subscriber.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telus.cmb.common.kafka.KafkaContentType;
import com.telus.cmb.common.kafka.KafkaEventType;
import com.telus.cmb.common.kafka.KafkaEventVersion;
import com.telus.cmb.common.kafka.KafkaMessage;
import com.telus.cmb.common.kafka.KafkaMessageBuilder;
import com.telus.cmb.common.kafka.KafkaMessagePublisher;
import com.telus.cmb.common.kafka.PublisherConstants;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;

/**
 * Responsible for publishing Kafka event for Phone number change flow.
 * Since May 2020 for SmartSpeaker Calling Project.
 *
 */
public class ChangePhoneNumberPublisherImpl implements ChangePhoneNumberPublisher {

	private static final Logger logger = LoggerFactory.getLogger("kafkaLogger");

	private static final String CLIENTAPI_CMDB_ID = "5284";
	private static final String PHONENUMBER_CHANGE = "PHONENUMBER_CHANGE";

	private KafkaMessagePublisher publisher;

	public void setPublisher(KafkaMessagePublisher publisher) {
		this.publisher = publisher;
	}
	
	
	@Override
	public void publishChangePhoneNumberEvent(final AccountInfo accountInfo,
			final SubscriberInfo subscriberInfo, final String oldPhoneNumber,
			final String dealerCode, final String salesRepCode,
			final AuditInfo auditInfo,final boolean notificationSuppressionInd, final String sessionId) {

		logger.info("begin publishChangePhoneNumberEvent for ban = [ "+ accountInfo.getBanId() + " ] ," + "oldPhoneNumber = [ "+ oldPhoneNumber + " ]," + "newPhoneNumber = [ "+ subscriberInfo.getPhoneNumber() + " ]");
		publisher.publish( new KafkaMessageBuilder() {
			
			@Override
			public void populate(KafkaMessage message) throws Throwable {
				message.addMetadata(PublisherConstants.EVENT_NAME, PHONENUMBER_CHANGE);
				message.addMetadata(PublisherConstants.NOTIFICATION_SUPPRESSION_IND, String.valueOf(notificationSuppressionInd));
				message.addMetadata(PublisherConstants.VERSION, KafkaEventVersion.PHONENUMBER_CHANGE.getVersion());
				message.addMetadata(PublisherConstants.ORIGINATING_APPLICATION_ID, CLIENTAPI_CMDB_ID);
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, auditInfo.getUserId());
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KB_APP_ID, auditInfo.getOriginatorAppId());
				message.setContent(ChangePhoneNumberEventPayloadFactory.createPhoneNumberChanegEvent(accountInfo,
								subscriberInfo, oldPhoneNumber, auditInfo, dealerCode,salesRepCode,sessionId,notificationSuppressionInd));
			}
			
		}, KafkaEventType.PHONENUMBER_CHANGE_REGULAR, KafkaContentType.JSON);
		
		logger.info("end publishChangePhoneNumberEvent for ban = [ "+ accountInfo.getBanId() + " ] ," + "oldPhoneNumber = [ "+ oldPhoneNumber + " ]," + "newPhoneNumber = [ "+ subscriberInfo.getPhoneNumber() + " ]");
	}
	
	@Override
	public void publishChangePhoneNumberPortInEvent(final AccountInfo accountInfo,
			final SubscriberInfo subscriberInfo, final String oldPhoneNumber,final String portProcess,
			final String dealerCode, final String salesRepCode,
			final AuditInfo auditInfo,final boolean notificationSuppressionInd, final String sessionId) {

		logger.info("begin publishChangePhoneNumberPortInEvent for ban = [ "+ accountInfo.getBanId() + " ] ," + "oldPhoneNumber = [ "+ oldPhoneNumber + " ]," + "newPhoneNumber = [ "+ subscriberInfo.getPhoneNumber() + " ]");

		final KafkaEventType eventType = getKafkaEventType(portProcess);
		
		publisher.publish( new KafkaMessageBuilder() {
			
			@Override
			public void populate(KafkaMessage message) throws Throwable {
				message.addMetadata(PublisherConstants.EVENT_NAME, PHONENUMBER_CHANGE);
				message.addMetadata(PublisherConstants.NOTIFICATION_SUPPRESSION_IND, String.valueOf(notificationSuppressionInd));
				message.addMetadata(PublisherConstants.VERSION, KafkaEventVersion.PHONENUMBER_CHANGE.getVersion());
				message.addMetadata(PublisherConstants.ORIGINATING_APPLICATION_ID, CLIENTAPI_CMDB_ID);
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, auditInfo.getUserId());
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KB_APP_ID, auditInfo.getOriginatorAppId());
				message.setContent(ChangePhoneNumberEventPayloadFactory.createPhoneNumberChanegPortInEvent(eventType,accountInfo,
								subscriberInfo, oldPhoneNumber, portProcess,auditInfo, dealerCode,salesRepCode,sessionId,notificationSuppressionInd));
			}
			
		}, eventType, KafkaContentType.JSON);
		
		logger.info("end publishChangePhoneNumberPortInEvent for ban = [ "+ accountInfo.getBanId() + " ] ," + "oldPhoneNumber = [ "+ oldPhoneNumber + " ]," + "newPhoneNumber = [ "+ subscriberInfo.getPhoneNumber() + " ]");
	}
	
	private KafkaEventType getKafkaEventType(String portProcess) {
		KafkaEventType eventType;
		if ("INTER_BRAND".equals(portProcess)) {
			eventType = KafkaEventType.PHONENUMBER_CHANGE_INTER_BRAND;
		} else if ("INTER_MVNE".equals(portProcess)) {
			eventType = KafkaEventType.PHONENUMBER_CHANGE_INTER_MVNE;
		} else {
			eventType = KafkaEventType.PHONENUMBER_CHANGE_INTER_CARRIER;
		}

		return eventType;

	}

}
