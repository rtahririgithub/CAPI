package com.telus.api.equipment;

import com.telus.api.TelusAPIException;import com.telus.api.message.ApplicationMessage;

import java.util.Locale;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 22-Dec-2005
 */
@Deprecated
public class InvalidPagerEquipmentException extends TelusAPIException {
  public static final int REASON_INVALID_CAP_CODE = 1;
  public static final int REASON_INVALID_EQUIPMENT_TYPE = 2;
  public static final int REASON_INVALID_COVERAGE_REGION = 3;

  public InvalidPagerEquipmentException(String message, Throwable exception, ApplicationMessage applicationMessage, int reason) {
    super(message, exception);
    this.applicationMessage = applicationMessage;
    this.reason = reason;
  }

  public InvalidPagerEquipmentException(Throwable exception, ApplicationMessage applicationMessage, int reason) {
    super(exception);
    this.applicationMessage = applicationMessage;
    this.reason = reason;
  }

  public InvalidPagerEquipmentException(String message, ApplicationMessage applicationMessage, int reason) {
    super(message);
    this.applicationMessage = applicationMessage;
    this.reason = reason;
  }

  public InvalidPagerEquipmentException(ApplicationMessage applicationMessage, int reason) {
    super(applicationMessage.getText(Locale.CANADA));
    this.applicationMessage = applicationMessage;
    this.reason = reason;
  }

  public ApplicationMessage getApplicationMessage() {
    return applicationMessage;
  }

  public int getReason() {
    return reason;
  }

  public String getReasonText() {
    return getApplicationMessage().getText(Locale.CANADA);
  }
}
