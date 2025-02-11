package com.telus.cmb.subscriber.lifecyclemanager.dao;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public interface UpdateIdenPcsSubscriberDao {
	
	void cancelPortedInSubscriber(
			int ban, 
			String subscriberId, 
			String deactivationReason, 
			Date activityDate, 
			String portOutInd, 
			boolean isBrandPort, 
			String sessionId) throws ApplicationException;

	void setPortTypeToPortIn(int ban, String subscriberId
			, Date sysDate, String sessionId) throws ApplicationException;

	void setPortTypeToSnapback(String resourceNumber, String sessionId) throws ApplicationException;

	void portChangeSubscriberNumber(SubscriberInfo subscriberInfo
			, AvailablePhoneNumberInfo newPhoneNumber, String reasonCode
			, String dealerCode, String salesRepCode, String portProcessType
			, int oldBanId, String oldSubscriberId, String sessionId) throws ApplicationException;
	
	void suspendPortedInSubscriber(int ban, String subscriberId
			, String deactivationReason, Date activityDate, String portOutInd, String sessionId)
		throws ApplicationException;

}
