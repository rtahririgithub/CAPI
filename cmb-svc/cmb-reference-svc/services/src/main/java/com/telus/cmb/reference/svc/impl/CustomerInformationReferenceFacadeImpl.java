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

import com.telus.cmb.reference.svc.CustomerInformationReferenceFacade;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.AccountTypeInfo;
import com.telus.eas.utility.info.MemoTypeInfo;
import com.telus.eas.utility.info.SegmentationInfo;

/**
 * @author Pavel Simonovsky
 *
 */

@Stateless(name="CustomerInformationReferenceFacade", mappedName="CustomerInformationReferenceFacade")
@Remote(CustomerInformationReferenceFacade.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Interceptors(SpringBeanAutowiringInterceptor.class)

public class CustomerInformationReferenceFacadeImpl implements CustomerInformationReferenceFacade {

	@Autowired
	private CustomerInformationReferenceFacade facade;
	
	public void setCustomerInformationReferenceFacade(CustomerInformationReferenceFacade facade) {
		this.facade = facade;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.CustomerInformationReferenceFacade#getAccountType(java.lang.String, int)
	 */
	@Override
	public AccountTypeInfo getAccountType(String accountTypeCode, int accountTypeBrandId) throws TelusException {
		return facade.getAccountType(accountTypeCode, accountTypeBrandId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.CustomerInformationReferenceFacade#getAccountTypes()
	 */
	@Override
	public AccountTypeInfo[] getAccountTypes() throws TelusException {
		return facade.getAccountTypes();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.CustomerInformationReferenceFacade#getMemoType(java.lang.String)
	 */
	@Override
	public MemoTypeInfo getMemoType(String memoTypeCode) throws TelusException {
		return facade.getMemoType(memoTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.CustomerInformationReferenceFacade#getMemoTypes()
	 */
	@Override
	public MemoTypeInfo[] getMemoTypes() throws TelusException {
		return facade.getMemoTypes();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.CustomerInformationReferenceFacade#getSegmentation(int, java.lang.String, java.lang.String)
	 */
	@Override
	public SegmentationInfo getSegmentation(int brandId, String accountTypeCode, String provinceCode) throws TelusException {
		return facade.getSegmentation(brandId, accountTypeCode, provinceCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.CustomerInformationReferenceFacade#getSegmentations()
	 */
	@Override
	public SegmentationInfo[] getSegmentations() throws TelusException {
		return facade.getSegmentations();
	}

}
