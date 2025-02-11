package com.telus.cmb.subscriber.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

/**
 * 
 * @author tongts
 *
 */
public interface ProductDataMgmtDao {
	public void insertProductInstance (SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String userId) throws ApplicationException;
	public void updateProductInstance (SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String userId) throws ApplicationException;
	public void manageProductParameters (SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, String userId) throws ApplicationException;
	public void manageProductResources (SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String userId) throws ApplicationException;
	TestPointResultInfo test();
}
