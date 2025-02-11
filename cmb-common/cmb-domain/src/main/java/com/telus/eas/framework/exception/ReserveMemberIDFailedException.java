package com.telus.eas.framework.exception;
/**
 * Title:        Telus - Amdocs Domain Beans
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Telus Mobility
 * @author Michael Lapish
 * @version 1.0
 */

import com.telus.eas.framework.info.*;

public class ReserveMemberIDFailedException extends TelusApplicationException {

  public static final char RESOURCE_PAIRING_FAILED = 'P';
  public static final char RESOURCE_IN_USE         = 'U';
  public static final char URBAN_FLEET_IN_USE      = 'F';
  public static final char SYSTEM_ERROR            = 'S';

  private char reason;
  private String phoneNumber;
  private String subscriberId;

  public ReserveMemberIDFailedException() {
    super();
  }
  public ReserveMemberIDFailedException(String id, String message) {
    super(id,message);
  }
  public ReserveMemberIDFailedException(String id, String message, Throwable exception) {
    super(id,message,exception);
  }
  public ReserveMemberIDFailedException(ExceptionInfo pExceptionInfo, char reason){
    super(pExceptionInfo);
    setReason(reason);
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  public String getSubscriberId() {
    return subscriberId;
  }
  public void setSubscriberId(String subscriberId) {
    this.subscriberId = subscriberId;
  }
  public char getReason() {
    return reason;
  }
  public void setReason(char reason) {
    this.reason = reason;
  }
}