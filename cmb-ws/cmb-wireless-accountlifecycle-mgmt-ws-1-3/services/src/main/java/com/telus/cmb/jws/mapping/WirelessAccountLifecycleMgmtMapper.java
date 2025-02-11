package com.telus.cmb.jws.mapping;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.AccountManager;
import com.telus.cmb.jws.mapping.account_information_30.InvoicePropertyMapper;
import com.telus.cmb.jws.mapping.customer_management_common_40.AddressMapper;
import com.telus.cmb.jws.mapping.customer_management_common_40.BusinessCreditInformationMapper;
import com.telus.cmb.jws.mapping.customer_management_common_40.CreditCheckResultMapper;
import com.telus.cmb.jws.mapping.customer_management_common_40.PaymentMethodMapper;
import com.telus.cmb.jws.mapping.customer_management_common_40.PersonalCreditInformationMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PostpaidAccountCreationResponseInfo;
import com.telus.eas.account.info.PostpaidBoxedConsumerAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessDealerAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessOfficialAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessPersonalAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.account.info.PostpaidCorporatePersonalAccountInfo;
import com.telus.eas.account.info.PostpaidCorporateRegularAccountInfo;
import com.telus.eas.account.info.PostpaidEmployeeAccountInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.WesternPrepaidConsumerAccountInfo;
import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.transaction.info.ActivityResultInfo;
import com.telus.cmb.jws.AccountCreationResult;
import com.telus.cmb.jws.ActivityResultType;
import com.telus.cmb.jws.BaseWirelessAccount;
import com.telus.cmb.jws.CreatePostpaidAccountResponse;
import com.telus.cmb.jws.WarningBaseType;
import com.telus.cmb.jws.WarningType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.customer_information_reference_types_1_0.MemoType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.BusinessCreditIdentity;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.FollowUp;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.AuditInfo;

public class WirelessAccountLifecycleMgmtMapper {

	public static BaseWirelessAccountMapper BaseWirelessAccountMapper() {
		return BaseWirelessAccountMapper.getInstance();
	}

	public static BusinessCreditIdentityMapper BusinessCreditIdentityMapper() {
		return BusinessCreditIdentityMapper.getInstance();
	}

	public static AuditInfoMapper AuditInfoMapper() {
		return AuditInfoMapper.getInstance();
	}

	public static CreatePostpaidAccountResponseMapper CreatePostpaidAccountResponseMapper() {
		return CreatePostpaidAccountResponseMapper.getInstance();
	}
	
	public static FollowUpMapper FollowUpMapper() {
		return FollowUpMapper.getInstance();
	}

	public static ActivityResultMapper ActivityResultMapper() {
		return ActivityResultMapper.getInstance();
	}
	
	public static WarningTypeMapper WarningTypeMapper() {
		return WarningTypeMapper.getInstance();
	}
	
	public static WarningBaseTypeMapper WarningBaseTypeMapper() {
		return WarningBaseTypeMapper.getInstance();
	}
	
	/**
	 * BaseWirelessAccountMapper
	 * 
	 * @author Anitha Duraisamy
	 * 
	 */
	public static class BaseWirelessAccountMapper extends AbstractSchemaMapper<BaseWirelessAccount, AccountInfo> {

		private static BaseWirelessAccountMapper INSTANCE;

		private BaseWirelessAccountMapper() {
			super(BaseWirelessAccount.class, AccountInfo.class);
		}

		protected synchronized static BaseWirelessAccountMapper getInstance() {			
			if (INSTANCE == null) {
				INSTANCE = new BaseWirelessAccountMapper();
			}
			return INSTANCE;
		}

