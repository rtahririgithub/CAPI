package com.telus.cmb.account.informationhelper.dao;

import java.util.Date;

import com.telus.eas.account.info.AddressHistoryInfo;
import com.telus.eas.account.info.AddressInfo;

public interface AddressDao {
	AddressInfo retrieveAlternateAddressByBan(int ban);

	/**
	 * Returns Address history
	 *
	 * @param   pBan       				billing account number (BAN)
	 * @param   pFromDate				from Date
	 * @param   pToDate     			to Date
	 * @return AddressHistoryInfo[]  	array of AddressHistoryInfo
	 *
	 */
	AddressHistoryInfo[] retrieveAddressHistory (int pBan, Date pFromDate, Date pToDate); 
	
}
