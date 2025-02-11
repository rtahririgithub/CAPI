package com.telus.cmb.account.lifecyclemanager.dao.impl;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import amdocs.APILink.datatypes.AccountTypeInfo;
import amdocs.APILink.datatypes.ContactInfo;
import amdocs.APILink.datatypes.IdentificationInfo;
import amdocs.APILink.datatypes.NameInfo;
import amdocs.APILink.sessions.interfaces.UpdateBanConv;

import com.telus.api.ApplicationException;
import com.telus.api.reference.AccountType;
import com.telus.cmb.account.lifecyclemanager.dao.ContactDao;
import com.telus.cmb.account.utilities.AccountLifecycleUtilities;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTelusMapping;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.ContactPropertyInfo;

public class ContactDaoImpl extends AmdocsDaoSupport implements ContactDao {

	private static final Logger LOGGER = Logger.getLogger(ContactDaoImpl.class);

	@Override
	public void updateBillingInformation(final int billingAccountNumber,
			final BillingPropertyInfo billingPropertyInfo, String sessionId)
	throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext)
			throws Exception {
				String methodName = "updateBillingInformation";
				LOGGER.info("("+getClass().getName()+"."+ methodName+") Incoming BillingPropertyInfo object for BAN["+billingAccountNumber+"]..."+billingPropertyInfo);
				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
				updateBanConv.setBanPK(billingAccountNumber);

				NameInfo amdocsBillingNameInfo = null;
				amdocs.APILink.datatypes.AddressInfo amdocsBillingAddressInfo = null;
				IdentificationInfo amdocsIdentificationInfo = null;
				char accountType;
				char accountSubType;
				boolean saveBanNecessary = false;

				AccountTypeInfo originalAmdocsAccountTypeInfo = updateBanConv.getAccountTypeInfo();
				accountType = (char)originalAmdocsAccountTypeInfo.accountType;
				accountSubType =(char) originalAmdocsAccountTypeInfo.accountSubType;
				Hashtable<String,Boolean> accountTypeIdentifiers = AccountLifecycleUtilities.getAccountTypeIdentifiers(accountType, accountSubType);

				// Set Billing Address Info
				if(billingPropertyInfo.getAddress()!=null){
					amdocsBillingAddressInfo = new amdocs.APILink.datatypes.AddressInfo();
					amdocsBillingAddressInfo = (amdocs.APILink.datatypes.AddressInfo)AmdocsTelusMapping.mapTelusToAmdocs(billingPropertyInfo.getAddress(),amdocsBillingAddressInfo);

					// Address - Billing
					amdocs.APILink.datatypes.AddressInfo originalAmdocsBillingAddressInfo = updateBanConv.getBillingAddress();
					if (AccountLifecycleUtilities.addressHasChanged(originalAmdocsBillingAddressInfo, amdocsBillingAddressInfo)) {
						saveBanNecessary = true;
						LOGGER.debug("("+getClass().getName()+"."+ methodName+" Calling setBillingAddress()...");
						updateBanConv.setBillingAddress(amdocsBillingAddressInfo);
					}
				}//End Set Billing Address Info

				// Set Billing Name Info
				if(billingPropertyInfo.getName()!=null){
					amdocsBillingNameInfo = new NameInfo();

					if (accountTypeIdentifiers.get("postpaidConsumer") || accountTypeIdentifiers.get("postpaidConsumerEmployee") || accountTypeIdentifiers.get("postpaidConsumerEmployeeNew") || accountTypeIdentifiers.get("postpaidBusinessPersonal") || 
							accountTypeIdentifiers.get("postpaidCorporatePersonal") || accountTypeIdentifiers.get("prepaidConsumer") || accountTypeIdentifiers.get("quebecTelPrepaidConsumer") || accountTypeIdentifiers.get("westernPrepaidConsumer") || 
							accountTypeIdentifiers.get("postpaidBoxedConsumer")){ 
						amdocsBillingNameInfo = (NameInfo)AmdocsTelusMapping.mapTelusToAmdocs(billingPropertyInfo.getName(), amdocsBillingNameInfo);
					}

					if (accountTypeIdentifiers.get("postpaidBusinessRegular") || accountTypeIdentifiers.get("postpaidBusinessDealer") || accountTypeIdentifiers.get("postpaidCorporateRegular")){
						amdocsBillingNameInfo.additionalTitle = AttributeTranslator.emptyFromNull(billingPropertyInfo.getTradeNameAttention());
					}

					//  post paid business regular, business dealer or corporate regular
					boolean nameFormatBusiness = billingPropertyInfo.getName().getNameFormat()==null?
							false:((billingPropertyInfo.getName().getNameFormat().charAt(0)== AccountType.BILLING_NAME_FORMAT_BUSINESS)?
									true:false);
					if (accountTypeIdentifiers.get("postpaidBusinessRegular") || accountTypeIdentifiers.get("postpaidBusinessDealer") || 
							accountTypeIdentifiers.get("postpaidCorporateRegular") || nameFormatBusiness) {
						amdocsBillingNameInfo.lastBusinessName = AttributeTranslator.emptyFromNull(billingPropertyInfo.getLegalBusinessName());
						amdocsBillingNameInfo.additionalTitle = AttributeTranslator.emptyFromNull(billingPropertyInfo.getTradeNameAttention());
					}


					// Billing Name
					NameInfo originalAmdocsBillingNameInfo = updateBanConv.getBillingName();
					boolean nameFormatPersonal = billingPropertyInfo.getName().getNameFormat()==null?
							false:((billingPropertyInfo.getName().getNameFormat().charAt(0)== AccountType.BILLING_NAME_FORMAT_PERSONAL)?
									true:false);
					if (accountTypeIdentifiers.get("postpaidCorporateRegular") && nameFormatPersonal) {
						amdocsBillingNameInfo.firstName = originalAmdocsBillingNameInfo.firstName;
					}

					if (AttributeTranslator.compare(amdocsBillingNameInfo.additionalTitle,originalAmdocsBillingNameInfo.additionalTitle) != 0 ||	  
							AttributeTranslator.compare(amdocsBillingNameInfo.firstName, originalAmdocsBillingNameInfo.firstName) != 0 || 
							AttributeTranslator.compare(amdocsBillingNameInfo.lastBusinessName, originalAmdocsBillingNameInfo.lastBusinessName) != 0 ||
							AttributeTranslator.compare(amdocsBillingNameInfo.middleInitial, originalAmdocsBillingNameInfo.middleInitial) != 0 ||
							AttributeTranslator.compare(amdocsBillingNameInfo.nameSuffix,originalAmdocsBillingNameInfo.nameSuffix) != 0 ||
							AttributeTranslator.compare(amdocsBillingNameInfo.nameTitle, originalAmdocsBillingNameInfo.nameTitle) != 0) {
						saveBanNecessary = true;
						LOGGER.debug("("+getClass().getName()+"."+ methodName+" Calling setBillingName()...");
						updateBanConv.setBillingName(amdocsBillingNameInfo);
					}

				}//End of Set Billing Name Info

				// Set Verified Date
				if(billingPropertyInfo.getVerifiedDate()!=null){
					amdocsIdentificationInfo = new IdentificationInfo();
					amdocsIdentificationInfo.verifiedDate = billingPropertyInfo.getVerifiedDate();

					IdentificationInfo originalAmdocsIdentificationInfo = updateBanConv.getIdentificationInfo();

					if (amdocsIdentificationInfo.verifiedDate != null && originalAmdocsIdentificationInfo.verifiedDate == null ||
							(amdocsIdentificationInfo.verifiedDate != null && originalAmdocsIdentificationInfo.verifiedDate != null && amdocsIdentificationInfo.verifiedDate.compareTo(originalAmdocsIdentificationInfo.verifiedDate) != 0) )
					{
						saveBanNecessary = true;
						LOGGER.debug("("+getClass().getName()+"."+ methodName+" Calling setIdentificationInfo()...");
						updateBanConv.setIdentificationInfo(amdocsIdentificationInfo);
					}
				}//End Set Verified Date


				// Save
				if (saveBanNecessary) {
					updateBanConv.saveBan();
					LOGGER.debug("("+getClass().getName()+"."+ methodName+" Changes updated...");
				}else{
					LOGGER.debug("("+getClass().getName()+"."+ methodName+" Nothing to update...");
				}

				return null;

			}
		});
	}

	@Override
	public void updateContactInformation(final int billingAccountNumber,
			final ContactPropertyInfo contactPropertyInfo, String sessionId)
	throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(
					AmdocsTransactionContext transactionContext)
			throws Exception {
				String methodName = "updateContactInformation";
				LOGGER.debug("("+getClass().getName()+"."+ methodName+") Incoming ContactPropertyInfo object for BAN["+billingAccountNumber+"]..."+contactPropertyInfo);
				
				// Set BanPK (which also retrieves the BAN)
				UpdateBanConv updateBanConv = transactionContext.createBean(UpdateBanConv.class);
				updateBanConv.setBanPK(billingAccountNumber);

				NameInfo amdocsContactNameInfo = new NameInfo();
				ContactInfo amdocsContactInfo = new ContactInfo();
				char accountType;
				char accountSubType;

				AccountTypeInfo originalAmdocsAccountTypeInfo = updateBanConv.getAccountTypeInfo();
				accountType = (char)originalAmdocsAccountTypeInfo.accountType;
				accountSubType =(char) originalAmdocsAccountTypeInfo.accountSubType;
				Hashtable<String,Boolean> accountTypeIdentifiers = AccountLifecycleUtilities.getAccountTypeIdentifiers(accountType, accountSubType);
				// Set contact info
				amdocsContactInfo.otherTelType = AttributeTranslator.emptyFromNull(contactPropertyInfo.getOtherPhoneType());
				amdocsContactInfo.otherTelNo = AttributeTranslator.emptyFromNull(contactPropertyInfo.getOtherPhoneNumber());
				amdocsContactInfo.otherTelExtNo = AttributeTranslator.emptyFromNull(contactPropertyInfo.getOtherPhoneExtension());

				amdocsContactInfo.homeTelNo = AttributeTranslator.emptyFromNull(contactPropertyInfo.getHomePhoneNumber());
				amdocsContactInfo.workTelNo = AttributeTranslator.emptyFromNull(contactPropertyInfo.getBusinessPhoneNumber());
				amdocsContactInfo.workTelExtNo = AttributeTranslator.emptyFromNull(contactPropertyInfo.getBusinessPhoneExtension()).trim();

				amdocsContactInfo.contactTelNo = AttributeTranslator.emptyFromNull(contactPropertyInfo.getContactPhoneNumber()).equals("") ? AttributeTranslator.emptyFromNull(contactPropertyInfo.getHomePhoneNumber()) : AttributeTranslator.emptyFromNull(contactPropertyInfo.getContactPhoneNumber());
				amdocsContactInfo.contactTelExtNo = AttributeTranslator.emptyFromNull(contactPropertyInfo.getContactPhoneExtension()).trim();

				// Set contact name info
				if(contactPropertyInfo.getName()!= null ){
					boolean nameFormatBusiness =contactPropertyInfo.getName().getNameFormat()==null?
							false:((contactPropertyInfo.getName().getNameFormat().charAt(0)== AccountType.BILLING_NAME_FORMAT_BUSINESS)?
									true:false);
					if (accountTypeIdentifiers.get("postpaidConsumer") || accountTypeIdentifiers.get("postpaidConsumerEmployee") || accountTypeIdentifiers.get("postpaidConsumerEmployeeNew") || 
							accountTypeIdentifiers.get("postpaidBusinessPersonal") || accountTypeIdentifiers.get("postpaidCorporatePersonal") ||
							accountTypeIdentifiers.get("postpaidBusinessRegular") || accountTypeIdentifiers.get("postpaidBusinessDealer") || 
							accountTypeIdentifiers.get("postpaidCorporateRegular") || accountTypeIdentifiers.get("postpaidBusinessRegular") || 
							accountTypeIdentifiers.get("postpaidBusinessDealer") || accountTypeIdentifiers.get("postpaidCorporateRegular") ||	
							nameFormatBusiness || !accountTypeIdentifiers.get("postpaidConsumer") || accountTypeIdentifiers.get("postpaidBoxedConsumer")) {
						amdocsContactNameInfo = (NameInfo)AmdocsTelusMapping.mapTelusToAmdocs(
								contactPropertyInfo.getName(),amdocsContactNameInfo);
					}
				}
				// Save		

				// Contact Name
				NameInfo originalAmdocsContactNameInfo = updateBanConv.getContactName();
				boolean saveBanNecessary = false;
				if (!amdocsContactNameInfo.lastBusinessName.trim().equals("") &&
						(AttributeTranslator.compare(amdocsContactNameInfo.additionalTitle, originalAmdocsContactNameInfo.additionalTitle) != 0 ||
								AttributeTranslator.compare(amdocsContactNameInfo.firstName, originalAmdocsContactNameInfo.firstName) != 0 ||
								AttributeTranslator.compare(amdocsContactNameInfo.lastBusinessName, originalAmdocsContactNameInfo.lastBusinessName) != 0 ||
								AttributeTranslator.compare(amdocsContactNameInfo.middleInitial, originalAmdocsContactNameInfo.middleInitial) != 0 ||
								AttributeTranslator.compare(amdocsContactNameInfo.nameSuffix, originalAmdocsContactNameInfo.nameSuffix) != 0 ||
								AttributeTranslator.compare(amdocsContactNameInfo.nameTitle, originalAmdocsContactNameInfo.nameTitle) != 0)) {
					saveBanNecessary = true;
					LOGGER.debug("("+getClass().getName()+"."+ methodName+" Calling setContactName()...");
					updateBanConv.setContactName(amdocsContactNameInfo);
				}

				ContactInfo originalAmdocsContactInfo = updateBanConv.getContactInfo();
				if (AttributeTranslator.compare(amdocsContactInfo.contactTelExtNo, originalAmdocsContactInfo.contactTelExtNo) != 0  ||
						AttributeTranslator.compare(amdocsContactInfo.contactTelNo, originalAmdocsContactInfo.contactTelNo) != 0  ||
						AttributeTranslator.compare(amdocsContactInfo.homeTelNo, originalAmdocsContactInfo.homeTelNo) != 0  ||
						AttributeTranslator.compare(amdocsContactInfo.otherTelExtNo, originalAmdocsContactInfo.otherTelExtNo) != 0  ||
						AttributeTranslator.compare(amdocsContactInfo.otherTelNo, originalAmdocsContactInfo.otherTelNo) != 0  ||
						AttributeTranslator.compare(amdocsContactInfo.otherTelType, originalAmdocsContactInfo.otherTelType) != 0  ||
						AttributeTranslator.compare(amdocsContactInfo.workTelExtNo, originalAmdocsContactInfo.workTelExtNo) != 0  ||
						AttributeTranslator.compare(amdocsContactInfo.workTelNo, originalAmdocsContactInfo.workTelNo) != 0 ) {
					saveBanNecessary = true;
					LOGGER.debug("("+getClass().getName()+"."+ methodName+" Calling setContactInfo()...");
					updateBanConv.setContactInfo(amdocsContactInfo);
				}

				// store BAN
				if (saveBanNecessary) {
					updateBanConv.saveBan();
					LOGGER.debug("("+getClass().getName()+"."+ methodName+" Changes updated...");
				}else{
					LOGGER.debug("("+getClass().getName()+"."+ methodName+" Nothing to update...");
				}

				return null;
			}
		});
	}

}
