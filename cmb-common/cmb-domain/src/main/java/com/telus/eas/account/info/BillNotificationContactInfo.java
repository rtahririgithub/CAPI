package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.account.BillNotificationContact;
import com.telus.eas.framework.info.Info;

public class BillNotificationContactInfo extends Info implements BillNotificationContact {

	static final long serialVersionUID = 1L;

	private String contactType;

	private String notificationAddress;

	private Date lastNotificationDate;

	private String eMailValidationStatus;

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	private String billNotificationRegistrationId;

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	private String billNotificationType;

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	private boolean billNotificationEnabled;
	
	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	private int portalUserId;

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	private int clientAccountId;

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getNotificationAddress() {
		return notificationAddress;
	}

	public void setNotificationAddress(String notificationAddress) {
		this.notificationAddress = notificationAddress;
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("BillNotificationContactInfo:{\n");
		s.append("    contactType=[").append(contactType).append("]\n");
		s.append("    notificationAddress=[").append(notificationAddress).append("]\n");
		s.append("    lastNotificationDate=[").append(lastNotificationDate).append("]\n");
		s.append("    eMailValidationStatus=[").append(eMailValidationStatus).append("]\n");
		s.append("    billNotificationRegistrationId=[").append(billNotificationRegistrationId).append("]\n");
		s.append("    billNotificationType=[").append(billNotificationType).append("]\n");
		s.append("    billNotificationEnabled=[").append(billNotificationEnabled).append("]\n");
		s.append("    portalUserId=[").append(portalUserId).append("]\n");
		s.append("    clientAccountId=[").append(clientAccountId).append("]\n");
		s.append("}");

		return s.toString();
	}

	public String getEMailValidationStatus() {
		return eMailValidationStatus;
	}

	public void setEMailValidationStatus(String mailValidationStatus) {
		eMailValidationStatus = mailValidationStatus;
	}

	public Date getLastNotificationDate() {
		return lastNotificationDate;
	}

	public void setLastNotificationDate(Date lastNotificationDate) {
		this.lastNotificationDate = lastNotificationDate;
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public String getBillNotificationRegistrationId() {
		return billNotificationRegistrationId;
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public String getBillNotificationType() {
		return billNotificationType;
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public boolean isBillNotificationEnabled() {
		return billNotificationEnabled;
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public int getPortalUserId() {
		return portalUserId;
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public int getClientAccountId() {
		return clientAccountId;
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public void setBillNotificationEnabled(boolean enabled) {
		this.billNotificationEnabled = enabled;
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public void setBillNotificationRegistrationId(
			String billNotificationRegistrationId) {
		this.billNotificationRegistrationId = billNotificationRegistrationId;
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public void setBillNotificationType(String billNotificationType) {
		this.billNotificationType = billNotificationType;
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public void setPortalUserId(int portalUserId) {
		this.portalUserId = portalUserId;
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public void setClientAccountId(int clientAccountId) {
		this.clientAccountId = clientAccountId;
	}

	public void copy (BillNotificationContact oldInfo) {
		//TODO - remove deprecated methods
		setBillNotificationEnabled(oldInfo.isBillNotificationEnabled());
		setBillNotificationRegistrationId(oldInfo.getBillNotificationRegistrationId());
		setBillNotificationType(oldInfo.getBillNotificationType());
		setClientAccountId(oldInfo.getClientAccountId());
		setContactType(oldInfo.getContactType());
		setEMailValidationStatus(oldInfo.getEMailValidationStatus());
		setLastNotificationDate(oldInfo.getLastNotificationDate());
		setNotificationAddress(oldInfo.getNotificationAddress());
		setPortalUserId(oldInfo.getPortalUserId());
	}
}
