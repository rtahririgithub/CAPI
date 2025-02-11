/**
 * 
 */
package com.telus.api.account;

import com.telus.api.TelusAPIException;

/**
 * @author tongts
 *
 */
public interface AnyToPrepaidMigrationRequest extends MigrationRequest {
	
	ActivationTopUp getActivationTopUp();
	void setActivationTopUp(ActivationTopUp activationTopUp);
	
	CreditCard getActivationCreditCard();
	
	/**
	 * 
	 * @param creditCard
	 * @param auditHeader
	 * @throws TelusAPIException
	 */
	void setActivationCreditCard(CreditCard creditCard, AuditHeader auditHeader) throws TelusAPIException;
	
	String getActivationAirtimeCardNumber();
	void setActivationAirtimeCardNumber(String cardNumber);
	
	double getActivationCreditAmount();
	void setActivationCreditAmount(double activationCreditAmount);

	int getActivationType();
	void setActivationType(int activationType);

	Contract getNewPrepaidContract() throws TelusAPIException;
}
