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

import java.util.List;

import com.telus.cmsc.domain.artifact.Artifact;

/**
 * @author Pavel Simonovsky	
 *
 */
public interface ArtifactDao {

	List<Artifact> findArtifacts();
	
	Artifact findArtifact(Integer artifactId);

	Artifact findArtifactByCode(String artifactCode);
	
	Artifact createArtifact(Artifact artifact);

	Artifact updateArtifact(Artifact artifact);
	
	void deleteArtifact(Artifact artifact);
	
}
