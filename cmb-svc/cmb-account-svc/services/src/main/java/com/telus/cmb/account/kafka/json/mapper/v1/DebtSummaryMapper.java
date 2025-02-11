package com.telus.cmb.account.kafka.json.mapper.v1;

import com.telus.cmb.common.kafka.account_v1_0.DebtSummary;
import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.DebtSummaryInfo;

public class DebtSummaryMapper extends AbstractSchemaMapper<DebtSummary, DebtSummaryInfo> {

	private static DebtSummaryMapper INSTANCE = null;

	public DebtSummaryMapper() {
		super(DebtSummary.class, DebtSummaryInfo.class);
	}

	public static synchronized DebtSummaryMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DebtSummaryMapper();
		}

		return INSTANCE;
	}

	@Override
	protected DebtSummary performSchemaMapping(DebtSummaryInfo source,
			DebtSummary target) {
		target.setCurrentDueAmt(source.getCurrentDue());
		target.setPastDueAmt(source.getPastDue());
		target.setAccountRealtimeBalance(source.getAccountRealTimeBalance());
		target.setAmtPastDue1To30Days(source.getPastDue1to30Days());
		target.setAmtPastDue31To60Days(source.getPastDue31to60Days());
		target.setAmtPastDue61To90Days(source.getPastDue61to90Days());
		target.setAmtPastDueOver90Days(source.getPastDueOver90Days());
		return super.performSchemaMapping(source, target);
	}

}
