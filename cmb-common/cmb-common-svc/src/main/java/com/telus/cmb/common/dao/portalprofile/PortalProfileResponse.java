package com.telus.cmb.common.dao.portalprofile;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PortalProfileResponse {

	@JsonProperty("userPersonaList")
	private List<PortalProfile> portalProfileList;

	public List<PortalProfile> getPortalProfileList() {
		return portalProfileList;
	}

	public void setPortalProfileList(List<PortalProfile> portalProfileList) {
		this.portalProfileList = portalProfileList;
	}

}
