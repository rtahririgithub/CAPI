package com.telus.cmb.account.mapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.cmb.common.mapping.EnterpriseCommonTypesV9Mapper;
import com.telus.eas.account.credit.info.CreditAssessmentInfo;
import com.telus.eas.account.credit.info.CreditProgramSubscriberEligibilityInfo;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.BusinessRegistrationInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PostpaidBusinessPersonalAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditassessmentproxysvcrequestresponse_v2.AccountCreditCheckAssessmentResult;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditassessmentproxysvcrequestresponse_v2.BaseCreditAssessmentResultAbstract;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditassessmentproxysvcrequestresponse_v2.CreditProfileInfoChanged;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditassessmentproxysvcrequestresponse_v2.CreditProgramSubscriberEligibilityResult;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditassessmentproxysvcrequestresponse_v2.EligibilityCreditAssessmentResult;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditassessmentproxysvcrequestresponse_v2.NewAccount;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditassessmentproxysvcrequestresponse_v2.OverrideCreditWorthinessResult;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.BusinessRegistration;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.CreditAccountInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.CreditAddress;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.CreditIdentification;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.CreditProfile;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.DriverLicense;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.PersonName;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.TelephoneContact;

/**
 * WirelessCreditAssessmentProxyServiceMapper
 * 
 * @author R. Fong
 *
 */
public class WirelessCreditAssessmentProxyServiceMapper {

	public static CreditAssessmentResultMapper CreditAssessmentResultMapper() {
		return CreditAssessmentResultMapper.getInstance();
	}

	public static CreditProgramSubscriberEligibilityMapper CreditProgramSubscriberEligibilityMapper() {
		return CreditProgramSubscriberEligibilityMapper.getInstance();
	}

	public static NewAccountMapper NewAccountMapper() {
		return NewAccountMapper.getInstance();
	}

	public static CreditProfileChangeMapper CreditProfileChangeMapper() {
		return CreditProfileChangeMapper.getInstance();
	}
	
	public static class CreditAssessmentResultMapper extends AbstractSchemaMapper<BaseCreditAssessmentResultAbstract, CreditAssessmentInfo> {

		private static CreditAssessmentResultMapper INSTANCE = null;

		private CreditAssessmentResultMapper() {
			super(BaseCreditAssessmentResultAbstract.class, CreditAssessmentInfo.class);
		}

		protected synchronized static CreditAssessmentResultMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditAssessmentResultMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CreditAssessmentInfo performDomainMapping(BaseCreditAssessmentResultAbstract source, CreditAssessmentInfo target) {

			// Base attributes
			target.setCreditAssessmentID(source.getCreditAssessmentId() != null ? source.getCreditAssessmentId() : 0L);
			target.setCreditAssessmentResultCode(source.getCreditAssessmentResultCd());
			target.setCreditAssessmentResultReasonCode(source.getCreditAssessmentResultReasonCd());
			target.setCreditAssessmentTypeCode(source.getCreditAssessmentTypeCd());
			target.setCreditAssessmentSubTypeCode(source.getCreditAssessmentSubTypeCd());
			if (source.getAsessmentMessage() != null) {
				target.setAssessmentMessageList(EnterpriseCommonTypesV9Mapper.DescriptionMapper().mapToDomain(source.getAsessmentMessage().getDescription()));
			}
			target.setCreditAssessmentDate(source.getCreditAssessmentTimestamp());

			// Extended attributes
			if (source instanceof AccountCreditCheckAssessmentResult) {
				mapAccountCreditCheckAssessmentResult((AccountCreditCheckAssessmentResult) source, target);
			} else if (source instanceof OverrideCreditWorthinessResult) {
				mapOverrideCreditWorthinessResult((OverrideCreditWorthinessResult) source, target);
			} else if (source instanceof EligibilityCreditAssessmentResult) {
				mapEligibilityCreditAssessmentResult((EligibilityCreditAssessmentResult) source, target);
			}

			return super.performDomainMapping(source, target);
		}

		private void mapAccountCreditCheckAssessmentResult(AccountCreditCheckAssessmentResult source, CreditAssessmentInfo target) {

			target.setCreditAssessmentType(CreditAssessmentInfo.TYPE_ACCOUNT_CREDIT_CHECK);
			if (source.getCreditWorthiness() != null) {
				target.setCreditWorthiness(WirelessCreditTypesMapper.CreditWorthinessMapper().mapToDomain(source.getCreditWorthiness()));
			}
			if (source.getCreditBureauReportDocument() != null) {
				target.setBureauReportDocument(WirelessCreditTypesMapper.CreditBureauDocumentMapper().mapToDomain(source.getCreditBureauReportDocument().getBureauReportDocument()));
				target.setPrintImageDocument(WirelessCreditTypesMapper.CreditBureauDocumentMapper().mapToDomain(source.getCreditBureauReportDocument().getPrintImageDocument()));
			}
			target.setCreditWarningList(WirelessCreditTypesMapper.CreditWarningMapper().mapToDomain(source.getWarningList()));
		}

