package com.telus.cmb.jws.mapping.billing_inquiry;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.jws.mapping.customer_management_common_10.ChequeMapper;
import com.telus.cmb.jws.mapping.customer_management_common_10.CreditCardMapper;
import com.telus.eas.account.info.CreditCardHolderInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.account.info.PaymentHistoryInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.CreditCardHolder;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.CreditCardTransaction;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.PaymentHistory;

/**
 * @author Tsz Chung Tong
 */

public class BillingInquiryMapper_10 {

	private static final CreditCardMapper ccMapper = new CreditCardMapper();
	
	public static CreditCardHolderMapper CreditCardHolderMapper() {
		return CreditCardHolderMapper.getInstance();
	}
	
	public static PaymentHistoryMapper PaymentHistoryMapper() {
		return PaymentHistoryMapper.getInstance();
	}
	
	public static CreditCardTransactionMapper CreditCardTransactionMapper() {
		return CreditCardTransactionMapper.getInstance();
	}
	

	/**
	 * CreditCardHolderMapper class
	 * @author tongts
	 *
	 */
	public static class CreditCardHolderMapper extends AbstractSchemaMapper<CreditCardHolder, CreditCardHolderInfo> {
		private static CreditCardHolderMapper INSTANCE;
		
		private CreditCardHolderMapper() {
			super (CreditCardHolder.class, CreditCardHolderInfo.class);
		}
		
		public static CreditCardHolderMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditCardHolderMapper();
			}
			return INSTANCE;
		}
		
		@Override
		public CreditCardHolderInfo performDomainMapping(CreditCardHolder source, CreditCardHolderInfo target) {
			if (source == null) return null;
					
			target.setAccountSubType(source.getAccountSubType());
			target.setAccountType(source.getAccountType().value());
			target.setActivationDate(source.getActivationDate());
			target.setApartmentNumber(source.getApartmentNumber());
			target.setBirthDate(source.getBirthDate());
			target.setBusinessRole(source.getBusinessRole());
			target.setCity(source.getCity());
			target.setCivicStreetNumber(source.getCivicStreetNumber());
			target.setClientID(source.getClientId());
			target.setFirstName(source.getFirstName());
			target.setHomePhone(source.getHomePhone());
			target.setlastName(source.getLastName());
			target.setPostalCode(source.getPostalCode());
			target.setProvince(source.getProvince().value());
			target.setStreetName(source.getStreetName());		
	
			return super.performDomainMapping(source, target);
		}
	}

	/**
	 * PaymentHistoryMapper inner class
	 * @author tongts
	 *
	 */
	public static class PaymentHistoryMapper extends AbstractSchemaMapper<PaymentHistory, PaymentHistoryInfo> {
		private static PaymentHistoryMapper INSTANCE;
		private ChequeMapper chequeMapper;
		
		private PaymentHistoryMapper () {
			super (PaymentHistory.class, PaymentHistoryInfo.class);
		}
		
		public static PaymentHistoryMapper getInstance() {
			if (INSTANCE == null) {
				 INSTANCE = new PaymentHistoryMapper();
			}
			return INSTANCE;
		}
		
		private ChequeMapper getChequeMapper() {
			if (chequeMapper == null) {
				chequeMapper = new ChequeMapper();
			}
			
			return chequeMapper;
		}

		@Override
		protected PaymentHistory performSchemaMapping(
				PaymentHistoryInfo source, PaymentHistory target) {
			target.setActivityCode(source.getActivityCode());
			target.setActivityReasonCode(source.getActivityReasonCode());
			target.setActualAmount(Double.toString(source.getActualAmount()));
			target.setAmountDue(Double.toString(source.getAmountDue()));
			target.setBatchLineNumber(Long.valueOf(source.getBatchLineNumber()));
			target.setBatchNumber(Long.valueOf(source.getBatchNumber()));
			target.setBillDate(source.getBillDate());
			target.setCheque(getChequeMapper().mapToSchema(source.getCheque0()));
			target.setCreditCard(ccMapper.mapToSchema(source.getCreditCard0()));
			target.setDate(source.getDate());
			target.setDepositDate(source.getDepositDate());
			target.setFileSequenceNumber(Long.valueOf(source.getFileSequenceNumber()));
			target.setIsBalanceIgnoreFlag(source.isBalanceIgnoreFlag());
			target.setOriginalAmount(Double.toString(source.getOriginalAmount()));
			target.setOriginalBanId(Integer.toString(source.getOriginalBanId()));
			target.setPaymentMethodCode(source.getPaymentMethodCode());
			target.setPaymentMethodSubCode(source.getPaymentMethodSubCode());
			target.setSequenceNo(Long.valueOf(source.getSeqNo()));
			target.setSourceId(source.getSourceID());
			target.setSourceTypeCode(source.getSourceTypeCode());
			return super.performSchemaMapping(source, target);
		}

	}
	
	/**
	 * CreditCardTransactionMapper inner class
	 * @author tongts
	 *
	 */
	public static class CreditCardTransactionMapper extends AbstractSchemaMapper<CreditCardTransaction, CreditCardTransactionInfo> {
		private static CreditCardTransactionMapper INSTANCE;
		
		private CreditCardTransactionMapper() {
			super (CreditCardTransaction.class, CreditCardTransactionInfo.class);
		}
		
		public static CreditCardTransactionMapper getInstance() {
			if (INSTANCE == null) {
				 INSTANCE = new CreditCardTransactionMapper();
			}
			return INSTANCE;
		}
		
		@Override
		protected CreditCardTransactionInfo performDomainMapping (CreditCardTransaction source, CreditCardTransactionInfo target) {
			target.setAmount(Double.parseDouble(source.getAmount()));
			target.setBan(Integer.parseInt(source.getBanId()));
			target.setBrandId(source.getBrandId());
			target.setChargeAuthorizationNumber(source.getChargeAuthorizationNumber());
			target.setCreditCardHolderInfo(CreditCardHolderMapper().mapToDomain(source.getCreditCardHolder()));
			target.setCreditCardInfo(ccMapper.mapToDomain(source.getCreditCard()));
			target.setTransactionType(source.getTransactionType().value());
			return super.performDomainMapping(source, target);
		}
	}
}