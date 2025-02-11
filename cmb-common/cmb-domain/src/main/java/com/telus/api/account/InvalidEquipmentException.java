/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;


public class InvalidEquipmentException extends TelusAPIException {

  public InvalidEquipmentException(String message, Throwable exception) {
    super(message, exception);
  }

  public InvalidEquipmentException(Throwable exception) {
    super(exception);
  }

  public InvalidEquipmentException(String message) {
    super(message);
  }

}


