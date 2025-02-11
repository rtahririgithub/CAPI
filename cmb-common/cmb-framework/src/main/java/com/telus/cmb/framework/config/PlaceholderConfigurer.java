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

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

/**
 * @author Pavel Simonovsky
 *
 */
public class PlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {

	/**
	 * 
	 */
	public PlaceholderConfigurer() {
		MutablePropertySources sources = new MutablePropertySources();
		sources.addFirst( new ConfigManagerPropertySource());
		setPropertySources(sources);
	}

	private class ConfigManagerPropertySource extends PropertySource<ConfigurationManager> {
		
		public ConfigManagerPropertySource() {
			super("configManagerPropertySource");
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.springframework.core.env.PropertySource#getProperty(java.lang.String)
		 */
		@Override
		public Object getProperty(String paramString) {
			return ConfigurationManagerFactory.getInstance().getStringValue(paramString, null);
		}
	}
}
