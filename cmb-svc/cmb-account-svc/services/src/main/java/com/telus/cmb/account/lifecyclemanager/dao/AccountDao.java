package com.telus.cmb.account.lifecyclemanager.dao;

import java.util.Date;
import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AddressValidationResultInfo;
import com.telus.eas.account.info.BillParametersInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.CancellationPenaltyInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CustomerNotificationPreferenceInfo;
import com.telus.eas.account.info.FutureStatusChangeRequestInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.framework.info.DiscountInfo;

public interface AccountDao {
	
	void updateEmailAddress(int ban, String emailAddress, String sessionId) throws ApplicationException;	
	
	void updateBillCycle(int ban, short billCycle, String sessionId) throws ApplicationException;
	
	void updateHotlineInd(int ban, boolean hotLineInd, String sessionId) throws ApplicationException;
	
	int createAccount(AccountInfo pAccountInfo, String sessionId) throws ApplicationException;	

	void updateBillParamsInfo(int banId, BillParametersInfo billParametersInfo, String sessionId) throws ApplicationException ;	

	void updateAccountPassword(int pBan, String pAccountPassword,String sessionId) throws ApplicationException;		
	
	CancellationPenaltyInfo retrieveCancellationPenalty(int pBan, 
				String sessionId) throws ApplicationException;
	
	CancellationPenaltyInfo[] retrieveCancellationPenaltyList(int banId, String[] subscriberId, 
			String sessionId) throws ApplicationException;
	
	void updateNationalGrowth(int ban, String nationalGrowthIndicator, String homeProvince, String sessionId ) throws ApplicationException;
	
	void restoreSuspendedAccount(int ban, Date restoreDate, String restoreReasonCode, String restoreComment, boolean collectionSuspensionsOnly, String sessionId) throws ApplicationException;
	
	void updateFutureStatusChangeRequest(int ban, FutureStatusChangeRequestInfo futureStatusChangeRequestInfo, String sessionId) throws ApplicationException;
	
	void  suspendAccount(int ban, Date activityDate, String activityReasonCode, String userMemoText,String sessionId) throws ApplicationException;
	List<FutureStatusChangeRequestInfo> retrieveFutureStatusChangeRequests(int ban,String sessionId )throws ApplicationException;
	
	/**
	    * Cancel an account
	    *
	    * The account has to be in 'Open' or 'Suspended' status.
	    *
	    * Under certain conditions the subscriber is charged a cancellation penalty which will be
	    * applied automatically unless the fee is waived by giving a reason.
	    *
	    * @param   int         billing account number  (mandatory)
	    * @param   Date        date on which to cancel the account (optional - defaul: today)
	    * @param   String      reason code for cancelling the account (mandatory)
	    * @param   String      method by which the deposit should be returned (mandatory)
	    *                      possible values are: 'O' cover open debts
	    *                                           'R' refund entire amount
	    *                                           'E' refund excess amount
	    * @param   String      reason for waving applicable cancellation fee (optional - default: fee is charged))
	    * @param   String      optional comment for cancelling the account (optional)
	    *
	    * @exception ApplicationException
	    */
	
	void cancelAccount(int ban, Date activityDate, String activityReasonCode, String depositReturnMethod, String waiveReason,
			String userMemoText, boolean isPortActivity, String sessionId) throws ApplicationException;
	
	void updateAuthorizationNames(int ban, ConsumerNameInfo[] authorizationNames,String sessionId) throws ApplicationException;
	
	void updateAutoTreatment(int ban, boolean holdAutoTreatment,String sessionId) throws ApplicationException;
	
	void updateBrand (int ban, int brandId, String memoText,String sessionId) throws ApplicationException;
	
	void updateSpecialInstructions(int ban, String specialInstructions,String sessionId ) throws ApplicationException;
	
	int[] retrieveAccountsByTalkGroup(int urbanId, int fleetId, int talkGroupId, String sessionId) throws ApplicationException;
	
	  /**
	   * Perform Address validation
	   *
	   * @param   AddressInfo   address information
	   * @return  AddressValidationResultInfo   address validation result
	   *
	   * @exception ApplicationException
	   *
	   * @see AddressInfo
	   * @see AddressValidationResultInfo
	   */
	AddressValidationResultInfo validateAddress(com.telus.eas.account.info.AddressInfo addressInfo, String sessionId) throws ApplicationException;
	
	void changePostpaidConsumerToPrepaidConsumer(int ban, short prepaidBillCycle, String sessionId) throws ApplicationException;
	
	List<DiscountInfo> retrieveDiscounts(int ban, String sessionId) throws ApplicationException;
	
	void cancelAccountForPortOut(int ban, String activityReasonCode, Date activityDate, boolean portOutInd, boolean isBrandPort, String sessionId) throws ApplicationException;
	

	/**
	 * Update an account - includes validation of all information except address
	 * and duplicate account check. It is assumed that the address that is being
	 * passed in is the address that needs to be stored, therefore the address
	 * apply type is set to 'I'. (It is up to the client to perform address
	 * validation if required.)
	 *
	 * @param AccountInfo
	 *            all general attributes of the account (i.e. account type, sub
	 *            type etc)
	 *
	 * @param blockDirectUpdate rather it should allow/disallow update on certain fields. if true, certain fields/objects will not be
	 *                          stored even it's set.
	 *
	 * @param sessionId   the EJB sessionId
	 * @see AccountInfo
	 *
	 * @excpetion ApplicationException
	 */
	 void updateAccount(AccountInfo accountInfo, boolean blockDirectUpdate, String sessionId) throws ApplicationException; 

	 /**
	  * This is preserved in case there's a need of the same functionality without the blockDirectUpdate restriction.
	  * This is the same as calling {@link #updateAccount(AccountInfo, boolean, String)} with blockDirectUpdate==false.
	  * 
	  * @param accountInfo
	  * @param sessionId
	  * @throws ApplicationException
	  */
	 void updateAccount(AccountInfo accountInfo, String sessionId) throws ApplicationException;
	 
	 /**
	   * Suspend Account for Port Out
	   * @param int banNumber  - Billing Account Number
	   * @param String deactivationReason - deactivation reason code 
	   * @param Date activityDate - activity date 
	   * @param String portOutInd - port out indicator 
	   * @excpetion ApplicationException
	   */
	  void suspendAccountForPortOut(int ban, String activityReasonCode, Date activityDate, String portOutInd,String sessionId) throws ApplicationException;
	  
	  void updatePersonalCreditInformation(int ban, PersonalCreditInfo personalCreditInfo, String sessionId) throws ApplicationException;
	  
	  void updateBusinessCreditInformation(int ban, BusinessCreditInfo businessCreditInfo, String sessionId) throws ApplicationException;

	  List<CustomerNotificationPreferenceInfo> getCustomerNotificationPreferenceList(int ban, String sessionId) throws ApplicationException;

	  void updateCustomerNotificationPreferenceList(int ban, List<CustomerNotificationPreferenceInfo> notificationPreferenceList, String sessionId) throws ApplicationException;

}
