package com.telus.api.account;

import java.util.Date;

public interface BillNotificationContact {

	public static final String CONTACT_TYPE_EMAIL = "EMAIL";

	public static final String CONTACT_TYPE_SMS = "SMS";

	public static final String E_MAIL_VALIDATION_STATUS_VALID_CONFIRMED = "V";

	public static final String E_MAIL_VALIDATION_STATUS_VALID_PENDING = "P";

	public static final String E_MAIL_VALIDATION_STATUS_VALID_NEW = "N";

	public static final String E_MAIL_VALIDATION_STATUS_INVALID_CONFIRMED = "I";

	public static final String E_MAIL_VALIDATION_STATUS_INVALID_ASSUMED = "X";

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public static final String NOTIFICATION_TYPE_EPOST = "EPOST";

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public static final String NOTIFICATION_TYPE_EBILL = "EBILL";

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public static final String NOTIFICATION_TYPE_PAPER = "PAPER";

	/**
	 * Sets type of the EBill Notification (SMS/EMAIL)
	 * 
	 */

	void setContactType(String contactType);
	
	/**
	 * Setss Notification Address for EBill - phone number or e-mail to sent
	 * E-Bill Notifications
	 * 
	 */

	void setNotificationAddress(String billNotificationAddress);

	/**
	 * Returns Contact type of the EBill Notification (SMS/EMAIL)
	 * 
	 */

	String getContactType();

	/**
	 * Returns Notification Address for EBill - phone number or e-mail to sent
	 * E-Bill Notifications
	 * 
	 */

	String getNotificationAddress();

	/**
	 * Returns the last date E-Bill Notification being sent
	 * 
	 */

	Date getLastNotificationDate();

	/**
	 * Returns EMail Validation Status
	 * 
	 */

	String getEMailValidationStatus();

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	boolean isBillNotificationEnabled();

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	void setBillNotificationEnabled(boolean enabled);

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	String getBillNotificationType();

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	void setBillNotificationType(String billNotificationType);

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	String getBillNotificationRegistrationId();

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	void setBillNotificationRegistrationId (String billNotificationRegistrationId);
	
	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	int getPortalUserId();
	
	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	void setPortalUserId(int portalUserId);
	
	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	int getClientAccountId();
}
