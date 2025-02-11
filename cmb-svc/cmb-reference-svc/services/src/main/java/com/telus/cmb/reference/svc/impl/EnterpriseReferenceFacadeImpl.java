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

import com.telus.cmb.reference.svc.EnterpriseReferenceFacade;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.CountryInfo;
import com.telus.eas.utility.info.LanguageInfo;
import com.telus.eas.utility.info.ProvinceInfo;
import com.telus.eas.utility.info.StateInfo;
import com.telus.eas.utility.info.UnitTypeInfo;

/**
 * @author Pavel Simonovsky
 *
 */

@Stateless(name="EnterpriseReferenceFacade", mappedName="EnterpriseReferenceFacade")
@Remote(EnterpriseReferenceFacade.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Interceptors(SpringBeanAutowiringInterceptor.class)

public class EnterpriseReferenceFacadeImpl implements EnterpriseReferenceFacade {

	@Autowired
	private EnterpriseReferenceFacade facade;
	
	public void setEnterpriseReferenceFacade(EnterpriseReferenceFacade facade) {
		this.facade = facade;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.EnterpriseReferenceFacade#getCountries()
	 */
	@Override
	public CountryInfo[] getCountries() throws TelusException {
		return facade.getCountries();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.EnterpriseReferenceFacade#getLanguages()
	 */
	@Override
	public LanguageInfo[] getLanguages() throws TelusException {
		return facade.getLanguages();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.EnterpriseReferenceFacade#getProvinces()
	 */
	@Override
	public ProvinceInfo[] getProvinces() throws TelusException {
		return facade.getProvinces();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.EnterpriseReferenceFacade#getStates()
	 */
	@Override
	public StateInfo[] getStates() throws TelusException {
		return facade.getStates();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.EnterpriseReferenceFacade#getUnitTypes()
	 */
	@Override
	public UnitTypeInfo[] getUnitTypes() throws TelusException {
		return facade.getUnitTypes();
	}

}
