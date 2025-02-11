package com.telus.api.account;

import com.telus.api.*;

public class UnsupportedEquipmentException  extends TelusAPIException {
	
    public UnsupportedEquipmentException(String message, Throwable exception) {
		super(message, exception);
	}

	public UnsupportedEquipmentException(String message) {
		super(message);
	}

	public UnsupportedEquipmentException(Throwable exception) {
		super(exception);
	}

}