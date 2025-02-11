package com.telus.api.account;

public interface PrepaidCreditCard extends CreditCard {

	void setTopupAmout(double topupAmount);
	
	double getTopupAmount();
}
