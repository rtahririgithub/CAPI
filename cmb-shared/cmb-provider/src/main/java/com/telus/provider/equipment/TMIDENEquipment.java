/*

 * $Id$

 * %E% %W%

 * Copyright (c) Telus Mobility Inc. All Rights Reserved.

 */



package com.telus.provider.equipment;


import com.telus.api.equipment.*;
import com.telus.provider.*;
import com.telus.eas.equipment.info.*;

@Deprecated
public class TMIDENEquipment extends TMEquipment implements IDENEquipment {

  /**

   * @link aggregation

   */

  private IDENEquipment delegate;

  public TMIDENEquipment(TMProvider provider, EquipmentInfo delegate) {

    super(provider, delegate);

    this.delegate = delegate;

  }



  public String getBrowserVersion() {

    return delegate.getBrowserVersion();

  }



  public String getFirmwareVersion() {

    return delegate.getFirmwareVersion();

  }

  public String[] getFirmwareVersionFeatureCodes(){
    return delegate.getFirmwareVersionFeatureCodes();
  }


   public String getBrowserProtocol() {
    return delegate.getBrowserProtocol();

  }

  public boolean isMule() {

    return delegate.isMule();

  }

  public boolean isLegacy(){
    return delegate.isLegacy();
  }
}









