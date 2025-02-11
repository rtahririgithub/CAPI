package com.telus.api;

import java.util.*;

public class InvalidChangeMemberIdException extends TelusAPIException {

  private static final int REASON_OFFSET  = 3000;

  public static final int PTN_BASEFLEET      = REASON_OFFSET;


  private final int reason;


  public InvalidChangeMemberIdException(int reason, String message, Throwable exception) {
    super(message, exception);
    this.reason = reason;
  }

  public InvalidChangeMemberIdException(int reason, Throwable exception) {
    super(exception);
    this.reason = reason;
  }

  public InvalidChangeMemberIdException(int reason, String message) {
    super(message);
    this.reason = reason;
  }

  public int getReason() {
    return reason;
  }

  protected static final Map reasons = new HashMap();

  static {
       reasons.put(new Integer(PTN_BASEFLEET), "PTN_BASEFLEET");
  }

}



