package com.telus.eas.framework.exception;

import com.telus.eas.framework.info.ExceptionInfo;

public class ChangePasswordException extends TelusValidationException {

	public static final int INVALID_PASSWORD_LENGTH = 1;
	public static final int INVALID_PASSWORD_FIRST_LAST_DIGIT = 2;
	public static final int INVALID_PASSWORD_NO_DIGIT = 3;
	public static final int INVALID_PASSWORD_ALREADY_USED = 4;
	public static final int OLD_PASSWORD_INCORRECT = 5;
	
	private int reason;

	public int getReason() {
		return reason;
	}

	public void setReason(int reason) {
		this.reason = reason;
	}
	
	public ChangePasswordException() {
	    super();
	}
	
	public ChangePasswordException(String id, String message) {
	    super(id,message);
	}
	
	public ChangePasswordException(String id, String message, Throwable exception) {
	    super(id,message,exception);
	}
	
	public ChangePasswordException(ExceptionInfo pExceptionInfo, int reason){
	    super(pExceptionInfo);
	    setReason(reason);
	}	
}
