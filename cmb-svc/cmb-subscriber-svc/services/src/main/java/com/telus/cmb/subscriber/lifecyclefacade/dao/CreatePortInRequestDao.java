package com.telus.cmb.subscriber.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.api.reference.Brand;
import com.telus.api.reference.Province;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public interface CreatePortInRequestDao {

	public String createPortInRequest( SubscriberInfo subscriber,
			String portProcessType,  int incomingBrandId,  int outgoingBrandId,
			String sourceNetwork,  String targetNetwork,  String applicationId,
			String user, Province[] provinces, Brand[] brands, PortRequestInfo portReq ) throws ApplicationException ;
	
	public String ping() throws ApplicationException;
	
}
