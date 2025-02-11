package com.telus.cmb.tool.services.log.config.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Environment {

	private String name;
	private String shortname;
	private String emValue;
	private String aliasname;
	private boolean production;
	private boolean current;
	private Ldap ldap;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	@XmlAttribute
	public String getEmValue() {
		return emValue;
	}

	public void setEmValue(String emValue) {
		this.emValue = emValue;
	}

	@XmlAttribute
	public String getAliasname() {
		return aliasname;
	}

	public void setAliasname(String aliasname) {
		this.aliasname = aliasname;
	}

	@XmlAttribute
	public boolean isProduction() {
		return production;
	}

	public void setProduction(boolean production) {
		this.production = production;
	}


	@XmlAttribute
	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	@XmlElement(name = "ldap")
	public Ldap getLdap() {
		return ldap;
	}

	public void setLdap(Ldap ldap) {
		this.ldap = ldap;
	}

}
