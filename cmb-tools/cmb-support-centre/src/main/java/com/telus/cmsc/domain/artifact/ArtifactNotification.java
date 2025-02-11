/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.domain.artifact;

import java.util.Date;

/**
 * @author Pavel Simonovsky	
 *
 */
public class ArtifactNotification {

	private int notificationId;
	
	private String artifactCode;
	
	private String version;
	
	private String environmentCode;
	
	private String clusterName;
	
	private String domainName;
	
	private String nodeName;
	
	private String hostName;
	
	private int portNumber;
	
	private String adminHostName;
	
	private int adminPortNumber;

	private Date time;
	
	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getArtifactCode() {
		return artifactCode;
	}

	public void setArtifactCode(String artifactCode) {
		this.artifactCode = artifactCode;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getEnvironmentCode() {
		return environmentCode;
	}

	public void setEnvironmentCode(String environmentCode) {
		this.environmentCode = environmentCode;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public String getAdminHostName() {
		return adminHostName;
	}

	public void setAdminHostName(String adminHostName) {
		this.adminHostName = adminHostName;
	}

	public int getAdminPortNumber() {
		return adminPortNumber;
	}

	public void setAdminPortNumber(int adminPortNumber) {
		this.adminPortNumber = adminPortNumber;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
}
