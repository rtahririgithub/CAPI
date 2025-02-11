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

import java.io.File;
import java.util.Collection;

import com.telus.cmsc.domain.artifact.ArtifactRuntimeInstance;
import com.telus.cmsc.domain.artifact.Environment;

/**
 * @author Pavel Simonovsky	
 *
 */
public interface LoggingService {

	Collection<File> getLogFiles(ArtifactRuntimeInstance runtimeInstance, Environment environment);
	
}
