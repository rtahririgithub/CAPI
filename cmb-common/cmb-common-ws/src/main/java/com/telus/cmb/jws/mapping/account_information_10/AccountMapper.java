/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.account_information_10;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.jws.mapping.customer_management_common_10.AddressMapper;
import com.telus.cmb.jws.mapping.customer_management_common_10.BusinessCreditInformationMapper;
import com.telus.cmb.jws.mapping.customer_management_common_10.ConsumerNameMapper;
import com.telus.cmb.jws.mapping.customer_management_common_10.CreditCardMapper;
import com.telus.cmb.jws.mapping.customer_management_common_10.CreditCheckResultMapper;
import com.telus.cmb.jws.mapping.customer_management_common_10.FinancialHistoryMapper;
import com.telus.cmb.jws.mapping.customer_management_common_10.PaymentMethodMapper;
import com.telus.cmb.jws.mapping.customer_management_common_10.PersonalCreditInformationMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PostpaidBusinessDealerAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessOfficialAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessPersonalAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.account_information_types_1.Account;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.account_information_types_1.AccountStatus;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.account_information_types_1.BanCategory;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.account_information_types_1.PrepaidAccountInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.account_information_types_1.ProductSubscriberList;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.Language;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.NameFormat;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.OtherPhoneType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.PersonalCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.AccountTypeCode;

/**
 * @author Dimitry Siganevich
 * 
 */
public class AccountMapper extends AbstractSchemaMapper<Account, AccountInfo> {

	public static final String PRODUCT_TYPE_PCS = "C";
	
	public char accountType='\u0000';
	public char accountSubType='\u0000';	
	
	public AccountInfo accountInfo;

