package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.framework.resource.ResourceInvocationCallback;
import com.telus.cmb.subscriber.lifecyclefacade.dao.SubmitPortInRequestDao;
import com.telus.cmb.wsclient.wlnp.spirs.SubmitPortInRequest;
import com.telus.cmb.wsclient.wlnp.spirs.SubmitPortInRequestData;
import com.telus.cmb.wsclient.wlnp.spirs.SubmitPortInRequestDataBodyType;
import com.telus.cmb.wsclient.wlnp.spirs.SubmitPortInRequestPort;
import com.telus.cmb.wsclient.wlnp.spirs.TypesHeaderType;

public class SubmitPortInRequestDaoImpl extends WnpLegacyClient implements SubmitPortInRequestDao {

	private static final String PRM_DATE_FORMAT = "MMddyyyyhhmmss";

	@Autowired
	private SubmitPortInRequestPort port;

	@Override
	public void submitPortInRequest(final String requestId, final String applicationId) throws ApplicationException {

		invoke( new ResourceInvocationCallback() {

			@Override
			public void doInCallback() throws Exception {

				SubmitPortInRequestData requestData = new SubmitPortInRequestData();

				// set request header
				TypesHeaderType header = new TypesHeaderType();
				header.setRequestId(requestId);
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

				SubmitPortInRequestDataBodyType requestDataBody = new SubmitPortInRequestDataBodyType();
				requestData.setSubmitRequestBody(requestDataBody);

				SubmitPortInRequest request = new SubmitPortInRequest();
				request.setSubmitPortInRequestData(requestData);

				port.submitPortInRequest(request);
			}
		}, "0001", "SUBS-SVC", "SBMT-PIRS", "WNP");
	}
}
