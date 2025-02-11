package com.telus.cmb.common.dao.amdocs;

import java.util.ArrayList;
import java.util.Date;

import amdocs.APILink.datatypes.AccountTypeInfo;
import amdocs.APILink.datatypes.ApplyPaymentDetailsInfo;
import amdocs.APILink.datatypes.BillDetailsInfo;
import amdocs.APILink.datatypes.BusinessInfo;
import amdocs.APILink.datatypes.BusinessListInfo;
import amdocs.APILink.datatypes.CPUIInfo;
import amdocs.APILink.datatypes.CheckDetailsInfo;
import amdocs.APILink.datatypes.ContactInfo;
import amdocs.APILink.datatypes.ContractInfo;
import amdocs.APILink.datatypes.CreditCardDetailsInfo;
import amdocs.APILink.datatypes.CreditResultDeposit;
import amdocs.APILink.datatypes.CreditResultExt1;
import amdocs.APILink.datatypes.CreditResultExt2;
import amdocs.APILink.datatypes.CreditResultExtInfo;
import amdocs.APILink.datatypes.CreditResultInfo;
import amdocs.APILink.datatypes.CycleInfo;
import amdocs.APILink.datatypes.IdentificationInfo;
import amdocs.APILink.datatypes.NameInfo;
import amdocs.APILink.datatypes.NationalGrowthInfo;
import amdocs.APILink.datatypes.SearchDuplicateResult;
import amdocs.APILink.datatypes.SpecialBillDetailsInfo;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.InvoiceProperties;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.ChequeInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.DuplicateBillingAccountInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PostpaidBoxedConsumerAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.framework.info.Info;

public class AmdocsTelusMapping {
	
	private static final String TUXEDO_NULL_STRING = "@@";
	
	public static Info mapAmdocsToTelus(Object pSourceAmdocsInfoClass, Info pTargetTelusInfoClass) throws ApplicationException {
		// map amdocsAddressInfo -> addressInfo
		//     -----------------
		if ((pSourceAmdocsInfoClass instanceof amdocs.APILink.datatypes.AddressInfo) &&
				pTargetTelusInfoClass instanceof AddressInfo) {
			amdocs.APILink.datatypes.AddressInfo amdocsAddressInfo = (amdocs.APILink.datatypes.AddressInfo)pSourceAmdocsInfoClass;
			AddressInfo addressInfo = pTargetTelusInfoClass != null ? (AddressInfo)pTargetTelusInfoClass : new AddressInfo();
			mapFromAmdocsAddressInfo (amdocsAddressInfo, addressInfo);
			return addressInfo;
		}

		// map amdocsNameInfo -> nameInfo
		//     -----------------
		if ((pSourceAmdocsInfoClass instanceof amdocs.APILink.datatypes.NameInfo) &&
				pTargetTelusInfoClass instanceof ConsumerNameInfo) {
			NameInfo amdocsNameInfo = (amdocs.APILink.datatypes.NameInfo)pSourceAmdocsInfoClass;
			ConsumerNameInfo consumerNameInfo = pTargetTelusInfoClass != null ? (ConsumerNameInfo)pTargetTelusInfoClass : new ConsumerNameInfo();

			mapFromAmdocsConsumerNameInfo (amdocsNameInfo, consumerNameInfo);
			return consumerNameInfo;
		}


		// map amdocsSearchDuplicateResult -> duplicateBillingAccountInfo
		//     ---------------------------
		if ((pSourceAmdocsInfoClass instanceof SearchDuplicateResult) &&
				pTargetTelusInfoClass instanceof DuplicateBillingAccountInfo) {
			SearchDuplicateResult amdocsSearchDuplicateResult = (SearchDuplicateResult)pSourceAmdocsInfoClass;
			DuplicateBillingAccountInfo duplicateBillingAccountInfo = pTargetTelusInfoClass != null ? (DuplicateBillingAccountInfo)pTargetTelusInfoClass : new DuplicateBillingAccountInfo();
			mapFromAmdocsDuplicateSearchResult(amdocsSearchDuplicateResult, duplicateBillingAccountInfo);

			return duplicateBillingAccountInfo;
		}

		// map amdocsCreditResultInfo -> CreditCheckResultInfo
		//     ---------------------
		if ((pSourceAmdocsInfoClass instanceof CreditResultInfo) &&
				pTargetTelusInfoClass instanceof CreditCheckResultInfo) {
			CreditResultInfo amdocsCreditResultInfo = (CreditResultInfo)pSourceAmdocsInfoClass;
			CreditCheckResultInfo creditCheckResultInfo = pTargetTelusInfoClass != null ? (CreditCheckResultInfo)pTargetTelusInfoClass : new CreditCheckResultInfo();

			mapFromAmdocsCreditCheckResult (amdocsCreditResultInfo, creditCheckResultInfo);
			return creditCheckResultInfo;
		}
		// map amdocsBusinessListInfo -> businessCreditIdentityInfo
		//     ----------------------
		if ((pSourceAmdocsInfoClass instanceof BusinessListInfo) &&
				pTargetTelusInfoClass instanceof BusinessCreditIdentityInfo) {
			BusinessListInfo amdocsBusinessListInfo = (BusinessListInfo)pSourceAmdocsInfoClass;
			BusinessCreditIdentityInfo businessCreditIdentityInfo = pTargetTelusInfoClass != null ? (BusinessCreditIdentityInfo)pTargetTelusInfoClass : new BusinessCreditIdentityInfo();

			mapFromAmdocsBusinessListInfo (amdocsBusinessListInfo, businessCreditIdentityInfo);
			return businessCreditIdentityInfo;
		}

		return null;
	}


	public static    Object mapTelusToAmdocs(Info pSourceTelusInfoClass, Object pTargetAmdocsInfoClass) throws ApplicationException {


		// Telus API Info classes
		AccountInfo accountInfo = new AccountInfo();
		PostpaidConsumerAccountInfo postpaidConsumerAccountInfo = null;
		PrepaidConsumerAccountInfo prepaidConsumerAccountInfo = PrepaidConsumerAccountInfo.newPCSInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID);
		// QuebectelPrepaidConsumerAccountInfo quebectelPrepaidConsumerAccountInfo = QuebectelPrepaidConsumerAccountInfo.newPCSInstance0();
		PostpaidBoxedConsumerAccountInfo postpaidBoxedConsumerAccountInfo = null;
		PostpaidBusinessRegularAccountInfo postpaidBusinessRegularAccountInfo = null;

		AddressInfo addressInfo = new AddressInfo();
		ConsumerNameInfo consumerNameInfo = new ConsumerNameInfo();
		PersonalCreditInfo personalCreditInfo = new PersonalCreditInfo();
		com.telus.eas.account.info.CreditCardInfo creditCardInfo = new com.telus.eas.account.info.CreditCardInfo();
		ChequeInfo chequeInfo = new ChequeInfo();
		// MemoInfo memoInfo = new MemoInfo();
		// FollowUpInfo followUpInfo = new FollowUpInfo();
		PaymentInfo paymentInfo = new PaymentInfo();
		PaymentMethodInfo paymentMethodInfo = new PaymentMethodInfo();
		BusinessCreditInfo businessCreditInfo = new BusinessCreditInfo();
		CreditCheckResultInfo creditCheckResultInfo = new CreditCheckResultInfo();

