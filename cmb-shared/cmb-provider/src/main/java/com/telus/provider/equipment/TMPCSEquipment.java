/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.equipment;

import com.telus.api.equipment.*;
import com.telus.api.reference.ProductType;
import com.telus.api.TelusAPIException;
import com.telus.eas.equipment.info.*;
import com.telus.provider.*;

public class TMPCSEquipment extends TMCellularDigitalEquipment implements PCSEquipment {

  private PCSEquipment delegate;

  public TMPCSEquipment(TMProvider provider, EquipmentInfo delegate) {
    super(provider, delegate);
    this.delegate = delegate;
  }
}
