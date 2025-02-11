package com.telus.api.account;

import com.telus.api.TelusAPIException;
import com.telus.api.message.ApplicationMessage;

/**
 * @author Roman Tov
 * @version 1.0, 24-Nov-2006
 */

public class CreditCheckNotRequiredException extends TelusAPIException {
  public CreditCheckNotRequiredException(String message, Throwable exception) {
    super(message, exception);
  }

  public CreditCheckNotRequiredException(Throwable exception) {
    super(exception);
  }

  public CreditCheckNotRequiredException(String message) {
    super(message);
  }

  public CreditCheckNotRequiredException(Throwable exception, ApplicationMessage applicationMessage, int reason) {
    super(exception, applicationMessage, reason);
  }

  public CreditCheckNotRequiredException(ApplicationMessage applicationMessage, int reason) {
    super(applicationMessage, reason);
  }
}
