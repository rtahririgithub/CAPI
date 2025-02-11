package com.telus.cmb.tool.services.log.config.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telus.cmb.tool.services.log.config.domain.Environment;
import com.telus.cmb.tool.services.log.config.domain.app.Application;

public class EnvironmentCache {

	private Environment environment;
	private Map<String, ApplicationCache> appCaches = new HashMap<String, ApplicationCache>();

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public List<Application> getApplications() {
		List<Application> applications = new ArrayList<Application>();
		for (ApplicationCache appCache : appCaches.values()) {
			applications.add(appCache.getApplication());
		}
		return applications;
	}
	
	public ApplicationCache getApplicationCache(String applicationName) {
		return appCaches.get(applicationName);
	}

	public void addApplicationCache(ApplicationCache cache) {
		appCaches.put(cache.getApplication().getShortname(), cache);
	}

}
