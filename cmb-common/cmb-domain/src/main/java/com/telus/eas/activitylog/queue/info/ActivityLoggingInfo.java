package com.telus.eas.activitylog.queue.info;

import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.eas.activitylog.domain.AbstractSubscriberActivity;



public abstract class ActivityLoggingInfo extends AbstractSubscriberActivity {

	private static final long serialVersionUID = -1372904776627122480L;
	
	public static final String MESSAGE_TYPE_CHANGE_SUBSCRIBER_STATUS = "changeSubscriberStatus";
	public static final String MESSAGE_TYPE_CHANGE_EQUIPMENT = "changeEquipment";
	public static final String MESSAGE_TYPE_CHANGE_CONTRACT = "changeContract";
	public static final String MESSAGE_TYPE_CHANGE_PHONE_NUMBER = "changePhoneNumber";
	public static final String MESSAGE_TYPE_MOVE_SUBSCRIBER = "moveSubscriber";
	public static final String MESSAGE_TYPE_CHANGE_ACCOUNT_TYPE = "changeAccountType";
	public static final String MESSAGE_TYPE_CHANGE_ACCOUNT_ADDRESS = "changeAccountAddress";
	public static final String MESSAGE_TYPE_CHANGE_ACCOUNT_PIN = "changeAccountPin";
	public static final String MESSAGE_TYPE_CHANGE_PAYMENT_METHOD = "changePaymentMethod";
	
	
	public ActivityLoggingInfo(ServiceRequestHeader header) {
		super(header);
		
	}
	
	public abstract String getMessageType() ;

   
	
}