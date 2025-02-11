package com.telus.cmb.account.informationhelper.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.telus.cmb.common.util.DateUtil;
import com.telus.eas.account.info.AccountInfo;

public class LightWeightAccountRetrievalMapper {
	private AccountInfo accountInfo;
	private ResultSet resultSet;
	private Date logicalDate;

	public LightWeightAccountRetrievalMapper(AccountInfo accountInfo, ResultSet resultSet, Date logicalDate) {
		this.accountInfo = accountInfo;
		this.resultSet = resultSet;
		this.logicalDate = logicalDate;
	}

	public void setCommonDetails() throws SQLException {

		accountInfo.setBanSegment(resultSet.getString("gl_segment"));
		accountInfo.setBanSubSegment(resultSet.getString("gl_subsegment"));
		accountInfo.setBrandId(resultSet.getInt("brand_id"));
		accountInfo.setStatus(resultSet.getString("ban_status").charAt(0));
		accountInfo.setStatusActivityReasonCode(resultSet.getString("status_actv_rsn_code"));
		accountInfo.setHomeProvince(resultSet.getString("home_province"));
		accountInfo.setBillCycle(resultSet.getInt("bill_cycle"));
		accountInfo.setBillCycleCloseDay(resultSet.getInt("cycle_close_day"));
		accountInfo.setDealerCode(resultSet.getString("dealer_code"));
		accountInfo.setSalesRepCode(resultSet.getString("sales_rep_code"));
		accountInfo.setEmail(resultSet.getString("email_address"));
		accountInfo.setLanguage(resultSet.getString("lang_pref"));
		setDebtSummary();
	}

	private void setDebtSummary() throws SQLException {
		Date collectionStatusEffDate = resultSet.getDate("cs_effective_date");
		double currentDue = 0;
		double pastDueAmount = 0;

		if (DateUtil.isSameDay(collectionStatusEffDate, logicalDate)) {
			currentDue = resultSet.getDouble("cu_age_bucket_0");
			pastDueAmount = resultSet.getDouble("cu_past_due_amt");
		} else if (DateUtil.isSameDay(DateUtil.addDay(collectionStatusEffDate, 1), logicalDate)) {
			currentDue = resultSet.getDouble("nx_age_bucket_0");
			pastDueAmount = resultSet.getDouble("nx_past_due_amt");
		} else {
			currentDue = resultSet.getDouble("fu_age_bucket_0");
			pastDueAmount = resultSet.getDouble("fu_past_due_amt");
		}
		// no Past Due Payments
		if (currentDue == 0 && pastDueAmount == 0) {
			accountInfo.getFinancialHistory0().getDebtSummary0().setCurrentDue(resultSet.getDouble("ar_balance"));
			accountInfo.getFinancialHistory0().getDebtSummary0().setPastDue(0);
		} else {
			accountInfo.getFinancialHistory0().getDebtSummary0().setCurrentDue(currentDue);
			accountInfo.getFinancialHistory0().getDebtSummary0().setPastDue(pastDueAmount);
		}
	}
}
