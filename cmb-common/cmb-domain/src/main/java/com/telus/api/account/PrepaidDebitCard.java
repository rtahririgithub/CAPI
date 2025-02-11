/**
 * 
 */
package com.telus.api.account;

/**
 * @author x139838
 *
 */
public interface PrepaidDebitCard extends DebitCard {

	void setTopupAmout(double topupAmount);
	
	double getTopupAmount();
}
