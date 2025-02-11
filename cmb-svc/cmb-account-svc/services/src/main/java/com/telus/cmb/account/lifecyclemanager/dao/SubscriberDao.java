package com.telus.cmb.account.lifecyclemanager.dao;

import java.util.Date;

import com.telus.api.ApplicationException;

public interface SubscriberDao {

	void cancelSubscribers(int ban, java.util.Date activityDate,String activityReasonCode
			, char depositReturnMethod,String[] subscriberId, String[] waiveReason, String userMemoText
			, String sessionId) throws ApplicationException;
	
    void suspendSubscribers(int ban, Date activityDate, String activityReasonCode, 
			String[] subscriberId, String userMemoText, String sessionId) throws ApplicationException;
    
    void restoreSuspendedSubscribers(int ban, Date restoreDate, String restoreReasonCode,
    		 String[] subscriberId, String restoreComment, String sessionId) throws ApplicationException;
}
