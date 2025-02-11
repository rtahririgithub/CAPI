package com.telus.eas.account.info;

import com.telus.api.account.PrepaidCreditCard;

public class PrepaidCreditCardInfo extends CreditCardInfo implements PrepaidCreditCard {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double topupAmount;
	

	public void setTopupAmout(double topupAmount) {
		this.topupAmount = topupAmount;
	}

	public double getTopupAmount() {
		return topupAmount;
	}
}
