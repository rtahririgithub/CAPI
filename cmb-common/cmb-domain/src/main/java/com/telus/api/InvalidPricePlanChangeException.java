/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api;


public class InvalidPricePlanChangeException extends InvalidServiceException {

  private static final int REASON_OFFSET  = 3000;

  public static final int DUPLICATE_PRICEPLAN      = PRICEPLAN_MISMATCH;
  public static final int UNAVAILABLE_PRICEPLAN    = REASON_OFFSET + 1;
  public static final int GRAND_FATHERED_PRICEPLAN = REASON_OFFSET + 2;
  public static final int SHARED_PLAN_LIMIT         = REASON_OFFSET + 3;
  public static final int SHARED_PLAN_TERM_MISMATCH = REASON_OFFSET + 4;
  /**
   * Error reason code indicating that the change price plan request is made on a 
   * price plan that that is not allowed on the account type/sub-type
   */
  public static final int ACCOUNT_TYPE_SUBTYPE_MISMATCH = REASON_OFFSET + 5;

  public InvalidPricePlanChangeException(int reason, String message, Throwable exception) {
    super(reason, message, exception);
  }

  public InvalidPricePlanChangeException(int reason, Throwable exception) {
    super(reason, exception);
  }

  public InvalidPricePlanChangeException(int reason, String message) {
    super(reason, message);
  }

  public InvalidPricePlanChangeException(int reason) {
    super(reason);
  }


  static {
    reasons.put(new Integer(DUPLICATE_PRICEPLAN), "DUPLICATE_PRICEPLAN");
    reasons.put(new Integer(UNAVAILABLE_PRICEPLAN),  "UNAVAILABLE_PRICEPLAN");
    reasons.put(new Integer(GRAND_FATHERED_PRICEPLAN),  "GRAND_FATHERED_PRICEPLAN");

    reasons.put(new Integer(SHARED_PLAN_LIMIT),  "SHARED_PLAN_LIMIT");
    reasons.put(new Integer(SHARED_PLAN_TERM_MISMATCH),  "SHARED_PLAN_TERM_MISMATCH");
    reasons.put(new Integer(ACCOUNT_TYPE_SUBTYPE_MISMATCH), "ACCOUNT_TYPE_SUBTYPE_MISMATCH");
  }

}



