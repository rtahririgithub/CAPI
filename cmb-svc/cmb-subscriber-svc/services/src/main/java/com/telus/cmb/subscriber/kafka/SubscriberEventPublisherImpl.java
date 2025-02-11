
package com.telus.cmb.subscriber.kafka;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telus.cmb.common.kafka.KafkaContentType;
import com.telus.cmb.common.kafka.KafkaEventType;
import com.telus.cmb.common.kafka.KafkaEventVersion;
import com.telus.cmb.common.kafka.KafkaMessage;
import com.telus.cmb.common.kafka.KafkaMessageBuilder;
import com.telus.cmb.common.kafka.KafkaMessagePublisher;
import com.telus.cmb.common.kafka.PublisherConstants;
import com.telus.cmb.subscriber.utilities.AppConfiguration;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;

public class SubscriberEventPublisherImpl implements SubscriberEventPublisher {

	private static final Logger logger = LoggerFactory.getLogger("kafkaLogger");

	private static final String CLIENTAPI_CMDB_ID = "5284";
	private static final String SUBSCRIBER_CREATED = "SUBSCRIBER_CREATED";
	private static final String SUBSCRIBER_STATUS_CHANGE = "SUBSCRIBER_STATUS_CHANGE";
	private static final String SUBSCRIBER_MOVE = "SUBSCRIBER_MOVE";
	
	private KafkaMessagePublisher publisher;
	
	public void setPublisher(KafkaMessagePublisher publisher) {
		this.publisher = publisher;
	}
	
