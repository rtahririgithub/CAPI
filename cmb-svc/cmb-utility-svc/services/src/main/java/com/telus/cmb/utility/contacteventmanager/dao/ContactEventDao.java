package com.telus.cmb.utility.contacteventmanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.contactevent.info.SMSNotificationInfo;



public interface ContactEventDao {
	  void processNotification(SMSNotificationInfo notification) throws ApplicationException; 

	  /**
	   * Record a Subscriber Authentication Contact Event in CONE database
	   * with optional associated dealer ids (could be null).
	   * @param subscriptionID is CODS.SUBSCRIPTION_ID
	   * @param isAuthenticationSuccedded
	   * @param channelOrganizationID is CODS.CHNL_ORG_CD
	   * @param outletID is CODS.OUTLET_CODE
	   * @param salesRepID is CODS.SALES_REP_CD
	   * @param applicationID is Telus API - Provider application ID
	   * @param userID is Telus API - Provider user ID
	   */
	  @Deprecated 
	  //As of 2015 Oct release, as part of SERV DB upgrade, this method is marked as deprecated and should not use any more. 
	  void logSubscriberAuthentication (long subscriptionID,boolean isAuthenticationSucceeded,
			  String channelOrganizationID,String outletID,String salesRepID,String applicationID,
			  String userID) throws ApplicationException;

	  /**
	   * Record an Account Authentication Contact Event in CONE database
	   * with optional associated dealer ids (could be null).
	   * @param accountID is CODS.CLIENT_ACCOUNT_ID
	   * @param isAuthenticationSuccedded
	   * @param channelOrganizationID is CODS.CHNL_ORG_CD
	   * @param outletID is CODS.OUTLET_CODE
	   * @param salesRepID is CODS.SALES_REP_CD
	   * @param applicationID is Telus API - Provider application ID
	   * @param userID is Telus API - Provider user ID
	   */
	  @Deprecated 
	  //As of 2015 Oct release, as part of SERV DB upgrade, this method is marked as deprecated and should not use any more. 
	  void logAccountAuthentication(long accountID, boolean isAuthenticationSucceeded,
			  String channelOrganizationID,String outletID, String salesRepID,
			  String applicationID,String userID) throws ApplicationException;

	  /**
	   * Look up ODS Account ID given a BAN
	   * @param BAN is Knowbility Account Number (BAN)
	   */
	  long getAccountID (String ban) throws ApplicationException;


	  /**
	   * Look up ODS Subscription ID given a (Knowbility) subscriber number
	   * @param min is Knowbility subscriber number
	   */
	  long getSubscriptionID (String min) throws ApplicationException;
	  
		
}
