/*

 * $Id$

 * %E% %W%

 * Copyright (c) Telus Mobility Inc. All Rights Reserved.

 */



package com.telus.provider.account;



import com.telus.api.*;

import com.telus.api.account.*;

import com.telus.api.equipment.*;

import java.util.*;

import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.eas.subscriber.info.*;




public class TMHandsetChangeHistory extends BaseProvider implements HandsetChangeHistory {

  /**

   * @link aggregation

   */

  private final HandsetChangeHistoryInfo delegate;

  private Equipment newEquipment;

  private Equipment oldEquipment;



  public TMHandsetChangeHistory(TMProvider provider, HandsetChangeHistoryInfo delegate) {

    super(provider);

    this.delegate = delegate;

  }



  //--------------------------------------------------------------------

  //  Decorative Methods

  //--------------------------------------------------------------------

  public Date getDate() {

    return delegate.getDate();

  }



  public String getNewSerialNumber() {

    return delegate.getNewSerialNumber();

  }



  public String getOldSerialNumber() {

    return delegate.getOldSerialNumber();

  }


   public String getOldTechnologyType() {

    return delegate.getOldTechnologyType();

  }

 public String  getNewTechnologyType() {

    return delegate.getNewTechnologyType();

  }

  public String getOldProductCode() {

    return delegate.getOldProductCode();

  }

  public String getNewProductCode() {

    return delegate.getNewProductCode();

  }




  //--------------------------------------------------------------------

  //  Service Methods

  //--------------------------------------------------------------------

  public Equipment getNewEquipment() throws TelusAPIException {

    if(newEquipment == null) {

      newEquipment = provider.getEquipmentManager().getEquipment(getNewSerialNumber());

    }

    return newEquipment;

  }



  public Equipment getOldEquipment() throws TelusAPIException {

    if(oldEquipment == null) {

      oldEquipment = provider.getEquipmentManager().getEquipment(getOldSerialNumber());

    }

    return oldEquipment;

  }



}







