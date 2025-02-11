/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import com.telus.api.*;
import com.telus.api.account.*;
import com.telus.api.fleet.*;
import com.telus.provider.*;
import com.telus.eas.account.info.*;

@Deprecated
public class TMIDENPostpaidConsumerAccount extends TMPostpaidConsumerAccount implements IDENPostpaidConsumerAccount {

  /**
   * @link aggregation
   */
  private final PostpaidConsumerAccountInfo delegate;


  /**
   * @link aggregation
   */
  private final TMIDENAccount account;

  public TMIDENPostpaidConsumerAccount(TMProvider provider, PostpaidConsumerAccountInfo delegate) {
    super(provider, delegate);
    this.delegate = delegate;
    account = new TMIDENAccount(provider, delegate, this);
  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public Address getAlternateCreditCheckAddress() {
    return account.getAlternateCreditCheckAddress();
  }

  public IDENSubscriber newIDENSubscriber(String serialNumber, boolean dealerHasDeposit, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return account.newIDENSubscriber(serialNumber, dealerHasDeposit, voiceMailLanguage);
  }

  public IDENSubscriber newIDENSubscriber(String serialNumber, String muleNumber, boolean dealerHasDeposit, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return account.newIDENSubscriber(serialNumber, muleNumber, dealerHasDeposit, voiceMailLanguage);
  }

  public IDENSubscriber newIDENSubscriber(String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return account.newIDENSubscriber(serialNumber, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage);
  }

  public IDENSubscriber newIDENSubscriber(String serialNumber, String muleNumber, boolean dealerHasDeposit, String activationFeeChargeCode,String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return account.newIDENSubscriber(serialNumber, muleNumber, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage);
  }
  public Fleet newFleet(int networkId, String name, int numberOfSubscriber) throws InvalidNetworkException, UnknownBANException, DuplicateObjectException, TelusAPIException {
    return account.newFleet(networkId, name, numberOfSubscriber);
  }

  public void addFleets(Fleet[] fleet) throws UnknownBANException, InvalidFleetException, TelusAPIException {
    account.addFleets(fleet);
  }

  public void addFleet(Fleet fleet) throws UnknownBANException, InvalidFleetException, TelusAPIException {
    account.addFleet(fleet);
  }

  public void addTalkGroups(TalkGroup[] talkGroup) throws UnknownBANException, InvalidFleetException, TelusAPIException {
    account.addTalkGroups(talkGroup);
  }

  public void addTalkGroup(TalkGroup talkGroup) throws UnknownBANException, InvalidFleetException, TelusAPIException {
    account.addTalkGroup(talkGroup);
  }

  public void removeTalkGroup(TalkGroup talkGroup) throws TelusAPIException {
    account.removeTalkGroup(talkGroup);
  }

  public Fleet[] getFleets() throws UnknownBANException, TelusAPIException {
    return account.getFleets();
  }

  public TalkGroup[] getTalkGroups() throws UnknownBANException, TelusAPIException {
    return account.getTalkGroups();
  }

  public TalkGroup[] getTalkGroups(int urbanId, int fleetId) throws UnknownBANException, InvalidFleetException, TelusAPIException {
    return account.getTalkGroups(urbanId, fleetId);
  }

  public IDENSubscriber[] getSubscribers(int urbanId, int fleetId, int maximum) throws UnknownBANException, InvalidFleetException, TelusAPIException {
    return account.getSubscribers(urbanId, fleetId, maximum);
  }

  public boolean contains(Fleet fleet) throws UnknownBANException, TelusAPIException {
    return account.contains(fleet);
  }

  public boolean contains(TalkGroup talkGroup) throws UnknownBANException, TelusAPIException {
    return account.contains(talkGroup);
  }
  
  public void removeFleet(Fleet fleet) throws InvalidFleetException, TelusAPIException {
    account.removeFleet(fleet);
  }

  public void refreshTalkGroups() throws TelusAPIException {
    account.refreshTalkGroups();
  }

public void refreshFleets() throws TelusAPIException {
    account.refreshFleets();
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

  public void refresh() throws TelusAPIException {
  	super.refresh();
    account.refresh();
  }
  
}



