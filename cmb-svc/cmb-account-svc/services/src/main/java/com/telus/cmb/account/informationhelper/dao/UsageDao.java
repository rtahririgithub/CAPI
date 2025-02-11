package com.telus.cmb.account.informationhelper.dao;

import java.util.Collection;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.AirtimeUsageChargeInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;

public interface UsageDao {
	
	  /**
	   * @param banId        BAN
	   * @param featureCode  Normally "STD" which means standards airtime feature
	   * @return VoiceUsageSummaryInfo given the BAN and featureCode
	   */
	Collection<VoiceUsageSummaryInfo> retrieveVoiceUsageSummary(int ban, String featureCode);

	double retrieveUnpaidAirTimeTotal(int ban)  throws ApplicationException;

	/**
	 * @param int Billing Account Number
	 * @return AirtimeUsageChargeInfo for given the BAN
	 */
	AirtimeUsageChargeInfo retrieveUnpaidAirtimeUsageChargeInfo(int ban) throws ApplicationException;
}
