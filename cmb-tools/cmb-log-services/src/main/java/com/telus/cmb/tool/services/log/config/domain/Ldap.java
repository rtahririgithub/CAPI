package com.telus.cmb.tool.services.log.config.domain;

import javax.xml.bind.annotation.XmlAttribute;

public class Ldap {

	private String url;
	private String principal;
	private String credential;
	private boolean flipper;

	@XmlAttribute
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@XmlAttribute
	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	@XmlAttribute
	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	@XmlAttribute
	public boolean isFlipper() {
		return flipper;
	}

	public void setFlipper(boolean flipper) {
		this.flipper = flipper;
	}

}
