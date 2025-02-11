package com.telus.cmb.subscriber.lifecyclemanager.dao;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public interface NewIdenPcsSubscriberDao {
	void migrateSubscriber(SubscriberInfo srcSubscriberInfo
			, SubscriberInfo newSubscriberInfo
			, Date activityDate, SubscriberContractInfo subscriberContractInfo
			, com.telus.eas.equipment.info.EquipmentInfo newPrimaryEquipmentInfo
			, com.telus.eas.equipment.info.EquipmentInfo[] newSecondaryEquipmentInfo
			, MigrationRequestInfo migrationRequestInfo, String sessionId) throws ApplicationException;
	
	void releasePortedInSubscriber(SubscriberInfo subscriberInfo
			, String sessionId) throws ApplicationException;
}
