package com.telus.api;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

public class ApplicationException extends Exception {

	private static final long serialVersionUID = 4494090506808388608L;

	private String systemCode;
	private String errorCode;
	private String errorMessage;
	private String errorMessageFr;
	private ApplicationException nestedException;
	private String subsystemStackTrace;
	private String subsystemMessage;

	public ApplicationException(String systemCode, String errorMessage, String errorMessageFr) {
		super(errorMessage);
		this.systemCode = systemCode;
		this.errorMessage = errorMessage;
		this.errorMessageFr = errorMessageFr;
	}

	public ApplicationException(String systemCode, String errorCode, String errorMessage, String errorMessageFr) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.systemCode = systemCode;
		this.errorMessage = errorMessage;
		this.errorMessageFr = errorMessageFr;
	}

	public ApplicationException(String systemCode, String errorMessage, String errorMessageFr, Throwable t) {
		
		super(errorMessage);
		this.systemCode = systemCode;
		this.errorMessage = errorMessage;
		this.errorMessageFr = errorMessageFr;

		if (t != null) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(os);
			t.printStackTrace(ps);
			this.subsystemStackTrace = os.toString();
			this.subsystemMessage = t.getMessage();
		}
	}

	public ApplicationException(String systemCode, String errorCode, String errorMessage, String errorMessageFr, Throwable t) {
		
		super(errorMessage);
		this.errorCode = errorCode;
		this.systemCode = systemCode;
		this.errorMessage = errorMessage;
		this.errorMessageFr = errorMessageFr;

		if (t != null) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(os);
			t.printStackTrace(ps);
			this.subsystemStackTrace = os.toString();
			this.subsystemMessage = t.getMessage();
		}
	}

	public ApplicationException(String systemCode, String errorCode, String errorMessage, String errorMessageFr, ApplicationException nestedException) {
		
		super(errorMessage);
		this.errorCode = errorCode;
		this.systemCode = systemCode;
		this.errorMessage = errorMessage;
		this.errorMessageFr = errorMessageFr;
		this.nestedException = nestedException;
		
		if (nestedException != null) {
			this.subsystemMessage = nestedException.getMessage();
		}
	}

	public ApplicationException(String systemCode, String errorMessage, String errorMessageFr, ApplicationException nestedException) {
		
		super(errorMessage);
		this.systemCode = systemCode;
		this.errorMessage = errorMessage;
		this.errorMessageFr = errorMessageFr;
		this.nestedException = nestedException;
		if (nestedException != null) {
			this.subsystemMessage = nestedException.getMessage();
		}
	}

	public void printStackTrace(PrintStream s) {
		
		s.println(constructMessage());
		super.printStackTrace(s);
		
		if (this.nestedException != null) {
			this.nestedException.printStackTrace(s);
		}		
		if (this.subsystemStackTrace != null && this.subsystemStackTrace.length() > 0) {
			s.println("\t Caused By:");
			s.println(this.subsystemStackTrace);
		}
	}

	public void printStackTrace(PrintWriter s) {
		
		s.println(constructMessage());
		super.printStackTrace(s);
		
		if (this.nestedException != null) {
			this.nestedException.printStackTrace(s);
		}		
		if (this.subsystemStackTrace != null && this.subsystemStackTrace.length() > 0) {
			s.println("\t Caused By:");
			s.println(this.subsystemStackTrace);
		}
	}

	private String constructMessage() {
		
		StringBuffer sb = new StringBuffer();
		
		if ((this.systemCode != null && this.systemCode.length() > 0) || (this.errorMessage != null && this.errorMessage.length() > 0)
				|| (this.errorMessageFr != null && this.errorMessageFr.length() > 0) || (this.errorCode != null && this.errorCode.length() > 0)) {
			sb.append("System\n");
			sb.append("------\n");
		}
		if (this.systemCode != null && this.systemCode.length() > 0) {
			sb.append("\tCode: " + this.systemCode + "\n");
		}
		if (this.errorCode != null && this.errorCode.length() > 0) {
			sb.append("\tError Code: " + this.errorCode + "\n");
		}
		if (this.errorMessage != null && this.errorMessage.length() > 0) {
			sb.append("\tError Message: " + this.errorMessage + "\n");
		}
		if (this.errorMessageFr != null && this.errorMessageFr.length() > 0) {
			sb.append("\tError Message French: " + this.errorMessageFr + "\n");
		}

		return sb.toString();
	}

	public String getSystemCode() {
		return systemCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorMessageFr() {
		return errorMessageFr;
	}

	public ApplicationException getNestedException() {
		return nestedException;
	}

	/**
	 * The sub-system stacktrace converted to a String.
	 * Throwable.initCause cannot be used because we cannot guarantee that
	 * there is a reference to the sub-system exception class.
	 * 
	 * @return The sub-system stacktrace
	 */
	public String getSubsystemStackTrace() {
		return subsystemStackTrace;
	}

	public String getStackTraceAsString() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(os);
		printStackTrace(ps);
		return os.toString();
	}

	public String getErrorCode() {
		return (errorCode == null) ? "" : errorCode;
	}

	/**
	 * Return the nested exception, including the top level, that matches the systemCode. If there's no match, a null is returned.
	 * If there're multiple nested exceptions with the same code, the top most level that matches will be returned.
	 *  
	 * @param systemCode
	 * @return ApplicationException
	 */
	public ApplicationException getNestedExceptionBySystemCode(String systemCode) {
		
		if (systemCode == null) {
			return null;
		}
		if (systemCode.equals(this.systemCode)) {
			return this;
		} else if (nestedException != null) {
			return nestedException.getNestedExceptionBySystemCode(systemCode);
		}

		return null;
	}

	/**
	 * The exception's getMessage() value of the underlying sub-system exception. This method
	 * never returns null. 
	 * 
	 * @return Whatever was returned from the getMessage() call on the sub-system exception
	 */
	public String getSubsystemMessage() {
		return subsystemMessage == null ? "" : subsystemMessage;
	}
	
}