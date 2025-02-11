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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.telus.cmsc.domain.artifact.ArtifactRuntimeInstance;
import com.telus.cmsc.domain.artifact.Environment;

/**
 * @author Pavel Simonovsky	
 *
 */

@Service
public class LoggingServiceImpl implements LoggingService {

	private static final Logger logger = LoggerFactory.getLogger(LoggingServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.modules.logging.service.LoggingService#getLogFiles(com.telus.cmbsc.modules.artifact.domain.ArtifactRuntimeInstance, com.telus.cmbsc.modules.environment.domain.Environment)
	 */
	@Override
	public Collection<File> getLogFiles(ArtifactRuntimeInstance runtimeInstance, Environment environment) {
		
		List<File> result = new ArrayList<File>();
		
		File file = new File(runtimeInstance.getLogFilePath(environment));
		if (file.exists() && file.isDirectory()) {
			File [] files = file.listFiles();
			result.addAll(Arrays.asList(files));

			logger.debug("Number of log files {}", files.length);
		}
		
		return result;
	}

}
