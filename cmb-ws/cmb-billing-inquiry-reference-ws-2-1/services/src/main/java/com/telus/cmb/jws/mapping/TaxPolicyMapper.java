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

import com.telus.eas.utility.info.TaxationPolicyInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.TaxPolicyType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode;

/**
 * @author Ruxandra Cioraca
 *
 */
public class TaxPolicyMapper extends AbstractSchemaMapper<TaxPolicyType, TaxationPolicyInfo> {
	
	public TaxPolicyMapper() {
		super(TaxPolicyType.class,TaxationPolicyInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected TaxPolicyType performSchemaMapping(TaxationPolicyInfo source, TaxPolicyType target) {
		
		target.setGstRate(source.getGSTRate());
		target.setHstRate(source.getHSTRate());
		target.setPstRate(source.getPSTRate());
		target.setProvinceCode(toEnum(source.getProvince(), ProvinceCode.class));
		target.setMinimumPSTTaxableAmount(source.getMinimumPSTTaxableAmount());
		target.setTaxationMethod(String.valueOf(source.getMethod()));
	
		return target;
	}

}
