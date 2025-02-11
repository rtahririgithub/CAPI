package com.telus.cmb.subscriber.lifecyclehelper.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.eas.subscriber.info.ResourceInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.SubscriberIdentifierInfo;
import com.telus.eas.subscriber.info.LightWeightSubscriberInfo;
import com.telus.eas.subscriber.info.ResourceChangeHistoryInfo;
import com.telus.eas.subscriber.info.SubscriberHistoryInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.eas.subscriber.info.SubscriptionRoleInfo;

/**
 * Retrievals of subscriber information
 * 
 *
 */
public interface RollbackSubscriberDao {


	/**
	 * This method retrieves the subscriber ID and subscription ID (EXTERNAL_ID in KB) with the given input. The underlying query may
	 * return more than 1 result but this method will execute the procedure that always returns the first record only, which is supposed
	 * to be the latest one.
	 * 
	 * The current logic indicates that if a BAN is passed in, include the cancelled subscriber.
	 * If the BAN is not set, do not include cancelled subscriber. 
	 * 
	 * @param ban  optional
	 * @param phoneNumber   required. Validation is done to make sure the phone number is not empty.
	 * @return
	 * @throws ApplicationException
	 */
	SubscriberIdentifierInfo retrieveLatestSubscriberIdentifierInfoByBanAndPhoneNumber(int ban, String phoneNumber) throws ApplicationException;
	
	/**
	 * Retrieves subscriber list from KB data source by BAN, subscriber ID, 
	 * maximum subscriber count to be returned and flag indicating if 
	 * cancelled subscribers should be included in the list.
	 * 
	 * @param ban
	 * @param maximumCount
	 * @param includeCancelled
	 * @return Collection<SubscriberInfo>  Collection of subscriber info object.  The list size will be based on the maximumCount parameter
	 */
	Collection<SubscriberInfo> retrieveSubscriberListByBAN(int ban, int maximumCount, boolean includeCancelled);
	
	/**
	 * Retrieves subscriber list from KB data source by BAN, subscriber ID, 
	 * maximum subscriber count to be returned and flag indicating if 
	 * cancelled subscribers should be included in the list.
	 * 
	 * @param ban required
	 * @param subscriberId required
	 * @param includeCancelled
	 * @param subId
	 * @return Collection<SubscriberInfo>  Collection of subscriber info object.  The list size will be based on the maximumCount parameter
	 */
	Collection<SubscriberInfo> retrieveSubscriberListByBANAndSubscriberId(int ban, String subscriberId, boolean includeCancelled, int maximumCount);

	/**
	 * Retrieves subscriber list from KB data source by subscriber ID, 
	 * maximum subscriber count to be returned and flag indicating if 
	 * cancelled subscribers should be included in the list.
	 * 
	 * @param subscriberId
	 * @param includeCancelled
	 * @param maximumCount
	 * @return
	 */
	Collection<SubscriberInfo> retrieveSubscriberListBySubscriberID(String subscriberId, boolean includeCancelled, int maximumCount);
	
	/**
	 * retrieve a list of partially reserved subscriber info by ban
	 * @param int ban
	 * @param int maximum
	 * 
	 * @return Collection collection of SubscriberInfo
	 * 
	 * @throws ApplicationException
	 */
	List<String> retrievePartiallyReservedSubscriberListByBan(int ban, int maximum);

	/**
	 * retrieve a list of subscriber info for ported out subscribers by BAN ordered by activation date, most recent first
	 *
	 * @param int BAN
	 * @param int listLength to retrieve <code>0</code> means all
	 * @return Collection collection of SubscriberInfo
	 *
	 * @throws ApplicationException
	 *
	 * @see com.telus.eas.subscriber.info.SubscriberInfo
	 */	
	Collection<SubscriberInfo> retrievePortedSubscriberListByBAN(int ban, int listLength);

	/**
	 * Retrieve Resource Change History Info for Subscriber
	 *
	 * @param ban
	 * @param String subscriber Id
	 * @param String type
	 * @param Date from
	 * @param Date to
	 * @return Array ResourceChangeHistory[]
	 *
	 * @throws ApplicationException
	 */
	List<ResourceChangeHistoryInfo> retrieveResourceChangeHistory(int ban, String subscriberID, String type, java.util.Date from, java.util.Date to);

