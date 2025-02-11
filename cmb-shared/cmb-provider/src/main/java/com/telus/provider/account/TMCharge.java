/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.Date;
import java.util.List;

import com.telus.api.LimitExceededException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Charge;
import com.telus.api.account.Credit;
import com.telus.api.util.SessionUtil;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;


public class TMCharge extends BaseProvider implements Charge {
  /**
   * @link aggregation
   */
  private final ChargeInfo delegate;

   public TMCharge(TMProvider provider, ChargeInfo delegate) {
    super(provider);
    this.delegate = delegate;
   }

  public ChargeInfo getDelegate() {
    return delegate;
  }


  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public void setText(String newText) {
    delegate.setText(newText);
  }

  public String getText() {
    return delegate.getText();
  }

  public void setChargeCode(String chargeCode) {
    delegate.setChargeCode(chargeCode);
  }

  public String getChargeCode() {
    return delegate.getChargeCode();
  }

  public void setEffectiveDate(Date effectiveDate) {
    delegate.setEffectiveDate(effectiveDate);
  }

  public Date getEffectiveDate() {
    return delegate.getEffectiveDate();
  }

  public void setAmount(double amount) {
    delegate.setAmount(amount);
  }

  public double getAmount() {
    return delegate.getAmount();
  }

  public double getId() {
    return delegate.getId();
  }

  public String getFeatureCode() {
    return delegate.getFeatureCode();
  }

  public String getFeatureRevenueCode() {
    return delegate.getFeatureRevenueCode();
  }
  
  public String getServiceCode() {
    return delegate.getServiceCode();
  }  

  public boolean isBalanceImpactFlag() {
    return delegate.isBalanceImpactFlag();
  }

  public Date getCreationDate() {
    return delegate.getCreationDate();
  }

  public String getReasonCode() {
    return delegate.getReasonCode();
  }

  public double getPSTAmount() {
    return delegate.getPSTAmount();
  }

  public double getHSTAmount() {
    return delegate.getHSTAmount();
  }

  public double getGSTAmount() {
    return delegate.getGSTAmount();
  }

  public char getApprovalStatus() {
    return delegate.getApprovalStatus();
  }

  public boolean isBalanceIgnoreFlag() {
    return delegate.isBalanceIgnoreFlag();
  }

  public String getSubscriberId() {
    return delegate.getSubscriberId();
  }

  public int getOperatorId() {
    return delegate.getOperatorId();
  }

  public String getProductType() {
    return delegate.getProductType();
  }

  public boolean isBilled() {
    return delegate.isBilled();
  }

  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }
  
  public Date getPeriodCoverageStartDate(){
	  return delegate.getPeriodCoverageStartDate();
  }
  
  public void setPeriodCoverageStartDate(Date startDate) {
	  delegate.setPeriodCoverageStartDate(startDate);
  }
  
  public Date getPeriodCoverageEndDate() {
	  return delegate.getPeriodCoverageEndDate();
  }
  
  public void setPeriodCoverageEndDate(Date endDate) {
	  delegate.setPeriodCoverageEndDate(endDate);
  }
  
  public double getRoamingTaxAmount() {
	  return delegate.getRoamingTaxAmount();
}

  public boolean isGSTExempt() {
	  return delegate.isGSTExempt();
  }

  public boolean isHSTExempt() {
	  return delegate.isHSTExempt();
  }

  public boolean isPSTExempt() {
	  return delegate.isPSTExempt();
  }

  public boolean isRoamingTaxExempt() {
	  return delegate.isRoamingTaxExempt();
  }

//--------------------------------------------------------------------
  //  Service Methods
  //--------------------------------------------------------------------
  public double apply() throws TelusAPIException {
    return apply(true);
  }

  public double apply(boolean overrideUserLimit) throws LimitExceededException, TelusAPIException {
	  double chargeSeqNo = 0; 
	  try{
		  if(overrideUserLimit) {
			  chargeSeqNo = provider.getAccountLifecycleManager().applyChargeToAccountWithOverride(delegate, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
		  } else {
			  chargeSeqNo = provider.getAccountLifecycleManager().applyChargeToAccount(delegate, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
		  }

		  //populate this sequence number, so that same Charge instance can be immediately adjusted.
		  delegate.setId(chargeSeqNo);
	  } catch (Throwable e) {
    	provider.getExceptionHandler().handleException(e);
    }
    return chargeSeqNo;
  }

  public double adjust(double adjustmentAmount, String adjustmentReasonCode, String memoText) throws TelusAPIException {
    return adjust(adjustmentAmount, adjustmentReasonCode, memoText, true);
  }

  public double adjust(double adjustmentAmount, String adjustmentReasonCode, String memoText, boolean overrideUserLimit) throws LimitExceededException, TelusAPIException {
	 double adjustmentId = 0;
	 try {
		adjustmentId = provider.getAccountLifecycleFacade().adjustCharge(delegate, adjustmentAmount, adjustmentReasonCode, memoText, overrideUserLimit, 
			 provider.getAccountNotificationSuppressionIndicator(delegate.getBan()),null,
			 SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));
		 
    } catch (Throwable e) {
    	provider.getExceptionHandler().handleException(e);
    }
    return adjustmentId;
  }

  public void delete(String deletionReasonCode, String memoText) throws TelusAPIException {
    delete(deletionReasonCode, memoText, true);
  }

  public void delete(String deletionReasonCode, String memoText, boolean overrideUserLimit) throws LimitExceededException, TelusAPIException {
    try{
    	if(overrideUserLimit) {
    		provider.getAccountLifecycleManager().deleteChargeWithOverride(delegate, deletionReasonCode, memoText, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
    	} else {
    		provider.getAccountLifecycleManager().deleteCharge(delegate, deletionReasonCode, memoText, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
    	}
    } catch (Throwable e) {
    	provider.getExceptionHandler().handleException(e);
    }
  }
  
  public Credit[] getRelatedCredits() throws TelusAPIException {
	Credit[] credits=null;
	try {
		List list= provider.getAccountInformationHelper().retrieveRelatedCreditsForCharge(delegate.getBan(), getId());
		credits=decorate((CreditInfo[])list.toArray(new CreditInfo[list.size()]));
	} catch (Throwable e) {
    	provider.getExceptionHandler().handleException(e);
    }
    return credits;
  }
  
  private Credit[] decorate(CreditInfo[] credits) throws TelusAPIException {
    TMCredit[] tmCredits = new TMCredit[credits.length];
    for(int i=0; i<credits.length; i++) {
      TMCredit tmCredit = new TMCredit(provider, (CreditInfo)credits[i], null);
      tmCredits[i] = tmCredit;
    }
    return tmCredits;
  }

  
  public int getBan() {
	  throw new UnsupportedOperationException("Method not implemented here");
  }

  public int getBillSequenceNo() {
	  throw new UnsupportedOperationException("Method not implemented here");
  }

}




