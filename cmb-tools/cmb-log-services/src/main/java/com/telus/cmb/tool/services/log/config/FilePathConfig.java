package com.telus.cmb.tool.services.log.config;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Service;

import com.telus.cmb.tool.services.log.config.cache.ApplicationCache;
import com.telus.cmb.tool.services.log.config.cache.EnvironmentCache;
import com.telus.cmb.tool.services.log.config.cache.LogServerCache;
import com.telus.cmb.tool.services.log.config.domain.AppMap;
import com.telus.cmb.tool.services.log.config.domain.AppMapRepo;
import com.telus.cmb.tool.services.log.config.domain.Environment;
import com.telus.cmb.tool.services.log.config.domain.Environments;
import com.telus.cmb.tool.services.log.config.domain.LogServer;
import com.telus.cmb.tool.services.log.config.domain.LogServers;
import com.telus.cmb.tool.services.log.config.domain.RootFolders;
import com.telus.cmb.tool.services.log.config.domain.app.Application;
import com.telus.cmb.tool.services.log.config.domain.app.Artifact;
import com.telus.cmb.tool.services.log.config.domain.app.Component;
import com.telus.cmb.tool.services.log.domain.LogFilePaths;
import com.telus.cmb.tool.services.log.domain.LogServerInfo;
import com.telus.cmb.tool.services.log.utils.ConfigUtil;

@Service
public class FilePathConfig extends BaseConfig {

	private static final String APP_XML_CONFIG_FILENAME_REGEX = "filepaths-.*[.]xml";

	private Environments environments;
	private RootFolders rootFolders;
	private LogServerCache logServerCache;
	private Map<String, EnvironmentCache> envCaches;

	private static FilePathConfig INSTANCE = null;

	private FilePathConfig() {
	}

	public synchronized static FilePathConfig getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FilePathConfig();
			INSTANCE.initialize();
		}
		return INSTANCE;
	}

	private void initialize() {
		try {
			environments = parseConfigXml(Environments.class);
			rootFolders = parseConfigXml(RootFolders.class);
			initLogServerCache(parseConfigXml(LogServers.class));
			initializeEnvCaches();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initLogServerCache(LogServers logServers) {
		
		logServerCache = new LogServerCache();
		Map<String, LogServerInfo> logServerMap = new LinkedHashMap<String, LogServerInfo>();
		for (LogServer logServer : logServers.getLogServers()) {
			LogServerInfo logServerInfo = new LogServerInfo(logServer); 
			logServerMap.put(logServer.getShortname(), logServerInfo);
			if (logServer.isDefaultLogServer()) {
				if (logServer.isProduction()) {
					logServerCache.setDefaultPRLogServer(logServerInfo);
				} else {
					logServerCache.setDefaultLogServer(logServerInfo);
				}
			}
		}
		logServerCache.setLogServers(logServerMap);
	}
	
	private void initializeEnvCaches() throws JAXBException {

		envCaches = new HashMap<String, EnvironmentCache>();
		Map<String, List<AppMap>> appMapCache = getAppMapCache();
		for (Environment environment : environments.getEnvironments()) {
			EnvironmentCache envCache = new EnvironmentCache();
			envCache.setEnvironment(environment);
			for (Application application : getApplications()) {
				ApplicationCache appCache = new ApplicationCache();
				appCache.setApplication(application);
				for (Component component : application.getComponents()) {
					for (Artifact artifact : component.getArtifacts()) {
						artifact.setAppMapList(appMapCache.get(artifact.getAppMapName()));
					}
					appCache.addComponent(component);
				}
				envCache.addApplicationCache(appCache);
			}
			envCaches.put(environment.getShortname(), envCache);
		}

	}

	private Map<String, List<AppMap>> getAppMapCache() throws JAXBException {

		Map<String, List<AppMap>> appMapCache = new HashMap<String, List<AppMap>>();
		AppMapRepo appMapRepo = parseConfigXml(AppMapRepo.class);
		for (AppMap appMap : appMapRepo.getAppMapList()) {
			List<AppMap> appMapList = appMapCache.get(appMap.getName());
			if (appMapList == null) {
				appMapList = new ArrayList<AppMap>();
			}
			appMapList.add(appMap);
			appMapCache.put(appMap.getName(), appMapList);
		}

		return appMapCache;
	}

	private List<Application> getApplications() throws JAXBException {

		List<Application> applications = new ArrayList<Application>();
		File resourceFolder = new File(getRootXmlConfigFolder() + File.separator + "apps");
		File[] appConfigFiles = resourceFolder.listFiles(new FilenameFilter() {
			public boolean accept(File resourceFolder, String filename) {
				return filename.matches(APP_XML_CONFIG_FILENAME_REGEX);
			}
		});

		for (File appConfigFile : appConfigFiles) {
			applications.add(parseConfigXml(Application.class, appConfigFile));
		}

		return applications;
	}

	public List<Environment> getEnvironments() {
		return environments.getEnvironments();
	}

	public RootFolders getRootFolders() {
		return rootFolders;
	}

	public LogServerCache getLogServers() {
		return logServerCache;
	}

	public Environment getEnvironment(String envShortName) {
		EnvironmentCache envCache = envCaches.get(envShortName);
		return envCache != null ? envCache.getEnvironment() : null;
	}

	public List<Application> getApplications(String envShortName) {
		EnvironmentCache envCache = envCaches.get(envShortName);
		if (envCache != null) {
			List<Application> applications = envCache.getApplications();
			Collections.sort(applications, Application.ApplicationComparator);
			return applications;
		}
		return null;
	}

	public Application getApplication(String envShortName, String appShortName) {
		EnvironmentCache envCache = envCaches.get(envShortName);
		if (envCache != null) {
			ApplicationCache appCache = envCache.getApplicationCache(appShortName);
			return appCache != null ? appCache.getApplication() : null;
		}
		return null;
	}

	public List<String> getComponents(String envShortName, String appShortName) {
		EnvironmentCache envCache = envCaches.get(envShortName);
		if (envCache != null) {
			ApplicationCache appCache = envCache.getApplicationCache(appShortName);
			return appCache != null ? appCache.getComponents() : null;
		}
		return null;
	}

	public List<LogFilePaths> getFilePaths(String envShortName, String appShortName, String componentName) {

		EnvironmentCache envCache = envCaches.get(envShortName);
		if (envCache != null) {
			ApplicationCache appCache = envCache.getApplicationCache(appShortName);
			if (appCache != null) {
				Component component = appCache.getComponent(componentName);
				if (component != null) {
					List<LogFilePaths> logFilePaths = new ArrayList<LogFilePaths>();
					String defaultPath = component.getPath(appCache.getApplication().getPath());
					logFilePaths.addAll(component.getLogPaths(envCache.getEnvironment(), appCache.getApplication(), defaultPath, rootFolders, logServerCache));
					return ConfigUtil.normalizeLogFilePaths(logFilePaths);
				}
			}
		}

		return null;
	}

	public Set<LogServerInfo> getLogServers(String envShortName, String appShortName) {

		EnvironmentCache envCache = envCaches.get(envShortName);
		if (envCache != null) {
			ApplicationCache appCache = envCache.getApplicationCache(appShortName);
			if (appCache != null) {
				Set<LogServerInfo> logServers = new HashSet<LogServerInfo>();
				for (Component component : appCache.getApplication().getComponents()) {
					logServers.addAll(component.getLogServers(envCache.getEnvironment(), logServerCache));
				}
				return logServers;
			}
		}

		return null;
	}
	
}
