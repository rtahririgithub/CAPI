/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.AutoTopUp;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;


public class TMAutoTopUp extends BaseProvider implements AutoTopUp {
  /**
   * @link aggregation
   */
  private final AutoTopUpInfo delegate;
  private boolean existingAutoTopUp;
  private boolean existingThresholdRecharge;
  private AccountLifecycleManager accountLifecycleManager;

  public TMAutoTopUp(TMProvider provider, AutoTopUpInfo delegate, boolean existingAutoTopUp) {
    super(provider);
    this.delegate = delegate;
    this.existingAutoTopUp = existingAutoTopUp;
    this.existingThresholdRecharge = existingAutoTopUp && delegate.hasThresholdRecharge(); 
	accountLifecycleManager = provider.getAccountLifecycleManager();
  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public double getChargeAmount() {
    return delegate.getChargeAmount();
  }

  public void setChargeAmount(double chargeAmount) {
    delegate.setChargeAmount(chargeAmount);
  }

  public Date getNextChargeDate() {
    return delegate.getNextChargeDate();
  }

  public boolean hasThresholdRecharge() {
    return delegate.hasThresholdRecharge();
  }

  public void setHasThresholdRecharge(boolean value) {
    delegate.setHasThresholdRecharge(value);
  }

  public double getThresholdAmount() {
    return delegate.getThresholdAmount();
  }

  public void setThresholdAmount(double value) {
    delegate.setThresholdAmount(value);
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
  public AutoTopUpInfo getDelegate() {
    return delegate;
  }

  public void apply() throws TelusAPIException {
    try {
      String transactionType;

      if (existingAutoTopUp) {
        if (getChargeAmount() == 0) {
          transactionType = AutoTopUpInfo.AUTO_TOPUP_TRANSACTION_TYPE_DELETE;
        }
        else {
          transactionType = AutoTopUpInfo.AUTO_TOPUP_TRANSACTION_TYPE_UPDATE;
        }
      }
      else {
        transactionType = AutoTopUpInfo.AUTO_TOPUP_TRANSACTION_TYPE_INSERT;
      }
      accountLifecycleManager.updateAutoTopUp(delegate, provider.getUser(), existingAutoTopUp, existingThresholdRecharge);
      provider.getInteractionManager0().prepaidAccountTopUp(this, transactionType.charAt(0));

      existingAutoTopUp = true;
      existingThresholdRecharge = hasThresholdRecharge();
    }
    catch(Throwable e) {
      throw new TelusAPIException(e);
    }
  }

}



