/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import com.telus.api.TelusAPIException;
import com.telus.api.account.PagerPostpaidBusinessRegularAccount;
import com.telus.api.account.PagerSubscriber;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.provider.TMProvider;

@Deprecated
public class TMPagerPostpaidBusinessRegularAccount extends TMPostpaidBusinessRegularAccount implements PagerPostpaidBusinessRegularAccount {

  /**
   * @link aggregation
   */
  private final PostpaidBusinessRegularAccountInfo delegate;


  /**
   * @link aggregation
   */
  private final TMPagerAccount account;

  public TMPagerPostpaidBusinessRegularAccount(TMProvider provider, PostpaidBusinessRegularAccountInfo delegate) {
    super(provider, delegate);
    this.delegate = delegate;
    account = new TMPagerAccount(provider, delegate, this);
 }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public PagerSubscriber newPagerSubscriber(String serialNumber) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
//    return account.newPagerSubscriber(serialNumber);
	  throw new UnsupportedOperationException("Decommissioned method");
  }

  public PagerSubscriber newPagerSubscriber(String serialNumber, String activationFeeChargeCode) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
//    return account.newPagerSubscriber(serialNumber, activationFeeChargeCode);
	  throw new UnsupportedOperationException("Decommissioned method");
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




