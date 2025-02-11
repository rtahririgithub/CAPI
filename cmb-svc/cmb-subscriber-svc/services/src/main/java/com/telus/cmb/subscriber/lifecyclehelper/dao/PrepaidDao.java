package com.telus.cmb.subscriber.lifecyclehelper.dao;


import java.util.List;

import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.PrepaidCallHistoryInfo;
import com.telus.eas.subscriber.info.PrepaidEventHistoryInfo;
import com.telus.eas.subscriber.info.PrepaidPromotionDetailInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.utility.info.PrepaidEventTypeInfo;

public interface PrepaidDao {
	
	
	/**
	 * @param phoneNumber
	 * @param from
	 * @param to
	 * @return
	 */
	List<PrepaidCallHistoryInfo> retrievePrepaidCallHistory(String phoneNumber, java.util.Date from, java.util.Date to);
	/**
	 * @param phoneNumber
	 * @param from
	 * @param to
	 * @return
	 */
	List<PrepaidEventHistoryInfo> retrievePrepaidEventHistory(String phoneNumber, java.util.Date from, java.util.Date to);
	/**
	 * @param phoneNumber
	 * @param from
	 * @param to
	 * @param prepaidEventTypes
	 * @return
	 */
	List<PrepaidEventHistoryInfo> retrievePrepaidEventHistory(String phoneNumber, java.util.Date from, java.util.Date to, PrepaidEventTypeInfo[] prepaidEventTypes);
	
	
}
