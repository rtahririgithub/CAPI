package com.telus.eas.framework.exception;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author tongts
 *
 */
public class FaultInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String messageId;
	private String errorCode;
	private String errorMessage;
	private List variables; // List of strings
	
	public FaultInfo(String messageId, String errorCode, String errorMessage, List variables) {
		this.messageId = messageId;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.variables = variables;
	}
	
	public String getMessageId() {
		return messageId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public List getVariables() {
		return variables;
	}
}