		// Amdocs API Info classes
		AccountTypeInfo amdocsAccountTypeInfo = new AccountTypeInfo();
		amdocs.APILink.datatypes.NameInfo amdocsNameInfo = new NameInfo();
		amdocs.APILink.datatypes.AddressInfo amdocsAddressInfo = new amdocs.APILink.datatypes.AddressInfo();
		IdentificationInfo amdocsIdentificationInfo = new IdentificationInfo();
		CycleInfo amdocsCycleInfo = new CycleInfo();
		ContactInfo  amdocsContactInfo = new ContactInfo();
		ContractInfo  amdocsContractInfo = new ContractInfo();
		// AddressMatchInfo amdocsAddressMatchInfo = new AddressMatchInfo();
		// SearchDuplicateResult amdocsSearchDuplicateResult = new SearchDuplicateResult();
		amdocs.APILink.datatypes.CreditCardInfo amdocsCreditCardInfo = new amdocs.APILink.datatypes.CreditCardInfo();
		CreditCardDetailsInfo amdocsCreditCardDetailsInfo = new CreditCardDetailsInfo();
		CheckDetailsInfo amdocsCheckDetailsInfo = new CheckDetailsInfo();
		BusinessInfo amdocsBusinessInfo = new BusinessInfo();
		ApplyPaymentDetailsInfo amdocsApplyPaymentDetailsInfo = new ApplyPaymentDetailsInfo();
		BillDetailsInfo amdocsBillDetailsInfo = new BillDetailsInfo();
		SpecialBillDetailsInfo amdocsSpecialBillDetailsInfo = new SpecialBillDetailsInfo();
		NationalGrowthInfo amdocsNationalGrowthInfo = new NationalGrowthInfo();
		CreditResultExtInfo amdocsCreditResultExtInfo = new CreditResultExtInfo();
		CPUIInfo amdocsCPUIInfo = new CPUIInfo();
		amdocs.APILink.datatypes.TaxExemptionInfo amdocsTaxExemptionInfo = new amdocs.APILink.datatypes.TaxExemptionInfo();


		// map consumerNameInfo -> amdocsNameInfo
		//     ----------------------------------
		if ((pSourceTelusInfoClass instanceof ConsumerNameInfo) &&
				pTargetAmdocsInfoClass instanceof NameInfo) {
			consumerNameInfo = (ConsumerNameInfo)pSourceTelusInfoClass;
			amdocsNameInfo = pTargetAmdocsInfoClass != null ? (NameInfo)pTargetAmdocsInfoClass : new NameInfo();

			amdocsNameInfo.nameTitle = AttributeTranslator.emptyFromNull(consumerNameInfo.getTitle());
			amdocsNameInfo.firstName = AttributeTranslator.emptyFromNull(consumerNameInfo.getFirstName());
			amdocsNameInfo.middleInitial = AttributeTranslator.emptyFromNull(consumerNameInfo.getMiddleInitial());
			amdocsNameInfo.lastBusinessName = AttributeTranslator.emptyFromNull(consumerNameInfo.getLastName());
			amdocsNameInfo.additionalTitle = AttributeTranslator.emptyFromNull(consumerNameInfo.getAdditionalLine());
			amdocsNameInfo.nameSuffix = AttributeTranslator.emptyFromNull(consumerNameInfo.getGeneration());
			return amdocsNameInfo;
		}

		// map accountInfo -> amdocsAccountTypeInfo
		//     ------------------------------------
		if ((pSourceTelusInfoClass instanceof AccountInfo) &&
				pTargetAmdocsInfoClass instanceof AccountTypeInfo) {
			accountInfo = (AccountInfo)pSourceTelusInfoClass;
			amdocsAccountTypeInfo = pTargetAmdocsInfoClass != null ? (AccountTypeInfo)pTargetAmdocsInfoClass : new AccountTypeInfo();

			amdocsAccountTypeInfo.accountType = AttributeTranslator.byteFromString(new Character(accountInfo.getAccountType()).toString());
			amdocsAccountTypeInfo.accountSubType = AttributeTranslator.byteFromString(new Character(accountInfo.getAccountSubType()).toString());
			amdocsAccountTypeInfo.brandId = accountInfo.getBrandId();

			return amdocsAccountTypeInfo;
		}

		// map accountInfo -> amdocsContractInfo
		//     ---------------------------------
		if ((pSourceTelusInfoClass instanceof AccountInfo) &&
				pTargetAmdocsInfoClass instanceof ContractInfo) {
			accountInfo = (AccountInfo)pSourceTelusInfoClass;
			amdocsContractInfo = pTargetAmdocsInfoClass != null ? (ContractInfo)pTargetAmdocsInfoClass : new ContractInfo();

			amdocsContractInfo.dealerCode = AttributeTranslator.emptyFromNull(accountInfo.getDealerCode());
			amdocsContractInfo.salesCode = AttributeTranslator.emptyFromNull(accountInfo.getSalesRepCode());

			return amdocsContractInfo;
		}

		// map accountInfo -> amdocsCycleInfo
		//     ------------------------------
		if ((pSourceTelusInfoClass instanceof AccountInfo) &&
				pTargetAmdocsInfoClass instanceof CycleInfo) {
			accountInfo = (AccountInfo)pSourceTelusInfoClass;
			amdocsCycleInfo = pTargetAmdocsInfoClass != null ? (CycleInfo)pTargetAmdocsInfoClass : new CycleInfo();

			amdocsCycleInfo.cycleCode = new Short(new Integer(accountInfo.getBillCycle()).toString()).shortValue();

			return amdocsCycleInfo;
		}

		// map accountInfo -> amdocsBillDetailsInfo
		//     ------------------------------------
		if ((pSourceTelusInfoClass instanceof AccountInfo) &&
				pTargetAmdocsInfoClass instanceof BillDetailsInfo) {
			accountInfo = (AccountInfo) pSourceTelusInfoClass;
			amdocsBillDetailsInfo = pTargetAmdocsInfoClass != null ? (BillDetailsInfo) pTargetAmdocsInfoClass : new BillDetailsInfo();

			amdocsBillDetailsInfo.csDefIxcCode = AttributeTranslator.emptyFromNull(accountInfo.getIxcCode());

			InvoiceProperties invoiceProperties = accountInfo.getInvoiceProperties();
			if (invoiceProperties.getInvoiceSuppressionLevel() != null && !invoiceProperties.getInvoiceSuppressionLevel().trim().equals(""))
				amdocsBillDetailsInfo.invSuppressionInd = AttributeTranslator.byteFromString(invoiceProperties.getInvoiceSuppressionLevel());

			if (invoiceProperties.getHoldRedirectDestinationCode() != null && !invoiceProperties.getHoldRedirectDestinationCode().trim().equals("") && !invoiceProperties.getHoldRedirectDestinationCode().trim().equals("0")) {
				amdocsBillDetailsInfo.blManHndlReqOpid = AttributeTranslator.parseInt(invoiceProperties.getHoldRedirectDestinationCode());
				amdocsBillDetailsInfo.blManHndlEffDate = invoiceProperties.getHoldRedirectFromDate() != null ? invoiceProperties.getHoldRedirectFromDate() : new Date();
				amdocsBillDetailsInfo.blManHndlExpDate = invoiceProperties.getHoldRedirectToDate() != null ? invoiceProperties.getHoldRedirectToDate() : AttributeTranslator.dateFromString("21001231", "yyyyMMdd");
			}

			return amdocsBillDetailsInfo;
		}

