package com.telus.cmb.account.informationhelper.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.MonthlyFinancialActivityInfo;

public class AccountExtAttribMapper {
	public static void mapClientConsentIndicatorCodes(AccountInfo accountInfo, ResultSet resultSet) throws SQLException {
		List<String> consentIndicatorList = new ArrayList<String>();

		if (resultSet != null) {
			while (resultSet.next()) {
				String indicator = resultSet.getString("cpui_cd");
				if (indicator != null) {
					consentIndicatorList.add(indicator);
				}
			}
		}

		accountInfo.setClientConsentIndicatorCodes(consentIndicatorList.toArray(new String[consentIndicatorList.size()]));
	}
	
	public static void setMonthlyFinancialDetails(AccountInfo accountInfo, ResultSet collectionResultSet, ResultSet dckResultSet, ResultSet financeHistoryResultSet) throws SQLException {
		String currentDateString;
		int currentMonth;
		int currentYear;
		int i, m;
		SimpleDateFormat dateFormat   =  new SimpleDateFormat("MM/dd/yyyy");

		// add Monthly Financial Activity
		currentDateString = dateFormat.format(new java.util.Date());
		currentMonth = Integer.parseInt(currentDateString.substring(0, 2));
		currentYear = Integer.parseInt(currentDateString.substring(6));


		String activities[] = populateFinancialDetailActivityCode(collectionResultSet);
		int[] dishonoredPaymentCounts = populateDishonoredPaymentCount(dckResultSet, financeHistoryResultSet);
		

		for (i = 0; i < 12; i++) {
			// Financial history month
			m = ((currentMonth + i + 1) % 12 == 0 ? 12 : (currentMonth + i + 1) % 12);
			MonthlyFinancialActivityInfo monthlyFinancialActivityInfo = new MonthlyFinancialActivityInfo();
			monthlyFinancialActivityInfo.setMonth(m);
			if (m <= currentMonth) {
				monthlyFinancialActivityInfo.setYear(currentYear);
			} else {
				monthlyFinancialActivityInfo.setYear(currentYear - 1);
			}
			setMonthlyFinancialDetailsActivityAndDishonoredPayment(monthlyFinancialActivityInfo, m, activities, dishonoredPaymentCounts);
			accountInfo.getFinancialHistory0().addMonthlyFinancialActivity(monthlyFinancialActivityInfo);
		}
	}
	
	private static String[] populateFinancialDetailActivityCode(ResultSet resultSet) throws SQLException {
		String activities[] = new String[12]; //indexed by month, 0=Jan
		
		for (int i = 0; i < activities.length; i++) {
			activities[i] = "";
		}
		
		while (resultSet.next()) {
			String activityDateMonthValue = resultSet.getString("actvDateMM");
			try {
				int activityDateMonth = Integer.valueOf(activityDateMonthValue);
				String activityCode = resultSet.getString("col_actv_code");
				activities[activityDateMonth-1] = (activityCode == null ? "" : activityCode.trim());
			}catch (NumberFormatException nfe) {
				
			}
		}
		
		return activities;
	}
	
	private static int[] populateDishonoredPaymentCount(ResultSet dckResultSet, ResultSet financeHistoryResultSet) throws SQLException {
		int[] count = new int[12]; //indexed by month, 0=Jan

		try {
			while (dckResultSet.next()) {
				int month = Integer.valueOf(dckResultSet.getString("mm_dck"));
				count[month - 1] = dckResultSet.getInt("nbr");
			}
			
			while (financeHistoryResultSet.next()) {
				int month = Integer.valueOf(financeHistoryResultSet.getString("mm_dck"));
				count[month - 1] += financeHistoryResultSet.getInt("nbr");
			}
		} finally {
			if (dckResultSet != null) {
				dckResultSet.close();
			}

			if (financeHistoryResultSet != null) {
				financeHistoryResultSet.close();
			}
		}

		return count;
	}
	
	private static void setMonthlyFinancialDetailsActivityAndDishonoredPayment(MonthlyFinancialActivityInfo monthlyFinancialActivityInfo, int m, String activities[], int[] dishonoredPaymentCounts) throws SQLException {
		if (m >= 1 && m <= 12) {
			monthlyFinancialActivityInfo.setActivity(activities[m-1]);
			monthlyFinancialActivityInfo.setDishonoredPaymentCount(dishonoredPaymentCounts[m-1]);
		}else {
			monthlyFinancialActivityInfo.setActivity("");
			monthlyFinancialActivityInfo.setDishonoredPaymentCount(0);
		}
	}
	
}
