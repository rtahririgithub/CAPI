/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.domain.identity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author Pavel Simonovsky	
 *
 */
public class ConfigEntry {

	private String name;
	
	private String description;
	
	private Map<String, String> values = new HashMap<String, String>();
	
	private List<ConfigEntry> children = new ArrayList<ConfigEntry>();

	public ConfigEntry(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}

	public List<ConfigEntry> getChildren() {
		return children;
	}

	public void setChildren(List<ConfigEntry> children) {
		this.children = children;
	}
	
	public void addChild(ConfigEntry child) {
		children.add(child);
	}
	
	public void addValue(String environment, String value) {
		values.put(environment.toLowerCase(), value);
	}
	
	public ConfigEntry getChild(String name) {
		for (ConfigEntry child : children) {
			if (StringUtils.equalsIgnoreCase(child.getName(), name)) {
				return child;
			}
		}
		return null;
	}
	
	public String getValue(String environment) {
		return values.get(environment.toLowerCase());
	}
	
	public String getValue() {
		return getValue("*");
	}
	
	public void setValue(String value) {
		addValue("*", value);
	}
}
