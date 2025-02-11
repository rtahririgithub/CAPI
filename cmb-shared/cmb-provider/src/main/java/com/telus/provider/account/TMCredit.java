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
import com.telus.api.account.AccountSummary;
import com.telus.api.account.Charge;
import com.telus.api.account.Credit;
import com.telus.api.account.TaxSummary;
import com.telus.api.reference.AdjustmentReason;
import com.telus.api.util.SessionUtil;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.util.Logger;


public class TMCredit extends BaseProvider implements Credit {
  /**
   * @link aggregation
   */
  private final CreditInfo delegate;
  //private final TaxationPolicy taxationPolicy;
  private final AccountSummary account;
  private static final String OOM = "OFFERM";
 

  //public TMCredit(TMProvider provider, CreditInfo delegate, TaxationPolicy taxationPolicy, AccountSummary account) {
  public TMCredit(TMProvider provider, CreditInfo delegate, AccountSummary account) {
    super(provider);
    this.delegate = delegate;
    //this.taxationPolicy = taxationPolicy;
    this.account = account;

    // OOM: allowed to bypass authorization for recurring credits
    // CR 03/01/2005 from OOM team
    if (OOM.equalsIgnoreCase(provider.getApplication()))
      this.delegate.setBypassAuthorization(true);
    }

  public CreditInfo getDelegate() {
    return delegate;
  }


  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public String getSubscriberId() {
    return delegate.getSubscriberId();
  }

  public String getText() {
    return delegate.getText();
  }

  public void setText(String newText) {
    delegate.setText(newText);
  }

  public void setReasonCode(String reasonCode) {
    delegate.setReasonCode(reasonCode);
  }

  public String getReasonCode() {
    return delegate.getReasonCode();
  }

  public void setEffectiveDate(Date effectiveDate) {
    delegate.setEffectiveDate(effectiveDate);
  }

  public Date getEffectiveDate() {
    return delegate.getEffectiveDate();
  }

  public void setBalanceImpactFlag(boolean balanceImpactFlag) {
    delegate.setBalanceImpactFlag(balanceImpactFlag);
  }

  public boolean isBalanceImpactFlag() {
    return delegate.isBalanceImpactFlag();
  }

  public double getAmount() {
    return delegate.getAmount();
  }

  public boolean isTaxable(){
    return delegate.isTaxable();
  }

  public double getPSTAmount(){
    return delegate.getPSTAmount();
  }

  public double getGSTAmount(){
    return delegate.getGSTAmount();
  }

  public double getHSTAmount(){
    return delegate.getHSTAmount();
  }

  public double getTotalAmount(){
    return delegate.getTotalAmount();
  }

  public int getId(){
    return delegate.getId();
  }

  public Date getCreationDate(){
    return delegate.getCreationDate();
  }

  public String getActivityCode(){
    return delegate.getActivityCode();
  }

  public char getApprovalStatus(){
    return delegate.getApprovalStatus();
  }

  public boolean isBalanceIgnoreFlag(){
    return delegate.isBalanceIgnoreFlag();
  }

  public int getOperatorId(){
    return delegate.getOperatorId();
  }

  public String getProductType(){
    return delegate.getProductType();
  }

  public String getSOC(){
    return delegate.getSOC();
  }

  public String getFeatureCode(){
    return delegate.getFeatureCode();
  }

  public boolean isBilled(){
    return delegate.isBilled();
  }

