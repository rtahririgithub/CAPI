package com.telus.cmb.account.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.MemoInfo;

public interface MemoDao {
	
	void createMemoForSubscriber(MemoInfo pMemoInfo, String sessionId) throws ApplicationException;

	void createMemoForBan(MemoInfo pMemoInfo, String sessionId) throws ApplicationException;
}
