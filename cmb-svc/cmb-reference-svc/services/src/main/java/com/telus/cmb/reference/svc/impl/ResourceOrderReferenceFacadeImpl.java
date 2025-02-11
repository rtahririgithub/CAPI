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

import com.telus.cmb.reference.svc.ResourceOrderReferenceFacade;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.NumberGroupInfo;

/**
 * @author Pavel Simonovsky
 *
 */

@Stateless(name="ResourceOrderReferenceFacade", mappedName="ResourceOrderReferenceFacade")
@Remote(ResourceOrderReferenceFacade.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Interceptors(SpringBeanAutowiringInterceptor.class)

public class ResourceOrderReferenceFacadeImpl implements ResourceOrderReferenceFacade {

	@Autowired
	private ResourceOrderReferenceFacade facade;
	
	public void setResourceOrderReferenceFacade(ResourceOrderReferenceFacade facade) {
		this.facade = facade;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ResourceOrderReferenceFacade#getAvailableNumberGroups(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public NumberGroupInfo[] getAvailableNumberGroups(String accountType, String accountSubType, String productType, String equipmentType, String marketAreaCode) throws TelusException {
		return facade.getAvailableNumberGroups(accountType, accountSubType, productType, equipmentType, marketAreaCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ResourceOrderReferenceFacade#getNumberGroupByPhoneNumberAndProductType(java.lang.String, java.lang.String)
	 */
	@Override
	public NumberGroupInfo getNumberGroupByPhoneNumberAndProductType(String phoneNumber, String productType) throws TelusException {
		return facade.getNumberGroupByPhoneNumberAndProductType(phoneNumber, productType);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ResourceOrderReferenceFacade#getNumberGroupByPortedInPhoneNumberAndProductType(java.lang.String, java.lang.String)
	 */
	@Override
	public NumberGroupInfo getNumberGroupByPortedInPhoneNumberAndProductType(String phoneNumber, String productType) throws TelusException {
		return facade.getNumberGroupByPortedInPhoneNumberAndProductType(phoneNumber, productType);
	}

}
