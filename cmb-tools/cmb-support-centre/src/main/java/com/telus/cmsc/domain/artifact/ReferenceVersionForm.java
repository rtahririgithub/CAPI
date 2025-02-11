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
import java.util.List;

/**
 * @author Pavel Simonovsky	
 *
 */
public class ReferenceVersionForm {
	
	private Artifact artifact;
	
	private int artifactId;
	
	private Collection<Artifact> artifacts;

	private List<ReferenceVersionEntry> entries = new ArrayList<ReferenceVersionEntry>();

	public List<ReferenceVersionEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ReferenceVersionEntry> entries) {
		this.entries = entries;
	}

	public Artifact getArtifact() {
		return artifact;
	}

	public void setArtifact(Artifact artifact) {
		this.artifact = artifact;
	}

	public Collection<Artifact> getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(Collection<Artifact> artifacts) {
		this.artifacts = artifacts;
	}

	public int getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(int artifactId) {
		this.artifactId = artifactId;
	}
	
}
