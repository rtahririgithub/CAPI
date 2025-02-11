/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telus.api.account.ActivationTopUpPaymentArrangement;
import com.telus.api.account.ActivationTopUpPaymentArrangementException;
import com.telus.api.account.CreditCard;
import com.telus.api.account.PaymentCard;
import com.telus.api.account.PreRegisteredPrepaidCreditCard;
import com.telus.api.account.PrepaidCreditCard;
import com.telus.api.account.PrepaidDebitCard;
import com.telus.api.equipment.AirtimeCard;
import com.telus.eas.account.info.ActivationTopUpPaymentArrangementInfo;
import com.telus.eas.account.info.PreRegisteredPrepaidCreditCardInfo;
import com.telus.eas.account.info.PrepaidCreditCardInfo;
import com.telus.eas.account.info.PrepaidDebitCardInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.equipment.TMAirtimeCard;


public class TMActivationTopUpPaymentArrangement extends BaseProvider implements ActivationTopUpPaymentArrangement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @link aggregation
	 */
	private final ActivationTopUpPaymentArrangementInfo delegate;
	private TMPrepaidConsumerAccount prepaidAccount;
	private TMAirtimeCard airtimeCard;
	private PreRegisteredPrepaidCreditCardInfo preRegisteredCreditCard  = null ;


	public TMActivationTopUpPaymentArrangement(TMProvider provider, ActivationTopUpPaymentArrangementInfo delegate, TMPrepaidConsumerAccount account) {
		super(provider);
		this.delegate = delegate;
		prepaidAccount = account;
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	/**
	 * @deprecated as part of Prepaid Gemini - Inba.
	 */
	public double getChargeAmount() {
		return delegate.getChargeAmount();
	}
	/**
	 * @deprecated as part of Prepaid Gemini - Inba.
	 */
	public void setChargeAmount(double chargeAmount) {
		delegate.setChargeAmount(chargeAmount);
	}
	/**
	 * @deprecated as part of Prepaid Gemini - Inba.
	 */
	public double getThresholdAmount() {
		return delegate.getThresholdAmount();
	}
	/**
	 * @deprecated as part of Prepaid Gemini - Inba.
	 */
	public void setThresholdAmount(double value) {
		delegate.setThresholdAmount(value);
	}
	/**
	 * @deprecated as part of Prepaid Gemini - Inba.
	 */
	public CreditCard getCreditCard() {
		return prepaidAccount.getTopUpCreditCard0();
	}

	/**
	 * @deprecated as part of Prepaid Gemini - Inba.
	 */
	public void setActivationTopUpAmount(double activationTopUpAmount) {
		delegate.setActivationTopUpAmount(activationTopUpAmount);
	}
	/**
	 * @deprecated as part of Prepaid Gemini - Inba.
	 */
	public double getActivationTopUpAmount() {
		return delegate.getActivationTopUpAmount();
	}
	/**
	 * @deprecated as part of Prepaid Gemini - Inba.
	 */
	public void setAirtimeCard(AirtimeCard aircard) {
		airtimeCard = (TMAirtimeCard) aircard;
		delegate.setAirtimeCard(airtimeCard.getDelegate());
	}
	/**
	 * @deprecated as part of Prepaid Gemini - Inba.
	 */
	public AirtimeCard getAirtimeCard() {
		return delegate.getAirtimeCard();
	}
	/**
	 * @deprecated as part of Prepaid Gemini - Inba.
	 */
	public void setActivationTopUpCardType(String activationTopUpCardType) {
		delegate.setActivationTopUpCardType(activationTopUpCardType);
	}
	/**
	 * @deprecated as part of Prepaid Gemini - Inba.
	 */
	public String getActivationTopUpCardType() {
		return delegate.getActivationTopUpCardType();
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
	public ActivationTopUpPaymentArrangementInfo getDelegate() {
		return delegate;
	}

	public boolean validate() throws ActivationTopUpPaymentArrangementException {
		if (prepaidAccount == null) {
			return false;
		}

		PaymentCard[] paymentCards = retrievePaymentCardList();
		List paymentCardList = null;

		if (paymentCards != null && paymentCards.length > 0) {
			paymentCardList = Arrays.asList(paymentCards);
			addPreRegisteredCreditCardToList(paymentCardList);
			validatePaymentCards(paymentCardList);
			delegate.setupPaymentCardList((PaymentCard[]) paymentCardList.toArray(new PaymentCard[paymentCardList.size()]));
		} else {
			paymentCardList = oldValidateProcess();
			if (paymentCardList.size() > 0) {
				delegate.setupPaymentCardList((PaymentCard[]) paymentCardList.toArray(new PaymentCard[paymentCardList.size()]));
			}
		}

		paymentCards = retrievePaymentCardList();
		return (paymentCards !=null && paymentCards.length > 0); //if there's no payment card then there's no need to call saveActivationTopUpArrangement
	}

	public PrepaidCreditCard newPrepaidCreditCard(CreditCard creditCard) throws ActivationTopUpPaymentArrangementException {
		if (creditCard == null) {
			throw new ActivationTopUpPaymentArrangementException(ActivationTopUpPaymentArrangementException.REASON_CREDITCARD_NOT_SET);					
		}
		PrepaidCreditCardInfo prepaidCreditCard = new PrepaidCreditCardInfo();
		prepaidCreditCard.setAuthorizationCode(creditCard.getAuthorizationCode());
		prepaidCreditCard.setCardVerificationData(creditCard.getCardVerificationData());
		prepaidCreditCard.setExpiryMonth(creditCard.getExpiryMonth());
		prepaidCreditCard.setExpiryYear(creditCard.getExpiryYear());
		prepaidCreditCard.setHolderName(creditCard.getHolderName());
		prepaidCreditCard.setLeadingDisplayDigits(creditCard.getLeadingDisplayDigits());
		prepaidCreditCard.setToken(creditCard.getToken());
		prepaidCreditCard.setTrailingDisplayDigits(creditCard.getTrailingDisplayDigits());
		return prepaidCreditCard;

	}

	public PrepaidDebitCard newPrepaidDebitCard() {
		return new PrepaidDebitCardInfo();
	}

	public PaymentCard[] retrievePaymentCardList() {
		return delegate.retrievePaymentCardList();
	}

	public void clearPaymentCard() {
		delegate.clearPaymentCard();
	}

	public void setupPaymentCardList(PaymentCard[] paymentCardList) {
		delegate.setupPaymentCardList(extractPaymentCardDelegates(paymentCardList));
	}

	public PreRegisteredPrepaidCreditCard getPreRegisteredCreditCard() {
		return getPreRegisteredPrepaidCreditCardInfo();			
	}

	private PreRegisteredPrepaidCreditCardInfo getPreRegisteredPrepaidCreditCardInfo() {
		CreditCard creditCard = getTopUpCreditCard();

		if (creditCard != null) {
			if(preRegisteredCreditCard == null){
				preRegisteredCreditCard = new PreRegisteredPrepaidCreditCardInfo();
			}
			preRegisteredCreditCard.setAuthorizationCode(creditCard.getAuthorizationCode());			
			preRegisteredCreditCard.setCardVerificationData(creditCard.getCardVerificationData());
			preRegisteredCreditCard.setExpiryMonth(creditCard.getExpiryMonth());
			preRegisteredCreditCard.setExpiryYear(creditCard.getExpiryYear());
			preRegisteredCreditCard.setHolderName(creditCard.getHolderName());
			preRegisteredCreditCard.setLeadingDisplayDigits(creditCard.getLeadingDisplayDigits());
			preRegisteredCreditCard.setToken(creditCard.getToken());
			preRegisteredCreditCard.setTrailingDisplayDigits(creditCard.getTrailingDisplayDigits());
		}else if (preRegisteredCreditCard != null && creditCard == null) {
			preRegisteredCreditCard = null;
		}
		return preRegisteredCreditCard;				
	}

	private void validatePreRegisteredPrepaidCreditCard(PreRegisteredPrepaidCreditCard preRegisteredPrepaidCreditCard)
			throws ActivationTopUpPaymentArrangementException {
		if (preRegisteredPrepaidCreditCard.getToken() == null|| preRegisteredPrepaidCreditCard.getToken().equals("")) {
			if (preRegisteredPrepaidCreditCard.getTopupAmount() > 0|| preRegisteredPrepaidCreditCard.getRechargeAmount() > 0) {		
				throw new ActivationTopUpPaymentArrangementException(ActivationTopUpPaymentArrangementException.REASON_CREDITCARD_NOT_SET);				
			}
		}
	}
	
	private void validatePaymentCard(PrepaidCreditCard prepaidCreditCard) throws ActivationTopUpPaymentArrangementException  {
		if (prepaidCreditCard.getToken() == null|| prepaidCreditCard.getToken().equals("")) {
			if (prepaidCreditCard.getTopupAmount() > 0) {		
				throw new ActivationTopUpPaymentArrangementException(ActivationTopUpPaymentArrangementException.REASON_CREDITCARD_NOT_SET);				
			}
		}
	}
	
	private void validatePaymentCard (PrepaidDebitCard prepaidDebitCard) throws ActivationTopUpPaymentArrangementException  {
		if (prepaidDebitCard != null && prepaidDebitCard.getTopupAmount() < 0) {
			throw new ActivationTopUpPaymentArrangementException(ActivationTopUpPaymentArrangementException.REASON_TOPUPAMOUNT_NOT_SET);
		}
	}
	
	private void validatePaymentCard(AirtimeCard airtimeCard) throws ActivationTopUpPaymentArrangementException  {
		if (airtimeCard != null && (airtimeCard.getSerialNumber() == null || "".equals(airtimeCard.getSerialNumber()))) {
			throw new ActivationTopUpPaymentArrangementException(ActivationTopUpPaymentArrangementException.REASON_AIRTIMECARD_NOT_SET);
		}
	}
	
	private boolean isPaymentCardListHasPreRegisteredCard(List paymentCardList) {
		for (int i = 0; i < paymentCardList.size(); i++) {
			Object cardObject = paymentCardList.get(i);
			if (cardObject instanceof PreRegisteredPrepaidCreditCard) {
				return true;

			}
		}
		return false;
	}

	private void addPreRegisteredCreditCardToList(List paymentCardList) {
		if (getTopUpCreditCard() != null && !isPaymentCardListHasPreRegisteredCard(paymentCardList)) {
			paymentCardList.add(getPreRegisteredCreditCard());
		}
	}

	private void validatePaymentCards(List paymentCardList)throws ActivationTopUpPaymentArrangementException {	
		for(int i=0;i<paymentCardList.size();i++){
			Object cardObject = paymentCardList.get(i);

			if (cardObject instanceof PreRegisteredPrepaidCreditCard) {
				validatePreRegisteredPrepaidCreditCard((PreRegisteredPrepaidCreditCard) cardObject);
			} else if (cardObject instanceof PrepaidCreditCard) {
				validatePaymentCard ((PrepaidCreditCard) cardObject);
			} else if (cardObject instanceof PrepaidDebitCard) {
				validatePaymentCard((PrepaidDebitCard) cardObject);
			} else if (cardObject instanceof AirtimeCard) {
				validatePaymentCard((AirtimeCard) cardObject);
			}
		}

	}

	private List oldValidateProcess()throws ActivationTopUpPaymentArrangementException {
		List cardList = new ArrayList();
		//Payment card list is EMPTY
		//support existing process, credit card and air time card top up, no need to set this value
		// [Naresh] Added below if block to fix the prepaid production defect :adding airtimeCard to paymentCardList if cardtype is not set and airtimeCard details is passed
		// QA regression testing we found an issue with autoTopup and secondary Airtime topup , to fix this issue we separated this block .
		
		if (isActivationTopUpCardTypeEmpty() && !isAirtimeCardEmpty()) {
			cardList.add(getAirtimeCard());
			autoCCTopUpPatch(cardList);
		} 
		
		else if (isActivationTopUpCardTypeEmpty() || ActivationTopUpPaymentArrangement.ACTIVATION_TOPUP_CREDITCARD.equals(getActivationTopUpCardType())){	  
			if (getCreditCard() != null && getCreditCard().getToken() != null) {
				PreRegisteredPrepaidCreditCardInfo preRegisteredCreditCard = getPreRegisteredPrepaidCreditCardInfo();
				preRegisteredCreditCard.setTopupAmout(getActivationTopUpAmount());
				preRegisteredCreditCard.setRechargeAmount(getChargeAmount());
				preRegisteredCreditCard.setThresholdAmount(getThresholdAmount());
				delegate.setPreRegisteredCard(true);
				cardList.add(preRegisteredCreditCard);
			}else {
				if (getChargeAmount() > 0 || getActivationTopUpAmount() > 0) {
					throw new ActivationTopUpPaymentArrangementException(ActivationTopUpPaymentArrangementException.REASON_CREDITCARD_NOT_SET);
				}
			}
		}
		else if (ActivationTopUpPaymentArrangement.ACTIVATION_TOPUP_DEBITCARD.equals(getActivationTopUpCardType())){
			if (getActivationTopUpAmount() <= 0) {
				throw new ActivationTopUpPaymentArrangementException(ActivationTopUpPaymentArrangementException.REASON_TOPUPAMOUNT_NOT_SET);
			}else{
				PrepaidDebitCardInfo prepaidDebitCard = new PrepaidDebitCardInfo();
				prepaidDebitCard.setTopupAmout(getActivationTopUpAmount());
				cardList.add(prepaidDebitCard);
				autoCCTopUpPatch(cardList);
			}
		}else if  (ActivationTopUpPaymentArrangement.ACTIVATION_TOPUP_AIRTIMECARD.equals(getActivationTopUpCardType())){
			if (getAirtimeCard() == null || getAirtimeCard().getSerialNumber() == null || "".equals(getAirtimeCard().getSerialNumber())) {
				throw new ActivationTopUpPaymentArrangementException(ActivationTopUpPaymentArrangementException.REASON_AIRTIMECARD_NOT_SET);
			}else{
				cardList.add(getAirtimeCard());
				autoCCTopUpPatch(cardList);
			}
		} else {
			throw new ActivationTopUpPaymentArrangementException(ActivationTopUpPaymentArrangementException.REASON_INVALID_CARDTYPE);
		}
		return cardList;
	}
	
	
    private void autoCCTopUpPatch(List cardList) throws ActivationTopUpPaymentArrangementException {
        if (getCreditCard() != null && getCreditCard().getToken() != null) { //cc not empty
              PreRegisteredPrepaidCreditCardInfo preRegisteredCreditCard = getPreRegisteredPrepaidCreditCardInfo();
              preRegisteredCreditCard.setRechargeAmount(getChargeAmount());
              preRegisteredCreditCard.setThresholdAmount(getThresholdAmount());
              delegate.setPreRegisteredCard(true);
              cardList.add(preRegisteredCreditCard);
        }else { //cc empty
              if (getChargeAmount() > 0) {
                    throw new ActivationTopUpPaymentArrangementException(ActivationTopUpPaymentArrangementException.REASON_CREDITCARD_NOT_SET);
              }
        }
  }

	
	private boolean isActivationTopUpCardTypeEmpty() {
		if (getActivationTopUpCardType() == null || getActivationTopUpCardType().equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isAirtimeCardEmpty() {
		if (getAirtimeCard() != null && getAirtimeCard().getSerialNumber() != null && !"".equals(getAirtimeCard().getSerialNumber())) {
			return false;
		} else {
			return true;
		}
	}
	
	private TMCreditCard getTopUpCreditCard() {
		TMCreditCard cc = prepaidAccount.getTopUpCreditCard0(); //this never returns null
		
		if (cc != null && cc.getToken() != null) {
			return cc;
		}else {
			return null;
		}
	}

	private PaymentCard[] extractPaymentCardDelegates(PaymentCard[] paymentCards) {
		PaymentCard[] paymentCardDelegates = null;
		
		if (paymentCards != null) {
			paymentCardDelegates = new PaymentCard[paymentCards.length];
			
			for (int i = 0 ; i < paymentCards.length; i++) {
				PaymentCard cardObject = paymentCards[i];
				
				if (cardObject instanceof TMAirtimeCard) {
					paymentCardDelegates[i] = ((TMAirtimeCard) cardObject).getDelegate();
				}else if (cardObject instanceof TMCreditCard) {
					paymentCardDelegates[i] = ((TMCreditCard) cardObject).getDelegate();
				}else {
					paymentCardDelegates[i] = cardObject;
				}
			}
		}
		
		return paymentCardDelegates;
	}
}
