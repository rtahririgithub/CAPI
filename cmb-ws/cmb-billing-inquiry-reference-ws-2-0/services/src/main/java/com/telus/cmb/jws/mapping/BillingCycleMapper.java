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
import com.telus.eas.utility.info.BillCycleInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.BillingCycle;

/**
 * @author Ruxandra Cioraca
 *
 */
public class BillingCycleMapper extends ReferenceMapper <BillingCycle, BillCycleInfo> {
	
	public BillingCycleMapper() {
		super(BillingCycle.class,BillCycleInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected BillingCycle performSchemaMapping(BillCycleInfo source, BillingCycle target) {
			
		target.setAllocationIndicator(source.getAllocationIndicator());
		target.setBillDay(source.getBillDay());
		target.setCloseDay(source.getCloseDay());
		target.setDueDay(source.getDueDay());
		target.setNumberOfAllocatedAccounts(source.getNumberOfAllocatedAccounts());
		target.setNumberOfAllocatedSuscribers(source.getNumberOfAllocatedSubscribers());
		
		return super.performSchemaMapping(source, target);
	}

}
