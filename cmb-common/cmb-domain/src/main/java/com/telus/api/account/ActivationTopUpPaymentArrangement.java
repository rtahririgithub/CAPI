package com.telus.api.account;
import com.telus.api.equipment.AirtimeCard;

public interface ActivationTopUpPaymentArrangement {
	
	/**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	double getThresholdAmount();
	/**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	 void setThresholdAmount(double value);
	 /**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	 double getChargeAmount();
	 /**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	 void setChargeAmount(double chargeAmount);
	 /**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	 CreditCard getCreditCard();
	 /**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	 void setActivationTopUpAmount(double activationTopUpAmount); //actual dollar amount
	 /**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	 double getActivationTopUpAmount();
	 /**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	 void setAirtimeCard(AirtimeCard aircard);
	 /**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	 AirtimeCard getAirtimeCard();
	 /**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	 String getActivationTopUpCardType();
	 /**
	  *@deprecated as part of Prepaid Gemini - Inba.
	  */
	 void setActivationTopUpCardType(String activationTopUpCardType);
	 
	 boolean validate() throws  ActivationTopUpPaymentArrangementException;

	 public static final String ACTIVATION_TOPUP_CREDITCARD = "CREDITCARD";
	 public static final String ACTIVATION_TOPUP_AIRTIMECARD = "AIRTIME";
	 public static final String ACTIVATION_TOPUP_DEBITCARD = "DEBITCARD";
	 
	 PrepaidCreditCard newPrepaidCreditCard(CreditCard c) throws ActivationTopUpPaymentArrangementException;
	 PrepaidDebitCard	newPrepaidDebitCard();
	 PaymentCard[]	retrievePaymentCardList();
	 void	clearPaymentCard();
	 void setupPaymentCardList(PaymentCard[] paymentCardList);
	 PreRegisteredPrepaidCreditCard	getPreRegisteredCreditCard();
}
