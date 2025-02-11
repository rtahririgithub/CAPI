/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import com.telus.api.*;
import com.telus.api.account.*;
import com.telus.provider.*;
import com.telus.eas.account.info.*;


public class TMPCSPostpaidBusinessRegularAccount extends TMPostpaidBusinessRegularAccount implements PCSPostpaidBusinessRegularAccount {

  /**
   * @link aggregation
   */
  private final PostpaidBusinessRegularAccountInfo delegate;


  /**
   * @link aggregation
   */
  private final TMPCSAccount account;

  public TMPCSPostpaidBusinessRegularAccount(TMProvider provider, PostpaidBusinessRegularAccountInfo delegate) {
    super(provider, delegate);
    this.delegate = delegate;
    account = new TMPCSAccount(provider, delegate, this);
 }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public Address getAlternateCreditCheckAddress() {
    return account.getAlternateCreditCheckAddress();
  }

  @Deprecated
  public PagerSubscriber newPagerSubscriber(String serialNumber) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
   return account.newPagerSubscriber(serialNumber);
  }

  @Deprecated
  public PagerSubscriber newPagerSubscriber(String serialNumber, String activationFeeChargeCode) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
    return account.newPagerSubscriber(serialNumber, activationFeeChargeCode);
  }

  public PCSSubscriber newPCSSubscriber(String serialNumber, boolean dealerHasDeposit, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return account.newPCSSubscriber(serialNumber, dealerHasDeposit, voiceMailLanguage);
  }

  @Deprecated
  public PCSSubscriber newPCSPagerSubscriber(String serialNumber, boolean dealerHasDeposit) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return account.newPCSPagerSubscriber(serialNumber, dealerHasDeposit);
  }

  public PCSSubscriber newPCSSubscriber(String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return account.newPCSSubscriber(serialNumber, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage);
  }

  public PCSSubscriber newPCSPagerSubscriber(String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return account.newPCSPagerSubscriber(serialNumber, dealerHasDeposit, activationFeeChargeCode);
  }

  public PCSSubscriber newPCSSubscriber(String serialNumber, String[] secondarySerialNumbers, boolean dealerHasDeposit, String activationFeeChargeCode, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
    return account.newPCSSubscriber(serialNumber, secondarySerialNumbers, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage);
  }

  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }

	public PCSSubscriber newPCSSubscriber(String serialNumber, String associatedHandsetIMEI, boolean dealerHasDeposit, String activationFeeChargeCode, String voidMailLanguage)
			throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, UnsupportedEquipmentException, TelusAPIException {
		return account.newPCSSubscriber(serialNumber, associatedHandsetIMEI, dealerHasDeposit, activationFeeChargeCode, voidMailLanguage);
	}
	
	public PCSSubscriber newPCSSubscriber(String phoneNumber, String serialNumber,
			boolean dealerHasDeposit, String voiceMailLanguage)
			throws UnknownSerialNumberException, SerialNumberInUseException,
			InvalidSerialNumberException, TelusAPIException {
		return account.newPCSSubscriber(phoneNumber, serialNumber, dealerHasDeposit, voiceMailLanguage);	
	}
  public boolean hasHSPASubscriberInBAN() throws TelusAPIException {
	  return account.hasHSPASubscriberInBAN();
  }
  
  public void refresh() throws TelusAPIException {
	  super.refresh();
	  account.refresh();
  }

public PCSSubscriber newPCSSubscriber(String phoneNumber, String serialNumber,
		String associatedHandsetIMEI, boolean dealerHasDeposit,
		String activationFeeChargeCode, String voiceMailLanguage)
		throws UnknownSerialNumberException, SerialNumberInUseException,
		InvalidSerialNumberException, UnsupportedEquipmentException,
		TelusAPIException {
	
	return account.newPCSSubscriber(phoneNumber, serialNumber, associatedHandsetIMEI, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage);
}

public PCSSubscriber newPCSSubscriber(String phoneNumber, String serialNumber,
		String[] secondarySerialNumbers, boolean dealerHasDeposit,
		String activationFeeChargeCode, String voiceMailLanguage)
		throws UnknownSerialNumberException, SerialNumberInUseException,
		InvalidSerialNumberException, TelusAPIException {

	return account.newPCSSubscriber(phoneNumber, serialNumber, secondarySerialNumbers, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage);
}




public PCSSubscriber newPCSBCSubscriber(String seatType, String phoneNumber,
		String serialNumber, boolean dealerHasDeposit,
		String activationFeeChargeCode, String voiceMailLanguage,
		String associatedHandsetIMEI) throws UnknownSerialNumberException,
		SerialNumberInUseException, InvalidSerialNumberException,
		TelusAPIException {
	return account.newPCSBCSubscriber(seatType, phoneNumber, serialNumber, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage, associatedHandsetIMEI);
}
  
}




