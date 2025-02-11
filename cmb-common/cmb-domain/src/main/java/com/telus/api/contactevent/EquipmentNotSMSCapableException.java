package com.telus.api.contactevent;

import com.telus.api.TelusAPIException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class EquipmentNotSMSCapableException extends TelusAPIException {

  public EquipmentNotSMSCapableException(String message, Throwable t) {
    super(message, t);
  }

  public EquipmentNotSMSCapableException(Throwable t) {
    super(t);
  }

  public EquipmentNotSMSCapableException(String message) {
    super(message);
  }

}
