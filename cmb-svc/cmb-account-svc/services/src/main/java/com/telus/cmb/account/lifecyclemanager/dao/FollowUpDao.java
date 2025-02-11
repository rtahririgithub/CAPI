package com.telus.cmb.account.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.FollowUpUpdateInfo;
import com.telus.eas.framework.info.FollowUpInfo;

public interface FollowUpDao {

	void updateFollowUp(FollowUpUpdateInfo followUpUpdateInfo, String sessionId) throws ApplicationException;

	/**
	 * Create a new followUp
	 *
	 * @param   followUpInfo    all attributes for the follow-up (i.e. ban #, follow-up type, follow-up text etc)
	 *
	 * @see FollowUpInfo
	 */
	void createFollowUp(FollowUpInfo followUpInfo, String sessionId) throws ApplicationException;

}