	@Override
	public void publishSubscriberActivationEvent(final AccountInfo accountInfo,final SubscriberInfo subscriberInfo,final SubscriberContractInfo newContractInfo,
			final boolean isPortedIn,final String portProcessType,final AuditInfo auditInfo,final boolean notificationSuppressionInd) {
		logger.info("begin publishSubscriberActivationEvent for ban = [ "+ accountInfo.getBanId() + " ] ," + "subscriberId = [ "+ subscriberInfo.getSubscriberId() + " ]");
		publisher.publish( new KafkaMessageBuilder() {
			@Override
			public void populate(KafkaMessage message) throws Throwable {
				message.addMetadata(PublisherConstants.EVENT_NAME, SUBSCRIBER_CREATED);
				message.addMetadata(PublisherConstants.NOTIFICATION_SUPPRESSION_IND, String.valueOf(notificationSuppressionInd));
				message.addMetadata(PublisherConstants.VERSION, KafkaEventVersion.SUBSCRIBER_ACTIVATE.getVersion());
				message.addMetadata(PublisherConstants.ORIGINATING_APPLICATION_ID, CLIENTAPI_CMDB_ID);
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, auditInfo.getUserId());
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KB_APP_ID, auditInfo.getOriginatorAppId());
				message.addMetadata(PublisherConstants.BRAND, String.valueOf(accountInfo.getBrandId()));
				message.addMetadata(PublisherConstants.ACCOUNT_TYPE, String.valueOf(accountInfo.getAccountType()));
				message.addMetadata(PublisherConstants.ACCOUNT_SUB_TYPE, String.valueOf(accountInfo.getAccountSubType()));
				message.setContent(SubscriberEventPayloadFactory.createSubscriberActivationEvent(subscriberInfo, accountInfo, newContractInfo, isPortedIn, portProcessType, auditInfo));

			}
		}, KafkaEventType.SUBSCRIBER_ACTIVATE,KafkaContentType.JSON);
		logger.info("end publishSubscriberActivationEvent for ban = [ "+ accountInfo.getBanId() + " ] ," + "subscriberId = [ "+ subscriberInfo.getSubscriberId() + " ]");
	}

	@Override
	public void publishSubscriberMoveEvent(final AccountInfo accountInfo,final SubscriberInfo subscriberInfo,final int targetBan, final Date activityDate, final String activityReasonCode,
			final boolean transferOwnership,final String userMemoText, final String dealerCode,final String salesRepCode,final AuditInfo auditInfo,final boolean notificationSuppressionInd) {

		if (!AppConfiguration.isKafkaSubscriberMoveEnabled()) {
			logger.info("publishSubscriberMoveEvent - kafka subscriber move call is disabled");
			return;
		}
		
		logger.info("begin publishSubscriberMoveEvent for ban = [ "+ accountInfo.getBanId() + " ] ," + "subscriberId = [ "+ subscriberInfo.getSubscriberId() + " ]");

		publisher.publish( new KafkaMessageBuilder() {
			@Override
			public void populate(KafkaMessage message) throws Throwable {
				message.addMetadata(PublisherConstants.EVENT_NAME, SUBSCRIBER_MOVE);
				message.addMetadata(PublisherConstants.NOTIFICATION_SUPPRESSION_IND, String.valueOf(notificationSuppressionInd));
				message.addMetadata(PublisherConstants.VERSION, KafkaEventVersion.SUBSCRIBER_MOVE.getVersion());
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, auditInfo.getUserId());
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KB_APP_ID, auditInfo.getOriginatorAppId());
				message.addMetadata(PublisherConstants.ORIGINATING_APPLICATION_ID, CLIENTAPI_CMDB_ID);
				message.addMetadata(PublisherConstants.BRAND, String.valueOf(accountInfo.getBrandId()));
				message.addMetadata(PublisherConstants.ACCOUNT_TYPE, String.valueOf(accountInfo.getAccountType()));
				message.addMetadata(PublisherConstants.ACCOUNT_SUB_TYPE, String.valueOf(accountInfo.getAccountSubType()));
				message.setContent(SubscriberEventPayloadFactory.createSubscriberMoveEvent(accountInfo, subscriberInfo, targetBan, activityDate, activityReasonCode, transferOwnership, userMemoText, dealerCode, salesRepCode, auditInfo,notificationSuppressionInd));

			}
		}, KafkaEventType.MOVE,KafkaContentType.JSON);
		logger.info("end publishSubscriberMoveEvent for ban = [ "+ accountInfo.getBanId() + " ] ," + "subscriberId = [ "+ subscriberInfo.getSubscriberId() + " ]");
	}

	
	@Override
	public void publishSubscriberCancelEvent(final AccountInfo accountInfo,final SubscriberInfo subscriberInfo,final Date activityDate, final String activityReasonCode,final String depositReturnMethod, 
			final String waiveReason,final String userMemoText,final boolean portOutActivityInd ,final AuditInfo auditInfo,final boolean activityDueToPrimaryCancelInd,final boolean notificationSuppressionInd) {
		
		if (!AppConfiguration.isKafkaSubscriberCancelEnabled()) {
			logger.info("publishSubscriberCancelEvent - kafka subscriber cancel call is disabled");
			return;
		}
		
		logger.info("begin publishSubscriberCancelEvent for ban = [ "+ accountInfo.getBanId() + " ] ," + "subscriberId = [ "+ subscriberInfo.getSubscriberId() + " ]");

		publisher.publish( new KafkaMessageBuilder() {
			@Override
			public void populate(KafkaMessage message) throws Throwable {
				message.addMetadata(PublisherConstants.EVENT_NAME, SUBSCRIBER_STATUS_CHANGE);
				message.addMetadata(PublisherConstants.NOTIFICATION_SUPPRESSION_IND, String.valueOf(notificationSuppressionInd));
				message.addMetadata(PublisherConstants.VERSION, KafkaEventVersion.SUBSCRIBER_MOVE.getVersion());
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, auditInfo.getUserId());
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KB_APP_ID, auditInfo.getOriginatorAppId());
				message.addMetadata(PublisherConstants.ORIGINATING_APPLICATION_ID, CLIENTAPI_CMDB_ID);
				message.addMetadata(PublisherConstants.BRAND, String.valueOf(accountInfo.getBrandId()));
				message.addMetadata(PublisherConstants.ACCOUNT_TYPE, String.valueOf(accountInfo.getAccountType()));
				message.addMetadata(PublisherConstants.ACCOUNT_SUB_TYPE, String.valueOf(accountInfo.getAccountSubType()));
				message.setContent(SubscriberEventPayloadFactory.createSubscriberCancelEvent(subscriberInfo, accountInfo, activityDate, activityReasonCode, depositReturnMethod, waiveReason, userMemoText, portOutActivityInd,auditInfo,activityDueToPrimaryCancelInd,notificationSuppressionInd));
			}
		}, KafkaEventType.SUB_CANCEL,KafkaContentType.JSON);
		logger.info("end publishSubscriberCancelEvent for ban = [ "+ accountInfo.getBanId() + " ] ," + "subscriberId = [ "+ subscriberInfo.getSubscriberId() + " ]");
	}

	
	
	@Override
	public void publishSubscriberCancelPortOutEvent(final AccountInfo accountInfo,final SubscriberInfo subscriberInfo, final Date activityDate,
			final String activityReasonCode, final boolean portOutActivityInd,final boolean interBrandPortOutInd, final AuditInfo auditInfo,
			final boolean activityDueToPrimaryCancelInd,final boolean notificationSuppressionInd) {
		
		if (!AppConfiguration.isKafkaSubscriberCancelEnabled()) {
			logger.info("publishSubscriberCancelPortOutEvent - kafka subscriber cancel call is disabled");
			return;
		}
		
		logger.info("begin publishSubscriberCancelPortOutEvent for ban = [ "+ accountInfo.getBanId() + " ] ," + "subscriberId = [ "+ subscriberInfo.getSubscriberId() + " ]");

		publisher.publish( new KafkaMessageBuilder() {
			@Override
			public void populate(KafkaMessage message) throws Throwable {
				message.addMetadata(PublisherConstants.EVENT_NAME, SUBSCRIBER_STATUS_CHANGE);
				message.addMetadata(PublisherConstants.NOTIFICATION_SUPPRESSION_IND, String.valueOf(notificationSuppressionInd));
				message.addMetadata(PublisherConstants.VERSION, KafkaEventVersion.SUBSCRIBER_MOVE.getVersion());
				message.addMetadata(PublisherConstants.ORIGINATING_APPLICATION_ID, CLIENTAPI_CMDB_ID);
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, auditInfo.getUserId());
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KB_APP_ID, auditInfo.getOriginatorAppId());
				message.addMetadata(PublisherConstants.BRAND, String.valueOf(accountInfo.getBrandId()));
				message.addMetadata(PublisherConstants.ACCOUNT_TYPE, String.valueOf(accountInfo.getAccountType()));
				message.addMetadata(PublisherConstants.ACCOUNT_SUB_TYPE, String.valueOf(accountInfo.getAccountSubType()));
				message.setContent(SubscriberEventPayloadFactory.createSubscriberCancelPortOutEvent(subscriberInfo, accountInfo, activityDate, activityReasonCode, portOutActivityInd,interBrandPortOutInd,auditInfo,activityDueToPrimaryCancelInd,notificationSuppressionInd));
			}
		}, KafkaEventType.SUB_CANCEL_PORT_OUT,KafkaContentType.JSON);
		logger.info("end publishSubscriberCancelPortOutEvent for ban = [ "+ accountInfo.getBanId() + " ] ," + "subscriberId = [ "+ subscriberInfo.getSubscriberId() + " ]");
	}
	
}
