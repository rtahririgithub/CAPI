package com.telus.cmb.common.dao.portalprofile;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PortalProfile {

	public static final String STATUS_ACTIVE = "Active";
	public static final String STATUS_PENDING = "Pending";
	
	@JsonProperty("uuid")
	private String uuid;

	@JsonProperty("uuidEmail")
	private String uuidEmail;

	@JsonProperty("uuidFirstNameTxt")
	private String uuidFirstName;

	@JsonProperty("uuidLastNameTxt")
	private String uuidLastName;

	@JsonProperty("uuidStatusTxt")
	private String uuidStatus;

	@JsonProperty("personaList")
	private List<Persona> personaList;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuidEmail() {
		return uuidEmail;
	}

	public void setUuidEmail(String uuidEmail) {
		this.uuidEmail = uuidEmail;
	}

	public String getUuidFirstName() {
		return uuidFirstName;
	}

	public void setUuidFirstName(String uuidFirstName) {
		this.uuidFirstName = uuidFirstName;
	}

	public String getUuidLastName() {
		return uuidLastName;
	}

	public void setUuidLastName(String uuidLastName) {
		this.uuidLastName = uuidLastName;
	}

	public String getUuidStatus() {
		return uuidStatus;
	}

	public void setUuidStatus(String uuidStatus) {
		this.uuidStatus = uuidStatus;
	}

	public List<Persona> getPersonaList() {
		return personaList;
	}

	public void setPersonaList(List<Persona> personaList) {
		this.personaList = personaList;
	}
	
	public boolean isActive() {
		return StringUtils.equalsIgnoreCase(uuidStatus, STATUS_ACTIVE);
	}
	
	public boolean hasMatchingPersonaToBan(String ban) {
		
		if (CollectionUtils.isNotEmpty(personaList)) {
			for (Persona persona : personaList) {
				if (StringUtils.equals(persona.getBan(), ban)) {
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "PortalProfile [uuid=" + uuid + ", uuidEmail=" + uuidEmail + ", uuidFirstName=" + uuidFirstName + ", uuidLastName=" + uuidLastName + ", uuidStatus=" + uuidStatus
				+ ", personaList=" + personaList + "]";
	}

}
