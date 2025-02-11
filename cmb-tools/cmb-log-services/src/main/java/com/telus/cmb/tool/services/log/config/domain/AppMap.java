package com.telus.cmb.tool.services.log.config.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"name", "environment", "domain", "cluster", "node", "host"})
public class AppMap {

	private String name;
	private String environment;
	private String domain;
	private String cluster;
	private String node;
	private String host;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@XmlAttribute
	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	
	@XmlAttribute
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	@XmlAttribute
	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}
	
	@XmlAttribute
	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}
	
	@XmlAttribute
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
