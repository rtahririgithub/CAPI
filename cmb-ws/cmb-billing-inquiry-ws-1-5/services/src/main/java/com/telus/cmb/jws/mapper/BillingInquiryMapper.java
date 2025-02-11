package com.telus.cmb.jws.mapper;

import java.math.BigInteger;
import java.util.List;

import com.telus.api.account.Charge;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.jws.mapping.customer_management_common_10.ChequeMapper;
import com.telus.cmb.jws.mapping.customer_management_common_10.CreditCardMapper;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.CreditBalanceTransferInfo;
import com.telus.eas.account.info.CreditCardHolderInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.account.info.InvoiceHistoryInfo;
import com.telus.eas.account.info.PaymentHistoryInfo;
import com.telus.eas.framework.info.ChargeAdjustmentCodeInfo;
import com.telus.eas.framework.info.ChargeAdjustmentInfo;
import com.telus.eas.framework.info.ChargeIdentifierInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.utility.info.DiscountPlanInfo;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.cmb.jws.ChargesAndAdjustmentsId;
import com.telus.cmb.jws.ChargesAndAdjustmentsIdAndAmount;
import com.telus.cmb.jws.RelatedCreditsForCharge;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.CreditCardHolder;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.CreditCardTransaction;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.DiscountType;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.Invoice;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.PaymentHistory;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.ChargeAndAdjustment;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.ChargeAndAdjustmentCode;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.ChargeAndAdjustmentForSubscriber;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.ChargeIdentifier;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.CreditTransfer;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.DiscountPlanType;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.GetAllCharges;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.GetAllCredits;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.TaxExemption;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.DiscountPlanType.BrandIdList;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.DiscountPlanType.DiscountGroupList;

/**
 * @author Brandon Wen
 *
 */
public class BillingInquiryMapper {

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

	public static InvoiceHistoryMapper InvoiceHistoryMapper() {
		return InvoiceHistoryMapper.getInstance();
	}

	public static CreditBalanceTransferMapper CreditBalanceTransferMapper() {
		return CreditBalanceTransferMapper.getInstance();
	}

	public static ChargesAndAdjustmentMapper ChargesAndAdjustmentMapper() {
		return ChargesAndAdjustmentMapper.getInstance();
	}

	public static ChargesAndAdjustmentIdMapper ChargesAndAdjustmentIdMapper() {
		return ChargesAndAdjustmentIdMapper.getInstance();
	}
	public static ChargesAndAdjustmentsIdAndAmountMapper ChargesAndAdjustmentsIdAndAmountMapper() {
		return ChargesAndAdjustmentsIdAndAmountMapper.getInstance();
	}

	
	public static ChargesAndAdjustmentToSubscriberMapper ChargesAndAdjustmentToSubscriberMapper() {
		return ChargesAndAdjustmentToSubscriberMapper.getInstance();
	}
	public static ChargeAndAdjustmentCodeMapper ChargeAndAdjustmentCodeMapper() {
		return ChargeAndAdjustmentCodeMapper.getInstance();
	}
	
	public static GetAllChargesMapper GetAllChargesMapper() {
		return GetAllChargesMapper.getInstance();
	}

	public static RelatedCreditsForChargeMapper RelatedCreditsForChargeMapper(){
		return RelatedCreditsForChargeMapper.getInstance();

	}

	public static ChargeIdentifierMapper ChargeIdentifierMapper() {
		return ChargeIdentifierMapper.getInstance();
	}

	public static GetAllCreditsMapper GetAllCreditsMapper() {
		return GetAllCreditsMapper.getInstance();
	}
	
	public static DiscountTypeMapper DiscountTypeMapper() {
		return DiscountTypeMapper.getInstance();
	}
	
