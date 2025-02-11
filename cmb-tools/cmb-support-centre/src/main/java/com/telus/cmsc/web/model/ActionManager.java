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
public class ActionManager {

	private List<Action> actions = new ArrayList<Action>();
	
	public void addAction(Action action) {
		actions.add(action);
	}
	
	public List<Action> getActions() {
		return actions;
	}
	
	public Action findAction(String actionId) {
		for (Action action :actions) {
			Action result = action.findAction(actionId);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	public List<Action> getActionChain(String actionId) {
		
		List<Action> result = new ArrayList<Action>();
		
		Action action = findAction(actionId);
		while (action != null) {
			result.add(0, action);
			action = action.getParent();
		}
		
		return result;
		
	}
}
