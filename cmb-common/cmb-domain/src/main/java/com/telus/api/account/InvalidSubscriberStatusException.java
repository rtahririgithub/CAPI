package com.telus.api.account;

import com.telus.api.TelusAPIException;

public class InvalidSubscriberStatusException extends TelusAPIException {

	private static final long serialVersionUID = -2494360033137070113L;

	public static final int INVALID_COMM_SUITE = 1;
	private int reason;

	public InvalidSubscriberStatusException(String message) {
		super(message);
	}

	public InvalidSubscriberStatusException(int reason) {
		super(getReasonText(reason));
		this.reason = reason;
	}

	public InvalidSubscriberStatusException(String message, int reason) {
		super(message);
		this.reason = reason;
	}

	public int getReason() {
		return reason;
	}

	public String getReasonText() {
		return getReasonText(reason);
	}

	private static String getReasonText(int reason) {
		switch (reason) {
		case INVALID_COMM_SUITE:
			return "One or more subscriber(s) in the associated communication suite is not active.";
		default:
			return "Unknown";
		}
	}

}
