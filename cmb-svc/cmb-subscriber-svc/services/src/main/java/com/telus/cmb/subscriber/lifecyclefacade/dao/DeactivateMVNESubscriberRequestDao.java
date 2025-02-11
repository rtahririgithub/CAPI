package com.telus.cmb.subscriber.lifecyclefacade.dao;

import java.util.Date;
import com.telus.api.ApplicationException;

public interface DeactivateMVNESubscriberRequestDao {
	public void  deactivateMVNESubcriber(String phoneNumber,Date logicalDate) throws ApplicationException;
}
