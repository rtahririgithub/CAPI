/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.account_information_20;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.InvoicePropertyListType;



public class InvoicePropertiesMapper extends AbstractSchemaMapper<InvoicePropertyListType, InvoicePropertiesInfo> {

	public InvoicePropertiesMapper(){
		super(InvoicePropertyListType.class, InvoicePropertiesInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected InvoicePropertyListType performSchemaMapping(InvoicePropertiesInfo source, InvoicePropertyListType target) {
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
	protected InvoicePropertiesInfo performDomainMapping(InvoicePropertyListType source, InvoicePropertiesInfo target) {
	      target.setHoldRedirectDestinationCode(source.getHoldRedirectDestinationCode());
	      if (source.getHoldRedirectFromDate() != null)
	        target.setHoldRedirectFromDate(source.getHoldRedirectFromDate());
	      if (source.getHoldRedirectToDate() != null)
	        target.setHoldRedirectToDate(source.getHoldRedirectToDate());
	      target.setInvoiceSuppressionLevel(source.getInvoiceSuppressionLevel());

		return super.performDomainMapping(source, target);
	}
}
