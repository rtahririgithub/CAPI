/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api;

import java.util.*;

import com.telus.api.reference.ServiceSummary;


public class InvalidServiceException extends TelusAPIException {

  public static final int UNKNOWN                 = 0;
  public static final int TECHNOLOGY_MISMATCH     = 1;
  public static final int PRICEPLAN_MISMATCH      = 2;
  public static final int PROVINCE_MISMATCH       = 3;
  public static final int BILLING_TYPE_MISMATCH   = 4;
  public static final int NETWORK_MISMATCH        = 5;
  public static final int INVALID_DEVICE          = 6;


  private final int reason;
  private final ServiceSummary conflictService;

  public InvalidServiceException(int reason, String message, Throwable exception, ServiceSummary service) {
		super(message + ": [reason=" + getReasonText(reason) + "]", exception);
		this.reason = reason;
		this.conflictService = service;
	}

  public InvalidServiceException(int reason, String message, Throwable exception) {
	this(reason, message, exception, exception instanceof InvalidServiceException ? 
			                         ((InvalidServiceException) exception).getConflictService() : null);
  }

  public InvalidServiceException(int reason, Throwable exception) {
    this(reason, "", exception);
  }

  public InvalidServiceException(int reason, String message, ServiceSummary service) {
	super(message + ": [reason=" + getReasonText(reason) + "]");
	this.reason = reason;
	this.conflictService = service;
  }
  
  public InvalidServiceException(int reason, String message) {
	 this(reason, message, (ServiceSummary) null);
  }

  public InvalidServiceException(int reason, ServiceSummary service) {
	super("[reason=" + getReasonText(reason) + "]");
	this.reason = reason;
	this.conflictService = service;
  }

  public InvalidServiceException(int reason) {
    this (reason, (ServiceSummary) null);
  }

  public int getReason() {
    return reason;
  }

  public String getReasonText() {
    return getReasonText(reason);
  }

  /**
   * Returns the service that causes the conflict as seen in the message text.  
   * @return ServiceSummary
   */
  public ServiceSummary getConflictService() {
	  return conflictService;
  }

  //---------------------------------------------------------
  // Reason to text translation.
  //---------------------------------------------------------
  protected static final Map reasons = new HashMap();

  static {
    reasons.put(new Integer(TECHNOLOGY_MISMATCH),     "TECHNOLOGY_MISMATCH");
    reasons.put(new Integer(PRICEPLAN_MISMATCH),      "PRICEPLAN_MISMATCH");
    reasons.put(new Integer(PROVINCE_MISMATCH),       "PROVINCE_MISMATCH");
    reasons.put(new Integer(BILLING_TYPE_MISMATCH),   "BILLING_TYPE_MISMATCH");
    reasons.put(new Integer(NETWORK_MISMATCH), 		  "NETWORK_MISMATCH");
    reasons.put(new Integer(INVALID_DEVICE), 		  "INVALID_DEVICE");
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



