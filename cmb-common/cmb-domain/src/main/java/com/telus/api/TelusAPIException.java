/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.telus.api.message.ApplicationMessage;


/**
 * <CODE>TelusAPIException</CODE> is the base for all API-defined, checked
 * exceptions.  It may be instantiated with another exception to indicate
 * the specific cause of the the overall failure.   If a nested exception is
 * used, it's stack trace and message will be prepended to this one's.
 *
 */
public class TelusAPIException extends Exception {

	private static final long serialVersionUID = 1L;
	private String stackTrace;
	protected int reason;
	protected ApplicationMessage applicationMessage;

	/**
	 * This constant is to specifically identify the situation where the APILink's session ticket expiry.
	 * Whenever client catch a TelusAPIExeption, in which the message contain the following constant, client should 
	 * destory the ClientAPI instance and re-acquire a new instance of ClientAPI. 
	 */
	public static final String KB_INVALID_SESSION_TICKET_ERROR = "KB_INVALID_SESSION_TICKET";

	public TelusAPIException(String message, Throwable exception) {
		super(constructMessage(message, exception));
		
		if (exception != null) {
			StringWriter s = new StringWriter();
			PrintWriter p = new PrintWriter(s);
			exception.printStackTrace(p);
			p.flush();
			this.stackTrace = s.toString();
		}else{
			this.stackTrace = "Exception Trace Not Available";
		}
		if (exception instanceof ApplicationException || exception instanceof SystemException || exception instanceof TelusAPIException) {
			initCause(exception);
		}
	}

	private static String constructMessage(String message, Throwable exception) {	
		String returnMsg = message + ((exception != null && exception.getMessage() != null) ? ": " + exception.getMessage() : "");
		
		if (exception instanceof SystemException) {
			SystemException sysEx = (SystemException) exception;
			if (sysEx.getSubsystemMessage().length() > 0)
				returnMsg = returnMsg + ": " + sysEx.getSubsystemMessage();
		}		
		// NB: the same getSubsystemMessage method has been added to ApplicationException
		// however, no ApplicationException trace has been identified that returns a
		// message missing the underlying exception details.  Add ApplicationException
		// if one is found otherwise leave as is to minimize impact
		
		return returnMsg;
		
	}

	public TelusAPIException(Throwable exception) {
		this("", exception);
	}

	public TelusAPIException(String message) {
		this(message, null);
	}

	public TelusAPIException(Throwable exception, ApplicationMessage applicationMessage, int reason) {
		super(exception);
		this.applicationMessage = applicationMessage;
		this.reason = reason;
	}

	public TelusAPIException(ApplicationMessage applicationMessage, int reason) {
		this(applicationMessage.getText(Locale.ENGLISH), null);
		this.applicationMessage = applicationMessage;
		this.reason = reason;
	}

	public void printStackTrace(PrintStream s) {
		super.printStackTrace(s);
		s.println(stackTrace);
	}

	public void printStackTrace(PrintWriter s) {
		super.printStackTrace(s);
		s.println(stackTrace);
	}

	public String getStackTrace0() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);		
		super.printStackTrace(pw);
		
		return sw.toString() + stackTrace;
	}
	
	/**
	 * Print our stack trace as a cause for the specified stack trace.
	 */
	private void printStackTraceAsCause(PrintStream s, StackTraceElement[] causedTrace) {
		printStackTrace(s);
	}
    
	/**
	 * Print our stack trace as a cause for the specified stack trace.
	 */
	private void printStackTraceAsCause(PrintWriter s, StackTraceElement[] causedTrace) {
		printStackTrace(s);
	}

	public ApplicationMessage getApplicationMessage() {
		return applicationMessage;
	}

	//  public Throwable fillInStackTrace() {
	//    //System.err.println("TelusAPIException.fillInStackTrace()");
	//    Throwable t = super.fillInStackTrace();
	//
	//    StringWriter s = new StringWriter();
	//    PrintWriter p = new PrintWriter(s);
	//
	//    printStackTrace(p);
	//    p.println("----- Continued in -----");
	//    saveStackTraceImpl(p, this, 0);
	//
	//    p.flush();
	//
	//    this.stackTrace = s.toString();
	//
	//    return t;
	//  }

	//=====================================================================================================
	// Reasons
	//=====================================================================================================
	private static final Map EXCEPTION_REASONS = new HashMap(2 * 1024);

	static final void setupReasons(HashMap map) {
		if (EXCEPTION_REASONS.size() > 0) {
			throw new RuntimeException("EXCEPTION_REASONS is already initialized");
		}
		EXCEPTION_REASONS.putAll(map);
	}

	//=====================================================================================================

	private static String getReasonTextKey(Class exceptionClass, int reasonId, String businessRole, String language) {
		return exceptionClass.getName() + "-" + reasonId + "-" + businessRole + "-" + language;
	}

	private static String lookupReasonText0(Class exceptionClass, int reasonId, String businessRole, String language) {
		return (String)EXCEPTION_REASONS.get(getReasonTextKey(exceptionClass, reasonId, businessRole, language));
	}

	private String lookupReasonText(Class exceptionClass, int reasonId, String businessRole, String language) {
		String text = lookupReasonText0(exceptionClass, reasonId, businessRole, language);

		//----------------------------------------------------
		// omit businessRole from key
		//----------------------------------------------------
		if (text == null && businessRole != null) {
			text = lookupReasonText0(exceptionClass, reasonId, null, language);
		}

		//----------------------------------------------------
		// omit language from key
		//----------------------------------------------------
		if (text == null && language != null) {
			text = lookupReasonText0(exceptionClass, reasonId, businessRole, null);
		}

		//----------------------------------------------------
		// omit businessRole and language from key
		//----------------------------------------------------
		if (text == null && (businessRole != null || language != null)) {
			text = lookupReasonText0(exceptionClass, reasonId, null, null);
		}

		//----------------------------------------------------
		// try looking up reason on superclass
		//----------------------------------------------------
		if (text == null) {
			exceptionClass = exceptionClass.getSuperclass();
			if (exceptionClass != null && exceptionClass != Object.class) {
				text = lookupReasonText0(exceptionClass, reasonId, businessRole, language);
			}
		}

		//----------------------------------------------------
		// Use system text
		//----------------------------------------------------
		if (text == null) {
			text = getMessage();
		}

		return text;
	}

	//=====================================================================================================

	protected int getReason0() {
		return reason;
	}

	protected String getReasonText() {
		return getReasonText(null);
	}

	protected String getReasonTextFrench() {
		return getReasonTextFrench(null);
	}

	protected String getReasonText(String businessRole) {
		return lookupReasonText(getClass(), reason, businessRole, "en");
	}


	protected String getReasonTextFrench(String businessRole) {
		return lookupReasonText(getClass(), reason, businessRole, "fr");
	}

	//=====================================================================================================

	private static void setupReasonText(Class exceptionClass, int reason, String businessRole, String language, String text) {
		EXCEPTION_REASONS.put(getReasonTextKey(exceptionClass, reason, businessRole, language), text);
	}


	protected static void setReasonText(Class exceptionClass, int reason, String businessRole, String englishText, String frenchText) {
		setupReasonText(exceptionClass, reason, businessRole, "en", englishText);
		setupReasonText(exceptionClass, reason, businessRole, "fr", frenchText);
	}


	protected static void setReasonText(Class exceptionClass, int reason, String englishText, String frenchText) {
		setReasonText(exceptionClass, reason, null, englishText, frenchText);
	}

}


