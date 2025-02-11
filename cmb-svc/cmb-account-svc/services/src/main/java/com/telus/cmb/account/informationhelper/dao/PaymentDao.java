package com.telus.cmb.account.informationhelper.dao;
/*
 * DAO that retrieves Payment related information from KNOWbility
 */
import java.util.Date;
import java.util.List;

import com.telus.eas.account.info.PaymentActivityInfo;
import com.telus.eas.account.info.PaymentHistoryInfo;
import com.telus.eas.account.info.PaymentMethodChangeHistoryInfo;
import com.telus.eas.account.info.RefundHistoryInfo;


public interface PaymentDao {

	/**
	   * Returns Last Payment Activity for a given Billing Account Number (BAN)
	   *
	   * @param   int     billing account number (BAN)
	   *
	   * @returns  PaymentHistoryInfo	
	   *
	   */
	PaymentHistoryInfo retrieveLastPaymentActivity (int ban) ;
	/**
	 * Retrieves Payment History
	 * 
	 * @param int ban 
	 * @param Date pFromDate
	 * @param Date pToDate
	 * @return	CollectionPaymentHistoryInfo
	 */
	List<PaymentHistoryInfo> retrievePaymentHistory (int ban, java.util.Date pFromDate, java.util.Date pToDate);
	/**
	 *Returns List of Payment Activities
	 * 
	 * @param int 		banId
	 * @param int 		paymentSeqNo
	 * @return
	 */
	List<PaymentActivityInfo> retrievePaymentActivities(int banId, int paymentSeqNo);
	/**
	 * Returns Payment Method Change HistoryInfo
	 * 
	 * @param int   			 ban
	 * @param java.Util.Date 	fromDate
	 * @param java.util.Date 	toDate
	 * @return
	 */
	List<PaymentMethodChangeHistoryInfo> retrievePaymentMethodChangeHistory (int ban, java.util.Date fromDate, java.util.Date toDate);

	/**
	   * Returns refund history
	   *
	   * @param ban
	   * @param fromDate
	   * @param toDate
	   * @return RefundHistoryInfo
	   */
	
	List<RefundHistoryInfo> retrieveRefundHistory (int ban, Date fromDate,Date toDate);

}
