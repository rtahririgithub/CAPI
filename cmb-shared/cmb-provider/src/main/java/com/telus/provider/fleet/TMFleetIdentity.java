/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.fleet;

import com.telus.api.*;
import com.telus.api.fleet.*;
import com.telus.eas.account.info.*;
import com.telus.provider.*;


public class TMFleetIdentity extends BaseProvider implements FleetIdentity {
  /**
   * @link aggregation
   */
  private final FleetIdentityInfo delegate;
  private TMFleet fleet;


  public TMFleetIdentity(TMProvider provider, FleetIdentityInfo delegate, TMFleet fleet) {
    super(provider);
    this.delegate = delegate;
    this.fleet = fleet;
  }

  public TMFleetIdentity(TMProvider provider, FleetIdentityInfo delegate) {
    super(provider);
    this.delegate = delegate;
  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public void clear() {
    delegate.clear();
    fleet = null;
  }

  public int getUrbanId() {
    return delegate.getUrbanId();
  }

  public int getFleetId() {
    return delegate.getFleetId();
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
  public boolean isValid() {
    return delegate.getUrbanId() != 0 && delegate.getFleetId() != 0;
  }

  public FleetIdentityInfo getDelegate() throws TelusAPIException {
    return delegate;
  }

  public Fleet getFleet() throws TelusAPIException {
    if(fleet == null && isValid()) {
      fleet = (TMFleet)provider.getFleetManager().getFleetById(getUrbanId(), getFleetId());
    }
    return fleet;
  }

  public boolean equals(Object o) {
    return delegate.equals(o);
  }

  public boolean equals(FleetIdentity o) {
    return delegate.equals(o);
  }

}




