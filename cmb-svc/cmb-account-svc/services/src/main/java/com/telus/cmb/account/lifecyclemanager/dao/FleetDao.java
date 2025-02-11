package com.telus.cmb.account.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.TalkGroupInfo;

public interface FleetDao {
	/**
	 * Create new fleet (and associate it to the account)
	 *
	 * <P>
	 * This method creates a new fleet and associates it to the account.
	 *
	 * <P>
	 * The following attributes are mandatory: network network/ndap id type
	 * fleet type ('B' public, 'P' Private or 'S' shared) name fleet name/alias
	 * #subscribers expected number of subscribers
	 *
	 * <P>
	 * When creating a new fleet, the 'expected # of subscribers' and 'expected #
	 * of talkgroups' are set as following: expected # of subs as input plus 2
	 * for future growth expected # of tg's 0 (number will increased as talk
	 * groups are being added)
	 *
	 * @param int
	 *            BAN - billing account number
	 * @param short
	 *            network/ndap id
	 * @param FleetInfo
	 *            fleet information
	 * @param int
	 *            number of subscribers that are expected to be added
	 *
	 * @returns FleetInfo fleet information
	 *
	 * @see FleetInfo
	 *
	 * @exception ApplicationException
	 *                10012 Failed to get primary NGP for network.
	 *
	 */
	public FleetInfo createFleet(int ban, short network, FleetInfo fleetInfo, int numberOfSubscribers, String sessionId) 
		throws ApplicationException;
	
	/**
	 * Add a fleet to an account
	 *
	 * <P>
	 * This method associates an existing shared fleet to the account.
	 *
	 * <P>
	 * The following attributes are mandatory: urbanId/fleetId primary key to
	 * fleet to be attached #subscribers expected number of subscribers
	 *
	 * @param int
	 *            BAN - billing account number
	 * @param short
	 *            network/ndap id
	 * @param FleetInfo
	 *            fleet information (existing)
	 * @param int
	 *            number of subscribers that are expected to be added
	 *
	 * @see FleetInfo
	 *
	 * @exception ApplicationException
	 *                10012 Failed to get primary NGP for network.
	 *
	 */

	public void addFleet(int ban, short network, FleetInfo fleetInfo,
			int numberOfSubscribers, String sessionId) throws ApplicationException;
	
	
	/**
	 * Dissociate Fleet
	 *
	 * @param int ban 				- billing account number
	 * @param FleetInfo fleetInfo	- fleet information (existing)
	 *
	 * @see FleetInfo
	 */
	public void dissociateFleet(int ban, FleetInfo fleetInfo, String sessionId) throws ApplicationException;
	
	/**
	 * Remove a talkgroup from an account
	 * <P>
	 * This method disassociates an existing TalkGroup from the account.
	 *
	 * @param int
	 *            BAN - billing account number
	 * @param TalkGroupInfo
	 *            talkgroup information existing
	 *
	 * @see TalkGroupInfo
	 *
	 */
	public void removeTalkGroup(int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException;
	
	/**
	 * Add a talkgroup to an account
	 *
	 * <P>
	 * This method associates an existing TalkGroup to the account.
	 *
	 * In order for a talkGroup to be associated with an account, the account
	 * has to be associated with the fleet that the talkgroup belongs to.
	 *
	 * <P>
	 * When associating an existing talkgroup, the following attributes are
	 * mandatory: urbanId/fleetId primary key to which to attach the talkgroup
	 * talkGroupId primary key of talkgroup to be attached
	 *
	 * @param int
	 *            BAN - billing account number
	 * @param TalkGroupInfo
	 *            talkgroup information existing
	 *
	 * @see TalkGroupInfo
	 * @throws ApplicationException
	 * 		10015 Expected no. of Talk Groups should lie between 0 and 255.
	 *
	 */
	public void addTalkGroup(int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException;
	
	/**
	 * Create a new talkgroup (and associate it to the account)
	 *
	 * <P>
	 * This method creates a new talkgroup and associates it to the account.
	 *
	 * In order for a talkGroup to be associated with an account, the account
	 * has to be associated with the fleet that the talkgroup belongs to.
	 *
	 * <P>
	 * When creating a new talkgroup, the following attributes are mandatory:
	 * urbanId/fleetId primary key to which to attach the talkgroup name
	 * talkgroup name/alias
	 *
	 * @param int
	 *            BAN - billing account number
	 * @param TalkGroupInfo
	 *            talkgroup information new
	 *
	 * @returns TalkGroupInfo talkgroup information
	 *
	 * @see TalkGroupInfo
	 *
	 * @exception ApplicationException	
	 *  10014 The requested TG alias %s already exists for this UF
	 *  10015 Expected no. of Talk Groups should lie between 0 and 255.
	 */
	
	public TalkGroupInfo createTalkGroup(int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException;
	
	/**
	 * Update a talkgroup
	 *
	 * <P>
	 * This method updates some attributes of a talkgroup.
	 *
	 * The following attributes can be updated:
	 * - alias
	 * - priority
	 *
	 * @param int
	 *            BAN - billing account number
	 * @param TalkGroupInfo
	 *            talkgroup information new
	 *
	 * @see TalkGroupInfo
	 *
	 * @exception ApplicationException
	 *
	 */
	public void updateTalkGroup(int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException;
}
