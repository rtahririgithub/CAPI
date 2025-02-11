package com.telus.cmb.subscriber.lifecyclehelper.dao;

import java.util.List;

import com.telus.eas.account.info.VoiceUsageSummaryInfo;

public interface UsageDao {
	
	/**
	 * Returns VoiceusageInfo Details
	 * 
	 * @param Integer			banId
	 * @param String 			subscriberId
	 * @param String			featureCode
	 * @return
	 */
	VoiceUsageSummaryInfo retrieveVoiceUsageSummary(int banId, String subscriberId, String featureCode); 
}
