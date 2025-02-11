package com.telus.cmb.utility.activitylogging.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

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

public interface ActivityLoggingServiceRemote extends EJBObject {

	ActivityLoggingResult logChangeAccountTypeActivity(ChangeAccountTypeActivity activity)throws RemoteException;

	ActivityLoggingResult logChangeAccountAddressActivity(ChangeAccountAddressActivity activity)throws RemoteException;
	
	ActivityLoggingResult logChangeAccountPinActivity(ChangeAccountPinActivity activity)throws RemoteException; 

	ActivityLoggingResult logChangeEquipmentActivity(ChangeEquipmentActivity activity)throws RemoteException;
	
	ActivityLoggingResult logChangePhoneNumberActivity(ChangePhoneNumberActivity activity)throws RemoteException;
	
	ActivityLoggingResult logChangeSubscriberStatusActivity(ChangeSubscriberStatusActivity activity)throws RemoteException;
	
	ActivityLoggingResult logChangeContractActivity(ChangeContractActivity activity)throws RemoteException;
	
	ActivityLoggingResult logMoveSubscriberActivity(MoveSubscriberActivity activity)throws RemoteException;
	
	ActivityLoggingResult logChangePaymentMethodActivity(ChangePaymentMethodActivity activity)throws RemoteException; 
	
	ServiceRequestRelationshipType [] getServiceRequestRelationshipTypes()throws RemoteException;
	
	ServiceRequestNoteType [] getServiceRequestNoteTypes()throws RemoteException;
}
