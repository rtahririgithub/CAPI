/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.customer_management_common_30;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.DebtSummaryInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.DebtSummary;


public class DebtSummaryMapper extends AbstractSchemaMapper<DebtSummary, DebtSummaryInfo> {

	public DebtSummaryMapper(){
		super(DebtSummary.class, DebtSummaryInfo.class);
	}


	@Override
	protected DebtSummary performSchemaMapping(DebtSummaryInfo source, DebtSummary target) {
		target.setBillDueDate(source.getBillDueDate());
		target.setCurrentDue(source.getCurrentDue());
		target.setPastDue(source.getPastDue());
		target.setPastDue1To30Days(source.getPastDue1to30Days());
		target.setPastDue31To60Days(source.getPastDue31to60Days());
		target.setPastDue61To90Days(source.getPastDue61to90Days());
		target.setPastDueOver90Days(source.getPastDueOver90Days());

		return super.performSchemaMapping(source, target);
	}

}
