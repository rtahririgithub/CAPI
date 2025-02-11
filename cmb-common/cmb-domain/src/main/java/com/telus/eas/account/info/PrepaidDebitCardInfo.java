/**
 * 
 */
package com.telus.eas.account.info;

import com.telus.api.account.PrepaidDebitCard;

/**
 * @author x139838
 *
 */
public class PrepaidDebitCardInfo extends DebitCardInfo implements PrepaidDebitCard {
	
	private static final long serialVersionUID = 1L;
	private double topupAmount;

	public void setTopupAmout(double topupAmount) {
		this.topupAmount = topupAmount;
	}

	public double getTopupAmount() {
		return topupAmount;
	}

}
