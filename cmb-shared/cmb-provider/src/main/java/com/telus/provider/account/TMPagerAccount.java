/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import com.telus.api.TelusAPIException;
import com.telus.api.account.InvalidSerialNumberException;
import com.telus.api.account.PagerAccount;
import com.telus.api.account.PagerSubscriber;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

@Deprecated
public class TMPagerAccount extends BaseProvider implements PagerAccount {
  /**
   *
   * @link aggregation
   *
   */
  private final PagerAccount delegate;

  /**
   *
   * @link aggregation
   *
   */
  private final TMAccount account;

  public TMPagerAccount(TMProvider provider, PagerAccount delegate, TMAccount account) {
    super(provider);
    this.delegate = delegate;
    this.account = account;
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
  public PagerSubscriber newPagerSubscriber(String serialNumber) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
//   return account.newPagerSubscriber(AccountManager.PRODUCT_TYPE_PAGER, serialNumber, false, null);
	  throw new UnsupportedOperationException("Decommissioned method");
  }

  public PagerSubscriber newPagerSubscriber(String serialNumber, String activationFeeChargeCode) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
//    return account.newPagerSubscriber(AccountManager.PRODUCT_TYPE_PAGER, serialNumber, false, activationFeeChargeCode);
	  throw new UnsupportedOperationException("Decommissioned method");
  }


  public PricePlanSubscriberCount[] getPricePlanSubscriberCount() throws TelusAPIException{
    // This method has been implemented in TMAccount to allow TMSubscriber
    // easier access to it.
    throw new UnsupportedOperationException("Method not implemented here");
  }


}




