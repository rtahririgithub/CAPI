package com.telus.cmb.tool.services.log.dao;

import com.telus.cmb.tool.services.log.domain.task.SubscriberInfo;

public interface SubscriberInfoDao {

	public SubscriberInfo getSubscriberBySubscriberId(String subscriberId);
	
}