		private void mapOverrideCreditWorthinessResult(OverrideCreditWorthinessResult source, CreditAssessmentInfo target) {

			target.setCreditAssessmentType(CreditAssessmentInfo.TYPE_OVERRIDE_CREDIT_WORTHINESS);
			if (source.getCreditWorthiness() != null) {
				target.setCreditWorthiness(WirelessCreditTypesMapper.CreditWorthinessMapper().mapToDomain(source.getCreditWorthiness()));
			}
		}

		private void mapEligibilityCreditAssessmentResult(EligibilityCreditAssessmentResult source, CreditAssessmentInfo target) {
			
			target.setCreditAssessmentType(CreditAssessmentInfo.TYPE_ELIGIBILITY_CREDIT_CHECK);
			target.setCreditProgramSubscriberEligibilityList(CreditProgramSubscriberEligibilityMapper().mapToDomain(source.getCreditProgramSubscriberEligibilityResultList()));
			target.setCreditWarningList(WirelessCreditTypesMapper.CreditWarningMapper().mapToDomain(source.getWarningList()));
		}
	}

	public static class CreditProgramSubscriberEligibilityMapper extends AbstractSchemaMapper<CreditProgramSubscriberEligibilityResult, CreditProgramSubscriberEligibilityInfo> {

		private static CreditProgramSubscriberEligibilityMapper INSTANCE = null;

		private CreditProgramSubscriberEligibilityMapper() {
			super(CreditProgramSubscriberEligibilityResult.class, CreditProgramSubscriberEligibilityInfo.class);
		}

		protected synchronized static CreditProgramSubscriberEligibilityMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditProgramSubscriberEligibilityMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CreditProgramSubscriberEligibilityInfo performDomainMapping(CreditProgramSubscriberEligibilityResult source, CreditProgramSubscriberEligibilityInfo target) {

			target.setCreditProgramEligibilityInd(source.getCreditProgramEligibility().isEligibilityInd() != null ? source.getCreditProgramEligibility().isEligibilityInd() : false);
			target.setCreditProgramEligibilityReasonList(source.getCreditProgramEligibility().getReasonCodeList());
			target.setCreditProgram(WirelessCreditTypesMapper.CreditProgramMapper().mapToDomain(source.getCreditProgram()));
			target.setSubscriberEligibilityList(WirelessCreditTypesMapper.SubscriberEligibilityMapper().mapToDomain(source.getRequestedSubscribersEligibilityResultList()));
			target.setDevicePaymentPlanThresholdList(WirelessCreditTypesMapper.DevicePaymentThresholdMapper().mapToDomain(source.getDevicePaymentPlanThresholdList()));
			target.setMaxNumberOfSubscribers(source.getHardStopMaxNumber() != null ? source.getHardStopMaxNumber() : 0);
			target.setReferToCreditMaxNumberOfSubscribers(source.getRefcMaxNumber() != null ? source.getRefcMaxNumber() : 0);

			return super.performDomainMapping(source, target);
		}
	}
	
	public static class NewAccountMapper extends AbstractSchemaMapper<NewAccount, AccountInfo> {

		private static NewAccountMapper INSTANCE = null;

		private NewAccountMapper() {
			super(NewAccount.class, AccountInfo.class);
		}

