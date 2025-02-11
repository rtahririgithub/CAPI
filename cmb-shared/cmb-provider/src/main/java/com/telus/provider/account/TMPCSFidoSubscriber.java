package com.telus.provider.account;

import com.telus.api.account.*;
import com.telus.provider.*;
import com.telus.eas.subscriber.info.*;
import com.telus.api.equipment.Equipment;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

@Deprecated
public class TMPCSFidoSubscriber extends TMPCSSubscriber implements PCSFidoSubscriber {

  private final PCSFidoSubscriberInfo delegate;

  public TMPCSFidoSubscriber(TMProvider provider, PCSFidoSubscriberInfo delegate, boolean activation, String activationFeeChargeCode,
      TMAccountSummary accountSummary, boolean dealerHasDeposit, Equipment equipment) {
    super(provider, delegate, activation, activationFeeChargeCode, accountSummary, dealerHasDeposit, equipment);
    this.delegate = delegate;
  }

  public TMPCSFidoSubscriber(TMProvider provider, PCSFidoSubscriberInfo delegate, boolean activation, String activationFeeChargeCode,
      TMAccountSummary accountSummary, boolean dealerHasDeposit) {
    super(provider, delegate, activation, activationFeeChargeCode, accountSummary, dealerHasDeposit);
    this.delegate = delegate;
  }

  public TMPCSFidoSubscriber(TMProvider provider, PCSFidoSubscriberInfo delegate, boolean activation, String activationFeeChargeCode) {
    super(provider, delegate, activation, activationFeeChargeCode);
    this.delegate = delegate;
  }

  public void setFidoSerialNumber(String serialNumber) {
    delegate.setFidoSerialNumber(serialNumber);
  }

  public void setFidoSimCard(String simCardNumber) {
    delegate.setFidoSimCard(simCardNumber);
  }

  public void setFidoPhoneNumber(String phoneNumber) {
    delegate.setFidoPhoneNumber(phoneNumber);
  }

  public void setFidoAccountId(String accountId) {
    delegate.setFidoAccountId(accountId);
  }

  public void setFidoFirstName(String firstName) {
    delegate.setFidoFirstName(firstName);
  }

  public void setFidoLastName(String lastName) {
    delegate.getConsumerName().setLastName(lastName);
  }

  public void setFidoAccountType(int accountType) {
    delegate.setFidoAccountType(accountType);
  }

  public void setFidoPhoneNumberHoldDays(int phoneNumberHoldDays) {
    delegate.setFidoPhoneNumberHoldDays(phoneNumberHoldDays);
  }

}
