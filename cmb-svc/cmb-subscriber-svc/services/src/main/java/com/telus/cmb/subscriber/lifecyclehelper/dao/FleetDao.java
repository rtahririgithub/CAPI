package com.telus.cmb.subscriber.lifecyclehelper.dao;

import java.util.List;

import com.telus.eas.account.info.TalkGroupInfo;

public interface FleetDao {

	/**
	 * Retrieves List TalkGroupInfo  Details 
	 * 
	 * @param String		subscriberId
	 * @return	List
	 */
	List<TalkGroupInfo> retrieveTalkGroupsBySubscriber(String subscriberId); 
}
