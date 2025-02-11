package com.telus.api.equipment;

import com.telus.api.*;

public interface SIMCardEquipment extends IDENEquipment {

  MuleEquipment getLastMule() throws TelusAPIException;

  void setLastMule(MuleEquipment lastMule);

  String getLastMuleIMEI();

  void setLastMuleIMEI(String muleNumber);

  /**
   * Returns the PUK Code.
   */
  String getPUKCode() throws TelusAPIException;

}
