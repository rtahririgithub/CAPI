package com.telus.eas.framework.exception;

/**
 * <CODE>TelusException</CODE> is the super-class for all user-thrown
 * checked exceptions.
 *
 * @author  Michael Lapish
 *
 */

import com.telus.api.TelusAPIException;
import com.telus.eas.framework.info.ExceptionInfo;

public class TelusException extends TelusAPIException {

	private static final long serialVersionUID = 1L;

	public String id;

	public TelusException(String id, String message, Throwable exception) {
		super("id=" + id + "; " + message, exception);
		this.id = id;
	}

	public TelusException(Throwable exception) {
		this("SYS00003", "", exception);
	}

	public TelusException(String pId, String pMessage) {
		this(pId, pMessage, null);
	}

	public TelusException(ExceptionInfo pExceptionInfo) {
		this(pExceptionInfo.getId(), pExceptionInfo.getMessage(), pExceptionInfo.getException());
	}

	public TelusException() {
		this("", "", null);
	}

	public TelusException(String id) {
		this(id, "", null);
	}

}
