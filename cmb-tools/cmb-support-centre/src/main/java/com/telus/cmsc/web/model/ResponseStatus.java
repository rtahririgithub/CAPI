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
import java.util.Collection;

/**
 * @author Pavel Simonovsky	
 *
 */
public class ResponseStatus {
	
	private Collection<String> messages = new ArrayList<String>();
	
	private Collection<String> errors = new ArrayList<String>();
	
	/**
	 * @return the messages
	 */
	public Collection<String> getMessages() {
		return messages;
	}
	
	/**
	 * @param messages the messages to set
	 */
	public void setMessages(Collection<String> messages) {
		this.messages = messages;
	}
	
	public ResponseStatus addMessage(String message) {
		messages.add(message);
		return this;
	}
	
	/**
	 * @return the errors
	 */
	public Collection<String> getErrors() {
		return errors;
	}
	
	/**
	 * @param errors the errors to set
	 */
	public void setErrors(Collection<String> errors) {
		this.errors = errors;
	}

	public ResponseStatus addError(String error) {
		errors.add(error);
		return this;
	}
	
	public static ResponseStatus createMessage(String message) {
		return new ResponseStatus().addMessage(message);
	}
	
	public static ResponseStatus createError(String error) {
		return new ResponseStatus().addError(error);
	}
	
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	public boolean hasMessages() {
		return !messages.isEmpty();
	}
}
