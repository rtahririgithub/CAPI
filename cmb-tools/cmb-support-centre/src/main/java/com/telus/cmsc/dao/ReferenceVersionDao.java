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

import com.telus.cmsc.domain.artifact.ReferenceVersion;

/**
 * @author Pavel Simonovsky	
 *
 */
public interface ReferenceVersionDao {

	void persistArtifactVersions(int artifactId, Collection<ReferenceVersion> versions);
	
	ReferenceVersion persistVersion(ReferenceVersion version);
	
	ReferenceVersion findVersion(int artifactId, int environmentId);
	
	void deleteArtifactVersions(int artifactId);

	void deleteEnvironmentVersions(int environmentId);
	
}
