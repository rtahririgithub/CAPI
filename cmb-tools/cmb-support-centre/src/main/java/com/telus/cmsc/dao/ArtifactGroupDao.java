/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.dao;

import java.util.Collection;
import java.util.List;

import com.telus.cmsc.domain.artifact.ArtifactGroup;

/**
 * @author Pavel Simonovsky	
 *
 */
public interface ArtifactGroupDao {

	List<ArtifactGroup> findGroups();
	
	ArtifactGroup findGroup(int groupId);
	
	ArtifactGroup createGroup(ArtifactGroup group);

	ArtifactGroup updateGroup(ArtifactGroup group);
	
	void deleteGroup(ArtifactGroup artifact);	

	List<Integer> getGroupArtifactIds(int groupId);
	
	void setGroupArtifacts(int groupId, Collection<Integer> artifactIds);

	List<Integer> getArtifactGroupIds(int artifactId);

	void setArtifactGroups(int artifactId, Collection<Integer> groupIds);
	
}
