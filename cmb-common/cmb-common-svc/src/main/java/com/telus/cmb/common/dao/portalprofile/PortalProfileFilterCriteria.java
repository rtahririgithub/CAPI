package com.telus.cmb.common.dao.portalprofile;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class PortalProfileFilterCriteria {

	private String subscriberId;
	private String status;
	private String email;

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<String, String> convertToQueryMap() {

		Map<String, String> queryParams = new HashMap<String, String>();
		if (StringUtils.isNotBlank(email)) {
			queryParams.put("email", email);
		}
		if (StringUtils.isNotBlank(status)) {
			queryParams.put("status", status);
		}
		if (StringUtils.isNotBlank(subscriberId)) {
			queryParams.put("sub", subscriberId);
		}

		return queryParams;
	}

}
