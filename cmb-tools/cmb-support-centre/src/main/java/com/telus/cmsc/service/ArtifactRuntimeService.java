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

import com.telus.cmsc.domain.artifact.EnvironmentRuntime;

/**
 * @author Pavel Simonovsky	
 *
 */
public interface ArtifactRuntimeService {

	EnvironmentRuntime getEnvironmentRuntime(int environmentId, boolean filterStandaloneArtifacts);
	
	Collection<EnvironmentRuntime> getEnvironmentRuntimes(boolean filterStandaloneArtifacts);
	
}