		// map accountInfo -> amdocsSpecialBillDetailsInfo
		//     ------------------------------------
		if ((pSourceTelusInfoClass instanceof AccountInfo) &&
				pTargetAmdocsInfoClass instanceof SpecialBillDetailsInfo) {
			accountInfo = (AccountInfo)pSourceTelusInfoClass;
			amdocsSpecialBillDetailsInfo = pTargetAmdocsInfoClass != null ? (SpecialBillDetailsInfo)pTargetAmdocsInfoClass : new SpecialBillDetailsInfo();

			amdocsSpecialBillDetailsInfo.csDefIxcCode = AttributeTranslator.emptyFromNull(accountInfo.getIxcCode());

			InvoiceProperties invoiceProperties = accountInfo.getInvoiceProperties();
			if (invoiceProperties.getInvoiceSuppressionLevel() != null && !invoiceProperties.getInvoiceSuppressionLevel().trim().equals(""))
				amdocsBillDetailsInfo.invSuppressionInd = AttributeTranslator.byteFromString(invoiceProperties.getInvoiceSuppressionLevel());

			if (invoiceProperties.getHoldRedirectDestinationCode() != null && !invoiceProperties.getHoldRedirectDestinationCode().trim().equals("") && !invoiceProperties.getHoldRedirectDestinationCode().trim().equals("0")) {
				amdocsBillDetailsInfo.blManHndlReqOpid = AttributeTranslator.parseInt(invoiceProperties.getHoldRedirectDestinationCode());
				amdocsBillDetailsInfo.blManHndlEffDate = invoiceProperties.getHoldRedirectFromDate() != null ? invoiceProperties.getHoldRedirectFromDate() : new Date();
				amdocsBillDetailsInfo.blManHndlExpDate = invoiceProperties.getHoldRedirectToDate() != null ? invoiceProperties.getHoldRedirectToDate() : AttributeTranslator.dateFromString("21001231", "yyyyMMdd");
			}

			return amdocsSpecialBillDetailsInfo;
		}

		// map accountInfo -> amdocsIdentificationInfo
		//     ---------------------------------------
		if ((pSourceTelusInfoClass instanceof AccountInfo) &&
				pTargetAmdocsInfoClass instanceof IdentificationInfo) {
			accountInfo = (AccountInfo)pSourceTelusInfoClass;
			amdocsIdentificationInfo = pTargetAmdocsInfoClass != null ? (IdentificationInfo)pTargetAmdocsInfoClass : new IdentificationInfo();

			amdocsIdentificationInfo.accPassword = AttributeTranslator.emptyFromNull(accountInfo.getPin());
			amdocsIdentificationInfo.emailAddress = AttributeTranslator.emptyFromNull(accountInfo.getEmail());
			amdocsIdentificationInfo.langPref = AttributeTranslator.emptyFromNull(accountInfo.getLanguage());
			amdocsIdentificationInfo.verifiedDate = accountInfo.getVerifiedDate();

			// - PrepaidConsumerAccountInfo
			if (pSourceTelusInfoClass instanceof PrepaidConsumerAccountInfo) {
				prepaidConsumerAccountInfo = (PrepaidConsumerAccountInfo)accountInfo;
				amdocsIdentificationInfo.birthDate = prepaidConsumerAccountInfo.getBirthDate();
			}
			return amdocsIdentificationInfo;
		}

		// map accountInfo -> amdocsContactInfo
		//     --------------------------------
		if ((pSourceTelusInfoClass instanceof AccountInfo) &&
				pTargetAmdocsInfoClass instanceof ContactInfo) {
			accountInfo = (AccountInfo)pSourceTelusInfoClass;
			amdocsContactInfo = pTargetAmdocsInfoClass != null ? (ContactInfo)pTargetAmdocsInfoClass : new ContactInfo();

			amdocsContactInfo.otherTelType = AttributeTranslator.emptyFromNull(accountInfo.getOtherPhoneType());
			amdocsContactInfo.otherTelNo = AttributeTranslator.emptyFromNull(accountInfo.getOtherPhone());
			amdocsContactInfo.otherTelExtNo = AttributeTranslator.emptyFromNull(accountInfo.getOtherPhoneExtension());

			amdocsContactInfo.homeTelNo = AttributeTranslator.emptyFromNull(accountInfo.getHomePhone());
			amdocsContactInfo.workTelNo = AttributeTranslator.emptyFromNull(accountInfo.getBusinessPhone());
			amdocsContactInfo.workTelExtNo = AttributeTranslator.emptyFromNull(accountInfo.getBusinessPhoneExtension()).trim();

			// - PostpaidConsumerAccountInfo
			if (pSourceTelusInfoClass instanceof PostpaidConsumerAccountInfo) {
				postpaidConsumerAccountInfo = (PostpaidConsumerAccountInfo)accountInfo;
				amdocsContactInfo.contactTelNo = AttributeTranslator.emptyFromNull(postpaidConsumerAccountInfo.getContactPhone()).equals("") ? AttributeTranslator.emptyFromNull(postpaidConsumerAccountInfo.getHomePhone()) : AttributeTranslator.emptyFromNull(postpaidConsumerAccountInfo.getContactPhone());
				amdocsContactInfo.contactTelExtNo = AttributeTranslator.emptyFromNull(postpaidConsumerAccountInfo.getContactPhoneExtension()).trim();
			}
			// - PrepaidConsumerAccountInfo
			if (pSourceTelusInfoClass instanceof PrepaidConsumerAccountInfo) {
				prepaidConsumerAccountInfo = (PrepaidConsumerAccountInfo)accountInfo;
				amdocsContactInfo.contactTelNo = AttributeTranslator.emptyFromNull(prepaidConsumerAccountInfo.getContactPhone()).equals("") ? AttributeTranslator.emptyFromNull(prepaidConsumerAccountInfo.getHomePhone()) : AttributeTranslator.emptyFromNull(prepaidConsumerAccountInfo.getContactPhone());
			}
			// - PostpaidBoxedConsumerAccountInfo
			if (pSourceTelusInfoClass instanceof PostpaidBoxedConsumerAccountInfo) {
				postpaidBoxedConsumerAccountInfo = (PostpaidBoxedConsumerAccountInfo)accountInfo;
				amdocsContactInfo.contactTelNo = AttributeTranslator.emptyFromNull(postpaidBoxedConsumerAccountInfo.getContactPhone()).equals("") ? AttributeTranslator.emptyFromNull(postpaidBoxedConsumerAccountInfo.getHomePhone()) : AttributeTranslator.emptyFromNull(postpaidBoxedConsumerAccountInfo.getContactPhone());
				amdocsContactInfo.contactTelExtNo = AttributeTranslator.emptyFromNull(postpaidBoxedConsumerAccountInfo.getContactPhoneExtension()).trim();
			}
			// - PostpaidBusinessRegularAccountInfo
			if (pSourceTelusInfoClass instanceof PostpaidBusinessRegularAccountInfo) {
				postpaidBusinessRegularAccountInfo = (PostpaidBusinessRegularAccountInfo)accountInfo;
				amdocsContactInfo.contactTelNo = AttributeTranslator.emptyFromNull(postpaidBusinessRegularAccountInfo.getContactPhone());
				amdocsContactInfo.contactTelExtNo = AttributeTranslator.emptyFromNull(postpaidBusinessRegularAccountInfo.getContactPhoneExtension()).trim();
			}
			return amdocsContactInfo;
		}


		// map accountInfo -> amdocsTaxExemptionInfo
		//     ------------------------------
		if ((pSourceTelusInfoClass instanceof AccountInfo) &&
				pTargetAmdocsInfoClass instanceof amdocs.APILink.datatypes.TaxExemptionInfo) {
			accountInfo = (AccountInfo)pSourceTelusInfoClass;
			amdocsTaxExemptionInfo = pTargetAmdocsInfoClass != null ? (amdocs.APILink.datatypes.TaxExemptionInfo)pTargetAmdocsInfoClass : new amdocs.APILink.datatypes.TaxExemptionInfo();

			//Set tax indicators
			amdocsTaxExemptionInfo.gstExemptionInd = accountInfo.getGSTExempt();
			amdocsTaxExemptionInfo.pstExemptionInd = accountInfo.getPSTExempt();
			amdocsTaxExemptionInfo.hstExemptionInd = accountInfo.getHSTExempt();

			//Set Effective dates for tax exemptions
			amdocsTaxExemptionInfo.gstEffDate = accountInfo.getGSTExemptEffectiveDate();
			amdocsTaxExemptionInfo.pstEffDate = accountInfo.getPSTExemptEffectiveDate();
			amdocsTaxExemptionInfo.hstEffDate = accountInfo.getHSTExemptEffectiveDate();

			//Set Effective dates for tax exemptions
			amdocsTaxExemptionInfo.gstExpDate = accountInfo.getGSTExemptExpiryDate();
			amdocsTaxExemptionInfo.pstExpDate = accountInfo.getPSTExemptExpiryDate();
			amdocsTaxExemptionInfo.hstExpDate = accountInfo.getHSTExemptExpiryDate();

			//Set Certificate for tax exemptions
			amdocsTaxExemptionInfo.gstCertificateNumber = accountInfo.getGSTCertificateNumber();

			return amdocsTaxExemptionInfo;
		}


