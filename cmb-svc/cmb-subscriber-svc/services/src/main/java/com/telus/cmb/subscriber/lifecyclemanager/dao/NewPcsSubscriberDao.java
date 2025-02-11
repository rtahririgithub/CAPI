package com.telus.cmb.subscriber.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public interface NewPcsSubscriberDao extends NewSubscriberDao {
	SubscriberInfo reservePortedInPhoneNumber(SubscriberInfo subscriberInfo
			, PhoneNumberReservationInfo phoneNumberReservation, boolean reserveNumberOnly
			, String sessionId)
		throws ApplicationException;
	
	 void updateBirthDate(SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException;
	 
	SubscriberInfo retrievePartiallyReservedSubscriber(int banId, String subscriberId, String sessionId) throws ApplicationException;
	 
}
