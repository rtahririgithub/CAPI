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

import com.telus.cmsc.domain.BaseEntity;


/**
 * @author Pavel Simonovsky	
 *
 */
public class Artifact extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer artifactId;

	private String code;
	
	private String logPathPattern;
	
	private String name;
	
	private String description;
	
	@Override
	public Integer getId() {
		return artifactId;
	}

	public Integer getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(Integer artifactId) {
		this.artifactId = artifactId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogPathPattern() {
		return logPathPattern;
	}

	public void setLogPathPattern(String logPathPattern) {
		this.logPathPattern = logPathPattern;
	}
	
}
