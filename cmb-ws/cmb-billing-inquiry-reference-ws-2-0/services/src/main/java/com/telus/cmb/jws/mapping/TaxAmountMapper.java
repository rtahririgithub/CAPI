/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping;

import com.telus.eas.account.info.TaxSummaryInfo;
import com.telus.cmb.jws.mapping.TaxPolicyMapper;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.TaxAmount;


/**
 * @author Ruxandra Cioraca
 *
 */
public class TaxAmountMapper extends AbstractSchemaMapper<TaxAmount, TaxSummaryInfo> {
	
	public TaxAmountMapper() {
		super(TaxAmount.class, TaxSummaryInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected TaxAmount performSchemaMapping(TaxSummaryInfo source, TaxAmount target) {
		
		target.setTaxationPolicy(new TaxPolicyMapper().mapToSchema(source.getTaxationPolicy()));
		target.setGstAmount(source.getGSTAmount());
		target.setHstAmount(source.getHSTAmount());
		target.setPstAmount(source.getPSTAmount());
	
		return target;
	}

}
