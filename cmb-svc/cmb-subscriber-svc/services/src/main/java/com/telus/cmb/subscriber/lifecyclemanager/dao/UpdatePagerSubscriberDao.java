package com.telus.cmb.subscriber.lifecyclemanager.dao;

import com.telus.api.ApplicationException;

@Deprecated
public interface UpdatePagerSubscriberDao extends UpdateSubscriberDao {
	 void sendTestPage(int ban, String subscriberId, String sessionId) throws ApplicationException;
}
