/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import com.telus.api.TelusAPIException;
import com.telus.api.account.InvalidSerialNumberException;
import com.telus.api.account.PCSPrepaidConsumerAccount;
import com.telus.api.account.PCSSubscriber;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.api.account.UnsupportedEquipmentException;
import com.telus.api.equipment.Equipment;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.provider.TMProvider;


public class TMPCSPrepaidConsumerAccount extends TMPrepaidConsumerAccount implements PCSPrepaidConsumerAccount {
  /**
   * @link aggregation
   */
  private final PrepaidConsumerAccountInfo delegate;


  /**
   * @link aggregation
   */
  private final TMPCSAccount account;

  public TMPCSPrepaidConsumerAccount(TMProvider provider, PrepaidConsumerAccountInfo delegate, Equipment equipment) {
    super(provider, delegate, equipment);
    this.delegate = delegate;
    account = new TMPCSAccount(provider, delegate, this);
 }

  public TMPCSPrepaidConsumerAccount(TMProvider provider, PrepaidConsumerAccountInfo delegate) {
    super(provider, delegate);
    this.delegate = delegate;
    account = new TMPCSAccount(provider, delegate, this);
 }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
/* -- implemented in TMBasePrepaidAccount
  public PCSSubscriber newPCSSubscriber(String serialNumber, boolean dealerHasDeposit) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return account.newPCSSubscriber(serialNumber, dealerHasDeposit);
  }

  public PCSSubscriber newPCSPagerSubscriber(String serialNumber, boolean dealerHasDeposit) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return account.newPCSPagerSubscriber(serialNumber, dealerHasDeposit);
  }

  public PCSSubscriber newPCSSubscriber(String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return account.newPCSSubscriber(serialNumber, dealerHasDeposit, activationFeeChargeCode);
  }

  public PCSSubscriber newPCSPagerSubscriber(String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode) throws UnknownSerialNumberException, SerialNumberInUseException, TelusAPIException {
    return account.newPCSPagerSubscriber(serialNumber, dealerHasDeposit, activationFeeChargeCode);
  }
*/
//  public PricePlanSubscriberCount[] getPricePlanSubscriberCount() throws TelusAPIException {
//    return account.getPricePlanSubscriberCount();
//  }
  
  public PrepaidConsumerAccountInfo getDelegate(){
      return delegate;
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

	public PCSSubscriber newPCSSubscriber(String phoneNumber,
			String serialNumber, boolean dealerHasDeposit,
			String voiceMailLanguage) throws UnknownSerialNumberException,
			SerialNumberInUseException, InvalidSerialNumberException,
			TelusAPIException {
		return account.newPCSSubscriber(phoneNumber, serialNumber, dealerHasDeposit, voiceMailLanguage);
	}
	
	public boolean hasHSPASubscriberInBAN() throws TelusAPIException {
		return account.hasHSPASubscriberInBAN();
	}
	
	  public void refresh() throws TelusAPIException {
		  super.refresh();
		  account.refresh();
	  }

	public PCSSubscriber newPCSSubscriber(String phoneNumber,
			String serialNumber, String associatedHandsetIMEI,
			boolean dealerHasDeposit, String activationFeeChargeCode,
			String voiceMailLanguage) throws UnknownSerialNumberException,
			SerialNumberInUseException, InvalidSerialNumberException,
			UnsupportedEquipmentException, TelusAPIException {

		return account.newPCSSubscriber(phoneNumber, serialNumber, associatedHandsetIMEI, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage);
	}

	public PCSSubscriber newPCSSubscriber(String phoneNumber,
			String serialNumber, String[] secondarySerialNumbers,
			boolean dealerHasDeposit, String activationFeeChargeCode,
			String voiceMailLanguage) throws UnknownSerialNumberException,
			SerialNumberInUseException, InvalidSerialNumberException,
			TelusAPIException {
 
		return account.newPCSSubscriber(phoneNumber, serialNumber, secondarySerialNumbers, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage);
	}

	public PCSSubscriber newPCSBCSubscriber(String seatType,
			String phoneNumber, String serialNumber, boolean dealerHasDeposit,
			String activationFeeChargeCode, String voiceMailLanguage,
			String associatedHandsetIMEI) throws UnknownSerialNumberException,
			SerialNumberInUseException, InvalidSerialNumberException,
			TelusAPIException {
		return account.newPCSBCSubscriber(seatType, phoneNumber, serialNumber, dealerHasDeposit, activationFeeChargeCode, voiceMailLanguage, associatedHandsetIMEI);
	}

}




