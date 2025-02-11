package com.telus.cmb.account.lifecyclemanager.dao;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.BillNotificationContactInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.framework.exception.TelusValidationException;

public interface InvoiceDao {
	
	void updateInvoiceProperties(int ban, InvoicePropertiesInfo invoicePropertiesInfo, String sessionId) throws ApplicationException;
	
	/**
	 * Update Bill Suppression changes the bill on-hold status of an account.
	 *
	 * <P>
	 * To suppress a paper bill, pass in 'true' in the 2nd parameter and specify
	 * the effective and expiry dates. The default dates as indicated below are
	 * used if a null values is passed in either of the dates.
	 *
	 * <P>
	 * A bill can only be suppressed for an account that is not tentative.
	 *
	 * <P>
	 * The on-hold for an account is removed by passing the BAN and 'false' in
	 * the 2nd parameter. No other parameters are required.
	 *
	 * @param int
	 *            BAN - billing account number
	 * @param boolean
	 *            true - add/update paper bill suppression false - remove paper
	 *            bill suppression
	 * @param date
	 *            effective Date (default: today's date)
	 * @param date
	 *            expiration Date (default: December 31, 2099)
	 *
	 * @exception TelusValidationException
	 *                VAL10009 BAN cannot be in tentative status.
	 *
	 */
	void updateBillSuppression(int ban, boolean suppressBill,
			Date effectiveDate, Date expiryDate, String sessionId) throws ApplicationException;
	
	void updateInvoiceSuppressionIndicator(int ban, String sessionId) throws ApplicationException;
	
	/**
	 * Update Return Envelope Indicator changes whether the customer receives a
	 * return envelope with his invoice or not.
	 *
	 * <P>
	 * The customer might request not to be sent a return envelope with each
	 * invoice. This method allows the changing of the relevent account-level
	 * indicator.
	 *
	 * @param int
	 *            BAN - billing account number
	 * @param boolean
	 *            true - return envelope will be sent false - no return envelope
	 *            will be sent
	 */
	public void updateReturnEnvelopeIndicator(int ban, boolean returnEnvelopeRequested, String sessionId) throws ApplicationException;

	/**
	 * @param Integer		ban
	 * @param long			portalUserID
	 * @param Array[]		billNotificationContact
	 * @param String		applicationCode
	 */
	void hasEPostFalseNotificationTypeNotEPost(int ban, long portalUserID,BillNotificationContactInfo[] billNotificationContact,String applicationCode);
	/**
	 * @param Integer		ban
	 * @param long			portalUserID
	 * @param Array[]		billNotificationContact
	 * @param String		applicationCode
	 */
	void hasEPostFalseNotificationTypeEPost(int ban, long portalUserID,BillNotificationContactInfo[] billNotificationContact,String applicationCode);
	/**
	 * @param Integer		ban
	 * @param long			portalUserID
	 * @param Array[]		billNotificationContact
	 * @param String		applicationCode
	 */
	void hasEPostNotificationTypeNotEPost(int ban, long portalUserID,BillNotificationContactInfo[] billNotificationContact,String applicationCode);
	/**
	 * @param Integer		ban
	 * @param long			portalUserID
	 * @param Array[]		billNotificationContact
	 * @param String		applicationCode
	 */
	void hasEPostNotificationTypeEPost(int ban, long portalUserID,BillNotificationContactInfo[] billNotificationContact,String applicationCode);

}
