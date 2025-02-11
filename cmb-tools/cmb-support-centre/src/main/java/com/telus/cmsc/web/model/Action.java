/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.web.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author Pavel Simonovsky	
 *
 */
public class Action {

	private String id;
	
	private String title;
	
	private String icon;
	
	private String url;
	
	private List<Action> actions = new ArrayList<Action>();
	
	private Action parent;

	public Action(String id, String title) {
		this(id, title, null, null);
	}

	public Action(String id, String title, String icon) {
		this(id, title, icon, null);
	}
	
	public Action(String id, String title, String icon, String url) {
		this.id = id;
		this.title = title;
		this.icon = icon;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
	public void addAction(Action action) {
		actions.add(action);
		action.setParent(this);
	}

	public Action getParent() {
		return parent;
	}

	public void setParent(Action parent) {
		this.parent = parent;
	}

	public Action findAction(String actionId) {
		
		if (StringUtils.equals(actionId, id)) {
			return this;
		}
		
		for (Action action : actions) {
			if (StringUtils.equals(actionId, action.getId())) {
				Action result = action.findAction(actionId);
				if (result != null) {
					return result;
				}
			}
		}
		
		return null;
	}
}

