package com.telus.eas.framework.info;

/**
 * Title: ExceptionInfo
 * <p>
 * Description: The ExceptionInfo holds attributes about an exception.
 * <p>
 * Copyright: Copyright (c) Peter Frei
 * <p>
 * Company: Telus Mobility Inc
 * <p>
 * 
 * @author Peter Frei
 * @version 1.0
 */

public class ExceptionInfo extends Info {

	static final long serialVersionUID = 1L;

	public static final byte SEVERITY_ERROR = (byte) 'E';
	public static final byte SEVERITY_WARNING = (byte) 'W';
	public static final byte SEVERITY_INFORMATION = (byte) 'I';

	private String id = "";
	private String message = "";
	private Throwable exception = null;
	private byte severity;

	public ExceptionInfo() {
	}

	public ExceptionInfo(String pID, String pMessage) {
		setId(pID);
		setMessage(pMessage);
	}

	public ExceptionInfo(String pID, String pMessage, byte pSeverity) {
		setId(pID);
		setMessage(pMessage);
		setSeverity(pSeverity);
	}

	public ExceptionInfo(String pID, String pMessage, byte pSeverity, Throwable pException) {
		setId(pID);
		setMessage(pMessage);
		setSeverity(pSeverity);
		setException(pException);
	}

	public ExceptionInfo(String pID, String pMessage, Throwable pException) {
		setId(pID);
		setMessage(pMessage);
		setException(pException);
	}

	public ExceptionInfo(String pID, String pMessage, ExceptionInfo[] exceptionInfoArray, Throwable pException) {
		StringBuffer message = new StringBuffer();

		message.append(pMessage);
		message.append("\n");
		message.append("Failure Details:");
		for (int i = 0; i < exceptionInfoArray.length; i++) {
			message.append("\n");
			message.append("  Message " + (i + 1) + ": " + exceptionInfoArray[i].getMessage());
		}
		setId(pID);
		setMessage(message.toString());
		setException(pException);
	}

	public String getId() {
		return id;
	}

	public void setId(String newId) {
		id = newId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String newMessage) {
		message = newMessage;
	}

	public void setException(Throwable newException) {
		exception = newException;
	}

	public Throwable getException() {
		return exception;
	}

	public void setSeverity(byte severity) {
		this.severity = severity;
	}

	public byte getSeverity() {
		return severity;
	}
}