		@Override
		protected AccountInfo performDomainMapping(BaseWirelessAccount source, AccountInfo target) {

			boolean postpaid = false;
			boolean prepaid = false;

			boolean postpaidConsumer = false;
			boolean postpaidBoxedConsumer = false;

			boolean postpaidBusinessRegular = false;
			boolean postpaidBusinessDealer = false;
			boolean postpaidBusinessOfficial = false;
			boolean postpaidBusinessPersonal = false;

			boolean postpaidCorporatePersonal = false;
			boolean postpaidCorporateRegular = false;
			boolean postpaidEmployee = false;

			boolean prepaidConsumer = false;
			boolean westernPrepaidConsumer = false;

			if (source.getAccountType() != null && source.getAccountSubType() != null) {

				target.setAccountType(source.getAccountType().value().charAt(0));
				target.setAccountSubType(source.getAccountSubType().charAt(0));

				if (target.isPostpaid()) {
					postpaid = true;
					if (target.isPostpaidBoxedConsumer()) {
						target = PostpaidBoxedConsumerAccountInfo.newPagerInstance();
						postpaidBoxedConsumer = true;
					} else if (target.isPostpaidBusinessDealer()) {
						target = PostpaidBusinessDealerAccountInfo.getNewInstance0(source.getAccountSubType().charAt(0));
						postpaidBusinessDealer = true;
					} else if (target.isPostpaidBusinessOfficial()) {
						target = PostpaidBusinessOfficialAccountInfo.newPCSInstance0();
						postpaidBusinessOfficial = true;
					} else if (target.isPostpaidBusinessPersonal()) {
						target = PostpaidBusinessPersonalAccountInfo.getNewInstance0(source.getAccountSubType().charAt(0));
						postpaidBusinessPersonal = true;
					} else if (target.isPostpaidBusinessRegular()) {
						target = PostpaidBusinessRegularAccountInfo.getNewInstance(source.getAccountSubType().charAt(0));
						postpaidBusinessRegular = true;
					} else if (target.isPostpaidCorporatePersonal()) {
						target = PostpaidCorporatePersonalAccountInfo.newInstance(source.getAccountSubType().charAt(0));
						postpaidCorporatePersonal = true;
					} else if (target.isPostpaidCorporateRegular()) {
						target = PostpaidCorporateRegularAccountInfo.newInstance(source.getAccountSubType().charAt(0));
						postpaidCorporateRegular = true;
					} else if (target.isPostpaidEmployee()) {
						target = PostpaidEmployeeAccountInfo.getNewInstance0(source.getAccountSubType().charAt(0));
						postpaidEmployee = true;
					} else if (target.isPostpaidConsumer()) {
						postpaidConsumer = true;
						target = PostpaidConsumerAccountInfo.getNewInstance(source.getAccountSubType().charAt(0));
					}
				} else {
					prepaid = true;
					if (target.isWesternPrepaidConsumer()) {
						target = WesternPrepaidConsumerAccountInfo.newPCSInstance0();
						westernPrepaidConsumer = true;
					} else if (target.isPrepaidConsumer() || target.isQuebectelPrepaidConsumer()) {
						target = PrepaidConsumerAccountInfo.newPCSInstance(source.getAccountSubType().charAt(0));
						prepaidConsumer = true;
					}
				}
			}
			if (source.getAccountCategory() != null)
				target.setAccountCategory(source.getAccountCategory().value());

			target.setBrandId(source.getBrandId());
			if (source.getBillCycle() != null)
				target.setBillCycle(source.getBillCycle());

			target.setIxcCode(source.getIxcCode());

			AddressInfo address = new AddressMapper().mapToDomain(source.getAddress());
			target.setAddress0(address);

			if (source.getHomeProvince() != null)
				target.setHomeProvince(source.getHomeProvince().value());

			if (source.getOtherPhoneType() != null)
				target.setOtherPhoneType(source.getOtherPhoneType().value());

			target.setOtherPhone(source.getOtherPhone());
			target.setOtherPhoneExtension(source.getOtherPhoneExtension());
			target.setHomePhone(source.getHomePhone());
			target.setBusinessPhone(source.getBusinessPhone());
			target.setBusinessPhoneExtension(source.getBusinessPhoneExtension());
			target.setPin(source.getPin());
			target.setEmail(source.getEmail());
			if (source.getLanguage() != null)
				target.setLanguage(source.getLanguage().value());
			target.setDealerCode(source.getDealerCode());
			target.setSalesRepCode(source.getSalesRepCode());

			InvoicePropertiesInfo invProperties = InvoicePropertyMapper.getInstance().mapToDomain(source.getInvoicePropertyList());
			target.setInvoiceProperties(invProperties);
			if (source.getClientConsentIndicatorCode() != null && source.getClientConsentIndicatorCode().getItem().size() > 0)
				target.setClientConsentIndicatorCodes(source.getClientConsentIndicatorCode().getItem().toArray(new String[0]));

			if (source.isGstExemptInd() != null)
				target.setGstExempt(source.isGstExemptInd() ? (byte) 'Y' : (byte) 'N');
			if (source.isPstExemptInd() != null)
				target.setPstExempt(source.isPstExemptInd() ? (byte) 'Y' : (byte) 'N');
			if (source.isHstExemptInd() != null)
				target.setHstExempt(source.isHstExemptInd() ? (byte) 'Y' : (byte) 'N');

			if (source.getGstExemptEffDate() != null)
				target.setGSTExemptEffectiveDate(source.getGstExemptEffDate());
			if (source.getPstExemptEffDate() != null)
				target.setPSTExemptEffectiveDate(source.getPstExemptEffDate());
			if (source.getHstExemptEffDate() != null)
				target.setHSTExemptEffectiveDate(source.getHstExemptEffDate());

			if (source.getGstExemptExpDate() != null)
				target.setGSTExemptExpiryDate(source.getGstExemptExpDate());
			if (source.getPstExemptExpDate() != null)
				target.setPSTExemptExpiryDate(source.getPstExemptExpDate());
			if (source.getHstExemptExpDate() != null)
				target.setHSTExemptExpiryDate(source.getHstExemptExpDate());

			target.setGSTCertificateNumber(source.getGstExemptCertNo());

			if (postpaidConsumer) {
				if (source.getName() != null) {
					((PostpaidConsumerAccountInfo) target).getName0().setTitle(source.getName().getTitle());
					((PostpaidConsumerAccountInfo) target).getName0().setFirstName(source.getName().getFirstName());
					((PostpaidConsumerAccountInfo) target).getName0().setMiddleInitial(source.getName().getMiddleInitial());
					((PostpaidConsumerAccountInfo) target).getName0().setLastName(source.getName().getLastName());
					((PostpaidConsumerAccountInfo) target).getName0().setGeneration(source.getName().getGeneration());
					((PostpaidConsumerAccountInfo) target).getName0().setAdditionalLine(source.getName().getAdditionalLine());
					if (source.getName().getNameFormat() != null)
						((PostpaidConsumerAccountInfo) target).getName0().setNameFormat(source.getName().getNameFormat().value());
				}

				PersonalCreditInfo personalCreditInfo = new PersonalCreditInformationMapper().mapToDomain(source.getPersonalCreditInformation());
				if (personalCreditInfo != null)
					((PostpaidConsumerAccountInfo) target).getPersonalCreditInformation0().copyFrom(personalCreditInfo);

				PaymentMethodInfo paymentMethodInfo = new PaymentMethodMapper().mapToDomain(source.getPaymentMethod());
				if (paymentMethodInfo != null)
					((PostpaidConsumerAccountInfo) target).getPaymentMethod0().copyFrom(paymentMethodInfo);

			} else if (postpaidBoxedConsumer) {
				if (source.getName() != null) {
					((PostpaidBoxedConsumerAccountInfo) target).getName0().setTitle(source.getName().getTitle());
					((PostpaidBoxedConsumerAccountInfo) target).getName0().setFirstName(source.getName().getFirstName());
					((PostpaidBoxedConsumerAccountInfo) target).getName0().setMiddleInitial(source.getName().getMiddleInitial());
					((PostpaidBoxedConsumerAccountInfo) target).getName0().setLastName(source.getName().getLastName());
					((PostpaidBoxedConsumerAccountInfo) target).getName0().setGeneration(source.getName().getGeneration());
					((PostpaidBoxedConsumerAccountInfo) target).getName0().setAdditionalLine(source.getName().getAdditionalLine());
					if (source.getName().getNameFormat() != null)
						((PostpaidBoxedConsumerAccountInfo) target).getName0().setNameFormat(source.getName().getNameFormat().value());
				}
				PersonalCreditInfo personalCreditInfo = new PersonalCreditInformationMapper().mapToDomain(source.getPersonalCreditInformation());
				if (personalCreditInfo != null)
					((PostpaidBoxedConsumerAccountInfo) target).getPersonalCreditInformation0().copyFrom(personalCreditInfo);

				PaymentMethodInfo paymentMethodInfo = new PaymentMethodMapper().mapToDomain(source.getPaymentMethod());
				if (paymentMethodInfo != null)
					((PostpaidBoxedConsumerAccountInfo) target).getPaymentMethod0().copyFrom(paymentMethodInfo);

			} else if (postpaidBusinessRegular) {
				((PostpaidBusinessRegularAccountInfo) target).setLegalBusinessName(source.getLegalBusinessName());
				((PostpaidBusinessRegularAccountInfo) target).setTradeNameAttention(source.getTradeNameAttention());

				PersonalCreditInfo personalCreditInfo = new PersonalCreditInformationMapper().mapToDomain(source.getPersonalCreditInformation());
				if (personalCreditInfo != null)
					((PostpaidBusinessRegularAccountInfo) target).getPersonalCreditInformation0().copyFrom(personalCreditInfo);

				BusinessCreditInfo businessCreditInfo = new BusinessCreditInformationMapper().mapToDomain(source.getBusinessCreditInformation());
				if (businessCreditInfo != null)
					((PostpaidBusinessRegularAccountInfo) target).getCreditInformation0().copyFrom(businessCreditInfo);

				PaymentMethodInfo paymentMethodInfo = new PaymentMethodMapper().mapToDomain(source.getPaymentMethod());
				if (paymentMethodInfo != null)
					((PostpaidBusinessRegularAccountInfo) target).getPaymentMethod0().copyFrom(paymentMethodInfo);

			} else if (postpaidBusinessPersonal) {
				if (source.getName() != null) {
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setTitle(source.getName().getTitle());
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setFirstName(source.getName().getFirstName());
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setMiddleInitial(source.getName().getMiddleInitial());
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setLastName(source.getName().getLastName());
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setGeneration(source.getName().getGeneration());
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setAdditionalLine(source.getName().getAdditionalLine());
					if (source.getName().getNameFormat() != null)
						((PostpaidBusinessPersonalAccountInfo) target).getName0().setNameFormat(source.getName().getNameFormat().value());
				}
				((PostpaidBusinessPersonalAccountInfo) target).setLegalBusinessName(source.getLegalBusinessName());
				PersonalCreditInfo personalCreditInfo = new PersonalCreditInformationMapper().mapToDomain(source.getPersonalCreditInformation());

				if (personalCreditInfo != null)
					((PostpaidBusinessPersonalAccountInfo) target).getPersonalCreditInformation0().copyFrom(personalCreditInfo);

				PaymentMethodInfo paymentMethodInfo = new PaymentMethodMapper().mapToDomain(source.getPaymentMethod());
				if (paymentMethodInfo != null)
					((PostpaidBusinessPersonalAccountInfo) target).getPaymentMethod0().copyFrom(paymentMethodInfo);

			} else if (postpaidBusinessOfficial) {
				((PostpaidBusinessOfficialAccountInfo) target).setLegalBusinessName(source.getLegalBusinessName());
				((PostpaidBusinessOfficialAccountInfo) target).setTradeNameAttention(source.getTradeNameAttention());

				PersonalCreditInfo personalCreditInfo = new PersonalCreditInformationMapper().mapToDomain(source.getPersonalCreditInformation());
				if (personalCreditInfo != null)
					((PostpaidBusinessOfficialAccountInfo) target).getPersonalCreditInformation0().copyFrom(personalCreditInfo);

				BusinessCreditInfo businessCreditInfo = new BusinessCreditInformationMapper().mapToDomain(source.getBusinessCreditInformation());
				if (businessCreditInfo != null)
					((PostpaidBusinessOfficialAccountInfo) target).getCreditInformation0().copyFrom(businessCreditInfo);

				PaymentMethodInfo paymentMethodInfo = new PaymentMethodMapper().mapToDomain(source.getPaymentMethod());
				if (paymentMethodInfo != null)
					((PostpaidBusinessOfficialAccountInfo) target).getPaymentMethod0().copyFrom(paymentMethodInfo);

			} else if (postpaidBusinessDealer) {
				((PostpaidBusinessDealerAccountInfo) target).setLegalBusinessName(source.getLegalBusinessName());
				((PostpaidBusinessDealerAccountInfo) target).setTradeNameAttention(source.getTradeNameAttention());

				PersonalCreditInfo personalCreditInfo = new PersonalCreditInformationMapper().mapToDomain(source.getPersonalCreditInformation());
				if (personalCreditInfo != null)
					((PostpaidBusinessDealerAccountInfo) target).getPersonalCreditInformation0().copyFrom(personalCreditInfo);

				BusinessCreditInfo businessCreditInfo = new BusinessCreditInformationMapper().mapToDomain(source.getBusinessCreditInformation());
				if (businessCreditInfo != null)
					((PostpaidBusinessDealerAccountInfo) target).getCreditInformation0().copyFrom(businessCreditInfo);

				PaymentMethodInfo paymentMethodInfo = new PaymentMethodMapper().mapToDomain(source.getPaymentMethod());
				if (paymentMethodInfo != null)
					((PostpaidBusinessDealerAccountInfo) target).getPaymentMethod0().copyFrom(paymentMethodInfo);

			} else if (postpaidCorporatePersonal) {

				if (source.getName() != null) {
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setTitle(source.getName().getTitle());
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setFirstName(source.getName().getFirstName());
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setMiddleInitial(source.getName().getMiddleInitial());
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setLastName(source.getName().getLastName());
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setGeneration(source.getName().getGeneration());
					((PostpaidBusinessPersonalAccountInfo) target).getName0().setAdditionalLine(source.getName().getAdditionalLine());
					if (source.getName().getNameFormat() != null)
						((PostpaidBusinessPersonalAccountInfo) target).getName0().setNameFormat(source.getName().getNameFormat().value());
				}
				((PostpaidCorporatePersonalAccountInfo) target).setLegalBusinessName(source.getLegalBusinessName());

				PersonalCreditInfo personalCreditInfo = new PersonalCreditInformationMapper().mapToDomain(source.getPersonalCreditInformation());
				if (personalCreditInfo != null)
					((PostpaidBusinessPersonalAccountInfo) target).getPersonalCreditInformation0().copyFrom(personalCreditInfo);

				PaymentMethodInfo paymentMethodInfo = new PaymentMethodMapper().mapToDomain(source.getPaymentMethod());
				if (paymentMethodInfo != null)
					((PostpaidBusinessPersonalAccountInfo) target).getPaymentMethod0().copyFrom(paymentMethodInfo);

			} else if (postpaidCorporateRegular) {
				((PostpaidCorporateRegularAccountInfo) target).setLegalBusinessName(source.getLegalBusinessName());
				((PostpaidCorporateRegularAccountInfo) target).setTradeNameAttention(source.getTradeNameAttention());

				PersonalCreditInfo personalCreditInfo = new PersonalCreditInformationMapper().mapToDomain(source.getPersonalCreditInformation());
				if (personalCreditInfo != null)
					((PostpaidCorporateRegularAccountInfo) target).getPersonalCreditInformation0().copyFrom(personalCreditInfo);

				BusinessCreditInfo businessCreditInfo = new BusinessCreditInformationMapper().mapToDomain(source.getBusinessCreditInformation());
				if (businessCreditInfo != null)
					((PostpaidCorporateRegularAccountInfo) target).getCreditInformation0().copyFrom(businessCreditInfo);

				PaymentMethodInfo paymentMethodInfo = new PaymentMethodMapper().mapToDomain(source.getPaymentMethod());
				if (paymentMethodInfo != null)
					((PostpaidCorporateRegularAccountInfo) target).getPaymentMethod0().copyFrom(paymentMethodInfo);

			} else if (postpaidEmployee) {
				if (source.getName() != null) {
					((PostpaidEmployeeAccountInfo) target).getName0().setTitle(source.getName().getTitle());
					((PostpaidEmployeeAccountInfo) target).getName0().setFirstName(source.getName().getFirstName());
					((PostpaidEmployeeAccountInfo) target).getName0().setMiddleInitial(source.getName().getMiddleInitial());
					((PostpaidEmployeeAccountInfo) target).getName0().setLastName(source.getName().getLastName());
					((PostpaidEmployeeAccountInfo) target).getName0().setGeneration(source.getName().getGeneration());
					((PostpaidEmployeeAccountInfo) target).getName0().setAdditionalLine(source.getName().getAdditionalLine());
					if (source.getName().getNameFormat() != null)
						((PostpaidEmployeeAccountInfo) target).getName0().setNameFormat(source.getName().getNameFormat().value());
				}
				PersonalCreditInfo personalCreditInfo = new PersonalCreditInformationMapper().mapToDomain(source.getPersonalCreditInformation());
				if (personalCreditInfo != null)
					((PostpaidEmployeeAccountInfo) target).getPersonalCreditInformation0().copyFrom(personalCreditInfo);

				PaymentMethodInfo paymentMethodInfo = new PaymentMethodMapper().mapToDomain(source.getPaymentMethod());
				if (paymentMethodInfo != null)
					((PostpaidEmployeeAccountInfo) target).getPaymentMethod0().copyFrom(paymentMethodInfo);
			} else if (westernPrepaidConsumer) {

				if (source.getName() != null) {
					((WesternPrepaidConsumerAccountInfo) target).getName0().setTitle(source.getName().getTitle());
					((WesternPrepaidConsumerAccountInfo) target).getName0().setFirstName(source.getName().getFirstName());
					((WesternPrepaidConsumerAccountInfo) target).getName0().setMiddleInitial(source.getName().getMiddleInitial());
					((WesternPrepaidConsumerAccountInfo) target).getName0().setLastName(source.getName().getLastName());
					((WesternPrepaidConsumerAccountInfo) target).getName0().setGeneration(source.getName().getGeneration());
					((WesternPrepaidConsumerAccountInfo) target).getName0().setAdditionalLine(source.getName().getAdditionalLine());
					if (source.getName().getNameFormat() != null)
						((WesternPrepaidConsumerAccountInfo) target).getName0().setNameFormat(source.getName().getNameFormat().value());
				}
			} else if (prepaidConsumer) {

				if (source.getName() != null) {
					((PrepaidConsumerAccountInfo) target).getName0().setTitle(source.getName().getTitle());
					((PrepaidConsumerAccountInfo) target).getName0().setFirstName(source.getName().getFirstName());
					((PrepaidConsumerAccountInfo) target).getName0().setMiddleInitial(source.getName().getMiddleInitial());
					((PrepaidConsumerAccountInfo) target).getName0().setLastName(source.getName().getLastName());
					((PrepaidConsumerAccountInfo) target).getName0().setGeneration(source.getName().getGeneration());
					((PrepaidConsumerAccountInfo) target).getName0().setAdditionalLine(source.getName().getAdditionalLine());
					if (source.getName().getNameFormat() != null)
						((PrepaidConsumerAccountInfo) target).getName0().setNameFormat(source.getName().getNameFormat().value());
				}
			}

			if (source.getContactName() != null) {
				target.getContactName0().setTitle(source.getContactName().getTitle());
				target.getContactName0().setFirstName(source.getContactName().getFirstName());
				target.getContactName0().setMiddleInitial(source.getContactName().getMiddleInitial());
				target.getContactName0().setLastName(source.getContactName().getLastName());
				target.getContactName0().setGeneration(source.getContactName().getGeneration());
				target.getContactName0().setAdditionalLine(source.getContactName().getAdditionalLine());
				if (source.getContactName().getNameFormat() != null)
					target.getContactName0().setNameFormat(source.getContactName().getNameFormat().value());

			}
			target.setContactPhone(source.getContactPhone());
			target.setContactPhoneExtension(source.getContactExtension());

			if (postpaid) {
				if (target.isPCS()) {
					target.setEvaluationProductType(AccountManager.PRODUCT_TYPE_PCS);
				} else if (target.isIDEN()) {
					target.setEvaluationProductType(AccountManager.PRODUCT_TYPE_IDEN);
				} else if (target.isPager()) {
					target.setEvaluationProductType(AccountManager.PRODUCT_TYPE_PAGER);
				}
			} else if (prepaid) {
				target.setEvaluationProductType(AccountManager.PRODUCT_TYPE_PCS);
			}

			if (source.getBillingNameFormat() != null)
				target.setBillingNameFormat(source.getBillingNameFormat().value().charAt(0));
			target.setBanSegment(source.getBanSegment());
			target.setBanSubSegment(source.getBanSubSegment());
			if (source.getNoOfInvoice() != null)
				target.setNoOfInvoice(source.getNoOfInvoice().intValue());
			target.setCreateDate(new java.util.Date());

			return super.performDomainMapping(source, target);
		}
		
	}

