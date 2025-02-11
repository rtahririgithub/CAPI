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

import com.telus.eas.account.info.TaxExemptionInfo;
import com.telus.eas.utility.info.TaxationPolicyInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.TaxExemption;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.TaxPolicyType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode;

/**
 * @author Ruxandra Cioraca
 *
 */
public class TaxExemptionMapper extends AbstractSchemaMapper<TaxExemption, TaxExemptionInfo> {
	
	public TaxExemptionMapper() {
		super(TaxExemption.class,TaxExemptionInfo.class);
	}


	@Override
	protected TaxExemptionInfo performDomainMapping(TaxExemption source, TaxExemptionInfo target) {
		
		target.setGstExemptionInd(source.isGstExemptInd());
		target.setHstExemptionInd(source.isHstExemptInd());
		target.setPstExemptionInd(source.isPstExemptInd());
	
		return target;
	}

}
