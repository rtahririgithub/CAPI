/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.chargeableservices;

import com.telus.api.reference.*;


public interface Waiver extends Reference {
  public static final String WAIVER_FIRST_TIME      = "FIRST_TIME";
  public static final String WAIVER_IVRU_DOWN       = "IVRU_DOWN";
  public static final String WAIVER_888_DOWN        = "888_DOWN";
  public static final String WAIVER_123_DOWN        = "123_DOWN";
  public static final String WAIVER_PIN_NOT_WORKING = "PIN_NOT_WORKING";
  public static final String WAIVER_DUPLICATE_FEE   = "DUPLICATE_FEE";

  // Handset swap waivers
  public static final String WAIVER_CONTRACT_RENEWAL = "CONTRACT_RENEWAL";
  public static final String WAIVER_SECONDARY_EQUIPMENT = "SEC_EQUIPMENT";
  public static final String WAIVER_FOR_DEALER = "FOR_DEALER";
  public static final String WAIVER_TELUS_INITIATIVE = "TELUS_INITIATIVE";
  public static final String WAIVER_FREQUENT = "FREQUENT";

  public static final String WAIVER_TELUS_FIRST_TIME  = "TELUS_FIRST_TIME";
  public static final String WAIVER_TELUS_SECOND_TIME = "TELUS_SECOND_TIME";
  public static final String WAIVER_TELUS_MISAPPLIED  = "TELUS_MISAPPLIED";
  public static final String WAIVER_TELUS_GOLD_CLIENT  = "TELUS_GOLD_CLIENT";
  public static final String WAIVER_TELUS_INITIATED_CAMPAIGN  = "TELUS_INITIATED_CAMPAIGN";
  public static final String WAIVER_TELUS_VAD = "TELUS_VAD";

  public static final String WAIVER_NO_CHARGE       = "NO_CHARGE";

  // Phone number change waivers
  public static final String WAIVER_PHONE_CHANGE_MOVE = "MOVE";
  public static final String WAIVER_PHONE_CHANGE_NUISANCE = "NUIS";
  public static final String WAIVER_PHONE_CHANGE_REACTIVATION = "REACT";
  public static final String WAIVER_PHONE_CHANGE_TRANSFER_OWNER = "TOWN";
  public static final String WAIVER_PHONE_CHANGE_ERROR = "ERROR";
  public static final String WAIVER_PHONE_CHANGE_AREA_SPLIT = "SPLT";
  public static final String WAIVER_PHONE_CHANGE_HOME_AREA = "HSA";
 
}




