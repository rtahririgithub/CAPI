package com.telus.cmb.tool.services.log.config.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class LogServer {

	private String name;
	private String shortname;
	private String host;
	private String passwordType;
	private boolean production;
	private boolean defaultLogServer;
	private List<String> mappedDomains;
	private List<String> mappedHosts;

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
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@XmlAttribute
	public String getPasswordType() {
		return passwordType;
	}

	public void setPasswordType(String passwordType) {
		this.passwordType = passwordType;
	}

	@XmlAttribute
	public boolean isProduction() {
		return production;
	}

	public void setProduction(boolean production) {
		this.production = production;
	}

	@XmlAttribute(name = "default")
	public boolean isDefaultLogServer() {
		return defaultLogServer;
	}

	public void setDefaultLogServer(boolean defaultLogServer) {
		this.defaultLogServer = defaultLogServer;
	}

	@XmlElement(name = "mappedDomain")
	public List<String> getMappedDomains() {
		return mappedDomains;
	}

	public void setMappedDomains(List<String> mappedDomains) {
		this.mappedDomains = mappedDomains;
	}

	@XmlElement(name = "mappedHost")
	public List<String> getMappedHosts() {
		return mappedHosts;
	}

	public void setMappedHosts(List<String> mappedHosts) {
		this.mappedHosts = mappedHosts;
	}

}
