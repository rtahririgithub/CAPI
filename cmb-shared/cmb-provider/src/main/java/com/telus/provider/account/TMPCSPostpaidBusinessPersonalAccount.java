/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import com.telus.api.account.*;
import com.telus.provider.*;
import com.telus.eas.account.info.*;


public class TMPCSPostpaidBusinessPersonalAccount extends TMPCSPostpaidConsumerAccount implements PCSPostpaidBusinessPersonalAccount {

  /**
   * @link aggregation
   */
  private final PostpaidBusinessPersonalAccountInfo delegate;

  public TMPCSPostpaidBusinessPersonalAccount(TMProvider provider, PostpaidBusinessPersonalAccountInfo delegate) {
    super(provider, delegate);
    this.delegate = delegate;
  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public String getLegalBusinessName() {
    return delegate.getLegalBusinessName();
  }

  public void setLegalBusinessName(String legalBusinessName) {
    delegate.setLegalBusinessName(legalBusinessName);
  }

  public String getOperatingAs() {
    return delegate.getOperatingAs();
  }

  public void setOperatingAs(String operatingAs) {
    delegate.setOperatingAs(operatingAs);
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

}