		// map personalCreditInfo -> amdocsIdentificationInfo
		//     ----------------------------------------------
		if ((pSourceTelusInfoClass instanceof PersonalCreditInfo) &&
				pTargetAmdocsInfoClass instanceof IdentificationInfo) {
			personalCreditInfo = (PersonalCreditInfo)pSourceTelusInfoClass;
			amdocsIdentificationInfo = pTargetAmdocsInfoClass != null ? (IdentificationInfo)pTargetAmdocsInfoClass : new IdentificationInfo();

			if (personalCreditInfo.getBirthDate() != null) amdocsIdentificationInfo.birthDate = personalCreditInfo.getBirthDate();
			if (personalCreditInfo.getSin() != null)
				if (personalCreditInfo.getSin().trim().equals(""))
					amdocsIdentificationInfo.customerSsn = 0;
				else{
					try{
					amdocsIdentificationInfo.customerSsn = Integer.parseInt(personalCreditInfo.getSin());
					}catch(java.lang.NumberFormatException nfe){
						throw new ApplicationException(SystemCodes.CMB_CMN_T2A,"Number Format Exception, SIN can only have numeric value","");
					}
				}
			if (personalCreditInfo.getDriversLicenseExpiry() != null) amdocsIdentificationInfo.drivrLicnsExpDt = personalCreditInfo.getDriversLicenseExpiry();
			if (personalCreditInfo.getDriversLicense() != null) amdocsIdentificationInfo.drivrLicnsNo = personalCreditInfo.getDriversLicense();
			if (personalCreditInfo.getDriversLicenseProvince() != null) amdocsIdentificationInfo.drivrLicnsProvince = personalCreditInfo.getDriversLicenseProvince();
			return amdocsIdentificationInfo;
		}

		// map personalCreditInfo -> amdocsCreditCardInfo
		//     ------------------------------------------
		if ((pSourceTelusInfoClass instanceof PersonalCreditInfo) &&
				pTargetAmdocsInfoClass instanceof amdocs.APILink.datatypes.CreditCardInfo) {
			personalCreditInfo = (PersonalCreditInfo)pSourceTelusInfoClass;
			amdocsCreditCardInfo = pTargetAmdocsInfoClass != null ? (amdocs.APILink.datatypes.CreditCardInfo)pTargetAmdocsInfoClass : new amdocs.APILink.datatypes.CreditCardInfo();

			//PCI changes
			mapCreditCardInfo(personalCreditInfo.getCreditCard0(), amdocsCreditCardInfo);
			return amdocsCreditCardInfo;
		}

		// map businessCreditInfo -> amdocsBusinessInfo
		//     ----------------------------------------
		if ((pSourceTelusInfoClass instanceof BusinessCreditInfo) &&
				pTargetAmdocsInfoClass instanceof BusinessInfo) {
			businessCreditInfo = (BusinessCreditInfo)pSourceTelusInfoClass;
			amdocsBusinessInfo = pTargetAmdocsInfoClass != null ? (BusinessInfo)pTargetAmdocsInfoClass : new BusinessInfo();

			if (businessCreditInfo.getIncorporationDate() != null) amdocsBusinessInfo.incorporationDate = businessCreditInfo.getIncorporationDate();
			if (businessCreditInfo.getIncorporationNumber() != null) amdocsBusinessInfo.incorporationNo = businessCreditInfo.getIncorporationNumber();
			return amdocsBusinessInfo;
		}

		// map addressInfo -> amdocsAddressInfo
		//     --------------------------------
		if ((pSourceTelusInfoClass instanceof AddressInfo) &&
				pTargetAmdocsInfoClass instanceof amdocs.APILink.datatypes.AddressInfo) {
			addressInfo = (AddressInfo)pSourceTelusInfoClass;
			amdocsAddressInfo = pTargetAmdocsInfoClass != null ? (amdocs.APILink.datatypes.AddressInfo)pTargetAmdocsInfoClass : new amdocs.APILink.datatypes.AddressInfo();

			amdocsAddressInfo.type = AttributeTranslator.byteFromString(addressInfo.getAddressType());
			amdocsAddressInfo.attention = AttributeTranslator.emptyFromNull(addressInfo.getAttention());
			amdocsAddressInfo.primaryLine = AttributeTranslator.emptyFromNull(addressInfo.getPrimaryLine());
			amdocsAddressInfo.secondaryLine = AttributeTranslator.emptyFromNull(addressInfo.getSecondaryLine());
			amdocsAddressInfo.city = AttributeTranslator.emptyFromNull(addressInfo.getCity());
			if ("NF".equals(addressInfo.getProvince())) {
				amdocsAddressInfo.province = AttributeTranslator.emptyFromNull("NL");
			}else {
				amdocsAddressInfo.province = AttributeTranslator.emptyFromNull(addressInfo.getProvince());
			}
			amdocsAddressInfo.postalCode = AttributeTranslator.emptyFromNull(addressInfo.getPostalCode());
			amdocsAddressInfo.civicNo = AttributeTranslator.emptyFromNull(addressInfo.getCivicNo());
			amdocsAddressInfo.civicNoSuffix = AttributeTranslator.emptyFromNull(addressInfo.getCivicNoSuffix());
			amdocsAddressInfo.streetDirection = AttributeTranslator.emptyFromNull(addressInfo.getStreetDirection());
			amdocsAddressInfo.streetName = AttributeTranslator.emptyFromNull(addressInfo.getStreetName());
			amdocsAddressInfo.streetType = AttributeTranslator.emptyFromNull(addressInfo.getStreetType());
			amdocsAddressInfo.unitDesignator = AttributeTranslator.emptyFromNull(addressInfo.getUnitDesignator());
			amdocsAddressInfo.unitIdentifier = AttributeTranslator.emptyFromNull(addressInfo.getUnitIdentifier());
			amdocsAddressInfo.rrAreaNumber = AttributeTranslator.emptyFromNull(addressInfo.getRrAreaNumber());
			amdocsAddressInfo.rrBox = AttributeTranslator.emptyFromNull(addressInfo.getRrBox());
			amdocsAddressInfo.rrCompartment = AttributeTranslator.emptyFromNull(addressInfo.getRrCompartment());
			amdocsAddressInfo.rrDeliveryType = AttributeTranslator.emptyFromNull(addressInfo.getRrDeliveryType());
			amdocsAddressInfo.rrDesignator = AttributeTranslator.emptyFromNull(addressInfo.getRrDesignator());
			amdocsAddressInfo.rrGroup = AttributeTranslator.emptyFromNull(addressInfo.getRrGroup());
			amdocsAddressInfo.rrIdentifier = AttributeTranslator.emptyFromNull(addressInfo.getRrIdentifier());
			amdocsAddressInfo.rrQualifier = AttributeTranslator.emptyFromNull(addressInfo.getRrQualifier());
			amdocsAddressInfo.rrSite = AttributeTranslator.emptyFromNull(addressInfo.getRrSite());
			amdocsAddressInfo.country = AttributeTranslator.emptyFromNull(addressInfo.getCountry());
			amdocsAddressInfo.zipGeoCode = AttributeTranslator.emptyFromNull(addressInfo.getZipGeoCode());
			amdocsAddressInfo.foreignState = AttributeTranslator.emptyFromNull(addressInfo.getForeignState());
			return amdocsAddressInfo;
		}

