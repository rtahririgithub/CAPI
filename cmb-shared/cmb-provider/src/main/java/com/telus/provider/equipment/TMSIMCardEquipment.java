/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.equipment;

import com.telus.api.equipment.*;
import com.telus.eas.equipment.info.*;
import com.telus.provider.*;
import com.telus.api.TelusAPIException;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.eas.framework.exception.TelusException;


public class TMSIMCardEquipment extends TMIDENEquipment implements SIMCardEquipment {
  /**
   * @link aggregation
   */
  private final EquipmentInfo delegate;

  public TMSIMCardEquipment(TMProvider provider, EquipmentInfo delegate) {
    super(provider, delegate);
    this.delegate = delegate;
  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  /**
   * @deprecated
   * @return
   */
  public String getLastMuleIMEI() {
    // populated when SIMCardEquipment is created
    return delegate.getLastMuleIMEI();
  }

//--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  /**
   * Returns the PUK Code.
   */
  public String getPUKCode() {
    // populated when SIMCardEquipment is created
    return delegate.getPUKCode();
  }

  public MuleEquipment getLastMule() throws TelusAPIException {
    EquipmentInfo lastMule = delegate.getLastMule0();

    if (lastMule == null) {
      try {
    	  lastMule = provider.getProductEquipmentHelper().getMuleBySIM(getSerialNumber());
    	  delegate.setLastMule0(lastMule);
      }catch (Throwable t) {
    	  provider.getExceptionHandler().handleException(t);
      }
    }

    if (lastMule != null) {
      return (new TMMuleEquipment(provider, lastMule));
    }

    return null;
  }

  public void setLastMuleIMEI(String muleNumber) {
    delegate.setLastMuleIMEI(muleNumber);
  }

  public void setLastMule(MuleEquipment lastMule) {
    delegate.setLastMule(((TMMuleEquipment)lastMule).getDelegate0());
    setLastMuleIMEI(lastMule.getSerialNumber());
  }
}




