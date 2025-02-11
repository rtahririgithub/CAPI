package com.telus.cmb.jws.mapper;

import com.telus.api.TelusAPIException;
import com.telus.api.account.AccountManager;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.jws.mapping.account_information_20.InvoicePropertiesMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.AddressMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.ConsumerNameMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.CreditCardMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.CreditCheckResultMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.PaymentMethodMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.ContactPropertyInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.DebtSummaryInfo;
import com.telus.eas.account.info.FinancialHistoryInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.account.info.MonthlyFinancialActivityInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PostpaidBusinessDealerAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessOfficialAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessPersonalAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.Account;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.AccountMemo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.AccountStatus;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.AutoTopUp;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.BanCategory;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.ContactPropertyListType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.PrepaidAccountInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.ProductSubscriberList;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.BusinessCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.DebtSummary;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.FinancialHistory;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.Language;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.MonthlyFinancialActivity;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.NameFormat;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.OtherPhoneType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PersonalCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.AccountTypeCode;

public class AccountInformationServiceMapper {
	public static AccountMapper AccountMapper() {
		return AccountMapper.getInstance();
	}
	

	public static ContactPropertyMapper ContactPropertyMapper() {
		return ContactPropertyMapper.getInstance();
	}

	/**
	 * AccountMapper
	 * @author tongts
	 *
	 */
	public static class AccountMapper extends AbstractSchemaMapper<Account, AccountInfo> {
		
		private static final int RETURN_MAXIMUM = 1000;
		
		private static AccountMapper INSTANCE = null;

		private AccountMapper() {
			super(Account.class, AccountInfo.class);
		}
		
		protected synchronized static AccountMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new AccountMapper();
			}
			
