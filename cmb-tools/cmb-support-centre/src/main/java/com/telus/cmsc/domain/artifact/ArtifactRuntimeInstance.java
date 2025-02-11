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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;

/**
 * @author Pavel Simonovsky	
 *
 */
public class ArtifactRuntimeInstance {
	
	private Artifact artifact;
	
	private int instanceId;
	
	private String version;
	
	private String clusterName;
	
	private String domainName;
	
	private String nodeName;
	
	private String hostName;
	
	private int portNumber;
	
	private String adminHostName;
	
	private int adminPortNumber;
	
	private Date notificationTime;
	
	private ArtifactInstanceStatus status = ArtifactInstanceStatus.OK; 
	
	public ArtifactRuntimeInstance() {
	}
	
	public ArtifactRuntimeInstance(ArtifactNotification notification, Artifact artifact) {
		
		this.artifact = artifact;
		
		this.instanceId = notification.getNotificationId();
		this.adminHostName = notification.getAdminHostName();
		this.adminPortNumber = notification.getAdminPortNumber();
		this.clusterName = notification.getClusterName();
		this.domainName = notification.getDomainName();
		this.hostName = notification.getHostName();
		this.nodeName = notification.getNodeName();
		this.portNumber = notification.getPortNumber();
		this.version = notification.getVersion();
		this.notificationTime = notification.getTime();
	}

	
	public Artifact getArtifact() {
		return artifact;
	}

	public void setArtifact(Artifact artifact) {
		this.artifact = artifact;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public ArtifactInstanceStatus getStatus() {
		return status;
	}
	
	public Date getNotificationTime() {
		return notificationTime;
	}

	public void setNotificationTime(Date notificationTime) {
		this.notificationTime = notificationTime;
	}

	public void updateStatus(ReferenceVersion referenceVersion, long thresholdSeconds) {
		if (referenceVersion == null || StringUtils.isEmpty(referenceVersion.getVersion())) {
			status = ArtifactInstanceStatus.UNKNOWN_REFERENCE;
		} else if (!StringUtils.equalsIgnoreCase(version, referenceVersion.getVersion())) {
			status = ArtifactInstanceStatus.INCORRECT_VERSION; 
		}
	}
	
	public String getLogFilePath(Environment environment) {

		Map<String, String> values = new HashMap<String, String>();
		
		values.put("logRoot", "d:");
		values.put("hostName", hostName);
		values.put("nodeName", nodeName);
		
		return StrSubstitutor.replace(artifact.getLogPathPattern(), values);
	}
}