		protected synchronized static NewAccountMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new NewAccountMapper();
			}
			return INSTANCE;
		}

		@Override
		protected NewAccount performSchemaMapping(AccountInfo source, NewAccount target) {

			target.setCreditAccountInfo(new CreditAccountInfoMapper().mapToSchema(source));
			target.setCreditProfileInfo(new CreditProfileMapper().mapToSchema(source));

			return super.performSchemaMapping(source, target);
		}
	}

	public static class CreditAccountInfoMapper extends AbstractSchemaMapper<CreditAccountInfo, AccountInfo> {

		private static CreditAccountInfoMapper INSTANCE = null;

		private CreditAccountInfoMapper() {
			super(CreditAccountInfo.class, AccountInfo.class);
		}

		protected synchronized static CreditAccountInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditAccountInfoMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CreditAccountInfo performSchemaMapping(AccountInfo source, CreditAccountInfo target) {

			target.setBillingAccountNumber(source.getBanId());
			target.setAccountTypeCd(String.valueOf(source.getAccountType()));
			target.setAccountSubTypeCd(String.valueOf(source.getAccountSubType()));
			target.setBrandId(String.valueOf(source.getBrandId()));
			target.setAccountCreationDate(source.getCreateDate());
			target.setStartServiceDate(source.getStartServiceDate());
			target.setAccountStatusCd(String.valueOf(source.getStatus()));
			target.setAccountStatusDate(source.getStatusDate());
			ConsumerNameInfo consumerNameInfo = (source.isPostpaidConsumer() || source.isPostpaidBusinessPersonal() || source.isPostpaidCorporatePersonal())
					? ((PostpaidConsumerAccountInfo) source).getName0() : null;
			target.setPersonName(new PersonNameMapper().mapToSchema(consumerNameInfo));			
			mapTelephoneContactList(source, target);
			target.setLanguageTxt(source.getLanguage());
			//target.setRevenueBandCd(null);

			return super.performSchemaMapping(source, target);
		}
		
		private void mapTelephoneContactList(AccountInfo source, CreditAccountInfo target) {
			
			List<TelephoneContact> list = new ArrayList<TelephoneContact>();
			if (StringUtils.isNotBlank(source.getHomePhone())) {
				TelephoneContact contact = new TelephoneContact();
				contact.setTelephoneNum(source.getHomePhone());
				//contact.setTelephoneContactTypeCd(null);
				contact.setTelephoneContactSubTypeCd("HOPH");
				list.add(contact);
			}
			if (StringUtils.isNotBlank(source.getBusinessPhone())) {
				TelephoneContact contact = new TelephoneContact();
				contact.setTelephoneNum(source.getBusinessPhone());
				contact.setTelephoneExtensionNum(source.getBusinessPhoneExtension());
				//contact.setTelephoneContactTypeCd(null);
				contact.setTelephoneContactSubTypeCd("WKPH");
				list.add(contact);
			}
			if (StringUtils.isNotBlank(source.getContactPhone())) {
				TelephoneContact contact = new TelephoneContact();
				contact.setTelephoneNum(source.getContactPhone());
				contact.setTelephoneExtensionNum(source.getContactPhoneExtension());
				//contact.setTelephoneContactTypeCd(null);
				contact.setTelephoneContactSubTypeCd("DYPH");
				list.add(contact);
			}
			if (StringUtils.isNotBlank(source.getOtherPhone())) {
				TelephoneContact contact = new TelephoneContact();
				contact.setTelephoneNum(source.getOtherPhone());
				contact.setTelephoneExtensionNum(source.getOtherPhoneExtension());
				//contact.setTelephoneContactTypeCd(null);
				contact.setTelephoneContactSubTypeCd("WKPH");
				list.add(contact);
			}
			
			target.setTelephoneContactList(list);
		}
	}
	
	public static class PersonNameMapper extends AbstractSchemaMapper<PersonName, ConsumerNameInfo> {

		public PersonNameMapper() {
			super(PersonName.class, ConsumerNameInfo.class);
		}

		@Override
		protected PersonName performSchemaMapping(ConsumerNameInfo source, PersonName target) {

			target.setFirstName(source.getFirstName());
			target.setMiddleInitialTxt(source.getMiddleInitial());
			target.setLastName(source.getLastName());
			target.setFullName(source.getFullName());

			return super.performSchemaMapping(source, target);
		}		
	}
	
	public static class CreditProfileMapper extends AbstractSchemaMapper<CreditProfile, AccountInfo> {

		private static CreditProfileMapper INSTANCE = null;

		private CreditProfileMapper() {
			super(CreditProfile.class, AccountInfo.class);
		}

		protected synchronized static CreditProfileMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditProfileMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CreditProfile performSchemaMapping(AccountInfo source, CreditProfile target) {

			//target.setCreditProfileId(null);
			Date birthDate = (source.isPostpaidConsumer() || source.isPostpaidBusinessPersonal() || source.isPostpaidCorporatePersonal())
					? ((PostpaidConsumerAccountInfo) source).getPersonalCreditInformation0().getBirthDate()
					: source.isPostpaidBusinessRegular() ? ((PostpaidBusinessRegularAccountInfo) source).getPersonalCreditInformation0().getBirthDate()
							: source.isPrepaidConsumer() ? ((PrepaidConsumerAccountInfo) source).getBirthDate() : null;
			target.setBirthDate(birthDate);
			PersonalCreditInfo personalCreditInfo = (source.isPostpaidConsumer() || source.isPostpaidBusinessPersonal() || source.isPostpaidCorporatePersonal())
					? ((PostpaidConsumerAccountInfo) source).getPersonalCreditInformation0()
					: source.isPostpaidBusinessRegular() ? ((PostpaidBusinessRegularAccountInfo) source).getPersonalCreditInformation0() : null;
			target.setCreditIdentification(new CreditIdentificationMapper().mapToSchema(personalCreditInfo));
			target.setCreditAddress(new CreditAddressMapper().mapToSchema(source.getAddress0()));
			
			// Set Business Registration Info
			if (source.isPostpaidBusinessPersonal()) {
				BusinessRegistrationInfo businessRegistrationInfo = ((PostpaidBusinessPersonalAccountInfo) source).getBusinessRegistration();	
				if (businessRegistrationInfo != null) {
					BusinessRegistration businessRegistration = new BusinessRegistration();
					businessRegistration.setBusinessRegistrationNumber(businessRegistrationInfo.getBusinessRegistrationNumber());
					businessRegistration.setBusinessRegistrationTypeCd(businessRegistrationInfo.getBusinessRegistrationType());
					target.getCreditIdentification().setBusinessRegistration(businessRegistration);
				}
			}			

			return super.performSchemaMapping(source, target);
		}		
	}
	
	public static class CreditProfileChangeMapper extends AbstractSchemaMapper<CreditProfileInfoChanged, AccountInfo> {

		private static CreditProfileChangeMapper INSTANCE = null;

		private CreditProfileChangeMapper() {
			super(CreditProfileInfoChanged.class, AccountInfo.class);
		}

		protected synchronized static CreditProfileChangeMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreditProfileChangeMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CreditProfileInfoChanged performSchemaMapping(AccountInfo source, CreditProfileInfoChanged target) {

			Date birthDate = (source.isPostpaidConsumer() || source.isPostpaidBusinessPersonal() || source.isPostpaidCorporatePersonal())
					? ((PostpaidConsumerAccountInfo) source).getPersonalCreditInformation0().getBirthDate()
					: source.isPostpaidBusinessRegular() ? ((PostpaidBusinessRegularAccountInfo) source).getPersonalCreditInformation0().getBirthDate()
							: source.isPrepaidConsumer() ? ((PrepaidConsumerAccountInfo) source).getBirthDate() : null;
			target.setBirthDate(birthDate);
			PersonalCreditInfo personalCreditInfo = (source.isPostpaidConsumer() || source.isPostpaidBusinessPersonal() || source.isPostpaidCorporatePersonal())
					? ((PostpaidConsumerAccountInfo) source).getPersonalCreditInformation0()
					: source.isPostpaidBusinessRegular() ? ((PostpaidBusinessRegularAccountInfo) source).getPersonalCreditInformation0() : null;
			target.setCreditIdentification(new CreditIdentificationMapper().mapToSchema(personalCreditInfo));
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

			// If the array length of source.getFullAddress() > 3, that means there's at least a primary or secondary address line in the array.
			// If the array length of source.getFullAddress() > 4, that means there's both a primary and secondary address line in the array.
			target.setAddressLineOneText(source.getFullAddress().length > 3 ? source.getFullAddress()[0] : null);
			target.setAddressLineTwoText(source.getFullAddress().length > 4 ? source.getFullAddress()[1] : null);
			//target.setAddressLineThreeText(null);
			//target.setAddressLineFourText(null);
			//target.setAddressLineFiveText(null);
			target.setCityName(source.getCity());
			target.setProvinceCd(StringUtils.equals(source.getProvince(), "QC") ? "PQ" : StringUtils.equals(source.getProvince(), "NL") ? "NF" : source.getProvince());
			target.setPostalCd(source.getPostalCode());
			target.setCountryCd(source.getCountry());

			return super.performSchemaMapping(source, target);
		}
	}
	
	public static class CreditIdentificationMapper extends AbstractSchemaMapper<CreditIdentification, PersonalCreditInfo> {

		public CreditIdentificationMapper() {
			super(CreditIdentification.class, PersonalCreditInfo.class);
		}

		@Override
		protected CreditIdentification performSchemaMapping(PersonalCreditInfo source, CreditIdentification target) {

			DriverLicense license = new DriverLicense();
			license.setDriverLicenseNumber(source.getDriversLicense());
			license.setProvinceCd(source.getDriversLicenseProvince());
			license.setExpiryDate(source.getDriversLicenseExpiry());
			target.setDriverLicense(license);
			target.setSocialInsuranceNum(source.getSin());
			if (source.getCreditCard0() != null) {
				target.setCreditCardFirstSixNum(source.getCreditCard0().getLeadingDisplayDigits());
				target.setCreditCardLastFourNum(source.getCreditCard0().getTrailingDisplayDigits());
				target.setCreditCardTokenTxt(source.getCreditCard0().getToken());
			}

			return super.performSchemaMapping(source, target);
		}
	}

}