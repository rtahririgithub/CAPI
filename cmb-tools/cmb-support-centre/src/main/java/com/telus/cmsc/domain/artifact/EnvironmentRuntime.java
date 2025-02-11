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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telus.cmsc.service.ArtifactService;

/**
 * @author Pavel Simonovsky	
 *
 */
public class EnvironmentRuntime {
	
	private Environment environment;
	
	private Map<String, ArtifactRuntime> artifactRuntimes = new HashMap<String, ArtifactRuntime>();  
	
	public EnvironmentRuntime(Environment environment) {
		this.environment = environment;
	}
	
	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Collection<ArtifactRuntime> getArtifactRuntimes() {
		return artifactRuntimes.values();
	}

	public ArtifactRuntime getArtifactRuntime(String artifactCode) {
		return artifactRuntimes.get(artifactCode);
	}

	public void setArtifactRuntimes(Map<String, ArtifactRuntime> artifactRuntimes) {
		this.artifactRuntimes = artifactRuntimes;
	}
	
	public void addArtifactRuntime(ArtifactRuntime runtime) {
		artifactRuntimes.put(runtime.getArtifactCode(), runtime);
	}
	
	public ArtifactStatusSummary getArtifactStatusSummary() {
		ArtifactStatusSummary result = new ArtifactStatusSummary();
		for (ArtifactRuntime artifactRuntime : artifactRuntimes.values()) {
			result.addSummary(artifactRuntime.getStatusSummary());
		}
		
		System.out.println(environment.getName() + " status: " + result);
		
		return result;
	}
	
	public List<ArtifactRuntimeGroup> getRuntimeGroups(ArtifactService artifactService) {
		List<ArtifactRuntimeGroup> result = new ArrayList<ArtifactRuntimeGroup>();
		for (ArtifactGroup group : artifactService.getGroups()) {
			result.add(getRuntimeGroup(group, artifactService));
		}
		return result;
	}
	
	public ArtifactRuntimeGroup getRuntimeGroup(ArtifactGroup group, ArtifactService artifactService) {
		ArtifactRuntimeGroup runtimeGroup = new ArtifactRuntimeGroup(group);
		
		for (Artifact artifact : artifactService.getGroupArtifacts(group.getGroupId())) {
			runtimeGroup.addRuntime(artifactRuntimes.get(artifact.getCode()));
		}
		
		System.out.println(group.getName() + " = " + runtimeGroup.getRuntimes().size());
		
		return runtimeGroup;
	}
	
	public List<ArtifactRuntime> getUnregisteredArtifactRuntimes() {
		List<ArtifactRuntime> result = new ArrayList<ArtifactRuntime>();
		for (ArtifactRuntime runtime : artifactRuntimes.values()) {
			if (runtime.getArtifact() == null) {
				result.add(runtime);
			}
		}
		return result;
	}
	
}
 