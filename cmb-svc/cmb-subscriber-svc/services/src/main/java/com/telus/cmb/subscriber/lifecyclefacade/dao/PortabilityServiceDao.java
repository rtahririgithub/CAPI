package com.telus.cmb.subscriber.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.portability.info.LocalServiceProviderInfo;

public interface PortabilityServiceDao {

	public LocalServiceProviderInfo getLocalServiceProvider(String phoneNumber) throws ApplicationException;

}
