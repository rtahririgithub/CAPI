package com.telus.cmb.tool.services.log.config.domain.app;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.telus.cmb.tool.services.log.config.domain.AppMap;
import com.telus.cmb.tool.services.log.utils.ConfigUtil;

public class Artifact {

	private String name;
	private String version;
	private String appMap;
	private String path;
	private String filenames;
	private List<OverrideValue> overrideValues;
	private List<OverridePath> overridePaths;
	private List<AppMap> appMapList;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@XmlAttribute
	public String getAppMap() {
		return appMap;
	}

	public void setAppMap(String appMap) {
		this.appMap = appMap;
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

	@XmlElement(name = "overrideValue")
	public List<OverrideValue> getOverrideValues() {
		return overrideValues;
	}

	public void setOverrideValues(List<OverrideValue> overrideValues) {
		this.overrideValues = overrideValues;
	}

	@XmlElement(name = "overridePath")
	public List<OverridePath> getOverridePaths() {
		return overridePaths;
	}

	public void setOverridePaths(List<OverridePath> overridePaths) {
		this.overridePaths = overridePaths;
	}

	public List<AppMap> getAppMapList() {
		return appMapList;
	}

	public void setAppMapList(List<AppMap> appMapList) {
		this.appMapList = appMapList;
	}

	public String getAppMapName() {
		if (appMap == null) {
			return name;
		}
		return appMap;
	}

	public List<String> getFilenames(List<String> defaultFilenames) {
		return ConfigUtil.getFilenames(this.filenames, defaultFilenames);
	}

}
