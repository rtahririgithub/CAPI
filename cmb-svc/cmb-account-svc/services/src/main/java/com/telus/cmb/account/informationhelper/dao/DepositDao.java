package com.telus.cmb.account.informationhelper.dao;

import java.util.Date;
import java.util.List;

import com.telus.eas.account.info.DepositAssessedHistoryInfo;
import com.telus.eas.account.info.DepositHistoryInfo;

public interface DepositDao {

	/**
	 * Returns deposit history
	 *
	 * @param pBan
	 * @param pFromDate
	 * @param pToDate
	 * @return
	 */
	List<DepositHistoryInfo> retrieveDepositHistory(int ban, Date fromDate, Date toDate);

	/**
	 * Returns deposit assessed history
	 *
	 * @param pBan
	 * @return
	 */
	List<DepositAssessedHistoryInfo> retrieveDepositAssessedHistoryList(int ban); 

	/**
	 * Returns original deposit assessed history
	 *
	 * @param ban
	 * @return
	 */
	List<DepositAssessedHistoryInfo> retrieveOriginalDepositAssessedHistoryList(int ban);
}
