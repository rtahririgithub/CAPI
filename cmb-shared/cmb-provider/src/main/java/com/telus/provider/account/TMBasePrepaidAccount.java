/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import com.telus.provider.*;
import com.telus.api.*;
import com.telus.api.account.*;
import com.telus.api.equipment.*;
import com.telus.eas.account.info.*;
import com.telus.eas.subscriber.info.*;
import java.util.*;


public class TMBasePrepaidAccount extends TMAccount implements BasePrepaidAccount {
  /**
   * @link aggregation
   */
  private final BasePrepaidAccountInfo delegate;
  private Equipment equipment;
  private TMSubscriber subscriber;

  public TMBasePrepaidAccount(TMProvider provider, BasePrepaidAccountInfo delegate, Equipment equipment) {
    super(provider, delegate);
    this.delegate = delegate;
    this.equipment = equipment;
  }

  public TMBasePrepaidAccount(TMProvider provider, BasePrepaidAccountInfo delegate) {
    super(provider, delegate);
    this.delegate = delegate;
  }


  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public ConsumerName getName() {
    return delegate.getName();
  }

  public boolean getBlockOutgoing976Numbers() {
    return delegate.getBlockOutgoing976Numbers();
  }

  public void setBlockOutgoing976Numbers(boolean blockOutgoing976Numbers) {
    delegate.setBlockOutgoing976Numbers(blockOutgoing976Numbers);
  }

  public Date getBirthDate() {
    return delegate.getBirthDate();
  }

