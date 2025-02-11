package com.telus.cmb.tool.services.log.config.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.telus.cmb.tool.services.log.domain.LogServerInfo;

public class LogServerCache {

	private Map<String, LogServerInfo> logServerMap;
	private List<LogServerInfo> logServers;
	private LogServerInfo defaultLogServer;
	private LogServerInfo defaultPRLogServer;

	public void setLogServers(Map<String, LogServerInfo> logServerMap) {
		this.logServerMap = logServerMap;
		this.logServers = new ArrayList<LogServerInfo>(logServerMap.values());
	}
	
	public List<LogServerInfo> getLogServers() {
		return logServers;
	}
	
	public LogServerInfo getLogServer(String shortname) {
		return logServerMap.get(shortname);
	}

	public LogServerInfo getDefaultLogServer() {
		return defaultLogServer;
	}

	public void setDefaultLogServer(LogServerInfo defaultLogServer) {
		this.defaultLogServer = defaultLogServer;
	}

	public LogServerInfo getDefaultPRLogServer() {
		return defaultPRLogServer;
	}

	public void setDefaultPRLogServer(LogServerInfo defaultPRLogServer) {
		this.defaultPRLogServer = defaultPRLogServer;
	}

}
