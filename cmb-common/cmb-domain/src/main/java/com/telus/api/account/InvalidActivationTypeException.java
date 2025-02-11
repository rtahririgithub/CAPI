/*
 * Created on 8-Apr-2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.telus.api.account;

import com.telus.api.TelusAPIException;

/**
 * @author x119734
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InvalidActivationTypeException extends TelusAPIException {
	
	public InvalidActivationTypeException(String message, Throwable exception) {
		super(message, exception);
	}

	public InvalidActivationTypeException(String message) {
		super(message);
	}

	public InvalidActivationTypeException(Throwable exception) {
		super(exception);
	}

}
