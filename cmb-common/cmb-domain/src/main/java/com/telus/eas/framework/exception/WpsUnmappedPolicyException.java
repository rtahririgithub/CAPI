package com.telus.eas.framework.exception;

/**
 * This class was created as a short term solution to relay the original WPS
 * Service Exception information to the Client API web service.
 * 
 * The medium term solution is to have an ESB web service operation, with a
 * request and response identical to WPS, orchestrate calls to WPS to perform
 * the credit card transaction and the call to KB to create a memo on a credit
 * card transaction failure. This class will not be required once this
 * refactoring occurs, so only the web service implementation should reference
 * this class.
 * 
 * @author Alan Koop
 * @see com.telus.eas.framework.exception.BankDeclinedTransactionException
 * @see com.telus.eas.framework.exception.WpsPolicyException
 */
public class WpsUnmappedPolicyException extends TelusSystemException {

	private static final long serialVersionUID = 1L;
	

	private String wpsServiceExceptionMessage;
	private PolicyFaultInfo wpsPolicyFaultInfo;
	private Throwable wpsServiceExceptionCause;

	public WpsUnmappedPolicyException(
			String id,
			String message,
			Throwable throwable,
			String wpsServiceExceptionMessage,
			PolicyFaultInfo wpsPolicyFaultInfo,
			Throwable wpsServiceExceptionCause) {

		super(id, message, throwable);

		this.wpsServiceExceptionMessage = wpsServiceExceptionMessage;
		this.wpsPolicyFaultInfo = wpsPolicyFaultInfo;
		this.wpsServiceExceptionCause = wpsServiceExceptionCause;
	}

	/**
	 * This method returns the message contained in the ServiceException
	 * originally thrown by the WPS service operation.
	 * 
	 * @return The original WPS ServiceException message.
	 */
	public String getWpsServiceExceptionMessage() {
		return wpsServiceExceptionMessage;
	}

	/**
	 * This method returns the ServiceFaultInfo contained in the ServiceException
	 * originally thrown by the WPS service operation.
	 * 
	 * @return The original WPS ServiceException ServiceFaultInfo.
	 */
	public PolicyFaultInfo getWpsPolicyFaultInfo() {
		return wpsPolicyFaultInfo;
	}

	/**
	 * This method returns the cause contained in the ServiceException
	 * originally thrown by the WPS service operation.
	 * 
	 * @return The original WPS ServiceException cause.
	 */
	public Throwable getWpsServiceExceptionCause() {
		return wpsServiceExceptionCause;
	}

}
