package com.telus.cmb.account.mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.eas.account.credit.info.CreditBureauDocumentInfo;
import com.telus.eas.account.credit.info.MatchedAccountInfo;
import com.telus.eas.account.credit.info.MatchedReasonInfo;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.ActivationOptionInfo;
import com.telus.eas.account.info.ActivationOptionTypeInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.BankAccountInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.CLPActivationOptionDetailInfo;
import com.telus.eas.account.info.ChequeInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.DebtSummaryInfo;
import com.telus.eas.account.info.FinancialHistoryInfo;
import com.telus.eas.account.info.HCDclpActivationOptionDetailsInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.account.info.MonthlyFinancialActivityInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PostpaidBoxedConsumerAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessPersonalAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.account.info.PostpaidCorporatePersonalAccountInfo;
import com.telus.eas.account.info.PostpaidEmployeeAccountInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.creditprofilesvcrequestresponse_v3.FindAccountsByCustomerProfile;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.Account;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.AccountStatus;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.BanCategory;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.InvoicePropertyListType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.ProductSubscriberList;
import com.telus.tmi.xmlschema.xsd.customer.customer.creditprofiletypes_v2.BusinessAccountInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.creditprofiletypes_v2.BusinessCreditIdentity;
import com.telus.tmi.xmlschema.xsd.customer.customer.creditprofiletypes_v2.CLPActivationOption;
import com.telus.tmi.xmlschema.xsd.customer.customer.creditprofiletypes_v2.CreditAddress;
import com.telus.tmi.xmlschema.xsd.customer.customer.creditprofiletypes_v2.CreditBusinessCustomerInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.creditprofiletypes_v2.EligibilityResult;
import com.telus.tmi.xmlschema.xsd.customer.customer.creditprofiletypes_v2.MatchReason;
import com.telus.tmi.xmlschema.xsd.customer.customer.creditprofiletypes_v2.MatchedAccount;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.Address;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.AddressType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.BankAccount;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.BusinessCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.Cheque;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ConsumerName;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.CreditCard;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.CreditCheckResult;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.CreditCheckResultDeposit;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.DebtSummary;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.FinancialHistory;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.Language;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.MonthlyFinancialActivity;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.NameFormat;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.OtherPhoneType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PaymentMethod;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PersonalCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.AccountTypeCode;

public class CreditProfileServiceMapper {

	public static AccountMapper AccountMapper() {
		return AccountMapper.getInstance();
	}

	public static BusinessCreditIdentityMapper BusinessCreditIdentityMapper() {
		return BusinessCreditIdentityMapper.getInstance();
	}

	public static CreditCheckResponseMapper CreditCheckResponseMapper() {
		return CreditCheckResponseMapper.getInstance();
	}

	public static EligibilityCheckResponseMapper EligibilityCheckResponseMapper() {
		return EligibilityCheckResponseMapper.getInstance();
	}

	public static CLPActivationOptionMapper CLPActivationOptionMapper() {
		return CLPActivationOptionMapper.getInstance();
	}

	public static BusinessAccountInfoMapper BusinessAccountInfoMapper() {
		return BusinessAccountInfoMapper.getInstance();
	}

	public static CreditBusinessCustomerInfoMapper CreditBusinessCustomerInfoMapper() {
		return CreditBusinessCustomerInfoMapper.getInstance();
	}

	public static FindAccountsByCustomerProfileMapper FindAccountsByCustomerProfileMapper() {
		return FindAccountsByCustomerProfileMapper.getInstance();
	}
	
	public static MatchedAccountMapper MatchedAccountMapper() {
		return MatchedAccountMapper.getInstance();
	}

	public static class AccountMapper extends AbstractSchemaMapper<Account, AccountInfo> {

		private static AccountMapper INSTANCE = null;

		public AccountMapper() {
			super(Account.class, AccountInfo.class);
		}

