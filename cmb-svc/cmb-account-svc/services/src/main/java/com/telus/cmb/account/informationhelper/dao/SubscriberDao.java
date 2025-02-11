package com.telus.cmb.account.informationhelper.dao;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telus.api.ApplicationException;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.PoolingPricePlanSubscriberCountInfo;
import com.telus.eas.account.info.PricePlanSubscriberCountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo.DataSharingResultInfo;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo;

public interface SubscriberDao {

	/**
	 * Returns a list containing up to 'maxNumbers' phone numbers. The numbers returned
	 * belong to currently suspended subscribers of the Account
	 *
	 * @param   int       billing account number (BAN)
	 * @param   int       maximum of active phone numbers to return
	 * @returns String[]  list of phone numbers  
	 */
	Collection<String> retrieveActiveSubscriberPhoneNumbers(int ban, int maximum);

	/**
	 * Returns a list containing up to 'maxNumbers' phone numbers. The numbers returned
	 * belong to currently active subscribers of the Account
	 *
	 * @param   int       billing account number (BAN)
	 * @param   int       maximum of active phone numbers to return
	 * @returns String[]  list of phone numbers   *	  
	 */
	Collection<String> retrieveSuspendedSubscriberPhoneNumbers(int ban, int maximum);

	/**
	 * This method is for IDEN only. Improper method name. TO DO: Rename method or change the query to support what the method name 
	 * truly suggests.
	 */
	Map<String, String> retrievePhoneNumbersForBAN(int ban);

	int retrieveAttachedSubscribersCount(int ban, FleetIdentityInfo fleetIdentityInfo);

	Collection<ProductSubscriberListInfo> retrieveProductSubscriberLists(int ban);

	/**
	 * Returns a list containing up to 'maxNumbers' subscriber Ids.   
	 *
	 * @param   int   billing account number (BAN)
	 * @param   char  status of the subscriber
	 * @param   int 	maximum of phone numbers to return
	 * @returns String[] list of subscribers ids	  
	 */
	List<String> retrieveSubscriberIdsByStatus(int banId, char status, int maximum);

	/**
	 * Return the list of subscriber phone numbers on this account filtered by specified subscriber status upto the
	 * specified maximum.
	 *
	 * @param   int       billing account number (BAN)
	 * @param char        the subscriber's status ( see one of the Subscriber.STATUS_xxx constants )
	 * @param   int       maximum of suspended phone numbers to return
	 * @returns String[]  list of phone numbers   
	 */
	List<String> retrieveSubscriberPhoneNumbersByStatus(int banId, char status, int maximum) ;
	/**
	 * Checks the FeatureCatogory Exists on the Subscriber
	 * 
	 * @param int       billing account number (BAN)
	 * @param String   pCategoryCode 
	 * @return boolean
	 */
	boolean isFeatureCategoryExistOnSubscribers(int ban, String pCategoryCode);

	/**
	 * Returns a PoolingPricePlanSubscriberCountInfo object representing all subscribers on the given BAN 
	 * participating in the given pooling group and also for Zero Minute pooling
	 *
	 * @param int - billing account number (BAN)
	 * @param String - product type
	 * @param int - pooling group ID
	 * @returns List PoolingPricePlanSubscriberCountInfo
	 */
	List<PoolingPricePlanSubscriberCountInfo> retrievePoolingPricePlanSubscriberCounts(
			final int banId, final int poolGroupId, final boolean zeroMinute);

	List<ServiceSubscriberCount> retrieveServiceSubscriberCounts(int banId, String[] serviceCodes, boolean includeExpired);

	/**
	 * Returns an array of Subscribers with minute-pooling capable price plans and
	 * pooling-enabled features on their contract.
	 *
	 * @param   int       billing account number (BAN)
	 * @param	String    pooling coverage type - long distance or airtime coverage type
	 * @returns List of PricePlanSubscriberCount
	 */
	List<PricePlanSubscriberCount> retrieveMinutePoolingEnabledPricePlanSubscriberCounts(int banId, String[] poolingCoverageTypes);

	/**
	 * Returns an array of PricePlanSubscriberCountInfo, which represents all subscribers on the given BAN
	 * on dollar pooling price plans.
	 *
	 * @param int - billing account number (BAN)
	 * @param String - product type
	 * @returns List of PricePlanSubscriberCountInfo
	 */
	List <PricePlanSubscriberCountInfo> retrievePricePlanSubscriberCountInfo(int banId, String productType);

	/**
	 * Retrieves shareable (Add-A-Line, Family Talk) priceplan subscriber counts
	 * by billing account number (BAN).
	 *
	 * @param String - billing account number (BAN)
	 * @returns List of PricePlanSubscriberCountInfo
	 */
	List <PricePlanSubscriberCountInfo> retrieveShareablePricePlanSubscriberCount(int ban);
	/**
	 * Retrieves Hotlined Phone Number
	 * 
	 * @param int BAN
	 * @return PhoneNumber String 
	 */
	String retrieveHotlinedSubscriberPhoneNumber(int ban);

	/**
	 * Retrieve the number of CDMA / HSPA subscribers. This method is created for IVR for performance purpose. 
	 * Instead of looping through the whole subscriber list, this method queries the database according to the
	 * serial number. If it is an DUMMY ESN (for USIM), then it's counted as HSPA. Otherwise, it's a CDMA. 
	 * It includes subscribers with product type "C" only.
	 * @param int - ban
	 * @return HashMap - the number of CDMA/HSPA subscribers
	 */

	HashMap<String,Integer> retrievePCSNetworkCountByBan (int ban);

	List<PricePlanSubscriberCount> retrieveMinutePoolingEnabledPricePlanSubscriberCounts(int banId);
	
	String[] retrieveSubscriberIdsByServiceFamily(int banId, String familyTypeCode, Date effectiveDate) ;
	
	SubscribersByDataSharingGroupResultInfo[] retrieveSubscribersByDataSharingGroupCodes(int banId, String[] dataSharingGroupCodes, Date effectiveDate) ;
	
	String retrieveChangedSubscriber( int ban, String subscriberId, String productType, Date searchFromDate,  Date searchToDate) throws ApplicationException ;	

	Collection<DataSharingResultInfo> retrieveSubscriberDataSharingInfoList(int banId, String[] dataSharingGroupCodes) throws ApplicationException;
	
	Collection<DataSharingResultInfo> retrieveSubscriberDataSharingInfoListUsingSqlPackage(int banId, String[] dataSharingGroupCodes) throws ApplicationException;
	
	Map<String, List<String>> retrieveFamilyTypesBySocs(String[] socCodes) throws ApplicationException;
}
