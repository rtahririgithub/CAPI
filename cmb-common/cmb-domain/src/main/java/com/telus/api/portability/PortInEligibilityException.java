package com.telus.api.portability;

import com.telus.api.message.ApplicationMessage;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 24-Oct-2006
 */

public class PortInEligibilityException extends PortRequestException {
	private String phoneNumber;

	public PortInEligibilityException(ApplicationMessage applicationMessage) {
		super(applicationMessage);
	}

	public PortInEligibilityException(ApplicationMessage applicationMessage, String phoneNumber) {
		super(applicationMessage);
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
}
