package com.telus.provider.account;

import java.util.Date;

import com.telus.api.account.BillNotificationContact;
import com.telus.eas.account.info.BillNotificationContactInfo;

public class TMBillNotificationContact implements BillNotificationContact {

	BillNotificationContactInfo delegate;

    public TMBillNotificationContact () {
       delegate = new BillNotificationContactInfo();
    }
    
    public TMBillNotificationContact (BillNotificationContactInfo delegate) {
        this.delegate = delegate;
    }

    public TMBillNotificationContact  getBillNotificationContact() {
        return this;
    }

    public BillNotificationContactInfo getBillNotificationContact0() {
        return delegate;
    } 
    
    public String getContactType() {
        return delegate.getContactType();		
    } 
	
    public String getNotificationAddress() {
        return delegate.getNotificationAddress();		
    } 
    
    public void setContactType(String contactType) {
        delegate.setContactType(contactType);
    }
   
    public void setNotificationAddress (String billNotificationAddress) {
        delegate.setNotificationAddress (billNotificationAddress);
    }

	public String getEMailValidationStatus() {
		return delegate.getEMailValidationStatus();
	}

	public Date getLastNotificationDate() {
		return delegate.getLastNotificationDate();
	}

	public void setEMailValidationStatus(String mailValidationStatus) {
		delegate.setEMailValidationStatus(mailValidationStatus);
	}

	public void setLastNotificationDate(Date lastNotificationDate) {
		delegate.setLastNotificationDate(lastNotificationDate);
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public String getBillNotificationRegistrationId() {
		return delegate.getBillNotificationRegistrationId();
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public String getBillNotificationType() {
		return delegate.getBillNotificationType();
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public boolean isBillNotificationEnabled() {
		return delegate.isBillNotificationEnabled();
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public int getPortalUserId() {
		return delegate.getPortalUserId();
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public int getClientAccountId() {
		return delegate.getClientAccountId();
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public void setBillNotificationEnabled(boolean enabled) {
		delegate.setBillNotificationEnabled(enabled);
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public void setBillNotificationRegistrationId(
			String billNotificationRegistrationId) {
		delegate.setBillNotificationRegistrationId(billNotificationRegistrationId);
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public void setBillNotificationType(String billNotificationType) {
		delegate.setBillNotificationType(billNotificationType);
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public void setPortalUserId(int portalUserId) {
		delegate.setPortalUserId(portalUserId);
	}

	/**
	 * @deprecated - to be removed as part of the BillNotificationManagement WS
	 *               handover for EPOST - target August-November 2010
	 */
	public void setClientAccountId(int clientAccountId) {
		delegate.setClientAccountId(clientAccountId);
	}
}
