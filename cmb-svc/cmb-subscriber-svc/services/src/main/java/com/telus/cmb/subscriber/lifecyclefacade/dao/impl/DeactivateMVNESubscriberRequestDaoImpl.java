package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.framework.resource.ResourceInvocationCallback;
import com.telus.cmb.subscriber.lifecyclefacade.dao.DeactivateMVNESubscriberRequestDao;
import com.telus.cmb.wsclient.wlnp.dss.DeactivateMVNESubscriber;
import com.telus.cmb.wsclient.wlnp.dss.DeactivateMvneSubscriberPort;
import com.telus.cmb.wsclient.wlnp.dss.DeactivateSubscriberData;
import com.telus.cmb.wsclient.wlnp.dss.DeactivateSubscriberDataBodyType;
import com.telus.cmb.wsclient.wlnp.dss.TypesHeaderType;

public class DeactivateMVNESubscriberRequestDaoImpl extends WnpLegacyClient implements DeactivateMVNESubscriberRequestDao {

	@Autowired
	private DeactivateMvneSubscriberPort port;

	@Override
	public void deactivateMVNESubcriber(final String phoneNumber, final Date logicalDate) throws ApplicationException {

		invoke(new ResourceInvocationCallback() {

			@Override
			public void doInCallback() throws Exception {

				DeactivateSubscriberData requestData = new DeactivateSubscriberData();
				DeactivateSubscriberDataBodyType requestDataBody = new DeactivateSubscriberDataBodyType();
				TypesHeaderType header = new TypesHeaderType();
				header.setDestination("TELUS");
				header.setOriginator("TELUS");
				header.setTimestamp(logicalDate.toString());
				requestDataBody.setPhoneNumber(phoneNumber);
				requestData.setDeactivateSubscriberBody(requestDataBody);
				requestData.setHeader(header);
				DeactivateMVNESubscriber deactivateMVNESubscriber = new DeactivateMVNESubscriber();
				deactivateMVNESubscriber.setDeactivateSubscriberData(requestData);

				port.deactivateMVNESubscriber(deactivateMVNESubscriber);
			}

		}, "0001", "SUBS-SVC", "DEACT-PIRS", "WNP");
	}

}
