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

import com.telus.cmsc.domain.artifact.Environment;

/**
 * @author Pavel Simonovsky	
 *
 */
public class ApplicationIdentityEntry {

	private String principal;
	
	private String decryptedCredentials;
	
	private boolean validate;
	
	private String encryptedCredentials;
	
	private Environment environment;

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getDecryptedCredentials() {
		return decryptedCredentials;
	}

	public void setDecryptedCredentials(String decryptedCredentials) {
		this.decryptedCredentials = decryptedCredentials;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public String getEncryptedCredentials() {
		return encryptedCredentials;
	}

	public void setEncryptedCredentials(String encryptedCredentials) {
		this.encryptedCredentials = encryptedCredentials;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
}
