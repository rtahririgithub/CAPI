/**
 * 
 */
package com.telus.eas.account.info;

import com.telus.api.account.PreRegisteredPrepaidCreditCard;

/**
 * @author x139838
 *
 */
public class PreRegisteredPrepaidCreditCardInfo extends PrepaidCreditCardInfo implements PreRegisteredPrepaidCreditCard{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double thresholdAmount;
	private double rechargeAmount;
	
	public void setThresholdAmount(double thresholdAmount) {
		this.thresholdAmount = thresholdAmount;
	}

	public double getThresholdAmount() {
		return thresholdAmount;
	}

	public void setRechargeAmount(double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public double getRechargeAmount() {
		return rechargeAmount;
	}

}
