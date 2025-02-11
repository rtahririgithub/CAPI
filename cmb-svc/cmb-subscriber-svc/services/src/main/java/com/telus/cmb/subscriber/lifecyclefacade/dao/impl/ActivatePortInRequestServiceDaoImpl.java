package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.framework.resource.ResourceInvocationCallback;
import com.telus.cmb.subscriber.lifecyclefacade.dao.ActivatePortInRequestServiceDao;
import com.telus.cmb.wsclient.wlnp.apirs.ActivateBodyType;
import com.telus.cmb.wsclient.wlnp.apirs.ActivatePortInRequest;
import com.telus.cmb.wsclient.wlnp.apirs.ActivatePortInRequestData;
import com.telus.cmb.wsclient.wlnp.apirs.ActivatePortInRequestPort;
import com.telus.cmb.wsclient.wlnp.apirs.TypesHeaderType;
import com.telus.cmb.wsclient.wlnp.apirs.TypesPortTNType;
import com.telus.eas.portability.info.PortRequestInfo;

public class ActivatePortInRequestServiceDaoImpl extends WnpLegacyClient implements ActivatePortInRequestServiceDao {

	private static final String PRM_DATE_FORMAT = "MMddyyyyhhmmss";

	@Autowired
	private ActivatePortInRequestPort port;

	@Override
	public void activatePortInRequest(final PortRequestInfo portRequest, final String applicationId) throws ApplicationException {

		invoke( new ResourceInvocationCallback() {

			@Override
			public void doInCallback() throws Exception {
				
				ActivatePortInRequestData requestData = new ActivatePortInRequestData();

				// set request header
				TypesHeaderType header = new TypesHeaderType();
				header.setRequestId(portRequest.getPortRequestId());
				String appId = applicationId;
				if (appId.length() > 6)
					appId = appId.substring(0, 6);
				header.setOriginator(appId);
				header.setDestination("SMG");
				Calendar toDay = Calendar.getInstance();
				SimpleDateFormat formatter = new SimpleDateFormat(PRM_DATE_FORMAT);
				String timeStamp = formatter.format(toDay.getTime());
				header.setTimestamp(timeStamp);
				requestData.setHeader(header);

				// set request body

				TypesPortTNType type = new TypesPortTNType();
				type.setPhoneNumber(portRequest.getPhoneNumber());

				ActivateBodyType activateBodyType = new ActivateBodyType();
				activateBodyType.getPortTN().add(type);
				requestData.setActivateRequestBody(activateBodyType);

				ActivatePortInRequest request = new ActivatePortInRequest();
				request.setActivatePortInRequestData(requestData);

				port.activatePortInRequest(request);
			}
		}, "0001", "SUBS-SVC", "ACTV-PIRS", "WNP");
	}
}
