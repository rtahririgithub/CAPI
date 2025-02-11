/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

/**
 * <CODE>PaymentFailedException</CODE>
 * 
 */
public class SerialNumberInUseException extends InvalidSerialNumberException {

	public SerialNumberInUseException(String message, Throwable exception, String serialNumber) {
		super(message, exception, serialNumber);
	}

	public SerialNumberInUseException(Throwable exception, String serialNumber) {
		super(exception, serialNumber);
	}

	public SerialNumberInUseException(String message, String serialNumber) {
		super(message, serialNumber);
	}

	public SerialNumberInUseException(String message) {
		super(message);
	}
    public SerialNumberInUseException(String message, String serialNumber, int reason) {
	    super(message, serialNumber, reason);
	}
    
    public SerialNumberInUseException(String message, int reason) {
	    super(message, reason);
	}
    

}