  public double getRelatedChargeId(){
    return delegate.getRelatedChargeId();
  }

  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }

  public char getTaxOption() {
    return delegate.getTaxOption();
  }
  
  //--------------------------------------------------------------------
  //  Service Methods
  //--------------------------------------------------------------------
  public void calculateTaxes() throws TelusAPIException {
    TaxSummary taxSummary = provider.getAccountManager().calculateTax(getAmount(), account);
    delegate.getTaxSummary().copyFrom(taxSummary);
  }

  public void apply() throws TelusAPIException {
    apply(true);
  }

  public void apply(boolean overrideUserLimit) throws LimitExceededException, TelusAPIException {
    try{
      
      if (account.isPostpaid())
      {
      	AdjustmentReason aReason = getAdjustmentReason();
      	if (aReason != null)
      		setCreditRecurring(aReason.isRecurring());
      	else
      	{
      	setCreditRecurring(false);	
      	Logger.debug("=>  Adjustment reason is not found for the reason code: " + this.getReasonCode());	
      	}
      }
      else
      	setCreditRecurring(false);

      provider.getAccountLifecycleFacade().applyCreditToAccount(delegate,  overrideUserLimit, 
    		  provider.getAccountNotificationSuppressionIndicator(delegate.getBan()), null, 
    		  SessionUtil.getSessionId(provider.getAccountLifecycleFacade()) );
      
    } catch (Throwable e) {
    	provider.getExceptionHandler().handleException(e);
    }
  }

  public void reverse(String reversalReasonCode, String memoText) throws TelusAPIException {
    reverse(reversalReasonCode, memoText, true);
  }

  public void reverse(String reversalReasonCode, String memoText, boolean overrideUserLimit) throws LimitExceededException, TelusAPIException {
    try{

      setCreditRecurring(getAdjustmentReason().isRecurring());

      if(overrideUserLimit) {
    	  provider.getAccountLifecycleManager().reverseCreditWithOverride(delegate, reversalReasonCode, memoText, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
      } else {
    	  provider.getAccountLifecycleManager().reverseCredit(delegate, reversalReasonCode, memoText, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
      }
    } catch (Throwable e) {
    	provider.getExceptionHandler().handleException(e);
    }
  }

  public void setAmount(double amount) throws TelusAPIException {
    delegate.setAmount(amount);
    calculateTaxes();
  }

  /**
   * Return AdjustmentReason, a reference object, associated with the Credit.
   *
   * @return AdjustmentReason
   */
  public AdjustmentReason getAdjustmentReason() {
    AdjustmentReason ar = null;
    try {
      if (this.getReasonCode() != null)
        ar = provider.getReferenceDataManager().getAdjustmentReason(this.getReasonCode());
      else
    	  Logger.debug("=>  ReasonCode is not set yet on Credit, can't retrieve the AdjustmentReason object");
    } catch (TelusAPIException e) {
    	Logger.debug(e);
    }

    return ar;
  }

  /**
   * For an open recurring credit with predefined frequency, the
  * calling program defines the number of recurring credits to be applied.
  *
   * @param numberOfRecurring int
   */
  public void setNumberOfRecurring(int numberOfRecurring) {
    delegate.setNumberOfRecurring(numberOfRecurring);
  }

  /**
   * Set to <code>true</code> if reverse for all recurring credits desired.
   * @param reverseAllRecurring boolean
   */
  public void setReverseAllRecurring(boolean reverseAllRecurring) {
    delegate.setReverseAllRecurring(reverseAllRecurring);
  }

  /**
   * Set to <code>true</code> if Adjusment Reason is recurring
   * @param  boolean
   */
  public void setCreditRecurring(boolean isRecurring ) {
    delegate.setRecurring(isRecurring);
  }

	public double getRoamingTaxAmount() {
		return delegate.getRoamingTaxAmount();
	}

	public Charge[] getRelatedCharges() throws TelusAPIException{
		Charge[] charge=null;
		try {
			List list= provider.getAccountInformationHelper().retrieveRelatedChargesForCredit(delegate.getBan(),delegate.getRelatedChargeId());
			charge= decorate((ChargeInfo[])list.toArray(new ChargeInfo[list.size()]));
		} catch (Throwable e) {
	    	provider.getExceptionHandler().handleException(e);
	    }
	    return charge;
	}
	
	private Charge[] decorate(ChargeInfo[] charges) throws TelusAPIException {
	    TMCharge[] tmCharges = new TMCharge[charges.length];
	    for(int i=0; i<charges.length; i++) {
	      TMCharge tmCharge = new TMCharge(provider, (ChargeInfo)charges[i]);
	      tmCharges[i] = tmCharge;
	    }
	    return tmCharges;
	}

	
}




