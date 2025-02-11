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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.telus.cmsc.dao.ArtifactNotificationDao;
import com.telus.cmsc.domain.artifact.ArtifactNotification;
import com.telus.cmsc.domain.artifact.EnvironmentRuntime;

/**
 * @author Pavel Simonovsky	
 *
 */
@Service
public class ArtifactRuntimeServiceImpl implements ArtifactRuntimeService {

	private static final Logger logger = LoggerFactory.getLogger(ArtifactRuntimeServiceImpl.class);
	
	@Autowired @Qualifier("developmentNotificationDao")
	private ArtifactNotificationDao developmentNotificationDao;

	@Autowired @Qualifier("stagingNotificationDao")
	private ArtifactNotificationDao stagingNotificationDao;
	
	@Autowired @Qualifier("productionNotificationDao")
	private ArtifactNotificationDao productionNotificationDao;
	
	@Autowired
	private ArtifactService artifactService;

	@Autowired
	private ExecutorService executorService;
	
	@Autowired
	private EnvironmentRuntimeBuilder environmentRuntimeBuilder;
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.ArtifactRuntimeService#getEnvironmentRuntime(int)
	 */
	@Override
	public EnvironmentRuntime getEnvironmentRuntime(int environmentId, boolean filterStandaloneArtifacts) {
		for (EnvironmentRuntime runtime : getEnvironmentRuntimes(filterStandaloneArtifacts)) {
			if (runtime.getEnvironment().getId() == environmentId) {
				return runtime;
			}
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmbsc.domain.service.ArtifactRuntimeService#getEnvironmentRuntimes()
	 */
	@Override
	public Collection<EnvironmentRuntime> getEnvironmentRuntimes(boolean filterStandaloneArtifacts) {
		logger.debug("Loading artifact notifications...");
		
		Collection<ArtifactNotification> notifications = getArtifactNotifications();
		
		logger.debug("Loaded [{}] notifications", notifications.size());
		
		return environmentRuntimeBuilder.buildRuntimes(notifications, filterStandaloneArtifacts);
	}
	
	
	private Collection<ArtifactNotification> getArtifactNotifications() {

		List<RuntimeInstanceRetrievalTask> tasks = new ArrayList<RuntimeInstanceRetrievalTask>();
		
		tasks.add( new RuntimeInstanceRetrievalTask(developmentNotificationDao, "development"));
		tasks.add( new RuntimeInstanceRetrievalTask(stagingNotificationDao, "staging"));
		tasks.add( new RuntimeInstanceRetrievalTask(productionNotificationDao, "production"));
		
		List<Future<?>> futures = new ArrayList<Future<?>>();
		
		for (RuntimeInstanceRetrievalTask task : tasks) {
			futures.add(executorService.submit(task));
		}
		
		try {
			for (Future<?> future : futures) {
				future.get();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<ArtifactNotification> notifications = new ArrayList<ArtifactNotification>();
		
		for (RuntimeInstanceRetrievalTask task : tasks) {
			notifications.addAll(task.getNotifications());
		}
		
		return notifications;
	}
	

	private class RuntimeInstanceRetrievalTask implements Runnable {
		
		private ArtifactNotificationDao notificationDao;
		
		private String logicalEnvironmentName;
		
		private Collection<ArtifactNotification> notifications;
		
		private Exception exception;
		
		public RuntimeInstanceRetrievalTask(ArtifactNotificationDao notificationDao, String logicalEnvironmentName) {
			this.notificationDao = notificationDao;
			this.logicalEnvironmentName = logicalEnvironmentName;
		}
		
		@Override
		public void run() {
			
			logger.debug("Loading artifact notifications from {} environment...", logicalEnvironmentName);
			
			try {
				notifications = notificationDao.getNotifications(logicalEnvironmentName); 
			} catch (Exception e) {
				exception = e;
			}
		}
		
		public Collection<ArtifactNotification> getNotifications() {
			return notifications;
		}
		
		public Exception getException() {
			return exception;
		}
	}
}
