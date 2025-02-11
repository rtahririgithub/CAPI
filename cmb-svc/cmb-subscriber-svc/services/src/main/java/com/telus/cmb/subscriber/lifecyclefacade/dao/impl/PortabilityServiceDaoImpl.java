package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.framework.resource.ResourceExecutionCallback;
import com.telus.cmb.subscriber.lifecyclefacade.dao.PortabilityServiceDao;
import com.telus.cmb.wsclient.wlnp.prts.GetLocalServiceProviderAndPortabilityByWtnRequest;
import com.telus.cmb.wsclient.wlnp.prts.GetLocalServiceProviderAndPortabilityByWtnResponse;
import com.telus.cmb.wsclient.wlnp.prts.PortabilityPort;
import com.telus.eas.portability.info.LocalServiceProviderInfo;

public class PortabilityServiceDaoImpl extends WnpLegacyClient implements PortabilityServiceDao {

	@Autowired
	private PortabilityPort port;
	
	@Override
	public LocalServiceProviderInfo getLocalServiceProvider(final String phoneNumber) throws ApplicationException {

		return execute( new ResourceExecutionCallback<LocalServiceProviderInfo>() {
			
			@Override
			public LocalServiceProviderInfo doInCallback() throws Exception {

				LocalServiceProviderInfo localServiceProviderInfo = new LocalServiceProviderInfo();

				GetLocalServiceProviderAndPortabilityByWtnRequest request = new GetLocalServiceProviderAndPortabilityByWtnRequest();
				request.setWtn(phoneNumber);

				GetLocalServiceProviderAndPortabilityByWtnResponse response = port.getLocalServiceProviderAndPortabilityByWtn(request);

				if (response != null) {
					localServiceProviderInfo.setLocalServiceProviderId(response.getLspId());
					localServiceProviderInfo.setLocalServiceProviderType(response.getLspType());
					localServiceProviderInfo.setLocationRoutingNumber(response.getLrn());
					localServiceProviderInfo.setPortableIndicator(response.isPortabilityIndicator());
				}

				return localServiceProviderInfo;
			}
		}, "0001", "SUBS-SVC", "PORTBLS", "WNP");
	}
}
