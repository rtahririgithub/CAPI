package com.telus.cmb.subscriber.lifecyclemanager.dao;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.subscriber.info.AdditionalMsiSdnFtrInfo;
import com.telus.eas.subscriber.info.SearchResultByMsiSdn;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.NumberGroupInfo;

@Deprecated
public interface UpdateIdenSubscriberDao extends UpdateSubscriberDao {

	void moveSubscriber(SubscriberInfo subscriberInfo
			, int targetBan, Date activityDate
			, boolean transferOwnership, String activityReasonCode
			, String userMemoText
			, String dealerCode, String salesRepCode, FleetInfo fleetInfo, String sessionId) throws ApplicationException;

	void cancelAdditionalMsisdn(AdditionalMsiSdnFtrInfo[] additionalMsiSdnFtrInfo,String additionalMsisdn
			, String sessionId) 
	throws ApplicationException;

	void changeAdditionalPhoneNumbers(int ban, String subscriberId, String primaryPhoneNumber
			, NumberGroupInfo numberGroup, boolean portIn, String sessionId)
	throws ApplicationException;

	void changeFaxNumber(int ban, String subscriberId
			, String newFaxNumber, NumberGroupInfo numberGroup
			, boolean isPortedInNumber, String sessionId)
	throws ApplicationException;

	void changeIMSI(int ban, String subscriberId, String sessionId) throws ApplicationException;

	void changeIP(int ban,
			String subscriberId, String newIp, String newIpType,
			String newIpCorpCode, String sessionId) throws ApplicationException;
	
	void reserveAdditionalPhoneNumber(int ban,
            String subscriberId,
            NumberGroupInfo numberGroup,
            String additionalPhoneNumber, String sessionId) throws ApplicationException;
	
	SearchResultByMsiSdn searchSubscriberByAdditionalMsiSdn(String additionalMsisdn, String sessionId) throws ApplicationException;
	
	void deleteMsisdnFeature(AdditionalMsiSdnFtrInfo ftrInfo, String sessionId) throws ApplicationException;
}
