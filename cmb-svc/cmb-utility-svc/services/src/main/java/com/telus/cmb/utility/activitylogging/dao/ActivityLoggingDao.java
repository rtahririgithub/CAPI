package com.telus.cmb.utility.activitylogging.dao;

import com.telus.api.reference.ServiceRequestNoteType;
import com.telus.api.reference.ServiceRequestRelationshipType;
import com.telus.eas.activitylog.domain.ActivityLoggingResult;
import com.telus.eas.activitylog.domain.ChangeAccountAddressActivity;
import com.telus.eas.activitylog.domain.ChangeAccountPinActivity;
import com.telus.eas.activitylog.domain.ChangeAccountTypeActivity;
import com.telus.eas.activitylog.domain.ChangeContractActivity;
import com.telus.eas.activitylog.domain.ChangeEquipmentActivity;
import com.telus.eas.activitylog.domain.ChangePaymentMethodActivity;
import com.telus.eas.activitylog.domain.ChangePhoneNumberActivity;
import com.telus.eas.activitylog.domain.ChangeSubscriberStatusActivity;
import com.telus.eas.activitylog.domain.MoveSubscriberActivity;


public interface ActivityLoggingDao {
	ActivityLoggingResult logChangeAccountTypeActivity(ChangeAccountTypeActivity activity);

	ActivityLoggingResult logChangeAccountAddressActivity(ChangeAccountAddressActivity activity);
	
	ActivityLoggingResult logChangeAccountPinActivity(ChangeAccountPinActivity activity); 

	ActivityLoggingResult logChangeEquipmentActivity(ChangeEquipmentActivity activity);
	
	ActivityLoggingResult logChangePhoneNumberActivity(ChangePhoneNumberActivity activity);
	
	ActivityLoggingResult logChangeSubscriberStatusActivity(ChangeSubscriberStatusActivity activity);
	
	ActivityLoggingResult logChangeContractActivity(ChangeContractActivity activity);
	
	ActivityLoggingResult logMoveSubscriberActivity(MoveSubscriberActivity activity);
	
	ActivityLoggingResult logChangePaymentMethodActivity(ChangePaymentMethodActivity activity); 
	
	ServiceRequestRelationshipType [] getServiceRequestRelationshipTypes();
	
	ServiceRequestNoteType [] getServiceRequestNoteTypes();

}
