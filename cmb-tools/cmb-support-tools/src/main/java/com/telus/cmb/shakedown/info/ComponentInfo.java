package com.telus.cmb.shakedown.info;

import java.io.Serializable;

import com.telus.cmb.common.shakedown.EjbShakedown;
import com.telus.cmnb.armx.agent.shakedown.ShakedownModule;

public class ComponentInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String category;
	private String group;
	private String[] clusters;
	private ShakedownModule testSuite;
	private String[] ldapPath;
	
	public ComponentInfo(String name, String category, String group, String[] clusters, ShakedownModule shakedownModule) {
		this.name = name;
		this.category = category;
		this.group = group;
		this.clusters = clusters;
		this.testSuite = shakedownModule;
		this.ldapPath = new String[] { "CMB", "services", name, "url"};
	}
	
	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public String getGroup() {
		return group;
	}

	public String[] getClusters() {
		return clusters;
	}

	public String getUrlKey() {
		return name + "Url";
	}
	
	public String[] getUrlPath() {
		return ldapPath;
	}
	
	public ShakedownModule getTestSuite() {
		return testSuite;
	}
}
