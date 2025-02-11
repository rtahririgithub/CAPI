package com.telus.cmb.tool.services.log.config.domain.app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.StringUtils;

import com.telus.cmb.tool.services.log.config.cache.LogServerCache;
import com.telus.cmb.tool.services.log.config.domain.Environment;
import com.telus.cmb.tool.services.log.config.domain.RootFolders;
import com.telus.cmb.tool.services.log.domain.LogFilePaths;
import com.telus.cmb.tool.services.log.domain.LogServerInfo;
import com.telus.cmb.tool.services.log.utils.ConfigUtil;

public class Component {

	private String name;
	private String path;
	private String filenames;
	private String timestampFormat;
	private List<Artifact> artifacts;
	private List<OverrideValue> overrideValues;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@XmlAttribute
	public String getFilenames() {
		return filenames;
	}

	public void setFilenames(String filenames) {
		this.filenames = filenames;
	}

	@XmlAttribute
	public String getTimestampFormat() {
		return timestampFormat;
	}

	public void setTimestampFormat(String timestampFormat) {
		this.timestampFormat = timestampFormat;
	}

	@XmlElement(name = "artifact")
	public List<Artifact> getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(List<Artifact> artifacts) {
		this.artifacts = artifacts;
	}

	@XmlElement(name = "overrideValue")
	public List<OverrideValue> getOverrideValues() {
		return overrideValues;
	}

	public void setOverrideValues(List<OverrideValue> overrideValues) {
		this.overrideValues = overrideValues;
	}

	public String getPath(String path) {
		return this.path == null ? path : this.path;
	}

	public boolean isTimestampFormatConfigured() {
		return StringUtils.isBlank(this.timestampFormat);
	}

	public List<LogFilePaths> getLogPaths(Environment environment, Application application, String defaultPath, RootFolders rootFolders, LogServerCache logServerCache) {
		List<LogFilePaths> logFilePathsList = new ArrayList<LogFilePaths>();
		for (Artifact artifact : artifacts) {
			logFilePathsList.addAll(ConfigUtil.getLogFilePaths(environment, application, this, artifact, defaultPath, rootFolders, logServerCache, artifact.getAppMapList()));
		}
		return ConfigUtil.normalizeLogFilePaths(logFilePathsList);
	}

	public Set<LogServerInfo> getLogServers(Environment environment, LogServerCache logServerCache) {
		Set<LogServerInfo> logServers = new HashSet<LogServerInfo>();
		for (Artifact artifact : artifacts) {
			logServers.addAll(ConfigUtil.getLogServers(environment, logServerCache, artifact.getAppMapList()));
		}
		return logServers;
	}

	public List<String> getFilenames(List<String> defaultFilenames) {
		return ConfigUtil.getFilenames(this.filenames, defaultFilenames);
	}

	public String getTimestampFormat(String defaultFormat) {
		return timestampFormat != null ? timestampFormat : defaultFormat;
	}
	
}
