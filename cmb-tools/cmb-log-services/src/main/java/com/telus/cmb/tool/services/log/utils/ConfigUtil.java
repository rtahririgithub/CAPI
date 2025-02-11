package com.telus.cmb.tool.services.log.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.telus.cmb.tool.services.log.config.cache.LogServerCache;
import com.telus.cmb.tool.services.log.config.domain.AppMap;
import com.telus.cmb.tool.services.log.config.domain.Environment;
import com.telus.cmb.tool.services.log.config.domain.RootFolder;
import com.telus.cmb.tool.services.log.config.domain.RootFolders;
import com.telus.cmb.tool.services.log.config.domain.app.Application;
import com.telus.cmb.tool.services.log.config.domain.app.Artifact;
import com.telus.cmb.tool.services.log.config.domain.app.Component;
import com.telus.cmb.tool.services.log.config.domain.app.OverridePath;
import com.telus.cmb.tool.services.log.config.domain.app.OverrideValue;
import com.telus.cmb.tool.services.log.domain.LogFilePaths;
import com.telus.cmb.tool.services.log.domain.LogServerInfo;

public class ConfigUtil {

	private static final String ROOT_REGEX = escapeKeyword("root");
	private static final String DOMAIN_REGEX = escapeKeyword("domain");
	private static final String CLUSTER_REGEX = escapeKeyword("cluster");
	private static final String HOST_REGEX = escapeKeyword("host");
	private static final String NODE_REGEX = escapeKeyword("node");
	private static final String ARTIFACT_REGEX = escapeKeyword("artifact");
	private static final String VERSION_REGEX = escapeKeyword("version");
	private static final String GENERAL_REGEX = "\\$\\{.*\\}";
	private static final String FILENAME_DELIMITER = ",";

	public static List<LogFilePaths> getLogFilePaths(Environment environment, Application application, Component component, Artifact artifact, String defaultPath, RootFolders rootFolders,
			LogServerCache logServerCache, List<AppMap> appMaps) {

		List<LogFilePaths> logFilePathsList = new ArrayList<LogFilePaths>();
		for (AppMap appMap : appMaps) {
			if (StringUtils.equals(appMap.getEnvironment(), environment.getEmValue())) {
				LogFilePaths logFilePaths = new LogFilePaths();
				logFilePaths.setLogServer(ConfigUtil.getLogServer(appMap, logServerCache, environment.isProduction()));
				logFilePaths.addFilepaths(ConfigUtil.getTrueLogPaths(defaultPath, environment.getShortname(), rootFolders, application, component, artifact, appMap));
				logFilePathsList.add(logFilePaths);
			}
		}

		return logFilePathsList;
	}

	/**
	 * This method groups up the logFilePaths based on the log server
	 * @param logFilePathsList
	 * @return
	 */
	public static List<LogFilePaths> normalizeLogFilePaths(List<LogFilePaths> logFilePathsList) {

		Map<LogServerInfo, LogFilePaths> normalizedMap = new HashMap<LogServerInfo, LogFilePaths>();
		for (LogFilePaths logFilePaths : logFilePathsList) {
			LogFilePaths normalized = normalizedMap.get(logFilePaths.getLogServer());
			if (normalized == null) {
				normalized = logFilePaths;
			} else {
				normalized.addFilepaths(logFilePaths.getFilepaths());
			}
			normalizedMap.put(normalized.getLogServer(), normalized);
		}

		return new ArrayList<LogFilePaths>(normalizedMap.values());
	}

	public static List<String> getFilenames(String filenames, List<String> defaultFilenames) {

		List<String> filenameList = new ArrayList<String>();
		if (filenames != null) {
			for (String filename : filenames.split(FILENAME_DELIMITER)) {
				if (!filename.trim().isEmpty()) {
					filenameList.add(filename.trim());
				}
			}
		}

		if (filenameList.isEmpty()) {
			return defaultFilenames == null ? new ArrayList<String>() : defaultFilenames;
		} else {
			return filenameList;
		}
	}

	public static Set<LogServerInfo> getLogServers(Environment environment, LogServerCache logServerCache, List<AppMap> appMaps) {

		Set<LogServerInfo> logServers = new HashSet<LogServerInfo>();
		if (appMaps != null) {
			for (AppMap appMap : appMaps) {
				if (StringUtils.equals(appMap.getEnvironment(), environment.getEmValue())) {
					logServers.add(ConfigUtil.getLogServer(appMap, logServerCache, environment.isProduction()));
				}
			}
		}

		return logServers;
	}
	
