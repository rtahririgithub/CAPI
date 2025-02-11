package com.telus.cmb.jws.mapping.customer_management_common_30;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.FinancialHistoryInfo;
import com.telus.eas.account.info.MonthlyFinancialActivityInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.FinancialHistory;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.MonthlyFinancialActivity;


public class FinancialHistoryMapper extends AbstractSchemaMapper<FinancialHistory, FinancialHistoryInfo> {

	public FinancialHistoryMapper(){
		super(FinancialHistory.class, FinancialHistoryInfo.class);
	}


	@Override
	protected FinancialHistory performSchemaMapping(FinancialHistoryInfo source, FinancialHistory target) {

		target.setDebtSummary(new DebtSummaryMapper().mapToSchema(source.getDebtSummary0()));

		target.setDelinquentInd(source.isDelinquent());
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
		target.setWrittenOffInd(source.isWrittenOff());
		return super.performSchemaMapping(source, target);
	}

}
