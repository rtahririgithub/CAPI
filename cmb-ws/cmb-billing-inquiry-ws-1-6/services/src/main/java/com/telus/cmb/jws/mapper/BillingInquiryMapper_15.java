package com.telus.cmb.jws.mapper;

import java.math.BigInteger;
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

public class BillingInquiryMapper_15 {

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
			
			if (source.getAccountSubType() != null)
			{
				target.setAccountSubType(source.getAccountSubType());
			}
			
			if (source.getAccountType() != null)
			{
				target.setAccountType(source.getAccountType().value());
			}
			
			if (source.getActivationDate() != null)
			{
				target.setActivationDate(source.getActivationDate());
			}
			
			if (source.getApartmentNumber() != null)
			{
				target.setApartmentNumber(source.getApartmentNumber());
			}
			
			if (source.getBirthDate() != null)
			{
				target.setBirthDate(source.getBirthDate());
			}
			
			if (source.getBusinessRole() != null)
			{
				target.setBusinessRole(source.getBusinessRole());
			}
			
			if (source.getCity() != null)
			{
				target.setCity(source.getCity());
			}
			
			if (source.getCivicStreetNumber() != null)
			{
				target.setCivicStreetNumber(source.getCivicStreetNumber());
			}
			
			if (source.getClientId() != null)
			{
				target.setClientID(source.getClientId());
			}
			
			if (source.getFirstName() != null)
			{
				target.setFirstName(source.getFirstName());
			}
			
			if (source.getHomePhone() != null)
			{
				target.setHomePhone(source.getHomePhone());
			}
			
			if (source.getLastName() != null)
			{
				target.setlastName(source.getLastName());
			}
			
			if (source.getPostalCode() != null)
			{
				target.setPostalCode(source.getPostalCode());
			}
			
			if (source.getProvince() != null)
			{
				target.setProvince(source.getProvince().value());
			}
			
			if (source.getStreetName() != null)
			{
				target.setStreetName(source.getStreetName());
			}
	
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
			target.setSequenceNo(new Long(source.getSeqNo()));
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