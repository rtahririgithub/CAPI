package com.telus.eas.account.info;

import com.telus.eas.framework.info.Info;

public class CustomerNotificationPreferenceInfo extends Info {
	
	static final long serialVersionUID = 1L;
	
	private String updateModeCode;
	private String code;
	private String description;
	
	public String getUpdateModeCode() {
		return updateModeCode;
	}
	public void setUpdateModeCode(String updateModeCode) {
		this.updateModeCode = updateModeCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
