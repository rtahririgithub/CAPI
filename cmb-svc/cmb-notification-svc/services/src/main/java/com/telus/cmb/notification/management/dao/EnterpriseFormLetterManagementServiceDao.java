package com.telus.cmb.notification.management.dao;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.confirmationnotification.ConfirmationNotification;

public interface EnterpriseFormLetterManagementServiceDao {

	void submitFormLetter(String categoryCode, String templateCode,	ConfirmationNotification notificationInfo, String xmlContent) throws ApplicationException;

}
