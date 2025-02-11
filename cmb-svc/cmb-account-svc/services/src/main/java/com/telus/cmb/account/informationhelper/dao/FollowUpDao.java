package com.telus.cmb.account.informationhelper.dao;
/*
 * Created by Inbaselvan Gandhi for WL10 Upgrade
 */
import java.util.Collection;
import java.util.List;

import com.telus.eas.account.info.FollowUpStatisticsInfo;
import com.telus.eas.account.info.FollowUpTextInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.utility.info.FollowUpCriteriaInfo;

public interface FollowUpDao {
	
	  /**
     * Retrieves Follow Up's additional text.
     *
     * @param ban
     * @param followUpId
     * @return
     */	
	List<FollowUpTextInfo> retrieveFollowUpAdditionalText(int ban, int followUpId);
	
	  /**
     * Retrieves the history for a given follow up.
     *
     * @param followUpId
     * @return
     */
	List<FollowUpInfo> retrieveFollowUpHistory(int followUpId);
	
	 /**
	   * Retrieve Follow Up Info for a given billing account number (BAN) and Follow Up ID
	   *
	   * @param   int      billing account number (BAN)
	   * @param   int  Follow Up ID
	   *
	   */
	FollowUpInfo retrieveFollowUpInfoByBanFollowUpID(int ban, int followUpID);
	
    /**
     * Retrieves the history for a given follow up.
     *
     * @param followUpCriteria
     * @return
     */
	List<FollowUpInfo> retrieveFollowUps(FollowUpCriteriaInfo followUpCriteria);
	
	 /**
	  * Retrieve Follow Up Info for a given billing account number (BAN) and Follow Up ID
	  *
	  * @param   int      billing account number (BAN)
	  * @param   int      maximum Follow Ups
	  *
	  */
	List<FollowUpInfo> retrieveFollowUps(int ban, int Count);
	
	  /**
	   * Retrieves statistics on the open follow ups for a given BAN.
	   *
	   * @param ban
	   * @return
	   */
	FollowUpStatisticsInfo retrieveFollowUpStatistics(int ban);
	
	/**
	   * Retrieve Last Follow Up ID a given billing account number (BAN) and Follow Up Type
	   *
	   * @param   int      billing account number (BAN)
	   * @param   String  Follow Up Type
	   */
	int retrieveLastFollowUpIDByBanFollowUpType(int ban, String followUpType);
}
