package com.telus.cmb.jws.mapper;

import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.DepositHistoryInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.DepositHistory;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.InvoiceStatus;

/**
 * @Author Brandon Wen
 */

public class DepositHistoryMapper extends AbstractSchemaMapper<DepositHistory, DepositHistoryInfo>{
	private static DepositHistoryMapper instance;
	
	protected DepositHistoryMapper() {
		super(DepositHistory.class, DepositHistoryInfo.class);
	}
	
	public synchronized static DepositHistoryMapper getInstance() {
		if (instance == null) {
			instance = new DepositHistoryMapper();
		}
		return instance;
	}
	
	@Override
	protected DepositHistory performSchemaMapping(DepositHistoryInfo source, DepositHistory target) {
		target.setSubscriberId(source.getSubscriberId());
		target.setInvoiceCreationDate(source.getInvoiceCreationDate());
		target.setInvoiceDueDate(source.getInvoiceDueDate());
		target.setInvoiceStatus(InvoiceStatus.fromValue(Character.toString(source.getInvoiceStatus())));
		target.setChargesAmount(source.getChargesAmount());
		target.setDepositPaidAmount(source.getDepositPaidAmount());
		target.setDepositPaidDate(source.getDepositPaidDate());
		target.setDepositReturnDate(source.getDepositReturnDate());
		target.setDepositReturnMethodCode(String.valueOf(source.getDepositReturnMethod()).trim());
		target.setDepositTermsCode(String.valueOf(source.getDepositTermsCode()).trim());
		target.setCancellationDate(source.getCancellationDate());
		target.setCancellationReasonCode(source.getCancellationReasonCode());
		target.setPaymentExpiryCode(String.valueOf(source.getPaymentExpIndicator()).trim());
		target.setOperatorId(source.getOperatorId());
		target.setReturnedAmount(source.getReturnedAmount());
		target.setCancelledAmount(source.getCancelledAmount());
		return super.performSchemaMapping(source, target);
	}
}


