/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Pavel Simonovsky
 *
 */
public class ValidationGroup {

	private String name;
	
	public ValidationGroup(String name) {
		this.name = name;
	}
	
	private List<ValidationError> errors = new ArrayList<ValidationError>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ValidationError> getErrors() {
		return errors;
	}

	public void setErrors(List<ValidationError> errors) {
		this.errors = errors;
	}
	
	public void addError(ValidationError error) {
		errors.add(error);
	}

	public void addErrors(Collection<ValidationError> errors) {
		this.errors.addAll(errors);
	}
	
}
