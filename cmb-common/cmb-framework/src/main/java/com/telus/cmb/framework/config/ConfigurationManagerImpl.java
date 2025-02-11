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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.telus.framework.config.ConfigContext;

/**
 * @author Pavel Simonovsky
 *
 */
public class ConfigurationManagerImpl implements ConfigurationManager {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationManagerImpl.class);
	
	private static final String MAPPINGS_FILE_NAME = "config-mapping.properties";
	private static final String OVERRIDES_FILE_NAME = "config-overrides.properties";

	private Properties mappings;
	
	private Properties overrides;
	
	private ConversionService conversionService = new DefaultConversionService();
	
	protected static final int SINGLE_REFRESH_INTERVAL_MINUTE = 5;
	protected static final int COMPLETE_REFRESH_INTERVAL_MINUTE = 60;
	
	private volatile Thread thread;
	
	/**
	 * 
	 */
	ConfigurationManagerImpl() {
		
		try {
			
			mappings = PropertiesLoaderUtils.loadAllProperties(MAPPINGS_FILE_NAME);
			
			logger.debug("Loaded {} custom configuration mappings", mappings.size());
			
			overrides = PropertiesLoaderUtils.loadAllProperties(OVERRIDES_FILE_NAME);

			logger.debug("Loaded {} configuration overrides", overrides.size());
			
			scheduleRefresh();
			
		} catch (Exception e) {
			throw new RuntimeException("Error loading configuration mappings: " + e.getMessage(), e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#refresh()
	 */
	@Override
	public void refresh() {
		logger.info("Ldap refresh has started..");
		ConfigContext.refresh();
		logger.info("Ldap refresh has completed..");
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#overrideValue(java.lang.String, java.lang.Object, java.util.Date, java.util.Date)
	 */
	@Override
	public void overrideValue(String key, Object value, Date fromDate, Date toDate) {
		//TODO: implement date effectiveness
		if (key != null && value != null) {
			overrides.setProperty(key, String.valueOf(value));
		}
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#overrideValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void overrideValue(String key, Object value) {
		overrideValue(key, value, null, null);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#resetValue(java.lang.String)
	 */
	@Override
	public void resetValue(String key) {
		if (key != null) {
			overrides.remove(key);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#buildPath(java.lang.String[])
	 */
	@Override
	public String buildPath(String ... segments) {
		StringBuffer buffer = new StringBuffer();
		for (int idx = 0; idx < segments.length; idx++) {
			if (idx != 0) {
				buffer.append('/');
			}
			buffer.append(segments[idx]);
		}
		return buffer.toString();
	}
	


	private String getPath(String key) {
		String path = mappings.getProperty(key);
		return StringUtils.isEmpty(path) ? key : path.trim();
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getValue(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T getValue(String key, Class<T> targetType) {
		return getValue(key, targetType, null);
	}


	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getValue(java.lang.String, java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> T getValue(String key, Class<T> targetType, T defaultValue) {
		
		T result = defaultValue;
		
		String value = System.getProperty(key);

		if (StringUtils.isEmpty(value)) {
			value = ConfigContext.getProperty(getPath(key));
		}
		
		value = overrides.getProperty(key, value);
		if (value != null) {
			result = conversionService.convert(value, targetType);
		}
		
		logger.debug("Resolved config entry [{}] -> [{}]", key, result);
		
		return result;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getValues(java.lang.String, java.lang.Class, java.lang.String)
	 */
	@Override
	public <E> List<E> getValues(String key, Class<E> targetType) {
		return getValues(key, targetType, new ArrayList<E>());
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getValues(java.lang.String, java.lang.Class, java.lang.String, java.util.List)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public <E> List<E> getValues(String key, Class<E> targetType, List<E> defaultValues) {
		
		List<E> result = new ArrayList<E>();

		List values = ConfigContext.getList(getPath(key));
		if (values != null) {
			for (Object value : values) {
				result.add(conversionService.convert(value, targetType));
			}
		} else {
			result.addAll(defaultValues);
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getValues(java.lang.String, java.lang.Class, java.lang.String)
	 */
	@Override
	public <E> List<E> getValues(String key, Class<E> targetType, String delimiter) {
		return getValues(key, targetType, delimiter, new ArrayList<E>());
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getValues(java.lang.String, java.lang.Class, java.lang.String, java.util.List)
	 */
	@Override
	public <E> List<E> getValues(String key, Class<E> targetType, String delimiter, List<E> defaultValues) {
		List<E> result = defaultValues;
		
		String value = getStringValue(key);
		if (StringUtils.isNotEmpty(value)) {
			
			result = new ArrayList<E>();
			
			StringTokenizer tokenizer = new StringTokenizer(value, delimiter);
			while (tokenizer.hasMoreTokens()) {
				result.add(conversionService.convert(tokenizer.nextToken(), targetType));
			}
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getStringValue(java.lang.String)
	 */
	@Override
	public String getStringValue(String key) {
		return getStringValue(key, "");
	}


	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getStringValue(java.lang.String, java.lang.String)
	 */
	@Override
	public String getStringValue(String key, String defaultValue) {
		return getValue(key, String.class, defaultValue);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getStringValues(java.lang.String)
	 */
	@Override
	public List<String> getStringValues(String key) {
		return getStringValues(key, new ArrayList<String>());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getStringValues(java.lang.String, java.util.List)
	 */
	@Override
	public List<String> getStringValues(String key, List<String> defaultValues) {
		return getValues(key, String.class, defaultValues);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getStringValues(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public List<String> getStringValues(String key, String delimiter, List<String> defaultValues) {
		return getValues(key, String.class, delimiter, defaultValues);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getLongValue(java.lang.String)
	 */
	@Override
	public Long getLongValue(String key) {
		return getLongValue(key, 0);
	}


	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getLongValue(java.lang.String, long)
	 */
	@Override
	public Long getLongValue(String key, long defaultValue) {
		return getValue(key, Long.class, defaultValue);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getIntegerValue(java.lang.String)
	 */
	@Override
	public Integer getIntegerValue(String key) {
		return getIntegerValue(key, 0);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getIntegerValue(java.lang.String, int)
	 */
	@Override
	public Integer getIntegerValue(String key, int defaultValue) {
		return getValue(key, Integer.class, defaultValue);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getBooleanValue(java.lang.String)
	 */
	@Override
	public Boolean getBooleanValue(String key) {
		return getBooleanValue(key, false);
	}


	/* (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getBooleanValue(java.lang.String, boolean)
	 */
	@Override
	public Boolean getBooleanValue(String key, boolean defaultValue) {
		return getValue(key, Boolean.class, defaultValue);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getDateValue(java.lang.String)
	 */
	@Override
	public Date getDateValue(String key) {
		return getDateValue(key, null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.framework.config.ConfigurationManager#getDateValue(java.lang.String, java.util.Date)
	 */
	@Override
	public Date getDateValue(String key, Date defaultValue) {
		return getValue(key, Date.class);
	}
	

	protected synchronized void scheduleRefresh() {
		if (this.thread == null) {
			this.thread = new Thread("LdapDataRefreshThread") {
				
				public void run() {
					Logger logger = LoggerFactory.getLogger("ldapRefreshLogger");
					boolean keepRunning = true;
					
					logger.info("Starting ldap refresh thread..");
					
					while (keepRunning) {
						try {
							sleep(COMPLETE_REFRESH_INTERVAL_MINUTE * 60 * 1000);
							refresh();
						} catch (InterruptedException ie) {
							keepRunning = false;
						} catch (Throwable e) {
							logger.error("Error refreshing LDAP: ", e);
						}
					}
					
					logger.info("Exiting ldap refresh thread..");
				};
			};
			thread.setDaemon(true);
			thread.start();
		}
	}
	
	public synchronized void destroy() {
		logger.info("Shutting down ConfigurationManagerImpl..");
		if (this.thread != null) {
			try {
				this.thread.interrupt();
			}catch (Throwable t) {
				logger.error("Error destroying refresh thread..", t);
			}
			this.thread = null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			super.finalize();
			destroy();
		} catch (Throwable t) {
			logger.error("Error cleaning up refresh thread..", t);
		}
	}

}
