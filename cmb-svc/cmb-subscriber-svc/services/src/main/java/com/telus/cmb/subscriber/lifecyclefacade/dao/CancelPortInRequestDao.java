package com.telus.cmb.subscriber.lifecyclefacade.dao;

import com.telus.api.ApplicationException;

public interface CancelPortInRequestDao {

	public void cancelPortInRequest(String requestId, String reasonCode, String applicationId) throws ApplicationException;

}
