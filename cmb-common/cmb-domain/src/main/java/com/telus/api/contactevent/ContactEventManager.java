package com.telus.api.contactevent;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import com.telus.api.*;
import com.telus.api.account.*;

public interface ContactEventManager {

  /**
   * Create a new SMSNotification object.
   *
   * @return SMSNotification
   */
  SMSNotification newSMSNotification() throws TelusAPIException;

  /**
   * Process SMSNotification event.
  *
   * @param notification SMSNotification
   * @throws UnknownSubscriberException
   * @throws EquipmentAccountMissMatchException
   * @throws EquipmentNotSMSCapableException
   * @throws TelusAPIException
   */
  void process(SMSNotification notification) throws UnknownSubscriberException, EquipmentAccountMissMatchException, EquipmentNotSMSCapableException, TelusAPIException;

  /**
   * Record a Subscriber Authentication Contact Event in CONE database
   * with optional associated dealer ids (could be null).
   *
   * @param subscriptionID is CODS.SUBSCRIPTION_ID
   * @param isAuthenticationSucceeded
   * @param channelOrganizationID is CODS.CHNL_ORG_CD
   * @param outletID is CODS.OUTLET_CODE
   * @param salesRepID is CODS.SALES_REP_CD
   * @throws TelusAPIException
   */
  void logSubscriberAuthentication(
      long subscriptionID,
      boolean isAuthenticationSucceeded,
      String channelOrganizationID,
      String outletID,
      String salesRepID
      ) throws TelusAPIException;

  /**
   * Record a Subscriber Authentication Contact Event in CONE database
   * with optional associated dealer ids (could be null).
   *
   * @param min is Knowbility subscriber number
   * @param isAuthenticationSucceeded
   * @param channelOrganizationID is CODS.CHNL_ORG_CD
   * @param outletID is CODS.OUTLET_CODE
   * @param salesRepID is CODS.SALES_REP_CD
   * @throws TelusAPIException
   */
  void logSubscriberAuthentication(
      String min,
      boolean isAuthenticationSucceeded,
      String channelOrganizationID,
      String outletID,
      String salesRepID
      ) throws TelusAPIException;


  /**
   * Record an Account Authentication Contact Event in CONE database
   * with optional associated dealer ids (could be null).
   *
   * @param accountID is CODS.CLIENT_ACCOUNT_ID
   * @param isAuthenticationSucceeded
   * @param channelOrganizationID is CODS.CHNL_ORG_CD
   * @param outletID is CODS.OUTLET_CODE
   * @param salesRepID is CODS.SALES_REP_CD
   * @throws TelusAPIException
   */
  void logAccountAuthentication(
      long accountID,
      boolean isAuthenticationSucceeded,
      String channelOrganizationID,
      String outletID,
      String salesRepID
      ) throws TelusAPIException;

  /**
   * Record an Account Authentication Contact Event in CONE database
   * with optional associated dealer ids (could be null).
   *
   * @param ban is Knowbility account number (BAN)
   * @param isAuthenticationSucceeded
   * @param channelOrganizationID is CODS.CHNL_ORG_CD
   * @param outletID is CODS.OUTLET_CODE
   * @param salesRepID is CODS.SALES_REP_CD
   * @throws TelusAPIException
   */
  void logAccountAuthentication(
      String ban,
      boolean isAuthenticationSucceeded,
      String channelOrganizationID,
      String outletID,
      String salesRepID
      ) throws TelusAPIException;


}
