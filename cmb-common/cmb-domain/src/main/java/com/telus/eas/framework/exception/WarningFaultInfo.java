package com.telus.eas.framework.exception;

import java.util.List;

public class WarningFaultInfo extends FaultInfo {
	final static public String APPLICATION_EXCEPTION = "ApplicationException";
	final static public String SYSTEM_EXCEPTION = "SystemException";
	private String warningType;
	private String systemCode;
	
	public WarningFaultInfo(String warningType, String systemCode, String messageId, String errorCode, String errorMessage, List variables) {
		super(messageId, errorCode, errorMessage, variables);
		this.warningType = warningType;
		this.systemCode = systemCode;
	}

	public String getWarningType() {
		return warningType;
	}

	public void setWarningType(String warningType) {
		this.warningType = warningType;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	
	

}
