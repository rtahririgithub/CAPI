/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import com.telus.api.account.*;
import com.telus.provider.*;
import com.telus.eas.account.info.*;

@Deprecated
public class TMPagerPostpaidBoxedConsumerAccount extends TMPagerPostpaidConsumerAccount implements PagerPostpaidBoxedConsumerAccount {

  /**
   * @link aggregation
   */
  private final PostpaidBoxedConsumerAccountInfo delegate;

  public TMPagerPostpaidBoxedConsumerAccount(TMProvider provider, PostpaidBoxedConsumerAccountInfo delegate) {
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

  //--------------------------------------------------------------------
  //  Service Methods
  //--------------------------------------------------------------------

}




