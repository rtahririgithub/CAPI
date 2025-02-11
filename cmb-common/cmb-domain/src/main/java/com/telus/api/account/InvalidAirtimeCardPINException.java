package com.telus.api.account;


public class InvalidAirtimeCardPINException  extends InvalidAirtimeCardException {
	private static final long serialVersionUID = 1L;
	
    public InvalidAirtimeCardPINException(String message, Throwable exception) {
		super(message, exception);
	}

	public InvalidAirtimeCardPINException(String message) {
		super(message);
	}

	public InvalidAirtimeCardPINException(Throwable exception) {
		super(exception);
	}
}