package com.telus.cmb.account.utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.AccountTypeInfo;
import amdocs.APILink.datatypes.ApplyPaymentDetailsInfo;
import amdocs.APILink.datatypes.BillDetailsInfo;
import amdocs.APILink.datatypes.BusinessInfo;
import amdocs.APILink.datatypes.CPUIInfo;
import amdocs.APILink.datatypes.CheckDetailsInfo;
import amdocs.APILink.datatypes.ContactInfo;
import amdocs.APILink.datatypes.ContractInfo;
import amdocs.APILink.datatypes.CreditCardDetailsInfo;
import amdocs.APILink.datatypes.CreditResultDeposit;
import amdocs.APILink.datatypes.CreditResultExt1;
import amdocs.APILink.datatypes.CreditResultExt2;
import amdocs.APILink.datatypes.CreditResultExtInfo;
import amdocs.APILink.datatypes.CycleInfo;
import amdocs.APILink.datatypes.IdentificationInfo;
import amdocs.APILink.datatypes.NameInfo;
import amdocs.APILink.datatypes.NationalGrowthInfo;
import amdocs.APILink.datatypes.SpecialBillDetailsInfo;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.InvoiceProperties;
import com.telus.api.reference.AccountType;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.ChequeInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PostpaidBoxedConsumerAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessPersonalAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.framework.info.Info;

public class TelusToAmdocsAccountMapper {
	private static final Logger LOGGER = Logger.getLogger(TelusToAmdocsAccountMapper.class);