	public static DiscountPlanTypeMapper DiscountPlanTypeMapper() {
		return DiscountPlanTypeMapper.getInstance();
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

	public static class InvoiceHistoryMapper extends AbstractSchemaMapper<Invoice, InvoiceHistoryInfo> {

		private static InvoiceHistoryMapper INSTANCE;

		private InvoiceHistoryMapper() {
			super(Invoice.class, InvoiceHistoryInfo.class);
		}

		protected static synchronized InvoiceHistoryMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new InvoiceHistoryMapper();
			}
			return INSTANCE;
		}

		@Override 
		protected Invoice performSchemaMapping(InvoiceHistoryInfo source, Invoice target) {
			target.setDate(source.getDate());
			target.setMailedInd(source.getMailedIndicator());
			target.setPreviousBalance(source.getPreviousBalance());
			target.setInvoiceAmount(source.getInvoiceAmount());
			target.setAmountDue(source.getAmountDue());
			target.setBillSequenceNo(source.getBillSeqNo());
			target.setBillCycleRunYear(source.getCycleRunYear());
			target.setBillCycleRunMonth(source.getCycleRunMonth());
			target.setBillCycleCode(source.getCycleCode());
			target.setStatus(source.getStatus());

			target.setPaymentReceivedAmount(source.getPaymentReceivedAmount());
			target.setAdjustmentAmount(source.getAdjustmentAmount());
			target.setPastDueAmount(source.getPastDueAmount());
			target.setLatePaymentChargeAmount(source.getLatePaymentCharge());
			target.setCurrentChargeAmount(source.getCurrentCharges());
			target.setTotalTax(source.getTotalTax());
			target.setHomeCallCount(source.getHomeCallCount());
			target.setRoamingCallCount(source.getRoamingCallCount());
			target.setHomeCallMinutes(source.getHomeCallMinutes());
			target.setRoamingCallMinutes(source.getRoamingCallMinutes());

			target.setMonthlyRecurringChargeAmount(source.getMonthlyRecurringCharge());
			target.setLocalCallingChargeAmount(source.getLocalCallingCharges());
			target.setOtherChargeAmount(source.getOtherCharges());
			target.setZoneUsageChargeAmount(source.getZoneUsageCharges());
			target.setExtendedHomeAreaUsageChargeAmount(source.getEHAUsageCharges());
			return target;
		}

