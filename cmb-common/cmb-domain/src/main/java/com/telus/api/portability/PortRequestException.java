package com.telus.api.portability;

import com.telus.api.TelusAPIException;
import com.telus.api.message.ApplicationMessage;

public class PortRequestException extends TelusAPIException {

  public PortRequestException(String message) {
    super(message);
  }

  public PortRequestException(Throwable exception, ApplicationMessage applicationMessage) {
    super(exception, applicationMessage, 0);
  }

  public PortRequestException(ApplicationMessage applicationMessage) {
    super(applicationMessage, 0);
  }
}