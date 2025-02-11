/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.subscriber.lifecyclemanager.dao;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.eas.subscriber.info.CallInfo;
import com.telus.eas.subscriber.info.CallListInfo;
import com.telus.eas.subscriber.info.UsageProfileAdditionalChargesListInfo;
import com.telus.eas.subscriber.info.UsageProfileListInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public interface UsageDao {
	
	void adjustCall(int ban, String subscriberId, String productType, int billSeqNo, Date channelSeizureDate,
			String messageSwitchId, double adjustmentAmount, String adjustmentReasonCode, String memoText, String usageProductType, String sessionId) throws ApplicationException;

	UsageProfileListInfo retrieveUsageProfileList(int ban, String subscriberId, int billSeqNo, String productType, String sessionId) throws ApplicationException;
	
	UsageProfileAdditionalChargesListInfo retrieveUsageProfileAdditionalChargesList(int ban, String subscriberId, int billSeqNo, String productType, String sessionId) throws ApplicationException;
	
	CallListInfo retrieveBilledCallsList(int ban, String subscriberId, String productType, int billSeqNo, char callType, Date fromDate, Date toDate, boolean getAll, String sessionId) throws ApplicationException;

	CallListInfo retrieveUnbilledCallsList(int ban, String subscriberId, String productType, Date fromDate, Date toDate, boolean getAll, String sessionId) throws ApplicationException;
	
	CallInfo retrieveCallDetails(int ban, String subscriberId, String productType, int billSeqNo, Date channelSeizureDate,
			String messageSwitchId,	String callProductType, String sessionId) throws ApplicationException;
	
	
}
