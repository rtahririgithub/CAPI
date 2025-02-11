/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.service;

import java.util.Collection;

import com.telus.cmsc.domain.artifact.Artifact;
import com.telus.cmsc.domain.artifact.ArtifactGroup;
import com.telus.cmsc.domain.artifact.ReferenceVersion;

/**
 * @author Pavel Simonovsky	
 *
 */
public interface ArtifactService {

	Collection<Artifact> getArtifacts();

	Artifact getArtifact(int artifactId);
	
	Artifact getArtifactByCode(String code);
	
	Artifact saveArtifact(Artifact artifact, Collection<Integer> groupIds);
	
	void deleteArtifact(int artifactId);
	
	Collection<ArtifactGroup> getGroups();
	
	ArtifactGroup getGroup(int groupId);
	
	ArtifactGroup saveGroup(ArtifactGroup group, Collection<Integer> artifactIds);
	
	void deleteGroup(int groupId);	
	
	Collection<Artifact> getGroupArtifacts(int groupId);
	
	Collection<ArtifactGroup> getArtifactGroups(int artifactId);
	
	void saveReferenceVersions(int artifactId, Collection<ReferenceVersion> versions);
	
	void saveReferenceVersion(ReferenceVersion version);
	
	ReferenceVersion getReferenceVersion(int artifactId, int environmentId);
	
}
