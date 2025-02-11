package com.telus.cmb.subscriber.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.portability.info.PortRequestInfo;

public interface ActivatePortInRequestServiceDao {

	public void activatePortInRequest(PortRequestInfo portRequest, String applicationId) throws ApplicationException;

}
