package com.telus.cmb.subscriber.messagebeans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageHandler;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.utility.activitylogging.svc.ActivityLoggingService;
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
import com.telus.eas.activitylog.queue.info.ActivityLoggingInfo;

public class ActivityLoggingServiceHandler implements MessageHandler<ActivityLoggingInfo> {

	private static final Log logger = LogFactory.getLog(ActivityLoggingServiceHandler.class);
	private ActivityLoggingService activityLoggingService = null;
	
	@Override
	public void handle(ActivityLoggingInfo activity, ClientIdentity clientIdentity, 
			MessageBeanContext beanContext) throws ApplicationException {
		
		ActivityLoggingResult   activityLoggingResult=null;
		String messageType=activity.getMessageType();
		
		logger.debug("ActivityLoggingService messageType : "+messageType);
		
		if(messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_SUBSCRIBER_STATUS)){
			 activityLoggingResult =getActivityLoggingService().logChangeSubscriberStatusActivity((ChangeSubscriberStatusActivity)activity);
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_EQUIPMENT)){
			activityLoggingResult = getActivityLoggingService().logChangeEquipmentActivity((ChangeEquipmentActivity)activity);
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_CONTRACT)){
			activityLoggingResult =getActivityLoggingService().logChangeContractActivity((ChangeContractActivity )activity);
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_PHONE_NUMBER)){
			activityLoggingResult =getActivityLoggingService().logChangePhoneNumberActivity((ChangePhoneNumberActivity )activity);
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_MOVE_SUBSCRIBER)){
			activityLoggingResult =getActivityLoggingService().logMoveSubscriberActivity((MoveSubscriberActivity )activity);
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_ACCOUNT_TYPE)){
			activityLoggingResult =getActivityLoggingService().logChangeAccountTypeActivity((ChangeAccountTypeActivity )activity);
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_ACCOUNT_ADDRESS)){
			activityLoggingResult =getActivityLoggingService().logChangeAccountAddressActivity((ChangeAccountAddressActivity )activity);
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_ACCOUNT_PIN)){
			activityLoggingResult =getActivityLoggingService().logChangeAccountPinActivity((ChangeAccountPinActivity  )activity);
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_PAYMENT_METHOD)){
			activityLoggingResult =getActivityLoggingService().logChangePaymentMethodActivity((ChangePaymentMethodActivity )activity);
		}else{
			logger.warn("Invalid Activity Type[ "+messageType+"] input for ActivityLoggingSErvice");
		}
		logger.debug("Result : "+activityLoggingResult.toString());
	}
	
	private ActivityLoggingService getActivityLoggingService() {
		if (activityLoggingService == null) {
			activityLoggingService = EJBUtil.getHelperProxy(ActivityLoggingService.class, EJBUtil.TELUS_CMBSERVICE_ACTIVITY_LOGGING);
		}
		return activityLoggingService;
	}

	
}
