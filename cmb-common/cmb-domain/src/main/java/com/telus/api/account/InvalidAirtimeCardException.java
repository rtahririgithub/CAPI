package com.telus.api.account;

import com.telus.api.*;

public class InvalidAirtimeCardException  extends TelusAPIException {
	private static final long serialVersionUID = 1L;
	
    public InvalidAirtimeCardException(String message, Throwable exception) {
		super(message, exception);
	}

	public InvalidAirtimeCardException(String message) {
		super(message);
	}

	public InvalidAirtimeCardException(Throwable exception) {
		super(exception);
	}
}