	public static HashMap<Object,Object> mapAccountInfoToAmdocs(AccountInfo pAccountInfo, HashMap<Object,Object> amdocsClasses) throws ApplicationException {
		// Telus Info classes
		PostpaidConsumerAccountInfo postpaidConsumerAccountInfo = null;
		PostpaidBusinessPersonalAccountInfo postpaidBusinessPersonalAccountInfo = null;
		PostpaidBusinessRegularAccountInfo postpaidBusinessRegularAccountInfo = null;
		PrepaidConsumerAccountInfo prepaidConsumerAccountInfo = null;
		PostpaidBoxedConsumerAccountInfo postpaidBoxedConsumerAccountInfo = null;

		// intialize amdocsClasses if necessary
		if (amdocsClasses == null) amdocsClasses = initializeAmdocsClassesHash(amdocsClasses);

	      // get amdocs classes 
		AccountTypeInfo amdocsAccountTypeInfo = (AccountTypeInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new AccountTypeInfo().getClass()));
		NameInfo amdocsBillingNameInfo = (NameInfo)amdocsClasses.get("Billing" +  AttributeTranslator.getBaseName(new NameInfo().getClass()));
		NameInfo amdocsContactNameInfo = (NameInfo)amdocsClasses.get("Contact" +  AttributeTranslator.getBaseName(new NameInfo().getClass()));
		amdocs.APILink.datatypes.AddressInfo amdocsBillingAddressInfo = (amdocs.APILink.datatypes.AddressInfo)amdocsClasses.get("Billing" +  AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.AddressInfo().getClass()));
		amdocs.APILink.datatypes.AddressInfo amdocsCreditCheckAddressInfo = (amdocs.APILink.datatypes.AddressInfo)amdocsClasses.get("CreditCheck" +  AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.AddressInfo().getClass()));
		IdentificationInfo amdocsIdentificationInfo = (IdentificationInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new IdentificationInfo().getClass()));
		ContractInfo amdocsContractInfo = (ContractInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new ContractInfo().getClass()));
		ContactInfo amdocsContactInfo = (ContactInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new ContactInfo().getClass()));
		amdocs.APILink.datatypes.CreditCardInfo amdocsCreditCardInfo = (amdocs.APILink.datatypes.CreditCardInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.CreditCardInfo().getClass()));
		CreditCardDetailsInfo amdocsCreditCardDetailsInfo = (CreditCardDetailsInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new CreditCardDetailsInfo().getClass()));
		CheckDetailsInfo amdocsCheckDetailsInfo = (CheckDetailsInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new CheckDetailsInfo().getClass()));
		CycleInfo amdocsCycleInfo = (CycleInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new CycleInfo().getClass()));
		BusinessInfo amdocsBusinessInfo = (BusinessInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new BusinessInfo().getClass()));
		BillDetailsInfo amdocsBillDetailsInfo = (BillDetailsInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new BillDetailsInfo().getClass()));
		SpecialBillDetailsInfo amdocsSpecialBillDetailsInfo = (SpecialBillDetailsInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new SpecialBillDetailsInfo().getClass()));
		NationalGrowthInfo amdocsNationalGrowthInfo = (NationalGrowthInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new NationalGrowthInfo().getClass()));
		CPUIInfo amdocsCPUIInfo = (CPUIInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new CPUIInfo().getClass()));
		amdocs.APILink.datatypes.TaxExemptionInfo amdocsAccountTaxExemptionInfo = (amdocs.APILink.datatypes.TaxExemptionInfo)amdocsClasses.get( AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.TaxExemptionInfo().getClass()));

		// populate Amdocs Info classes based on type of account
		// - all account types
		amdocsAccountTypeInfo = (amdocs.APILink.datatypes.AccountTypeInfo)mapTelusToAmdocs(pAccountInfo,amdocsAccountTypeInfo);
		amdocsBillingAddressInfo = (amdocs.APILink.datatypes.AddressInfo)mapTelusToAmdocs(pAccountInfo.getAddress0(),amdocsBillingAddressInfo);
		amdocsCreditCheckAddressInfo = (amdocs.APILink.datatypes.AddressInfo)mapTelusToAmdocs(pAccountInfo.getAlternateCreditCheckAddress0(),amdocsCreditCheckAddressInfo);
		amdocsIdentificationInfo = (IdentificationInfo)mapTelusToAmdocs(pAccountInfo,amdocsIdentificationInfo);
		amdocsContractInfo = (amdocs.APILink.datatypes.ContractInfo)mapTelusToAmdocs(pAccountInfo,amdocsContractInfo);
		amdocsCycleInfo = (amdocs.APILink.datatypes.CycleInfo)mapTelusToAmdocs(pAccountInfo,amdocsCycleInfo);
		amdocsBillDetailsInfo = (BillDetailsInfo)mapTelusToAmdocs(pAccountInfo,amdocsBillDetailsInfo);
		amdocsSpecialBillDetailsInfo = (SpecialBillDetailsInfo)mapTelusToAmdocs(pAccountInfo,amdocsSpecialBillDetailsInfo);
		amdocsNationalGrowthInfo = (NationalGrowthInfo)mapTelusToAmdocs(pAccountInfo,amdocsNationalGrowthInfo);
		amdocsContactInfo = (ContactInfo)mapTelusToAmdocs(pAccountInfo,amdocsContactInfo);
		CPUIInfo[] amdocsCPUIArrayInfo = (CPUIInfo[])mapTelusToAmdocs(pAccountInfo,amdocsCPUIInfo);
		amdocsAccountTaxExemptionInfo = (amdocs.APILink.datatypes.TaxExemptionInfo)mapTelusToAmdocs(pAccountInfo,amdocsAccountTaxExemptionInfo);

		// - postpaid consumer or postpaid employee
		if (pAccountInfo.isPostpaidConsumer() || pAccountInfo.isPostpaidEmployee()) {
			postpaidConsumerAccountInfo = (PostpaidConsumerAccountInfo)pAccountInfo;

	        amdocsBillingNameInfo = (NameInfo)mapTelusToAmdocs(postpaidConsumerAccountInfo.getName0(), amdocsBillingNameInfo);
	        amdocsContactNameInfo = (NameInfo)mapTelusToAmdocs(AttributeTranslator.emptyFromNull(postpaidConsumerAccountInfo.getContactName0().getLastName()).trim().equals("")
	        		? postpaidConsumerAccountInfo.getName0() : postpaidConsumerAccountInfo.getContactName0(),amdocsContactNameInfo);
	        amdocsIdentificationInfo = (amdocs.APILink.datatypes.IdentificationInfo)mapTelusToAmdocs(postpaidConsumerAccountInfo.getPersonalCreditInformation0(), amdocsIdentificationInfo);
	        amdocsCreditCardInfo = (amdocs.APILink.datatypes.CreditCardInfo)mapTelusToAmdocs(postpaidConsumerAccountInfo.getPersonalCreditInformation0(), amdocsCreditCardInfo);
	        amdocsCreditCardDetailsInfo = (amdocs.APILink.datatypes.CreditCardDetailsInfo)mapTelusToAmdocs(postpaidConsumerAccountInfo.getPaymentMethod0(), amdocsCreditCardDetailsInfo);
	        amdocsCheckDetailsInfo = (CheckDetailsInfo)mapTelusToAmdocs(postpaidConsumerAccountInfo.getPaymentMethod0(), amdocsCheckDetailsInfo);
		}
		// - postpaid business personal or corporate personal only
		if (pAccountInfo.isPostpaidBusinessPersonal() || pAccountInfo.isPostpaidCorporatePersonal()) {
			postpaidBusinessPersonalAccountInfo = (PostpaidBusinessPersonalAccountInfo)pAccountInfo;

	        amdocsBillingNameInfo = (NameInfo)mapTelusToAmdocs(postpaidBusinessPersonalAccountInfo.getName0(), amdocsBillingNameInfo);
	        amdocsContactNameInfo = (NameInfo)mapTelusToAmdocs(AttributeTranslator.emptyFromNull(postpaidBusinessPersonalAccountInfo.getContactName0().getLastName()).trim().equals("")
	        		? postpaidBusinessPersonalAccountInfo.getName0() : postpaidBusinessPersonalAccountInfo.getContactName0(), amdocsContactNameInfo);
	        amdocsIdentificationInfo = (amdocs.APILink.datatypes.IdentificationInfo)mapTelusToAmdocs(postpaidBusinessPersonalAccountInfo.getPersonalCreditInformation0(), amdocsIdentificationInfo);
	        amdocsCreditCardInfo = (amdocs.APILink.datatypes.CreditCardInfo)mapTelusToAmdocs(postpaidBusinessPersonalAccountInfo.getPersonalCreditInformation0(), amdocsCreditCardInfo);
	        amdocsCreditCardDetailsInfo = (amdocs.APILink.datatypes.CreditCardDetailsInfo)mapTelusToAmdocs(postpaidBusinessPersonalAccountInfo.getPaymentMethod0(), amdocsCreditCardDetailsInfo);
	        amdocsCheckDetailsInfo = (CheckDetailsInfo)mapTelusToAmdocs(postpaidBusinessPersonalAccountInfo.getPaymentMethod0(), amdocsCheckDetailsInfo);
		}
		// - postpaid business regular, business dealer or corporate regular
		if (pAccountInfo.isPostpaidBusinessRegular() || pAccountInfo.isPostpaidBusinessDealer() || pAccountInfo.isPostpaidCorporateRegular() ||
				pAccountInfo.getBillingNameFormat() == AccountType.BILLING_NAME_FORMAT_BUSINESS) {
			postpaidBusinessRegularAccountInfo = (PostpaidBusinessRegularAccountInfo)pAccountInfo;

	        amdocsBillingNameInfo.lastBusinessName = AttributeTranslator.emptyFromNull(postpaidBusinessRegularAccountInfo.getLegalBusinessName());
	        amdocsBillingNameInfo.additionalTitle = AttributeTranslator.emptyFromNull(postpaidBusinessRegularAccountInfo.getTradeNameAttention());
	        amdocsContactNameInfo = (NameInfo)mapTelusToAmdocs(postpaidBusinessRegularAccountInfo.getContactName0(), amdocsContactNameInfo);        
	        amdocsIdentificationInfo = (amdocs.APILink.datatypes.IdentificationInfo)mapTelusToAmdocs(postpaidBusinessRegularAccountInfo.getPersonalCreditInformation0(), amdocsIdentificationInfo);        
	        amdocsBusinessInfo = (BusinessInfo)mapTelusToAmdocs(postpaidBusinessRegularAccountInfo.getCreditInformation0(), amdocsBusinessInfo);
	        amdocsCreditCardDetailsInfo = (amdocs.APILink.datatypes.CreditCardDetailsInfo)mapTelusToAmdocs(postpaidBusinessRegularAccountInfo.getPaymentMethod0(), amdocsCreditCardDetailsInfo);
	        amdocsCheckDetailsInfo = (CheckDetailsInfo)mapTelusToAmdocs(postpaidBusinessRegularAccountInfo.getPaymentMethod0(), amdocsCheckDetailsInfo);
		}
		// - prepaid consumer
		if (!pAccountInfo.isPostpaid()) {
			prepaidConsumerAccountInfo = (PrepaidConsumerAccountInfo)pAccountInfo;

			amdocsBillingNameInfo = (NameInfo)mapTelusToAmdocs(prepaidConsumerAccountInfo.getName0(), amdocsBillingNameInfo);
	        amdocsContactNameInfo = (NameInfo)mapTelusToAmdocs(AttributeTranslator.emptyFromNull(prepaidConsumerAccountInfo.getContactName0().getLastName()).trim().equals("") 
	        		? prepaidConsumerAccountInfo.getName0() : prepaidConsumerAccountInfo.getContactName0(), amdocsContactNameInfo);
	        //PCI changes
	        mapCreditCardInfo ( prepaidConsumerAccountInfo.getTopUpCreditCard0(), amdocsCreditCardDetailsInfo );
	        amdocsCreditCardDetailsInfo.directDebitStatus = PaymentMethodInfo.DIRECT_DEBIT_STATUS_HOLD;
	        amdocsCreditCardDetailsInfo.reasonCode = PaymentMethodInfo.DIRECT_DEBIT_REASON_CHANGE_TO_DD;
	        amdocsCreditCardDetailsInfo.startDate = new java.util.Date();
	        amdocsCreditCardDetailsInfo.creditCardType = prepaidConsumerAccountInfo.getTopUpCreditCard0().getType();
	      }

		// - Box Pager consumer only
		if (pAccountInfo.isPostpaidBoxedConsumer()) {
	        postpaidBoxedConsumerAccountInfo = (PostpaidBoxedConsumerAccountInfo)pAccountInfo;

	        amdocsBillingNameInfo = (NameInfo)mapTelusToAmdocs(postpaidBoxedConsumerAccountInfo.getName0(), amdocsBillingNameInfo);
	        amdocsContactNameInfo = (NameInfo)mapTelusToAmdocs(AttributeTranslator.emptyFromNull(postpaidBoxedConsumerAccountInfo.getContactName0().getLastName()).trim().equals("") 
	        		? postpaidBoxedConsumerAccountInfo.getName0() : postpaidBoxedConsumerAccountInfo.getContactName0(), amdocsContactNameInfo);
	        amdocsIdentificationInfo = (amdocs.APILink.datatypes.IdentificationInfo)mapTelusToAmdocs(postpaidBoxedConsumerAccountInfo.getPersonalCreditInformation0(), amdocsIdentificationInfo);
		}

		// replace amdocs classes in hash map
		amdocsClasses.put( AttributeTranslator.getBaseName(new AccountTypeInfo().getClass()),amdocsAccountTypeInfo);
		amdocsClasses.put("Billing" +  AttributeTranslator.getBaseName(new NameInfo().getClass()),amdocsBillingNameInfo);
		amdocsClasses.put("Contact" +  AttributeTranslator.getBaseName(new NameInfo().getClass()),amdocsContactNameInfo);
		amdocsClasses.put("Billing" +  AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.AddressInfo().getClass()),amdocsBillingAddressInfo);
		amdocsClasses.put("CreditCheck" +  AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.AddressInfo().getClass()),amdocsCreditCheckAddressInfo);
		amdocsClasses.put( AttributeTranslator.getBaseName(new IdentificationInfo().getClass()),amdocsIdentificationInfo);
		amdocsClasses.put( AttributeTranslator.getBaseName(new ContractInfo().getClass()),amdocsContractInfo);
		amdocsClasses.put( AttributeTranslator.getBaseName(new ContactInfo().getClass()),amdocsContactInfo);
		amdocsClasses.put( AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.CreditCardInfo().getClass()),amdocsCreditCardInfo);
		amdocsClasses.put( AttributeTranslator.getBaseName(new CreditCardDetailsInfo().getClass()),amdocsCreditCardDetailsInfo);
		amdocsClasses.put( AttributeTranslator.getBaseName(new CheckDetailsInfo().getClass()),amdocsCheckDetailsInfo);
		amdocsClasses.put( AttributeTranslator.getBaseName(new CycleInfo().getClass()),amdocsCycleInfo);
		amdocsClasses.put( AttributeTranslator.getBaseName(new BusinessInfo().getClass()),amdocsBusinessInfo);
		amdocsClasses.put( AttributeTranslator.getBaseName(new BillDetailsInfo().getClass()),amdocsBillDetailsInfo);
		amdocsClasses.put( AttributeTranslator.getBaseName(new SpecialBillDetailsInfo().getClass()),amdocsSpecialBillDetailsInfo);
		amdocsClasses.put( AttributeTranslator.getBaseName(new NationalGrowthInfo().getClass()),amdocsNationalGrowthInfo);
		amdocsClasses.put( AttributeTranslator.getBaseName(new CPUIInfo().getClass()),amdocsCPUIArrayInfo);

		return amdocsClasses;

	}

	
	private static HashMap<Object,Object> initializeAmdocsClassesHash(HashMap<Object,Object> amdocsClasses) {

		amdocsClasses = new HashMap <Object,Object>();
		amdocsClasses.put(AttributeTranslator.getBaseName(new AccountTypeInfo().getClass()),new AccountTypeInfo());
		amdocsClasses.put("Billing" + AttributeTranslator.getBaseName(new NameInfo().getClass()),new NameInfo());
		amdocsClasses.put("Contact" + AttributeTranslator.getBaseName(new NameInfo().getClass()),new NameInfo());
		amdocsClasses.put("Billing" + AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.AddressInfo().getClass()),new amdocs.APILink.datatypes.AddressInfo());
		amdocsClasses.put("CreditCheck" + AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.AddressInfo().getClass()),new amdocs.APILink.datatypes.AddressInfo());
		amdocsClasses.put( AttributeTranslator.getBaseName(new IdentificationInfo().getClass()),new IdentificationInfo());
		amdocsClasses.put( AttributeTranslator.getBaseName(new ContractInfo().getClass()),new ContractInfo());
		amdocsClasses.put( AttributeTranslator.getBaseName(new ContactInfo().getClass()),new ContactInfo());
		amdocsClasses.put( AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.CreditCardInfo().getClass()),new amdocs.APILink.datatypes.CreditCardInfo());
		amdocsClasses.put( AttributeTranslator.getBaseName(new CreditCardDetailsInfo().getClass()),new CreditCardDetailsInfo());
		amdocsClasses.put( AttributeTranslator.getBaseName(new CheckDetailsInfo().getClass()),new CheckDetailsInfo());
		amdocsClasses.put( AttributeTranslator.getBaseName(new CycleInfo().getClass()),new CycleInfo());
		amdocsClasses.put( AttributeTranslator.getBaseName(new BusinessInfo().getClass()),new BusinessInfo());
		amdocsClasses.put( AttributeTranslator.getBaseName(new BillDetailsInfo().getClass()),new BillDetailsInfo());
		amdocsClasses.put( AttributeTranslator.getBaseName(new SpecialBillDetailsInfo().getClass()),new SpecialBillDetailsInfo());
		amdocsClasses.put( AttributeTranslator.getBaseName(new NationalGrowthInfo().getClass()),new NationalGrowthInfo());
		amdocsClasses.put( AttributeTranslator.getBaseName(new CPUIInfo().getClass()),new CPUIInfo());
		amdocsClasses.put( AttributeTranslator.getBaseName(new amdocs.APILink.datatypes.TaxExemptionInfo().getClass()), new amdocs.APILink.datatypes.TaxExemptionInfo());
		return amdocsClasses;
	}
	
	
	public static Object mapTelusToAmdocs(Info pSourceTelusInfoClass, Object pTargetAmdocsInfoClass) throws ApplicationException {

		// Telus API Info classes
		AccountInfo accountInfo = new AccountInfo();
		PostpaidConsumerAccountInfo postpaidConsumerAccountInfo = null;
		PrepaidConsumerAccountInfo prepaidConsumerAccountInfo = PrepaidConsumerAccountInfo.newPCSInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID);
		PostpaidBoxedConsumerAccountInfo postpaidBoxedConsumerAccountInfo = null;
		PostpaidBusinessRegularAccountInfo postpaidBusinessRegularAccountInfo = null;

		AddressInfo addressInfo = new AddressInfo();
		ConsumerNameInfo consumerNameInfo = new ConsumerNameInfo();
		PersonalCreditInfo personalCreditInfo = new PersonalCreditInfo();
		com.telus.eas.account.info.CreditCardInfo creditCardInfo = new com.telus.eas.account.info.CreditCardInfo();
		ChequeInfo chequeInfo = new ChequeInfo();
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

		LOGGER.debug("Processing Input: " + pSourceTelusInfoClass.getClass().getName());
		LOGGER.debug("          Output: " + pTargetAmdocsInfoClass.getClass().getName());

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
						throw new ApplicationException(SystemCodes.CMB_UTL_T2A,"Number Format Exception, SIN can only have numeric value","");
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

        	amdocsNationalGrowthInfo.homeProvince =	AttributeTranslator.emptyFromNull(
              accountInfo.getHomeProvince()).equals(
              "")
              ? "ON"
              : accountInfo.getHomeProvince();            // default 'Ontario'
          // if home province is not set, only set home province if address is in Canada
          // otherwise it is defaulted to 'Ontario'
          // (will be updated with first activated subscriber's phone number province)
          
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

        	amdocsNationalGrowthInfo.nationalAccount =  AttributeTranslator.emptyFromNull(
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
        	if (codes != null) {
        		for (int i=0;i<codes.length;i++) {
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
		if ((pSourceTelusInfoClass instanceof CreditCheckResultInfo) && pTargetAmdocsInfoClass instanceof CreditResultExtInfo) {

			creditCheckResultInfo = (CreditCheckResultInfo) pSourceTelusInfoClass;
			amdocsCreditResultExtInfo = pTargetAmdocsInfoClass != null ? (CreditResultExtInfo) pTargetAmdocsInfoClass : new CreditResultExtInfo();
			amdocsCreditResultExtInfo.creditClass = AttributeTranslator.byteFromString(creditCheckResultInfo.getCreditClass());
			amdocsCreditResultExtInfo.creditLimit = creditCheckResultInfo.getLimit();
			amdocsCreditResultExtInfo.creditResult2 = AttributeTranslator.emptyFromNull(StringUtils.isNotBlank(creditCheckResultInfo.getBureauMessage()) 
					? creditCheckResultInfo.getBureauMessage() : creditCheckResultInfo.getMessage());
			amdocsCreditResultExtInfo.beaconScore = creditCheckResultInfo.getCreditScore();
			amdocsCreditResultExtInfo.transAlert1 = AttributeTranslator.emptyFromNull(creditCheckResultInfo.getTransAlert1());
			amdocsCreditResultExtInfo.transAlert2 = AttributeTranslator.emptyFromNull(creditCheckResultInfo.getTransAlert2());
			amdocsCreditResultExtInfo.transAlert3 = AttributeTranslator.emptyFromNull(creditCheckResultInfo.getTransAlert3());
			amdocsCreditResultExtInfo.transAlert4 = AttributeTranslator.emptyFromNull(creditCheckResultInfo.getTransAlert4());
			amdocsCreditResultExtInfo.tuReturnInd = AttributeTranslator.emptyFromNull(creditCheckResultInfo.getTuReturnInd());
			amdocsCreditResultExtInfo.node = AttributeTranslator.emptyFromNull(creditCheckResultInfo.getNode());
			amdocsCreditResultExtInfo.performExistNegInd = AttributeTranslator.byteFromString(creditCheckResultInfo.getExistingNegInd());
			amdocsCreditResultExtInfo.performHawkInd = AttributeTranslator.byteFromString(creditCheckResultInfo.getHawkAlertInd());

			String[] stringArray = null;
			if (creditCheckResultInfo.getErrorCode() > 0) {
				stringArray = AttributeTranslator.stringArraryFromString(AttributeTranslator.emptyFromNull(creditCheckResultInfo.getErrorMessage()).trim(), 1000);
			} else {
				stringArray = AttributeTranslator.stringArraryFromString(AttributeTranslator.emptyFromNull(creditCheckResultInfo.getBureauFile()).trim(), 1000);
			}

			// Need to initialize or we get Tuxedo failure (failed to build request for service csCrManCrd)
			CreditResultExt1[] creditResultExt1s = new CreditResultExt1[0];
			amdocsCreditResultExtInfo.creditResultExt1 = creditResultExt1s;

			CreditResultExt2[] amdocsCreditResultExt2s = new CreditResultExt2[stringArray.length];
			for (int i = 0; i < stringArray.length; i++) {
				amdocsCreditResultExt2s[i] = new CreditResultExt2();
				amdocsCreditResultExt2s[i].txtResult = stringArray[i];
			}
			amdocsCreditResultExtInfo.creditResultExt2 = amdocsCreditResultExt2s;

			if (creditCheckResultInfo.getDeposits() != null) {
				CreditResultDeposit[] amdocsCreditResultDeposits = new CreditResultDeposit[creditCheckResultInfo.getDeposits().length];
				for (int i = 0; i < creditCheckResultInfo.getDeposits().length; i++) {
					amdocsCreditResultDeposits[i] = new CreditResultDeposit();
					amdocsCreditResultDeposits[i].depositAmt = creditCheckResultInfo.getDeposits()[i].getDeposit();
					amdocsCreditResultDeposits[i].depositArrProductType = AttributeTranslator.byteFromString(creditCheckResultInfo.getDeposits()[i].getProductType());
				}
				amdocsCreditResultExtInfo.creditResultDeposit = amdocsCreditResultDeposits;
			} else {
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
						throw new ApplicationException(SystemCodes.CMB_UTL_T2A,"Number Format Exception, SIN can only have numeric value","");
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
 
        throw new ApplicationException(SystemCodes.CMB_EJB, "No Mapping performed.", "");
	}
 
	private static void mapCreditCardInfo ( com.telus.eas.account.info.CreditCardInfo telusCreditCardInfo, 
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
	
	private static void mapCreditCardInfo ( com.telus.eas.account.info.CreditCardInfo telusCreditCardInfo, 
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
}
