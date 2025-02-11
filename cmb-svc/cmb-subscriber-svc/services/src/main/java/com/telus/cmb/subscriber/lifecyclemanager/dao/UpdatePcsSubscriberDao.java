package com.telus.cmb.subscriber.lifecyclemanager.dao;

import java.util.Date;
import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.subscriber.info.ResourceActivityInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public interface UpdatePcsSubscriberDao extends UpdateSubscriberDao {
	
	 void migrateSubscriber(SubscriberInfo subscriberInfo, int targetBan, Date activityDate
			 	, SubscriberContractInfo subscriberContractInfo
			 	, com.telus.eas.equipment.info.EquipmentInfo newPrimaryEquipmentInfo
			 	, com.telus.eas.equipment.info.EquipmentInfo[] newSecondaryEquipmentInfo
			 	, MigrationRequestInfo migrationRequestInfo, String sessionId) throws ApplicationException;
	 
	 void updateBirthDate(SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException;
	 
	 void changeResources(SubscriberInfo subscriberInfo, List<ResourceActivityInfo> resourceList, Date activityDate, String sessionId) throws ApplicationException;
	 
	 void changeSeatGroup(SubscriberInfo subscriberInfo, String seatGroupId, String sessionId) throws ApplicationException;
	 
}