	/**
	 * retireve light weight subscriber list for given BAN
	 * @param banId
	 * @param listLength
	 * @param includeCancelled
	 * 
	 * @throws ApplicationException
	 * 
	 */
	List<LightWeightSubscriberInfo> retrieveLightWeightSubscriberListByBAN( int banId, boolean isIDEN, int listLength, boolean includeCancelled);

	/**
	 * retrieve a Subscription ID by imsi
	 *
	 * @param String imsi
	 * @return Subscribtion ID
	 *
	 * @throws ApplicationException
	 *
	 * @see com.telus.eas.subscriber.info.SubscriberInfo
	 * 
	 * transaction-attribute = NotSupported
	 */
	String retrieveLastAssociatedSubscriptionId (String imsi);

	/**
	 * Returns a boolean value of HotlineIndicator.
	 * 
	 * @param subscriberId String
	 * 
	 * @throws ApplicationException
	 * 
	 * @return boolean
	 *
	 */
	boolean retrieveHotlineIndicator(String subscriberId);

	String getPortProtectionIndicator(int ban, String subscriberId, String phoneNumber,String status);

	/**
	 * Retrieves a list of subscription preference for the given sbuscriptionId and preferenceTopicId
	 *
	 * @param banId
	 * @param subscriptionId
	 * @param preferenceTopicId
	 * 
	 * @throws ApplicationException
	 *
	 */
	SubscriptionPreferenceInfo retrieveSubscriptionPreference( long subscriptionId, int preferenceTopicId);

	/**
	 * Retrieve talkgroups associated to a subscriber
	 *
	 * @param   String              Subscriber Id
	 * @returns TalkGroupInfo[]     array of TalkGroupInfo
	 *
	 * @exception ApplicationException
	 *
	 */
	List<String> retrieveAvailableCellularPhoneNumbersByRanges(
			PhoneNumberReservationInfo phoneNumberReservation,
			String     startFromPhoneNumber,
			String     searchPattern,
			boolean   asian,
			int maxNumber) throws ApplicationException;
	/**
	 * Returns boolean value of PostRestricted Constraint
	 * 
	 * @param Integer		ban
	 * @param String		subscriberId
	 * @param String		phoneNumber
	 * @param Srring		status
	 * @return
	 */
	boolean isPortRestricted(int ban, String subscriberId, String phoneNumber, String status);
	/**
	 *Retrieves Subscriber Info Object with phone Number
	 * 
	 * @param String	phoneNumber
	 * @return
	 * @throws ApplicationException 
	 */
	SubscriberInfo retrieveSubscriberByPhoneNumber(String phoneNumber) throws ApplicationException;

	/**
	 * @param ban
	 * @param subscriberID
	 * @param from
	 * @param to
	 * @return
	 */
	List<SubscriberHistoryInfo> retrieveSubscriberHistory(int ban, String subscriberID, java.util.Date from, java.util.Date to);

	/**
	 * @param Integer		ban
	 * @param Integer		urbanId
	 * @param Integer		fleetId
	 * @param Integer		listLength
	 * @return
	 */
	Collection<SubscriberInfo> retrieveSubscriberListByBanAndFleet(int ban, int urbanId, int fleetId, int listLength);
	/**
	 * @param pBan
	 * @param pUrbanId
	 * @param pFleetId
	 * @param pTalkGroupId
	 * @param pListLength
	 * @return
	 */
	Collection<SubscriberInfo> retrieveSubscriberListByBanAndTalkGroup(int ban, int urbanId, int fleetId, int talkGroupId, int listLength);

