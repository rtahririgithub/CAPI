package com.telus.cmb.account.informationhelper.dao;

import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.BillParametersInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.ContactDetailInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.account.info.StatusChangeHistoryInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.SubscriberCountInfo;

public interface AccountDao {
	/**
	 * Retrieve Account By billing account number (BAN)
	 *
	 * @param   ban   billing account number (BAN)
	 * @return AccountInfo account related information
	 *
	 * @see AccountInfo
	 */
	AccountInfo retrieveAccountByBan(int ban) ;
	AccountInfo retrieveAccountByBanRollback(int ban) ;
	
	void retrieveAccountExtendProperties(AccountInfo accountInfo);
	SubscriberCountInfo retrieveSubscriberCounts(int ban, char accountType, char accountSubType);

	List<AccountInfo> retrieveAccountsByBan(int[] banArray) ;
	/**
	 * Returns all accounts ever associated with this postal code and last name.
	 *
	 * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 */
	List<AccountInfo> retrieveAccountsByPostalCode(String lastName, String postalCode, int maximum);
	/**
	 * Returns all accounts ever associated with a mobile number.
	 *
	 * @param phoneNumber the mobile number to search on.
	 * @param onlyLastAccount include Past Accounts, on which subscriber cancelled
	 *
	 */
	List<AccountInfo> retrieveAccountsByPhoneNumber(String phoneNumber, boolean onlyLastAccount);

	/**
	 * Returns last associated account (Active/Cancelled) for given seatNumber  ( it may be voip number/toll free number/hsia ).
	 *
	 * @param seatNumber ,the voip/tollfree number/hsia number to search on.
	 * 
	 *
	 */
	List<AccountInfo> retrieveLastAccountsBySeatNumber(String seatNumber);

	/**
	 * Retrieve Account Status for a given billing account number (BAN)
	 *
	 * @param   ban      billing account number (BAN)
	 * @return  String      account status
	 *                      C - Closed
	 *                      N - Cancelled
	 *                      O - Open
	 *                      S - Suspended
	 *                      T - Tentative
	 *
	 */
	String retrieveAccountStatusByBan(int ban) ;

	SearchResultsInfo retrieveAccountsByName(String nameType, String firstName, boolean firstNameExactMatch, String lastName, boolean lastNameExactMatch, char accountStatus, char accountType, String provinceCode, int brandId, int maximum);


	/**
	 * This is a lightweight version of retrieveAccountByBan (int)
	 * 
	 * @param ban
	 * @return
	 */
	AccountInfo retrieveLwAccountByBan(int ban);
	AccountInfo retrieveLwAccountByBanRollback(int ban);
	/**
	 * Retrieve Ban By PhoneNumber
	 *<P> Method works just for subscribers that are not  cancelled
	 *
	 * @param   phoneNumber      phone number
	 * @return AccountInfo account related information
	 * @see AccountInfo
	 */
	int  retrieveBANByPhoneNumber(String phoneNumber);

	/**
	 * Retrieve List of Accounts  that were created, or had a subscriber added, by a given
	 * dealership from a specific date.
	 *
	 * @param dealerCode the dealership to search on.
	 * @param startDate date to start searching from.
	 * @param endDate   date to search to.
	 * @param maximum the total number of bans to retrieve.
	 *
	 * @return list of AccountInfo
	 *
	 * @see AccountInfo
	 */
	List<AccountInfo> retrieveAccountsByDealership(char accountStatus, String dealerCode, java.util.Date startDate
			, java.util.Date endDate,int maximum);

	List<AccountInfo> retrieveAccountsBySalesRep(char accountStatus, String dealerCode
			, String salesRepCode, java.util.Date  startDate, java.util.Date endDate, int maximum);

	int[] retrieveBanIds(char accountType, char accountSubType, char banStatus, int maximum);
	int[] retrieveBanIdByAddressType(char accountType, char accountSubType, char banStatus, char addressType, int maximum); 

	/**
	 * Retrieves the bill parameters given a BAN
	 * 
	 * @param banId					BAN
	 * @return BillParametersInfo	Bill Parameters Info object 
	 */
	BillParametersInfo retrieveBillParamsInfo(int banId);	

	/**
	 * Retrieve Accounts By IMSI
	 * 
	 * @param imsi  imsi
	 * @return Collection of AccountInfo account related information
	 * 
	 * @see AccountInfo
	 */
	int retrieveBanByImsi(String imsi); 

	/**
	 * Retrieve Accounts By SerialNumber (ESN, IMEI etc)
	 *
	 * @param   serialNumber      equipment serial number
	 * @return Collection of AccountInfo account related information
	 *
	 * @see AccountInfo
	 */
	List<AccountInfo> retrieveAccountsBySerialNumber(String serialNumber);  
	/**
	 * @param id
	 * @return String
	 */
	String retrieveCorporateName(int id);

	/**
	 * @param ban
	 * @return
	 */
	int getClientAccountId(int ban); 

	/**
	 * Returns Paper Bill Suppression At Activation Indicator
	 * 
	 * @param pBan			billing account number (BAN)
	 * @return				one of AccountSummary.BILL_SUPPRESSION_AT_ACTIVATION_UNKNOWN,
	 * AccountSummary.BILL_SUPPRESSION_AT_ACTIVATION_NO, BILL_SUPPRESSION_AT_ACTIVATION_YES
	 */	
	String getPaperBillSupressionAtActivationInd(int pBan);

	/**
	 * Returns a list of BusinessCreditIdentityInfo objects.
	 *
	 * @param   		ban	- billing account number (BAN)
	 * @return		  BusinessCreditIdentityInfo[] - array of BusinessCreditIdentityInfo (not null but could be empty)
	 */
	BusinessCreditIdentityInfo[] retrieveBusinessList(int ban);

	/**
	 * Retrieve the authorized names By billing account number (BAN)
	 *
	 * @param   ban     billing account number (BAN)
	 *
	 */
	ConsumerNameInfo[] retrieveAuthorizedNames(int ban); 
	
	
    /**
     * Returns Account status change history
     *
     * @param   int       billing account number (BAN)
     * @param   Date      from Date
     * @param   Date      to Date
     * @returns StatusChangeHistoryInfo[]  array of StatusChangeHistoryInfo
     *
     */
    List<StatusChangeHistoryInfo> retrieveStatusChangeHistory (int ban, java.util.Date fromDate, java.util.Date toDate); 
    
    /**
     * Returns the customer contact info such as phone numbers and email address
     * @param ban
     * @return ContactDetailInfo
     */
    ContactDetailInfo getCustomerContactInfo(int ban);
    
    PersonalCreditInfo retrievePersonalCreditInformation(int ban) throws ApplicationException;
	
	BusinessCreditInfo retrieveBusinessCreditInformation(int ban) throws ApplicationException;
	
	TestPointResultInfo testKnowbilityDataSource();
	
	/**
	 * Retrieve Ban By seatNumber
	 *<P> Method works just for business connect seats that are not  cancelled
	 *
	 * @param   seatNumber,     Business connect seat number
	 * @return ban, billing account number of the seat.
	 * @exception ApplicationException;
	 */
	int  retrieveBANBySeatNumber(String seatNumber);
	
	 List<Integer> retrieveBanListByImsi(final String imsi) ;

}
