package com.telus.cmb.account.informationhelper.dao;

import java.util.Date;
import java.util.List;

import com.telus.api.account.BillNotificationHistoryRecord;
import com.telus.eas.account.info.BillNotificationContactInfo;
import com.telus.eas.account.info.EBillRegistrationReminderInfo;
import com.telus.eas.account.info.InvoiceHistoryInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.account.info.SubscriberInvoiceDetailInfo;
import com.telus.eas.framework.info.ChargeInfo;

public interface InvoiceDao {
	/**	
	 * Returns Billing Account Info for a given BAN
	 * @param int ban
	 * @return Collection of BillNotificationContactInfo
	 */
	BillNotificationContactInfo getLastEBillNotificationSent(int ban);

	/**
	 * Returns BillNotification History Record
	 * @param int ban
	 * @param String subscriptionType
	 * @return Collection of BillNotificationHistoryRecord
	 */
	List<BillNotificationHistoryRecord> getBillNotificationHistory(int ban, String subscriptionType);

	/**
	 * Updates the BillNotification Date
	 * @param int ban
	 */
	void expireBillNotificationDetails(int ban);
	

	  /**
	   * Retrieves the subscriber invoice detail
	   *
	   * @param  int banId
	   * @param  int billSeqNo
	   * 
	   * @return SubscriberInvoiceDetailInfo
	   */
	List<SubscriberInvoiceDetailInfo> retrieveSubscriberInvoiceDetails(int banId, int billSeqNo);
	/**
	 * Returns the BillNotification Contact Info for EPOST
	 * @param int ban
	 * @param int clientID
	 * @return List	<BillNotificationContactInfo>
	 */
	List<BillNotificationContactInfo> retrieveBillNotificationContactsHasEPost(int ban,int clientID);
	/**
	 * Returns the BillNotification Contact Info for Non EPOST
	 * @param int ban
	 * @param int clientID
	 * @return List 	<BillNotificationContactInfo>
	 */
	List<BillNotificationContactInfo> retrieveBillNotificationContactsHasEPostFalse(int ban, int clientID);
	/**
	 * Returns Collection of ChargeInfo
	 * @param int ban
	 * @param int pBillSeqNo
	 * @param String pPhoneNumber
	 * @param Date from
	 * @param Date to
	 * @return Collection<ChargeInfo>
	 */
	List<ChargeInfo> retrieveBilledCharges(int ban, int pBillSeqNo, String pPhoneNumber, Date from, Date to);
	
	/**
	 * @param ban  int
	 * @return	boolean
	 */
	boolean hasEPostSubscription(int ban);
	/**
	 * Returns Invoice Properties
	 * 
	 * @param int   ban
	 * @return  InvoicePropertiesInfo
	 */
	InvoicePropertiesInfo getInvoiceProperties(int ban);
	/**
	 * Returns InvoiceHistoryInfo for a given ban and dates
	 * 
	 * @param  int ban
	 * @param Date fromDate
	 * @param Date toDate
	 * @return Collection of Invoice History
	 */
	List<InvoiceHistoryInfo> retrieveInvoiceHistory(int ban, java.util.Date fromDate, java.util.Date toDate);

}