		// map paymentMethodInfo -> amdocsCreditCardDetailsInfo
		//     ------------------------------------------------
		if ((pSourceTelusInfoClass instanceof PaymentMethodInfo) &&
				pTargetAmdocsInfoClass instanceof CreditCardDetailsInfo) {
			paymentMethodInfo = (PaymentMethodInfo)pSourceTelusInfoClass;
			amdocsCreditCardDetailsInfo = pTargetAmdocsInfoClass != null ? (CreditCardDetailsInfo)pTargetAmdocsInfoClass : new CreditCardDetailsInfo();

			if (paymentMethodInfo.getPaymentMethod() == null) {
				return amdocsCreditCardDetailsInfo;
			}

			// payment method is 'Regular' in the case of the top-up credit card information that
			// is 'on-hold' for pay&talk accounts
			if (paymentMethodInfo.getPaymentMethod().equals(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD) ||
					(paymentMethodInfo.getPaymentMethod().equals(PaymentMethodInfo.PAYMENT_METHOD_REGULAR) &&
							paymentMethodInfo.getStatus() != null && !paymentMethodInfo.getStatus().trim().equals(""))) {
				//PCI changes
				mapCreditCardInfo( paymentMethodInfo.getCreditCard0(), amdocsCreditCardDetailsInfo );
				amdocsCreditCardDetailsInfo.directDebitStatus = paymentMethodInfo.getStatus() == null ? PaymentMethodInfo.DIRECT_DEBIT_STATUS_ACTIVE : paymentMethodInfo.getStatus().trim();
				amdocsCreditCardDetailsInfo.reasonCode = paymentMethodInfo.getStatusReason() == null ? PaymentMethodInfo.DIRECT_DEBIT_REASON_CHANGE_BILL_DATA : paymentMethodInfo.getStatusReason();
				amdocsCreditCardDetailsInfo.startDate = paymentMethodInfo.getStartDate();
				amdocsCreditCardDetailsInfo.endDate = paymentMethodInfo.getEndDate();
			}
			return amdocsCreditCardDetailsInfo;
		}
		// map paymentMethodInfo -> amdocsCheckDetailsInfo
		//     -------------------------------------------
		if ((pSourceTelusInfoClass instanceof PaymentMethodInfo) &&
				pTargetAmdocsInfoClass instanceof CheckDetailsInfo) {
			paymentMethodInfo = (PaymentMethodInfo)pSourceTelusInfoClass;
			amdocsCheckDetailsInfo = pTargetAmdocsInfoClass != null ? (CheckDetailsInfo)pTargetAmdocsInfoClass : new amdocs.APILink.datatypes.CheckDetailsInfo();

			if (paymentMethodInfo.getPaymentMethod() == null) {
				return amdocsCheckDetailsInfo;
			}

			if (paymentMethodInfo.getPaymentMethod().equals(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT) ||
					(paymentMethodInfo.getPaymentMethod().equals(PaymentMethodInfo.PAYMENT_METHOD_REGULAR) &&
							paymentMethodInfo.getStatus() != null && !paymentMethodInfo.getStatus().trim().equals(""))) {
				if (paymentMethodInfo.getCheque0().getBankAccount0().getBankAccountNumber() != null) {
					amdocsCheckDetailsInfo.bankAccountType = AttributeTranslator.byteFromString(paymentMethodInfo.getCheque0().getBankAccount0().getBankAccountType());
					amdocsCheckDetailsInfo.bankAccountNo = paymentMethodInfo.getCheque0().getBankAccount0().getBankAccountNumber();
					amdocsCheckDetailsInfo.bankBranchNumber = paymentMethodInfo.getCheque0().getBankAccount0().getBankBranchNumber();
					amdocsCheckDetailsInfo.bankCode = paymentMethodInfo.getCheque0().getBankAccount0().getBankCode();
					amdocsCheckDetailsInfo.checkNo = paymentMethodInfo.getCheque0().getChequeNumber();
				} else {
					amdocsCheckDetailsInfo.bankAccountType = AttributeTranslator.byteFromString("");
					amdocsCheckDetailsInfo.bankAccountNo = "";
					amdocsCheckDetailsInfo.bankBranchNumber = "";
					amdocsCheckDetailsInfo.bankCode = "";
					amdocsCheckDetailsInfo.checkNo = "";
				}
				amdocsCheckDetailsInfo.directDebitStatus = paymentMethodInfo.getStatus() == null ? PaymentMethodInfo.DIRECT_DEBIT_STATUS_ACTIVE : paymentMethodInfo.getStatus().trim();
			}
			return amdocsCheckDetailsInfo;
		}
		// map creditCardInfo -> amdocsCreditCardDetailsInfo
		//     ---------------------------------------------
		if ((pSourceTelusInfoClass instanceof com.telus.eas.account.info.CreditCardInfo)&&
				pTargetAmdocsInfoClass instanceof CreditCardDetailsInfo) {
			creditCardInfo = (com.telus.eas.account.info.CreditCardInfo)pSourceTelusInfoClass;
			amdocsCreditCardDetailsInfo = pTargetAmdocsInfoClass != null ? (CreditCardDetailsInfo)pTargetAmdocsInfoClass : new CreditCardDetailsInfo();
			//PCI change
			mapCreditCardInfo( creditCardInfo, amdocsCreditCardDetailsInfo );
			return amdocsCreditCardDetailsInfo;
		}

		// map chequeInfo -> amdocsCheckDetailsInfo
		//     ------------------------------------
		if ((pSourceTelusInfoClass instanceof ChequeInfo) &&
				pTargetAmdocsInfoClass instanceof CheckDetailsInfo) {
			chequeInfo = (ChequeInfo)pSourceTelusInfoClass;
			amdocsCheckDetailsInfo = pTargetAmdocsInfoClass != null ? (CheckDetailsInfo)pTargetAmdocsInfoClass : new CheckDetailsInfo();

			amdocsCheckDetailsInfo.bankCode = chequeInfo.getBankAccount0().getBankCode();
			amdocsCheckDetailsInfo.bankAccountNo = chequeInfo.getBankAccount0().getBankAccountNumber();
			amdocsCheckDetailsInfo.bankBranchNumber = chequeInfo.getBankAccount0().getBankBranchNumber();
			amdocsCheckDetailsInfo.checkNo = chequeInfo.getChequeNumber();
			return amdocsCheckDetailsInfo;
		}

		// map paymentInfo -> amdocsApplyPaymentDetailsInfo
		//     --------------------------------------------
		if ((pSourceTelusInfoClass instanceof PaymentInfo) &&
				pTargetAmdocsInfoClass instanceof ApplyPaymentDetailsInfo) {
			paymentInfo = (PaymentInfo)pSourceTelusInfoClass;
			amdocsApplyPaymentDetailsInfo = pTargetAmdocsInfoClass != null ? (ApplyPaymentDetailsInfo)pTargetAmdocsInfoClass : new ApplyPaymentDetailsInfo();

			amdocsApplyPaymentDetailsInfo.allowOverPayment = paymentInfo.isAllowOverpayment();
			amdocsApplyPaymentDetailsInfo.banNumber = paymentInfo.getBan();
			amdocsApplyPaymentDetailsInfo.depDate = paymentInfo.getDepositDate();
			amdocsApplyPaymentDetailsInfo.depositPaymentIndicator = paymentInfo.isDepositPaymentIndicator();
			amdocsApplyPaymentDetailsInfo.paymentAmount = paymentInfo.getAmount();
			amdocsApplyPaymentDetailsInfo.paymentSourceType = paymentInfo.getPaymentSourceType();
			amdocsApplyPaymentDetailsInfo.sourceId = paymentInfo.getPaymentSourceID();

			return amdocsApplyPaymentDetailsInfo;
		}


