/**
 * 
 */
package com.telus.api.account;

/**
 * @author x139838
 *
 */
public interface PreRegisteredPrepaidCreditCard extends PrepaidCreditCard {

	void setThresholdAmount(double thresholdAmount);
	
	double getThresholdAmount();
	
	void setRechargeAmount(double rechargeAmount);
	
	double getRechargeAmount();
}