	public static class BusinessCreditIdentityMapper extends AbstractSchemaMapper<BusinessCreditIdentity, BusinessCreditIdentityInfo> {

		private static BusinessCreditIdentityMapper INSTANCE;

		private BusinessCreditIdentityMapper() {
			super(BusinessCreditIdentity.class, BusinessCreditIdentityInfo.class);
		}

		protected synchronized static BusinessCreditIdentityMapper getInstance() {			
			if (INSTANCE == null) {
				INSTANCE = new BusinessCreditIdentityMapper();
			}
			return INSTANCE;
		}

		@Override
		protected BusinessCreditIdentityInfo performDomainMapping(BusinessCreditIdentity source, BusinessCreditIdentityInfo target) {
			
			target.setCompanyName(source.getCompanyName());
			target.setMarketAccount(source.getMarketAccount());
			
			return super.performDomainMapping(source, target);
		}
		
	}

	public static class AuditInfoMapper extends AbstractSchemaMapper<AuditInfo, com.telus.eas.transaction.info.AuditInfo> {

		private static AuditInfoMapper INSTANCE;

		private AuditInfoMapper() {
			super(AuditInfo.class, com.telus.eas.transaction.info.AuditInfo.class);
		}

		protected synchronized static AuditInfoMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new AuditInfoMapper();
			}
			return INSTANCE;
		}

		@Override
		protected com.telus.eas.transaction.info.AuditInfo performDomainMapping(AuditInfo source, com.telus.eas.transaction.info.AuditInfo target) {
			
			target.setUserId(source.getUserId());
			target.setSalesRepId(source.getSalesRepresentativeId());
			target.setChannelOrgId(source.getChannelOrganizationId());
			target.setOutletId(source.getOutletId());
			target.setOriginatorAppId(source.getOriginatorApplicationId());
			target.setCorrelationId(source.getCorrelationId());
			target.setUserTypeCode(source.getUserTypeCode());
			target.setTimestamp(source.getTimestamp());
			
			return super.performDomainMapping(source, target);
		}
		
	}

	public static class CreatePostpaidAccountResponseMapper extends AbstractSchemaMapper<CreatePostpaidAccountResponse, PostpaidAccountCreationResponseInfo> {

		private static CreatePostpaidAccountResponseMapper INSTANCE;

		private CreatePostpaidAccountResponseMapper() {
			super(CreatePostpaidAccountResponse.class, PostpaidAccountCreationResponseInfo.class);
		}

		protected synchronized static CreatePostpaidAccountResponseMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new CreatePostpaidAccountResponseMapper();
			}
			return INSTANCE;
		}

		@Override
		protected CreatePostpaidAccountResponse performSchemaMapping(PostpaidAccountCreationResponseInfo source, CreatePostpaidAccountResponse target) {
			target.setAccountCreationResult(new AccountCreationResultMapper().mapToSchema(source));
			return super.performSchemaMapping(source, target);
		}
		
	}

	public static class AccountCreationResultMapper extends AbstractSchemaMapper<AccountCreationResult, PostpaidAccountCreationResponseInfo> {

		private AccountCreationResultMapper() {
			super(AccountCreationResult.class, PostpaidAccountCreationResponseInfo.class);
		}

		@Override
		protected AccountCreationResult performSchemaMapping(PostpaidAccountCreationResponseInfo source, AccountCreationResult target) {
			target.setBillingAccountNumber(String.valueOf(source.getBan()));
			target.setCreditCheckResult(new CreditCheckResultMapper().mapToSchema(source.getCreditCheckResult0()));
			return super.performSchemaMapping(source, target);
		}
		
	}
	
	public static class FollowUpMapper extends AbstractSchemaMapper<FollowUp, FollowUpInfo> {
		
		private static FollowUpMapper INSTANCE;
		
		public FollowUpMapper() {
			super(FollowUp.class, FollowUpInfo.class);
		}

		protected static synchronized FollowUpMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new FollowUpMapper();
			}
			return INSTANCE;
		}

		@Override
		protected FollowUp performSchemaMapping(FollowUpInfo source, FollowUp target) {
			
			target.setFollowUpId(source.getFollowUpId());
			target.setBan(String.valueOf(source.getBanId()));
			target.setCloseReasonCode(source.getCloseReasonCode());
			target.setStatus(String.valueOf(source.getStatus()));
			target.setAssignedToWorkPositionId(source.getAssignedToWorkPositionId());
			target.setDueDate(source.getDueDate());
			target.setText(source.getText());
			// Note: re-using MemoType as the FollowUp type definition in the CustomerManagementCommonTypes schema seems to be a mistake. This needs to be fixed in the next version of the schema.
			// As a temporary workaround, create a MemoType object and map the FollowUpInfo type code to it.
			MemoType memoType = new MemoType();
			memoType.setCode(source.getType());
			target.setType(memoType);
			
			return super.performSchemaMapping(source, target);
		}

	}
	
	public static class ActivityResultMapper extends AbstractSchemaMapper<ActivityResultType, ActivityResultInfo> {
		
		private static ActivityResultMapper INSTANCE;
		
		public ActivityResultMapper() {
			super(ActivityResultType.class, ActivityResultInfo.class);
		}

		protected static synchronized ActivityResultMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new ActivityResultMapper();
			}
			return INSTANCE;
		}

		@Override
		protected ActivityResultType performSchemaMapping(ActivityResultInfo source, ActivityResultType target) {
			
			target.setActivityNumber(source.getActivity().getValue());
			target.setActivityWarning(WarningTypeMapper().mapToSchema(convertToWarningFaultInfo(source.getThrowable())));
			
			return super.performSchemaMapping(source, target);
		}

	}
	
	public static class WarningTypeMapper extends AbstractSchemaMapper<WarningType, WarningFaultInfo> {
		
		private static WarningTypeMapper INSTANCE;
		
		protected WarningTypeMapper() {
			super(WarningType.class, WarningFaultInfo.class);
		}
		
		protected synchronized static WarningTypeMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new WarningTypeMapper();
			}
			return INSTANCE;
		}

		@Override
		protected WarningType performSchemaMapping(WarningFaultInfo source, WarningType target) {
			target.setSystemCode(source.getSystemCode());
			target.setWarningType(source.getWarningType());
			target.setWarning(WarningBaseTypeMapper().mapToSchema(source));
			return super.performSchemaMapping(source, target);
		}
	
	}
		
	public static class WarningBaseTypeMapper extends AbstractSchemaMapper<WarningBaseType, WarningFaultInfo> {
		
		private static WarningBaseTypeMapper INSTANCE;
		
		protected WarningBaseTypeMapper() {
			super (WarningBaseType.class, WarningFaultInfo.class);
		}
		
		protected synchronized static WarningBaseTypeMapper getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new WarningBaseTypeMapper();
			}
			return INSTANCE;
		}

		@Override
		protected WarningBaseType performSchemaMapping(WarningFaultInfo source, WarningBaseType target) {
			target.setMessageId(source.getMessageId());
			target.setWarningCode(source.getErrorCode());
			target.setWarningMessage(source.getErrorMessage());
			return super.performSchemaMapping(source, target);
		}
		
	}
	
	private static WarningFaultInfo convertToWarningFaultInfo(Throwable t) {

		if (t instanceof TelusAPIException) {
			TelusAPIException tapie = (TelusAPIException) t;
			WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.APPLICATION_EXCEPTION, SystemCodes.CMB_ALF_EJB, null, null, tapie.getStackTrace0(), null);
			return warning;
		} else if (t instanceof ApplicationException) {
			ApplicationException ae = (ApplicationException) t;
			WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.APPLICATION_EXCEPTION, SystemCodes.CMB_ALF_EJB, null, ae.getErrorCode(), ae.getStackTraceAsString(), null);
			return warning;
		} else if (t instanceof SystemException) {
			SystemException se = (SystemException) t;
			WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.SYSTEM_EXCEPTION, SystemCodes.CMB_ALF_EJB, null, se.getErrorCode(), se.getStackTraceAsString(), null);
			return warning;
		} else {
			WarningFaultInfo warning = new WarningFaultInfo("Throwable", SystemCodes.CMB_ALF_EJB, null, null, t.toString(), null);
			return warning;
		}
	}
	
}
