package com.telus.cmb.subscriber.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.portability.info.PortInEligibilityInfo;

public interface EligibilityCheckRequestDao {

	public PortInEligibilityInfo checkPortInEligibility( String phoneNumber,  String portVisibility,  int incomingBrand) throws ApplicationException ;
}
