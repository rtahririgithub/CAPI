package com.telus.api.account;

/**
 * Title:        Telus ECA
 * Description:  Enterprise Development
 * Copyright:    Copyright (c) 2002
 * Company:      TELUS Mobility
 * @author Michael Qin
 * @version 1.0
 */

public class InvalidWarrantyTransferException extends InvalidEquipmentChangeException {

  public InvalidWarrantyTransferException(String message, Throwable exception, int reason) {
    super(message, exception, reason);
  }
}