package com.telus.eas.account.info;

import com.telus.api.account.ActivationTopUpPaymentArrangement;
import com.telus.api.account.ActivationTopUpPaymentArrangementException;
import com.telus.api.account.CreditCard;
import com.telus.api.account.PaymentCard;
import com.telus.api.account.PreRegisteredPrepaidCreditCard;
import com.telus.api.account.PrepaidCreditCard;
import com.telus.api.account.PrepaidDebitCard;
import com.telus.api.equipment.AirtimeCard;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.framework.info.Info;

public class ActivationTopUpPaymentArrangementInfo extends Info implements ActivationTopUpPaymentArrangement {

	 static final long serialVersionUID = 1L;

     private double thresholdAmount;
     private double chargeAmount;
     private boolean preRegisteredCard = false;
     private double activationTopUpAmount;
     private CardInfo airtimeCard;
     private String activationTopUpCardType;
     private PaymentCard[] paymentCards = null;
     
	
	/**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	public String getActivationTopUpCardType() {
		return activationTopUpCardType;
	}
	/**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	public void setActivationTopUpCardType(String activationTopUpCardType) {
		this.activationTopUpCardType = activationTopUpCardType;
	}
	/**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	public double getThresholdAmount() {
       return thresholdAmount;
	}
	 /**
	 * @deprecated as part of Prepaid Gemini - Inba.
	 */
	public void setThresholdAmount(double amount) {
      this.thresholdAmount = amount;
	}
	/**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	public double getChargeAmount() {
		return chargeAmount;
	}
	/**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	public void setChargeAmount(double amount) {
		this.chargeAmount = amount;
	}
	
	public void setPreRegisteredCard (boolean prc) {
		this.preRegisteredCard = prc;
	}
	
	public boolean isPreRegisteredCard() {
		return this.preRegisteredCard;
	}
	/**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	public CreditCard getCreditCard() {
		throw new UnsupportedOperationException("Method not implemented here"); 
	}
	/**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	public void setActivationTopUpAmount(double activationTopUpAmount) {
		this.activationTopUpAmount = activationTopUpAmount;
	}
	/**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	public double getActivationTopUpAmount() {
		return activationTopUpAmount;
	}
	/**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	public void setAirtimeCard (AirtimeCard aircard) {
		setAirtimeCard0 ((CardInfo) aircard);
	}
	private void setAirtimeCard0(CardInfo aircard) {
		airtimeCard = aircard;
	}
	/**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	public AirtimeCard getAirtimeCard() {
		return airtimeCard;
	}
	
	 
	public boolean validate() throws ActivationTopUpPaymentArrangementException {
		throw new UnsupportedOperationException("Method not implemented here"); 
	}

	public PrepaidCreditCard newPrepaidCreditCard(CreditCard creditCard) {
		throw new UnsupportedOperationException("Method not implemented here"); 
	}

	public PrepaidDebitCard newPrepaidDebitCard() {
		throw new UnsupportedOperationException("Method not implemented here"); 
	}

	public PaymentCard[] retrievePaymentCardList() {
		return paymentCards;
	}

	public void clearPaymentCard() {
		this.paymentCards = null;
	}

	public void setupPaymentCardList(PaymentCard[] paymentCardList) {
		this.paymentCards = paymentCardList;
	}

	public PreRegisteredPrepaidCreditCard getPreRegisteredCreditCard() {
		throw new UnsupportedOperationException("Method not implemented here");
	}

}
