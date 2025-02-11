package com.telus.cmb.jws.mapping.account_information_30;

import com.telus.api.account.AccountManager;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.AddressMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.BusinessCreditInformationMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.ConsumerNameMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.CreditCardMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.CreditCheckResultMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.FinancialHistoryMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.PaymentMethodMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.PersonalCreditInformationMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
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
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.Account;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.AccountStatus;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.BanCategory;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.PrepaidAccountInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.ProductSubscriberList;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.Language;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.NameFormat;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.OtherPhoneType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PersonalCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.AccountTypeCode;



public class AccountMapper extends AbstractSchemaMapper<Account, AccountInfo> {
	
	private static final int RETURN_MAXIMUM = 1000;
	
	private static AccountMapper INSTANCE = null;

	public AccountMapper() {
		super(Account.class, AccountInfo.class);
	}
	
	
	public synchronized static AccountMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AccountMapper();
		}
		
		return INSTANCE;
	}


	@SuppressWarnings("deprecation")
	@Override
	protected Account performSchemaMapping(AccountInfo source, Account target) {

		//start populating all mandatory fields (per XSD), we need to make sure all fields has valid value 
		target.setBillingAccountNumber(Integer.toString(source.getBanId()));
		target.setAccountType(toEnum(String.valueOf(source.getAccountType()), AccountTypeCode.class));
		target.setAccountSubType(String.valueOf(source.getAccountSubType()));
		target.setBrandId(source.getBrandId());
		
		AddressInfo billingAddress = source.getAddress0();
		target.setAddress(AddressMapper.getInstance().mapToSchema(billingAddress));
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
		} else {
			target.setHomeProvince( toEnum(source.getHomeProvince(), ProvinceCode.class ) );
		}
		
		if (source.isPostpaidConsumer() && source instanceof PostpaidConsumerAccountInfo) {
			target.setName(ConsumerNameMapper.getInstance().mapToSchema(((PostpaidConsumerAccountInfo) source).getName0()));
			target.setConsumerBillingName(ConsumerNameMapper.getInstance().mapToSchema(((PostpaidConsumerAccountInfo) source).getName0()));
		} else if (source.isPostpaidBusinessPersonal() && source instanceof PostpaidBusinessPersonalAccountInfo) {
			target.setName(ConsumerNameMapper.getInstance().mapToSchema(((PostpaidBusinessPersonalAccountInfo) source).getName0()));
		} else if (source.isPrepaidConsumer() && source instanceof PrepaidConsumerAccountInfo) {
			target.setName(ConsumerNameMapper.getInstance().mapToSchema(((PrepaidConsumerAccountInfo) source).getName0()));
		} else { 
			//apply empty name force generating XML element
			ConsumerNameInfo emptyConsumerName = new ConsumerNameInfo();
			emptyConsumerName.setFirstName(""); //apply empty string to force generating XML element
			emptyConsumerName.setLastName(""); //apply empty string to force generating XML element
			target.setName(ConsumerNameMapper.getInstance().mapToSchema( emptyConsumerName ));
		}
		target.setFullName(source.getFullName());
		if (target.getFullName()==null) target.setFullName(""); //apply empty string to force generating XML element

		if (source.getLanguage() != null && !source.getLanguage().trim().equals("")) {
			target.setLanguage(toEnum(source.getLanguage(), Language.class));
		} else {
			target.setLanguage(toEnum("EN", Language.class) );
		}
		
		target.setDealerCode(source.getDealerCode());
		if (target.getDealerCode()==null) target.setDealerCode("A001000001"); //TODO remove this line once EJB is ready
		target.setSalesRepCode(source.getSalesRepCode());
		if( target.getSalesRepCode()==null ) target.setSalesRepCode("0000"); //TODO remove this line when EJB is populating this field for lightweight
		
		//end of mandatory field population
		
		if ( source.getBillCycle()>0)
			target.setBillCycle(source.getBillCycle());
		if ( source.getBillCycleCloseDay()>0)
			target.setBillCycleCloseDay(source.getBillCycleCloseDay());
		
		boolean lightweightInd = (billingAddress==null) || billingAddress.isEmpty() ;
		if (lightweightInd==false ) {
			target.setAccountCategory(toEnum(source.getAccountCategory(), BanCategory.class));
			
			AddressInfo alternateCreditAddress = source.getAlternateCreditCheckAddress0();
			if ( alternateCreditAddress!=null && alternateCreditAddress.isEmpty()==false)
				target.setAlternateCreditCheckAddress(AddressMapper.getInstance().mapToSchema(source.getAlternateCreditCheckAddress0()));

			target.setBanSegment(source.getBanSegment());
			target.setBanSubSegment(source.getBanSubSegment());

			target.setBillingNameFormat(toEnum(String.valueOf(source.getBillingNameFormat()), NameFormat.class));
			if (source.isPostpaidConsumer() && source instanceof PostpaidConsumerAccountInfo) {
				target.setBirthDate(((PostpaidConsumerAccountInfo) source).getPersonalCreditInformation0().getBirthDate());
			} else if (source.isPostpaidBusinessRegular() && source instanceof PostpaidBusinessRegularAccountInfo) {
				target.setBirthDate(((PostpaidBusinessRegularAccountInfo) source).getPersonalCreditInformation0().getBirthDate());
				target.setBusinessCreditInformation(new BusinessCreditInformationMapper().mapToSchema(((PostpaidBusinessRegularAccountInfo) source).getCreditInformation0()));
			} else if (source.isPrepaidConsumer() && source instanceof PrepaidConsumerAccountInfo) {
				target.setBirthDate(((PrepaidConsumerAccountInfo) source).getBirthDate());
			}
			target.setBusinessPhone(source.getBusinessPhone());
			target.setBusinessPhoneExtension(source.getBusinessPhoneExtension());
			target.getClientConsentIndicatorCode().addAll(toCollection(source.getClientConsentIndicatorCodes()));
			target.setContactExtension(source.getContactPhoneExtension());
			target.setContactFax(source.getContactFax());
			target.setContactName(ConsumerNameMapper.getInstance().mapToSchema(source.getContactName0()));
			target.setContactPhone(source.getContactPhone());
			target.setCreateDate(source.getCreateDate());
			target.setCreditCheckResult(new CreditCheckResultMapper().mapToSchema(source.getCreditCheckResult0()));
			target.setEmail(source.getEmail());
			target.setFinancialHistory(new FinancialHistoryMapper().mapToSchema(source.getFinancialHistory0()));
			target.setHomePhone(source.getHomePhone());
			target.setHotlinedInd(source.isHotlined());

			target.setInvoicePropertyList(InvoicePropertyMapper.getInstance().mapToSchema((InvoicePropertiesInfo) source.getInvoiceProperties()));

			target.setIDENInd(source.isIDEN());
			target.setPCSInd(source.isPCS());
			target.setIxcCode(source.getIxcCode());
			
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

			if (source.isPostpaidConsumer() && source instanceof PostpaidConsumerAccountInfo) {
				target.setPaymentMethod(new PaymentMethodMapper().mapToSchema(((PostpaidConsumerAccountInfo) source).getPaymentMethod0()));
			} else if (source.isPostpaidBusinessRegular() && source instanceof PostpaidBusinessRegularAccountInfo) {
				target.setPaymentMethod(new PaymentMethodMapper().mapToSchema(((PostpaidBusinessRegularAccountInfo) source).getPaymentMethod0()));
			} else if (source.isPostpaidBusinessPersonal() && source instanceof PostpaidBusinessPersonalAccountInfo) {
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
			
			target.setStartServiceDate(source.getStartServiceDate());
			target.setStatus(toEnum(String.valueOf(source.getStatus()), AccountStatus.class));
			target.setStatusDate(source.getStatusDate());
			target.setVerifiedDate(source.getVerifiedDate());
			if (source.getCustomerId()>0)
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
		}
		
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
			
			/** This is needed again as the above new instance methods would overwrite the account type/subtype mapping **/
			target.setAccountType(source.getAccountType().value().charAt(0));
			target.setAccountSubType(source.getAccountSubType().charAt(0));
		}

		if (source.getAccountCategory() != null)
			target.setAccountCategory(source.getAccountCategory().value());
		
		AddressInfo address = AddressMapper.getInstance().mapToDomain(source.getAddress());
		target.setAddress0(address);
		
		AddressInfo alternateAddress = AddressMapper.getInstance().mapToDomain(source.getAlternateCreditCheckAddress());
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
		
		InvoicePropertiesInfo invProperties = InvoicePropertyMapper.getInstance().mapToDomain(source.getInvoicePropertyList());
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
