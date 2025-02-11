package com.telus.cmb.notification.management.mapping;

import com.telus.cmb.common.confirmationnotification.ConfirmationNotification;

public interface AccountTransactionEmailTemplateMapper {
	public Object mapToSchema (ConfirmationNotification notificationInfo);
}
