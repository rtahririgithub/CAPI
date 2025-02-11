/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Date;
import com.telus.api.reference.PrepaidRateProfile;

/**
 * The PrepaidCallHistory is the interface holds the prepaid call history information.
 * 
 * <br><br>
 * <b>TODO</b>: any modification requested should result in deprecating this interface and having Prepaid web services handle the requested changes.  For futher details, 
 * refer to Client API Design.doc design document for Prepaid Real Time Rating project
 * 
 */
public interface PrepaidCallHistory {

  Date getStartDate();
  Date getEndDate();
  String getOrigin_cd();
  int getChargeDuration();
  String getCallingPhoneNumber();
  String getCalledPhoneNumber();
  String getVoiceMailIndicator();
  double getEndBalance();
  double getStartBalance();
  double getChargedAmount();
  int getLocalLongDistanceIndicator();
  int getInternationalDomesticIndicator();
  double getRate();
  String getRateId();
  String getServingSID();
  int getReasonTypeId();
  String getReasonId();
  String getWPSServiceCode();
  double getLongDistanceCallCost();
  double getLocalCallCost();
 
/**
   * This method will return the cost for roaming call 
   * 
   * @return double
   */
  double getRoamingCallCost();
  
  /**
   * This method will return the country code for the call receiver. 
   * For inbound call, this is the country code for customer location.
   * 
   * @return String
   */
  String getCalledMarketCode();
  
  /**
   * This method will return the country code for the call maker. 
   * For outbound call, this is the country code for customer location
   * 
   * @return String
   */
  String getCallingMarketCode();
  
  /**
   * This method will return the switch id for the call. 
   * 
   * @return String
   */
  String getSwitchId();
  
  /**
   * This method will return the route id for the call. 
   * @return String
   */
  String getRouteId();
  
  /**
   * This method will return an array of PrepaidRateProfile associated with the rate Id returned in prepaid call history. 
   * May return local rate, long distance rate, roaming rate and international rate etc. indicated by pre-defined rate type.
   *  
   * @return PrepaidRateProfile[]
   */
  PrepaidRateProfile[] getRates();
  
  public static final String CALL_DIRECTION_INBOUND = "I";
  public static final String CALL_DIRECTION_OUTBOUND = "O";
  public static final int PREPAID_CALL_TYPE_LOCAL = -1;
  public static final int PREPAID_CALL_TYPE_LONG_DISTANCE = -2;
  public static final int PREPAID_CALL_TYPE_ROAMING = -3;
  public static final int PREPAID_CALL_TYPE_LONG_DISTANCE_NATIONAL = -4;
  public static final int PREPAID_CALL_TYPE_LONG_DISTANCE_US = -5;
  public static final int PREPAID_CALL_TYPE_LONG_DISTANCE_CARIBBEAN = -6;
  public static final int PREPAID_CALL_TYPE_INTERNATIONAL_SAPCC = -7;
  public static final int PREPAID_CALL_TYPE_INTERNATIONAL = 1;


  /* Commented the Prepaid 5.1 rel changes
  String[] getDiscountIds();
  double getLongDistanceRate();
  */
}
