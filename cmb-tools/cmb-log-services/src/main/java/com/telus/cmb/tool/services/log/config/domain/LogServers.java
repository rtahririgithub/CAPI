package com.telus.cmb.tool.services.log.config.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LogServers {

	private List<LogServer> logServers;

	@XmlElement(name = "logServer")
	public List<LogServer> getLogServers() {
		return logServers;
	}

	public void setLogServers(List<LogServer> logServers) {
		this.logServers = logServers;
	}

}