  public void setBirthDate(Date birthDate) {
    delegate.setBirthDate(birthDate);
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
  public PCSSubscriber newPCSSubscriber() throws TelusAPIException {
    return newPCSSubscriber(null);
  }

  public PCSSubscriber newPCSSubscriber(String activationFeeChargeCode) throws TelusAPIException {
    return newSubscriber(AccountManager.PRODUCT_TYPE_PCS, delegate.getSerialNumber(), false, activationFeeChargeCode, null);
  }

  protected TMPCSSubscriber newSubscriber(String productType, String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode, String voiceMailLanguage) throws UnknownSerialNumberException,
  SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {

    assertAccountExists();

    //-------------------------------------------------------------
    // SerialNumbers are validated at the account level, but stored
    // at the subscriber level.  This means a prepaid account
    // retrieval will not have a SerialNumber.
    //-------------------------------------------------------------
    if(delegate.getSerialNumber() == null || !serialNumber.equals(delegate.getSerialNumber()) || equipment == null) {
      delegate.setSerialNumber(serialNumber);
      equipment = provider.getEquipmentManager().validateSerialNumber(serialNumber);
    }

    /*  -- this piece of logic is not compatible with error recovery.
    if(delegate.getSerialNumber() == null) {
      delegate.setSerialNumber(serialNumber);
      equipment = equipmentManagerBean.validateSerialNumber(serialNumber);
    }

    if (!serialNumber.equals(delegate.getSerialNumber())) {
      throw new TelusAPIException("the subscriber's serial number (" + serialNumber +
          ") differs from that validated on the account (" + delegate.getSerialNumber() + ")");
    }
    */

   PCSSubscriberInfo info = new PCSSubscriberInfo();
   info.setProductType(productType);
   info.setSerialNumber(serialNumber);
   info.setBanId(getBanId());
   info.setEquipmentType(equipment.getEquipmentType());

   info.setDealerCode(getDealerCode());
   info.setSalesRepId(getSalesRepCode());
   info.setLanguage(getLanguage());
   info.setVoiceMailLanguage(voiceMailLanguage);

   delegate.copyTo(info);

   subscriber = new TMPCSSubscriber(provider, info, true, activationFeeChargeCode, this, dealerHasDeposit, equipment);
   return (TMPCSSubscriber)subscriber;
  }

  protected TMPCSFidoSubscriber newFidoSubscriber(String productType, String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode) throws UnknownSerialNumberException,
      SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {

    assertAccountExists();

    //-------------------------------------------------------------
    // SerialNumbers are validated at the account level, but stored
    // at the subscriber level.  This means a prepaid account
    // retrieval will not have a SerialNumber.
    //-------------------------------------------------------------
    if(delegate.getSerialNumber() == null || !serialNumber.equals(delegate.getSerialNumber()) || equipment == null) {
      delegate.setSerialNumber(serialNumber);
      equipment = provider.getEquipmentManager().validateSerialNumber(serialNumber);
    }

   PCSFidoSubscriberInfo info = new PCSFidoSubscriberInfo();
   info.setProductType(productType);
   info.setSerialNumber(serialNumber);
   info.setBanId(getBanId());
   info.setEquipmentType(equipment.getEquipmentType());
   info.setDealerCode(getDealerCode());
   info.setSalesRepId(getSalesRepCode());
   info.setLanguage(getLanguage());
   info.setFidoConversion(true);

   delegate.copyTo(info);

   subscriber = new TMPCSFidoSubscriber(provider, info, true, activationFeeChargeCode, this, dealerHasDeposit, equipment);
   return (TMPCSFidoSubscriber)subscriber;

  }

  public void refresh() throws TelusAPIException {
    String serialNumber   = delegate.getSerialNumber();
   // int    activationType = delegate.getActivationType();
   // String activationCode = delegate.getActivationCode();

    super.refresh();

    delegate.setSerialNumber(serialNumber);
    //delegate.setActivationType(activationType);
   // delegate.setActivationCode(activationCode);

  }

  //--------------------------------------------------------------------
  //  Service Methods: implementation of PCSAccount's new subscriber
  //                   methods.  Realized in TMPCSQuebectelPrepaidConsumerAccount
  //                   & TMPCSPrepaidConsumerAccount.
  //--------------------------------------------------------------------
  public PCSSubscriber newPCSSubscriber(String serialNumber, boolean dealerHasDeposit, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return newPCSSubscriber(serialNumber, dealerHasDeposit, null, voiceMailLanguage);
  }

  public PCSSubscriber newPCSPagerSubscriber(String serialNumber, boolean dealerHasDeposit, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return newPCSPagerSubscriber(serialNumber, dealerHasDeposit, null, voiceMailLanguage);
  }

  public PCSSubscriber newPCSSubscriber(String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return newSubscriber(AccountManager.PRODUCT_TYPE_PCS, serialNumber, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage);
  }

  public PCSSubscriber newPCSPagerSubscriber(String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return newSubscriber(AccountManager.PRODUCT_TYPE_PAGER, serialNumber, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage);
  }

  /**
   * Implements method of the PCSAccount interface to be used in descendant classes. Since no multiple subscribers should
   * be activated on the prepaid accounts this method ignores the provided pieces of secondary equipment.
   *
   * @param serialNumber serial number of the primary equipment of the new subscriber.
   * @param secondarySerialNumbers serial numbers of the pieces of secondary equipment - ignored for prepaid accounts.
   * @param dealerHasDeposit <tt>true</tt> if the dealer has a deposit.
   * @param activationFeeChargeCode activation fee charge code.
   * @return new PCS subscriber.
   * @throws UnknownSerialNumberException if serial number not found in the database.
   * @throws SerialNumberInUseException if serial number is already active on the existing subscriber.
   * @throws InvalidSerialNumberException if serial number corresponds to the invalid equipment.
   * @throws TelusAPIException if a remote call to the database failed.
   * @see PCSAccount
   * @see TMPCSWesternPrepaidConsumerAccount
   * @see TMPCSPrepaidConsumerAccount
   */
  public PCSSubscriber newPCSSubscriber(String serialNumber, String[] secondarySerialNumbers, boolean dealerHasDeposit, String activationFeeChargeCode, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
    return newPCSSubscriber(serialNumber, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage);
  }

  public TMSubscriber getSubscriber0() throws TelusAPIException {
    if (subscriber == null) {
      assertAccountExists();

      Subscriber[] subscribers = getSubscribers(1);
      subscriber = subscribers != null && subscribers.length > 0 ? (TMSubscriber) subscribers[0] : null;
    }
    return subscriber;
  }
}