		// map accountInfo -> amdocsNationalGrowthInfo
		//     ---------------------------------------
		if ((pSourceTelusInfoClass instanceof AccountInfo) &&
				pTargetAmdocsInfoClass instanceof NationalGrowthInfo) {
			accountInfo = (AccountInfo)pSourceTelusInfoClass;
			amdocsNationalGrowthInfo = pTargetAmdocsInfoClass != null ? (NationalGrowthInfo)pTargetAmdocsInfoClass : new NationalGrowthInfo();

			amdocsNationalGrowthInfo.homeProvince =
				AttributeTranslator.emptyFromNull(
						accountInfo.getHomeProvince()).equals(
						"")
						? "ON"
								: accountInfo.getHomeProvince();            // default 'Ontario'


			// if home province is not set, only set home province if address is in Canada
			// otherwise it is defaulted to 'Ontario'
			// (will be updated with first activated subscriber's phone number province)

			// print(getClass().getName(),methodName,"AccountInfo.."+ accountInfo);
			// print(getClass().getName(),methodName,"accountInfo.getHomeProvince():"+ accountInfo.getHomeProvince());
			if (AttributeTranslator.emptyFromNull(accountInfo.getHomeProvince()).equals("") &&
					AttributeTranslator.emptyFromNull(accountInfo.getAddress0().getCountry()).equals("CAN")) {
				if (accountInfo.getAddress0().getProvince() != null &&
						accountInfo.getAddress0().getProvince().equals("QC")) {
					amdocsNationalGrowthInfo.homeProvince = "PQ";
				}
				else if (accountInfo.getAddress0().getProvince() != null &&
						accountInfo.getAddress0().getProvince().equals("NL")) {
					amdocsNationalGrowthInfo.homeProvince = "NF";
				}
				else {
					amdocsNationalGrowthInfo.homeProvince = accountInfo.getAddress0().
					getProvince();
				}
			}

			amdocsNationalGrowthInfo.nationalAccount =
				AttributeTranslator.emptyFromNull(
						accountInfo.getAccountCategory()).equals(
						"")
						? (byte) 'R'
								: AttributeTranslator.byteFromString(
										accountInfo.getAccountCategory());            // default 'Regional'

						return amdocsNationalGrowthInfo;
		}

		//		      map CPUIInfo -> amdocsCPUIInfo
		//     ---------------------------------
		if ((pSourceTelusInfoClass instanceof AccountInfo) &&
				pTargetAmdocsInfoClass instanceof CPUIInfo) {
			accountInfo = (AccountInfo)pSourceTelusInfoClass;

			ArrayList<CPUIInfo> list = new  ArrayList<CPUIInfo>(); 

			String [] codes = accountInfo.getClientConsentIndicatorCodes();
			if (codes != null)
			{
				for (int i=0;i<codes.length;i++) 
				{
					amdocsCPUIInfo =  new CPUIInfo();	
					amdocsCPUIInfo.cpuiCd = codes[i]; 
					list.add(amdocsCPUIInfo);
				}
			}	
			else 
				list.add(new CPUIInfo());

			return (CPUIInfo[])list.toArray(new CPUIInfo[list.size()]);
		}

		// map creditCheckResultInfo -> amdocsCreditResultExtInfo
		//     --------------------------------------------------
		if ((pSourceTelusInfoClass instanceof CreditCheckResultInfo) &&
				pTargetAmdocsInfoClass instanceof CreditResultExtInfo) {

			creditCheckResultInfo = (CreditCheckResultInfo)pSourceTelusInfoClass;
			amdocsCreditResultExtInfo = pTargetAmdocsInfoClass != null ? (CreditResultExtInfo)pTargetAmdocsInfoClass : new CreditResultExtInfo();
			amdocsCreditResultExtInfo.creditClass = AttributeTranslator.byteFromString(creditCheckResultInfo.getCreditClass());
			amdocsCreditResultExtInfo.creditLimit = creditCheckResultInfo.getLimit();
			amdocsCreditResultExtInfo.creditResult2 = creditCheckResultInfo.getMessage();

			// if null - Amdocs service fails.
			if (amdocsCreditResultExtInfo.creditResult2 == null) {
				amdocsCreditResultExtInfo.creditResult2 = "";
			}

			amdocsCreditResultExtInfo.beaconScore = creditCheckResultInfo.getCreditScore();
			amdocsCreditResultExtInfo.transAlert1 = emptyFromNull(creditCheckResultInfo.getTransAlert1());
			amdocsCreditResultExtInfo.transAlert2 = emptyFromNull(creditCheckResultInfo.getTransAlert2());
			amdocsCreditResultExtInfo.transAlert3 = emptyFromNull(creditCheckResultInfo.getTransAlert3());
			amdocsCreditResultExtInfo.transAlert4 = emptyFromNull(creditCheckResultInfo.getTransAlert4());
			amdocsCreditResultExtInfo.tuReturnInd = emptyFromNull(creditCheckResultInfo.getTuReturnInd());
			amdocsCreditResultExtInfo.node = emptyFromNull(creditCheckResultInfo.getNode());
			amdocsCreditResultExtInfo.performExistNegInd = AttributeTranslator.byteFromString(creditCheckResultInfo.getExistingNegInd());
			amdocsCreditResultExtInfo.performHawkInd = AttributeTranslator.byteFromString(creditCheckResultInfo.getHawkAlertInd());

			String[] stringArray = null;
			if (creditCheckResultInfo.getErrorCode() > 0) {
				stringArray = AttributeTranslator.stringArraryFromString(emptyFromNull(creditCheckResultInfo.getErrorMessage()).trim(),1000);
			}
			else {
				stringArray = AttributeTranslator.stringArraryFromString(emptyFromNull(creditCheckResultInfo.getBureauFile()).trim(),1000);
			}

			// need to initialize or we get tuxedo failure (Failed to build request for service csCrManCrd)
			CreditResultExt1[] creditResultExt1s = new CreditResultExt1[0];
			amdocsCreditResultExtInfo.creditResultExt1 = creditResultExt1s;

			CreditResultExt2[] amdocsCreditResultExt2s = new CreditResultExt2[stringArray.length];
			for (int i=0; i < stringArray.length; i++) {
				amdocsCreditResultExt2s[i] = new CreditResultExt2();
				amdocsCreditResultExt2s[i].txtResult = stringArray[i];
			}
			amdocsCreditResultExtInfo.creditResultExt2 = amdocsCreditResultExt2s;

			if (creditCheckResultInfo.getDeposits() != null) {
				CreditResultDeposit[] amdocsCreditResultDeposits = new CreditResultDeposit[creditCheckResultInfo.getDeposits().length];
				for (int i=0; i < creditCheckResultInfo.getDeposits().length; i++) {
					amdocsCreditResultDeposits[i] = new CreditResultDeposit();
					amdocsCreditResultDeposits[i].depositAmt = creditCheckResultInfo.getDeposits()[i].getDeposit();
					amdocsCreditResultDeposits[i].depositArrProductType = AttributeTranslator.byteFromString(creditCheckResultInfo.getDeposits()[i].getProductType());
				}
				amdocsCreditResultExtInfo.creditResultDeposit = amdocsCreditResultDeposits;
			}
			else {
				amdocsCreditResultExtInfo.creditResultDeposit = new CreditResultDeposit[0];
			}

			return amdocsCreditResultExtInfo;
		}

