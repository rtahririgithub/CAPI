package com.telus.cmb.tool.services.log.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.telus.cis.wsclient.SubscriberInformationServicePort;
import com.telus.cmb.tool.services.log.domain.task.SubscriberInfo;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.subscriberinformationservicerequestresponse_v3.GetSubscriberBySubscriberId;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.subscriberinformationservicerequestresponse_v3.GetSubscriberBySubscriberIdResponse;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.Subscriber;

@Component
@Qualifier("database")
public class SubscriberInfoSvcDaoImpl implements SubscriberInfoDao {

	@Autowired
	private SubscriberInformationServicePort port;

	public static final String SOURCE = "DATABASE";
	
	@Override
	public SubscriberInfo getSubscriberBySubscriberId(String subscriberId) {
		try {
			GetSubscriberBySubscriberId parameters = new GetSubscriberBySubscriberId();
			parameters.setSubscriberId(subscriberId);
			GetSubscriberBySubscriberIdResponse response = port.getSubscriberBySubscriberId(parameters);
			return mapToDomain(response.getSubscriber());
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	private SubscriberInfo mapToDomain(Subscriber subscriber) {
		
		SubscriberInfo subInfo = new SubscriberInfo();
		if (subscriber != null) {
			subInfo.setBan(Integer.parseInt(subscriber.getBillingAccountNumber()));
			subInfo.setPhoneNumber(subscriber.getPhoneNumber());
			subInfo.setSubscriberId(subscriber.getSubscriberId());
			subInfo.setSubscriptionId(String.valueOf(subscriber.getSubscriptionId()));
			subInfo.setFirstName(subscriber.getConsumerName().getFirstName());
			subInfo.setLastName(subscriber.getConsumerName().getLastName());
			subInfo.setEmail(subscriber.getEmailAddress());	
			subInfo.setStatus(subscriber.getStatus().value());
			subInfo.setDatasource(SOURCE);
		}
		
		return subInfo;
	}
	
}
