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
public class ArtifactGroup extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer groupId;

	private String name;
	
	private String description;
	
	
	@Override
	public Integer getId() {
		return groupId;
	}
	
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
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

	
}
