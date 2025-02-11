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

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.BillAdjustmentInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.BillAdjustment;

/**
 * @author Gregory Bragg
 *
 */
public abstract class BillAdjustmentMapper<S extends BillAdjustment, T extends BillAdjustmentInfo> extends ReferenceMapper<S, T> {
	
	public BillAdjustmentMapper(Class<S> schemaClass, Class<T> domainClass) {
		super(schemaClass, domainClass);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected S performSchemaMapping(T source, S target) {
		
		target.setAmount(source.getAmount());
		target.setManualChargeInd(source.isManualCharge());
		target.setAmountOverrideableInd(source.isAmountOverrideable());
		
		return super.performSchemaMapping(source, target);
	}

}
