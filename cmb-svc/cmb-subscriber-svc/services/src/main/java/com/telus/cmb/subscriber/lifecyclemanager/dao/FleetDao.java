package com.telus.cmb.subscriber.lifecyclemanager.dao;

import java.util.Collection;


import com.telus.api.ApplicationException;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;

public interface FleetDao {
	void addMemberIdentity(IDENSubscriberInfo idenSubscriberInfo,
			SubscriberContractInfo subscriberContractInfo,
			String dealerCode,
			String salesRepCode,
			int urbanId,
			int fleetId,
			String memberId,
			boolean pricePlanChange, String sessionId) throws ApplicationException;

	void changeMemberId(IDENSubscriberInfo idenSubscriberInfo, String newMemberId
			, String sessionId) throws ApplicationException;

	void changeMemberIdentity(IDENSubscriberInfo idenSubscriberInfo
			, int newUrbanId, int newFleetId, String newMemberId
			, String sessionId) throws ApplicationException;

	void changeTalkGroups(IDENSubscriberInfo idenSubscriberInfo, com.telus.eas.account.info.TalkGroupInfo[] addedTalkGroups
			, com.telus.eas.account.info.TalkGroupInfo[] removedTalkGroups, String sessionId) throws ApplicationException;

	int[] retrieveAvailableMemberIDs(int urbanId, int fleetId, String memberIdPattern, int max
			, String sessionId) throws ApplicationException; 

	void removeMemberIdentity(IDENSubscriberInfo idenSubscriberInfo,
			SubscriberContractInfo subscriberContractInfo,
			String dealerCode,
			String salesRepCode,
			boolean pricePlanChange, String sessionId) throws ApplicationException;

	IDENSubscriberInfo reserveMemberId(IDENSubscriberInfo idenSubscriberInfo
			, FleetIdentityInfo fleetIdentityInfo, String wildCard,boolean isPtnBasedFleet, String sessionId)
	throws ApplicationException;

	String[] retrieveAvailableMemberIds(int urbanId, int fleetId
			, String memberIdPattern, int maxMemberIds, String sessionId) throws ApplicationException;

	/**
	 * Retrieve talkgroups associated to a subscriber
	 *
	 * <P>This method retrieves a list of all talkgroups of a specific subscriber.
	 *
	 * @param   int                 BAN - billing account number
	 * @param   String              subscriber id
	 * @param   String              product type
	 * @returns Collection          list of TalkGroupInfo
	 *
	 * @excpetion   TelusException
	 */
	Collection<TalkGroupInfo> retrieveTalkGroupsBySubscriber(int ban, String subscriberId
			, String sessionId) throws ApplicationException;
	
	boolean availableFleetList(IDENSubscriberInfo pIdenSubscriberInfo,FleetInfo fleetInfo, String sessionId)throws ApplicationException;
	
}