		// map personalCreditInfo -> amdocsCreditResultExtInfo
		//     -----------------------------------------------
		if ((pSourceTelusInfoClass instanceof PersonalCreditInfo) &&
				pTargetAmdocsInfoClass instanceof CreditResultExtInfo) {
			personalCreditInfo = (PersonalCreditInfo)pSourceTelusInfoClass;
			amdocsCreditResultExtInfo = pTargetAmdocsInfoClass != null ? (CreditResultExtInfo)pTargetAmdocsInfoClass : new CreditResultExtInfo();

			if (personalCreditInfo.getBirthDate() != null) amdocsCreditResultExtInfo.dateOfBirth = AttributeTranslator.stringFromDate(personalCreditInfo.getBirthDate(),"yyyyMMdd");
			if (personalCreditInfo.getSin() != null)
				if (personalCreditInfo.getSin().trim().equals(""))
					amdocsCreditResultExtInfo.sin = 0;
				else{
					try{
						amdocsCreditResultExtInfo.sin = Integer.parseInt(personalCreditInfo.getSin());
					}catch(java.lang.NumberFormatException nfe){
						throw new ApplicationException(SystemCodes.CMB_CMN_T2A,"Number Format Exception, SIN can only have numeric value","");
					}
				}
			//PCI changes
			mapCreditCardInfo ( personalCreditInfo.getCreditCard0(), amdocsCreditResultExtInfo );
			return amdocsCreditResultExtInfo;
		}

