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

/**
 * @author Pavel Simonovsky	
 *
 */
public class MenuManager {

	private List<MenuItem> items = new ArrayList<MenuItem>();

	public List<MenuItem> getItems() {
		return items;
	}

	public void setItems(List<MenuItem> items) {
		this.items = items;
	}
	
	public void addItem(MenuItem item) {
		items.add(item);
	}
	
}
