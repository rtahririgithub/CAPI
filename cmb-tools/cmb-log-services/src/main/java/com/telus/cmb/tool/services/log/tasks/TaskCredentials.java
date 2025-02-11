package com.telus.cmb.tool.services.log.tasks;

import org.apache.commons.lang3.StringUtils;

public class TaskCredentials {

	private static TaskCredentials INSTANCE = null;

	private TaskCredentials() {
	}

	public synchronized static TaskCredentials getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TaskCredentials();
		}
		return INSTANCE;
	}

	private String unixUsername;
	private String unixPassword;
	private String windowsUsername;
	private String windowsPassword;

	public String getUnixUsername() {
		return unixUsername;
	}

	public void setUnixUsername(String unixUsername) {
		this.unixUsername = unixUsername;
	}

	public String getUnixPassword() {
		return unixPassword;
	}

	public void setUnixPassword(String unixPassword) {
		this.unixPassword = unixPassword;
	}

	public String getWindowsUsername() {
		return windowsUsername;
	}

	public void setWindowsUsername(String windowsUsername) {
		this.windowsUsername = windowsUsername;
	}

	public String getWindowsPassword() {
		return windowsPassword;
	}

	public void setWindowsPassword(String windowsPassword) {
		this.windowsPassword = windowsPassword;
	}

	public boolean isInitialized() {
		return isPTInitialized() && isPRInitialized();
	}

	public boolean isPTInitialized() {
		return StringUtils.isNotBlank(windowsUsername) && StringUtils.isNotBlank(windowsPassword);
	}

	public boolean isPRInitialized() {
		return StringUtils.isNotBlank(unixPassword) && StringUtils.isNotBlank(unixPassword);
	}
	
	public String getUsername(boolean usesUnixLogin) {
		if (usesUnixLogin) {
			return unixUsername;
		}
		return windowsUsername;
	}
	
	public String getPassword(boolean usesUnixLogin) {
		if (usesUnixLogin) {
			return unixPassword;
		}
		return windowsPassword;
	}

}
