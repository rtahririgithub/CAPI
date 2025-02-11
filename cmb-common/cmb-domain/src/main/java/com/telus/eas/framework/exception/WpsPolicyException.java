package com.telus.eas.framework.exception;

/**
 * This class was created as a short term solution to relay the original WPS
 * Policy Exception information to the Client API web service.
 * 
 * The medium term solution is to have an ESB web service operation, with a
 * request and response identical to WPS, orchestrate calls to WPS to perform
 * the credit card transaction and the call to KB to create a memo on a credit
 * card transaction failure. This class will not be required once this
 * refactoring occurs, so only the web service implementation should reference
 * this class.
 * 
 * The WPS service throws PolicyException's with the following values in the errorCode attribute:
 * 1.  errorCode values prefixed with "AV" whenever an Avalon error occurs
 * 2.  errorCode values prefixed with "PYMT" whenever WPS throws a PolicyException
 * (e.g. fails validation checks, fraud checks, AVS checks, velocity checks etc) 
 * 
 * Note that the WPS web service does not throw a PolciyException when the bank
 * declines the credit card transaction. See BankDeclinedTransactionException for
 * how this use case is communicated to the web service.
 * 
 * @author Alan Koop
 * @see com.telus.eas.framework.exception.BankDeclinedTransactionException
 * @see com.telus.eas.framework.exception.WpsServiceException
 */
public class WpsPolicyException extends TelusCreditCardException {

	private static final long serialVersionUID = 1L;

		
	private PolicyFaultInfo wpsPolicyFaultInfo;

	public WpsPolicyException(
			String id,
			String ccardMessageEN,
			String ccardMessageFR,
			PolicyFaultInfo wpsPolicyFaultInfo,
			Throwable wpsPolicyExceptionCause) {

		super(id,ccardMessageEN, ccardMessageFR, wpsPolicyExceptionCause);

		this.wpsPolicyFaultInfo = wpsPolicyFaultInfo;
	}


	/**
	 * This method returns the PolicyFaultInfo contained in the PolicyException
	 * originally thrown by the WPS service operation.
	 * 
	 * @return The original WPS PolicyException PolicyFaultInfo.
	 */
	public PolicyFaultInfo getWpsPolicyFaultInfo() {
		return wpsPolicyFaultInfo;
	}

}
