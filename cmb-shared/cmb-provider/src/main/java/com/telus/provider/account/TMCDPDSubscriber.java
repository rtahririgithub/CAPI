package com.telus.provider.account;

import com.telus.api.account.CDPDSubscriber;import com.telus.api.equipment.Equipment;import com.telus.provider.TMProvider;import com.telus.eas.subscriber.info.CDPDSubscriberInfo;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 21-Feb-2006
 */
@Deprecated
public class TMCDPDSubscriber extends TMSubscriber implements CDPDSubscriber {
  private final CDPDSubscriberInfo delegate;

  public TMCDPDSubscriber(TMProvider provider, CDPDSubscriberInfo delegate, boolean activation, String activationFeeChargeCode,
                          TMAccountSummary accountSummary, boolean dealerHasDeposit, Equipment equipment) {
    super(provider, delegate, activation, activationFeeChargeCode, accountSummary, dealerHasDeposit, equipment);
    this.delegate = delegate;
  }

  public TMCDPDSubscriber(TMProvider provider, CDPDSubscriberInfo delegate, boolean activation, String activationFeeChargeCode,
                          TMAccountSummary accountSummary, boolean dealerHasDeposit) {
    super(provider, delegate, activation, activationFeeChargeCode, accountSummary, dealerHasDeposit);
    this.delegate = delegate;
  }

  public TMCDPDSubscriber(TMProvider provider, CDPDSubscriberInfo delegate, boolean activation, String activationFeeChargeCode) {
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
}
