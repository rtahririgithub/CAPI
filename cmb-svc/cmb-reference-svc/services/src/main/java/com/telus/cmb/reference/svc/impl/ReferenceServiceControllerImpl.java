/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.svc.impl;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.cmb.common.app.ApplicationServiceLocator;
import com.telus.cmb.reference.svc.ReferenceServiceController;

/**
 * @author Pavel Simonovsky
 *
 */

@Stateless(name="ReferenceServiceController", mappedName="ReferenceServiceController")
@Remote(ReferenceServiceController.class)

@Interceptors(SpringBeanAutowiringInterceptor.class)

@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

public class ReferenceServiceControllerImpl implements ReferenceServiceController {
	
	@Autowired
	private ReferenceDataFacadeImpl referenceDataFacade;

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataServiceController#clearCache()
	 */
	@Override
	public void clearCache() {
		referenceDataFacade.clearCache();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataServiceController#getLoggingLevel()
	 */
	@Override
	public String getLoggingLevel() {
		return ApplicationServiceLocator.getInstance().getLoggingManager().getLoggingLevel();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataServiceController#getPerformanceData()
	 */
	@Override
	public String getPerformanceData() {
		return ApplicationServiceLocator.getInstance().getPerformanceMonitor().getReport();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataServiceController#restartCache()
	 */
	@Override
	public void restartCache() {
		referenceDataFacade.restartCache();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataServiceController#setLoggingLevel()
	 */
	@Override
	public void setLoggingLevel(String loggingLevel) {
		ApplicationServiceLocator.getInstance().getLoggingManager().setLoggingLevel(loggingLevel);
	}

	@Override
	public String getCacheConfiguration(String cacheName) {
		return referenceDataFacade.getCacheConfiguration(cacheName);
	}

	@Override
	public String getCacheStatistics(String cacheName) {
		return referenceDataFacade.getCacheStatistics(cacheName);
	}
	
	@Override
	public void clearCache(String cacheName) {
		referenceDataFacade.clearCache(cacheName);
	}

	@Override
	public void refreshAllCache() {
		referenceDataFacade.refreshAllCache();
	}

	@Override
	public void refreshCache(String cacheName) {
		if ("REFPDS".equalsIgnoreCase(cacheName)) {
			referenceDataFacade.initializePdsRefData();
		}else {
			referenceDataFacade.refreshCache(cacheName);
		}
	}
	

}
