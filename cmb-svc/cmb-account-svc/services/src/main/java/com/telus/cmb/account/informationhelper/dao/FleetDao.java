package com.telus.cmb.account.informationhelper.dao;

import java.util.Collection;
import java.util.List;

import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.TalkGroupInfo;

public interface FleetDao {

	/**
	 * Retrieve fleets associated to a ban
	 *
	 * @param   int       billing account number (BAN)
	 * @returns FleetInfo[]  array of FleetInfo
	 */
	public Collection<FleetInfo> retrieveFleetsByBan(int ban);

	/**
	 * Retrieve Talk Groups counter by Urban Id and Fleet Id and Ban
	 *
	 * @params  Urban Id and Fleet Id , Ban
	 * @return  int Talk Groups counter
	 */
	public int retrieveAssociatedAccountsCount(int urbanId,int fleetId);

	/**
	 * Retrieve Associated TalkGroup Count
	 * 
	 * @param pFleeIdentity
	 * @param ban
	 * @return int	count 
	 */
	int retrieveAssociatedTalkGroupsCount(FleetIdentityInfo pFleeIdentity,int ban);
	/**
	 * Gives the count of Attached Subscribers Talk group
	 * 
	 * @param urbanID
	 * @param fleetID
	 * @param talkGroupId
	 * @param ban
	 * @return int Count 
	 */
	int retrieveAttachedSubscribersCountForTalkGroup (int urbanID, int fleetID,int talkGroupId, int ban);
	/**
	 * Returns Collection of TalkGroupInfo 
	 * @param pBan
	 * @return Collection TalkGroupInfo
	 */
	List<TalkGroupInfo> retrieveTalkGroupsByBan(int ban);

}
