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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmsc.domain.artifact.Artifact;
import com.telus.cmsc.domain.artifact.ArtifactNotification;
import com.telus.cmsc.domain.artifact.ArtifactRuntime;
import com.telus.cmsc.domain.artifact.ArtifactRuntimeInstance;
import com.telus.cmsc.domain.artifact.Environment;
import com.telus.cmsc.domain.artifact.EnvironmentRuntime;

/**
 * @author Pavel Simonovsky	
 *
 */
public class EnvironmentRuntimeBuilder {

	private static final Logger logger = LoggerFactory.getLogger(EnvironmentRuntimeBuilder.class);
	
	@Autowired
	private EnvironmentService environmentService;
	
	@Autowired
	private ArtifactService artifactService;
	
	public List<EnvironmentRuntime> buildRuntimes(Collection<ArtifactNotification> notifications, boolean filterStandaloneArtifacts) {
		
		logger.debug("Building runtimes...");
		
		List<EnvironmentRuntime> result = new ArrayList<EnvironmentRuntime>();

		Map<String, EnvironmentRuntime> environmentRuntimes = new HashMap<String, EnvironmentRuntime>();
		
		for (ArtifactNotification notification : notifications) {
			String environmentCode = notification.getEnvironmentCode();
			if (filterStandaloneArtifacts && (StringUtils.isEmpty(notification.getClusterName()) || StringUtils.equalsIgnoreCase("N/A", notification.getClusterName()))) {
				continue;
			}
			EnvironmentRuntime environmentRuntime = getEnvironmentRuntime(environmentCode, environmentRuntimes);
			if (environmentRuntime != null) {
				String artifactCode = notification.getArtifactCode();
				Artifact artifact = artifactService.getArtifactByCode(artifactCode);
				ArtifactRuntime artifactRuntime = environmentRuntime.getArtifactRuntime(artifactCode);
				if (artifactRuntime == null) {
					artifactRuntime = new ArtifactRuntime(artifactCode, artifact);
					if (artifact != null) {
						artifactRuntime.setReferenceVersion(artifactService.getReferenceVersion(artifact.getArtifactId(), environmentRuntime.getEnvironment().getEnvironmentId()));
					}
					environmentRuntime.addArtifactRuntime(artifactRuntime);
				}
				ArtifactRuntimeInstance instance = new ArtifactRuntimeInstance(notification, artifact);
				instance.updateStatus(artifactRuntime.getReferenceVersion(), 300);
				artifactRuntime.addInstance(instance);
			}
		}
		
		for (EnvironmentRuntime environmentRuntime : environmentRuntimes.values()) {
			result.add(environmentRuntime);
		}
		
		logger.debug("Created [{}] environment runtimes", result.size());
			
		return result;
	}
	
	private EnvironmentRuntime getEnvironmentRuntime(String code, Map<String, EnvironmentRuntime> environmentRuntimes) {
		//logger.debug("Code = {}", code);
		EnvironmentRuntime runtime = environmentRuntimes.get(code);
		if (runtime == null) {
			Environment environment = environmentService.getEnvironmentByCode(code);
			if (environment != null) {
				runtime = new EnvironmentRuntime(environment);
				environmentRuntimes.put(code, runtime);
			} else {
				//logger.warn("Ignoring unknown environment code = [{}]", code);
			}
		}
		return runtime;
	}
}
