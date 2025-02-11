/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.validation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pavel Simonovsky
 *
 */
public class ValidationResult {
	
	private static final String DEFAULT_GROUP_NAME = "Default Group";
	
	private Map<String, ValidationGroup> errorGroups = new HashMap<String, ValidationGroup>();

	public Collection<ValidationGroup> getErrorGroups() {
		return errorGroups.values();
	}

	public void addErrorGroup(ValidationGroup errorGroup) {

		ValidationGroup existingGroup = errorGroups.get(errorGroup.getName());
		
		if (existingGroup != null) {
			existingGroup.addErrors(errorGroup.getErrors());
		} else {
			errorGroups.put(errorGroup.getName(), errorGroup);
		}
	}

	public void addError(String errorCode, String errorMessage) {
		addError(DEFAULT_GROUP_NAME, new ValidationError(errorCode, errorMessage));
	}
	
	public void addError(String groupName, String errorCode, String errorMessage) {
		addError(groupName, new ValidationError(errorCode, errorMessage));
	}
	
	public void addError(String groupName, ValidationError error) {
		ValidationGroup group = errorGroups.get(groupName);
		if (group == null) {
			group = new ValidationGroup(groupName);
			errorGroups.put(groupName, group);
		}
		group.addError(error);
	}
	
	public boolean hasErrors() {
		return !errorGroups.isEmpty();
	}
	
}
