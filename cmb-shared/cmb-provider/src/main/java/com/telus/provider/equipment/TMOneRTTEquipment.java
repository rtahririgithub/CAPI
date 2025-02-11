/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.equipment;

import com.telus.api.equipment.*;
import com.telus.eas.equipment.info.*;
import com.telus.provider.*;


public class TMOneRTTEquipment extends TMCellularDigitalEquipment implements OneRTTEquipment {
  /**
   * @link aggregation
   */
  private OneRTTEquipment delegate;

  public TMOneRTTEquipment(TMProvider provider, EquipmentInfo delegate) {
    super(provider, delegate);
    this.delegate = delegate;
  }
  
 public boolean isDataCard()  {
   return delegate.isDataCard();
 }
 /* public boolean isVoiceCapable()  {
    return delegate.isVoiceCapable();
  }*/
 /* public boolean isDataCapable()  {
    return delegate.isDataCapable();
  } */
}




