package com.telus.cmb.tool.services.log.config.domain.task;

import javax.xml.bind.annotation.XmlAttribute;

public class NotifyParty {

	private String email;
	private Boolean onSuccess;
	private Boolean onFailure;

	@XmlAttribute
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlAttribute
	public Boolean getOnSuccess() {
		return onSuccess;
	}

	public void setOnSuccess(Boolean onSuccess) {
		this.onSuccess = onSuccess;
	}

	@XmlAttribute
	public Boolean getOnFailure() {
		return onFailure;
	}

	public void setOnFailure(Boolean onFailure) {
		this.onFailure = onFailure;
	}
	
	public boolean isEmailEligible(Boolean isSuccessful) {
				
		if (isSuccessful != null) {
			if (isSuccessful) {
				// If event is successful, only return emails that are configured to "onSuccess" or not configured
				return onSuccess == null || onSuccess;
			} else {
				// If event is unsuccessful, only return emails that are configured to "onFailure" or not configured
				return onFailure == null || onFailure;
			}
		}
		
		// If successful event is non-determinant, return true
		return true;
	}

}