		protected synchronized static AccountMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new AccountMapper();
			}
			return INSTANCE;
		}

		@Override
		protected Account performSchemaMapping(AccountInfo source, Account target) {

			Date birthDate = null;
			target.setBillingAccountNumber(Integer.toString(source.getBanId()));
			target.setAccountType(toEnum(String.valueOf(source.getAccountType()), AccountTypeCode.class));
			target.setAccountSubType(String.valueOf(source.getAccountSubType()));
			target.setAccountCategory(toEnum(source.getAccountCategory(), BanCategory.class));
			target.setBrandId(source.getBrandId());
			target.setCustomerId(Long.valueOf(Integer.toString(source.getCustomerId())));
			target.setBanSegment(source.getBanSegment());
			target.setBanSubSegment(source.getBanSubSegment());
			target.setStatus(toEnum(String.valueOf(source.getStatus()), AccountStatus.class));
			target.setStatusDate(source.getStatusDate());
			target.setStatusActivityCode(source.getStatusActivityCode());
			target.setStatusActivityReasonCode(source.getStatusActivityReasonCode());
			target.setCreateDate(source.getCreateDate());
			target.setStartServiceDate(source.getStartServiceDate());
			target.setBillCycle(source.getBillCycle());
			target.setBillCycleCloseDay(source.getBillCycleCloseDay());
			if (source.getNextBillCycle() > 0) {
				target.setNextBillCycle(source.getNextBillCycle());
			}
			if (source.getNextBillCycleCloseDay() > 0) {
				target.setNextBillCycleCloseDay(source.getNextBillCycleCloseDay());
			}
			target.setIxcCode(source.getIxcCode());
			target.setAddress(new AddressMapper().mapToSchema(source.getAddress0()));
			target.setAlternateCreditCheckAddress(new AddressMapper().mapToSchema(source.getAlternateCreditCheckAddress0()));
			String country = source.getAddress0().getCountry();
			if (country != null && !country.trim().equals("") && country.trim().equals("CAN")) {
				if (source.getHomeProvince() != null && !source.getHomeProvince().trim().equals("")) {
					String province = source.getHomeProvince();
					if (province.equals("QC")) {
						province = "PQ";
					}
					if (province.equals("NL")) {
						province = "NF";
					}
					target.setHomeProvince(toEnum(province, ProvinceCode.class));
				}
			}
			target.setOtherPhone(source.getOtherPhone());
			if (source.getOtherPhoneType() != null && !source.getOtherPhoneType().trim().equals("")) {
				target.setOtherPhoneType(toEnum(source.getOtherPhoneType(), OtherPhoneType.class));
			}
			target.setOtherPhoneExtension(source.getOtherPhoneExtension());
			target.setHomePhone(source.getHomePhone());
			target.setBusinessPhone(source.getBusinessPhone());
			target.setBusinessPhoneExtension(source.getBusinessPhoneExtension());
			target.setPin(source.getPin());
			target.setEmail(source.getEmail());
			if (source.getLanguage() != null && !source.getLanguage().trim().equals("")) {
				target.setLanguage(toEnum(source.getLanguage(), Language.class));
			}
			target.setVerifiedDate(source.getVerifiedDate());
			if (source.isPostpaidConsumer()) {
				birthDate = ((PostpaidConsumerAccountInfo) source).getPersonalCreditInformation0().getBirthDate();
			} else if (source.isPostpaidBusinessRegular()) {
				birthDate = ((PostpaidBusinessRegularAccountInfo) source).getPersonalCreditInformation0().getBirthDate();
			} else if (source.isPrepaidConsumer()) {
				birthDate = ((PrepaidConsumerAccountInfo) source).getBirthDate();
			}
			target.setBirthDate(birthDate);
			target.setDealerCode(source.getDealerCode());
			target.setSalesRepCode(source.getSalesRepCode());
			String corpId = source.getCorporateId();
			if (corpId != null && !corpId.equals("")) {
				target.setCorporateId(Integer.valueOf(corpId.trim()));
			}
			target.setCorporateAccountRepCode(source.getCorporateAccountRepCode() == null || source.getCorporateAccountRepCode().trim().equals("") ? null : source.getCorporateAccountRepCode());
			target.setInvoicePropertyList(new InvoicePropertiesMapper().mapToSchema((InvoicePropertiesInfo) source.getInvoiceProperties()));
			if (source.getClientConsentIndicatorCodes() != null) {
				target.getClientConsentIndicatorCode().addAll(Arrays.asList(source.getClientConsentIndicatorCodes()));
			}
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
			if (source.isPostpaidConsumer()) {
				target.setName(new ConsumerNameMapper().mapToSchema(((PostpaidConsumerAccountInfo) source).getName0()));
				target.setConsumerBillingName(new ConsumerNameMapper().mapToSchema(((PostpaidConsumerAccountInfo) source).getName0()));
				target.setPaymentMethod(new PaymentMethodMapper().mapToSchema(((PostpaidConsumerAccountInfo) source).getPaymentMethod0()));
				PersonalCreditInfo creditInfo = ((PostpaidConsumerAccountInfo) source).getPersonalCreditInformation0();
				target.setPersonalCreditInformation(new PersonalCreditInformationMapper().mapToSchema(creditInfo));
			} else if (source.isPostpaidBoxedConsumer()) {
				target.setName(new ConsumerNameMapper().mapToSchema(((PostpaidBoxedConsumerAccountInfo) source).getName0()));
				PersonalCreditInfo creditInfo = ((PostpaidBoxedConsumerAccountInfo) source).getPersonalCreditInformation0();
				target.setPersonalCreditInformation(new PersonalCreditInformationMapper().mapToSchema(creditInfo));
				target.setPaymentMethod(new PaymentMethodMapper().mapToSchema(((PostpaidBoxedConsumerAccountInfo) source).getPaymentMethod0()));
			} else if (source.isPostpaidBusinessPersonal()) {
				PersonalCreditInfo creditInfo = ((PostpaidBusinessPersonalAccountInfo) source).getPersonalCreditInformation0();
				target.setPersonalCreditInformation(new PersonalCreditInformationMapper().mapToSchema(creditInfo));
				target.setName(new ConsumerNameMapper().mapToSchema(((PostpaidBusinessPersonalAccountInfo) source).getName0()));
				target.setPaymentMethod(new PaymentMethodMapper().mapToSchema(((PostpaidBusinessPersonalAccountInfo) source).getPaymentMethod0()));
				target.setLegalBusinessName(((PostpaidBusinessPersonalAccountInfo) source).getLegalBusinessName());
				target.setBusinessBillingName(((PostpaidBusinessPersonalAccountInfo) source).getLegalBusinessName());
			} else if (source.isPostpaidBusinessDealer() || source.isPostpaidBusinessOfficial() || source.isPostpaidBusinessRegular() || source.isPostpaidCorporateRegular()) {
				PersonalCreditInfo creditInfo = ((PostpaidBusinessRegularAccountInfo) source).getPersonalCreditInformation0();
				target.setPersonalCreditInformation(new PersonalCreditInformationMapper().mapToSchema(creditInfo));
				target.setPaymentMethod(new PaymentMethodMapper().mapToSchema(((PostpaidBusinessRegularAccountInfo) source).getPaymentMethod0()));
				target.setLegalBusinessName(((PostpaidBusinessRegularAccountInfo) source).getLegalBusinessName());
				target.setTradeNameAttention(((PostpaidBusinessRegularAccountInfo) source).getTradeNameAttention());
				target.setBusinessBillingName(((PostpaidBusinessRegularAccountInfo) source).getLegalBusinessName());
				target.setBusinessCreditInformation(new BusinessCreditInformationMapper().mapToSchema(((PostpaidBusinessRegularAccountInfo) source).getCreditInformation0()));
			} else if (source.isPostpaidCorporatePersonal()) {
				target.setName(new ConsumerNameMapper().mapToSchema(((PostpaidCorporatePersonalAccountInfo) source).getName0()));
				PersonalCreditInfo creditInfo = ((PostpaidCorporatePersonalAccountInfo) source).getPersonalCreditInformation0();
				target.setPersonalCreditInformation(new PersonalCreditInformationMapper().mapToSchema(creditInfo));
				target.setPaymentMethod(new PaymentMethodMapper().mapToSchema(((PostpaidCorporatePersonalAccountInfo) source).getPaymentMethod0()));
			} else if (source.isPostpaidEmployee()) {
				target.setName(new ConsumerNameMapper().mapToSchema(((PostpaidEmployeeAccountInfo) source).getName0()));
				PersonalCreditInfo creditInfo = ((PostpaidEmployeeAccountInfo) source).getPersonalCreditInformation0();
				target.setPersonalCreditInformation(new PersonalCreditInformationMapper().mapToSchema(creditInfo));
				target.setPaymentMethod(new PaymentMethodMapper().mapToSchema(((PostpaidEmployeeAccountInfo) source).getPaymentMethod0()));
			}
			target.setFullName(source.getFullName());
			if (source.getFullAddress() != null) {
				target.getFullAddress().addAll(Arrays.asList(source.getFullAddress()));
			}
			target.setContactName(new ConsumerNameMapper().mapToSchema(source.getContactName0()));
			target.setContactPhone(source.getContactPhone());
			target.setContactExtension(source.getContactPhoneExtension());
			target.setContactFax(source.getContactFax());
			target.setCreditCheckResult(new CreditCheckResultMapper().mapToSchema(source.getCreditCheckResult0()));
			target.setFinancialHistory(new FinancialHistoryMapper().mapToSchema(source.getFinancialHistory0()));
			target.setHotlinedInd(source.isHotlined());
			target.setIDENInd(source.isIDEN());
			target.setPCSInd(source.isPCS());
			ProductSubscriberListInfo[] subList = (ProductSubscriberListInfo[]) source.getProductSubscriberLists();
			if (subList != null && subList.length > 0) {
				ProductSubscriberList[] pSubscriberList = new ProductSubscriberList[subList.length];
				for (int i = 0; i < subList.length; i++) {
					pSubscriberList[i] = new ProductSubscriberListMapper().mapToSchema(subList[i]);
				}
				target.getProductSubscriberList().addAll(Arrays.asList(pSubscriberList));
			}
			target.setBillingNameFormat(toEnum(String.valueOf(source.getBillingNameFormat()), NameFormat.class));
			target.setHandledBySubscriberOnlyInd(source.isHandledBySubscriberOnly());
			target.setPagerInd(source.isPager());
			target.setPostpaidConsumerInd(source.isPostpaidConsumer());
			target.setPostpaidBusinessRegularInd(source.isPostpaidBusinessRegular());
			target.setPostpaidBusinessDealerInd(source.isPostpaidBusinessDealer());
			target.setPostpaidBusinessOfficialInd(source.isPostpaidBusinessOfficial());
			target.setPostpaidBusinessPersonalInd(source.isPostpaidBusinessPersonal());
			target.setPostpaidOfficialInd(source.isPostpaidOfficial());
			target.setPostpaidEmployeeInd(source.isPostpaidEmployee());
			target.setPostpaidCorporatePersonalInd(source.isPostpaidCorporatePersonal());

			// if (source.isPrepaidConsumer()) {
			// PrepaidAccountInfo prepaidAccInfo = new
			// PrepaidAccountInfoMapper().mapToSchema((PrepaidConsumerAccountInfo)
			// source);
			// target.setPrepaidAccountInfo(prepaidAccInfo);
			// }
			return super.performSchemaMapping(source, target);
		}
	}

	public static class AddressMapper extends AbstractSchemaMapper<Address, AddressInfo> {

		public AddressMapper() {
			super(Address.class, AddressInfo.class);
		}

		@Override
		protected Address performSchemaMapping(AddressInfo source, Address target) {

			target.setAddressType(toEnum(source.getAddressType(), AddressType.class));
			target.setAttention(source.getAttention());
			target.setPrimaryLine(source.getPrimaryLine());
			target.setSecondaryLine(source.getSecondaryLine());
			target.setCity(source.getCity());
			target.setCivicNo(source.getCivicNo());
			target.setCivicNoSuffix(source.getCivicNoSuffix());
			target.setCountry(source.getCountry());
			String country = target.getCountry();
			if (country != null && !country.trim().equals("") && country.trim().equals("CAN")) {
				if (source.getProvince() != null && !source.getProvince().trim().equals("")) {
					String province = source.getProvince();
					if (province.equals("QC")) {
						province = "PQ";
					}
					if (province.equals("NL")) {
						province = "NF";
					}
					target.setProvince(toEnum(province, ProvinceCode.class));
				}
			}
			target.setPostalCode(source.getPostalCode());
			target.setStreetNumber(source.getStreetNumber());
			target.setStreetNumberSuffix(source.getStreetNumberSuffix());
			target.setStreetDirection(source.getStreetDirection());
			target.setStreetName(source.getStreetName());
			target.setStreetType(source.getStreetType());
			target.setUnitType(source.getUnitType());
			target.setUnit(source.getUnit());
			target.setPoBox(source.getPoBox());
			target.setRuralLocation(source.getRuralLocation());
			target.setRuralQualifier(source.getRuralQualifier());
			target.setRuralSite(source.getRuralSite());
			target.setRuralCompartment(source.getRuralCompartment());
			target.setRuralDeliveryType(source.getRuralDeliveryType());
			target.setRuralGroup(source.getRuralGroup());
			target.setRuralNumber(source.getRuralNumber());
			target.setRuralType(source.getRuralType());
			target.setZipGeoCode(source.getZipGeoCode());
			target.setForeignState(source.getForeignState());

			return super.performSchemaMapping(source, target);
		}
	}

	public static class InvoicePropertiesMapper extends AbstractSchemaMapper<InvoicePropertyListType, InvoicePropertiesInfo> {

		public InvoicePropertiesMapper() {
			super(InvoicePropertyListType.class, InvoicePropertiesInfo.class);
		}

		@Override
		protected InvoicePropertyListType performSchemaMapping(InvoicePropertiesInfo source, InvoicePropertyListType target) {

			target.setHoldRedirectDestinationCode(source.getHoldRedirectDestinationCode());
			target.setHoldRedirectFromDate(source.getHoldRedirectFromDate());
			target.setHoldRedirectToDate(source.getHoldRedirectToDate());
			target.setInvoiceSuppressionLevel(source.getInvoiceSuppressionLevel());

			return super.performSchemaMapping(source, target);
		}
	}

	public static class ConsumerNameMapper extends AbstractSchemaMapper<ConsumerName, ConsumerNameInfo> {

		protected ConsumerNameMapper() {
			super(ConsumerName.class, ConsumerNameInfo.class);
		}

		@Override
		protected ConsumerName performSchemaMapping(ConsumerNameInfo source, ConsumerName target) {

			if (source != null) {
				target.setAdditionalLine(source.getAdditionalLine());
				target.setFirstName(source.getFirstName());
				target.setGeneration(source.getGeneration());
				target.setLastName(source.getLastName());
				target.setMiddleInitial(source.getMiddleInitial());
				target.setNameFormat(toEnum(source.getNameFormat(), NameFormat.class));
				target.setTitle(source.getTitle());
			}

			return super.performSchemaMapping(source, target);
		}
	}

	public static class PersonalCreditInformationMapper extends AbstractSchemaMapper<PersonalCreditInformation, PersonalCreditInfo> {

		public PersonalCreditInformationMapper() {
			super(PersonalCreditInformation.class, PersonalCreditInfo.class);
		}

		@Override
		protected PersonalCreditInformation performSchemaMapping(PersonalCreditInfo source, PersonalCreditInformation target) {

			if (source.getBirthDate() != null) {
				target.setBirthDate(source.getBirthDate());
			}
			target.setDriversLicense(source.getDriversLicense());
			if (source.getDriversLicenseExpiry() != null) {
				target.setDriversLicenseExpiry(source.getDriversLicenseExpiry());
			}
			if (source.getDriversLicenseProvince() != null) {
				target.setDriversLicenseProvince(ProvinceCode.fromValue(source.getDriversLicenseProvince()));
			}
			target.setSin(source.getSin());
			target.setCreditCard(new CreditCardV3Mapper().mapToSchema(source.getCreditCard0()));

			return super.performSchemaMapping(source, target);
		}
	}

	public static class CreditCardV3Mapper extends AbstractSchemaMapper<CreditCard, CreditCardInfo> {

		public CreditCardV3Mapper() {
			super(CreditCard.class, CreditCardInfo.class);
		}

		@Override
		protected CreditCard performSchemaMapping(CreditCardInfo source, CreditCard target) {

			target = null;
			if (source.getToken() != null && !source.getToken().trim().equals("")) {
				target = new CreditCard();
				target.setExpiryMonth(source.getExpiryMonth());
				target.setExpiryYear(source.getExpiryYear());
				if (source.getHolderName() != null) {
					target.setHolderName(source.getHolderName());
				} else {
					target.setHolderName("");
				}
				target.setToken(source.getToken());
				target.setFirst6(source.getLeadingDisplayDigits());
				target.setLast4(source.getTrailingDisplayDigits());
				target.setCardVerificationData(source.getCardVerificationData());
			}

			return super.performSchemaMapping(source, target);
		}
	}

	public static class CreditCardV5Mapper extends AbstractSchemaMapper<com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditCard, CreditCardInfo> {

		public CreditCardV5Mapper() {
			super(com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditCard.class, CreditCardInfo.class);
		}

		@Override
		protected com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditCard performSchemaMapping(CreditCardInfo source,
				com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditCard target) {

			target = null;
			if (source.getToken() != null && !source.getToken().trim().equals("")) {
				target = new com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditCard();
				target.setExpiryMonth(source.getExpiryMonth());
				target.setExpiryYear(source.getExpiryYear());
				if (source.getHolderName() != null) {
					target.setHolderName(source.getHolderName());
				} else {
					target.setHolderName("");
				}
				target.setToken(source.getToken());
				target.setFirst6(source.getLeadingDisplayDigits());
				target.setLast4(source.getTrailingDisplayDigits());
				target.setCardVerificationData(source.getCardVerificationData());
			}

			return super.performSchemaMapping(source, target);
		}
	}

	public static class PaymentMethodMapper extends AbstractSchemaMapper<PaymentMethod, PaymentMethodInfo> {

		public PaymentMethodMapper() {
			super(PaymentMethod.class, PaymentMethodInfo.class);
		}

		@Override
		protected PaymentMethod performSchemaMapping(PaymentMethodInfo source, PaymentMethod target) {

			Cheque cheque = new ChequeMapper().mapToSchema(source.getCheque0());
			if (cheque != null) {
				target.setCheque(cheque);
			}
			CreditCard cCard = new CreditCardV3Mapper().mapToSchema(source.getCreditCard0());
			target.setCreditCard(cCard);
			target.setEndDate(source.getEndDate());
			target.setPaymentMethod(source.getPaymentMethod());
			target.setStartDate(source.getStartDate());
			target.setStatus(source.getStatus());
			target.setStatusReason(source.getStatusReason());
			target.setSuppressReturnEnvelopeInd(source.getSuppressReturnEnvelope());

			return super.performSchemaMapping(source, target);
		}
	}

	public static class ChequeMapper extends AbstractSchemaMapper<Cheque, ChequeInfo> {

		public ChequeMapper() {
			super(Cheque.class, ChequeInfo.class);
		}

		@Override
		protected Cheque performSchemaMapping(ChequeInfo source, Cheque target) {

			target = null;
			int notNullCount = 0;
			Cheque tmpCheque = new Cheque();
			BankAccount bankAccount = new BankAccountMapper().mapToSchema(source.getBankAccount0());
			if (bankAccount != null) {
				tmpCheque.setBankAccount(bankAccount);
				notNullCount++;
			}
			if (source.getChequeNumber() != null) {
				tmpCheque.setNumber(source.getChequeNumber());
				notNullCount++;
			}
			if (notNullCount > 0) {
				target = tmpCheque;
			}

			return super.performSchemaMapping(source, target);
		}
	}

	public static class BankAccountMapper extends AbstractSchemaMapper<BankAccount, BankAccountInfo> {

		public BankAccountMapper() {
			super(BankAccount.class, BankAccountInfo.class);
		}

		@Override
		protected BankAccount performSchemaMapping(BankAccountInfo source, BankAccount target) {

			target = null;
			int notNullCount = 0;
			BankAccount tmpBankAccount = new BankAccount();
			if (source.getBankAccountHolder() != null) {
				tmpBankAccount.setBankAccountHolder(source.getBankAccountHolder());
				notNullCount++;
			}
			if (source.getBankAccountNumber() != null) {
				tmpBankAccount.setBankAccountNumber(source.getBankAccountNumber());
				notNullCount++;
			}
			if (source.getBankAccountType() != null) {
				tmpBankAccount.setBankAccountType(source.getBankAccountType());
				notNullCount++;
			}
			if (source.getBankBranchNumber() != null) {
				tmpBankAccount.setBankBranchNumber(source.getBankBranchNumber());
				notNullCount++;
			}
			if (source.getBankCode() != null) {
				tmpBankAccount.setBankCode(source.getBankCode());
				notNullCount++;
			}
			if (notNullCount > 0) {
				target = tmpBankAccount;
			}

			return super.performSchemaMapping(source, target);
		}
	}

	public static class BusinessCreditInformationMapper extends AbstractSchemaMapper<BusinessCreditInformation, BusinessCreditInfo> {

		public BusinessCreditInformationMapper() {
			super(BusinessCreditInformation.class, BusinessCreditInfo.class);
		}

		@Override
		protected BusinessCreditInformation performSchemaMapping(BusinessCreditInfo source, BusinessCreditInformation target) {

			if (source != null) {
				target.setIncorporationNumber(source.getIncorporationNumber());
				target.setIncorporationDate(source.getIncorporationDate());
			}

			return super.performSchemaMapping(source, target);
		}
	}

	public static class CreditCheckResultMapper extends AbstractSchemaMapper<CreditCheckResult, CreditCheckResultInfo> {

		public CreditCheckResultMapper() {
			super(CreditCheckResult.class, CreditCheckResultInfo.class);
		}

		@Override
		protected CreditCheckResult performSchemaMapping(CreditCheckResultInfo source, CreditCheckResult target) {

			target.setCreditClass(source.getCreditClass());
			target.setCreditScore(source.getCreditScore());
			com.telus.api.account.CreditCheckResultDeposit[] creditCheckDeposits = source.getDeposits();
			if (creditCheckDeposits != null) {
				CreditCheckResultDeposit[] deposits = new CreditCheckResultDeposit[creditCheckDeposits.length];
				for (int i = 0; i < creditCheckDeposits.length; i++) {
					deposits[i] = new CreditCheckResultDeposit();
					deposits[i].setDepositAmount(creditCheckDeposits[i].getDeposit());
					deposits[i].setProductType(creditCheckDeposits[i].getProductType());
				}
				target.getDeposits().addAll(Arrays.asList(deposits));
			}
			target.setLimit(source.getLimit());
			target.setMessage(source.getMessage());
			target.setMessageFrench(source.getMessageFrench());
			target.setReferToCreditAnalystInd(source.isReferToCreditAnalyst());

			return super.performSchemaMapping(source, target);
		}
	}

	public static class FinancialHistoryMapper extends AbstractSchemaMapper<FinancialHistory, FinancialHistoryInfo> {

		public FinancialHistoryMapper() {
			super(FinancialHistory.class, FinancialHistoryInfo.class);
		}

		@Override
		protected FinancialHistory performSchemaMapping(FinancialHistoryInfo source, FinancialHistory target) {

			target.setDebtSummary(new DebtSummaryMapper().mapToSchema(source.getDebtSummary0()));
			target.setDelinquentInd(source.isDelinquent());
			target.setLastPaymentAmount(source.getLastPaymentAmount());
			target.setLastPaymentDate(source.getLastPaymentDate());
			MonthlyFinancialActivityInfo[] monthlyFinancialActivity = source.getMonthlyFinancialActivity0();
			if (monthlyFinancialActivity != null) {
				MonthlyFinancialActivity[] monthlyFinancialActivities = new MonthlyFinancialActivity[monthlyFinancialActivity.length];
				for (int i = 0; i < monthlyFinancialActivity.length; i++) {
					monthlyFinancialActivities[i] = new MonthlyFinancialActivity();
					monthlyFinancialActivities[i].setActivity(monthlyFinancialActivity[i].getActivity());
					monthlyFinancialActivities[i].setDishonoredPaymentCount(monthlyFinancialActivity[i].getDishonoredPaymentCount());
					monthlyFinancialActivities[i].setMonth(monthlyFinancialActivity[i].getMonth());
					monthlyFinancialActivities[i].setYear(monthlyFinancialActivity[i].getYear());
				}
				target.getMonthlyFinancialActivities().addAll(Arrays.asList(monthlyFinancialActivities));
			}
			target.setWrittenOffInd(source.isWrittenOff());

			return super.performSchemaMapping(source, target);
		}
	}

	public static class DebtSummaryMapper extends AbstractSchemaMapper<DebtSummary, DebtSummaryInfo> {

		public DebtSummaryMapper() {
			super(DebtSummary.class, DebtSummaryInfo.class);
		}

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

	public static class ProductSubscriberListMapper extends AbstractSchemaMapper<ProductSubscriberList, ProductSubscriberListInfo> {

		public ProductSubscriberListMapper() {
			super(ProductSubscriberList.class, ProductSubscriberListInfo.class);
		}

		@Override
		protected ProductSubscriberList performSchemaMapping(ProductSubscriberListInfo source, ProductSubscriberList target) {

			target.setProductType(source.getProductType());
			if (source.getActiveSubscribers() != null) {
				target.getActiveSubscriber().addAll(Arrays.asList(source.getActiveSubscribers()));
			}
			if (source.getCancelledSubscribers() != null) {
				target.getCancelledSubscriber().addAll(Arrays.asList(source.getCancelledSubscribers()));
			}
			if (source.getReservedSubscribers() != null) {
				target.getReservedSubscriber().addAll(Arrays.asList(source.getReservedSubscribers()));
			}
			if (source.getSuspendedSubscribers() != null) {
				target.getSuspendedSubscriber().addAll(Arrays.asList(source.getSuspendedSubscribers()));
			}

			return super.performSchemaMapping(source, target);
		}
	}

	public static class BusinessCreditIdentityMapper extends AbstractSchemaMapper<BusinessCreditIdentity, BusinessCreditIdentityInfo> {

		private static BusinessCreditIdentityMapper INSTANCE = null;

		protected synchronized static BusinessCreditIdentityMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new BusinessCreditIdentityMapper();
			}
			return INSTANCE;
		}

		public BusinessCreditIdentityMapper() {
			super(BusinessCreditIdentity.class, BusinessCreditIdentityInfo.class);
		}

		@Override
		protected BusinessCreditIdentity performSchemaMapping(BusinessCreditIdentityInfo source, BusinessCreditIdentity target) {

			target.setCompanyName(source.getCompanyName());
			target.setMarketAccount(source.getMarketAccount());

			return super.performSchemaMapping(source, target);
		}

		@Override
		protected BusinessCreditIdentityInfo performDomainMapping(BusinessCreditIdentity source, BusinessCreditIdentityInfo target) {

			target.setCompanyName(source.getCompanyName());
			target.setMarketAccount(source.getMarketAccount());

			return super.performDomainMapping(source, target);
		}
	}

	public static class CreditCheckResponseMapper
			extends AbstractSchemaMapper<com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditCheckResult, CreditCheckResultInfo> {

		private static CreditCheckResponseMapper INSTANCE = null;

		protected synchronized static CreditCheckResponseMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditCheckResponseMapper();
			}
			return INSTANCE;
		}

		public CreditCheckResponseMapper() {
			super(com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditCheckResult.class, CreditCheckResultInfo.class);
		}

		@Override
		protected CreditCheckResultInfo performDomainMapping(com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditCheckResult source, CreditCheckResultInfo target) {

			target.setCreditClass(source.getCreditClass());
			target.setCreditScore(source.getCreditScore());
			target.setDeposits(mapCreditCheckDeposits(source.getDepositList()));
			target.setLimit(source.getCreditLimit());
			target.setMessage(source.getCreditDecision().getCreditDecisionMessage());
			target.setMessageFrench(source.getCreditDecision().getCreditDecisionMessageFrench());
			if (source.getReferToCreditAnalyst() != null) {
				target.getReferToCreditAnalyst().setReferToCreditAnalyst(source.getReferToCreditAnalyst().isReferToCreditAnalystInd());
				target.getReferToCreditAnalyst().setReasonCode(source.getReferToCreditAnalyst().getReasonCode());
				target.getReferToCreditAnalyst().setReasonMessage(source.getReferToCreditAnalyst().getReasonMessage());
			}
			if (source.getBureauFile() != null) {
				// ISO8859_1 is required to preserve French characters when doing decoding
				target.setBureauFile(new String(source.getBureauFile(), CreditBureauDocumentInfo.charsetForDecoding)); 
			}

			return super.performDomainMapping(source, target);
		}

		private CreditCheckResultDepositInfo[] mapCreditCheckDeposits(List<com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditCheckResultDeposit> deposits) {

			List<CreditCheckResultDepositInfo> creditCheckResultDepositInfoList = new ArrayList<CreditCheckResultDepositInfo>();
			if (deposits != null) {
				for (com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditCheckResultDeposit creditCheckResultDeposit : deposits) {
					CreditCheckResultDepositInfo creditCheckResultDepositInfo = new CreditCheckResultDepositInfo();
					creditCheckResultDepositInfo.setDeposit(creditCheckResultDeposit.getDepositAmount());
					creditCheckResultDepositInfo.setProductType(creditCheckResultDeposit.getProductType());
					creditCheckResultDepositInfoList.add(creditCheckResultDepositInfo);
				}
			}

			return (CreditCheckResultDepositInfo[]) creditCheckResultDepositInfoList.toArray(new CreditCheckResultDepositInfo[creditCheckResultDepositInfoList.size()]);
		}
	}

	public static class EligibilityCheckResponseMapper extends AbstractSchemaMapper<EligibilityResult, CreditCheckResultInfo> {

		private static EligibilityCheckResponseMapper INSTANCE = null;

		protected synchronized static EligibilityCheckResponseMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new EligibilityCheckResponseMapper();
			}
			return INSTANCE;
		}

		public EligibilityCheckResponseMapper() {
			super(EligibilityResult.class, CreditCheckResultInfo.class);
		}

		@Override
		protected CreditCheckResultInfo performDomainMapping(EligibilityResult source, CreditCheckResultInfo target) {

			if (source.getCreditDecision() != null) {
				target.setMessage(source.getCreditDecision().getCreditDecisionMessage());
				target.setMessageFrench(source.getCreditDecision().getCreditDecisionMessageFrench());
			}
			if (source.getReferToCreditAnalyst() != null) {
				target.setReferToCreditAnalyst(source.getReferToCreditAnalyst().isReferToCreditAnalystInd());
			}
			if (source.getActivationOptionList() != null) {
				List<ActivationOptionInfo> targetActivationOptionList = new ArrayList<ActivationOptionInfo>();
				for (com.telus.tmi.xmlschema.xsd.customer.customer.creditprofiletypes_v2.ActivationOption activationOption : source.getActivationOptionList()) {
					ActivationOptionInfo targetActivationOption = new ActivationOptionInfo();
					targetActivationOption.setOptionType(new ActivationOptionTypeInfo(activationOption.getActivationOptionName()));
					targetActivationOption.setCreditClass(activationOption.getCreditClass());
					targetActivationOption.setCreditLimit(activationOption.getCreditLimitAmount());
					targetActivationOption.setCLPPricePlanLimitAmount(activationOption.getCLPPricePlanLimitAmount());
					targetActivationOption.setMaxContractTerm(activationOption.getMaxCLPContractTerm());
					targetActivationOption.setDeposit(activationOption.getDepositAmount());
					targetActivationOptionList.add(targetActivationOption);
				}
				target.setActivationOptions(targetActivationOptionList.toArray(new ActivationOptionInfo[0]));
			}
			if (source.getServiceDeclined() != null && source.getServiceDeclined().isServiceDeclinedInd() && source.getServiceDeclined().getDeclinedReasonCodeList() != null
					&& !source.getServiceDeclined().getDeclinedReasonCodeList().isEmpty()) {
				target.setErrorCode(Integer.valueOf(source.getServiceDeclined().getDeclinedReasonCodeList().get(0)));
			} else {
				target.setErrorCode(0);
			}
			target.setCreditCheckResultStatus(target.getErrorCode() != 0 ? "E" : "D");
			if (source.getCLPActivationOptionDetails() != null) {
				CLPActivationOptionDetailInfo cLPActivationOptionDetail = new CLPActivationOptionDetailInfo();
				cLPActivationOptionDetail.setMaxNumberOfCLPSubscribers(source.getCLPActivationOptionDetails().getMaxNumberOfCLPSubscribers());
				cLPActivationOptionDetail.setMaxAdditionalCLPSubscribers(source.getCLPActivationOptionDetails().getMaxAdditionalCLPSubscribers());
				cLPActivationOptionDetail.setCurrentNumberOfSubscribers(source.getCLPActivationOptionDetails().getCurrentNumberOfSubscribers());
				cLPActivationOptionDetail.setCLPAccountInd(source.getCLPActivationOptionDetails().isClpAccountInd());
				if (source.getCLPActivationOptionDetails().getResultReasonCodeList() != null && !source.getCLPActivationOptionDetails().getResultReasonCodeList().isEmpty()) {
					cLPActivationOptionDetail.setResultReasonCodes(source.getCLPActivationOptionDetails().getResultReasonCodeList().toArray(new String[0]));
				}
				target.setCLPActivationOptionDetail(cLPActivationOptionDetail);
			}

			return super.performDomainMapping(source, target);
		}
	}

	public static class CLPActivationOptionMapper extends AbstractSchemaMapper<CLPActivationOption, HCDclpActivationOptionDetailsInfo> {

		public static CLPActivationOptionMapper INSTANCE = null;

		protected synchronized static CLPActivationOptionMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CLPActivationOptionMapper();
			}
			return INSTANCE;
		}

		public CLPActivationOptionMapper() {
			super(CLPActivationOption.class, HCDclpActivationOptionDetailsInfo.class);
		}

		@Override
		protected HCDclpActivationOptionDetailsInfo performDomainMapping(CLPActivationOption source, HCDclpActivationOptionDetailsInfo target) {
			
			target.setMaxNumberOfCLPSubscribers(source.getMaxNumberOfCLPSubscribers());
			target.setMaxAdditionalCLPSubscribers(source.getMaxAdditionalCLPSubscribers());
			target.setCurrentNumberOfSubscribers(source.getCurrentNumberOfSubscribers());
			target.setCLPAccountInd(source.isClpAccountInd());
			if (source.getResultReasonCodeList() != null && !source.getResultReasonCodeList().isEmpty()) {
				target.setResultReasonCodes(source.getResultReasonCodeList().toArray(new String[0]));
			}
			target.setCLPPricePlanLimitAmount(source.getCLPPricePlanLimitAmount());
			target.setMaxCLPContractTerm(source.getMaxCLPContractTerm());
			target.setOperationResultCd(source.getOperationResultCd());
			
			return super.performDomainMapping(source, target);
		}
	}

	public static class BusinessAccountInfoMapper extends AbstractSchemaMapper<BusinessAccountInfo, AccountInfo> {

		private static BusinessAccountInfoMapper INSTANCE = null;

		protected synchronized static BusinessAccountInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new BusinessAccountInfoMapper();
			}
			return INSTANCE;
		}

		public BusinessAccountInfoMapper() {
			super(BusinessAccountInfo.class, AccountInfo.class);
		}

		@Override
		protected BusinessAccountInfo performSchemaMapping(AccountInfo source, BusinessAccountInfo target) {

			target.setBillingAccountNum(Integer.toString(source.getBanId()));
			target.setBillingAccountTypeCd(String.valueOf(source.getAccountType()));
			target.setBillingAccountSubTypeCd(String.valueOf(source.getAccountSubType()));
			target.setBrandId(source.getBrandId());

			return super.performSchemaMapping(source, target);
		}
	}

	public static class CreditBusinessCustomerInfoMapper extends AbstractSchemaMapper<CreditBusinessCustomerInfo, PostpaidBusinessRegularAccountInfo> {

		private static CreditBusinessCustomerInfoMapper INSTANCE = null;

		protected synchronized static CreditBusinessCustomerInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditBusinessCustomerInfoMapper();
			}
			return INSTANCE;
		}

		public CreditBusinessCustomerInfoMapper() {
			super(CreditBusinessCustomerInfo.class, PostpaidBusinessRegularAccountInfo.class);
		}

		@Override
		protected CreditBusinessCustomerInfo performSchemaMapping(PostpaidBusinessRegularAccountInfo source, CreditBusinessCustomerInfo target) {

			target.setLegalBusinessName(source.getLegalBusinessName());
			target.setContactPhoneNum(source.getContactPhone());
			target.setContactPhoneExtensionNum(source.getContactPhoneExtension());
			target.setIncorporationNum(source.getCreditInformation0().getIncorporationNumber());
			target.setIncorporationNumberDate(source.getCreditInformation0().getIncorporationDate());
			target.setCreditAddress(new CreditAddressMapper().mapToSchema(source.getAddress0()));

			return super.performSchemaMapping(source, target);
		}
	}

	public static class CreditAddressMapper extends AbstractSchemaMapper<CreditAddress, AddressInfo> {

		public CreditAddressMapper() {
			super(CreditAddress.class, AddressInfo.class);
		}

		@Override
		protected CreditAddress performSchemaMapping(AddressInfo source, CreditAddress target) {

			target.setStreetNum(source.getStreetNumber());
			target.setStreetName(source.getStreetName());
			target.setStreetDirectionTxt(source.getStreetDirection());
			target.setCityName(source.getCity());
			target.setProvinceCd(StringUtils.equals(source.getProvince(), "QC") ? "PQ" : StringUtils.equals(source.getProvince(), "NL") ? "NF" : source.getProvince());
			target.setCountryName(source.getCountry());
			target.setPostalCd(source.getPostalCode());
			target.setAddressPrimaryLineTxt(source.getPrimaryLine());
			target.setAddressSecondaryLineTxt(source.getSecondaryLine());

			return super.performSchemaMapping(source, target);
		}
	}

	public static class FindAccountsByCustomerProfileMapper extends AbstractSchemaMapper<FindAccountsByCustomerProfile, AccountInfo> {

		private static FindAccountsByCustomerProfileMapper INSTANCE = null;

		protected synchronized static FindAccountsByCustomerProfileMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new FindAccountsByCustomerProfileMapper();
			}
			return INSTANCE;
		}

		public FindAccountsByCustomerProfileMapper() {
			super(FindAccountsByCustomerProfile.class, AccountInfo.class);
		}

		@Override
		protected FindAccountsByCustomerProfile performSchemaMapping(AccountInfo source, FindAccountsByCustomerProfile target) {

			target.setAccountTypeCode(toEnum(String.valueOf(source.getAccountType()), AccountTypeCode.class));
			target.setAccountSubTypeCode(String.valueOf(source.getAccountSubType()));
			target.setBrandId(source.getBrandId());
			if (source.isPostpaidConsumer() || source.isPostpaidBoxedConsumer() || source.isPostpaidBusinessPersonal() || source.isPostpaidCorporatePersonal() || source.isPostpaidEmployee()) {
				target.setLastName((((PostpaidConsumerAccountInfo) source).getName0()).getLastName());
				PersonalCreditInfo creditInfo = ((PostpaidConsumerAccountInfo) source).getPersonalCreditInformation0();
				target.setDriversLicense(creditInfo.getDriversLicense());
				target.setSin(creditInfo.getSin());
				target.setCreditCard(new CreditCardV5Mapper().mapToSchema(creditInfo.getCreditCard0()));
			} else if (source.isPostpaidBusinessDealer() || source.isPostpaidBusinessOfficial() || source.isPostpaidBusinessRegular() || source.isPostpaidCorporateRegular()) {
				target.setLastName(((PostpaidBusinessRegularAccountInfo) source).getLegalBusinessName());
				target.setIncorporationNumber(((PostpaidBusinessRegularAccountInfo) source).getCreditInformation0().getIncorporationNumber());
				PersonalCreditInfo creditInfo = ((PostpaidBusinessRegularAccountInfo) source).getPersonalCreditInformation0();
				target.setDriversLicense(creditInfo.getDriversLicense());
				target.setSin(creditInfo.getSin());
				target.setCreditCard(new CreditCardV5Mapper().mapToSchema(creditInfo.getCreditCard0()));
			}

			return super.performSchemaMapping(source, target);
		}
	}
	
	public static class MatchedAccountMapper extends AbstractSchemaMapper<MatchedAccount, MatchedAccountInfo> {

		private static MatchedAccountMapper INSTANCE = null;

		protected synchronized static MatchedAccountMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new MatchedAccountMapper();
			}
			return INSTANCE;
		}

		public MatchedAccountMapper() {
			super(MatchedAccount.class, MatchedAccountInfo.class);
		}

		@Override
		protected MatchedAccountInfo performDomainMapping(MatchedAccount source, MatchedAccountInfo target) {

			target.setAccount(AccountMapper().mapToDomain(source.getAccount()));
			target.setMatchedReasonList(new MatchedReasonMapper().mapToDomain(source.getMatchReasonList()));

			return super.performDomainMapping(source, target);
		}
	}

	public static class MatchedReasonMapper extends AbstractSchemaMapper<MatchReason, MatchedReasonInfo> {

		public MatchedReasonMapper() {
			super(MatchReason.class, MatchedReasonInfo.class);
		}

		@Override
		protected MatchedReasonInfo performDomainMapping(MatchReason source, MatchedReasonInfo target) {

			target.setMatchCriteria(source.getMatchParameter());
			target.setMatchedInd(source.isMatchIndicator());
			
			return super.performDomainMapping(source, target);
		}
	}
	
}