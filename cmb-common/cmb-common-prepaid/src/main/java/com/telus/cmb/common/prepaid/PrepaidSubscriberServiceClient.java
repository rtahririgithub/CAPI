package com.telus.cmb.common.prepaid;
import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.PrepaidSubscriberServicePort;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.prepaidsubscriberservice_v3.GetDetail;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.prepaidsubscriberservice_v3.GetDetailResponse;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.prepaidsubscriberservice_v3.UpdateSubscriber;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.Subscriber;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;

public class PrepaidSubscriberServiceClient extends SoaBaseSvcClient {

	protected PrepaidSubscriberServicePort port;

	public void setPort(PrepaidSubscriberServicePort port) {
		this.port = port;
	}
	
	@Override
	public String ping() throws ApplicationException {
		return execute(new SoaCallback<String>() {
			@Override
			public String doCallback() throws Exception {
				Ping parameters = null;
				return port.ping(parameters).getVersion();
			}
		});
	}
	
	public void updateSubscriber(final Subscriber subscriber)
			throws ApplicationException {
		execute(new SoaCallback<Object>() {
			@Override
			public Object doCallback() throws Exception {
				UpdateSubscriber parameters = new UpdateSubscriber();
				parameters.setSubscriber(subscriber);
				port.updateSubscriber(parameters,new OriginatingUserType());
				return null;
			}
		});
	}
	public GetDetailResponse getDetail(final GetDetail parameters) throws ApplicationException {
		return execute(new SoaCallback<GetDetailResponse>() {
			@Override
			public GetDetailResponse doCallback() throws Exception {
				GetDetailResponse response = port.getDetail(parameters); 
				return response;
			}
		});
	}
	
	public void updateSubscriber(final UpdateSubscriber parameters) throws ApplicationException {
		execute(new SoaCallback<Object>() {
			@Override
			public Object doCallback() throws Exception {
				port.updateSubscriber(parameters, null);
				return null;
			}
		});
	}
	
	
}