	public AccountMapper() {
		super(Account.class, AccountInfo.class);
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
		target.setAddress(new AddressMapper().mapToSchema(source.getAddress0()));
		target.setAlternateCreditCheckAddress(new AddressMapper().mapToSchema(source.getAlternateCreditCheckAddress0()));
		target.setBan(Integer.toString(source.getBanId()));
		target.setBanSegment(source.getBanSegment());
		target.setBanSubSegment(source.getBanSubSegment());
		target.setBillCycle(source.getBillCycle());
		target.setBillCycleCloseDay(source.getBillCycleCloseDay());
		target.setBillingNameFormat(toEnum(String.valueOf(source.getBillingNameFormat()), NameFormat.class));
		if (source.isPostpaidConsumer())
			target.setBirthDate(((PostpaidConsumerAccountInfo) source).getPersonalCreditInformation0().getBirthDate());
		else if (source.isPostpaidBusinessRegular())
			target.setBirthDate(((PostpaidBusinessRegularAccountInfo) source).getPersonalCreditInformation0().getBirthDate());
		else if (source.isPrepaidConsumer())
			target.setBirthDate(((PrepaidConsumerAccountInfo) source).getBirthDate());
		target.setBrandId(source.getBrandId());
		if (source.isPostpaidBusinessRegular()) {
			target.setBusinessCreditInformation(new BusinessCreditInformationMapper().mapToSchema(((PostpaidBusinessRegularAccountInfo) source).getCreditInformation0()));
		}
		target.setBusinessPhone(source.getBusinessPhone());
		target.setBusinessPhoneExtension(source.getBusinessPhoneExtension());
		target.getClientConsentIndicatorCode().addAll(toCollection(source.getClientConsentIndicatorCodes()));
		target.setContactExtension(source.getContactPhoneExtension());
		target.setContactFax(source.getContactFax());
		target.setContactName(new ConsumerNameMapper().mapToSchema(source.getContactName0()));
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
		target.setIsHotlined(source.isHotlined());

		target.setInvoiceProperties(new InvoicePropertiesMapper().mapToSchema((InvoicePropertiesInfo) source.getInvoiceProperties()));
		target.getInvoiceProperties().setBan(Integer.toString(source.getBanId()));

		target.setIsIDEN(source.isIDEN());
		target.setIsPCS(source.isPCS());
		target.setIxcCode(source.getIxcCode());
		if (source.getLanguage() != null && !source.getLanguage().trim().equals(""))
			target.setLanguage(toEnum(source.getLanguage(), Language.class));
		if (source.isPostpaidBusinessRegular()) {
			target.setLegalBusinessName(((PostpaidBusinessRegularAccountInfo) source).getLegalBusinessName());
			target.setTradeNameAttention(((PostpaidBusinessRegularAccountInfo) source).getTradeNameAttention());
			target.setBusinessBillingName(((PostpaidBusinessRegularAccountInfo) source).getLegalBusinessName());
		} else if (source.isPostpaidBusinessDealer()) {
			target.setLegalBusinessName(((PostpaidBusinessDealerAccountInfo) source).getLegalBusinessName());
			target.setTradeNameAttention(((PostpaidBusinessDealerAccountInfo) source).getTradeNameAttention());
			target.setBusinessBillingName(((PostpaidBusinessDealerAccountInfo) source).getLegalBusinessName());
		} else if (source.isPostpaidBusinessOfficial()) {
			target.setLegalBusinessName(((PostpaidBusinessOfficialAccountInfo) source).getLegalBusinessName());
			target.setTradeNameAttention(((PostpaidBusinessOfficialAccountInfo) source).getTradeNameAttention());
			target.setBusinessBillingName(((PostpaidBusinessOfficialAccountInfo) source).getLegalBusinessName());
		} else if (source.isPostpaidBusinessPersonal()) {
			target.setLegalBusinessName(((PostpaidBusinessPersonalAccountInfo) source).getLegalBusinessName());
			target.setBusinessBillingName(((PostpaidBusinessPersonalAccountInfo) source).getLegalBusinessName());
		}

		if (source.isPostpaidConsumer()) {
			target.setName(new ConsumerNameMapper().mapToSchema(((PostpaidConsumerAccountInfo) source).getName0()));
			target.setConsumerBillingName(new ConsumerNameMapper().mapToSchema(((PostpaidConsumerAccountInfo) source).getName0()));
		} else if (source.isPostpaidBusinessPersonal()) {
			target.setName(new ConsumerNameMapper().mapToSchema(((PostpaidBusinessPersonalAccountInfo) source).getName0()));
		}

		if (source.getNextBillCycle() > 0)
			target.setNextBillCycle(source.getNextBillCycle());
		if (source.getNextBillCycleCloseDay() > 0)
			target.setNextBillCycleCloseDay(source.getNextBillCycleCloseDay());

		target.setOtherPhone(source.getOtherPhone());
		target.setOtherPhoneExtension(source.getOtherPhoneExtension());
		if (source.getOtherPhoneType() != null && !source.getOtherPhoneType().trim().equals(""))
			target.setOtherPhoneType(toEnum(source.getOtherPhoneType(), OtherPhoneType.class));

		if (source.isPostpaidConsumer()) {
			target.setPaymentMethod(new PaymentMethodMapper().mapToSchema(((PostpaidConsumerAccountInfo) source).getPaymentMethod0()));
		} else if (source.isPostpaidBusinessRegular()) {
			target.setPaymentMethod(new PaymentMethodMapper().mapToSchema(((PostpaidBusinessRegularAccountInfo) source).getPaymentMethod0()));
		} else if (source.isPostpaidBusinessPersonal()) {
			target.setPaymentMethod(new PaymentMethodMapper().mapToSchema(((PostpaidBusinessPersonalAccountInfo) source).getPaymentMethod0()));
		}

		if (source.isPostpaidConsumer()) {
			PersonalCreditInfo creditInfo = ((PostpaidConsumerAccountInfo) source).getPersonalCreditInformation0();
			PersonalCreditInformation pCreditInformation = mapPersonalCreditInformation(creditInfo, source.getAddress0());
			target.setPersonalCreditInformation(pCreditInformation);
		} else if (source.isPostpaidBusinessPersonal()) {
			PersonalCreditInfo creditInfo = ((PostpaidBusinessPersonalAccountInfo) source).getPersonalCreditInformation0();
			PersonalCreditInformation pCreditInformation = mapPersonalCreditInformation(creditInfo, source.getAddress0());
			target.setPersonalCreditInformation(pCreditInformation);
		}
		
		target.setPin(source.getPin());
		if (source.isPrepaidConsumer()) {
			PrepaidAccountInfo prepaidAccInfo = new PrepaidAccountInfoMapper().mapToSchema((PrepaidConsumerAccountInfo) source);
			target.setPrepaidAccountInfo(prepaidAccInfo);
		}
		ProductSubscriberListInfo[] subList = (ProductSubscriberListInfo[]) source.getProductSubscriberLists();
		if (subList != null && subList.length > 0) {
			for (int i = 0; i < subList.length; i++) {
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
		target.setIsGstExempt(source.isGSTExempt());
		target.setIsPstExempt(source.isPSTExempt());
		target.setIsHstExempt(source.isHSTExempt());
		target.setGstExemptEffDate(source.getGSTExemptEffectiveDate());
		target.setPstExemptEffDate(source.getPSTExemptEffectiveDate());
		target.setHstExemptEffDate(source.getHSTExemptEffectiveDate());
		target.setGstExemptExpDate(source.getGSTExemptExpiryDate());
		target.setPstExemptExpDate(source.getPSTExemptExpiryDate());
		target.setHstExemptExpDate(source.getHSTExemptExpiryDate());
		target.setGstExemptCertNo(source.getGSTCertificateNumber());
		target.setPstExemptCertNo(source.getPSTCertificateNumber());
		target.setHstExemptCertNo(source.getHSTCertificateNumber());
		target.setIsHandledBySubscriberOnly(source.isHandledBySubscriberOnly());

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
			accountType=source.getAccountType().value().charAt(0);
			accountSubType= source.getAccountSubType().charAt(0);
			accountInfo= new AccountInfo(accountType, accountSubType);
			
			if (accountInfo.isPostpaidConsumer()) {
				target = PostpaidConsumerAccountInfo.newPCSInstance();
				postpaidConsumer = true;
			} else if (accountInfo.isPostpaidBusinessRegular()) {
				target = PostpaidBusinessRegularAccountInfo.newPCSInstance();
				postpaidBusinessRegular = true;
			} else if (accountInfo.isPostpaidBusinessDealer()) {
				target = PostpaidBusinessDealerAccountInfo.newPCSInstance0();
				postpaidBusinessDealer = true;
			} else if (accountInfo.isPostpaidBusinessOfficial()) {
				target = PostpaidBusinessOfficialAccountInfo.newPCSInstance0();
				postpaidBusinessOfficial = true;
			} else if (accountInfo.isPostpaidBusinessPersonal()) {
				target = PostpaidBusinessPersonalAccountInfo.newPCSInstance0();
				postpaidBusinessPersonal = true;
			}
		}

		if (source.getAccountCategory() != null)
			target.setAccountCategory(source.getAccountCategory().value());
		if (source.getAccountSubType() != null)
			target.setAccountSubType(source.getAccountSubType().charAt(0));
		if (source.getAccountType() != null)
			target.setAccountType(source.getAccountType().value().charAt(0));
		AddressInfo address = new AddressMapper().mapToDomain(source.getAddress());
		target.setAddress0(address);
		
		AddressInfo alternateAddress = new AddressMapper().mapToDomain(source.getAlternateCreditCheckAddress());
		target.getAlternateCreditCheckAddress0().copyFrom(alternateAddress);
		
//		if (createAccount)
//			target.setBanId(0);
//		else
//			target.setBanId(source.getBan());
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
		target.setEvaluationProductType(PRODUCT_TYPE_PCS);
		target.setFidoConversion(false);
		target.setGSTCertificateNumber(source.getGstExemptCertNo());
		if (source.isIsGstExempt() != null)
			target.setGstExempt(source.isIsGstExempt() ? (byte) 'Y' : (byte) 'N');
		if (source.getGstExemptEffDate() != null)
			target.setGSTExemptEffectiveDate(source.getGstExemptEffDate());
		if (source.getGstExemptExpDate() != null)
			target.setGSTExemptExpiryDate(source.getGstExemptExpDate());
		if (source.isIsHandledBySubscriberOnly() != null)
			target.setHandledBySubscriberOnly(source.isIsHandledBySubscriberOnly());
		target.setHomePhone(source.getHomePhone());
		if (source.getHomeProvince() != null)
			target.setHomeProvince(source.getHomeProvince().value());
		if (source.isIsHotlined() != null)
			target.setHotlined(source.isIsHotlined());
		target.setHSTCertificateNumber(source.getHstExemptCertNo());
		if (source.isIsHstExempt() != null)
			target.setHstExempt(source.isIsHstExempt() ? (byte) 'Y' : (byte) 'N');
		if (source.getHstExemptEffDate() != null)
			target.setHSTExemptEffectiveDate(source.getHstExemptEffDate());
		if (source.getHstExemptExpDate() != null)
			target.setHSTExemptExpiryDate(source.getHstExemptExpDate());
		
		InvoicePropertiesInfo invProperties = new InvoicePropertiesMapper().mapToDomain(source.getInvoiceProperties());
		target.setInvoiceProperties(invProperties);
		
		target.setIxcCode(source.getIxcCode());
		target.setLanguage(source.getLanguage().value());
		target.setOtherPhone(source.getOtherPhone());
		target.setOtherPhoneExtension(source.getOtherPhoneExtension());
		if (source.getOtherPhoneType() != null)
			target.setOtherPhoneType(source.getOtherPhoneType().value());
		target.setPin(source.getPin());
		target.setPSTCertificateNumber(source.getPstExemptCertNo());
		if (source.isIsPstExempt() != null)
			target.setPstExempt(source.isIsPstExempt() ? (byte) 'Y' : (byte) 'N');
		if (source.getPstExemptEffDate() != null)
			target.setPSTExemptEffectiveDate(source.getPstExemptEffDate());
		if (source.getPstExemptExpDate() != null)
			target.setPSTExemptExpiryDate(source.getPstExemptExpDate());
		target.setSalesRepCode(source.getSalesRepCode());

		return super.performDomainMapping(source, target);
	}


}
