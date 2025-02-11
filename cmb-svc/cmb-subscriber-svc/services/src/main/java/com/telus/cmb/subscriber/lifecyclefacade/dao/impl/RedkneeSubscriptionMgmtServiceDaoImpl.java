package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.redknee.util.crmapi.soap.subscriptions.xsd.subscriptions_v2.UpdateSubscriptionWithStateTransition;
import com.telus.api.ApplicationException;
import com.telus.cmb.framework.resource.ResourceInvocationCallback;
import com.telus.cmb.subscriber.lifecyclefacade.dao.RedkneeSubscriptionMgmtServiceDao;
import com.telus.cmb.wsclient.RedKneeSubscriptionManagementServicePort;
import com.redknee.util.crmapi.soap.subscriptions.xsd._2011._01.SubscriptionReference;
import com.redknee.util.crmapi.soap.common.xsd._2008._08.GenericParameter;
import com.redknee.util.crmapi.soap.common.xsd._2011._05.CRMRequestHeader;

public class RedkneeSubscriptionMgmtServiceDaoImpl extends WnpLegacyClient implements RedkneeSubscriptionMgmtServiceDao {

	@Autowired
	private RedKneeSubscriptionManagementServicePort port;
	
	private final static long  REDKNEE_SUBSCRIBER_STATE_ACTIVE = 1;
	private final static long  REDKNEE_SUBSCRIBER_STATE_CANCEL = 2;
	private final static String REDKNEE_SUBSCRIBER_ACTIVITY_HEADER_ID =  "SOA_SM_AppID";
	private final static String REDKNEE_SUBSCRIBER_NUM_PREFIX =  "1";
	private final static String REDKNEE_SUBSCRIBER_PORT_OUT_NAME=  "PortOut";





	@Override
	public void updateSubscriptionWithStateTransition(final String phoneNumber) throws ApplicationException {

		invoke(new ResourceInvocationCallback() {

			@Override
			public void doInCallback() throws Exception {

				UpdateSubscriptionWithStateTransition requestDataBody = new UpdateSubscriptionWithStateTransition();
				
				//Set SubscriptionRef value
				SubscriptionReference subRef =  new SubscriptionReference();
				subRef.setMobileNumber(REDKNEE_SUBSCRIBER_NUM_PREFIX.concat(phoneNumber));
				requestDataBody.setSubscriptionRef(subRef);
				
				//Set the header
				CRMRequestHeader header = new CRMRequestHeader();
				header.setUsername(REDKNEE_SUBSCRIBER_ACTIVITY_HEADER_ID);
				header.setPassword(REDKNEE_SUBSCRIBER_ACTIVITY_HEADER_ID);
				requestDataBody.setHeader(header);
				
				//Set the state change values
				requestDataBody.getCurrentState().add(REDKNEE_SUBSCRIBER_STATE_ACTIVE);
				requestDataBody.setNewState(REDKNEE_SUBSCRIBER_STATE_CANCEL);
				
				//Set the portOut indicator 
                GenericParameter portOutGenericParameter = new GenericParameter();
                portOutGenericParameter.setName(REDKNEE_SUBSCRIBER_PORT_OUT_NAME);
                portOutGenericParameter.setValue(Boolean.TRUE);
                requestDataBody.getParameters().add(portOutGenericParameter);

				port.updateSubscriptionWithStateTransition(requestDataBody);
			}

		}, "0001", "SUBS-SVC", "REDKNEE-SUB-MGMT", "WNP");
	}
}
