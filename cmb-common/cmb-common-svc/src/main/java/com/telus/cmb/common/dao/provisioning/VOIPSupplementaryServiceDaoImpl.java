package com.telus.cmb.common.dao.provisioning;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.common.dao.provisioning.mapping.VOIPSupplementaryServiceMapper;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClientV2;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.common.dao.soa.spring.SoaDefaultErrorHandler;
import com.telus.cmb.wsclient.ServiceException_v3;
import com.telus.cmb.wsclient.VoipSupplementaryServicePort;
import com.telus.eas.account.info.VOIPAccountInfo;
import com.telus.tmi.xmlschema.srv.smo.activation.voipsupplementaryservicerequestresponse_v1.GetAccountInfo;
import com.telus.tmi.xmlschema.srv.smo.activation.voipsupplementaryservicerequestresponse_v1.GetOperatorSubscriptionId;
import com.telus.tmi.xmlschema.srv.smo.activation.voipsupplementaryservicerequestresponse_v1.GetOperatorSubscriptionIdResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingResponse;

public class VOIPSupplementaryServiceDaoImpl extends SoaBaseSvcClientV2 implements VOIPSupplementaryServiceDao {
	
	public static final String UNKNOWN_SUBSCRIPTION_ID = "0";

	@Autowired
	public VoipSupplementaryServicePort voipSupplementaryService_v1_1_PortType;
	
	public VOIPSupplementaryServiceDaoImpl() {
		super(new SoaDefaultErrorHandler() {
			
			@Override
			protected SystemException getSystemException(ServiceException_v3 cause) {

				String errorCode = getErrorCode(cause);
				String errorMessage = getErrorMsg(cause);
				String messageId = getErrorMsgId(cause);

				return new SystemException(SystemCodes.CMB_VOIP_SUPPLEMENTARY_DAO, errorCode,
						"ServiceException occurred while calling VOIPSupplementaryService: message ID [" + messageId + "]; errorCode [" + errorCode + "]; error message [" + errorMessage + "].",
						StringUtils.EMPTY, cause);
			}
			
		}, null);
	}
		
	public void setVoipSupplementaryServicePort(VoipSupplementaryServicePort voipSupplementaryService_v1_1_PortType) {
		this.voipSupplementaryService_v1_1_PortType = voipSupplementaryService_v1_1_PortType;
	}

	@Override
	public String getPrimaryStarterSeatSubscriptionId(final int ban) throws ApplicationException {
		
		return execute(new SoaCallback<String>() {
			@Override
			public String doCallback() throws Throwable {

				// Set request header
				GetOperatorSubscriptionId request = new GetOperatorSubscriptionId();								
				request.setBillingAccountNum(String.valueOf(ban));
				
				// Send the request
				GetOperatorSubscriptionIdResponse response = getResponseHandler().handleErrorResponse(voipSupplementaryService_v1_1_PortType.getOperatorSubscriptionId(request), SystemCodes.CMB_VOIP_SUPPLEMENTARY_DAO);
				
				return response != null && StringUtils.isNumeric(response.getSubscriptionId()) ? response.getSubscriptionId() : UNKNOWN_SUBSCRIPTION_ID;
			}
		});
	}

	private VOIPAccountInfo getAccountInfo(final int ban) throws ApplicationException {
		
		return execute(new SoaCallback<VOIPAccountInfo>() {
			@Override
			public VOIPAccountInfo doCallback() throws Throwable {

				// Set request header
				GetAccountInfo request = new GetAccountInfo();
				request.setBillingAccountNum(String.valueOf(ban));
				
				// Send the request and map the response for the account info; note we do not map the operator data returned here as it is unreliable
				return VOIPSupplementaryServiceMapper.VOIPAccountMapper().mapToDomain(getResponseHandler().handleErrorResponse(voipSupplementaryService_v1_1_PortType.getAccountInfo(request), SystemCodes.CMB_VOIP_SUPPLEMENTARY_DAO));
			}
		});
	}
	
	@Override
	public VOIPAccountInfo getVOIPAccountInfo(final int ban) throws ApplicationException {

		// Get the account info using getAccountInfo
		VOIPAccountInfo voipAccountInfo = getAccountInfo(ban);

		// Get the primary starter seat subscription ID using getPrimaryStarterSeatSubscriptionId, as the operator data returned by getAccountInfo is not reliable
		String subscriptionId = getPrimaryStarterSeatSubscriptionId(ban);
		voipAccountInfo.setOperatorSubscriptionId(Long.valueOf(subscriptionId));

		// Add the BAN to the return object
		voipAccountInfo.setBan(ban);

		return voipAccountInfo;
	}
	
	@Override
	public String ping() throws ApplicationException {
		
		return execute(new SoaCallback<String>() {
			
			@Override
			public String doCallback() throws Throwable {
			
				Ping ping = new Ping();
				ping.setDeepPing(true);
				ping.setOperationName("getOperatorSubscriptionId");
				PingResponse pingResponse = voipSupplementaryService_v1_1_PortType.ping(ping);
				String result = pingResponse.getPingStats().getServiceName() ;
				
				return result;
			}
		});
	}
	
}