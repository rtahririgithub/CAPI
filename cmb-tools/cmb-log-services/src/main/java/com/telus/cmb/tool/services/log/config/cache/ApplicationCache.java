package com.telus.cmb.tool.services.log.config.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.telus.cmb.tool.services.log.config.domain.Environment;
import com.telus.cmb.tool.services.log.config.domain.app.Application;
import com.telus.cmb.tool.services.log.config.domain.app.Component;
import com.telus.cmb.tool.services.log.domain.LogServerInfo;

public class ApplicationCache {

	private Application application;
	private Map<String, Component> componentMap = new HashMap<String, Component>();

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public List<String> getComponents() {
		List<String> components = new ArrayList<String>();
		for (Component component : componentMap.values()) {
			components.add(component.getName());
		}
		Collections.sort(components);
		return components;
	}
	
	public Component getComponent(String componentName) {
		return componentMap.get(componentName);
	}

	public void addComponent(Component component) {
		this.componentMap.put(component.getName(), component);
	}

	public Set<LogServerInfo> getLogServers(Environment environment, LogServerCache logServerCache) {
		Set<LogServerInfo> logServers = new HashSet<LogServerInfo>();
		for (Component component : componentMap.values()) {
			logServers.addAll(component.getLogServers(environment, logServerCache));
		}
		return logServers;
	}

}
