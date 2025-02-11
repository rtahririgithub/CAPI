/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.interaction;

import com.telus.api.*;
import java.util.*;

public interface InteractionManager {

    public static final String TYPE_ACCOUNT_STATUS_CHANGE   = "B";
    public static final String TYPE_ADDRESS_CHANGE          = "A";
    public static final String TYPE_BILL_PAYMENT            = "Y";
    public static final String TYPE_EQUIPMENT_CHANGE        = "E";
    public static final String TYPE_PAYMENT_METHOD_CHANGE   = "M";
    public static final String TYPE_PHONE_NUMBER_CHANGE     = "N";
    public static final String TYPE_PREPAID_TOPUP           = "T";
    public static final String TYPE_PRICE_PLAN_CHANGE       = "P";
    public static final String TYPE_SERVICE_CHANGE          = "S";
    public static final String TYPE_SUBSCRIBER_CHANGE       = "R";
    public static final String TYPE_SUBSCRIBER_CHARGE       = "C";
    public static final String TYPE_ROLE_CHANGE             = "O";

  /**
   * Retrieves the interactions occuring within the given date range.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @link aggregation
   *
   */
  Interaction[] getInteractionsByBan(int banId, Date from, Date to) throws TelusAPIException;

  /**
   * Retrieves the interactions of a specific type occuring within the given date range.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param type one of the TYPE_xxx constants.
   *
   */
  Interaction[] getInteractionsByBan(int banId, Date from, Date to, String type) throws TelusAPIException;

  /**
   * Retrieves the interactions occuring within the given date range.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @link aggregation
   *
   */
  Interaction[] getInteractionsBySubscriber(String subscriberId, Date from, Date to) throws TelusAPIException;

  /**
   * Retrieves the interactions of a specific type occuring within the given date range.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param type one of the TYPE_xxx constants.
   *
   */
  Interaction[] getInteractionsBySubscriber(String subscriberId, Date from, Date to, String type) throws TelusAPIException;


  /**
   * Retrieves the knowbility activity code associated to the interaction
   *
   * <P>This method may return <CODE>null</CODE>
   *
   */
  String getActivityCode() throws TelusAPIException;

  /**
   * Retrieves the knowbility activity reason code associated to the interaction
   *
   * <P>This method may return <CODE>null</CODE>
   *
   */
  String getReasonCode() throws TelusAPIException;


  /**
   * Stores the knowbility activity code associated to the interaction
   * for later internal use in the provider.
   */
  void setActivityCode(String actvityCode) throws TelusAPIException;

  /**
   * Stores the knowbility activity reason code associated to the interaction
   * for later internal use in the provider.
   */
  void setReasonCode(String reasonCode) throws TelusAPIException;

}



