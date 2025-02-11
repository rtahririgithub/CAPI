/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.logging;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;

import com.telus.cmb.framework.config.ConfigurationManager;
import com.telus.cmb.framework.config.ConfigurationManagerFactory;
import com.telus.cmb.framework.util.XmlUtil;

/**
 * @author Pavel Simonovsky
 *
 */
public class LoggingManager {
	
	private static final Log logger = LogFactory.getLog(LoggingManager.class);
	
	private ConfigurationManager configurationManager = ConfigurationManagerFactory.getInstance();
	
	public String getLoggingLevel() {
		return LogManager.getRootLogger().getLevel().toString();		
	}
	
	public void setLoggingLevel(String loggingLevel) {
		try {
			
			Level level = Level.toLevel(loggingLevel);
			LogManager.getRootLogger().setLevel(level);

			logger.info("Logger is set to [" + level + "].");
			
		} catch (Exception e) {
			logger.error("Error setting logging level: " + e.getMessage(), e);
		}
	}
	
	public void configure(String applicationName) {
		
		String config = readConfigurationTemplate(applicationName);
		
		String root = getLoggingRoot();
		
		logger.info("Application logging directory is [" + root + "]");
		
		config = config.replaceAll("%loggingRoot%", root).trim();
		
		try {

			Document document = XmlUtil.parse(config);
			DOMConfigurator.configure(document.getDocumentElement());
			
		} catch (Exception e) {
			logger.error("Application logging configuration error:" + e.getMessage(), e);
		}
		
		logger.info("Application logging configured successfully.");
	
	}
	
	private String readConfigurationTemplate(String applicationName) {
		
		// try to read application specific log4j configuration from LDAP...
		

		String template = configurationManager.getStringValue("logs/applications/" + applicationName);
		if (StringUtils.isEmpty(template)) {
			template = configurationManager.getStringValue("logs/config");
		}

		if (StringUtils.isEmpty(template)) {
			try {
	
				InputStream is = LoggingManager.class.getClassLoader().getResourceAsStream("logging-failsafe.xml");
				template = IOUtils.toString(is);
				
			} catch (Exception e) {
				throw new RuntimeException("Unable to read local logging configuration file");
			}
		}
		
		return template;
	}
	
	public String getLoggingRoot() {
		return configurationManager.getStringValue("logs/root");
	}
	
}
