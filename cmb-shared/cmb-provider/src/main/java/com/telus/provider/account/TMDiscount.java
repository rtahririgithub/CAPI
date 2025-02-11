/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Discount;
import com.telus.api.reference.DiscountPlan;
import com.telus.api.util.SessionUtil;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;


public class TMDiscount extends BaseProvider implements Discount {
  /**
   * @link aggregation
   */
  private final DiscountInfo delegate;
  
  public TMDiscount(TMProvider provider, DiscountInfo delegate) {
    super(provider);
    this.delegate = delegate;
  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public void setDiscountCode(String discountCode) {
    delegate.setDiscountCode(discountCode);
  }

  public String getDiscountCode() {
    return delegate.getDiscountCode();
  }

  public void setEffectiveDate(Date effectiveDate) {
    delegate.setEffectiveDate(effectiveDate);
  }

  public Date getEffectiveDate() {
    return delegate.getEffectiveDate();
  }

  public void setExpiryDate(Date expiryDate) {
    delegate.setExpiryDate(expiryDate);
  }

  public Date getExpiryDate() {
    return delegate.getExpiryDate();
  }
  
  public String getKnowbilityOperatorID() {
    return delegate.getKnowbilityOperatorID();
  }
  
  public String getDiscountByUserId(){
  	 return delegate.getDiscountByUserId();
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
  public void apply() throws TelusAPIException {
    try{
    	provider.getAccountLifecycleManager().applyDiscountToAccount(delegate, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
    } catch (Throwable e) {
    	provider.getExceptionHandler().handleException(e);
    }
  }

  public void expire() throws TelusAPIException{
    setExpiryDate(provider.getReferenceDataManager0().getLogicalDate());
    apply();
  }

  public DiscountPlan getDiscountPlan() throws TelusAPIException{
    return provider.getReferenceDataManager0().getDiscountPlan(getDiscountCode());
  }

}




