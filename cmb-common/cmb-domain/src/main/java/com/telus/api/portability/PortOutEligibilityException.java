package com.telus.api.portability;

import com.telus.api.message.ApplicationMessage;

public class PortOutEligibilityException extends PortRequestException {

	private static final long serialVersionUID = 1L;

	// these error codes correspond to message IDs in the EAS.adapted_messages table
	public static final int ERR_OSP_TRANSFER_BLOCK = 167;
	public static final int ERR_INVALID_OSP_ACCOUNT_NUMBER = 168;
	public static final int ERR_INVALID_OSP_ESN = 169;
	public static final int ERR_INVALID_OSP_PIN = 170;
	
	public PortOutEligibilityException(String message) {		
		super(message);
	}
	
	public PortOutEligibilityException(Throwable exception, ApplicationMessage applicationMessage) {
		super(exception, applicationMessage);
	}
	
	public PortOutEligibilityException(ApplicationMessage applicationMessage) {
		super(applicationMessage);
	}
	
}