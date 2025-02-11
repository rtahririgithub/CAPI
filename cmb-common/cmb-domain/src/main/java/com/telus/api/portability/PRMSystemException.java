package com.telus.api.portability;

import com.telus.api.TelusAPIException;

public class PRMSystemException extends TelusAPIException {

	public PRMSystemException(String message) {
		super(message);
	}
	
	public PRMSystemException(String message, Throwable t) {
	    super(message, t);
	  }
}
