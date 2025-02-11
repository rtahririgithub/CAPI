package com.telus.cmb.subscriber.lifecyclemanager.dao;

import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.ChargeAdjustmentInfo;

public interface AdjustmentDao {

	/**
	 * Applies charges and Adjustment to account for the  Subscriber
	 * 
	 * NOT IN USE 
	 * @param List ChargeAdjustmentInfo
	 * @throws ApplicationException
	 */
	/*
	List<ChargeAdjustmentInfo> applyChargesAndAdjustmentsForSubscriber(
			 List<ChargeAdjustmentInfo> chargeInfoList ,String sessionId) throws ApplicationException;
			 */
}
