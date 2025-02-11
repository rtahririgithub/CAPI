/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.equipment;

import com.telus.api.*;
import com.telus.api.account.*;
import com.telus.api.reference.*;
import java.util.*;

/**
 * <CODE>Card</CODE> represents the maximum intersection of all
 * card types. It is the superclass for specific card types.
 *
 * @see FeatureCard
 *
 */
public interface Card {

  public static final String TYPE_FEATURE        = "FEA";
  public static final String TYPE_AIRTIME        = "AIR";
  public static final String TYPE_ACTIVATION     = "ACT";
  public static final String NEW_TYPE_ACTIVATION = "AAC";
  public static final String TYPE_ACTIVATION_ACR = "ACR";

  public static final int STATUS_MISSING       = 107;
  public static final int STATUS_MFG_ERROR     = 108;
  public static final int STATUS_CARD_LOCKED   = 100;
  //public static final int STATUS_ACTIVE        = 8;
  public static final int STATUS_PENDING       = 102;
  public static final int STATUS_LIVE          = 101;
  public static final int STATUS_CREDITED      = 109;
  public static final int STATUS_USED          = 103;
  public static final int STATUS_STOLEN        = 104;
  public static final int STATUS_EXPIRED       = 106;
  public static final int STATUS_DESTROYED     = 105;
  public static final int STATUS_NOT_AVAILABLE = 6;
  public static final int STATUS_NOT_ACTIVE    = 7;

  public static final String PRODUCT_TYPE_CALLERID_VOICEMAIL25_CALL_FORWARDING_BUNDLE = "10004001";
  public static final String PRODUCT_TYPE_UNLIMITED_TEXT_MESSAGING                    = "10004002";
  public static final String PRODUCT_TYPE_AFTER_SCHOOL_CALLING                        = "10004003";
  public static final String PRODUCT_TYPE_100_GAMES                                   = "10004004";
  public static final String PRODUCT_TYPE_UNLIMITED_EVENINGS_AND_WEEKENDS             = "10004005";
  public static final String PRODUCT_TYPE_100_MINUTES                                 = "10004006";


  /**
   *  Returns one of the TYPE_xxx constants.
   */
  String getType();

  /**
   *
   * Returns one of the PRODUCT_TYPE_xxx constants.  The productTypeId is used to identify
   * different subtype of cards (i.e. 100 free minutes feature card, 100 free games feature
   * card, etc).
   *
   */
  String getProductTypeId();

  /**
   * Returns one of the STATUS_xxx constants.
   */
  int  getStatus();

  /**
   * Returns the date the card entered its current status.
   *
   * @see #getStatus
   */
  Date getStatusDate();

  String getSerialNumber();

  String getDescription();

  String getDescriptionFrench();

  /**
   * Returns the first day this card can be activated.
   */
  Date getAvailableFromDate();

  /**
   * Returns the last day this card can be activated.
   */
  Date getAvailableToDate();

  /**
   * Returns the retail value of this card.  This value is used to credit the customer
   * if the card cannot be activated.
   *
   * @see #getAdjustmentCode
   *
   */
  double getAmount();

  /**
   * Returns the phone number on which this card was activated or <CODE>null</CODE>
   */
  String getPhoneNumber();

  /**
   * Returns the banId on which this card was activated or <CODE>0</CODE>
   */
  int getBanId();

  /**
   * Returns the adjustment code to use when crediting an account (in the case the card cannot
   * be activated).
   *
   * @see #getAmount
   *
   */
  String getAdjustmentCode();

  /**
   * Returns this card's services that are appropriate for the given subscriber.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.  It may also be freely modified.
   *
   * <P>This method may involve a remote method call.
   *
   * @exception InvalidServiceException if this card is in appropriate for the subsciber.
   *
   */
  Service[] getServices(Subscriber subscriber) throws InvalidServiceException, TelusAPIException;

  boolean isFeatureCard();

  boolean isMinuteCard();

  boolean isGameCard();

  boolean isAirtimeCard();

  void setCredited(Subscriber subscriber, boolean autoRenew) throws TelusAPIException;

  void setStolen() throws TelusAPIException;
  
  //check card status if it is Live, return true;
  boolean isLive();

}


