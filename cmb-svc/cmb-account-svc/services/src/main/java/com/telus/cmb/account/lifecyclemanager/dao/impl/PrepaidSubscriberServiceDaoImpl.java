package com.telus.cmb.account.lifecyclemanager.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.dao.PrepaidSubscriberServiceDao;
import com.telus.cmb.common.prepaid.PrepaidSubscriberServiceClient;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.prepaidsubscriberservice_v3.UpdateSubscriber;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.Subscriber;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.Subscription;


@Component
public class PrepaidSubscriberServiceDaoImpl implements PrepaidSubscriberServiceDao {
	
	private static final Logger LOGGER = Logger.getLogger(PrepaidSubscriberServiceDaoImpl.class);
	
	@Autowired
	private PrepaidSubscriberServiceClient pssWSClient;
	
	
	@Override
	public void updateAccountPIN(String phoneNumber, String pin) throws ApplicationException {
		/* 
		 * updateSubscriberPIN -> PrepaidSubscriberService.updateSubscriber
		 * 
		 * BAN				- N/A
		 * MDN				- subscriber/id
		 * serialNo			- N/A
		 * prevPIN			- N/A
		 * PIN				- subscriber/pin
		 * user				- N/A
		 */
		
		//Log request
		logUpdateAccountPINReq(phoneNumber, pin);
		
		UpdateSubscriber parameters = new UpdateSubscriber();
		parameters.setSubscriber(createUpdatePinSubscriberObj(phoneNumber, pin));
		pssWSClient.updateSubscriber(parameters);
		
		//Log response
		LOGGER.info("UpdateAccountPIN successfully for " + phoneNumber);
	}
	
	private void logUpdateAccountPINReq(String phoneNumber, String pin) {
		if (LOGGER.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("UpdateAccountPIN");
			sb.append(" | PhoneNumber: " + phoneNumber);
			sb.append(" | PIN: " + pin);
			LOGGER.info(sb.toString());
		}
	}
	
	@Override
	public TestPointResultInfo test() {
		return pssWSClient.test();
	}
	
	
	
	//Helper methods
	
	private Subscriber createUpdatePinSubscriberObj(String phoneNumber, String pin) {
		Subscriber result = new Subscriber();
		Subscription subscription = new Subscription();
		if (StringUtils.isNotBlank(phoneNumber))
			subscription.setId(phoneNumber);
		if (StringUtils.isNotBlank(pin))
			subscription.setPin(pin);
		result.setSubscription(subscription);
		return result;
	}

}
