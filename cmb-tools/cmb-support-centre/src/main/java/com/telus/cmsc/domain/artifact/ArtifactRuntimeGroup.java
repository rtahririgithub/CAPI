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
public class ArtifactRuntimeGroup {

	private ArtifactGroup group;
	
	private List<ArtifactRuntime> runtimes = new ArrayList<ArtifactRuntime>();

	public ArtifactRuntimeGroup(ArtifactGroup group) {
		this.group = group;
	}
	
	public ArtifactGroup getGroup() {
		return group;
	}

	public void setGroup(ArtifactGroup group) {
		this.group = group;
	}

	public List<ArtifactRuntime> getRuntimes() {
		return runtimes;
	}

	public void setRuntimes(List<ArtifactRuntime> runtimes) {
		this.runtimes = runtimes;
	}
	
	public void addRuntime(ArtifactRuntime runtime) {
		if (runtime != null) {
			runtimes.add(runtime);
		}
	}
}
