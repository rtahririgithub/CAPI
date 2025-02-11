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

import org.apache.commons.lang.StringUtils;

import com.telus.cmsc.domain.BaseEntity;


/**
 * @author Pavel Simonovsky	
 *
 */
public class ReferenceVersion extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer referenceVersionId;
	
	private Integer artifactId;

	private Integer environmentId;
	
	private String version;
	
	private String notes;
	
	@Override
	public Integer getId() {
		return referenceVersionId;
	}

	public Integer getReferenceVersionId() {
		return referenceVersionId;
	}

	public void setReferenceVersionId(Integer referenceVersionId) {
		this.referenceVersionId = referenceVersionId;
	}

	public Integer getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(Integer artifactId) {
		this.artifactId = artifactId;
	}

	public Integer getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(Integer environmentId) {
		this.environmentId = environmentId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean matches(String version) {
		return StringUtils.equalsIgnoreCase(this.version, version);
	}
}
