package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.framework.resource.ResourceAccessor;
import com.telus.cmb.framework.resource.ResourceInvocationCallback;
import com.telus.cmb.subscriber.lifecyclefacade.dao.CancelPortInRequestDao;
import com.telus.cmb.wsclient.wlnp.cnclpirs.CancelPortInRequest;
import com.telus.cmb.wsclient.wlnp.cnclpirs.CancelPortInRequestData;
import com.telus.cmb.wsclient.wlnp.cnclpirs.CancelPortInRequestDataBodyType;
import com.telus.cmb.wsclient.wlnp.cnclpirs.CancelPortInRequestPort;
import com.telus.cmb.wsclient.wlnp.cnclpirs.TypesHeaderType;
import com.telus.cmb.wsclient.wlnp.cnclpirs.TypesPACInfoType;

public class CancelPortInRequestDaoImpl extends WnpLegacyClient implements CancelPortInRequestDao {

	private static final String PRM_DATE_FORMAT = "MMddyyyyhhmmss";

	@Autowired
	private CancelPortInRequestPort port;

	@Override
	public void cancelPortInRequest(final String requestId, final String reasonCode, final String applicationId) throws ApplicationException {

		invoke( new ResourceInvocationCallback() {

			@Override
			public void doInCallback() throws Exception {

				CancelPortInRequestData requestData = new CancelPortInRequestData();

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

				// set request body
				CancelPortInRequestDataBodyType requestDataBody = new CancelPortInRequestDataBodyType();
				TypesPACInfoType pacInfo = new TypesPACInfoType();
				pacInfo.setInternalReasonTypeCode(reasonCode);
				requestDataBody.setPACInfo(pacInfo);
				requestData.setCancelPortInRequestDataBody(requestDataBody);

				CancelPortInRequest request = new CancelPortInRequest();
				request.setCancelPortInRequestData(requestData);

				port.cancelPortInRequest(request);
			}
		}, "0001", "SUBS-SVC", "CNCL-PIRS", "WNP");
	}

}
