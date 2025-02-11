/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.logging;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import com.telus.cmb.common.app.ApplicationServiceLocator;

/**
 * @author Pavel Simonovsky
 *
 */
public class LoggingManagerMBeanImpl extends StandardMBean implements LoggingManagerMBean {

	public LoggingManagerMBeanImpl() throws NotCompliantMBeanException {
		super(LoggingManagerMBean.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.common.logging.LoggingManagerMBean#getLogLevel()
	 */
	@Override
	public String getLogLevel() {
		return ApplicationServiceLocator.getInstance().getLoggingManager().getLoggingLevel();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.common.logging.LoggingManagerMBean#setLogLevel(java.lang.String)
	 */
	@Override
	public void setLogLevel(String loggingLevel) {
		ApplicationServiceLocator.getInstance().getLoggingManager().setLoggingLevel(loggingLevel);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.common.logging.LoggingManagerMBean#getLogRoot()
	 */
	@Override
	public String getLogRoot() {
		return ApplicationServiceLocator.getInstance().getLoggingManager().getLoggingRoot();
	}

}
