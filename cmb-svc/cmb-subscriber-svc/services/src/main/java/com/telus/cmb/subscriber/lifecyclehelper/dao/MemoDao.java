package com.telus.cmb.subscriber.lifecyclehelper.dao;

import com.telus.eas.framework.info.MemoInfo;

public interface MemoDao {
	
	/**
	 * Retrieves MemoInfoObject
	 * 
	 * @param Integer		ban
	 * @param String		subscriberID
	 * @param String		memoType
	 * @return MemoInfo
	 */
	MemoInfo retrieveLastMemo(int ban, String subscriberID, String memoType);
}