	public static String getTimestampFormat(Application application, Component component) {
		String timestampFormat = null;
		timestampFormat = StringUtils.isNotBlank(application.getTimestampFormat()) ? application.getTimestampFormat() : timestampFormat;
		timestampFormat = StringUtils.isNotBlank(component.getTimestampFormat()) ? component.getTimestampFormat() : timestampFormat;
		return timestampFormat;
	}

	private static LogServerInfo getLogServer(AppMap appMap, LogServerCache logServerCache, boolean isProduction) {
		for (LogServerInfo logServer : logServerCache.getLogServers()) {
			if (logServer.isAppMapped(appMap)) {
				return logServer;
			}
		}
		return isProduction ? logServerCache.getDefaultPRLogServer() : logServerCache.getDefaultLogServer();
	}

	private static List<String> getTrueLogPaths(String path, String envShortname, RootFolders rootFolders, Application application, Component component, Artifact artifact, AppMap appMap) {
		String logPath = applyPathOverrides(path, envShortname, artifact);
		logPath = applyOverrideSubstitutions(logPath, envShortname, artifact.getOverrideValues());
		logPath = applyOverrideSubstitutions(logPath, envShortname, component.getOverrideValues());
		logPath = applyOverrideSubstitutions(logPath, envShortname, application.getOverrideValues());
		logPath = applyDefaultSubstitutions(logPath, envShortname, rootFolders, artifact, appMap);
		
		List<String> filenames = artifact.getFilenames(component.getFilenames(application.getFilenameList()));
		if (filenames.isEmpty()) {
			return Arrays.asList(new String[]{logPath + (logPath.endsWith("/") ? "*" : "")});
		} else {
			List<String> logPaths = new ArrayList<String>();
			for (String filename : filenames) {
				logPaths.add(logPath + filename);
			}
			return logPaths;
		}
	}

	private static String applyPathOverrides(String path, String envShortname, Artifact artifact) {

		String logPath = path;
		if (artifact.getOverridePaths() != null) {
			for (OverridePath overridePath : artifact.getOverridePaths()) {
				for (String opEnvName : overridePath.getEnvironments().split(",")) {
					if (StringUtils.equalsIgnoreCase(opEnvName.trim(), envShortname)) {
						logPath = overridePath.getPath();
					}
				}
			}
		}

		return logPath;
	}

	private static String applyOverrideSubstitutions(String logPath, String envShortname, List<OverrideValue> overrideValues) {

		if (overrideValues != null) {
			for (OverrideValue override : overrideValues) {
				if (override.getEnvironments() == null) {
					logPath = logPath.replaceAll(escapeKeyword(override.getName()), override.getValue());
				} else {
					for (String ovEnvName : override.getEnvironments().split(",")) {
						if (StringUtils.equalsIgnoreCase(ovEnvName.trim(), envShortname)) {
							logPath = logPath.replaceAll(escapeKeyword(override.getName()), override.getValue());
							break;
						}
					}

				}
			}
		}

		return logPath;
	}

	private static String applyDefaultSubstitutions(String logPath, String envShortName, RootFolders rootFolders, Artifact artifact, AppMap appMap) {

		logPath = logPath.replaceAll(ROOT_REGEX, getLogRoot(rootFolders, appMap.getHost(), envShortName));
		logPath = logPath.replaceAll(DOMAIN_REGEX, appMap.getDomain());
		logPath = logPath.replaceAll(CLUSTER_REGEX, appMap.getCluster());
		logPath = logPath.replaceAll(NODE_REGEX, appMap.getNode());
		logPath = logPath.replaceAll(HOST_REGEX, appMap.getHost());
		logPath = logPath.replaceAll(ARTIFACT_REGEX, artifact.getName());
		logPath = logPath.replaceAll(VERSION_REGEX, artifact.getVersion());
		logPath = logPath.replaceAll(GENERAL_REGEX, "");

		return logPath;
	}

	private static String escapeKeyword(String key) {
		return Pattern.quote("${" + key + "}");
	}

	private static String getLogRoot(RootFolders rootFolders, String host, String envShortName) {

		// PRA and PRB are kinda stable for these values (for now)... so we're hardcoding it here because xml config changes too much
		if (StringUtils.equals(envShortName, "pra")) {
			return "logsa";
		} else if (StringUtils.equals(envShortName, "prb")) {
			return "logsb";
		} else if (StringUtils.equals(envShortName, "st101a")) {
			// They've resorted to use some funky naming conventions for staging so we're handling it via config
			for (RootFolder rootFolder : rootFolders.getRootFolders()) {
				for (String rootHost : rootFolder.getHosts()) {
					if (StringUtils.equals(rootHost, host)) {
						return rootFolder.getName().toLowerCase();
					}
				}
			}
		}
		
		// Pretty much the default log path here
		return "logs";
	}

}
