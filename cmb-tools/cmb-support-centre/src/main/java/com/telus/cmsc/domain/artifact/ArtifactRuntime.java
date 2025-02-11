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
import java.util.List;

/**
 * @author Pavel Simonovsky	
 *
 */
public class ArtifactRuntime {

	private String artifactCode;
	
	private Artifact artifact;
	
	private ReferenceVersion referenceVersion;
	
	private List<ArtifactRuntimeInstance> instances = new ArrayList<ArtifactRuntimeInstance>();
	
	public ArtifactRuntime(String artifactCode, Artifact artifact) {
		this.artifactCode = artifactCode;
		this.artifact = artifact;
	}
	
	public String getArtifactCode() {
		return artifactCode;
	}

	public Artifact getArtifact() {
		return artifact;
	}

	public List<ArtifactRuntimeInstance> getInstances() {
		return instances;
	}

	public void setInstances(List<ArtifactRuntimeInstance> instances) {
		this.instances = instances;
	}

	public void addInstance(ArtifactRuntimeInstance instance) {
		instances.add(instance);
	}
	
	public ArtifactRuntimeInstance getInstance(int instanceId) {
		for (ArtifactRuntimeInstance instance : instances) {
			if (instance.getInstanceId() == instanceId) {
				return instance;
			}
		}
		return null;
	}
	
	public ArtifactStatusSummary getStatusSummary() {
//		if (artifactCode.equals("cmb-provider")) {
//			System.out.println(instances);
//		}
		ArtifactStatusSummary summary = new ArtifactStatusSummary();
		for (ArtifactRuntimeInstance instance : instances) {
			if (artifact == null) {
				summary.setUnknownCount(summary.getUnknownCount() + 1);
			} else {
				summary.addStatus(instance.getStatus());
			}
		}
		return summary;
	}

	public ReferenceVersion getReferenceVersion() {
		return referenceVersion;
	}

	public void setReferenceVersion(ReferenceVersion referenceVersion) {
		this.referenceVersion = referenceVersion;
	}
	
}
