/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.account_information_10;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.account_information_types_1.InvoiceProperties;

/**
 * @author Dimitry Siganevich
 *
 */
public class InvoicePropertiesMapper extends AbstractSchemaMapper<InvoiceProperties, InvoicePropertiesInfo> {

	public InvoicePropertiesMapper(){
		super(InvoiceProperties.class, InvoicePropertiesInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected InvoiceProperties performSchemaMapping(InvoicePropertiesInfo source, InvoiceProperties target) {
	      target.setBan(String.valueOf(source.getBan())); // changed for charge for Paper Bill
	      target.setHoldRedirectDestinationCode(source.getHoldRedirectDestinationCode());
	      target.setHoldRedirectFromDate(source.getHoldRedirectFromDate());
	      target.setHoldRedirectToDate(source.getHoldRedirectToDate());
	      target.setInvoiceSuppressionLevel(source.getInvoiceSuppressionLevel());
		
		return super.performSchemaMapping(source, target);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected InvoicePropertiesInfo performDomainMapping(InvoiceProperties source, InvoicePropertiesInfo target) {
	      target.setBan(Integer.valueOf(source.getBan()));
	      target.setHoldRedirectDestinationCode(source.getHoldRedirectDestinationCode());
	      if (source.getHoldRedirectFromDate() != null)
	        target.setHoldRedirectFromDate(source.getHoldRedirectFromDate());
	      if (source.getHoldRedirectToDate() != null)
	        target.setHoldRedirectToDate(source.getHoldRedirectToDate());
	      target.setInvoiceSuppressionLevel(source.getInvoiceSuppressionLevel());

		return super.performDomainMapping(source, target);
	}
}
