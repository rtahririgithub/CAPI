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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Pavel Simonovsky	
 *
 */

@XmlRootElement
public class MenuItem {

	private MenuItem parent;
	
	private String id;
	
	private String label;

	private String action;
	
	private List<MenuItem> items = new ArrayList<MenuItem>();

	public MenuItem() {
	}
	
	public MenuItem(String id, String label, String action) {
		this.id = id;
		this.label = label;
		this.action = action;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public MenuItem getParent() {
		return parent;
	}

	public void setParent(MenuItem parent) {
		this.parent = parent;
	}

	public void addItem(MenuItem item) {
		items.add(item);
		item.setParent(this);
	}

	public void addItem(String id, String label, String action) {
		addItem( new MenuItem(id, label, action));
	}
	
	
}
