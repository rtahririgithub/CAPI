/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.config;

/**
 * @author Pavel Simonovsky
 *
 */
public class ConfigurationManagerFactory {

	private static ConfigurationManager manager;
	
	public static ConfigurationManager getInstance() {
		if (manager == null) {
			manager = new ConfigurationManagerImpl();
		}
		return manager;
	}
	
	public void destroy() {
		if (manager != null) {
			manager.destroy();
		}
		
		manager = null;
	}
}
