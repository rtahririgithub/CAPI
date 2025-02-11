package com.telus.cmb.account.mapping;

import org.apache.log4j.Logger;

import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.BasePrepaidAccountInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.EnterpriseAddressInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.customercommon_v3.Address;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.customercommon_v3.CustomerMarketSegment;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.customercommon_v3.Name;
import com.telus.tmi.xmlschema.xsd.customer.customer.customersubdomain_v3.Customer;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.customer_billing_sub_domain_v2.BillingAccount;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.customer_billing_sub_domain_v2.BillingAccountExtension;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.customer_billing_sub_domain_v2.PayChannel;

public class BillingAccountDataMgmtMapper {
	private static final Logger LOGGER = Logger.getLogger(BillingAccountDataMgmtMapper.class);
	private static ReferenceDataFacade refFacade = null;
	
	/**
	 * Requires synchronization, but it does not really matter.
	 * @return
	 */
	private static ReferenceDataFacade getReferenceDataFacade() {
		if (refFacade == null) {
			refFacade = EJBUtil.getHelperProxy(ReferenceDataFacade.class, EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_FACADE);
		}
		
		return refFacade;
	}
	public static class NameMapper extends AbstractSchemaMapper<Name, ConsumerNameInfo> {

		public NameMapper() {
			super(Name.class, ConsumerNameInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.common.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected Name performSchemaMapping(ConsumerNameInfo source, Name target) {
			try {
				target.setFullName(source.getFullName());
				target.setSalutationCode(getReferenceDataFacade().getSaluationCodeByKBSaluationCode(source.getTitle()));
				target.setFirstName(source.getFirstName());
				target.setMiddleName(source.getMiddleInitial());
				target.setLastName(source.getLastName());
				target.setNameSuffixCode(getReferenceDataFacade().getNameSuffixByKBNameSuffix(source.getGeneration()));
			} catch (TelusException e) {
				throw new SystemException (SystemCodes.CMB_ALF_EJB, e.getMessage(), "", e);
			}
			
			return super.performSchemaMapping(source, target);
		}
	
	}
	
	public static class MarketSegmentMapper extends AbstractSchemaMapper<CustomerMarketSegment, AccountInfo> {
		public MarketSegmentMapper() {
			super (CustomerMarketSegment.class, AccountInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.common.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected CustomerMarketSegment performSchemaMapping(AccountInfo source, CustomerMarketSegment target) {
			target.setMarketSegmentCode(source.getBanSegment());
			target.setMarketSegmentTypeCode(source.getBanSubSegment());
			return super.performSchemaMapping(source, target);
		}
		
	}
	
	public static class PayChannelMapper extends AbstractSchemaMapper<PayChannel, AccountInfo> {
	
		public PayChannelMapper() {
			super (PayChannel.class, AccountInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.common.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected PayChannel performSchemaMapping(AccountInfo source, PayChannel target) {
			String banIdString = String.valueOf(source.getBanId());
			
			target.setBillingAccountNumber(banIdString);
			target.setPayChannelNumber(banIdString);
			
			target.setBillingMasterSourceId(130L);
			target.setPayChannelOpenDate(source.getStartServiceDate());
			try {
				target.setPayChannelStatusCode(getReferenceDataFacade().getBillingAccountStatusByKBAccountStatus (String.valueOf(source.getStatus())));
			}catch (TelusException e) {
				throw new SystemException (SystemCodes.CMB_ALF_EJB, e.getMessage(), "", e);
			}
			target.setPayChannelStatusDate(source.getCreateDate());
			if (source.isPrepaidConsumer()) {
				target.setPayChannelCategoryCode("PRE");
			}else if (source.isPostpaid()){
				target.setPayChannelCategoryCode("POST");
			}
			
			
			if (source instanceof PostpaidBusinessRegularAccountInfo) {
				PaymentMethodInfo paymentMethod = ((PostpaidBusinessRegularAccountInfo) source).getPaymentMethod0();
				mapSubdomainToSchema (paymentMethod, target);
				if (paymentMethod.getCreditCard0() != null) {
					mapSubdomainToSchema (paymentMethod.getCreditCard0(), target);
				}
			}else if (source instanceof PostpaidConsumerAccountInfo) {
				PaymentMethodInfo paymentMethod = ((PostpaidConsumerAccountInfo) source).getPaymentMethod0();
				mapSubdomainToSchema (paymentMethod, target);
				if (paymentMethod.getCreditCard0() != null) {
					mapSubdomainToSchema (paymentMethod.getCreditCard0(), target);
				}
			}else if (source instanceof PrepaidConsumerAccountInfo) {
				CreditCardInfo creditCardInfo = ((PrepaidConsumerAccountInfo) source).getTopUpCreditCard0();

				if (creditCardInfo != null) {
					mapSubdomainToSchema (creditCardInfo, target);
				}
			}
			return super.performSchemaMapping(source, target);
		}
		
		private PayChannel mapSubdomainToSchema(PaymentMethodInfo source, PayChannel target) {
			if (source != null) {
				if (source.getPaymentMethod() != null) {
					try {
						target.setPaymentMethodCode(getReferenceDataFacade().getPaymentMethodTypeByKBPaymentMethodType(source.getPaymentMethod()));
					} catch (TelusException e) {
						throw new SystemException(SystemCodes.CMB_ALF_EJB, e.getMessage(), "", e);
					}
				}
				if (source.getCheque0() != null && source.getCheque0().getBankAccount0() != null) {
					target.setBankBranchNum(source.getCheque0().getBankAccount0().getBankBranchNumber());
					target.setBankTransitNum(source.getCheque0().getBankAccount0().getBankCode());
				}
			}
			
			return target;
		}
		
		private PayChannel mapSubdomainToSchema(CreditCardInfo source, PayChannel target) {
			if (source != null) {
				if (source.getType() != null) {
					try {
						target.setCreditCardTypeCd(getReferenceDataFacade().getCreditCardTypeByKBCreditCardType(source.getType()));
					} catch (TelusException e) {
						throw new SystemException(SystemCodes.CMB_ALF_EJB, e.getMessage(), "", e);
					}
				}
				target.setCreditCardLastNDigitNumber(source.getTrailingDisplayDigits());
				target.setCreditCardExipryDt(source.getExpiryDate());
			}
			return target;
		}
		
	}
	
	public static class BillingAccountExtensionMapper extends AbstractSchemaMapper<BillingAccountExtension, AccountInfo> {
		public BillingAccountExtensionMapper() {
			super (BillingAccountExtension.class, AccountInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.common.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected BillingAccountExtension performSchemaMapping(AccountInfo source, BillingAccountExtension target) {
			target.setSourceAccountTypeCode(String.valueOf(source.getAccountType()));
			target.setSourceAccountSubTypeCode(String.valueOf(source.getAccountSubType()));
			return super.performSchemaMapping(source, target);
		}
	}
	
	public static class BillingAccountMapper extends AbstractSchemaMapper<BillingAccount, AccountInfo> {
		public BillingAccountMapper() {
			super (BillingAccount.class, AccountInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.common.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected BillingAccount performSchemaMapping(AccountInfo source, BillingAccount target) {
			if (source.getAddress0() != null) {
				EnterpriseAddressInfo enterpriseAddressInfo = new EnterpriseAddressInfo(source.getAddress0());
				updateAddressXref (enterpriseAddressInfo);
				Address address = EnterpriseAddressValidationServiceMapper.EnterpriseAddressMapper().mapToSchema(enterpriseAddressInfo);
				target.getAddresses().add(address);
			}
			target.setBillingNames(null);
			target.setBillingAccountNumber(String.valueOf(source.getBanId()));
			target.setBillingMasterSourceId(130L);
			target.setBrandId((long) source.getBrandId());
			target.setOpenDate(source.getStartServiceDate());
			target.setStatusDate(source.getCreateDate());
			try {
				target.setStatusCode(getReferenceDataFacade().getBillingAccountStatusByKBAccountStatus(String.valueOf(source.getStatus())));
				target.setBillCycleCode(getReferenceDataFacade().getBillCycleCodeByKBBillCycleCode(String.valueOf(source.getBillCycle())));
				target.setNewBillCycleCode(getReferenceDataFacade().getBillCycleCodeByKBBillCycleCode(String.valueOf(source.getNextBillCycle())));
			} catch (TelusException e) {
				throw new SystemException(SystemCodes.CMB_ALF_EJB, e.getMessage(), "", e);
			}
			target.setPayChannel(new PayChannelMapper().mapToSchema(source));
			target.setBillingAccountExtension(new BillingAccountExtensionMapper().mapToSchema(source));
			
			Name billingName = null;
			if (source instanceof PostpaidConsumerAccountInfo) {
				billingName = new NameMapper().mapToSchema(((PostpaidConsumerAccountInfo) source).getName0());
			}else if (source instanceof BasePrepaidAccountInfo) {
				billingName = new NameMapper().mapToSchema(((BasePrepaidAccountInfo) source).getName0());
			}else {
				billingName = new NameMapper().mapToSchema(source.getContactName0());
			}
			if (billingName != null) {
				target.getBillingNames().add(billingName);
			}
			return super.performSchemaMapping(source, target);
		}
	
	}
	
	public static class CustomerMapper extends AbstractSchemaMapper<Customer, AccountInfo> {
		public CustomerMapper() {
			super (Customer.class, AccountInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.common.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected Customer performSchemaMapping(AccountInfo source, Customer target) {
			if (source.getAddress0() != null) {
				EnterpriseAddressInfo enterpriseAddressInfo = new EnterpriseAddressInfo(source.getAddress0());
				updateAddressXref(enterpriseAddressInfo);
				Address address = EnterpriseAddressValidationServiceMapper.EnterpriseAddressMapper().mapToSchema(enterpriseAddressInfo);
				target.getAddressList().add(address);
			}
			try {
				target.setCustomerStatusCode(getReferenceDataFacade().getBillingAccountStatusByKBAccountStatus(String.valueOf(source.getStatus())));
				target.setBillCycleCode(getReferenceDataFacade().getBillCycleCodeByKBBillCycleCode(String.valueOf(source.getBillCycle())));
				target.setNewBillCycleCode(getReferenceDataFacade().getBillCycleCodeByKBBillCycleCode(String.valueOf(source.getNextBillCycle())));
			} catch (TelusException e) {
				throw new SystemException(SystemCodes.CMB_ALF_EJB, e.getMessage(), "", e);
			}
			target.setBrandId((long) source.getBrandId());
			target.setCustomerCreationDate(source.getCreateDate());
			target.setCustomerId(0L);
			target.setCustomerMasterSourceId(1012L); //correct?
			target.setCustomerPIN(source.getPin() == null ? "" : source.getPin());
			target.setCustomerStatusCode("AC");
			target.setCustomerTypeCode("R"); //required
			target.setCustomerSubTypeCode("I");
			target.setMarketSegmentList(null);
			target.setCreditValueCode(null);
		
			Name name = null;
			if (source instanceof PostpaidConsumerAccountInfo) {
				name = new NameMapper().mapToSchema(((PostpaidConsumerAccountInfo) source).getName0());
			}else if (source instanceof BasePrepaidAccountInfo) {
				name = new NameMapper().mapToSchema(((BasePrepaidAccountInfo) source).getName0());
			}else {
				name = new NameMapper().mapToSchema(source.getContactName0());
			}
			if (name != null) {
				target.getNameList().add(name);
			}

			return super.performSchemaMapping(source, target);
		}
		
		
	}
	
	private static void updateAddressXref(EnterpriseAddressInfo enterpriseAddressInfo) {
		updateAddressProvinceXref(enterpriseAddressInfo);
		updateAddressCountryXref(enterpriseAddressInfo);
	}
	
	private static void updateAddressProvinceXref(EnterpriseAddressInfo enterpriseAddressInfo) {
		String kbProvinceCode = enterpriseAddressInfo.getProvinceStateCode();
		if (kbProvinceCode != null && !kbProvinceCode.isEmpty()) {
			try {
				String odsProvinceStateCode = getReferenceDataFacade().getProvinceCodeByKBProvinceCode(kbProvinceCode);
				if (odsProvinceStateCode != null) {
					enterpriseAddressInfo.setProvinceStateCode(odsProvinceStateCode);
				} else {
					LOGGER.error("ODS returns null for mapping province [" + kbProvinceCode + "].");
				}
			} catch (TelusException e) {
				throw new SystemException(SystemCodes.CMB_ALF_EJB, e.getMessage(), "", e);
			}
		}
	}
	
	private static void updateAddressCountryXref(EnterpriseAddressInfo enterpriseAddressInfo) {
		String kbCountryCode = enterpriseAddressInfo.getCountryCode();
		if (kbCountryCode != null && !kbCountryCode.isEmpty()) {
			try {
				String odsCountryCode = getReferenceDataFacade().getCountryCodeByKBCountryCode(kbCountryCode);
				if (odsCountryCode != null) {
					enterpriseAddressInfo.setCountryCode(odsCountryCode);
				} else {
					LOGGER.error("ODS returns null for mapping country [" + kbCountryCode + "].");
				}
			} catch (TelusException e) {
				throw new SystemException(SystemCodes.CMB_ALF_EJB, e.getMessage(), "", e);
			}
		}
	}
}
