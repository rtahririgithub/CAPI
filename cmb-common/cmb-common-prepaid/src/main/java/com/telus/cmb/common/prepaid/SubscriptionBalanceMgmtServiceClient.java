package com.telus.cmb.common.prepaid;

import org.springframework.stereotype.Component;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.SubscriptionBalanceMgmtServicePort;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.srv.cmo.receivablesmgmt.subscriptionbalancemgmtservicerequestresponse_v1.Charge;
import com.telus.tmi.xmlschema.srv.cmo.receivablesmgmt.subscriptionbalancemgmtservicerequestresponse_v1.ChargeResponse;
import com.telus.tmi.xmlschema.srv.cmo.receivablesmgmt.subscriptionbalancemgmtservicerequestresponse_v1.Credit;
import com.telus.tmi.xmlschema.srv.cmo.receivablesmgmt.subscriptionbalancemgmtservicerequestresponse_v1.CreditResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;


@Component
public class SubscriptionBalanceMgmtServiceClient extends SoaBaseSvcClient {
	
	public static  String IP_ADDRESS="127.0.0.1";

	protected SubscriptionBalanceMgmtServicePort port;

	public void setPort(SubscriptionBalanceMgmtServicePort port) {
		this.port = port;
	}
	
	@Override
	public String ping() throws ApplicationException {
		return execute(new SoaCallback<String>() {
			@Override
			public String doCallback() throws Exception {
				return port.ping(new Ping()).getVersion();
			}
		});
	}
	
	public CreditResponse credit(final Credit parameters, final OriginatingUserType creditInSoapHdr) throws ApplicationException {
		return execute(new SoaCallback<CreditResponse>() {
			@Override
			public CreditResponse doCallback() throws Exception {
				CreditResponse creditResponse = port.credit(parameters, creditInSoapHdr); 
				return creditResponse;
			}
		});
	}
	
	public ChargeResponse charge(final Charge parameters, final OriginatingUserType chargeInSoapHdr) throws ApplicationException {
		return execute(new SoaCallback<ChargeResponse>() {
			@Override
			public ChargeResponse doCallback() throws Exception {
				ChargeResponse chargeResponse = port.charge(parameters, chargeInSoapHdr); 
				return chargeResponse;
			}
		});
	}
	
	
}
