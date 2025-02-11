/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.perfmon;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import com.telus.cmb.common.app.ApplicationServiceLocator;

/**
 * @author Pavel Simonovsky
 *
 */
public class PerformanceMonitorMBeanImpl extends StandardMBean implements PerformanceMonitorMBean {

	public PerformanceMonitorMBeanImpl() throws NotCompliantMBeanException {
		super(PerformanceMonitorMBean.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.common.perfmon.PerformanceMonitorMBean#getReport()
	 */
	@Override
	public String getReport() {
		return ApplicationServiceLocator.getInstance().getPerformanceMonitor().getReport();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.common.perfmon.PerformanceMonitorMBean#reset()
	 */
	@Override
	public void reset() {
		ApplicationServiceLocator.getInstance().getPerformanceMonitor().reset();
	}

}
