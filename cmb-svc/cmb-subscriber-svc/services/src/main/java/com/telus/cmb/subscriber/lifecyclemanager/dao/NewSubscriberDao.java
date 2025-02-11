package com.telus.cmb.subscriber.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.api.account.ServicesValidation;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public interface NewSubscriberDao extends SubscriberDao {
	String[] retrieveAvailablePhoneNumbers(int ban
			, PhoneNumberReservationInfo phoneNumberReservation
			, int maxNumbers, String sessionId) throws ApplicationException;
	
	void createSubscriber(SubscriberInfo subscriberInfo
			, SubscriberContractInfo subscriberContractInfo
			, boolean activate, boolean dealerHasDeposit
			, boolean portedIn, ServicesValidation srvValidation
			, String portProcessType, int oldBanId
			, String oldSubscriberId, String sessionId) throws ApplicationException;

	void releaseSubscriber(SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException;
	
	SubscriberInfo reserveLikePhoneNumber(SubscriberInfo subscriberInfo
			, PhoneNumberReservationInfo phoneNumberReservation, String sessionId) throws ApplicationException;
	
	SubscriberInfo reservePhoneNumber(SubscriberInfo subscriberInfo
			, PhoneNumberReservationInfo phoneNumberReservation, boolean isOfflineReservation,String sessionId)
		throws ApplicationException;
}
