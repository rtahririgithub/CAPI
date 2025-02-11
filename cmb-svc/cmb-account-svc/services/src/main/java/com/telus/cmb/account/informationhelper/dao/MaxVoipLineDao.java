package com.telus.cmb.account.informationhelper.dao;

import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.MaxVoipLineInfo;

public interface MaxVoipLineDao {
	
	/**
	 * Retrieves the next seat group ID value.
	 * 
	 * @return String
	 */
	String getNextSeatGroupId();
	
	/**
	 *  Creates the MaxVoipLine record in the client_service_capacity table.
	 *
	 *  @param  MaxVoipLineInfo
	 *  @return void
	 */
	void createMaxVoipLine(MaxVoipLineInfo maxVoipLineInfo);

	/**	
	 * Returns a list of MaxVoipLine records by BAN and subscription ID. If subscription ID == 0, returns all current
	 * records for each subscription ID associated with the BAN. Note, for each unique BAN/subscription ID combination,
	 * there should be only one current record.
	 * 
	 * @param int ban
	 * @param long subscriptionId
	 * @return List<MaxVoipLineInfo>
	 */
	List<MaxVoipLineInfo> getMaxVoipLineList(int ban, long subscriptionId);

	/**
	 * Creates a new current MaxVoipLine record for the BAN and subscription ID of each maxVoipLineInfo in the list, 
	 * and expires all previously current records for the BAN/subscription ID combination. 
	 * 
	 * @param List<MaxVoipLineInfo>
	 * @return void
	 */
	void updateMaxVoipLineList(List<MaxVoipLineInfo> maxVoipLineList);
	
	/**
	 * Creates a new current MaxVoipLine record for the BAN and subscription ID of the maxVoipLineInfo, and expires
	 * all previously current records for the BAN/subscription ID combination. 
	 * 
	 * @param MaxVoipLineInfo
	 * @return void
	 */
	void updateMaxVoipLine(MaxVoipLineInfo maxVoipLineInfo);
	
}