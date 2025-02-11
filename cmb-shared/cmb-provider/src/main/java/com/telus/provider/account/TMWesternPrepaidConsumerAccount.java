/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import com.telus.provider.*;
import com.telus.api.account.*;
import com.telus.api.equipment.*;
import com.telus.eas.account.info.*;


public class TMWesternPrepaidConsumerAccount extends TMPrepaidConsumerAccount implements WesternPrepaidConsumerAccount {
  /**
   * @link aggregation
   */
  private final WesternPrepaidConsumerAccountInfo delegate;

  public TMWesternPrepaidConsumerAccount(TMProvider provider, WesternPrepaidConsumerAccountInfo delegate, Equipment equipment) {
    super(provider, delegate, equipment);
    this.delegate = delegate;
  }

  public TMWesternPrepaidConsumerAccount(TMProvider provider, WesternPrepaidConsumerAccountInfo delegate) {
    super(provider, delegate);
    this.delegate = delegate;
  }


  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }

}




