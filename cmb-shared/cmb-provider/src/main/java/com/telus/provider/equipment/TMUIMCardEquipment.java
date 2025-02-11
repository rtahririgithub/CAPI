
/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.equipment;

import com.telus.api.equipment.*;
import com.telus.eas.equipment.info.*;
import com.telus.provider.*;



public class TMUIMCardEquipment extends TMCellularEquipment implements UIMCardEquipment{
  /**
   * @link aggregation
   */
  private UIMCardEquipment delegate;


    public TMUIMCardEquipment(TMProvider provider, EquipmentInfo delegate) {
      super(provider, delegate);
      this.delegate = delegate;

    }

}
