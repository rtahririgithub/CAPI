package com.telus.api.account;

import com.telus.api.*;

public class InvalidBillCycleException extends TelusAPIException {

	private static final long serialVersionUID = 1L;	
	
	public InvalidBillCycleException(Throwable exception) {
		super(exception);
	}

	public InvalidBillCycleException(String message) {
		super(message);
	}

}



