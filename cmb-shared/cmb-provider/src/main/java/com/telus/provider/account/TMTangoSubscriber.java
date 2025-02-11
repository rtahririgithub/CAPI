package com.telus.provider.account;

import java.util.Date;

import com.telus.api.account.TangoSubscriber;
import com.telus.api.equipment.Equipment;
import com.telus.provider.TMProvider;
import com.telus.eas.subscriber.info.TangoSubscriberInfo;

public class TMTangoSubscriber extends TMSubscriber implements TangoSubscriber {
  private final TangoSubscriberInfo delegate;

  public TMTangoSubscriber(TMProvider provider, TangoSubscriberInfo delegate, boolean activation, String activationFeeChargeCode,
                           TMAccountSummary accountSummary, boolean dealerHasDeposit, Equipment equipment) {
    super(provider, delegate, activation, activationFeeChargeCode, accountSummary, dealerHasDeposit, equipment);
    this.delegate = delegate;
  }

  public TMTangoSubscriber(TMProvider provider, TangoSubscriberInfo delegate, boolean activation, String activationFeeChargeCode,
                           TMAccountSummary accountSummary, boolean dealerHasDeposit) {
    super(provider, delegate, activation, activationFeeChargeCode, accountSummary, dealerHasDeposit);
    this.delegate = delegate;
  }

  public TMTangoSubscriber(TMProvider provider, TangoSubscriberInfo delegate, boolean activation, String activationFeeChargeCode) {
    super(provider, delegate, activation, activationFeeChargeCode);
    this.delegate = delegate;
  }

  public void clear() {
    super.clear();
    delegate.clear();
  }

  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }
  
  public String getNextPhoneNumber() {
    return delegate.getNextPhoneNumber();
  }

  public Date getNextPhoneNumberChangeDate() {
    return delegate.getNextPhoneNumberChangeDate();
  }
}
