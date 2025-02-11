package com.telus.cmb.subscriber.lifecyclehelper.dao;

import com.telus.eas.account.info.InvoiceTaxInfo;

public interface InvoiceDao {

	/**
	 * @param integer		ban
	 * @param String		subscriberId
	 * @param integer		billSeqNo
	 * @return
	 */
	InvoiceTaxInfo retrieveInvoiceTaxInfo(int ban, String subscriberId, int billSeqNo);
}