		// map businessCreditInfo -> amdocsCreditResultExtInfo
		//     -----------------------------------------------
		if ((pSourceTelusInfoClass instanceof BusinessCreditInfo) &&
				pTargetAmdocsInfoClass instanceof CreditResultExtInfo) {
			businessCreditInfo = (BusinessCreditInfo)pSourceTelusInfoClass;
			amdocsCreditResultExtInfo = pTargetAmdocsInfoClass != null ? (CreditResultExtInfo)pTargetAmdocsInfoClass : new CreditResultExtInfo();

			if (businessCreditInfo.getIncorporationDate() != null) amdocsCreditResultExtInfo.incorporationDate =  AttributeTranslator.stringFromDate(businessCreditInfo.getIncorporationDate(),"yyyyMMdd");
			if (businessCreditInfo.getIncorporationNumber() != null) amdocsCreditResultExtInfo.incorporationNumber = businessCreditInfo.getIncorporationNumber();
			return amdocsCreditResultExtInfo;
		}
		return null;
	}

	private static  String nullFromEmpty(String pString) {
		if (pString == null) return null;
		if (pString.equals(TUXEDO_NULL_STRING) || pString.trim().equals("")) return null;
		return pString;
	}
	private static  String emptyFromNull(String pString) {
		if ((pString == null) || (pString.toUpperCase().equals("NULL")) )
			return "";
		return pString;
	}

	private static	void mapCreditCardInfo ( com.telus.eas.account.info.CreditCardInfo telusCreditCardInfo, 
			amdocs.APILink.datatypes.CreditResultExtInfo amdocsCreditResultExtInfo) {
		if (telusCreditCardInfo.getExpiryDate() != null) amdocsCreditResultExtInfo.creditCardExpiryDate = AttributeTranslator.stringFromDate(telusCreditCardInfo.getExpiryDate(),"yyyyMMdd");
		if (telusCreditCardInfo.getToken() != null) {
			amdocsCreditResultExtInfo.creditCardNo = telusCreditCardInfo.getToken();
			//only when has token, then we set the other first 6 and last 4 digits
			if (telusCreditCardInfo.getLeadingDisplayDigits() != null) amdocsCreditResultExtInfo.ccFirstSixDigits = telusCreditCardInfo.getLeadingDisplayDigits();
			if (telusCreditCardInfo.getTrailingDisplayDigits() != null) amdocsCreditResultExtInfo.ccLastFourDigits = telusCreditCardInfo.getTrailingDisplayDigits();
		}
	}
	private static void mapCreditCardInfo ( com.telus.eas.account.info.CreditCardInfo telusCreditCardInfo, 
			amdocs.APILink.datatypes.CreditCardDetailsInfo amdocsCreditCardDetailsInfo) {

		if ( telusCreditCardInfo.hasToken()) {
			amdocsCreditCardDetailsInfo.creditCardType = telusCreditCardInfo.getType();
			amdocsCreditCardDetailsInfo.creditCardExpDate = telusCreditCardInfo.getExpiryDate();
			amdocsCreditCardDetailsInfo.creditCardNo = telusCreditCardInfo.getToken();
			amdocsCreditCardDetailsInfo.cardMemberName = AttributeTranslator.emptyFromNull(telusCreditCardInfo.getHolderName());
			amdocsCreditCardDetailsInfo.creditCardAuthCode = telusCreditCardInfo.getAuthorizationCode();
			amdocsCreditCardDetailsInfo.ccFirstSixDigits = telusCreditCardInfo.getLeadingDisplayDigits();
			amdocsCreditCardDetailsInfo.ccLastFourDigits = telusCreditCardInfo.getTrailingDisplayDigits();
		} else {
			amdocsCreditCardDetailsInfo.creditCardType = "";
			amdocsCreditCardDetailsInfo.creditCardNo = "";
			amdocsCreditCardDetailsInfo.creditCardAuthCode = "";
			amdocsCreditCardDetailsInfo.cardMemberName = "";
			amdocsCreditCardDetailsInfo.ccFirstSixDigits = "";
			amdocsCreditCardDetailsInfo.ccLastFourDigits = "";
		}
	}

	private static   void mapCreditCardInfo ( com.telus.eas.account.info.CreditCardInfo telusCreditCardInfo, 
			amdocs.APILink.datatypes.CreditCardInfo amdocsCreditCardInfo) {
		if (telusCreditCardInfo.getExpiryDate() != null) amdocsCreditCardInfo.creditCardExpDt = AttributeTranslator.stringFromDate(telusCreditCardInfo.getExpiryDate(),"MM/yyyy");
		if (telusCreditCardInfo.getType() != null) amdocsCreditCardInfo.creditCardType = telusCreditCardInfo.getType();
		if (telusCreditCardInfo.getToken() != null) {
			amdocsCreditCardInfo.creditCardNum = telusCreditCardInfo.getToken();
			//only when has token, then we set the other first 6 and last 4 digits
			if (telusCreditCardInfo.getLeadingDisplayDigits() != null) amdocsCreditCardInfo.ccFirstSixDigits = telusCreditCardInfo.getLeadingDisplayDigits();
			if (telusCreditCardInfo.getTrailingDisplayDigits() != null) amdocsCreditCardInfo.ccLastFourDigits = telusCreditCardInfo.getTrailingDisplayDigits();
		}
	}
	
	private static void mapFromAmdocsAddressInfo(amdocs.APILink.datatypes.AddressInfo amdocsAddressInfo, AddressInfo addressInfo) {
		if (amdocsAddressInfo != null && addressInfo != null) {
			addressInfo.setAttention(nullFromEmpty(amdocsAddressInfo.attention));
			addressInfo.setPrimaryLine(nullFromEmpty(amdocsAddressInfo.primaryLine));
			addressInfo.setSecondaryLine(nullFromEmpty(amdocsAddressInfo.secondaryLine));
			addressInfo.setCity(nullFromEmpty(amdocsAddressInfo.city));
			addressInfo.setProvince(nullFromEmpty(amdocsAddressInfo.province));
			addressInfo.setPostalCode(nullFromEmpty(amdocsAddressInfo.postalCode));
			addressInfo.setCivicNo(nullFromEmpty(amdocsAddressInfo.civicNo));
			addressInfo.setCivicNoSuffix(nullFromEmpty(amdocsAddressInfo.civicNoSuffix));
			addressInfo.setStreetDirection(nullFromEmpty(amdocsAddressInfo.streetDirection));
			addressInfo.setStreetName(nullFromEmpty(amdocsAddressInfo.streetName));
			addressInfo.setStreetType(nullFromEmpty(amdocsAddressInfo.streetType));
			addressInfo.setUnitDesignator(nullFromEmpty(amdocsAddressInfo.unitDesignator));
			addressInfo.setUnitIdentifier(nullFromEmpty(amdocsAddressInfo.unitIdentifier));
			addressInfo.setRrAreaNumber(nullFromEmpty(amdocsAddressInfo.rrAreaNumber));
			addressInfo.setRrBox(nullFromEmpty(amdocsAddressInfo.rrBox));
			addressInfo.setRrCompartment(nullFromEmpty(amdocsAddressInfo.rrCompartment));
			addressInfo.setRrDeliveryType(nullFromEmpty(amdocsAddressInfo.rrDeliveryType));
			addressInfo.setRrDesignator(nullFromEmpty(amdocsAddressInfo.rrDesignator));
			addressInfo.setRrGroup(nullFromEmpty(amdocsAddressInfo.rrGroup));
			addressInfo.setRrIdentifier(nullFromEmpty(amdocsAddressInfo.rrIdentifier));
			addressInfo.setRrQualifier(nullFromEmpty(amdocsAddressInfo.rrQualifier));
			addressInfo.setRrSite(nullFromEmpty(amdocsAddressInfo.rrSite));
			addressInfo.setRrCompartment(nullFromEmpty(amdocsAddressInfo.rrCompartment));
			addressInfo.setCountry(nullFromEmpty(amdocsAddressInfo.country));
			addressInfo.setZipGeoCode(nullFromEmpty(amdocsAddressInfo.zipGeoCode));
			addressInfo.setForeignState(nullFromEmpty(amdocsAddressInfo.foreignState));
			addressInfo.setAddressType(AttributeTranslator.stringFrombyte(amdocsAddressInfo.type));
		}
	}

	private static void mapFromAmdocsConsumerNameInfo(amdocs.APILink.datatypes.NameInfo amdocsNameInfo, ConsumerNameInfo consumerNameInfo) {
		if (amdocsNameInfo != null && consumerNameInfo != null) {
			consumerNameInfo.setAdditionalLine(amdocsNameInfo.additionalTitle);
			consumerNameInfo.setFirstName(amdocsNameInfo.firstName);
			consumerNameInfo.setLastName(amdocsNameInfo.lastBusinessName);
			consumerNameInfo.setMiddleInitial(amdocsNameInfo.middleInitial);
			consumerNameInfo.setTitle(amdocsNameInfo.nameTitle);
			consumerNameInfo.setGeneration(amdocsNameInfo.nameSuffix);
		}
	}
	
	private static void mapFromAmdocsDuplicateSearchResult(SearchDuplicateResult amdocsSearchDuplicateResult, DuplicateBillingAccountInfo duplicateBillingAccountInfo) {
		if (amdocsSearchDuplicateResult != null && duplicateBillingAccountInfo != null) {
			duplicateBillingAccountInfo.setBan(amdocsSearchDuplicateResult.ban);
			duplicateBillingAccountInfo.setAccountType(AttributeTranslator.stringFrombyte(amdocsSearchDuplicateResult.accountType));
			duplicateBillingAccountInfo.setAccountSubType(AttributeTranslator.stringFrombyte(amdocsSearchDuplicateResult.accountSubType));
			duplicateBillingAccountInfo.setBusinessType(nullFromEmpty(amdocsSearchDuplicateResult.businessType));
			duplicateBillingAccountInfo.setCustomerSIN(amdocsSearchDuplicateResult.customerSsn);
			duplicateBillingAccountInfo.setCustomerID(amdocsSearchDuplicateResult.customerId);
			duplicateBillingAccountInfo.setCompanyName(nullFromEmpty(amdocsSearchDuplicateResult.cstCompname1));
			duplicateBillingAccountInfo.setCompositeAddress(nullFromEmpty(amdocsSearchDuplicateResult.compaddr));
			duplicateBillingAccountInfo.setIncorporationNumber(nullFromEmpty(amdocsSearchDuplicateResult.incorporationNo));
			duplicateBillingAccountInfo.setIncorporationDate(nullFromEmpty(amdocsSearchDuplicateResult.incorporationDate));
		}
	}
	
	private static void mapFromAmdocsCreditCheckResult(CreditResultInfo amdocsCreditResultInfo, CreditCheckResultInfo creditCheckResultInfo) {
		if (amdocsCreditResultInfo != null && creditCheckResultInfo != null) {
			creditCheckResultInfo.setCreditClass(AttributeTranslator.stringFrombyte(amdocsCreditResultInfo.creditClass));
			creditCheckResultInfo.setLimit(amdocsCreditResultInfo.creditLimit);
			creditCheckResultInfo.setMessage(nullFromEmpty(amdocsCreditResultInfo.creditResult2));
			creditCheckResultInfo.setCreditScore(amdocsCreditResultInfo.beaconScore);
			mapFromAmdocsCreditCheckResultDepositInfo (amdocsCreditResultInfo.creditResultDeposit, creditCheckResultInfo);
			mapFromAmdocsCreditBureauFile (amdocsCreditResultInfo.creditResultExt2, creditCheckResultInfo);
		}
	}
	
	private static void mapFromAmdocsCreditCheckResultDepositInfo (CreditResultDeposit[] amdocsCreditResultDeposit, CreditCheckResultInfo creditCheckResultInfo) {
		if (amdocsCreditResultDeposit != null && amdocsCreditResultDeposit.length > 0) {
			CreditCheckResultDepositInfo[] creditCheckResultDeposits = new CreditCheckResultDepositInfo[amdocsCreditResultDeposit.length];

			for (int i = 0; i < amdocsCreditResultDeposit.length && amdocsCreditResultDeposit[i] != null;i++) {
				creditCheckResultDeposits[i] = new CreditCheckResultDepositInfo();
				creditCheckResultDeposits[i].setDeposit(amdocsCreditResultDeposit[i].depositAmt);
				creditCheckResultDeposits[i].setProductType(AttributeTranslator.stringFrombyte(amdocsCreditResultDeposit[i].depositArrProductType));
			}
			creditCheckResultInfo.setDeposits(creditCheckResultDeposits);
		}
	}
	
	private static void mapFromAmdocsCreditBureauFile(CreditResultExt2[] amdocsCreditResultExt2s, CreditCheckResultInfo creditCheckResultInfo) {
		String bureauFile = "";
		for (int i = 0; i < (amdocsCreditResultExt2s != null ? amdocsCreditResultExt2s.length : 0); i++) {
			if (amdocsCreditResultExt2s[i] != null) {
				bureauFile += amdocsCreditResultExt2s[i].txtResult;
			} else {
				break;
			}
		}
		creditCheckResultInfo.setBureauFile(bureauFile);
	}
	
	private static void mapFromAmdocsBusinessListInfo(BusinessListInfo amdocsBusinessListInfo, BusinessCreditIdentityInfo businessCreditIdentityInfo) {
		if (amdocsBusinessListInfo != null && businessCreditIdentityInfo != null) {
			businessCreditIdentityInfo.setCompanyName(nullFromEmpty(amdocsBusinessListInfo.companyName));
			businessCreditIdentityInfo.setMarketAccount(amdocsBusinessListInfo.marketAccount);
		}
	}
}
