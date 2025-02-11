package com.telus.api.account;

import com.telus.api.TelusAPIException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TELUS Mobility</p>
 * @author Michael Qin
 * @version 1.0
 */

public class VoiceUsageSummaryException extends TelusAPIException {

  public static final int UNKNOWN = 0;
  public static final int HOME_PROVINCE_NOT_ALLOWED = 1;
  public static final int NOT_AVAILABLE = 2;

  private int reason;

  public VoiceUsageSummaryException(String message, int reason) {
    super(message);
    this.reason = reason;
  }

  public VoiceUsageSummaryException(Throwable exception, int reason) {
    super(exception);
    this.reason = reason;
  }

  public VoiceUsageSummaryException(String message, Throwable exception, int reason) {
    super(message, exception);
    this.reason = reason;
  }

  public VoiceUsageSummaryException(String message, Throwable exception) {
    super(message, exception);
  }

  public VoiceUsageSummaryException(Throwable exception) {
    super(exception);
  }

  public VoiceUsageSummaryException(String message) {
    super(message);
  }

  public VoiceUsageSummaryException(int reason) {
    super("");
    this.reason = reason;
  }

  public int getReason() {
    return reason;
  }

  public String getReasonText() {
    switch (reason) {
      case UNKNOWN:
        return "Unknown.";
      case HOME_PROVINCE_NOT_ALLOWED:
        return "Airtime summary is not allowed to be viewed for this province.";
      case NOT_AVAILABLE:
        return "Airtime summary is not available.";
      default:
        return "Unknown.";
    }
  }
}