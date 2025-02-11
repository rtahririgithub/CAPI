package com.telus.cmb.tool.services.log.config.domain.app;

import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

import com.telus.cmb.tool.services.log.utils.ConfigUtil;

@XmlRootElement
public class Application {

	private String name;
	private String shortname;
	private String path;
	private String filenames;
	private String timestampFormat;
	private List<Component> components;
	private List<OverrideValue> overrideValues;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
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

	@XmlElement(name = "component")
	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

	@XmlElement(name = "overrideValue")
	public List<OverrideValue> getOverrideValues() {
		return overrideValues;
	}

	public void setOverrideValues(List<OverrideValue> overrideValues) {
		this.overrideValues = overrideValues;
	}

	public static Comparator<Application> ApplicationComparator = new Comparator<Application>() {
		public int compare(Application application1, Application application2) {
			return application1.getName().toUpperCase().compareTo(application2.getName().toUpperCase());
		}
	};

	public Component getComponent(String componentName) {
		if (this.components != null) {
			for (Component component : this.components) {
				if (StringUtils.equals(component.getName(), componentName)) {
					return component;
				}
			}
		}
		return null;
	}

	public List<String> getFilenameList() {
		return ConfigUtil.getFilenames(this.filenames, null);
	}

}
