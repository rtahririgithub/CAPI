package com.telus.api.equipment;

import com.telus.api.message.ApplicationMessage;import com.telus.api.TelusAPIException;

import java.util.Locale;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 22-Dec-2005
 */
@Deprecated
public class ExistingPagerEquipmentException extends TelusAPIException {

  public static final int REASON_SERIAL_NUMBER_EXISTS = 1;
  public static final int REASON_CAP_CODE_EXISTS = 2;

  public ExistingPagerEquipmentException(String message, Throwable exception, ApplicationMessage applicationMessage, int reason) {
    super(message, exception);
    this.applicationMessage = applicationMessage;
    this.reason = reason;
  }

  public ExistingPagerEquipmentException(Throwable exception, ApplicationMessage applicationMessage, int reason) {
    super(exception);
    this.applicationMessage = applicationMessage;
    this.reason = reason;
  }

  public ExistingPagerEquipmentException(String message, ApplicationMessage applicationMessage, int reason) {
    super(message);
    this.applicationMessage = applicationMessage;
    this.reason = reason;
  }

  public ExistingPagerEquipmentException(ApplicationMessage applicationMessage, int reason) {
    super(applicationMessage.getText(Locale.CANADA));
    this.applicationMessage = applicationMessage;
    this.reason = reason;
  }

  public int getReason() {
    return reason;
  }

  public String getReasonText() {
    return getApplicationMessage().getText(Locale.CANADA);
  }
}
