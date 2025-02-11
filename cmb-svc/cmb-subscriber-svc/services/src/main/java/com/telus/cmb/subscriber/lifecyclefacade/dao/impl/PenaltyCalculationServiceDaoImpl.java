package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.subscriber.lifecyclefacade.dao.PenaltyCalculationServiceDao;
import com.telus.cmb.wsclient.PenaltyCalculationServicePort;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.SubscriptionMSCInfo;
import com.telus.eas.subscriber.info.SubscriptionMSCResultInfo;
import com.telus.eas.subscriber.info.SubscriptionMSCSpendingInfo;
import com.telus.tmi.xmlschema.srv.cmo.contactmgmt.penaltycalculationservicerequestresponse_v3.ValidateSubscriptionMscList;
import com.telus.tmi.xmlschema.srv.cmo.contactmgmt.penaltycalculationservicerequestresponse_v3.SubscriptionMscRequest;
import com.telus.tmi.xmlschema.srv.cmo.contactmgmt.penaltycalculationservicerequestresponse_v3.SubscriptionMscResponse;
import com.telus.tmi.xmlschema.srv.cmo.contactmgmt.penaltycalculationservicerequestresponse_v3.ValidateSubscriptionMscListResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse;

public class PenaltyCalculationServiceDaoImpl extends SoaBaseSvcClient implements PenaltyCalculationServiceDao {
		
	@Autowired
	private PenaltyCalculationServicePort port;
	
	@Override
	public String ping() throws ApplicationException {
		return execute ( new SoaCallback<String>() {
			@Override
			public String doCallback() throws Throwable {				
				Ping parameters = new Ping();
				PingResponse response = port.ping(parameters);				
				if (response != null) {
					return response.getVersion();					
				}
				return null;
			}
		});
	}

	@Override
	public SubscriptionMSCResultInfo validateSubscriptionMSCList(final List<SubscriptionMSCSpendingInfo> subscriptionMSCSpendingList) 
			throws ApplicationException {
		return execute ( new SoaCallback<SubscriptionMSCResultInfo>() {			
			@Override
			public SubscriptionMSCResultInfo doCallback() throws Throwable {
				SubscriptionMSCResultInfo subscriptionMSCResult = new SubscriptionMSCResultInfo();
				List<SubscriptionMSCInfo> subscriptionMSCInfoList = new ArrayList<SubscriptionMSCInfo>();
				if (subscriptionMSCSpendingList != null) {
					ValidateSubscriptionMscList subscriptionMSCList = new ValidateSubscriptionMscList();
					List<SubscriptionMscRequest> subscriptionMSCRequestList = new ArrayList<SubscriptionMscRequest>();
					
					for (SubscriptionMSCSpendingInfo subscriptionMSCSpending : subscriptionMSCSpendingList) {
						subscriptionMSCRequestList.add(performSchemaMapping(subscriptionMSCSpending));
					}
					subscriptionMSCList.setSubscriptionMscRequestList(subscriptionMSCRequestList);	
					ValidateSubscriptionMscListResponse response = port.validateSubscriptionMscList(subscriptionMSCList);
					subscriptionMSCResult.setReturnCode(response.getReturnCode());
					List<SubscriptionMscResponse> subscriptionMSCResponseList = response.getSubscriptionMscResponseList();
					for (SubscriptionMscResponse subscriptionMSCResponse : subscriptionMSCResponseList) {						
						subscriptionMSCInfoList.add(performDomainMapping(subscriptionMSCResponse));
					}
				}
				subscriptionMSCResult.setSubscriptionMSCInfoList(subscriptionMSCInfoList);
				return subscriptionMSCResult;
			}

			private SubscriptionMSCInfo performDomainMapping(SubscriptionMscResponse subscriptionMSCResponse) {
				SubscriptionMSCInfo subscriptionMSCInfo = new SubscriptionMSCInfo();
				subscriptionMSCInfo.setSubscriptionId(subscriptionMSCResponse.getSubscriptionId());
				subscriptionMSCInfo.setReturnCode(subscriptionMSCResponse.getReturnCode());
				subscriptionMSCInfo.setMissedMSCAmount(subscriptionMSCResponse.getMissedMscAmt());
				return subscriptionMSCInfo;
			}

			private SubscriptionMscRequest performSchemaMapping(SubscriptionMSCSpendingInfo subscriptionMSCSpending) {
				SubscriptionMscRequest subscriptionMSCRequest = new SubscriptionMscRequest();
				subscriptionMSCRequest.setSubscriptionId(subscriptionMSCSpending.getSubscriptionId());
				subscriptionMSCRequest.setPlanSpentAmt(subscriptionMSCSpending.getPlanSpentAmount());
				subscriptionMSCRequest.setServiceSpentAmt(subscriptionMSCSpending.getServiceSpentAmount());
				return subscriptionMSCRequest;
			}
		});
	}
		
	@Override
	public TestPointResultInfo test() {
		final TestPointResultInfo resultInfo = new TestPointResultInfo();
		resultInfo.setTimestamp(new Date());
		resultInfo.setTestPointName("Penalty Calculation Service");
		try {
			String pingResult = ping();
			resultInfo.setResultDetail(pingResult);
			resultInfo.setPass(true);
		}catch (Throwable t) {
			resultInfo.setExceptionDetail(t);
			resultInfo.setPass(false);
		}
		
		return resultInfo;	
	}		
		
}
