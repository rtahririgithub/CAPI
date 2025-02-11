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

import com.telus.cmb.reference.svc.CustomerOrderReferenceFacade;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.SalesRepInfo;

/**
 * @author Pavel Simonovsky
 *
 */

@Stateless(name="CustomerOrderReferenceFacade", mappedName="CustomerOrderReferenceFacade")
@Remote(CustomerOrderReferenceFacade.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Interceptors(SpringBeanAutowiringInterceptor.class)

public class CustomerOrderReferenceFacadeImpl implements CustomerOrderReferenceFacade {

	@Autowired
	private CustomerOrderReferenceFacade facade;
	
	public void setCustomerOrderReferenceFacade(CustomerOrderReferenceFacade facade) {
		this.facade = facade;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.CustomerOrderReferenceFacade#getDealerSalesRep(java.lang.String, java.lang.String)
	 */
	@Override
	public SalesRepInfo getDealerSalesRep(String dealerCode, String salesRepCode) throws TelusException {
		return facade.getDealerSalesRep(dealerCode, salesRepCode);
	}

}
