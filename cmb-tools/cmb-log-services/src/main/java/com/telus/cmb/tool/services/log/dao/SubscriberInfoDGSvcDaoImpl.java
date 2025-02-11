package com.telus.cmb.tool.services.log.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.telus.cis.wsclient.SubscriberInformationDataGridServicePort;
import com.telus.cmb.tool.services.log.domain.task.SubscriberInfo;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.subscriberinformationdatagridservicerequestresponse_v1.GetSubscriberByPhoneNumber;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.subscriberinformationdatagridservicerequestresponse_v1.GetSubscriberByPhoneNumberResponse;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.subscriberinformationdatagridservicerequestresponse_v1.SubscriberResponse;

@Component
@Primary
@Qualifier("datagrid")
public class SubscriberInfoDGSvcDaoImpl implements SubscriberInfoDao {

	@Autowired
	private SubscriberInformationDataGridServicePort port;

	public static final String SOURCE = "DATAGRID";
	
	@Override
	public SubscriberInfo getSubscriberBySubscriberId(String subscriberId) {
		try {
			GetSubscriberByPhoneNumber parameters = new GetSubscriberByPhoneNumber();
			parameters.setPhoneNum(subscriberId);
			GetSubscriberByPhoneNumberResponse response = port.getSubscriberByPhoneNumber(parameters);
			return mapToDomain(response.getSubscriberByPhoneNumberResponse());
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	private SubscriberInfo mapToDomain(SubscriberResponse subscriber) {
		
		SubscriberInfo subInfo = new SubscriberInfo();
		if (subscriber != null) {
			subInfo.setBan(subscriber.getBillingAccountNum());
			subInfo.setPhoneNumber(subscriber.getPhoneNum());
			subInfo.setSubscriberId(subscriber.getSubscriberId());
			subInfo.setSubscriptionId(String.valueOf(subscriber.getSubscriptionId()));
			subInfo.setFirstName(subscriber.getConsumerName().getFirstName());
			subInfo.setLastName(subscriber.getConsumerName().getLastName());
			subInfo.setEmail(subscriber.getEmailId());	
			subInfo.setStatus(subscriber.getStatusCd());
			subInfo.setDatasource(SOURCE);
		}
		
		return subInfo;
	}
	
}
