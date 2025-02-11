/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api;

import java.util.HashMap;
import java.util.Map;


/**
 * <CODE>InvalidPasswordException</CODE>
 *
 */

  public class InvalidPasswordException extends TelusAPIException {
  
	public static final int INVALID_PASSWORD = 0;
	public static final int INVALID_PASSWORD_LENGTH = 1;
	public static final int INVALID_PASSWORD_FIRST_LAST_DIGIT = 2;
	public static final int INVALID_PASSWORD_NO_DIGIT = 3;
	public static final int INVALID_PASSWORD_ALREADY_USED = 4;
	public static final int OLD_PASSWORD_INCORRECT = 5;
	public static final int PASSWORD_MISMATCH = 6;
	
	private String password = "";
	private final int reason;

  public InvalidPasswordException(int reason, String message, Throwable exception) {
	super(message + ": [reason=" + getReasonText(reason) + "]", exception);
    this.reason = reason;
  }

  public InvalidPasswordException(int reason, Throwable exception) {
	this(reason, "", exception);
  }

  public InvalidPasswordException(int reason, String message) {
    super(message + ": [reason=" + getReasonText(reason) + "]");
    this.reason = reason;
  }

  public InvalidPasswordException(int reason) {
    super("[reason=" + getReasonText(reason) + "]");
    this.reason = reason;
  }	
	
  public InvalidPasswordException(String message, Throwable exception, String password) {
    super(message + "; password=[" +password+ "]", exception);
    this.password = password;
    this.reason = INVALID_PASSWORD;
  }


  public InvalidPasswordException(Throwable exception, String password) {
    super(exception + "; password=[" +password+ "]");
    this.password = password;
    this.reason = INVALID_PASSWORD;
  }

  public InvalidPasswordException(String message, String password) {
    super(message + "; password=[" +password+ "]");
    this.password = password;
    this.reason = INVALID_PASSWORD;
  }

  public InvalidPasswordException(String password) {
	super("password=[" +password+ "]");
	this.password = password;
	this.reason = INVALID_PASSWORD;
  }

  public String getpassword() {
    return password;
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
    reasons.put(new Integer(INVALID_PASSWORD), "INVALID_PASSWORD");
    reasons.put(new Integer(INVALID_PASSWORD_LENGTH), "INVALID_PASSWORD_LENGTH");
    reasons.put(new Integer(INVALID_PASSWORD_FIRST_LAST_DIGIT), "INVALID_PASSWORD_FIRST_LAST_DIGIT");
    reasons.put(new Integer(INVALID_PASSWORD_NO_DIGIT),   "INVALID_PASSWORD_NO_DIGIT");
    reasons.put(new Integer(INVALID_PASSWORD_ALREADY_USED), "INVALID_PASSWORD_ALREADY_USED");
    reasons.put(new Integer(OLD_PASSWORD_INCORRECT), "OLD_PASSWORD_INCORRECT");
    reasons.put(new Integer(PASSWORD_MISMATCH), "PASSWORD_MISMATCH");
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
