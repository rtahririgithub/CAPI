package com.telus.api.equipment;

import com.telus.api.TelusAPIException;

/**
 * An exception extends TelusAPIException, and is thrown if equipment warranty is not available
 * @version 1.0
 */
public class EquipmentWarrantyNotAvailableException extends TelusAPIException {

  public EquipmentWarrantyNotAvailableException(Throwable t) {
    super(t);
  }

  public EquipmentWarrantyNotAvailableException(String message, Throwable t) {
    super(message, t);
  }

  public EquipmentWarrantyNotAvailableException(String message) {
    super(message);
  }

}
