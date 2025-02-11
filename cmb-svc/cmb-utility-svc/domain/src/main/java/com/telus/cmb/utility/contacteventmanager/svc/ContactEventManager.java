package com.telus.cmb.utility.contacteventmanager.svc;

import com.telus.api.ApplicationException;
import com.telus.eas.contactevent.info.SMSNotificationInfo;

public interface ContactEventManager {
	
	/**
	 * Record a Subscriber Authentication Contact Event in CONE database with optional associated dealer ids (could be null).
	 * 
	 * @param subcriptionID is CODS.SUBSCRIPTION_ID
	 * @param isAuthenticationSuccedded
	 * @param channelOrganizationID is CODS.CHNL_ORG_CD
	 * @param outletID is CODS.OUTLET_CODE
	 * @param salesRepID is CODS.SALES_REP_CD
	 * @param applicationID is Telus API - Provider application ID
	 * @param userID is Telus API - Provider user ID
	 */
	void logSubscriberAuthentication(long subcriptionID,boolean isAuthenticationSucceeded,
			String channelOrganizationID,String outletID,String salesRepID,String applicationID,
			String userID) throws ApplicationException;

	/**
	 * Record a Subscriber Authentication Contact Event in CONE database with optional associated dealer ids (could be null).
	 * 
	 * @param min is Knowbility subscriber number
	 * @param isAuthenticationSucceeded
	 * @param channelOrganizationID is CODS.CHNL_ORG_CD
	 * @param outletID is CODS.OUTLET_CODE
	 * @param salesRepID is CODS.SALES_REP_CD
	 * @param applicationID is Telus API - Provider application ID
	 * @param userID is Telus API - Provider user ID
	 */
	void logSubscriberAuthentication(String min,boolean isAuthenticationSucceeded,
			String channelOrganizationID,String outletID,String salesRepID,String applicationID,
			String userID) throws ApplicationException;

	/**
	 * Record an Account Authentication Contact Event in CONE database with optional associated dealer ids (could be null).
	 * 
	 * @param accountID is CODS.CLIENT_ACCOUNT_ID
	 * @param isAuthenticationSuccedded
	 * @param channelOrganizationID is CODS.CHNL_ORG_CD
	 * @param outletID is CODS.OUTLET_CODE
	 * @param salesRepID is CODS.SALES_REP_CD
	 * @param applicationID is Telus API - Provider application ID
	 * @param userID is Telus API - Provider user ID
	 */
	void logAccountAuthentication(long accountID,boolean isAuthenticationSucceeded,
			String channelOrganizationID,String outletID,String salesRepID,
			String applicationID,String userID) throws ApplicationException;

	/**
	 * Record an Account Authentication Contact Event in CONE database with optional associated dealer ids (could be null).
	 * 
	 * @param accountID is CODS.CLIENT_ACCOUNT_ID
	 * @param isAuthenticationSuccedded
	 * @param channelOrganizationID is CODS.CHNL_ORG_CD
	 * @param outletID is CODS.OUTLET_CODE
	 * @param salesRepID is CODS.SALES_REP_CD
	 * @param applicationID is Telus API - Provider application ID
	 * @param userID is Telus API - Provider user ID
	 */
	void logAccountAuthentication(String ban,boolean isAuthenticationSucceeded,
			String channelOrganizationID,String outletID,String salesRepID,String applicationID,
			String userID) throws ApplicationException;

	void processNotification(SMSNotificationInfo notification) throws ApplicationException;

}