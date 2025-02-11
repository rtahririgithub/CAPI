package com.telus.cmb.tool.services.log.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telus.cmb.tool.services.log.config.cache.LogServerCache;
import com.telus.cmb.tool.services.log.config.domain.AppMap;
import com.telus.cmb.tool.services.log.config.domain.Environment;
import com.telus.cmb.tool.services.log.config.domain.RootFolders;
import com.telus.cmb.tool.services.log.config.domain.app.Application;
import com.telus.cmb.tool.services.log.config.domain.app.Artifact;
import com.telus.cmb.tool.services.log.config.domain.app.Component;
import com.telus.cmb.tool.services.log.dao.EmtoolsDao;
import com.telus.cmb.tool.services.log.domain.LogFilePaths;
import com.telus.cmb.tool.services.log.domain.LogServerInfo;
import com.telus.cmb.tool.services.log.utils.ConfigUtil;

@Service
public class FilePathConfigRTImpl implements FilePathConfigRT {

	@Autowired
	private EmtoolsDao emtoolsDao;

	private FilePathConfig filePathConfig = FilePathConfig.getInstance();

	@Override
	public List<LogFilePaths> getFilePaths(String envShortName, String appShortName, String componentName) {

		Environment environment = filePathConfig.getEnvironment(envShortName);
		RootFolders rootFolders = filePathConfig.getRootFolders();
		LogServerCache logServers = filePathConfig.getLogServers();
		Application application = filePathConfig.getApplication(envShortName, appShortName);
		if (application != null) {
			Component component = application.getComponent(componentName);
			if (component != null) {
				List<LogFilePaths> logFilePaths = new ArrayList<LogFilePaths>();
				for (Artifact artifact : component.getArtifacts()) {
					String defaultPath = component.getPath(application.getPath());
					List<AppMap> appMaps = emtoolsDao.getAppMap(artifact.getAppMapName());
					logFilePaths.addAll(ConfigUtil.getLogFilePaths(environment, application, component, artifact, defaultPath, rootFolders, logServers, appMaps));
				}
				return ConfigUtil.normalizeLogFilePaths(logFilePaths);
			}
		}

		return null;
	}

	@Override
	public Set<LogServerInfo> getLogServers(String envShortName, String appShortName) {

		Environment environment = filePathConfig.getEnvironment(envShortName);
		Application application = filePathConfig.getApplication(envShortName, appShortName);
		LogServerCache logServerCache = filePathConfig.getLogServers();
		if (application != null) {
			Set<LogServerInfo> logServers = new HashSet<LogServerInfo>();
			for (Component component : application.getComponents()) {
					for (Artifact artifact : component.getArtifacts()) {
						List<AppMap> appMaps = emtoolsDao.getAppMap(artifact.getAppMapName());
						logServers.addAll(ConfigUtil.getLogServers(environment, logServerCache, appMaps));
					}
			}
			return logServers;
		}
	
		return null;
	}

}
