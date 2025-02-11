package com.telus.cmb.account.informationhelper.dao;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;

public interface CreditCheckDao {

	/**
	* Retrieve credit info by billing account number (BAN)
	*
	* @param   String      billing account number (BAN)
	* @param   Char Product Type
	* @returns CreditInfo  related information
	* @see AccountInfo
	*/
	CreditCheckResultInfo retrieveLastCreditCheckResultByBan(int ban, String productType);

	/**
	 * Retrieve deposits by billing account number
	 * 
	 * @param ban - billing account number
	 * @return
	 */
	CreditCheckResultDepositInfo[] retrieveDepositsByBan(int ban);

}