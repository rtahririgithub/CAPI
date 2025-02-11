/*

 * $Id$

 * %E% %W%

 * Copyright (c) Telus Mobility Inc. All Rights Reserved.

 */



package com.telus.provider.account;



import java.util.Date;

import com.telus.api.account.Address;
import com.telus.api.account.AddressHistory;
import com.telus.eas.account.info.AddressHistoryInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

public class TMAddressHistory extends BaseProvider implements AddressHistory {

  private final AddressHistoryInfo delegate;
  private final TMAddress address;


  public TMAddressHistory(TMProvider provider, AddressHistoryInfo delegate) {
    super(provider);
    this.delegate = delegate;
    this.address = new TMAddress(provider, delegate.getAddress0());
  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public Date getEffectiveDate() {
    return delegate.getEffectiveDate();
  }

  public Date getExpirationDate() {
    return delegate.getExpirationDate();
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
  public AddressHistoryInfo getDelegate() {
    return delegate;
  }

  public Address getAddress() {
    return getAddress0();
  }

  public TMAddress getAddress0() {
    return address;
  }

}




