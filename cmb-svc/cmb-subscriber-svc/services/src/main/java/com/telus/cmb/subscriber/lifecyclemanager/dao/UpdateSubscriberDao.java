package com.telus.cmb.subscriber.lifecyclemanager.dao;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public interface UpdateSubscriberDao extends SubscriberDao {
	String[] retrieveAvailablePhoneNumbers(int ban
			, String subscriberId
			, PhoneNumberReservationInfo phoneNumberReservation
			, int maxNumbers
			, String sessionId) throws ApplicationException;

	void changePhoneNumber(SubscriberInfo subscriberInfo
			, AvailablePhoneNumberInfo newPhoneNumber, String reasonCode
			, String dealerCode, String salesRepCode, String sessionId) throws ApplicationException;

	void moveSubscriber(SubscriberInfo subscriberInfo
			, int targetBan, Date activityDate
			, boolean transferOwnership, String activityReasonCode
			, String userMemoText
			, String dealerCode, String salesRepCode, String sessionId) throws ApplicationException;

	void resetVoiceMailPassword(int ban, String subscriberId
			, String[] voiceMailSocAndFeature, String sessionId)
		throws ApplicationException;
	
	void changeSerialNumberAndMaybePricePlan(SubscriberInfo subscriberInfo
			, com.telus.eas.equipment.info.EquipmentInfo newPrimaryEquipmentInfo
			, com.telus.eas.equipment.info.EquipmentInfo[] newSecondaryEquipmentInfo
			, SubscriberContractInfo subscriberContractInfo, String dealerCode, String salesRepCode
			, PricePlanValidationInfo pricePlanValidation, String sessionId) throws ApplicationException;
	
	void resetCSCSubscription(int ban, String subscriberId, String[] cscFeature,String sessionId) throws ApplicationException;
}
