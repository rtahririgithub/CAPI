package com.telus.cmb.subscriber.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

@Deprecated
public interface NewIdenSubscriberDao extends NewSubscriberDao {
	SubscriberInfo reservePortedInPhoneNumber(SubscriberInfo subscriberInfo
			, PhoneNumberReservation phoneNumberReservation, boolean reserveNumberOnly
			, boolean reserveUfmi, boolean ptnBased, byte ufmiReserveMethod
			, int urbanId, int fleetId, int memberId
			, AvailablePhoneNumber availPhoneNumber, String sessionId) throws ApplicationException;

	IDENSubscriberInfo retrievePartiallyReservedSubscriber(int banId, String subscriberId, String sessionId) throws ApplicationException;
}
