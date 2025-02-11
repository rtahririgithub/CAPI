package com.telus.cmb.subscriber.lifecyclehelper.dao;

import java.util.List;

import com.telus.eas.account.info.DepositHistoryInfo;

public interface DepositDao {

	
	/**
	 * Retrieves List of DepositHistoryInfo
	 * 
	 * @param Integer			ban
	 * @param String			subscriber
	 * @return
	 */
	List<DepositHistoryInfo> retrieveDepositHistory (int ban, String subscriber);
	/**
	 * @param integer		banId
	 * @param String		subscriberNo
	 * @param String		productType
	 * @return
	 */
	double retrievePaidSecurityDeposit( int banId, String subscriberNo, String productType);
}
