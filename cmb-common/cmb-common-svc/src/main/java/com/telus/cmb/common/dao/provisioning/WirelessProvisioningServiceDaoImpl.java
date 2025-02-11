package com.telus.cmb.common.dao.provisioning;

import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.WirelessProvisioningPort;
import com.telus.eas.utility.info.ProvisioningRequestInfo;
import com.telus.tmi.xmlschema.srv.smo.activation.wirelessprovisioningservicerequestresponse_v1.Parameter;
import com.telus.tmi.xmlschema.srv.smo.activation.wirelessprovisioningservicerequestresponse_v1.ParameterList;
import com.telus.tmi.xmlschema.srv.smo.activation.wirelessprovisioningservicerequestresponse_v1.SubmitProvisioningOrder;
import com.telus.tmi.xmlschema.srv.smo.activation.wirelessprovisioningservicerequestresponse_v1.SubmitProvisioningOrderResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingResponse;

public class WirelessProvisioningServiceDaoImpl extends SoaBaseSvcClient implements WirelessProvisioningServiceDao {

	private final Logger logger = Logger.getLogger(WirelessProvisioningServiceDaoImpl.class);
	
	@Autowired
	public WirelessProvisioningPort wirelessProvisioningServiceV10PortType;
	
	public void setWirelessProvisioningServicePort(WirelessProvisioningPort wirelessProvisioningServiceV10PortType) {
		this.wirelessProvisioningServiceV10PortType = wirelessProvisioningServiceV10PortType;
	}
	
	public void submitProvisioningOrder(final ProvisioningRequestInfo provisioningRequestInfo) throws ApplicationException {
		
		execute(new SoaCallback<Object>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Object doCallback() throws Throwable {

				logger.info(provisioningRequestInfo.toString());
				
				SubmitProvisioningOrder request = new SubmitProvisioningOrder();
				
				// Set request header
				request.setBillingAccountNum(provisioningRequestInfo.getBan());
				request.setBillingTypeCd(provisioningRequestInfo.getBillingType());
				request.setBrandCd(provisioningRequestInfo.getBrand());
				request.setActionTypeCd(provisioningRequestInfo.getRequestActionType());
				request.setSourceSystemCd(provisioningRequestInfo.getSourceSystemCode());
				
				// Add request parameters
				ParameterList paramList = new ParameterList();
				List<Parameter> listParams = paramList.getParameter();
				for (Object object : provisioningRequestInfo.getRequestParams().entrySet()) {
					Entry<String, String> entry = (Entry) object;
					Parameter param = new Parameter();
					param.setName(entry.getKey());
					param.setValue(entry.getValue());
					listParams.add(param);
				}
				request.setParameterList(paramList);
				
				// Send the request
				SubmitProvisioningOrderResponse response = wirelessProvisioningServiceV10PortType.submitProvisioningOrder(request);
				
				logger.info(response.toString());

				return null;
			}
		});
	}

	@Override
	public String ping() throws ApplicationException {
		
		return execute(new SoaCallback<String>() {
			@Override
			public String doCallback() throws Throwable {
				Ping ping = new Ping();
				ping.setDeepPing(true);
				ping.setOperationName("submitProvisioningOrder");
				PingResponse pingResponse = wirelessProvisioningServiceV10PortType.ping(ping);
				String result = pingResponse.getPingStats().getServiceName() ;
				return result;
			}
		});
	}
	
}