/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.chargeableservices;

import com.telus.api.*;
import com.telus.api.chargeableservices.*;
import com.telus.eas.config.info.*;
import com.telus.provider.*;
import com.telus.provider.account.*;

public class TMChargeableServiceDescriptor extends BaseProvider implements ChargeableService {
  /**
   * @link aggregation
   */
  protected final ChargeableServiceInfo delegate;


  public TMChargeableServiceDescriptor(TMProvider provider, ChargeableServiceInfo delegate) {
    super(provider);
    this.delegate = delegate;
  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public double getCharge() {
    return delegate.getCharge();
  }

  public Waiver[] getWaivers() {
    return delegate.getWaivers();
  }

  public Waiver getWaiver(String code) {
    return delegate.getWaiver(code);
  }

  public boolean containsWaiver(String code) {
    return delegate.containsWaiver(code);
  }

  public boolean isApplied() {
    return delegate.isApplied();
  }

  public boolean isWaived() {
    return delegate.isWaived();
  }

  public boolean isAppliedOrWaived() {
    return delegate.isAppliedOrWaived();
  }

  public String getDescription() {
    return delegate.getDescription();
  }

  public String getDescriptionFrench() {
    return delegate.getDescriptionFrench();
  }

  public String getCode() {
    return delegate.getCode();
  }

  public void apply() throws TelusAPIException {
    delegate.apply();
  }

  public void waive(Waiver waiver) throws TelusAPIException {
    delegate.waive(waiver);
  }

  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }

  //--------------------------------------------------------------------
  //  Service Methods
  //--------------------------------------------------------------------
  public ChargeableServiceInfo getDelegate(){
    return delegate;
  }

  public WaiverInfo newWaiver(String code, String description, String descriptionFrench){
    WaiverInfo info = new WaiverInfo(code, description, descriptionFrench);
    delegate.addWaiver(info);
    return info;
  }

  public WaiverInfo newWaiver(String code){
    return newWaiver(code, code, code);
  }

  public TMChargeableService newInstance(TMSubscriber subscriber){
    return new TMChargeableService(provider, (ChargeableServiceInfo)delegate.clone(), subscriber);
  }

//  public boolean containsWaiver(String code) {
//    Waiver[] waivers = getWaivers();
//
//    for (int i = 0; i < waivers.length; i++) {
//      if (waivers[i].getCode().equals(code)) {
//        return true;
//      }
//    }
//    return false;
//  }



}



