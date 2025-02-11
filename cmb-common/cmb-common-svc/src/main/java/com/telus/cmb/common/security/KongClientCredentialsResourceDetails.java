package com.telus.cmb.common.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

/**
 * Responsible for keeping OAuth2 related config values.
 * 
 * Also, Kong-specific environment value gets set by this class 
 * and will be injected through Http Header when OAuth2RestTemplate is executed.
 *
 */
public class KongClientCredentialsResourceDetails extends ClientCredentialsResourceDetails {

	/** Represents kong-gateway environment matching for LDAP environment value */
	private String kongEnv = "dv";
	
	public KongClientCredentialsResourceDetails() {
		super();
	}
	
	public String getKongEnv() {
		return this.kongEnv;
	}
	
	public void setKongEnv(String env) {
		if (StringUtils.isBlank(env)) {
			return;
		}
		if (env.startsWith("dv")) {
			this.kongEnv = "dv";
		} else if (env.equalsIgnoreCase("pt168")) {
			this.kongEnv = "it01";
		} else if (env.equalsIgnoreCase("pt148")) {
			this.kongEnv = "it02";
		} else if (env.equalsIgnoreCase("pt140")) {
			this.kongEnv = "it03";
		} else if (env.equalsIgnoreCase("st101")) {
			this.kongEnv = "it04";
		} else if (env.equalsIgnoreCase("pr")) {
			this.kongEnv = "pr";
		} 
	}
	
}
