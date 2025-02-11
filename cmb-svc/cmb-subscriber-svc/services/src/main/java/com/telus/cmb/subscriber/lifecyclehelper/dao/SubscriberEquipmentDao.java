package com.telus.cmb.subscriber.lifecyclehelper.dao;

import java.util.Hashtable;
import java.util.List;

import com.telus.eas.equipment.info.EquipmentSubscriberInfo;
import com.telus.eas.subscriber.info.EquipmentChangeHistoryInfo;
import com.telus.eas.subscriber.info.HandsetChangeHistoryInfo;

public interface SubscriberEquipmentDao {

	/**
	 * Retrieves Count for the given Repair ID
	 * 
	 * @param String		repairID
	 * @return	Integer		
	 */
	int getCountForRepairID(String repairID);
	/**
	 *Retrieves List of EquipmentChangeHistoryInfo Objects
	 * 
	 * @param Integer		ban
	 * @param String		subscriberID
	 * @param Date			from
	 * @param Date			to
	 * @return
	 */
	List<EquipmentChangeHistoryInfo> retrieveEquipmentChangeHistory(int ban, String subscriberID, java.util.Date from, java.util.Date to);
	/**
	 *Retrieves List of HandsetChangeHistoryInfo Objects
	 * 
	 * @param Integer		ban
	 * @param String		subscriberID
	 * @param Date			from
	 * @param Date			to
	 * @return
	 */
	List<HandsetChangeHistoryInfo> retrieveHandsetChangeHistory(int ban, String subscriberID, java.util.Date from, java.util.Date to);
	/**
	 * @param Array  if IMISIs
	 * @return
	 */
	Hashtable getUSIMListByIMSIs (String [] IMISIs );
	
	/**
	 * @param String		uSIM_Id
	 * @return
	 */
	List<String> getIMSIsByUSIM (String uSIM_Id);	
	/**
	 * @param serialNumber
	 * @param active
	 * @return
	 */
	List<EquipmentSubscriberInfo> retrieveEquipmentSubscribers(String serialNumber, boolean active); 
	/**
	 * Retrieves ISMIs List 
	 * 
	 * @param String		serialNumber
	 * @return
	 */
	List<String> getIMSIsBySerialNumber(String serialNumber); 
	
}
