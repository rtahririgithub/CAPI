/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.equipment;

import com.telus.api.equipment.*;
import com.telus.eas.equipment.info.*;
import com.telus.provider.*;

@Deprecated
public class TMAnalogEquipment extends TMCellularEquipment implements AnalogEquipment {
  /**
   * @link aggregation
   */
  private AnalogEquipment delegate;

  public TMAnalogEquipment(TMProvider provider, EquipmentInfo delegate) {
    super(provider, delegate);
    this.delegate = delegate;
  }
}