			return INSTANCE;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java
		 * .lang.Object, java.lang.Object)
		 */
		@SuppressWarnings("deprecation")
		@Override
		protected Account performSchemaMapping(AccountInfo source, Account target) {

			target.setAccountCategory(toEnum(source.getAccountCategory(), BanCategory.class));
			target.setAccountType(toEnum(String.valueOf(source.getAccountType()), AccountTypeCode.class));
			target.setAccountSubType(String.valueOf(source.getAccountSubType()));
			target.setAddress(AddressMapper.getInstance().mapToSchema(source.getAddress0()));
			target.setAlternateCreditCheckAddress(AddressMapper.getInstance().mapToSchema(source.getAlternateCreditCheckAddress0()));
			target.setBillingAccountNumber(Integer.toString(source.getBanId()));
			target.setBanSegment(source.getBanSegment());
			target.setBanSubSegment(source.getBanSubSegment());
			target.setBillCycle(source.getBillCycle());
			target.setBillCycleCloseDay(source.getBillCycleCloseDay());
			target.setBillingNameFormat(toEnum(String.valueOf(source.getBillingNameFormat()), NameFormat.class));
			if (source.isPostpaidConsumer() && source instanceof PostpaidConsumerAccountInfo) {
				target.setBirthDate(((PostpaidConsumerAccountInfo) source).getPersonalCreditInformation0().getBirthDate());
			} else if (source.isPostpaidBusinessRegular() && source instanceof PostpaidBusinessRegularAccountInfo) {
				target.setBirthDate(((PostpaidBusinessRegularAccountInfo) source).getPersonalCreditInformation0().getBirthDate());
				target.setBusinessCreditInformation(new BusinessCreditInformationMapper().mapToSchema(((PostpaidBusinessRegularAccountInfo) source).getCreditInformation0()));
			} else if (source.isPrepaidConsumer() && source instanceof PrepaidConsumerAccountInfo) {
				target.setBirthDate(((PrepaidConsumerAccountInfo) source).getBirthDate());
			}
			target.setBrandId(source.getBrandId());
			target.setBusinessPhone(source.getBusinessPhone());
			target.setBusinessPhoneExtension(source.getBusinessPhoneExtension());
			target.getClientConsentIndicatorCode().addAll(toCollection(source.getClientConsentIndicatorCodes()));
			target.setContactExtension(source.getContactPhoneExtension());
			target.setContactFax(source.getContactFax());
			target.setContactName(ConsumerNameMapper.getInstance().mapToSchema(source.getContactName0()));
			target.setContactPhone(source.getContactPhone());
			target.setCreateDate(source.getCreateDate());
			target.setCreditCheckResult(new CreditCheckResultMapper().mapToSchema(source.getCreditCheckResult0()));
			target.setDealerCode(source.getDealerCode());
			target.setEmail(source.getEmail());
			target.setFinancialHistory(new FinancialHistoryMapper().mapToSchema(source.getFinancialHistory0()));
			target.setFullName(source.getFullName());
			target.setHomePhone(source.getHomePhone());
			String country = source.getAddress0().getCountry();
			if (country != null && !country.trim().equals("") && country.trim().equals("CAN")) {
				if (source.getHomeProvince() != null && !source.getHomeProvince().trim().equals("")) {
					String province = source.getHomeProvince();
					if (province.equals("QC"))
						province = "PQ";
					if (province.equals("NL"))
						province = "NF";
					target.setHomeProvince(toEnum(province, ProvinceCode.class));
				}
			}
			target.setHotlinedInd(source.isHotlined());

			target.setInvoicePropertyList(new InvoicePropertiesMapper().mapToSchema((InvoicePropertiesInfo) source.getInvoiceProperties()));

			target.setIDENInd(source.isIDEN());
			target.setPCSInd(source.isPCS());
			target.setIxcCode(source.getIxcCode());
			if (source.getLanguage() != null && !source.getLanguage().trim().equals("")) {
				target.setLanguage(toEnum(source.getLanguage(), Language.class));
			}
			
			if (source.isPostpaidBusinessRegular() && source instanceof PostpaidBusinessRegularAccountInfo) {
				target.setLegalBusinessName(((PostpaidBusinessRegularAccountInfo) source).getLegalBusinessName());
				target.setTradeNameAttention(((PostpaidBusinessRegularAccountInfo) source).getTradeNameAttention());
				target.setBusinessBillingName(((PostpaidBusinessRegularAccountInfo) source).getLegalBusinessName());
			} else if (source.isPostpaidBusinessDealer() && source instanceof PostpaidBusinessDealerAccountInfo) {
				target.setLegalBusinessName(((PostpaidBusinessDealerAccountInfo) source).getLegalBusinessName());
				target.setTradeNameAttention(((PostpaidBusinessDealerAccountInfo) source).getTradeNameAttention());
				target.setBusinessBillingName(((PostpaidBusinessDealerAccountInfo) source).getLegalBusinessName());
			} else if (source.isPostpaidBusinessOfficial() && source instanceof PostpaidBusinessOfficialAccountInfo) {
				target.setLegalBusinessName(((PostpaidBusinessOfficialAccountInfo) source).getLegalBusinessName());
				target.setTradeNameAttention(((PostpaidBusinessOfficialAccountInfo) source).getTradeNameAttention());
				target.setBusinessBillingName(((PostpaidBusinessOfficialAccountInfo) source).getLegalBusinessName());
			} else if (source.isPostpaidBusinessPersonal() && source instanceof PostpaidBusinessPersonalAccountInfo) {
				target.setLegalBusinessName(((PostpaidBusinessPersonalAccountInfo) source).getLegalBusinessName());
				target.setBusinessBillingName(((PostpaidBusinessPersonalAccountInfo) source).getLegalBusinessName());
			}

			if (source.isPostpaidConsumer() && source instanceof PostpaidConsumerAccountInfo) {
				target.setName(ConsumerNameMapper.getInstance().mapToSchema(((PostpaidConsumerAccountInfo) source).getName0()));
				target.setConsumerBillingName(ConsumerNameMapper.getInstance().mapToSchema(((PostpaidConsumerAccountInfo) source).getName0()));
			} else if (source.isPostpaidBusinessPersonal() && source instanceof PostpaidBusinessPersonalAccountInfo) {
				target.setName(ConsumerNameMapper.getInstance().mapToSchema(((PostpaidBusinessPersonalAccountInfo) source).getName0()));
			} else if (source.isPrepaidConsumer() && source instanceof PrepaidConsumerAccountInfo) {
				target.setName(ConsumerNameMapper.getInstance().mapToSchema(((PrepaidConsumerAccountInfo) source).getName0()));
			}
			
			if (source.getNextBillCycle() > 0) {
				target.setNextBillCycle(source.getNextBillCycle());
			}
			if (source.getNextBillCycleCloseDay() > 0) {
				target.setNextBillCycleCloseDay(source.getNextBillCycleCloseDay());
			}

			target.setOtherPhone(source.getOtherPhone());
			target.setOtherPhoneExtension(source.getOtherPhoneExtension());
			if (source.getOtherPhoneType() != null && !source.getOtherPhoneType().trim().equals("")) {
				target.setOtherPhoneType(toEnum(source.getOtherPhoneType(), OtherPhoneType.class));
			}

			if (source instanceof PostpaidConsumerAccountInfo) {
				target.setPaymentMethod(new PaymentMethodMapper().mapToSchema(((PostpaidConsumerAccountInfo) source).getPaymentMethod0()));
			} else if (source instanceof PostpaidBusinessRegularAccountInfo) {
				target.setPaymentMethod(new PaymentMethodMapper().mapToSchema(((PostpaidBusinessRegularAccountInfo) source).getPaymentMethod0()));
			} else if (source instanceof PostpaidBusinessPersonalAccountInfo) {
				target.setPaymentMethod(new PaymentMethodMapper().mapToSchema(((PostpaidBusinessPersonalAccountInfo) source).getPaymentMethod0()));
			}

			if (source.isPostpaidConsumer() && source instanceof PostpaidConsumerAccountInfo) {
				PersonalCreditInfo creditInfo = ((PostpaidConsumerAccountInfo) source).getPersonalCreditInformation0();
				PersonalCreditInformation pCreditInformation = mapPersonalCreditInformation(creditInfo, source.getAddress0());
				target.setPersonalCreditInformation(pCreditInformation);
			} else if (source.isPostpaidBusinessPersonal() && source instanceof PostpaidBusinessPersonalAccountInfo) {
				PersonalCreditInfo creditInfo = ((PostpaidBusinessPersonalAccountInfo) source).getPersonalCreditInformation0();
				PersonalCreditInformation pCreditInformation = mapPersonalCreditInformation(creditInfo, source.getAddress0());
				target.setPersonalCreditInformation(pCreditInformation);
			}
			
			target.setPin(source.getPin());
			if (source.isPrepaidConsumer() && source instanceof PrepaidConsumerAccountInfo) {
				PrepaidAccountInfo prepaidAccInfo = new PrepaidAccountInfoMapper().mapToSchema((PrepaidConsumerAccountInfo) source);
				target.setPrepaidAccountInfo(prepaidAccInfo);
			}
			ProductSubscriberListInfo[] subList = (ProductSubscriberListInfo[]) source.getProductSubscriberLists();
			if (subList != null && subList.length > 0) {
				int subListLength = (subList.length > RETURN_MAXIMUM ? RETURN_MAXIMUM : subList.length);
				for (int i = 0; i < subListLength; i++) {
					ProductSubscriberList pSubscriberList = new ProductSubscriberList();
					pSubscriberList = new ProductSubscriberListMapper().mapToSchema(subList[i]);
					target.getProductSubscriberList().add(pSubscriberList);
				}
			}
			target.setSalesRepCode(source.getSalesRepCode());
			target.setStartServiceDate(source.getStartServiceDate());
			target.setStatus(toEnum(String.valueOf(source.getStatus()), AccountStatus.class));
			target.setStatusDate(source.getStatusDate());
			target.setVerifiedDate(source.getVerifiedDate());
			target.setCustomerId(Long.valueOf(Integer.toString(source.getCustomerId())));
			target.getFullAddress().addAll(toCollection(source.getFullAddress()));
			target.setStatusActivityCode(source.getStatusActivityCode());
			target.setStatusActivityReasonCode(source.getStatusActivityReasonCode());
			target.setCorporateAccountRepCode(source.getCorporateAccountRepCode() == null || source.getCorporateAccountRepCode().trim().equals("") ? null : source
					.getCorporateAccountRepCode());
			String corpId = source.getCorporateId();
			if (corpId != null && !corpId.equals(""))
				target.setCorporateId(Integer.valueOf(corpId.trim()));
			target.setGstExemptInd(source.isGSTExempt());
			target.setPstExemptInd(source.isPSTExempt());
			target.setHstExemptInd(source.isHSTExempt());
			target.setGstExemptEffDate(source.getGSTExemptEffectiveDate());
			target.setPstExemptEffDate(source.getPSTExemptEffectiveDate());
			target.setHstExemptEffDate(source.getHSTExemptEffectiveDate());
			target.setGstExemptExpDate(source.getGSTExemptExpiryDate());
			target.setPstExemptExpDate(source.getPSTExemptExpiryDate());
			target.setHstExemptExpDate(source.getHSTExemptExpiryDate());
			target.setGstExemptCertNo(source.getGSTCertificateNumber());
			target.setPstExemptCertNo(source.getPSTCertificateNumber());
			target.setHstExemptCertNo(source.getHSTCertificateNumber());
			target.setHandledBySubscriberOnlyInd(source.isHandledBySubscriberOnly());

			return super.performSchemaMapping(source, target);
		}

		private PersonalCreditInformation mapPersonalCreditInformation(PersonalCreditInfo source, AddressInfo address) {
			PersonalCreditInformation target = new PersonalCreditInformation();
			if (source != null) {
				target.setBirthDate(source.getBirthDate());
				target.setCreditCard(new CreditCardMapper().mapToSchema(source.getCreditCard0()));
				target.setDriversLicense(source.getDriversLicense());
				target.setDriversLicenseExpiry(source.getDriversLicenseExpiry());
				String country = address == null ? null : address.getCountry();
				if (country != null && !country.trim().equals("") && country.trim().equals("CAN")) {
					if (source.getDriversLicenseProvince() != null && !source.getDriversLicenseProvince().trim().equals("")) {
						String province = source.getDriversLicenseProvince();
						if (province.equals("QC"))
							province = "PQ";
						if (province.equals("NL"))
							province = "NF";
						target.setDriversLicenseProvince(toEnum(province, ProvinceCode.class));
					}
				}
				target.setSin(source.getSin());
			}
			return target;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java
		 * .lang.Object, java.lang.Object)
		 */
		@Override
		protected AccountInfo performDomainMapping(Account source, AccountInfo target) {
			boolean postpaidConsumer = false;
			boolean postpaidBusinessRegular = false;
			boolean postpaidBusinessDealer = false;
			boolean postpaidBusinessOfficial = false;
			boolean postpaidBusinessPersonal = false;

			if (source.getAccountType() != null && source.getAccountSubType() != null) {
				 
				target.setAccountType(source.getAccountType().value().charAt(0));
                target.setAccountSubType(source.getAccountSubType().charAt(0));
				
				if (target.isPostpaidConsumer()) {
					target = PostpaidConsumerAccountInfo.newPCSInstance();
					postpaidConsumer = true;
				} else if (target.isPostpaidBusinessRegular()) {
					target = PostpaidBusinessRegularAccountInfo.newPCSInstance();
					postpaidBusinessRegular = true;
				} else if (target.isPostpaidBusinessDealer()) {
					target = PostpaidBusinessDealerAccountInfo.newPCSInstance0();
					postpaidBusinessDealer = true;
				} else if (target.isPostpaidBusinessOfficial()) {
					target = PostpaidBusinessOfficialAccountInfo.newPCSInstance0();
					postpaidBusinessOfficial = true;
				} else if (target.isPostpaidBusinessPersonal()) {
					target = PostpaidBusinessPersonalAccountInfo.newPCSInstance0();
					postpaidBusinessPersonal = true;
				}
			}

			if (source.getAccountCategory() != null)
				target.setAccountCategory(source.getAccountCategory().value());
			AddressInfo address = AddressMapper.getInstance().mapToDomain(source.getAddress());
			target.setAddress0(address);
			
			AddressInfo alternateAddress = AddressMapper.getInstance().mapToDomain(source.getAlternateCreditCheckAddress());
			target.getAlternateCreditCheckAddress0().copyFrom(alternateAddress);
			
//			if (createAccount)
//				target.setBanId(0);
//			else
//				target.setBanId(source.getBan());
			target.setBanSegment(source.getBanSegment());
			target.setBanSubSegment(source.getBanSubSegment());
			if (source.getBillingNameFormat() != null)
				target.setBillingNameFormat(source.getBillingNameFormat().value().charAt(0));
			if (postpaidConsumer) {
				PersonalCreditInfo personalCreditInfo = new PersonalCreditInformationMapper().mapToDomain(source.getPersonalCreditInformation());
				((PostpaidConsumerAccountInfo) target).getPersonalCreditInformation0().copyFrom(personalCreditInfo);

				if (source.getName() != null) {
					((PostpaidConsumerAccountInfo) target).getName0().setAdditionalLine(source.getName().getAdditionalLine());
					((PostpaidConsumerAccountInfo) target).getName0().setFirstName(source.getName().getFirstName());
					((PostpaidConsumerAccountInfo) target).getName0().setGeneration(source.getName().getGeneration());
					((PostpaidConsumerAccountInfo) target).getName0().setLastName(source.getName().getLastName());
					((PostpaidConsumerAccountInfo) target).getName0().setMiddleInitial(source.getName().getMiddleInitial());
					if (source.getName().getNameFormat() != null)
						((PostpaidConsumerAccountInfo) target).getName0().setNameFormat(source.getName().getNameFormat().value());
					((PostpaidConsumerAccountInfo) target).getName0().setTitle(source.getName().getTitle());
				}
				
				PaymentMethodInfo paymentMethodInfo = new PaymentMethodMapper().mapToDomain(source.getPaymentMethod());
				((PostpaidConsumerAccountInfo) target).getPaymentMethod0().copyFrom(paymentMethodInfo);
			} else if (postpaidBusinessRegular) {
				PersonalCreditInfo personalCreditInfo = new PersonalCreditInformationMapper().mapToDomain(source.getPersonalCreditInformation());
				if (personalCreditInfo != null)
					((PostpaidBusinessRegularAccountInfo) target).getPersonalCreditInformation0().copyFrom(personalCreditInfo);
				
				BusinessCreditInfo businessCreditInfo = new BusinessCreditInformationMapper().mapToDomain(source.getBusinessCreditInformation());
				((PostpaidBusinessRegularAccountInfo) target).getCreditInformation0().copyFrom(businessCreditInfo);
				
				PaymentMethodInfo paymentMethodInfo = new PaymentMethodMapper().mapToDomain(source.getPaymentMethod());
				((PostpaidBusinessRegularAccountInfo) target).getPaymentMethod0().copyFrom(paymentMethodInfo);
			} else if (postpaidBusinessPersonal) {
				PersonalCreditInfo personalCreditInfo = new PersonalCreditInformationMapper().mapToDomain(source.getPersonalCreditInformation());
				if (personalCreditInfo != null)
					((PostpaidBusinessPersonalAccountInfo) target).getPersonalCreditInformation0().copyFrom(personalCreditInfo);

				if (source.getName() != null) {
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setAdditionalLine(source.getName().getAdditionalLine());
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setFirstName(source.getName().getFirstName());
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setGeneration(source.getName().getGeneration());
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setLastName(source.getName().getLastName());
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setMiddleInitial(source.getName().getMiddleInitial());
					if (source.getName().getNameFormat() != null)
						((PostpaidBusinessPersonalAccountInfo) target).getName0().setNameFormat(source.getName().getNameFormat().value());
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setTitle(source.getName().getTitle());
				}
				
				PaymentMethodInfo paymentMethodInfo = new PaymentMethodMapper().mapToDomain(source.getPaymentMethod());
				((PostpaidBusinessPersonalAccountInfo) target).getPaymentMethod0().copyFrom(paymentMethodInfo);
			}
			target.setBrandId(source.getBrandId());
			target.setBusinessPhone(source.getBusinessPhone());
			target.setBusinessPhoneExtension(source.getBusinessPhoneExtension());
			
			target.setClientConsentIndicatorCodes(source.getClientConsentIndicatorCode().toArray(new String[0]));
			
			target.setContactFax(source.getContactFax());
			if (source.getContactName() != null) {
				target.getContactName0().setAdditionalLine(source.getContactName().getAdditionalLine());
				target.getContactName0().setFirstName(source.getContactName().getFirstName());
				target.getContactName0().setGeneration(source.getContactName().getGeneration());
				target.getContactName0().setLastName(source.getContactName().getLastName());
				target.getContactName0().setMiddleInitial(source.getContactName().getMiddleInitial());
				if (source.getContactName().getNameFormat() != null)
					target.getContactName0().setNameFormat(source.getContactName().getNameFormat().value());
				target.getContactName0().setTitle(source.getContactName().getTitle());
			}
			target.setContactPhone(source.getContactPhone());
			target.setContactPhoneExtension(source.getContactExtension());
			target.setCorporateAccountRepCode(source.getCorporateAccountRepCode());
			if (source.getCorporateId() != null)
				target.setCorporateId(String.valueOf(source.getCorporateId().intValue()));
			target.setCreateDate(new java.util.Date());
			target.setCustomerId(0);

			CreditCheckResultInfo creditCheckResultInfo = new CreditCheckResultMapper().mapToDomain(source.getCreditCheckResult());
			target.getCreditCheckResult0().copyFrom(creditCheckResultInfo);

			if (postpaidBusinessRegular) {
				((PostpaidBusinessRegularAccountInfo) target).setLegalBusinessName(source.getLegalBusinessName());
				((PostpaidBusinessRegularAccountInfo) target).setTradeNameAttention(source.getTradeNameAttention());
			} else if (postpaidBusinessDealer) {
				((PostpaidBusinessDealerAccountInfo) target).setLegalBusinessName(source.getLegalBusinessName());
				((PostpaidBusinessDealerAccountInfo) target).setTradeNameAttention(source.getTradeNameAttention());
			} else if (postpaidBusinessOfficial) {
				((PostpaidBusinessOfficialAccountInfo) target).setLegalBusinessName(source.getLegalBusinessName());
				((PostpaidBusinessOfficialAccountInfo) target).setTradeNameAttention(source.getTradeNameAttention());
			} else if (postpaidBusinessPersonal)
				((PostpaidBusinessPersonalAccountInfo) target).setLegalBusinessName(source.getLegalBusinessName());

			target.setDealerCode(source.getDealerCode());
			target.setEmail(source.getEmail());
			target.setEvaluationProductType(AccountManager.PRODUCT_TYPE_PCS);
			target.setFidoConversion(false);
			target.setGSTCertificateNumber(source.getGstExemptCertNo());
			if (source.isGstExemptInd() != null)
				target.setGstExempt(source.isGstExemptInd() ? (byte) 'Y' : (byte) 'N');
			if (source.getGstExemptEffDate() != null)
				target.setGSTExemptEffectiveDate(source.getGstExemptEffDate());
			if (source.getGstExemptExpDate() != null)
				target.setGSTExemptExpiryDate(source.getGstExemptExpDate());
			if (source.isHandledBySubscriberOnlyInd() != null)
				target.setHandledBySubscriberOnly(source.isHandledBySubscriberOnlyInd());
			target.setHomePhone(source.getHomePhone());
			if (source.getHomeProvince() != null)
				target.setHomeProvince(source.getHomeProvince().value());
			if (source.isHotlinedInd() != null)
				target.setHotlined(source.isHotlinedInd());
			target.setHSTCertificateNumber(source.getHstExemptCertNo());
			if (source.isHstExemptInd() != null)
				target.setHstExempt(source.isHstExemptInd() ? (byte) 'Y' : (byte) 'N');
			if (source.getHstExemptEffDate() != null)
				target.setHSTExemptEffectiveDate(source.getHstExemptEffDate());
			if (source.getHstExemptExpDate() != null)
				target.setHSTExemptExpiryDate(source.getHstExemptExpDate());
			
			InvoicePropertiesInfo invProperties = new InvoicePropertiesMapper().mapToDomain(source.getInvoicePropertyList());
			target.setInvoiceProperties(invProperties);
			
			target.setIxcCode(source.getIxcCode());
			target.setLanguage(source.getLanguage().value());
			target.setOtherPhone(source.getOtherPhone());
			target.setOtherPhoneExtension(source.getOtherPhoneExtension());
			if (source.getOtherPhoneType() != null)
				target.setOtherPhoneType(source.getOtherPhoneType().value());
			target.setPin(source.getPin());
			target.setPSTCertificateNumber(source.getPstExemptCertNo());
			if (source.isPstExemptInd() != null)
				target.setPstExempt(source.isPstExemptInd() ? (byte) 'Y' : (byte) 'N');
			if (source.getPstExemptEffDate() != null)
				target.setPSTExemptEffectiveDate(source.getPstExemptEffDate());
			if (source.getPstExemptExpDate() != null)
				target.setPSTExemptExpiryDate(source.getPstExemptExpDate());
			target.setSalesRepCode(source.getSalesRepCode());

			return super.performDomainMapping(source, target);
		}

		
	}
	
	
	/**
	 * BusinessCreditInformaitonMapper
	 * @author tongts
	 *
	 */
	public static class BusinessCreditInformationMapper extends AbstractSchemaMapper<BusinessCreditInformation, BusinessCreditInfo> {

		private static BusinessCreditInformationMapper INSTANCE;
		
		private BusinessCreditInformationMapper(){
			super(BusinessCreditInformation.class, BusinessCreditInfo.class);
		}

		protected static synchronized BusinessCreditInformationMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new BusinessCreditInformationMapper();
			}
			return INSTANCE;
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected BusinessCreditInformation performSchemaMapping(BusinessCreditInfo source, BusinessCreditInformation target) {
			target.setIncorporationDate(source.getIncorporationDate());
			target.setIncorporationNumber(source.getIncorporationNumber());

			return super.performSchemaMapping(source, target);
		}

		/*
		 * (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected BusinessCreditInfo performDomainMapping(BusinessCreditInformation source, BusinessCreditInfo target) {
	        target.setIncorporationDate(source.getIncorporationDate());
	        target.setIncorporationNumber(source.getIncorporationNumber());
			
			return super.performDomainMapping(source, target);
		}
	}

	

	/**
	 * FinancialHistoryMapper
	 * @author tongts
	 *
	 */
	private static class FinancialHistoryMapper extends AbstractSchemaMapper<FinancialHistory, FinancialHistoryInfo> {

		public FinancialHistoryMapper(){
			super(FinancialHistory.class, FinancialHistoryInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected FinancialHistory performSchemaMapping(FinancialHistoryInfo source, FinancialHistory target) {

			target.setDebtSummary(new DebtSummaryMapper().mapToSchema(source.getDebtSummary0()));

			target.setDelinquentInd(source.isDelinquent());
			target.setLastPaymentAmount(source.getLastPaymentAmount());
			target.setLastPaymentDate(source.getLastPaymentDate());
			MonthlyFinancialActivityInfo[] monthlyFinancialActivity = source.getMonthlyFinancialActivity0();
			if (monthlyFinancialActivity != null) {
				for (int i = 0; i < monthlyFinancialActivity.length; i++) {
					MonthlyFinancialActivity mFinancialActivity = new MonthlyFinancialActivity();

					mFinancialActivity.setActivity(monthlyFinancialActivity[i].getActivity());
					mFinancialActivity.setDishonoredPaymentCount(monthlyFinancialActivity[i].getDishonoredPaymentCount());
					mFinancialActivity.setMonth(monthlyFinancialActivity[i].getMonth());
					mFinancialActivity.setYear(monthlyFinancialActivity[i].getYear());
					target.getMonthlyFinancialActivities().add(mFinancialActivity);
				}
			}
			target.setWrittenOffInd(source.isWrittenOff());
			return super.performSchemaMapping(source, target);
		}
	}
	
	
	/**
	 * DebtSummaryMapper
	 * @author tongts
	 *
	 */
	private static class DebtSummaryMapper extends AbstractSchemaMapper<DebtSummary, DebtSummaryInfo> {

		public DebtSummaryMapper(){
			super(DebtSummary.class, DebtSummaryInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected DebtSummary performSchemaMapping(DebtSummaryInfo source, DebtSummary target) {
			target.setBillDueDate(source.getBillDueDate());
			target.setCurrentDue(source.getCurrentDue());
			target.setPastDue(source.getPastDue());
			target.setPastDue1To30Days(source.getPastDue1to30Days());
			target.setPastDue31To60Days(source.getPastDue31to60Days());
			target.setPastDue61To90Days(source.getPastDue61to90Days());
			target.setPastDueOver90Days(source.getPastDueOver90Days());

			return super.performSchemaMapping(source, target);
		}

	}
	
	/**
	 * PrepaidAccountInfoMapper
	 * @author tongts
	 *
	 */
	private static class PrepaidAccountInfoMapper extends AbstractSchemaMapper<PrepaidAccountInfo, PrepaidConsumerAccountInfo> {

		public PrepaidAccountInfoMapper(){
			super(PrepaidAccountInfo.class, PrepaidConsumerAccountInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected PrepaidAccountInfo performSchemaMapping(PrepaidConsumerAccountInfo source, PrepaidAccountInfo target) {
			target.setAirtimeRate(source.getAirtimeRate());
			AutoTopUp autoTopUp = new AutoTopUpMapper().mapToSchema(source.getAutoTopUp0());
			target.setAutoTopUp(autoTopUp);
			target.setBalance(source.getBalance());
			target.setBalanceExpiryDate(source.getBalanceExpiryDate());
			target.setBillingType(source.getBillingType());
			target.setLongDistanceRate(source.getLongDistanceRate());
			target.setMaximumBalanceCap(source.getMaximumBalanceCap());
			target.setMinimumBalanceDate(source.getMinimumBalanceDate());
			target.setOutstandingCharge(source.getOutstandingCharge());
			target.setTopUpCreditCard(new CreditCardMapper().mapToSchema(source.getTopUpCreditCard0()));

			return super.performSchemaMapping(source, target);
		}

	}
	
	
	/**
	 * AutoTopUpMapper
	 * @author tongts
	 *
	 */
	private static class AutoTopUpMapper extends AbstractSchemaMapper<AutoTopUp, AutoTopUpInfo> {

		public AutoTopUpMapper(){
			super(AutoTopUp.class, AutoTopUpInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected AutoTopUp performSchemaMapping(AutoTopUpInfo source, AutoTopUp target) {
			target.setChargeAmount(source.getChargeAmount());
			target.setHasThresholdRecharge(source.hasThresholdRecharge());
			target.setNextChargeDate(source.getNextChargeDate());
			target.setThresholdAmount(source.getThresholdAmount());

			return super.performSchemaMapping(source, target);
		}
	}
	
	
	/**
	 * ProductSubscriberListMapper
	 * @author tongts
	 *
	 */
	private static class ProductSubscriberListMapper extends AbstractSchemaMapper<ProductSubscriberList, ProductSubscriberListInfo> {

		public ProductSubscriberListMapper(){
			super(ProductSubscriberList.class, ProductSubscriberListInfo.class);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected ProductSubscriberList performSchemaMapping(ProductSubscriberListInfo source, ProductSubscriberList target) {
			target.getActiveSubscriber().addAll(toCollection(source.getActiveSubscribers()));
			target.getCancelledSubscriber().addAll(toCollection(source.getCancelledSubscribers()));
			target.setProductType(source.getProductType());
			target.getReservedSubscriber().addAll(toCollection(source.getReservedSubscribers()));
			target.getSuspendedSubscriber().addAll(toCollection(source.getSuspendedSubscribers()));

			return super.performSchemaMapping(source, target);
		}
	}
	
	/**
	 * PersonalCreditInformationMapper
	 * @author tongts
	 *
	 */
	public static class PersonalCreditInformationMapper extends AbstractSchemaMapper<PersonalCreditInformation, PersonalCreditInfo> {

		private static PersonalCreditInformationMapper INSTANCE;
		
		private PersonalCreditInformationMapper(){
			super(PersonalCreditInformation.class, PersonalCreditInfo.class);
		}

		protected static synchronized PersonalCreditInformationMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new PersonalCreditInformationMapper();
			}
			return INSTANCE;
		}

		/*
		 * (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected PersonalCreditInformation performSchemaMapping(PersonalCreditInfo source, PersonalCreditInformation target) {
			if (source.getBirthDate() != null)
				target.setBirthDate(source.getBirthDate());
			target.setDriversLicense(source.getDriversLicense());
			if (source.getDriversLicenseExpiry() != null)
				target.setDriversLicenseExpiry(source.getDriversLicenseExpiry());
			if (source.getDriversLicenseProvince() != null)
				target.setDriversLicenseProvince(ProvinceCode.fromValue(source.getDriversLicenseProvince()));
			target.setSin(source.getSin());
			target.setCreditCard(new CreditCardMapper().mapToSchema(source.getCreditCard0()));
			
			return super.performSchemaMapping(source, target);
		}


		/*
		 * (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected PersonalCreditInfo performDomainMapping(PersonalCreditInformation source, PersonalCreditInfo target) {
			if (source.getBirthDate() != null)
				target.setBirthDate(source.getBirthDate());
			target.setDriversLicense(source.getDriversLicense());
			if (source.getDriversLicenseExpiry() != null)
				target.setDriversLicenseExpiry(source.getDriversLicenseExpiry());
			if (source.getDriversLicenseProvince() != null)
				target.setDriversLicenseProvince(source.getDriversLicenseProvince().value());
			target.setSin(source.getSin());

			if (source.getCreditCard() != null) {
				String token = source.getCreditCard().getToken();
				String first6 = source.getCreditCard().getFirst6();
				String last4 = source.getCreditCard().getLast4();
				if (token != null && !token.equals("")  &&
					first6 != null && !first6.equals("")  &&	
					last4 != null && !last4.equals("")  ) {
				
					try {
						target.getCreditCard().setToken(token,first6 , last4);
					} catch (TelusAPIException e) {
						e.printStackTrace();
					}
				}
				target.getCreditCard().setCardVerificationData(source.getCreditCard().getCardVerificationData());
				target.getCreditCard().setExpiryMonth(source.getCreditCard().getExpiryMonth());
				target.getCreditCard().setExpiryYear(source.getCreditCard().getExpiryYear());
				target.getCreditCard().setHolderName(source.getCreditCard().getHolderName());
			}
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class ContactPropertyMapper extends AbstractSchemaMapper<ContactPropertyListType, ContactPropertyInfo> {
		private static ContactPropertyMapper INSTANCE = null;
		
		private ContactPropertyMapper() {
			super (ContactPropertyListType.class, ContactPropertyInfo.class);
		}
		
		protected static synchronized ContactPropertyMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ContactPropertyMapper();
			}
			
			return INSTANCE;
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected ContactPropertyListType performSchemaMapping(ContactPropertyInfo source, ContactPropertyListType target) {
			if (source != null) {
				target.setBusinessPhone(source.getBusinessPhoneNumber());
				target.setBusinessPhoneExtension(source.getBusinessPhoneExtension());
				target.setContactExtension(source.getContactPhoneExtension());
				target.setContactFax(source.getContactFax());
				target.setContactName(ConsumerNameMapper.getInstance().mapToSchema(source.getName()));
				target.setContactPhone(source.getContactPhoneNumber());
				target.setHomePhone(source.getHomePhoneNumber());
				target.setOtherPhone(source.getOtherPhoneNumber());
				target.setOtherPhoneExtension(source.getOtherPhoneExtension());
				if (source.getOtherPhoneType() != null) {
					target.setOtherPhoneType(toEnum(source.getOtherPhoneType(), OtherPhoneType.class));
				}
			}
			return super.performSchemaMapping(source, target);
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
		 */
		@Override
		protected ContactPropertyInfo performDomainMapping(ContactPropertyListType source, ContactPropertyInfo target) {
			target.setBusinessPhoneExtension(source.getBusinessPhoneExtension());
			target.setBusinessPhoneNumber(source.getBusinessPhone());
			target.setContactFax(source.getContactFax());
			target.setContactPhoneExtension(source.getContactExtension());
			target.setContactPhoneNumber(source.getContactPhone());
			target.setHomePhoneNumber(source.getHomePhone());
			target.setName(ConsumerNameMapper.getInstance().mapToDomain(source.getContactName()));
			target.setOtherPhoneExtension(source.getOtherPhoneExtension());
			target.setOtherPhoneNumber(source.getOtherPhone());
			if (source.getOtherPhoneType() != null) {
				target.setOtherPhoneType(source.getOtherPhoneType().value());
			}
			return super.performDomainMapping(source, target);
		}
	}
	
	public static class AccountMemoMapper extends AbstractSchemaMapper<AccountMemo, MemoInfo>  {
		
		private static AccountMemoMapper INSTANCE;
		
		private AccountMemoMapper() {
			super (AccountMemo.class, MemoInfo.class);
		}
		
		protected static synchronized AccountMemoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new AccountMemoMapper();
			}
			return INSTANCE;
		}

		@Override
		protected MemoInfo performDomainMapping(AccountMemo source, MemoInfo target) {
			target.setDate(source.getCreationDate());
			if (source.getMemoId() != null) {
				target.setMemoId(source.getMemoId().doubleValue());
			}
			target.setText(source.getMemoText());
			target.setMemoType(source.getMemoType());
			if (source.getModifyDate() != null) {
				target.setModifyDate(source.getModifyDate());
			}
			if (source.getOperatorId() != null) {
				target.setOperatorId(Integer.parseInt(source.getOperatorId()));
			}
			
			target.setSystemText(source.getSystemText());
			return super.performDomainMapping(source, target);
		}

		@Override
		protected AccountMemo performSchemaMapping(MemoInfo source, AccountMemo target) {
			target.setCreationDate(source.getDate());
			target.setMemoId(Double.valueOf(source.getMemoId()));
			target.setMemoText(source.getText());
			target.setMemoType(source.getMemoType());
			target.setModifyDate(source.getModifyDate());
			target.setOperatorId(String.valueOf(source.getOperatorId()));
			target.setSystemText(source.getSystemText());
			return super.performSchemaMapping(source, target);
		}
	}
	
	public static AccountMemoMapper AccountMemoMapper() {
		return AccountMemoMapper.getInstance();
	}
	
	public static PersonalCreditInformationMapper PersonalCreditMapper(){
		return PersonalCreditInformationMapper.getInstance();
	}

	public static BusinessCreditInformationMapper BusinessCreditMapper() {
		return BusinessCreditInformationMapper.getInstance();
	}
}
