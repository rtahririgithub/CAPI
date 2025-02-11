package com.telus.api.account;

import com.telus.api.*;

public class InvalidActivationAmountException  extends TelusAPIException {

    public InvalidActivationAmountException(String message, Throwable exception) {
		super(message, exception);
	}

	public InvalidActivationAmountException(String message) {
		super(message);
	}

	public InvalidActivationAmountException(Throwable exception) {
		super(exception);
	}

}