package com.telus.cmb.tool.services.log.domain;

public class LdifSearchResult {

	private String message;
	private String ldapDirectory;
	private String attribute;
	private String value;

	public LdifSearchResult(String message, String ldapDirectory, String attribute, String value) {
		this.message = message;
		this.ldapDirectory = ldapDirectory;
		this.attribute = attribute;
		this.value = value;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLdapDirectory() {
		return ldapDirectory;
	}

	public void setLdapDirectory(String ldapDirectory) {
		this.ldapDirectory = ldapDirectory;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
