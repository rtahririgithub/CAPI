/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.customer_management_common_10;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.FinancialHistoryInfo;
import com.telus.eas.account.info.MonthlyFinancialActivityInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.FinancialHistory;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.MonthlyFinancialActivity;

/**
 * @author Dimitry Siganevich
 *
 */
public class FinancialHistoryMapper extends AbstractSchemaMapper<FinancialHistory, FinancialHistoryInfo> {

	public FinancialHistoryMapper(){
		super(FinancialHistory.class, FinancialHistoryInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected FinancialHistory performSchemaMapping(FinancialHistoryInfo source, FinancialHistory target) {

		target.setDebtSummary(new DebtSummaryMapper().mapToSchema(source.getDebtSummary0()));

		target.setDelinquent(source.isDelinquent());
		target.setLastPaymentAmount(source.getLastPaymentAmount());
		target.setLastPaymentDate(source.getLastPaymentDate());
		MonthlyFinancialActivityInfo[] monthlyFinancialActivity = source.getMonthlyFinancialActivity0();
		if (monthlyFinancialActivity != null) {
			for (int i = 0; i < monthlyFinancialActivity.length; i++) {
				MonthlyFinancialActivity mFinancialActivity = new MonthlyFinancialActivity();

				mFinancialActivity.setActivity(monthlyFinancialActivity[i].getActivity());
				mFinancialActivity.setDishonoredPaymentCount(monthlyFinancialActivity[i].getDishonoredPaymentCount());
				mFinancialActivity.setMonth(monthlyFinancialActivity[i].getMonth());
				mFinancialActivity.setYear(monthlyFinancialActivity[i].getYear());
				target.getMonthlyFinancialActivities().add(mFinancialActivity);
			}
		}
		target.setWrittenOff(source.isWrittenOff());
		return super.performSchemaMapping(source, target);
	}

}
