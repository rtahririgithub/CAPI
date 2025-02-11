package com.telus.cmb.common.dao.portalprofile;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Persona {
	
	public static final String ROLE_OWNER = "Owner";
	public static final String ROLE_MANAGER = "Manager";
	public static final String ROLE_MEMBER = "Member";
	
	@JsonProperty("personaId")
	private Long personaId;

	@JsonProperty("roleTxt")
	private String role;

	@JsonProperty("billingAccountNum")
	private String ban;

	@JsonProperty("statusTxt")
	private String status;

	@JsonProperty("phoneNumberTxt")
	private String phoneNumber;

	public Long getPersonaId() {
		return personaId;
	}

	public void setPersonaId(Long personaId) {
		this.personaId = personaId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getBan() {
		return ban;
	}

	public void setBan(String ban) {
		this.ban = ban;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isOwner() {
		return StringUtils.equalsIgnoreCase(role, ROLE_OWNER);
	}
	
	public boolean isManager() {
		return StringUtils.equalsIgnoreCase(role, ROLE_MANAGER);
	}
	
	public boolean isMember() {
		return StringUtils.equalsIgnoreCase(role, ROLE_MEMBER);
	}

	@Override
	public String toString() {
		return "Persona [personaId=" + personaId + ", role=" + role + ", ban="
				+ ban + ", status=" + status + ", phoneNumber=" + phoneNumber
				+ "]";
	}
	
}
