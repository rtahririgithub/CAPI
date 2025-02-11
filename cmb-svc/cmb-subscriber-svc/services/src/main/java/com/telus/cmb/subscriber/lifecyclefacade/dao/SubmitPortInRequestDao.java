package com.telus.cmb.subscriber.lifecyclefacade.dao;

import com.telus.api.ApplicationException;

public interface SubmitPortInRequestDao {

	public void submitPortInRequest(String requestId, String applicationId) throws ApplicationException;

}
