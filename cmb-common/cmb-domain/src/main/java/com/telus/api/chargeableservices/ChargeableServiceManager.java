/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.chargeableservices;

import com.telus.api.*;
import com.telus.api.account.*;


public interface ChargeableServiceManager {

  public static final String SERVICE_PREPAID_TOP_UPS       = "PREPAID_TOP_UPS";
  public static final String SERVICE_PREPAID_FEATURE_ADD   = "PREPAID_FEATURE_ADD";
  public static final String SERVICE_PRICEPLAN_CHANGE              = "PRICEPLAN_CHANGE";
  //public static final String SERVICE_PREPAID_ACTIVATIONS = "PREPAID_ACTIVATIONS";
  public static final String SERVICE_HANDSET_EXCHANGE      = "HANDSET_EXCHANGE";
  public static final String SERVICE_MIKE_ACTIVATION_FEE   = "ACTMMK";
  public static final String SERVICE_PCS_ACTIVATION_FEE    = "ACT35";
  public static final String SERVICE_CLIENT_PCS_ACTIVATION_FEE  = "ACTWEB";
  public static final String SERVICE_ONEXRTT_ACTIVATION_FEE     = "ACT4";

  public static final String SERVICE_PHONE_NUMBER_CHANGE_FEE = "PNUM";
  public static final String ECARE_SERVICE_PHONE_NUMBER_CHANGE_FEE = "SPNUM";

  public static final String SERVICE_PAGER_REGULAR_SEARCH_PHONE_NUMBER_CHANGE_FEE = "PRNM";
  public static final String SERVICE_PAGER_PRESTIGE_SEARCH_PHONE_NUMBER_CHANGE_FEE = "PPNM";
  public static final String SERVICE_AUTOTEL_REGULAR_SEARCH_PHONE_NUMBER_CHANGE_FEE = "ARNM";
  public static final String SERVICE_AUTOTEL_PRESTIGE_SEARCH_PHONE_NUMBER_CHANGE_FEE = "APNM";
  
  /**
   * @link aggregation
   *
   * @param serviceName one of the SERVICE_xxx constants
   * @param roleName One of com.telus.api.reference.BusinessRole.BUSINESS_ROLE_xxx constants, except BUSINESS_ROLE_ALL
   * @param subscriber the subscriber the charge will concern.
   *
   *
   */
  //ChargeableService getChargeableService(String serviceName, Subscriber subscriber) throws UnknownObjectException, TelusAPIException;
  ChargeableService getChargeableService(String serviceName, String roleName, Subscriber subscriber) throws UnknownObjectException, TelusAPIException;
}




