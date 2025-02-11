package com.telus.cmb.shakedown.info;

import java.io.Serializable;

public class ServerInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String clusterName;
	private String nodeName;
	private String serverAddress;
	private int serverPort;
	private String nodeUrl;
	private String domainName;


	public ServerInfo(String clusterName, String nodeName, String serverAddress, int serverPort) {
		this.clusterName = clusterName;
		this.nodeName = nodeName;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}
	public String getServerAddress() {
		return serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}
	public String getNodeName() {
		return nodeName;
	}
	public String getClusterName() {
		return clusterName;
	}
	public String getNodeUrl(boolean toConsole) {
		if (nodeUrl == null) {
			String protocol = "t3://";
			
			if (toConsole) {
				protocol = "t3s://";
			}
			nodeUrl = protocol + getServerAddress() + ":" + getServerPort();
		}
		
		return nodeUrl;
	}
	public void setNodeUrl(String nodeUrl) {
		this.nodeUrl = nodeUrl;
	}
	@Override
	public String toString() {
		return "ServerInfo [ domainName = "+domainName +", clusterName=" + clusterName + ", nodeName=" + nodeName + ", serverAddress=" + serverAddress + ", serverPort=" + serverPort + "]";
	}
	
	public String getDomainName() {
		if (domainName != null) {
			return domainName;
		}else {
			return "UNKNOWNDOMAIN";
		}
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
}