	/**
	 * Retrieves SubscriptionRoleInfo Object
	 * 
	 * @param String		phoneNumber
	 * @return
	 */
	SubscriptionRoleInfo retrieveSubscriptionRole(String phoneNumber);
	/**
	 * Retrives List of PhoneNumbers for a subscriberStatus,accounttype,banstatus
	 * 
	 * @param Char		subscriberStatus
	 * @param Char		accountType
	 * @param Char		accountSubType
	 * @param Char		banStatus
	 * @param integer	maximum
	 * @return
	 */
	List<String> retrieveSubscriberPhonenumbers(char subscriberStatus, char accountType, char accountSubType, char banStatus, int maximum);
	/**
	 * Retrives List of PhoneNumbers for a subscriberStatus,accounttype,banstatus,addressType
	 * 
	 * @param Char		subscriberStatus
	 * @param Char		accountType
	 * @param Char		accountSubType
	 * @param Char		banStatus
	 * @param addressType
	 * @param integer	maximum
	 * @return
	 */
	List<String> retrieveSubscriberPhonenumbers(char subscriberStatus, char accountType, char accountSubType, char banStatus, String addressType,int maximum);
	/**
	 * @param serialNumber
	 * @param includeCancelled
	 * @return
	 */
	Collection<SubscriberInfo> retrieveSubscriberListBySerialNumber(String serialNumber, boolean includeCancelled);
	
	/**
	 * 
	 * @param phoneNumbers
	 * @param maximum
	 * @param includeCancelled
	 * @return
	 * @throws ApplicationException
	 */
	Collection<SubscriberInfo> retrieveSubscriberListByPhoneNumbers(String[] phoneNumbers, int maximum, boolean includeCancelled) throws ApplicationException;
	
	/**
	 * @param Array		phoneNumbers
	 * @param Boolean	includeCancelled
	 * @return
	 */
	Collection<SubscriberInfo> retrieveSubscriberListByPhoneNumbers(String[] phoneNumbers, boolean includeCancelled);

	/**
	 * retrieves a Collection of subscriber info by IMSI
	 *
	 * @param IMSI
	 * @param includeCancelled
	 * @return
	 * @throws ApplicationException
	 */
	Collection<SubscriberInfo> retrieveHSPASubscriberListByIMSI(String IMSI, boolean includeCancelled) throws ApplicationException;

	/**
	 * @param banId
	 * @param phoneNumber
	 * @param status
	 * @return
	 * @throws ApplicationException
	 */
	long retrieveSubscriptionId(int banId, String phoneNumber, String status) throws ApplicationException;

	/**
	 * Retrieve Email Address 
	 * 
	 * @param ban
	 * @param SubscriberNumber
	 * @return
	 * @throws ApplicationException
	 */
	String retrieveEmailAddress(int ban, String subscriberNumber) throws ApplicationException;
		
	SubscriberIdentifierInfo retrieveSubscriberIdentifierBySubscriptionId(long subscriptionId) throws ApplicationException;
	
	/**
	 * Retrieve Subscriber By BAN and phone number
	 * 
	 * @param ban mandatory
	 * @param PhoneNumber  mandatory
	 * @return
	 * @throws ApplicationException
	 */
	SubscriberInfo retrieveSubscriberByBANAndPhoneNumber  (final int ban, final String phoneNumber) throws ApplicationException;
	
	String retrieveChangedSubscriber( int ban, String subscriberId, String productType, Date searchFromDate,  Date searchToDate) throws ApplicationException ;	
	
	
	Collection<SubscriberInfo> retrieveSubscriberListBySeatResourceNumber(int ban, String seatResourceNumber,int listLength, boolean includeCancelled)throws ApplicationException;
	Collection<SubscriberInfo> retrieveSubscriberListBySeatResourceNumber(String seatResourceNumber,int listLength, boolean includeCancelled)throws ApplicationException;
	SubscriberInfo retrieveSubscriberBySeatResourceNumber( String seatResourceNumber) throws ApplicationException;
	SubscriberInfo retrieveSubscriberByBanAndSeatResourceNumber( int ban,  String seatResourceNumber) throws ApplicationException;
	 
	List<ResourceInfo> retrieveSeatResourceInfoByBanAndPhoneNumber( int ban,  String phoneNumber,boolean includeCancelled) throws ApplicationException;
    SubscriberIdentifierInfo retrieveLatestSubscriberIdentifierInfoBySeatResourceNumber(final int ban, final String seatResourceNumber) throws ApplicationException ;

	int updateSubscriptionRole(String phoneNumber,String subscriptionRoleCd) throws ApplicationException;
	
	List<LightWeightSubscriberInfo> retrieveLightWeightSubscriberListInSameBan(int ban, String[] subscriberNumList);
	Collection<SubscriberInfo> retrieveSubscriberListByPhoneNumbersNew(final List<String> phoneNumberList, final int maximum,final boolean includeCancelled) throws ApplicationException ;

}
