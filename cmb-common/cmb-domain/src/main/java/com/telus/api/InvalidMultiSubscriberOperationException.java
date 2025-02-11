/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api;

import java.util.*;


public class InvalidMultiSubscriberOperationException extends TelusAPIException {
	private static final int REASON_OFFSET = 2100;
	
	public static final int REASON_INCORRECT_SUBSCRIBER_NUMBER        = REASON_OFFSET + 1;
	public static final int REASON_SUBSCRIBER_STATUS_ILLEGAL        = REASON_OFFSET + 2;
	public static final int REASON_UNABLE_LOCATE_SUBSCRIBER        = REASON_OFFSET + 3;
	public static final int REASON_INVALID_SUBSCRIBER_ARRAY_SIZE        = REASON_OFFSET + 4;
	public static final int REASON_CANNOT_SUSPEND_LAST_ACTIVE        = REASON_OFFSET + 5;
	public static final int REASON_HANDLE_BY_SUBSCRIBER_ACCOUNT        = REASON_OFFSET + 6;
	public static final int REASON_CANNOT_CANCEL_LAST_ACTIVE        = REASON_OFFSET + 7;
	public static final int REASON_INVALID_WAIVE_CODE        = REASON_OFFSET + 8;
	public static final int REASON_INVALID_DEPOSIT_RETURN_METHOD        = REASON_OFFSET + 9;
	public static final int REASON_INVALID_CANCELLATION_REASON        = REASON_OFFSET + 10;
	public static final int REASON_INVALID_SUSPENDING_REASON        = REASON_OFFSET + 11;
	public static final int REASON_INVALID_RESUMING_REASON        = REASON_OFFSET + 12;
	public static final int REASON_INCORRECT_EFFECTIVE_DATE        = REASON_OFFSET + 13;


	private final int reason;


  public InvalidMultiSubscriberOperationException(int reason, String message, Throwable exception) {
    super(message + ": [reason=" + getReasonText(reason) + "]", exception);
    this.reason = reason;
  }

  public InvalidMultiSubscriberOperationException(int reason, Throwable exception) {
    this(reason, "", exception);
  }

  public InvalidMultiSubscriberOperationException(int reason, String message) {
    super(message + ": [reason=" + getReasonText(reason) + "]");
    this.reason = reason;
  }

  public InvalidMultiSubscriberOperationException(int reason) {
    super("[reason=" + getReasonText(reason) + "]");
    this.reason = reason;
  }

  public int getReason() {
    return reason;
  }

  public String getReasonText() {
    return getReasonText(reason);
  }


  //---------------------------------------------------------
  // Reason to text translation.
  //---------------------------------------------------------
  protected static final Map reasons = new HashMap();

  static {
    reasons.put(new Integer(REASON_INCORRECT_SUBSCRIBER_NUMBER),"INCORRECT SUBSCRIBER NUMBER");
    reasons.put(new Integer(REASON_SUBSCRIBER_STATUS_ILLEGAL),"SUBSCRIBER STATUS ILLEGAL");
    reasons.put(new Integer(REASON_UNABLE_LOCATE_SUBSCRIBER),"UNABLE TO LOCATE SUBSCRIBER");
    reasons.put(new Integer(REASON_INVALID_SUBSCRIBER_ARRAY_SIZE),"INVALID SUBSCRIBER ARRAY SIZE");
    reasons.put(new Integer(REASON_CANNOT_SUSPEND_LAST_ACTIVE),"CANNOT SUSPEND LAST ACTIVE");
    reasons.put(new Integer(REASON_HANDLE_BY_SUBSCRIBER_ACCOUNT),"HANDLE BY SUBSCRIBER ACCOUNT");
    reasons.put(new Integer(REASON_CANNOT_CANCEL_LAST_ACTIVE),"CANNOT CANCEL LAST ACTIVE");
    reasons.put(new Integer(REASON_INVALID_WAIVE_CODE),"INVALID WAIVE CODE");
    reasons.put(new Integer(REASON_INVALID_DEPOSIT_RETURN_METHOD),"INVALID DEPOSIT RETURN METHOD");
    reasons.put(new Integer(REASON_INVALID_CANCELLATION_REASON),"INVALID CANCELLATION REASON");
    reasons.put(new Integer(REASON_INVALID_SUSPENDING_REASON),"INVALID SUSPENDING REASON");
    reasons.put(new Integer(REASON_INVALID_RESUMING_REASON),"INVALID RESUMING REASON");
    reasons.put(new Integer(REASON_INCORRECT_EFFECTIVE_DATE),"INCORRECT EFFECTIVE DATE");
  }

  protected static String getReasonText(int reason) {
    Integer i = new Integer(reason);
    String text = (String)reasons.get(i);
    if(text == null) {
      return "UNKNOWN";
    }
    return text;
  }


}
