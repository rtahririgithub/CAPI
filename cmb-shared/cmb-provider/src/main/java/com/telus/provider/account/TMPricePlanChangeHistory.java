package com.telus.provider.account;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.PricePlanChangeHistory;
import com.telus.api.reference.PricePlanSummary;
import com.telus.eas.subscriber.info.PricePlanChangeHistoryInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;




public class TMPricePlanChangeHistory extends BaseProvider implements PricePlanChangeHistory {

	private static final long serialVersionUID = 1L;

	private final PricePlanChangeHistoryInfo delegate;
	private PricePlanSummary newPricePlan;
	private PricePlanSummary oldPricePlan;


  public TMPricePlanChangeHistory(TMProvider provider, PricePlanChangeHistoryInfo delegate) {

    super(provider);
    this.delegate = delegate;

  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------

  public Date getDate() {
    return delegate.getDate();
  }

  public String getNewPricePlanCode() {
    return delegate.getNewPricePlanCode();
  }

  public String getOldPricePlanCode() {
    return delegate.getOldPricePlanCode();
  }


  public Date getNewExpirationDate() {
   return delegate.getNewExpirationDate();
  }
 
  
  public String getKnowbilityOperatorID() {
    return delegate.getKnowbilityOperatorID();
  }
  
   
  
  public String getApplicationID() {

    return delegate.getApplicationID();

  }
  
  public String getDealerCode() {

    return delegate.getDealerCode();

  }
  
  public String getSalesRepId() {

    return delegate.getSalesRepId();

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

  public PricePlanSummary getNewPricePlan() throws TelusAPIException {

    if(newPricePlan == null) {

      newPricePlan = provider.getReferenceDataManager().getPricePlan(getNewPricePlanCode());

    }

    return newPricePlan;

  }



  /**
   * @return null
   * @deprecated This method always return null, shall not be used, will be removed in the 
   * future release
   * 
   */  public PricePlanSummary getOldPricePlan() throws TelusAPIException {

    if(oldPricePlan == null && getOldPricePlanCode()!=null) {

      oldPricePlan = provider.getReferenceDataManager().getPricePlan(getOldPricePlanCode());

    }

    return oldPricePlan;

  }



}







