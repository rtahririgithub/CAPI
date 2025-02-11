package com.telus.eas.framework.exception;

/**
 * This class was created as a short term solution to relay the original Global
 * Payments (GP) bank response code to the Client API web service.
 * 
 * The medium term solution is to have an ESB web service operation, with a
 * request and response identical to WPS, orchestrate calls to WPS to perform
 * the credit card transaction and the call to KB to create a memo on a credit
 * card transaction failure. This class will not be required once this
 * refactoring occurs, so only the web service implementation should reference
 * this class.
 *
 * This exception indicates that the bank did not approve the credit card transaction.
 * Thus the original WPS service operation response "transactionResult" attribute
 * had a value of "F" (Failed) and the provider "responseCode" attribute 
 * had a value > 49.
 * 
 * @author Alan Koop
 * @see com.telus.eas.framework.exception.WpsPolicyException
 * @see com.telus.eas.framework.exception.WpsServiceException
 */
public class BankDeclinedTransactionException extends TelusCreditCardException {

	private String bankResponseCode;
	private String bankResponseMessage;

	public BankDeclinedTransactionException (
			String id,
			String ccardMessageEN,
			String ccardMessageFR,
			String bankResponseCode,
			String bankResponseMessage) {

		super(id,ccardMessageEN, ccardMessageFR);

		this.bankResponseCode = bankResponseCode;
		this.bankResponseMessage = bankResponseMessage;
	}

	/**
	 * This method returns the original provider response code from Global Payments (GP) whenever
	 * the provider does not approve the credit card transaction.
	 * 
	 * @return String
	 */
	public String getBankResponseCode() {
		return bankResponseCode;
	}

	/**
	 * This method returns the original provider response message from Global Payments (GP)
	 * 
	 * @return String
	 */
	public String getBankResponseMessage() {
		return bankResponseMessage;
	}
}