		@Override 
		protected InvoiceHistoryInfo performDomainMapping(Invoice source, InvoiceHistoryInfo target) {
			target.setDate(source.getDate());
			target.setMailedIndicator(source.isMailedInd());
			target.setPreviousBalance(source.getPreviousBalance());
			target.setInvoiceAmount(source.getInvoiceAmount());
			target.setAmountDue(source.getAmountDue());
			target.setBillSeqNo(source.getBillSequenceNo());
			target.setCycleRunYear(source.getBillCycleRunYear());
			target.setCycleRunMonth(source.getBillCycleRunMonth());
			target.setCycleCode(source.getBillCycleCode());
			target.setStatus(source.getStatus());

			target.setPaymentReceivedAmount(source.getPaymentReceivedAmount());
			target.setAdjustmentAmount(source.getAdjustmentAmount());
			target.setPastDueAmount(source.getPastDueAmount());
			target.setLatePaymentCharge(source.getLatePaymentChargeAmount());
			target.setCurrentCharges(source.getCurrentChargeAmount());
			target.setTotalTax(source.getTotalTax());
			target.setHomeCallCount(source.getHomeCallCount());
			target.setRoamingCallCount(source.getRoamingCallCount());
			target.setHomeCallMinutes(source.getHomeCallMinutes());
			target.setRoamingCallMinutes(source.getRoamingCallMinutes());

			target.setMonthlyRecurringCharge(source.getMonthlyRecurringChargeAmount());
			target.setLocalCallingCharges(source.getLocalCallingChargeAmount());
			target.setOtherCharges(source.getOtherChargeAmount());
			target.setZoneUsageCharges(source.getZoneUsageChargeAmount());
			target.setEHAUsageCharges(source.getExtendedHomeAreaUsageChargeAmount());
			return target;
		}
	}

	public static class CreditBalanceTransferMapper extends AbstractSchemaMapper<CreditTransfer,CreditBalanceTransferInfo> {

		private static CreditBalanceTransferMapper INSTANCE;
		private CreditBalanceTransferMapper() {
			super(CreditTransfer.class, CreditBalanceTransferInfo.class);
		}

		protected static synchronized CreditBalanceTransferMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditBalanceTransferMapper();
			}
			return INSTANCE;
		}

		@Override 
		protected CreditTransfer performSchemaMapping(CreditBalanceTransferInfo source, CreditTransfer target) {	
			target.setAdjustmentSequenceNumber(source.getAdjSeqNo());
			target.setBillCycleCode(String.valueOf(source.getBillCycle()));
			target.setChargeSequenceNumber(source.getChargeSeqNo());
			target.setRequestCreationDate(source.getReqCreateDt());
			target.setSourceAccountNumber(String.valueOf(source.getSourceBan()));
			target.setStatusUpdateDate(source.getStatusUpdateDt());
			target.setTransferAmount(source.getTransferAmt());
			target.setTargetAccountNumber(String.valueOf(source.getTargetBan()));
			target.setCreditTransferSequenceNumber(String.valueOf(source.getCreditBalanceTransferSeqNum()));  
			target.setTransferStatus(String.valueOf(source.getTransferStatus()));	
			target.setCreditTransferSystemCreationDate(source.getSystemCreationDate());
			target.setCreditTransferSystemUpdateDate(source.getSystemUpdateDate());
			target.setCreditTransferSystemOperatorId(String.valueOf(source.getOperatorId()));
			target.setCreditTransferSystemApplicationId(source.getApplicationId());
			target.setCreditTransferUpStamp(source.getUpdateStamp());
			target.setFailureCode(source.getFailureCd());
			return super.performSchemaMapping(source, target);
		}
	}

	public static class ChargesAndAdjustmentMapper extends AbstractSchemaMapper<ChargeAndAdjustment, ChargeAdjustmentInfo> {

		private static ChargesAndAdjustmentMapper INSTANCE;

		private ChargesAndAdjustmentMapper() {
			super(ChargeAndAdjustment.class, ChargeAdjustmentInfo.class);
		}

		protected static synchronized ChargesAndAdjustmentMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ChargesAndAdjustmentMapper();
			}
			return INSTANCE;
		}

		@Override 
		protected ChargeAdjustmentInfo performDomainMapping(ChargeAndAdjustment source, ChargeAdjustmentInfo target) {

			target.setBan(Integer.valueOf(source.getAccountNumber()));
			target.setChargeCode(source.getChargeCode());
			target.setChargeAmount(source.getChargeAmount());
			target.setChargeEffectiveDate(source.getChargeEffectiveDate());
			target.setChargeMemoText(source.getMemoText());
			double adjustmentAmount = source.getAdjustmentAmount()== null ? 0.0 : source.getAdjustmentAmount();
			target.setAdjustmentAmount(adjustmentAmount);
			target.setAdjustmentReasonCode(source.getAdjustmentReasonCode());
			target.setAdjustmentMemoText(source.getAdjustmentMemoText());
			boolean isBypassAuthorizationIndicator = source.isBypassAuthorizationInd() == null? false: source.isBypassAuthorizationInd();
			target.setBypassAuthorization(isBypassAuthorizationIndicator);
			boolean isAuthorizedToCreateFollowupIndicator = source.isAuthorizedToCreateFollowupInd() == null? false: source.isAuthorizedToCreateFollowupInd();
			target.setAuthorizedToCreateFollowUp(isAuthorizedToCreateFollowupIndicator);

			return super.performDomainMapping(source, target);
		}
	}

	public static class ChargesAndAdjustmentIdMapper extends AbstractSchemaMapper<ChargesAndAdjustmentsId, ChargeAdjustmentInfo> {

		private static ChargesAndAdjustmentIdMapper INSTANCE;

		private ChargesAndAdjustmentIdMapper() {
			super(ChargesAndAdjustmentsId.class, ChargeAdjustmentInfo.class);
		}

		protected static synchronized ChargesAndAdjustmentIdMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ChargesAndAdjustmentIdMapper();
			}
			return INSTANCE;
		}

		@Override 
		protected ChargesAndAdjustmentsId performSchemaMapping(ChargeAdjustmentInfo source, ChargesAndAdjustmentsId target) {
			if(source.isChargeApplied()){
				target.setAdjustmentId(source.getAdjustmentId());
				target.setChargeCode(source.getChargeCode());
				target.setChargeSequenceNumber(source.getChargeSequenceNumber());
			}else{
				target.setErrorCode(source.getErrorCode());
				target.setMessageType(source.getErrorMessage());
				target.setChargeCode(source.getChargeCode());
				
			}
			return super.performSchemaMapping(source, target);
		}
	}
	
	public static class ChargesAndAdjustmentsIdAndAmountMapper extends AbstractSchemaMapper<ChargesAndAdjustmentsIdAndAmount, ChargeAdjustmentInfo> {

		private static ChargesAndAdjustmentsIdAndAmountMapper INSTANCE;

		private ChargesAndAdjustmentsIdAndAmountMapper() {
			super(ChargesAndAdjustmentsIdAndAmount.class, ChargeAdjustmentInfo.class);
		}

		protected static synchronized ChargesAndAdjustmentsIdAndAmountMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ChargesAndAdjustmentsIdAndAmountMapper();
			}
			return INSTANCE;
		}

		@Override 
		protected ChargesAndAdjustmentsIdAndAmount performSchemaMapping(ChargeAdjustmentInfo source, ChargesAndAdjustmentsIdAndAmount target) {
			target.setChargeCode(source.getChargeCode());
			target.setAdjustmentReasonCode(source.getAdjustmentReasonCode());
			if(source.getErrorCode() != null){
				target.setErrorCode(source.getErrorCode());
				target.setMessageType(source.getErrorMessage());
			}else{
				target.setAdjustmentId(source.getAdjustmentId());
				target.setChargeSequenceNumber(source.getChargeSequenceNumber());
				target.setAdjustmentAmount(source.getAdjustmentAmount());
			}
			return super.performSchemaMapping(source, target);
		}
	}
	
	
	public static class ChargeAndAdjustmentCodeMapper extends AbstractSchemaMapper<ChargeAndAdjustmentCode, ChargeAdjustmentCodeInfo> {

		private static ChargeAndAdjustmentCodeMapper INSTANCE;

		private ChargeAndAdjustmentCodeMapper() {
			super(ChargeAndAdjustmentCode.class, ChargeAdjustmentCodeInfo.class);
		}

		protected static synchronized ChargeAndAdjustmentCodeMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ChargeAndAdjustmentCodeMapper();
			}
			return INSTANCE;
		}

		@Override 
		protected ChargeAdjustmentCodeInfo performDomainMapping(ChargeAndAdjustmentCode source, ChargeAdjustmentCodeInfo target) {
			target.setChargeCode(source.getChargeCode());
			target.setAdjustmentReasonCode(source.getAdjustmentReasonCode());
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class ChargesAndAdjustmentToSubscriberMapper extends AbstractSchemaMapper<ChargeAndAdjustmentForSubscriber, ChargeAdjustmentInfo> {

		private static ChargesAndAdjustmentToSubscriberMapper INSTANCE;

		private ChargesAndAdjustmentToSubscriberMapper() {
			super(ChargeAndAdjustmentForSubscriber.class, ChargeAdjustmentInfo.class);
		}

		protected static synchronized ChargesAndAdjustmentToSubscriberMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ChargesAndAdjustmentToSubscriberMapper();
			}
			return INSTANCE;
		}

		@Override 
		protected ChargeAdjustmentInfo performDomainMapping(ChargeAndAdjustmentForSubscriber source, ChargeAdjustmentInfo target) {
			target.setBan(Integer.valueOf(source.getAccountNumber()));
			target.setChargeCode(source.getChargeCode());
			target.setChargeAmount(source.getChargeAmount());
			target.setChargeEffectiveDate(source.getChargeEffectiveDate());
			target.setProductType(source.getProductType());
			target.setSubscriberId(source.getSubscriberNumber());
			target.setChargeMemoText(source.getMemoText());
			double adjustmentAmount = source.getAdjustmentAmount()== null ? 0.0: source.getAdjustmentAmount();
			target.setAdjustmentAmount(adjustmentAmount);
			target.setAdjustmentReasonCode(source.getAdjustmentReasonCode());
			target.setAdjustmentMemoText(source.getAdjustmentMemoText());
			boolean isBypassAuthorizationIndicator = source.isBypassAuthorizationInd() == null? false: source.isBypassAuthorizationInd();
			target.setBypassAuthorization(isBypassAuthorizationIndicator);
			boolean isAuthorizedToCreateFollowupIndicator = source.isAuthorizedToCreateFollowupInd() == null? false: source.isAuthorizedToCreateFollowupInd();
			target.setAuthorizedToCreateFollowUp(isAuthorizedToCreateFollowupIndicator);

			return super.performDomainMapping(source, target);
		}
	}

	public static class GetAllChargesMapper extends AbstractSchemaMapper<GetAllCharges, Charge> {

		private static GetAllChargesMapper INSTANCE;
		private GetAllChargesMapper() {
			super(GetAllCharges.class, Charge.class);
		}

		protected static synchronized GetAllChargesMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new GetAllChargesMapper();
			}
			return INSTANCE;
		}

		@Override 
		protected GetAllCharges performSchemaMapping(Charge source, GetAllCharges target) {
			target.setAccountNumber(String.valueOf(source.getBan()));
			target.setAmount(source.getAmount());
			target.setApprovalStatus(String.valueOf(source.getApprovalStatus()));
			target.setBalanceImpactCode(String.valueOf(source.isBalanceImpactFlag()));
			target.setBillSequenceNumber(new Long(source.getBillSequenceNo()));
			target.setChargeCode(source.getChargeCode());
			target.setChargeCreationDate(source.getCreationDate());
			target.setChargeSequenceNumber(source.getId());
			target.setEffectiveDate(source.getEffectiveDate());
			target.setFeatureCode(source.getFeatureCode());
			target.setFeatureRevenueCode(source.getFeatureRevenueCode());
			target.setGstAmount(source.getGSTAmount());
			target.setHstAmount(source.getHSTAmount());
			target.setPstAmount(source.getPSTAmount());
			target.setBalanceIgnoreFlagInd(source.isBalanceIgnoreFlag());
			target.setBilledInd(source.isBilled());
			target.setOperatorId(String.valueOf(source.getOperatorId()));
			target.setPeriodCoverageEndDate(source.getPeriodCoverageEndDate());
			target.setPeriodCoverageStartDate(source.getPeriodCoverageStartDate());
			target.setProductType(source.getProductType());
			target.setReasonCode(source.getReasonCode());
			target.setRoamingTaxAmount(source.getRoamingTaxAmount());
			target.setServiceCode(source.getServiceCode());
			target.setSubscriberNumber(source.getSubscriberId());
			TaxExemption taxExemption = new TaxExemption();
			taxExemption.setGstExemptInd(source.isGSTExempt()); 
			taxExemption.setHstExemptInd(source.isHSTExempt());	
			taxExemption.setPstExemptInd(source.isPSTExempt());
			target.setTaxExemption(taxExemption);	
			return super.performSchemaMapping(source, target);
		}
	}

	public static class RelatedCreditsForChargeMapper extends AbstractSchemaMapper<RelatedCreditsForCharge, CreditInfo> {

		private static RelatedCreditsForChargeMapper INSTANCE;
		private RelatedCreditsForChargeMapper() {
			super(RelatedCreditsForCharge.class, CreditInfo.class);
		}

		protected static synchronized RelatedCreditsForChargeMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new RelatedCreditsForChargeMapper();
			}
			return INSTANCE;
		}

		@Override 
		protected RelatedCreditsForCharge performSchemaMapping(CreditInfo source, RelatedCreditsForCharge target) {
			target.setAccountNumber(String.valueOf(source.getBan()));
			target.setAdjustmentId(new Double(source.getId())); 
			target.setChargeCreationDate(source.getCreationDate());
			target.setEffectiveDate(source.getEffectiveDate());
			target.setActivityCode(source.getActivityCode());
			target.setReasonCode(source.getReasonCode());
			target.setBalanceImpactCode(String.valueOf(source.isBalanceImpactFlag()));
			target.setSubscriberNumber(source.getSubscriberId());
			if (source.getProductType()!= null && source.getProductType().charAt(0) == 0) {
				target.setProductType(null);
			} else {
				target.setProductType(source.getProductType());
			}
			target.setServiceCode(source.getSOC()); 
			target.setOperatorId(String.valueOf(source.getOperatorId()));
			target.setAmount(source.getAmount());
			target.setGstAmount(source.getGSTAmount());
			target.setPstAmount(source.getPSTAmount());
			target.setHstAmount(source.getHSTAmount());
			target.setBalanceIgnoreFlagInd(source.isBalanceIgnoreFlag());
			target.setChargeSequenceNumber(source.getRelatedChargeId());
			target.setBilledInd(source.isBilled());
			target.setApprovalStatus(String.valueOf(source.getApprovalStatus()));
			return super.performSchemaMapping(source, target);
		}
	}
	public static class ChargeIdentifierMapper extends AbstractSchemaMapper<ChargeIdentifier, ChargeIdentifierInfo> {

		private static ChargeIdentifierMapper INSTANCE;

		private ChargeIdentifierMapper() {
			super(ChargeIdentifier.class, ChargeIdentifierInfo.class);
		}

		protected static synchronized ChargeIdentifierMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ChargeIdentifierMapper();
			}
			return INSTANCE;
		}

		@Override 
		protected ChargeIdentifierInfo performDomainMapping(ChargeIdentifier source, ChargeIdentifierInfo target) {
			target.setAccountNumber(Integer.valueOf(source.getAccountNumber()));
			target.setChargeSequenceNumber(source.getChargeSequenceNumber());

			return super.performDomainMapping(source, target);
		}
	}

	public static class GetAllCreditsMapper extends AbstractSchemaMapper<GetAllCredits, CreditInfo> {

		private static GetAllCreditsMapper INSTANCE;

		private GetAllCreditsMapper() {
			super(GetAllCredits.class, CreditInfo.class);
		}

		protected static synchronized GetAllCreditsMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new GetAllCreditsMapper();
			}
			return INSTANCE;
		}

		@Override
		protected GetAllCredits performSchemaMapping(CreditInfo source,GetAllCredits target) {
			target.setAccountNumber(String.valueOf(source.getBan()));
			target.setActivityCode(source.getActivityCode());
			target.setAdjustmentId(new Double(source.getId()));
			target.setAmount(source.getAmount());
			target.setApprovalStatus(String.valueOf(source.getApprovalStatus()));
			target.setBalanceImpactCode(String.valueOf(source.isBalanceImpactFlag()));
			target.setBalanceIgnoreFlagInd(source.isBalanceIgnoreFlag());
			target.setBilledInd(source.isBilled());
			target.setChargeSequenceNumber(source.getRelatedChargeId()); 
			target.setCreditCreationDate(source.getCreationDate());
			target.setEffectiveDate(source.getEffectiveDate());
			target.setFeatureCode(source.getFeatureCode());
			target.setAmount(source.getAmount());
			target.setGstAmount(source.getGSTAmount());
			target.setPstAmount(source.getPSTAmount());
			target.setHstAmount(source.getHSTAmount());
			target.setOperatorId(String.valueOf(source.getOperatorId()));
			target.setProductType(source.getProductType());
			target.setReasonCode(source.getReasonCode());
			target.setRoamingTaxAmount(source.getRoamingTaxAmount());
			target.setServiceCode(source.getSOC());
			target.setSubscriberNumber(source.getSubscriberId());

			return super.performSchemaMapping(source, target);

		}
	}

	public static class DiscountTypeMapper extends AbstractSchemaMapper<DiscountType, DiscountInfo> {
		
		private static DiscountTypeMapper INSTANCE;
		
		private DiscountTypeMapper() {
			super(DiscountType.class, DiscountInfo.class);
		}
		
		protected static synchronized DiscountTypeMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new DiscountTypeMapper();
			}
			return INSTANCE;
		}
		
		protected DiscountInfo performDomainMapping(DiscountType source,DiscountInfo target) {
			target.setDiscountByUserId(source.getDiscountByUserId());
			target.setDiscountCode(source.getDiscountCode());
			int discountSequenceNo = 0;
			if (source.getDiscountSequenceNumber() != null) {
				discountSequenceNo = source.getDiscountSequenceNumber();
			}
			target.setDiscountSequenceNo(discountSequenceNo);
			target.setEffectiveDate(source.getEffectiveDate());
			target.setExpiryDate(source.getExpiryDate());
			
			return super.performDomainMapping(source, target);	
		}
		
		protected DiscountType performSchemaMapping(DiscountInfo source,DiscountType target) {
			target.setDiscountByUserId(source.getDiscountByUserId());
			target.setDiscountCode(source.getDiscountCode());
			target.setDiscountSequenceNumber(source.getDiscountSequenceNo());
			target.setEffectiveDate(source.getEffectiveDate());
			target.setExpiryDate(source.getExpiryDate());
			
			return super.performSchemaMapping(source, target);
			
		}
	}
	
	public static class DiscountPlanTypeMapper extends AbstractSchemaMapper<DiscountPlanType, DiscountPlanInfo> {

		private static DiscountPlanTypeMapper INSTANCE;
		
		public DiscountPlanTypeMapper() {
			super(DiscountPlanType.class, DiscountPlanInfo.class);
		}
		
		protected static synchronized DiscountPlanTypeMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new DiscountPlanTypeMapper();
			}
			return INSTANCE;
		}
		
protected DiscountPlanType performSchemaMapping(DiscountPlanInfo source,DiscountPlanType target) {
			
			target.setAmount(source.getAmount());
		
			BrandIdList brandIdList = new BrandIdList();
			for(int brandId:source.getDiscountBrandIDs()){
				brandIdList.getBrandId().add(brandId);
			}
			
			target.setBrandIdList(brandIdList);
			target.setCode(source.getCode());
			target.setDescription(source.getDescription());
			target.setDescriptionFrench(source.getDescriptionFrench());
			
			DiscountGroupList discountGroupList = new DiscountGroupList();
			for(String discountGroup:source.getGroupCodes()){
				discountGroupList.getDiscountGroup().add(discountGroup);
			}
			
			target.setDiscountGroupList(discountGroupList);
			target.setEffectiveDate(source.getEffectiveDate());
			target.setExpirationDate(source.getExpiration());
			target.setLevel(source.getLevel());
			target.setMonths(source.getMonths());
			target.setOfferExpirationDate(source.getOfferExpirationDate());
			target.setPercent(source.getPercent());
			target.setPricePlanInd(source.isPricePlanDiscount());
			target.setProductType(source.getProductType());
			
			return super.performSchemaMapping(source, target);
			
		}
	}

}
