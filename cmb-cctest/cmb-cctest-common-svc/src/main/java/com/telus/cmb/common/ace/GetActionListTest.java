package com.telus.cmb.common.ace;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.junit.Test;

import com.telus.cmb.common.dao.ace.AceAdvisorActionInfo;
import com.telus.cmb.common.dao.ace.GetActionListImpl;
import com.telus.cmb.common.dao.ace.RealTimeActionAdvisorNotificationMapper;
import com.telus.cmb.wsclient.RealTimeActionAdvisorPort;
import com.telus.cmb.wsclient.RealTimeActionAdvisorServiceV20;

public class GetActionListTest {

	@Test
	public void test() throws Throwable {
		String phoneNumber = "4184180127";
		String channelType = "TELUS_WLS_NOTIFICATION";
		Map<String, String> channelAttributeMap = new HashMap<String, String>();
		channelAttributeMap.put(RealTimeActionAdvisorNotificationMapper.ATTRIBUTE_REQUEST_TYPE, RealTimeActionAdvisorNotificationMapper.REQUEST_TYPE_PORT_OUT_CANCELLATION);
		channelAttributeMap.put(RealTimeActionAdvisorNotificationMapper.ATTRIBUTE_ACCOUNT_TYPE, "I");
		channelAttributeMap.put(RealTimeActionAdvisorNotificationMapper.ATTRIBUTE_ACCOUNT_SUBTYPE, "R");

		GetActionListImpl impl = new GetActionListImpl(getService());
		try {
			List<AceAdvisorActionInfo> actionList = impl.getActionList(phoneNumber, channelType, channelAttributeMap);
			for (AceAdvisorActionInfo action : actionList) {
				System.out.println(action);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	private RealTimeActionAdvisorPort getService() throws Exception {
		String url = "https://soa-mp-laird-pt148.tsl.telus.com:443/v2/mso/campaignmgmt/actionadvisor/realtimeactionadvisorservice-v2-0_vs0";
		
		RealTimeActionAdvisorServiceV20 service = new RealTimeActionAdvisorServiceV20();
		RealTimeActionAdvisorPort port = service.getRealTimeActionAdvisorServicePort();

		Map<String, Object> requestContext = ((BindingProvider)port).getRequestContext();
		requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
		requestContext.put(BindingProvider.USERNAME_PROPERTY, "SOA Application Identity\\ClientAPI_EJB");
		requestContext.put(BindingProvider.PASSWORD_PROPERTY, "soaorgid");

	    return port;
	}
}
