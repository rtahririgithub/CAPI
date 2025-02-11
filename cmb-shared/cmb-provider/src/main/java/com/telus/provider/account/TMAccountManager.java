/*
 * Copyright © 2005 TELE-MOBILE COMPANY c.o.b TELUS Mobility, All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.api.LimitExceededException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountManager;
import com.telus.api.account.AccountMatchException;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.ActivationCredit;
import com.telus.api.account.ActivationTopUp;
import com.telus.api.account.Address;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.AutoTopUp;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.Credit;
import com.telus.api.account.CreditCard;
import com.telus.api.account.Discount;
import com.telus.api.account.EnterpriseAddress;
import com.telus.api.account.FollowUp;
import com.telus.api.account.IDENAccount;
import com.telus.api.account.IDENPostpaidBusinessDealerAccount;
import com.telus.api.account.IDENPostpaidBusinessPersonalAccount;
import com.telus.api.account.IDENPostpaidBusinessRegularAccount;
import com.telus.api.account.IDENPostpaidConsumerAccount;
import com.telus.api.account.IDENPostpaidCorporateRegularAccount;
import com.telus.api.account.IDENPostpaidEmployeeAccount;
import com.telus.api.account.IDENSubscriber;
import com.telus.api.account.InvalidAccountTypeChangeException;
import com.telus.api.account.InvalidActivationAmountException;
import com.telus.api.account.InvalidActivationCodeException;
import com.telus.api.account.InvalidActivationTypeException;
import com.telus.api.account.InvalidAirtimeCardException;
import com.telus.api.account.InvalidAirtimeCardPINException;
import com.telus.api.account.InvalidAirtimeCardStatusException;
import com.telus.api.account.InvalidBillCycleException;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.account.InvalidSerialNumberException;
import com.telus.api.account.Memo;
import com.telus.api.account.MemoCriteria;
import com.telus.api.account.PCSAccount;
import com.telus.api.account.PCSPostpaidBusinessDealerAccount;
import com.telus.api.account.PCSPostpaidBusinessOfficialAccount;
import com.telus.api.account.PCSPostpaidBusinessPersonalAccount;
import com.telus.api.account.PCSPostpaidBusinessRegularAccount;
import com.telus.api.account.PCSPostpaidConsumerAccount;
import com.telus.api.account.PCSPostpaidCorporatePersonalAccount;
import com.telus.api.account.PCSPostpaidCorporateRegularAccount;
import com.telus.api.account.PCSPostpaidEmployeeAccount;
import com.telus.api.account.PCSPrepaidConsumerAccount;
import com.telus.api.account.PagerPostpaidBoxedConsumerAccount;
import com.telus.api.account.PagerPostpaidBusinessRegularAccount;
import com.telus.api.account.PagerPostpaidConsumerAccount;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.PaymentTransfer;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.api.account.PhoneNumberSearchOption;
import com.telus.api.account.PrepaidCallHistory;
import com.telus.api.account.PrepaidEventHistory;
import com.telus.api.account.QueueThresholdEvent;
import com.telus.api.account.SearchResults;
import com.telus.api.account.SeatData;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.ServicesValidation;
import com.telus.api.account.Subscriber;
import com.telus.api.account.TaxSummary;
import com.telus.api.account.UnknownBANException;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.api.account.UnsupportedEquipmentException;
import com.telus.api.equipment.AirtimeCard;
import com.telus.api.equipment.Card;
import com.telus.api.equipment.CellularDigitalEquipment;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.USIMCardEquipment;
import com.telus.api.reference.Brand;
import com.telus.api.reference.FollowUpCriteria;
import com.telus.api.reference.PrepaidEventType;
import com.telus.api.reference.PrepaidRateProfile;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.util.SessionUtil;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.ActivationTopUpInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.EnterpriseAddressInfo;
import com.telus.eas.account.info.MemoCriteriaInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PaymentTransferInfo;
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
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.account.info.ServicesValidationInfo;
import com.telus.eas.account.info.TaxExemptionInfo;
import com.telus.eas.account.info.WesternPrepaidConsumerAccountInfo;
import com.telus.eas.framework.info.ActivationCreditInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.queueevent.info.QueueThresholdEventInfo;
import com.telus.eas.subscriber.info.CDPDSubscriberInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.PCSSubscriberInfo;
import com.telus.eas.subscriber.info.PagerSubscriberInfo;
import com.telus.eas.subscriber.info.PrepaidCallHistoryInfo;
import com.telus.eas.subscriber.info.PrepaidEventHistoryInfo;
import com.telus.eas.subscriber.info.SeatDataInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.TangoSubscriberInfo;
import com.telus.eas.utility.info.FollowUpCriteriaInfo;
import com.telus.eas.utility.info.PrepaidEventTypeInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.equipment.TMEquipment;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.ProviderCreditCardExceptionTranslator;
import com.telus.provider.util.ProviderEquipmentExceptionTranslator;


public class TMAccountManager extends BaseProvider implements AccountManager {

	private static final long serialVersionUID = 1L;
	public static final int PREPAID_BILL_CYCLE = 88;

	/**
	 * @link aggregation
	 */
	public TMAccountManager(TMProvider provider) {
		super(provider);
	}


	public CreditCard newCreditCard(Account account) throws TelusAPIException {
		return new TMCreditCard(provider, new CreditCardInfo(), account);
	}


	public CreditCard newCreditCard(String holderName) throws TelusAPIException{
		CreditCardInfo creditCardInfo = new CreditCardInfo();
		creditCardInfo.setHolderName(holderName);
		return new TMCreditCard(provider, creditCardInfo);
	}


	public PaymentMethod newPaymentMethod(Account account) throws TelusAPIException {
		return new TMPaymentMethod(provider, (TMAccount)account, new PaymentMethodInfo());
	}

	public PaymentMethod newPaymentMethod(Account account, PaymentMethod oldPaymentMethod) throws TelusAPIException {
		PaymentMethodInfo paymentMethodInfo = new PaymentMethodInfo();
		paymentMethodInfo.copyFrom(((TMPaymentMethod)oldPaymentMethod).getDelegate());
		return new TMPaymentMethod(provider, (TMAccount)account, paymentMethodInfo);
	}

	public ConsumerName newAuthorizedName() throws TelusAPIException {
		return new ConsumerNameInfo();
	}

	public PCSPostpaidConsumerAccount newPCSPostpaidConsumerAccount() throws TelusAPIException {
		PostpaidConsumerAccountInfo info = PostpaidConsumerAccountInfo.newPCSInstance();
		info.setEvaluationProductType(PRODUCT_TYPE_PCS);
		return new TMPCSPostpaidConsumerAccount(provider, info);
	}

	@Deprecated
	public PCSPostpaidConsumerAccount newPCSPostpaidConsumerAccount(boolean isFidoConversion) throws TelusAPIException {
		PostpaidConsumerAccountInfo info = PostpaidConsumerAccountInfo.newPCSInstance();
		info.setEvaluationProductType(PRODUCT_TYPE_PCS);
		info.setFidoConversion(isFidoConversion);
		return new TMPCSPostpaidConsumerAccount(provider, info);
	}

	public PCSPostpaidEmployeeAccount newPCSPostpaidEmployeeAccount() throws TelusAPIException {
		PostpaidEmployeeAccountInfo info = PostpaidEmployeeAccountInfo.newPCSInstance0();
		info.setEvaluationProductType(PRODUCT_TYPE_PCS);
		return new TMPCSPostpaidEmployeeAccount(provider, info);
	}

	public PCSPostpaidBusinessRegularAccount newPCSPostpaidBusinessRegularAccount() throws TelusAPIException {
		PostpaidBusinessRegularAccountInfo info = PostpaidBusinessRegularAccountInfo.newPCSInstance();
		info.setEvaluationProductType(PRODUCT_TYPE_PCS);
		return new TMPCSPostpaidBusinessRegularAccount(provider, info);
	}

	@Deprecated
	public PCSPostpaidBusinessRegularAccount newPCSPostpaidBusinessRegularAccount(boolean isFidoConversion) throws TelusAPIException {
		PostpaidBusinessRegularAccountInfo info = PostpaidBusinessRegularAccountInfo.newPCSInstance();
		info.setEvaluationProductType(PRODUCT_TYPE_PCS);
		info.setFidoConversion(isFidoConversion);
		return new TMPCSPostpaidBusinessRegularAccount(provider, info);
	}

	public PCSPostpaidBusinessDealerAccount newPCSPostpaidBusinessDealerAccount(boolean isFidoConversion) throws TelusAPIException {
		PostpaidBusinessDealerAccountInfo info =  PostpaidBusinessDealerAccountInfo.newPCSInstance0();
		info.setEvaluationProductType(PRODUCT_TYPE_PCS);
		info.setFidoConversion(isFidoConversion);
		return new TMPCSPostpaidBusinessDealerAccount(provider, info);
	}

	public PCSPostpaidBusinessOfficialAccount newPCSPostpaidBusinessOfficialAccount(boolean isFidoConversion) throws TelusAPIException {
		PostpaidBusinessOfficialAccountInfo info =  PostpaidBusinessOfficialAccountInfo.newPCSInstance0();
		info.setEvaluationProductType(PRODUCT_TYPE_PCS);
		info.setFidoConversion(isFidoConversion);
		return new TMPCSPostpaidBusinessOfficialAccount(provider, info);
	}

	public PCSPostpaidBusinessPersonalAccount newPCSPostpaidBusinessPersonalAccount() throws TelusAPIException {
		PostpaidBusinessPersonalAccountInfo info = PostpaidBusinessPersonalAccountInfo.newPCSInstance0();
		info.setEvaluationProductType(PRODUCT_TYPE_PCS);
		return new TMPCSPostpaidBusinessPersonalAccount(provider, info);
	}

	@Deprecated
	public PCSPostpaidBusinessPersonalAccount newPCSPostpaidBusinessPersonalAccount(boolean isFidoConversion) throws TelusAPIException {
		PostpaidBusinessPersonalAccountInfo info = PostpaidBusinessPersonalAccountInfo.newPCSInstance0();
		info.setEvaluationProductType(PRODUCT_TYPE_PCS);
		info.setFidoConversion(isFidoConversion);
		return new TMPCSPostpaidBusinessPersonalAccount(provider, info);
	}

	public PCSPrepaidConsumerAccount newPCSPrepaidConsumerAccount(String serialNumber, int activationType, String activationCode, CreditCard creditCard, String businessRole, AuditHeader auditHeader)
	throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, InvalidActivationCodeException,
	InvalidCreditCardException, TelusAPIException {
		return newPCSPrepaidConsumerAccount(serialNumber, activationType, activationCode, creditCard, businessRole, false, auditHeader);
	}
	public PCSPrepaidConsumerAccount newPCSPrepaidConsumerAccount(String serialNumber, String associatedHandsetIMEI, int activationType, String activationCode, 			
			CreditCard creditCard, String businessRole, double activationAmount, AuditHeader auditHeader) throws UnknownSerialNumberException, SerialNumberInUseException, 
			InvalidSerialNumberException, InvalidActivationCodeException, InvalidCreditCardException, InvalidAirtimeCardPINException, 
			InvalidActivationAmountException, UnsupportedEquipmentException, InvalidAirtimeCardStatusException, InvalidActivationTypeException, 
			InvalidAirtimeCardException, TelusAPIException {
		try {
			//-----------------------------------------------
			//PCI changes
			//-----------------------------------------------
			if ( creditCard != null ) {
				if ( auditHeader==null ) {
					throw new TelusAPIException( "The required AuditHeader is missing." );
				}
				((TMCreditCard)creditCard).setAuditHeader(auditHeader);
			}

			//-----------------------------------------------
			// Validate serialNumber.
			//-----------------------------------------------
			Equipment equipment = provider.getEquipmentManager().validateSerialNumber(serialNumber);

			if(equipment.isHSPA()) {
				if(!equipment.isUSIMCard()) {
					throw new UnsupportedEquipmentException ("This is HSPA but not USIM ");
				}
			}
			else if(equipment.isRIM() || equipment.is1xRTTCard() || this.isWorldPhone(equipment) || this.isPDA(equipment)) {
				throw new InvalidSerialNumberException(serialNumber, InvalidSerialNumberException.EQUIPMENT_NOT_ALLOWED);
			}

			//-----------------------------------------------
			// Validate Purchace-now creditCard.
			//-----------------------------------------------
			double amountInCard = 0.0;
			if(activationType == ACTIVATION_TYPE_CREDIT_CARD) {
				((TMCreditCard)creditCard).getCreditCardTransactionInfo().getCreditCardHolderInfo().setAccountType("" + AccountSummary.ACCOUNT_TYPE_CONSUMER);
				((TMCreditCard)creditCard).getCreditCardTransactionInfo().getCreditCardHolderInfo().setAccountSubType("" + AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID);
				creditCard.validate("Purchase Activation Credit", businessRole, auditHeader ); //use new PCI version
			}
			else if(activationType == ACTIVATION_TYPE_AIRTIME_CARD){
				AirtimeCard airtimeCard = null;
				Card card = null;
				try {
					//getAirCardByCardNumber may return an instance that's not AirtimeCard, so cast it later
					card = provider.getEquipmentManager().getAirCardByCardNumber(activationCode, provider.getUser(), serialNumber);
				}
				catch (TelusAPIException ex){ //handle any runtime error first
					throw new InvalidAirtimeCardPINException(ex.getMessage());
				}

				try {
					airtimeCard = (AirtimeCard) card;
				}catch (ClassCastException cce) {
					throw new InvalidAirtimeCardException("Card is not an airtime card. card type=["+card.getType()+"]", cce);
				}

				if(!airtimeCard.isLive())
					throw new InvalidAirtimeCardStatusException("Air Time Card is not in live status.");

				if (!airtimeCard.isValidAmountForApp())
					throw new InvalidActivationAmountException("The amount " +amountInCard+" is inavlid amount");

				amountInCard = card.getAmount();   
			}
			else if(activationType == ACTIVATION_TYPE_VIRTUAL_WITH_CHARGE) {
				((TMCreditCard)creditCard).getCreditCardTransactionInfo().getCreditCardHolderInfo().setAccountType("" + AccountSummary.ACCOUNT_TYPE_CONSUMER);
				((TMCreditCard)creditCard).getCreditCardTransactionInfo().getCreditCardHolderInfo().setAccountSubType("" + AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID);
				creditCard.validate("Purchace-now", businessRole, auditHeader); //use new PCI version

			} 
			else if(-1 == activationType) 
				throw new InvalidActivationTypeException("Invalid activation type");

			//-----------------------------------------------
			// Prepare account
			//-----------------------------------------------
			PrepaidConsumerAccountInfo info = PrepaidConsumerAccountInfo.newPCSInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID);
			info.setEvaluationProductType(PRODUCT_TYPE_PCS);
			info.setSerialNumber(serialNumber);
			info.setActivationType(activationType);
			info.setActivationCode(activationCode);
			if (activationType == ACTIVATION_TYPE_VIRTUAL_WITH_CHARGE || activationType == ACTIVATION_TYPE_CREDIT_CARD) 
				info.getActivationCreditCard().copyFrom(((TMCreditCard)creditCard).getDelegate());
			//info.setFidoConversion(isFidoConversion);
			if (null != associatedHandsetIMEI)
				info.setAssociatedHandsetIMEI(associatedHandsetIMEI);
			if (activationType == ACTIVATION_TYPE_AIRTIME_CARD)
				info.setActivationCreditAmount(amountInCard);
			else 
				info.setActivationCreditAmount(activationAmount);
			//-----------------------------------------------
			// Validate ActivationCode.
			//-----------------------------------------------
			if(activationType == ACTIVATION_TYPE_NORMAL) {
				provider.getAccountInformationHelper().validatePayAndTalkSubscriberActivation(provider.getApplication(), provider.getUser(),info, null );
			}

			TMPCSPrepaidConsumerAccount result = new TMPCSPrepaidConsumerAccount(provider, info, equipment);
			//PCI: Need to save auditHeader into ActivationCreditCard0, this is two different instances of TMCreditCard. So that Account.save will have it.
			if ( auditHeader!=null ) result.getActivationCreditCard0().setAuditHeader(auditHeader);
			return result;
		}catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderCreditCardExceptionTranslator(creditCard);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}	
		return null;
	}
	public PCSPrepaidConsumerAccount newPCSPrepaidConsumerAccount(String serialNumber, int activationType, String activationCode, CreditCard creditCard, String businessRole, boolean isFidoConversion, AuditHeader auditHeader )
	throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, InvalidActivationCodeException,
	InvalidCreditCardException, TelusAPIException {

		try {
			//-----------------------------------------------
			// Validate serialNumber.
			//-----------------------------------------------
			Equipment equipment = provider.getEquipmentManager().validateSerialNumber(serialNumber);

			if(equipment.isRIM() || equipment.is1xRTTCard() || this.isWorldPhone(equipment) || this.isPDA(equipment)) {
				throw new InvalidSerialNumberException(serialNumber, InvalidSerialNumberException.EQUIPMENT_NOT_ALLOWED);
			}

			//-----------------------------------------------
			//PCI changes
			//-----------------------------------------------
			if ( creditCard != null ) {
				if ( auditHeader==null ) {
					throw new TelusAPIException( "The required AuditHeader is missing." );
				}
				((TMCreditCard)creditCard).setAuditHeader(auditHeader);
			}

			//-----------------------------------------------
			// Validate Purchace-now creditCard.
			//-----------------------------------------------
			if(activationType == ACTIVATION_TYPE_VIRTUAL_WITH_CHARGE) {
				((TMCreditCard)creditCard).getCreditCardTransactionInfo().getCreditCardHolderInfo().setAccountType("" + AccountSummary.ACCOUNT_TYPE_CONSUMER);
				((TMCreditCard)creditCard).getCreditCardTransactionInfo().getCreditCardHolderInfo().setAccountSubType("" + AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID);
				creditCard.validate("Purchace-now", businessRole, auditHeader);
			}

			//-----------------------------------------------
			// Prepare account
			//-----------------------------------------------
			PrepaidConsumerAccountInfo info = PrepaidConsumerAccountInfo.newPCSInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID);
			info.setEvaluationProductType(PRODUCT_TYPE_PCS);
			info.setSerialNumber(serialNumber);
			info.setActivationType(activationType);
			info.setActivationCode(activationCode);
			info.getActivationCreditCard().copyFrom(((TMCreditCard)creditCard).getDelegate());
			info.setFidoConversion(isFidoConversion);

			//-----------------------------------------------
			// Validate ActivationCode.
			//-----------------------------------------------
			provider.getAccountInformationHelper().validatePayAndTalkSubscriberActivation(provider.getApplication(), provider.getUser(),info, null );
		
			TMPCSPrepaidConsumerAccount result = new TMPCSPrepaidConsumerAccount(provider, info, equipment);
			//PCI: Need to save auditHeader into ActivationCreditCard0, this is two different instances of TMCreditCard. So that Account.save will have it.
			if ( auditHeader!=null ) result.getActivationCreditCard0().setAuditHeader(auditHeader);
			return result;
			
		}catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderCreditCardExceptionTranslator(creditCard);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}	
		return null;
	}

	//  public PCSQuebectelPrepaidConsumerAccount newPCSQuebectelPrepaidConsumerAccount(String productType, String serialNumber)
	//  throws UnknownSerialNumberException, SerialNumberInUseException, NotAnalogSerialNumberException, TelusAPIException {
	//    Equipment equipment = provider.getEquipmentManager().validateSerialNumber(serialNumber);
	//    if(!equipment.isAnalog()){
	//      throw new NotAnalogSerialNumberException("Not Analog Serial Number", serialNumber);
	//    }
	//    QuebectelPrepaidConsumerAccountInfo info = QuebectelPrepaidConsumerAccountInfo.newPCSInstance0();
	//
	//    info.setEvaluationProductType(PRODUCT_TYPE_PCS);
	//    info.setSerialNumber(serialNumber);
	//    return new TMPCSQuebectelPrepaidConsumerAccount(provider, info, equipment);
	//  }

	public IDENPostpaidConsumerAccount newIDENPostpaidConsumerAccount() throws TelusAPIException {
		PostpaidConsumerAccountInfo info = PostpaidConsumerAccountInfo.newIDENInstance();
		info.setEvaluationProductType(PRODUCT_TYPE_IDEN);
		return new TMIDENPostpaidConsumerAccount(provider, info);
	}

	@Deprecated
	public IDENPostpaidEmployeeAccount newIDENPostpaidEmployeeAccount() throws TelusAPIException {
		PostpaidEmployeeAccountInfo info =  PostpaidEmployeeAccountInfo.newIDENInstance0();
		info.setEvaluationProductType(PRODUCT_TYPE_IDEN);
		return new TMIDENPostpaidEmployeeAccount(provider, info);
	}

	@Deprecated
	public IDENPostpaidBusinessRegularAccount newIDENPostpaidBusinessRegularAccount() throws TelusAPIException {
		PostpaidBusinessRegularAccountInfo info = PostpaidBusinessRegularAccountInfo.newIDENInstance();
		info.setEvaluationProductType(PRODUCT_TYPE_IDEN);
		return new TMIDENPostpaidBusinessRegularAccount(provider, info);
	}

	@Deprecated
	public IDENPostpaidBusinessDealerAccount newIDENPostpaidBusinessDealerAccount() throws TelusAPIException {
		PostpaidBusinessDealerAccountInfo info = PostpaidBusinessDealerAccountInfo.newIDENInstance0();
		info.setEvaluationProductType(PRODUCT_TYPE_IDEN);
		return new TMIDENPostpaidBusinessDealerAccount(provider, info);
	}

	@Deprecated
	public IDENPostpaidBusinessPersonalAccount newIDENPostpaidBusinessPersonalAccount() throws TelusAPIException {
		PostpaidBusinessPersonalAccountInfo info = PostpaidBusinessPersonalAccountInfo.newIDENInstance0();
		info.setEvaluationProductType(PRODUCT_TYPE_IDEN);
		return new TMIDENPostpaidBusinessPersonalAccount(provider, info);
	}

	// changed implementation MQ@2005.05.16
	@Deprecated
	public IDENPostpaidCorporateRegularAccount newIDENPostpaidCorporateRegularAccount(char accountSubtype) throws TelusAPIException {
		return newIDENPostpaidCorporateRegularAccount( accountSubtype, 0 );
	}

	//  public IDENPostpaidCorporateVPNAccount newIDENPostpaidCorporateVPNAccount() throws TelusAPIException {
	//    PostpaidCorporateVPNAccountInfo info = PostpaidCorporateVPNAccountInfo.newIDENInstance0();
	//    info.setEvaluationProductType(PRODUCT_TYPE_IDEN);
	//    return new TMIDENPostpaidCorporateVPNAccount(provider, info);
	//  }

	// new implementation MQ@2005.05.16
	public PCSPostpaidCorporateRegularAccount newPCSPostpaidCorporateRegularAccount(char accountSubtype) throws TelusAPIException{
		return newPCSPostpaidCorporateRegularAccount( accountSubtype, 0);
	}

	@Deprecated
	public PagerPostpaidConsumerAccount newPagerPostpaidConsumerAccount() throws TelusAPIException {
		PostpaidConsumerAccountInfo info = PostpaidConsumerAccountInfo.newPagerInstance();
		info.setEvaluationProductType(PRODUCT_TYPE_PAGER);
		return new TMPagerPostpaidConsumerAccount(provider, info);
	}

	@Deprecated
	public PagerPostpaidBusinessRegularAccount newPagerPostpaidBusinessRegularAccount() throws TelusAPIException {
		PostpaidBusinessRegularAccountInfo info = PostpaidBusinessRegularAccountInfo.newPagerInstance();
		info.setEvaluationProductType(PRODUCT_TYPE_PAGER);
		return new TMPagerPostpaidBusinessRegularAccount(provider, info);
	}

	@Deprecated
	public PagerPostpaidBoxedConsumerAccount newPagerPostpaidBoxedConsumerAccount() throws TelusAPIException {
		PostpaidBoxedConsumerAccountInfo info = PostpaidBoxedConsumerAccountInfo.newPagerInstance0();
		info.setEvaluationProductType(PRODUCT_TYPE_PAGER);
		return new TMPagerPostpaidBoxedConsumerAccount(provider, info);
	}

	//  public PagerPostpaidBusinessPersonalAccount newPagerPostpaidBusinessPersonalAccount() throws TelusAPIException {
	//    PostpaidBusinessPersonalAccountInfo info = PostpaidBusinessPersonalAccountInfo.newPagerInstance0();
	//    info.setEvaluationProductType(PRODUCT_TYPE_PAGER);
	//    return new TMPagerPostpaidBusinessPersonalAccount(provider, info);
	//  }

	public Account findAccountByBAN0(int banId) throws TelusAPIException, UnknownBANException {
		
		try {
			AccountInfo info = null;
			info = provider.getAccountInformationHelper().retrieveAccountByBan(banId, Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
			info.setInternalUse(true);
			
			return decorateSubclass(info);
			
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		
		return null;
	}

	public Account findAccountByBAN(int banId) throws TelusAPIException, UnknownBANException {
		
		try {
			return decorateSubclass(provider.getAccountInformationHelper().retrieveAccountByBan(banId, Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		
		return null;
	}

	public Account[] findAccountsByBANs(int[] banId) throws TelusAPIException {
		
		int maxBanLimit = getMaxBanLimit();
		if (banId != null && banId.length > maxBanLimit) {
			throw new LimitExceededException("Maximum BAN search limit[" + maxBanLimit + "] exceeded (" + banId.length + ".");
		}
		try {
			List list = new ArrayList();

			for (int i = 0; i < banId.length; i++) {

				// We have to retrieve each one separatelly in order to decorate
				try {
					AccountInfo accountInfo = null;
					accountInfo = provider.getAccountInformationHelper().retrieveAccountByBan(banId[i], Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
					list.add(decorateSubclass(accountInfo));

				} catch (TelusAPIException tapie) {
					// Ignore - do not add to the list
				} catch (ApplicationException ae) {
					// Ignore - do not add to the list
				}
			}
			return (Account[]) list.toArray(new Account[list.size()]);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		
		return null;
	}

	public Account findAccountByPhoneNumber(String phoneNumber) throws TelusAPIException, UnknownBANException {
		try {
			return decorateSubclass(provider.getAccountInformationHelper().retrieveAccountByPhoneNumber(phoneNumber));

		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public Account findAccountByPhoneNumber(String phoneNumber,PhoneNumberSearchOption phoneNumberSearchOption) throws TelusAPIException, UnknownBANException {
		try {
			return decorateSubclass(provider.getAccountInformationHelper().retrieveAccountByPhoneNumber(phoneNumber,((TMPhoneNumberSearchOption)phoneNumberSearchOption).getDelegate()));

		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}
	
	public Account findLwAccountByPhoneNumber(String phoneNumber) throws TelusAPIException, UnknownBANException {
		try {
			return decorateSubclass(provider.getAccountInformationHelper().retrieveLwAccountByPhoneNumber(phoneNumber));

		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}
	
	public Account findLwAccountByPhoneNumber(String phoneNumber,PhoneNumberSearchOption phoneNumberSearchOption) throws TelusAPIException, UnknownBANException {
		try {
			return decorateSubclass(provider.getAccountInformationHelper().retrieveLwAccountByPhoneNumber(phoneNumber, ((TMPhoneNumberSearchOption)phoneNumberSearchOption).getDelegate()));

		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public AccountSummary[] findAccountsBySerialNumber(String serialNumber) throws TelusAPIException {
		try {
			Collection c;
			c = provider.getAccountInformationHelper().retrieveAccountsBySerialNumber(serialNumber);
			return decorate((AccountSummary[])c.toArray(new AccountSummary[c.size()]));

		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public AccountSummary[] findAccountsByPhoneNumber(String phoneNumber)  throws TelusAPIException {
		try {
			Collection c;
			c = provider.getAccountInformationHelper().retrieveAccountsByPhoneNumber(phoneNumber);
			return decorate((AccountSummary[])c.toArray(new AccountSummary[c.size()]));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public AccountSummary[] findAccountsByPhoneNumber(String phoneNumber, boolean includePastAccounts, boolean onlyLastAccount)  throws TelusAPIException {
		try {
			Collection c;
			c = provider.getAccountInformationHelper().retrieveAccountsByPhoneNumber(phoneNumber, includePastAccounts, onlyLastAccount);
			return decorate((AccountSummary[])c.toArray(new AccountSummary[c.size()]));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public AccountSummary[] findLastAccountsByPhoneNumber(String phoneNumber, PhoneNumberSearchOption phoneNumberSearchOption)  throws TelusAPIException {
		try {
			Collection c;
			c = provider.getAccountInformationHelper().retrieveLatestAccountsByPhoneNumber(phoneNumber, ((TMPhoneNumberSearchOption)phoneNumberSearchOption).getDelegate());
			return decorate((AccountSummary[])c.toArray(new AccountSummary[c.size()]));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}
	
	public AccountSummary[] findAccountsByPostalCode(String lastName, String postalCode, int maximum) throws TelusAPIException {
		int maxBanLimit = getMaxBanLimit();
		if (maximum  > maxBanLimit) {
			throw new LimitExceededException ("Maximum BAN search limit["+maxBanLimit+"] exceeded ("+ maximum +".");
		}

		try {
			Collection c;
			c = provider.getAccountInformationHelper().retrieveAccountsByPostalCode(lastName, postalCode, maximum);
			return decorate((AccountSummary[])c.toArray(new AccountSummary[c.size()]));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public SearchResults findAccountsByBusinessName(String nameType, String legalBusinessName,
			boolean legalBusinessNameExactMatch, char accountStatus,
			char accountType, String provinceCode, int maximum) throws TelusAPIException {

		int maxBanLimit = getMaxBanLimit();
		if (maximum  > maxBanLimit) {
			throw new LimitExceededException ("Maximum BAN search limit["+maxBanLimit+"] exceeded ("+ maximum +".");
		}

		return findAccountsByBusinessName(nameType, legalBusinessName,
				legalBusinessNameExactMatch, accountStatus, accountType,
				provinceCode, Brand.BRAND_ID_TELUS, maximum);

	}

	public SearchResults findAccountsByBusinessName(String nameType,
			String legalBusinessName, boolean legalBusinessNameExactMatch,
			char accountStatus, char accountType, String provinceCode,
			int brandId, int maximum) throws TelusAPIException {

		int maxBanLimit = getMaxBanLimit();
		if (maximum  > maxBanLimit) {
			throw new LimitExceededException ("Maximum BAN search limit["+maxBanLimit+"] exceeded ("+ maximum +".");
		}

		try {
			SearchResultsInfo searchResults = null;
			searchResults = provider.getAccountInformationHelper().retrieveAccountsByBusinessName(nameType,
					legalBusinessName, legalBusinessNameExactMatch,
					accountStatus, accountType, provinceCode, brandId, maximum);
			searchResults.setItems(decorate((AccountSummary[]) searchResults
					.getItems()));
			return searchResults;
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public SearchResults findAccountsByName(String nameType, String firstName, boolean firstNameExactMatch,
			String lastName, boolean lastNameExactMatch, char accountStatus,
			char accountType, String provinceCode, int maximum) throws TelusAPIException {
		int maxBanLimit = getMaxBanLimit();
		if (maximum  > maxBanLimit) {
			throw new LimitExceededException ("Maximum BAN search limit["+maxBanLimit+"] exceeded ("+ maximum +".");
		}

		return findAccountsByName (nameType, firstName, firstNameExactMatch, lastName, lastNameExactMatch, accountStatus, accountType, provinceCode, Brand.BRAND_ID_TELUS, maximum);
	}

	public SearchResults findAccountsByName(String nameType, String firstName,
			boolean firstNameExactMatch, String lastName,
			boolean lastNameExactMatch, char accountStatus, char accountType,
			String provinceCode, int brandId, int maximum)
	throws TelusAPIException {
		int maxBanLimit = getMaxBanLimit();
		if (maximum  > maxBanLimit) {
			throw new LimitExceededException ("Maximum BAN search limit["+maxBanLimit+"] exceeded ("+ maximum +".");
		}

		try {
			SearchResultsInfo searchResults = null;
			searchResults = provider.getAccountInformationHelper()
			.retrieveAccountsByName(nameType, firstName,
					firstNameExactMatch, lastName, lastNameExactMatch,
					accountStatus, accountType, provinceCode, brandId, maximum);
			searchResults.setItems(decorate((AccountSummary[]) searchResults
					.getItems()));
			return searchResults;
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public Account[] findAccountsByDealership(char accountStatus, String dealerCode, Date startDate, int maximum) throws TelusAPIException {
		int maxBanLimit = getMaxBanLimit();
		if (maximum  > maxBanLimit) {
			throw new LimitExceededException ("Maximum BAN search limit["+maxBanLimit+"] exceeded ("+ maximum +".");
		}

		return findAccountsByDealership(accountStatus,dealerCode, startDate, new Date(System.currentTimeMillis() + 86400000), maximum);
	}

	public Account[] findAccountsByDealership(char accountStatus, String dealerCode, Date startDate, Date endDate, int maximum) throws TelusAPIException {
		int maxBanLimit = getMaxBanLimit();
		if (maximum > maxBanLimit) {
			throw new LimitExceededException("Maximum BAN search limit[" + maxBanLimit + "] exceeded (" + maximum + ".");
		}

		try {			
			Collection c = null;
			c = provider.getAccountInformationHelper().retrieveAccountsByDealership(accountStatus,dealerCode, startDate, endDate, maximum);
			return decorate((Account[])c.toArray(new Account[c.size()]));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public Account[] findAccountsBySalesRep(char accountStatus, String dealerCode, String salesRepCode, Date startDate, int maximum) throws TelusAPIException {
		int maxBanLimit = getMaxBanLimit();
		if (maximum  > maxBanLimit) {
			throw new LimitExceededException ("Maximum BAN search limit["+maxBanLimit+"] exceeded ("+ maximum +".");
		}
		return findAccountsBySalesRep(accountStatus,dealerCode, salesRepCode, startDate, new Date(System.currentTimeMillis() + 86400000), maximum);
	}

	public Account[] findAccountsBySalesRep(char accountStatus, String dealerCode, String salesRepCode, Date startDate, Date endDate, int maximum) throws TelusAPIException {
		int maxBanLimit = getMaxBanLimit();
		if (maximum  > maxBanLimit) {
			throw new LimitExceededException ("Maximum BAN search limit["+maxBanLimit+"] exceeded ("+ maximum +".");
		}

		try {
			Collection c = null;
			c = provider.getAccountInformationHelper().retrieveAccountsBySalesRep(accountStatus, dealerCode, salesRepCode, startDate, endDate, maximum);
			return decorate((Account[])c.toArray(new Account[c.size()]));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public Subscriber findSubscriberByPhoneNumber0(String phoneNumber) throws TelusAPIException, UnknownSubscriberException {
		try {
			SubscriberInfo info = provider.getSubscriberLifecycleHelper().retrieveSubscriberByPhoneNumber(phoneNumber);
			info.setInternalUse(true);
			return decorate(info, true);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}
	public Subscriber findSubscriberByPhoneNumber(String phoneNumber) throws TelusAPIException, UnknownSubscriberException {
		try {
			SubscriberInfo info = provider.getSubscriberLifecycleHelper().retrieveSubscriberByPhoneNumber(phoneNumber);
			return decorate(info, true);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public Subscriber findSubscriberByPhoneNumber(String phoneNumber,PhoneNumberSearchOption phoneNumberSearchOption) throws TelusAPIException, UnknownSubscriberException {
		try {
			SubscriberInfo info = provider.getSubscriberLifecycleHelper().retrieveSubscriberByPhoneNumber(phoneNumber, ((TMPhoneNumberSearchOption)phoneNumberSearchOption).getDelegate());
			return decorate(info, true);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public Subscriber[] findSubscribersBySerialNumber(String serialNumber) throws UnknownSubscriberException, TelusAPIException {
		try {
			Collection c = provider.getSubscriberLifecycleHelper().retrieveSubscriberListBySerialNumber(serialNumber);
			return decorate((Subscriber[])c.toArray(new Subscriber[c.size()]), true, false);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public Subscriber[] findSubscribersBySerialNumber(String serialNumber,  boolean includeCancelled) throws UnknownSubscriberException, TelusAPIException {
		try {
			Collection c = provider.getSubscriberLifecycleHelper().retrieveSubscriberListBySerialNumber(serialNumber, includeCancelled);
			return decorate((Subscriber[])c.toArray(new Subscriber[c.size()]), true, false);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public Subscriber[] findSubscribersByBAN(int banId, int maximum) throws TelusAPIException, UnknownBANException {
		int maxSubLimit = getMaxSubLimit();
		if (maximum  > maxSubLimit) {
			throw new LimitExceededException ("Maximum subscriber search limit["+maxSubLimit+"] exceeded ("+ maximum +".");
		}

		try {
			Collection c = provider.getSubscriberLifecycleHelper().retrieveSubscriberListByBAN(banId, maximum);
			return decorate((Subscriber[])c.toArray(new Subscriber[c.size()]), true, false);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public Subscriber[] findPortedSubscribersByBAN(int banId, int maximum) throws TelusAPIException, UnknownBANException {
		try {
			Collection c = provider.getSubscriberLifecycleHelper().retrievePortedSubscriberListByBAN(banId, maximum);
			return decorate((Subscriber[])c.toArray(new Subscriber[c.size()]), true, false);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public Subscriber[] findSubscribersByPhoneNumber0(String phoneNumber,int maximum, boolean includeCancelled) throws TelusAPIException, UnknownBANException {
		int maxSubLimit = getMaxSubLimit();
		if (maximum  > maxSubLimit) {
			throw new LimitExceededException ("Maximum subscriber search limit["+maxSubLimit+"] exceeded ("+ maximum +".");
		}
		try {
			Collection c = provider.getSubscriberLifecycleHelper().retrieveSubscriberListByPhoneNumber(phoneNumber,maximum, includeCancelled);
			return decorate((Subscriber[])c.toArray(new Subscriber[c.size()]), true, true);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	} 


	public Subscriber[] findSubscribersByPhoneNumber(String phoneNumber, int maximum, boolean includeCancelled) throws TelusAPIException, UnknownBANException {
		int maxSubLimit = getMaxSubLimit();
		if (maximum > maxSubLimit) {
			throw new LimitExceededException("Maximum subscriber search limit[" + maxSubLimit + "] exceeded (" + maximum + ".");
		}
		try {
			Collection c = provider.getSubscriberLifecycleHelper().retrieveSubscriberListByPhoneNumber(phoneNumber,maximum, includeCancelled);
			return decorate((Subscriber[])c.toArray(new Subscriber[c.size()]), true, false);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}
	
	public Subscriber[] findSubscribersByPhoneNumber(String phoneNumber, PhoneNumberSearchOption phoneNumberSearchOption, int maximum, boolean includeCancelled) throws TelusAPIException, UnknownBANException {
		try {
			
			Collection c = provider.getSubscriberLifecycleHelper().retrieveSubscriberListByPhoneNumber(phoneNumber, ((TMPhoneNumberSearchOption)phoneNumberSearchOption).getDelegate(), maximum, includeCancelled);
			return decorate((Subscriber[])c.toArray(new Subscriber[c.size()]), true, false);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}
	
	
	public Subscriber[] findSubscribersByBAN(int banId, int maximum, boolean includeCancelled) throws TelusAPIException, UnknownBANException {
		int maxSubLimit = getMaxSubLimit();
		if (maximum  > maxSubLimit) {
			throw new LimitExceededException ("Maximum subscriber search limit["+maxSubLimit+"] exceeded ("+ maximum +".");
		}
		try {
			Collection c = provider.getSubscriberLifecycleHelper().retrieveSubscriberListByBAN(banId, maximum, includeCancelled);
			return decorate((Subscriber[])c.toArray(new Subscriber[c.size()]), true, false);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public IDENSubscriber[] findSubscribersByBanAndFleet(int banId, int urbanId, int fleetId, int maximum) throws TelusAPIException, UnknownBANException {
		int maxSubLimit = getMaxSubLimit();
		if (maximum  > maxSubLimit) {
			throw new LimitExceededException ("Maximum subscriber search limit["+maxSubLimit+"] exceeded ("+ maximum +".");
		}

		try {
			Collection c = provider.getSubscriberLifecycleHelper().retrieveSubscriberListByBanAndFleet(banId, urbanId, fleetId, maximum);
			return decorate((IDENSubscriber[])c.toArray(new IDENSubscriber[c.size()]), true, false);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}


	public IDENSubscriber[] findSubscribersByBanAndTalkGroup(int banId, int urbanId, int fleetId, int talkGroupId, int maximum) throws TelusAPIException, UnknownBANException {
		int maxSubLimit = getMaxSubLimit();
		if (maximum  > maxSubLimit) {
			throw new LimitExceededException ("Maximum subscriber search limit["+maxSubLimit+"] exceeded ("+ maximum +".");
		}

		try {
			Collection c = provider.getSubscriberLifecycleHelper().retrieveSubscriberListByBanAndTalkGroup(banId, urbanId, fleetId, talkGroupId, maximum);
			return (IDENSubscriber[])decorate((Subscriber[])c.toArray(new IDENSubscriber[c.size()]), true, false);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}


	public TaxSummary calculateTax(double amount, String provinceCode, boolean isGSTExempt, boolean isPSTExempt, boolean isHSTExempt) throws TelusAPIException {
			TaxExemptionInfo taxExemptionInfo = new TaxExemptionInfo();
			taxExemptionInfo.setGstExemptionInd(isGSTExempt);
			taxExemptionInfo.setPstExemptionInd(isPSTExempt);
			taxExemptionInfo.setHstExemptionInd(isHSTExempt);
			return provider.getBillingInquiryReferenceFacade().getTaxCalculationListByProvince(provinceCode, amount, taxExemptionInfo);	
	}

	public TaxSummary calculateTax(double amount, AccountSummary account) throws TelusAPIException {
		// use account home province not province of account's address
		// fixed: PROD00020414
		return calculateTax(amount, account.getAccount().getHomeProvince(), account.isGSTExempt(), account.isPSTExempt(), account.isHSTExempt());
	}
	
	


	// This method is used to round currency (to 2 decimal digits) before passing value
	// into the 'format' method of the java.text.NumberFormat object
	private double roundCurrency(double amt) {
		double dblX = amt * 100;
		long lngX = Math.round(dblX);
		dblX = (double) lngX/100;
		return dblX;
	}

	//-----------------------------------------------------------------------------------
	// Helper methods.
	//-----------------------------------------------------------------------------------
	/* *
	 * Asserts a credit card's ability to accept transactions.  This method
	 * returns normally if the card is good, otherwise it throws an exception.
	 *
	 * <P>Since no specific amount is associated with this assertion, an
	 * actual debit of this card may still fail.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @exception InvalidCreditCardException the card is unknown or unable to accept transactions.
	 *
	 * /
  public void validateCreditCard(String reason, CreditCardInfo card) throws TelusAPIException, InvalidCreditCardException {
    try {
      String authorizationNumber = provider.getAccountManagerEJB().validateCreditCard(card);
    } catch (TelusException e){
      provider.checkServiceFailure(e);
      if ("APP10003".equals(e.id) || "APP10005".equals(e.id)) {
        throw new InvalidCreditCardException(reason, e, card);
      } else {
        throw new TelusAPIException(e);
      }
    } catch (Throwable e){
      throw new TelusAPIException(e);
    }
  }
	 */


	//-----------------------------------------------------------------------------------
	// Decorators.
	//-----------------------------------------------------------------------------------
	private Subscriber decorate(Subscriber subscriber, boolean existingSubscriber) throws TelusAPIException, UnknownBANException {
		TMSubscriber tmSubscriber = null;
		if (subscriber.isIDEN()) {
			tmSubscriber = new TMIDENSubscriber(provider, (IDENSubscriberInfo) subscriber, !existingSubscriber, null);
		}
		else if (subscriber.isPCS()) {
			tmSubscriber = new TMPCSSubscriber(provider, (PCSSubscriberInfo) subscriber, !existingSubscriber, null);
		}
		else if (subscriber.isPager()) {
			tmSubscriber =  new TMPagerSubscriber(provider, (PagerSubscriberInfo) subscriber, !existingSubscriber, null);
		}
		else if (subscriber.isTango()) {
			tmSubscriber = new TMTangoSubscriber(provider, (TangoSubscriberInfo) subscriber, !existingSubscriber, null);
		}
		else if (subscriber.isCDPD()) {
			tmSubscriber=  new TMCDPDSubscriber(provider, (CDPDSubscriberInfo) subscriber, !existingSubscriber, null);
		}
		else
			throw new IllegalArgumentException("Invalid subscriber product type: " + subscriber.getProductType());
		if (!((SubscriberInfo)subscriber).isInternalUse()){
			return ReferenceDataManager.Helper.filterSubscriberByBrand(tmSubscriber, provider.getSupportedBrandIds());
		}
		return tmSubscriber;
	}

	private IDENSubscriber decorate(IDENSubscriber subscriber, boolean existingSubscriber) throws TelusAPIException, UnknownBANException {
		return new TMIDENSubscriber(provider, (IDENSubscriberInfo)subscriber, !existingSubscriber, null);
	}

	private Subscriber[] decorate(Subscriber[] subscribers, boolean existingSubscriber, boolean internalUse) throws TelusAPIException, UnknownBANException {
		subscribers = ReferenceDataManager.Helper.filterSubscribersByBrand(subscribers, provider.getSupportedBrandIds(), internalUse);
		for(int i=0; i<subscribers.length; i++) {   
			((SubscriberInfo)subscribers[i]).setInternalUse(internalUse);
			subscribers[i] = decorate(subscribers[i], existingSubscriber);
		}

		return subscribers;
	}

	private IDENSubscriber[] decorate(IDENSubscriber[] subscribers, boolean existingSubscriber, boolean internalUse) throws TelusAPIException, UnknownBANException {
		subscribers = (IDENSubscriber[])ReferenceDataManager.Helper.filterSubscribersByBrand(subscribers, provider.getSupportedBrandIds(), internalUse);
		for(int i=0; i<subscribers.length; i++) {
			((SubscriberInfo)subscribers[i]).setInternalUse(internalUse);
			subscribers[i] = decorate(subscribers[i], existingSubscriber);
		}
		return subscribers;
	}

	private AccountSummary[] decorate(AccountSummary[] accounts) throws TelusAPIException, UnknownBANException {
		accounts = ReferenceDataManager.Helper.filterAccountByBrand(accounts, provider.getSupportedBrandIds()); 
		for(int i=0; i<accounts.length; i++) {
			accounts[i] = new TMAccountSummary(provider, (AccountInfo)accounts[i]);
		}

		return accounts;
	}

	private Account[] decorate(Account[] accounts) throws TelusAPIException, UnknownBANException {
		accounts = (Account[])ReferenceDataManager.Helper.filterAccountByBrand(accounts, provider.getSupportedBrandIds());
		for(int i=0; i<accounts.length; i++) {
			TMAccount a = new TMAccount(provider, (AccountInfo)accounts[i]);
			a.commit();

			//--------------------------------------------------------
			// Store the old address for reporting if it changes
			//--------------------------------------------------------
			if (a.getBanId() != 0) {
				a.setupOldData();
			}


			accounts[i] = a;
		}

		return accounts;
	}

	private Account decorate(AccountInfo account) throws TelusAPIException, UnknownBANException {
		TMAccount a = new TMAccount(provider, (AccountInfo)account);
		a.commit();

		//--------------------------------------------------------
		// Store the old address for reporting if it changes
		//--------------------------------------------------------
		if (a.getBanId() != 0) {
			a.setupOldData();
		}


		return a;
	}

	private ActivationCredit[] decorate(ActivationCreditInfo[] credits) throws TelusAPIException, UnknownBANException {
		TMActivationCredit[] tmCredits = new TMActivationCredit[credits.length];
		for(int i=0; i<credits.length; i++) {
			TMActivationCredit tmCredit = new TMActivationCredit(provider, (ActivationCreditInfo)credits[i], null);
			tmCredits[i] = tmCredit;
		}
		return tmCredits;
	}

	private Account decorateSubclass(AccountInfo account) throws TelusAPIException, UnknownBANException {
		TMAccount a = null;

		if (account.isIDEN()) {
			if (account.isPostpaidEmployee()) {
				a = new TMIDENPostpaidEmployeeAccount(provider, (PostpaidEmployeeAccountInfo) account);
			}
			else if (account.isPostpaidBusinessPersonal()) {
				a = new TMIDENPostpaidBusinessPersonalAccount(provider, (PostpaidBusinessPersonalAccountInfo) account);
			}
			else if (account.isPostpaidBusinessDealer()) {
				a = new TMIDENPostpaidBusinessDealerAccount(provider, (PostpaidBusinessDealerAccountInfo) account);
			}
			else if (account.isPostpaidBusinessRegular()) {
				a = new TMIDENPostpaidBusinessRegularAccount(provider, (PostpaidBusinessRegularAccountInfo) account);
			}
			else if (account.isPostpaidConsumer()) {
				a = new TMIDENPostpaidConsumerAccount(provider, (PostpaidConsumerAccountInfo) account);
			}
			else if (account.isCorporateRegular()) {
				a = new TMIDENPostpaidCorporateRegularAccount(provider, (PostpaidCorporateRegularAccountInfo) account);
			}
		}
		else if (account.isPCS()) {
			if (account.isPostpaidBusinessPersonal()) {
				a = new TMPCSPostpaidBusinessPersonalAccount(provider, (PostpaidBusinessPersonalAccountInfo) account);
			}
			else if (account.isPostpaidBusinessOfficial()) {
				a = new TMPCSPostpaidBusinessOfficialAccount(provider, (PostpaidBusinessOfficialAccountInfo) account);
			}
			else if (account.isPostpaidBusinessDealer()) {
				a = new TMPCSPostpaidBusinessDealerAccount(provider, (PostpaidBusinessDealerAccountInfo) account);
			}
			else if (account.isPostpaidBusinessRegular()) {
				a = new TMPCSPostpaidBusinessRegularAccount(provider, (PostpaidBusinessRegularAccountInfo) account);
			}
			else if (account.isPostpaidEmployee()) {
				a = new TMPCSPostpaidEmployeeAccount(provider, (PostpaidEmployeeAccountInfo) account);
			}
			else if (account.isPostpaidConsumer()) {
				a = new TMPCSPostpaidConsumerAccount(provider, (PostpaidConsumerAccountInfo) account);
			}
			else if (account.isWesternPrepaidConsumer()) {
				a = new TMPCSWesternPrepaidConsumerAccount(provider, (WesternPrepaidConsumerAccountInfo) account);
			}
			else if (account.isPrepaidConsumer()) {
				a = new TMPCSPrepaidConsumerAccount(provider, (PrepaidConsumerAccountInfo) account);
			}
			if (account.isPostpaidCorporatePersonal()) {
				a = new TMPCSPostpaidCorporatePersonalAccount(provider, (PostpaidCorporatePersonalAccountInfo) account);
			}
			if (account.isPostpaidCorporateRegular()) {
				a = new TMPCSPostpaidCorporateRegularAccount(provider, (PostpaidCorporateRegularAccountInfo) account);
			}
		}
		else if (account.isPager()) {
			if (account.isPostpaidBusinessPersonal()) {
				a = new TMPagerPostpaidBusinessPersonalAccount(provider, (PostpaidBusinessPersonalAccountInfo) account);
			}
			else if (account.isPostpaidBusinessRegular()) {
				a = new TMPagerPostpaidBusinessRegularAccount(provider, (PostpaidBusinessRegularAccountInfo) account);
			}
			else if (account.isPostpaidConsumer()) {
				a = new TMPagerPostpaidConsumerAccount(provider, (PostpaidConsumerAccountInfo) account);
			}
			else if (account.isPostpaidBoxedConsumer()) {
				a = new TMPagerPostpaidBoxedConsumerAccount(provider, (PostpaidBoxedConsumerAccountInfo) account);
			}
		}
		else if (account.isAutotel()) {
			if (account.isPostpaidBusinessRegular()) {
				a = new TMPostpaidBusinessRegularAccount(provider, (PostpaidBusinessRegularAccountInfo) account);
			}
			else if (account.isPostpaidConsumer()) {
				a = new TMPostpaidConsumerAccount(provider, (PostpaidConsumerAccountInfo) account);
			}
			else if (account.isCorporate()) {
				a = new TMPostpaidCorporateRegularAccount(provider, (PostpaidCorporateRegularAccountInfo) account);
			}
		}

		//------------------------------------------------------------
		// Since the accounts we don't directly support are generally
		// corporate, decorate them as BusinessRegular.
		//------------------------------------------------------------
		if (a == null && account instanceof PostpaidBusinessRegularAccountInfo) {
			if (account.isIDEN()) {
				a = new TMIDENPostpaidBusinessRegularAccount(provider, (PostpaidBusinessRegularAccountInfo) account);
			}
			else if (account.isPCS()) {
				a = new TMPCSPostpaidBusinessRegularAccount(provider, (PostpaidBusinessRegularAccountInfo) account);
			}
			else if (account.isPager()) {
				a = new TMPagerPostpaidBusinessRegularAccount(provider, (PostpaidBusinessRegularAccountInfo) account);
			}
			else if (account.isAutotel()) {
				a = new TMPostpaidBusinessRegularAccount(provider, (PostpaidBusinessRegularAccountInfo) account);
			}
		}

		//------------------------------------------------------------
		// Decorate everything else as a base account.
		//------------------------------------------------------------
		if (a == null) {
			a = new TMAccount(provider, (AccountInfo) account);
		}
		a.commit();

		//--------------------------------------------------------
		// Store the old address for reporting if it changes
		//--------------------------------------------------------
		if (a.getBanId() != 0) {
			a.setupOldData();
		}
		if (!account.isInternalUse()) {
			return ReferenceDataManager.Helper.filterAnAccountByBrand(a,
					provider.getSupportedBrandIds());
		}
		return a;
	}

	public Discount decorate(Discount discount) {
		// TODO: don't decorate TMDiscount again, use instanceof
		return new TMDiscount(provider, (DiscountInfo)discount);
	}

	public Discount[] decorate(Discount[] discounts) {
		Discount[] decoratedDiscounts = new Discount[discounts.length];
		for(int i=0; i<discounts.length; i++) {
			decoratedDiscounts[i] = decorate(discounts[i]);
		}
		return decoratedDiscounts;
	}

	public Credit decorate(Credit credit, AccountSummary accountSummary) {
		// TODO: don't decorate TMDiscount again, use instanceof
		return new TMCredit(provider, (CreditInfo)credit, accountSummary);
	}

	public Credit[] decorate(Credit[] credits, AccountSummary accountSummary) {
		Credit[] decoratedCredits = new Credit[credits.length];
		for(int i=0; i<credits.length; i++) {
			decoratedCredits[i] = decorate(credits[i],accountSummary);
		}
		return decoratedCredits;
	}

	public PrepaidEventHistory decorate(PrepaidEventHistory history) throws TelusAPIException {
		PrepaidEventHistoryInfo info = (PrepaidEventHistoryInfo)history;
		info.setEventType(provider.getReferenceDataManager().getPrepaidEventType(info.getPrepaidEventTypeCode()));
		return history;
	}

	public PrepaidEventHistory[] decorate(PrepaidEventHistory[] history) throws TelusAPIException {
		for(int i=0; i<history.length; i++) {
			decorate(history[i]);
		}
		return history;
	}

	public AutoTopUp decorate(AutoTopUp topUp, boolean existingAutoTopUp) throws TelusAPIException {
		return new TMAutoTopUp(provider, (AutoTopUpInfo)topUp, existingAutoTopUp);
	}

	public AutoTopUp[] decorate(AutoTopUp[] topUp, boolean existingAutoTopUp) throws TelusAPIException {
		for(int i=0; i<topUp.length; i++) {
			decorate(topUp[i], existingAutoTopUp);
		}
		return topUp;
	}

	public ActivationCredit newActivationCredit( String creditType, int contractTerm,
			Date effectiveDate, Date expiryDate,
			String description, String descriptionFrench,
			double amount, String barcode, String productType )
	throws TelusAPIException
	{
		return newActivationCredit(null, creditType, contractTerm, effectiveDate,
				expiryDate, description, descriptionFrench,
				amount, barcode, productType );
	}

	public ActivationCredit newActivationCredit( String offerCode, String creditType, int contractTerm,
			Date effectiveDate, Date expiryDate,
			String description, String descriptionFrench,
			double amount, String barcode, String productType )
	throws TelusAPIException
	{
		try {
			ActivationCreditInfo creditInfo = new ActivationCreditInfo(ActivationCreditInfo.TAX_OPTION_NO_TAX);
			creditInfo.setOfferCode(offerCode);
			creditInfo.setCode(creditType);
			creditInfo.setCreditType(creditType);
			creditInfo.setContractTerm(contractTerm);
			creditInfo.setEffectiveDate(effectiveDate);
			creditInfo.setExpiryDate(expiryDate);
			creditInfo.setDescription(description);
			creditInfo.setText(description); // assume english
			creditInfo.setDescriptionFrench(descriptionFrench);
			creditInfo.setAmount(amount);
			creditInfo.setBarCode(barcode);
			creditInfo.setProductType(productType);

			ActivationCredit[] credits = decorate(new ActivationCreditInfo[]{creditInfo});
			return credits[0];
		} catch (TelusAPIException e) {
			throw e;
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	/**
	 * @deprecated use P3MS EJB method
	 * 
	 */
	public ActivationCredit[] getActivationCredits(String serialNumber, String province, String npa, int contractTermMonths, String pricePlan, boolean fidoConversion) throws TelusAPIException
	{
		try {
			ActivationCreditInfo[] credits = null;
			credits = provider.getProductEquipmentHelper().getActivationCredits(serialNumber,
					province, npa, contractTermMonths, pricePlan, fidoConversion);
			return decorate(credits);
		} catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(serialNumber);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
		return null;
	}
	
	public ActivationCredit[] getActivationCreditsByDate(String serialNumber, String province, String npa, int contractTermMonths, Date activationDate, boolean fidoConversion) throws TelusAPIException {
		try {
			ActivationCreditInfo[] credits = null;
			credits = provider.getProductEquipmentHelper().getActivationCredits(serialNumber,
					province, npa, contractTermMonths, activationDate, fidoConversion);
			return decorate(credits);
		} catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(serialNumber);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
		return null;
	}
	
	public ActivationCredit[] getActivationCreditsByDate(String serialNumber, String province, String npa, int contractTermMonths, Date activationDate) throws TelusAPIException {
		return getActivationCreditsByDate(serialNumber, province, npa, contractTermMonths, activationDate, false);
	}

	public ActivationCredit[] getActivationCreditsByCreditType(String serialNumber, String province, String npa, String creditType) throws TelusAPIException {
		try {
			ActivationCreditInfo[] credits = null;
			credits = provider.getProductEquipmentHelper().getActivationCredits(serialNumber,
					province, npa, creditType);
			return decorate(credits);
		} catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(serialNumber);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
		return null;
	}

	/**
	 * @deprecated use P3MS EJB method
	 * 
	 */
	public java.util.HashMap getActivationCreditsByProductCodes(String[] productCodes, String province, String npa, int contractTermMonths, String[] productTypes, boolean isInitialActivation) throws TelusAPIException {
		try {
			java.util.HashMap map = null;
			map = provider.getProductEquipmentHelper().getActivationCreditsByProductCodes(productCodes, province, npa, contractTermMonths, null, false, productTypes, isInitialActivation );
			java.util.HashMap newMap = new java.util.HashMap();
			java.util.Set keySet = map.keySet();
			java.util.Iterator itr = keySet.iterator();
			while( itr.hasNext() ) {
				String productCode = (String)itr.next();
				ActivationCreditInfo[] credits = (ActivationCreditInfo[]) map.get(productCode);
				newMap.put( productCode, decorate(credits) );
			}
			return newMap;
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public PhoneNumberReservation newPhoneNumberReservation(){
		return new TMPhoneNumberReservation();
	}

	public PhoneNumberSearchOption newPhoneNumberSearchOption(){
		return new TMPhoneNumberSearchOption();
	}
	
	public void restoreSuspendedAccount(int pBan, Date pRestoreDate, String pRestoreReasonCode, String pRestoreComment, boolean collectionSuspensionsOnly,Integer brandId,Boolean isPostpaidBusinessConnect) throws TelusAPIException {
		try {
			provider.getAccountLifecycleFacade().restoreSuspendedAccount(pBan,
					pRestoreDate, pRestoreReasonCode, pRestoreComment,
					collectionSuspensionsOnly, brandId,isPostpaidBusinessConnect,SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
		}
		catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	public PrepaidEventHistory[] getPrepaidEventHistory(String phoneNumber, Date startDate, Date endDate) throws TelusAPIException{
		try {
			Collection c =  provider.getSubscriberLifecycleHelper().retrievePrepaidEventHistory(phoneNumber, startDate, endDate);
			return decorate((PrepaidEventHistoryInfo[])c.toArray(new PrepaidEventHistoryInfo[c.size()]));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public PrepaidEventHistory[] getPrepaidEventHistory(String phoneNumber, Date startDate, Date endDate, PrepaidEventType[] eventTypes) throws TelusAPIException{
		try {
			PrepaidEventTypeInfo[] info = new PrepaidEventTypeInfo[eventTypes.length];
			System.arraycopy(eventTypes, 0, info, 0, eventTypes.length);
			
			Collection c = provider.getSubscriberLifecycleHelper().retrievePrepaidEventHistory(phoneNumber, startDate, endDate, info);
			return decorate((PrepaidEventHistoryInfo[])c.toArray(new PrepaidEventHistoryInfo[c.size()]));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public PrepaidCallHistory[] getPrepaidCallHistory(String phoneNumber, Date startDate, Date endDate) throws TelusAPIException {
		try {
			Collection c = provider.getSubscriberLifecycleHelper().retrievePrepaidCallHistory(phoneNumber, startDate, endDate);
			PrepaidCallHistoryInfo[] history = (PrepaidCallHistoryInfo[])c.toArray(new PrepaidCallHistoryInfo[c.size()]);
			return convertRateIdToRate(history);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	/**
	 * Returns an array of follow ups corresponding to the provided search criteria.
	 *
	 * @param followUpCriteria
	 * @return
	 * @throws TelusAPIException
	 */
	 public FollowUp[] findFollowUps(FollowUpCriteria followUpCriteria) throws TelusAPIException {
		// return object
		TMFollowUp[] followUps = null;
		try {
			FollowUpCriteriaInfo info = ((TMFollowUpCriteria)followUpCriteria).getFollowUpCriteria0();

			FollowUpInfo[] followUpInfos = null;
			Collection c = provider.getAccountInformationHelper().retrieveFollowUps(info);
			followUpInfos = (FollowUpInfo[])c.toArray(new FollowUpInfo[c.size()]);

			int followUpInfosSize = followUpInfos != null ? followUpInfos.length : 0;

			followUps = new TMFollowUp[followUpInfosSize];

			for (int i = 0; i < followUpInfosSize; i++)
				followUps[i] = new TMFollowUp(provider, followUpInfos[i]);
		}
		catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return followUps;
	 }

	 /**
	  * Returns an array of Memo objects for a given set of criteria as specified in the MemoCriteria object.
	  * The criteria will be validated and an exception will be thrown if found to be invalid.
	  *
	  * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	  * elements, but may contain no (zero) elements.
	  *
	  * <P>This method may involve a remote method call.
	  */
	 public Memo[] findMemos(MemoCriteria criteria) throws TelusAPIException {
		 try {
			 MemoCriteriaInfo info = ((TMMemoCriteria)criteria).getMemoCriteria0();
			 Collection c = provider.getAccountInformationHelper().retrieveMemos(info);
			 return (Memo[])c.toArray(new Memo[c.size()]);
		 } catch (Throwable t) {
				provider.getExceptionHandler().handleException(t);
		}
		return null; 
	 }

	 /**
	  * Returns a new instance of MemoCriteria for use in <CODE>findMemo</CODE>.
	  */
	 public MemoCriteria newMemoCriteria() throws TelusAPIException {
		 return new TMMemoCriteria();
	 }

	 /**
	  * Returns a new instance of MemoCriteria for use in <CODE>findMemo</CODE>.
	  */
	 public FollowUpCriteria newFollowUpCriteria() throws TelusAPIException {
		 return new TMFollowUpCriteria();
	 }

	 /**
	  * Changes account type and returns the new object.
	  *
	  * @param account
	  * @param accountType
	  * @param accountSubType
	  * @return
	  * @throws InvalidAccountTypeChangeException
	  * @throws TelusAPIException
	  */
	 public Account changeAccountType(Account account, char accountType, char accountSubType) throws InvalidAccountTypeChangeException, TelusAPIException {
		 // return object
		 Account newAccount = null;
		 AccountInfo newAccountInfo = null;

		 // create appropriate info object
		 if (!account.validTargetAccountType(accountType, accountSubType)) {
			 StringBuffer sb = new StringBuffer();
			 sb.append("Cannot change this account (").append( account.getBanId()).append(") 's account type from [")
			 .append(account.getAccountType()).append("/").append(account.getAccountSubType())
			 .append("] to [").append(accountType).append("/").append(accountSubType).append("]");
			 //throw new InvalidAccountTypeChangeException("Cannot change this account to account type [" + accountType + "] and account subtype [" + accountSubType + "]...");
			 throw new InvalidAccountTypeChangeException( sb.toString() );
		 }

		 if (accountType == Account.ACCOUNT_TYPE_BUSINESS && 
				 (accountSubType == Account.ACCOUNT_SUBTYPE_PCS_PERSONAL ||accountSubType ==Account.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL || accountSubType ==Account.ACCOUNT_SUBTYPE_PCS_CONNECT_PERSONAL)) {
			 newAccountInfo = PostpaidBusinessPersonalAccountInfo.getNewInstance0(accountSubType);
			 newAccount = new TMPCSPostpaidBusinessPersonalAccount(provider, (PostpaidBusinessPersonalAccountInfo) newAccountInfo);
		 }
		 else if (accountType == Account.ACCOUNT_TYPE_BUSINESS && accountSubType == Account.ACCOUNT_SUBTYPE_IDEN_PERSONAL) {
			 newAccountInfo = PostpaidBusinessPersonalAccountInfo.newIDENInstance0();
			 newAccount = new TMIDENPostpaidBusinessPersonalAccount(provider, (PostpaidBusinessPersonalAccountInfo) newAccountInfo);
		 }
		 else if (accountType == Account.ACCOUNT_TYPE_CONSUMER && accountSubType == Account.ACCOUNT_SUBTYPE_PCS_REGULAR) {
			 newAccountInfo = PostpaidConsumerAccountInfo.newPCSInstance();
			 newAccount = new TMPCSPostpaidConsumerAccount(provider, (PostpaidConsumerAccountInfo) newAccountInfo);
		 }
		 else if (accountType == Account.ACCOUNT_TYPE_CONSUMER && accountSubType == Account.ACCOUNT_SUBTYPE_IDEN_REGULAR) {
			 newAccountInfo = PostpaidConsumerAccountInfo.newIDENInstance();
			 newAccount = new TMIDENPostpaidConsumerAccount(provider, (PostpaidConsumerAccountInfo) newAccountInfo);
		 }

		 //SmartDesktop CR40, Aug 2008 release: BAN type change: corporate <=> business ban type change
		 else if ( accountType == Account.ACCOUNT_TYPE_BUSINESS ) {
			 switch (accountSubType) {
			 case Account.ACCOUNT_SUBTYPE_IDEN_DEALER:
				 newAccountInfo = PostpaidBusinessDealerAccountInfo.newIDENInstance0();
				 newAccount = new TMIDENPostpaidBusinessDealerAccount(provider, (PostpaidBusinessDealerAccountInfo) newAccountInfo);
				 break;
			 case Account.ACCOUNT_SUBTYPE_PCS_DEALER:
				 newAccountInfo = PostpaidBusinessDealerAccountInfo.newPCSInstance0();
				 newAccount = new TMPCSPostpaidBusinessDealerAccount(provider, (PostpaidBusinessDealerAccountInfo) newAccountInfo);
				 break;
			 case Account.ACCOUNT_SUBTYPE_IDEN_REGULAR:
				 newAccountInfo = PostpaidBusinessRegularAccountInfo.newIDENInstance();
				 newAccount = new TMIDENPostpaidBusinessRegularAccount(provider, (PostpaidBusinessRegularAccountInfo) newAccountInfo);
				 break;
			 case Account.ACCOUNT_SUBTYPE_PCS_REGULAR:
			 case Account.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR:
			 case Account.ACCOUNT_SUBTYPE_REGULAR_MEDIUM:
			 case Account.ACCOUNT_SUBTYPE_PCS_CONNECT_REGULAR:
				 newAccountInfo = PostpaidBusinessRegularAccountInfo.getNewInstance(accountSubType);
				 newAccount = new TMPCSPostpaidBusinessRegularAccount(provider, (PostpaidBusinessRegularAccountInfo) newAccountInfo);
				 break;
			 case Account.ACCOUNT_SUBTYPE_PCS_OFFICAL:
				 newAccountInfo = PostpaidBusinessOfficialAccountInfo.newPCSInstance();
				 newAccount = new TMPCSPostpaidBusinessOfficialAccount(provider, (PostpaidBusinessOfficialAccountInfo) newAccountInfo);
				 break;
			 case Account.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR:
				 newAccountInfo = PostpaidBusinessRegularAccountInfo.newAutotelInstance();
				 newAccount = new TMPostpaidBusinessRegularAccount (provider, (PostpaidBusinessRegularAccountInfo) newAccountInfo);
				 break;
			 case Account.ACCOUNT_SUBTYPE_PAGER_REGULAR:
				 newAccountInfo = PostpaidBusinessRegularAccountInfo.newPagerInstance();
				 newAccount = new TMPagerPostpaidBusinessRegularAccount (provider, (PostpaidBusinessRegularAccountInfo) newAccountInfo);
				 break;
			 default: 
				 newAccountInfo = PostpaidBusinessRegularAccountInfo.newPCSInstance();
				 newAccount = new TMPostpaidBusinessRegularAccount (provider, (PostpaidBusinessRegularAccountInfo) newAccountInfo);
				 break;
			 }
		 }
		 else if ( accountType == Account.ACCOUNT_TYPE_CORPORATE ) {
			 switch (accountSubType) {
			 case Account.ACCOUNT_SUBTYPE_IDEN_REGULAR:
			 case Account.ACCOUNT_SUBTYPE_IDEN_PRIVATE_NETWORK_PLUS:
				 newAccountInfo = PostpaidCorporateRegularAccountInfo.newInstance(accountSubType);
				 newAccount = new TMIDENPostpaidCorporateRegularAccount( provider, (PostpaidCorporateRegularAccountInfo) newAccountInfo);
				 break;

			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_FEDERAL_GOVERNMENT:
			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_ENTERPRISE:
			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE:
			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE:
			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_ABORIGINAL:
			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_FUSION_EAST_CONV:
			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_KEY:
			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_OFFICIAL:
			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_CNBS:
			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_GOVERNMENT:
			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_NATIONAL_STRATEGIC:
			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_REGIONAL_STRATEGIC:
			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_TMI_AFFILIATE:
			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_TMI_DIVISION:
			 case Account.ACCOUNT_SUBTYPE_CORP_RESELLER:
				 newAccountInfo = PostpaidCorporateRegularAccountInfo.newInstance0(accountSubType);
				 newAccount = new TMPCSPostpaidCorporateRegularAccount( provider, (PostpaidCorporateRegularAccountInfo) newAccountInfo);
				 break;

			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_INDIVIDUAL:
			 case Account.ACCOUNT_SUBTYPE_CORP_PCS_EMPLOYEE:
				 newAccountInfo = PostpaidCorporatePersonalAccountInfo.newInstance0(accountSubType);
				 newAccount = new TMPCSPostpaidCorporatePersonalAccount( provider,(PostpaidCorporatePersonalAccountInfo) newAccountInfo);
				 break;

			 case Account.ACCOUNT_SUBTYPE_CORP_IDEN_REGIONAL_STRATEGIC:
			 case Account.ACCOUNT_SUBTYPE_CORP_IDEN_NATIONAL_STRATEGIC:
			 case Account.ACCOUNT_SUBTYPE_IDEN_ENTERPRISE:
			 case Account.ACCOUNT_SUBTYPE_IDEN_GOVERNMENT:
			 case Account.ACCOUNT_SUBTYPE_IDEN_FEDERAL_GOVENMENT:
			 case Account.ACCOUNT_SUBTYPE_IDEN_INDIVIDUAL:
			 case Account.ACCOUNT_SUBTYPE_CORP_IDEN_DURHAM_POLICE:
			 case Account.ACCOUNT_SUBTYPE_CORP_IDEN_TMI_AFFILIATE:
			 case Account.ACCOUNT_SUBTYPE_CORP_IDEN_TMI_DIVISION:
				 newAccountInfo = PostpaidBusinessRegularAccountInfo.newIDENInstance();
				 newAccount = new TMIDENPostpaidBusinessRegularAccount( provider,(PostpaidBusinessRegularAccountInfo) newAccountInfo);
				 break;

			 case Account.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR:
			 case Account.ACCOUNT_SUBTYPE_AUTOTEL_EARS:
				 newAccountInfo = PostpaidCorporateRegularAccountInfo.newInstance(accountSubType);
				 newAccount = new TMPostpaidCorporateRegularAccount ( provider, (PostpaidCorporateRegularAccountInfo) newAccountInfo );
				 break;

			 default:
				 newAccountInfo = PostpaidBusinessRegularAccountInfo.newPCSInstance();
				 newAccount = new TMPCSPostpaidBusinessRegularAccount( provider,(PostpaidBusinessRegularAccountInfo) newAccountInfo);
				 break;
			 }
		 }

		 // copy data from the original info object
		 newAccountInfo.copyFrom(((TMAccount) account).getDelegate0());

		 // set type and subtype that were overridden by copying over the data
		 newAccountInfo.setAccountType(accountType);
		 newAccountInfo.setAccountSubType(accountSubType);

		 //indicate there is account type change, and preserve the old type
		 newAccountInfo.setAccountTypeChanged(true);
		 newAccountInfo.setOldAccountType(((TMAccount) account).getDelegate0().getAccountType());
		 newAccountInfo.setOldAccountSubType(((TMAccount) account).getDelegate0().getAccountSubType());
		 return newAccount;
	 }

	 public AccountSummary[] findAccountsByTalkGroup(int urbanId, int fleetId, int talkGroupId) throws TelusAPIException {
		 // return object
		 AccountSummary[] accounts = null;
		 try {
			 Collection c = null;
			 c = provider.getAccountLifecycleManager().retrieveAccountsByTalkGroup(urbanId, fleetId, talkGroupId, 
					 SessionUtil.getSessionId(provider.getAccountLifecycleManager()));	
			 accounts = decorate((AccountSummary[]) c.toArray(new AccountSummary[c.size()]));
		 }
		 catch (Throwable t) {
				provider.getExceptionHandler().handleException(t);
		}

		return accounts;
	 }

	 public IDENSubscriber[] findSubscribersByMemberIdentity(int urbanId, int fleetId, String memberId) throws TelusAPIException {
		 // return object
		 IDENSubscriber[] subscribers = null;

		 try {
			 Collection c = provider.getSubscriberManagerBean().retrieveSubscribersByMemberIdentity(urbanId, fleetId, Integer.parseInt(memberId));
			 subscribers = decorate((IDENSubscriber[]) c.toArray(new IDENSubscriber[c.size()]), true, false);
		 }
		 catch (Throwable t) {
				provider.getExceptionHandler().handleException(t);
		}

		 return subscribers;
	 }

	 // GHB - 2005/08/25 - Added for Worldphone
	 private boolean isWorldPhone(Equipment equipment) {
		 if ( equipment instanceof CellularDigitalEquipment &&
				 ((CellularDigitalEquipment) equipment).isWorldPhone() ) {
			 return true;
		 }
		 return false;
	 }

	 // GHB - 2005/08/25 - Added for PDA
	 private boolean isPDA(Equipment equipment) {
		 if ( equipment instanceof CellularDigitalEquipment &&
				 ((CellularDigitalEquipment) equipment).isPDA() ) {
			 return true;
		 }
		 return false;
	 }


	 public ActivationTopUp newActivationTopUp() {
		 return new ActivationTopUpInfo();
	 }




	 public PCSPrepaidConsumerAccount newPCSPrepaidConsumerAccount() {
		 PrepaidConsumerAccountInfo info = PrepaidConsumerAccountInfo.newPCSInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID);
		 info.setValidForMigration(true);
		 return new TMPCSPrepaidConsumerAccount(provider, info, null);
	 }

	 public QueueThresholdEvent getQueueThresholdEventbyCallCentreConnectionId(long callCentreConnectionId)
	 throws TelusAPIException {
		 try {
			  return decorate(provider.getQueueEventManagerNew().getEvent(callCentreConnectionId));
		 } catch (Throwable t) {
				provider.getExceptionHandler().handleException(t);
		}
		 return null;
	 }

	 /**
	  * defect PROD00087624, shuold return null instead of empty object
	  * @param info
	  * @return
	  */
	 private TMQueueThresholdEvent decorate(QueueThresholdEventInfo info) {
		 if (info != null)
			 return new TMQueueThresholdEvent(provider, info);
		 else
			 return null;
	 }

	 public PaymentTransfer newPaymentTransfer(int targetBanId, String reason, double amount) throws TelusAPIException {
		 try {
			 return new PaymentTransferInfo(targetBanId,reason,amount);
		 } catch (Throwable t) {
			 throw new TelusAPIException(t);
		 }
	 }

	 public PCSAccount newPCSAccount(IDENAccount oldAccount) throws AccountMatchException, TelusAPIException{
		 PCSAccount newAccount = null;
		 AccountInfo accountInfo = null;

		 if (oldAccount instanceof IDENPostpaidBusinessDealerAccount){
			 accountInfo = PostpaidBusinessDealerAccountInfo.newPCSInstance0();
			 accountInfo.copyFrom(((TMAccount)oldAccount).getDelegate0());
			 accountInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_BUSINESS);
			 accountInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_PCS_DEALER);
			 accountInfo.setBanId(0);

			 newAccount =  new TMPCSPostpaidBusinessDealerAccount(provider,(PostpaidBusinessDealerAccountInfo)accountInfo);

		 }
		 else if (oldAccount instanceof IDENPostpaidBusinessPersonalAccount){
			 accountInfo = PostpaidBusinessPersonalAccountInfo.newPCSInstance0();
			 accountInfo.copyFrom(((TMAccount)oldAccount).getDelegate0());
			 accountInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_BUSINESS);
			 accountInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_PCS_PERSONAL);
			 accountInfo.setBanId(0);

			 newAccount =  new TMPCSPostpaidBusinessPersonalAccount(provider,(PostpaidBusinessPersonalAccountInfo) accountInfo);
		 }
		 else if (oldAccount instanceof IDENPostpaidCorporateRegularAccount){
			 accountInfo = PostpaidCorporateRegularAccountInfo.newInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR);
			 accountInfo.copyFrom(((TMAccount)oldAccount).getDelegate0());
			 accountInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_CORPORATE);
			 accountInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE);
			 accountInfo.setBanId(0);

			 newAccount =  new TMPCSPostpaidCorporateRegularAccount(provider,(PostpaidCorporateRegularAccountInfo) accountInfo);
		 }
		 else if (oldAccount instanceof IDENPostpaidBusinessRegularAccount){
			 accountInfo = PostpaidBusinessRegularAccountInfo.newPCSInstance();
			 accountInfo.copyFrom(((TMAccount)oldAccount).getDelegate0());
			 accountInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_BUSINESS);
			 accountInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR);
			 accountInfo.setBanId(0);

			 newAccount =  new TMPCSPostpaidBusinessRegularAccount(provider,(PostpaidBusinessRegularAccountInfo)accountInfo);
		 }
		 else if (oldAccount instanceof IDENPostpaidEmployeeAccount){
			 char oldAccountSubtype = ((TMAccount)oldAccount).getDelegate0().getAccountSubType();
			 if (oldAccountSubtype == AccountSummary.ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE){
				 accountInfo = PostpaidEmployeeAccountInfo.newPCSInstance0();
				 accountInfo.copyFrom(((TMAccount)oldAccount).getDelegate0());
				 accountInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_CONSUMER);
				 accountInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE);
				 accountInfo.setBanId(0);
			 }
			 else if (oldAccountSubtype == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL){
				 accountInfo = PostpaidEmployeeAccountInfo.newPCSInstance1();
				 accountInfo.copyFrom(((TMAccount)oldAccount).getDelegate0());
				 accountInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_CONSUMER);
				 accountInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE_NEW);
				 accountInfo.setBanId(0);
			 }

			 newAccount =  new TMPCSPostpaidEmployeeAccount(provider,(PostpaidEmployeeAccountInfo) accountInfo);

		 }
		 else if (oldAccount instanceof IDENPostpaidConsumerAccount){
			 accountInfo = PostpaidConsumerAccountInfo.newPCSInstance();
			 accountInfo.copyFrom(((TMAccount)oldAccount).getDelegate0());
			 accountInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_CONSUMER);
			 accountInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR);
			 accountInfo.setBanId(0);

			 newAccount =  new TMPCSPostpaidConsumerAccount(provider,(PostpaidConsumerAccountInfo) accountInfo);
		 }
		 else
			 throw new AccountMatchException("PCS Account cannot be created, there is no corresponding pair to the given IDEN Account: " + oldAccount.getClass().getName());

		 //populate the original account's BAN id into new account instance
		 accountInfo.setOriginalBanId(((TMAccount)oldAccount).getBanId());

		 return newAccount;
	 }

	 @Deprecated
	 public IDENAccount newIDENAccount(PCSAccount oldAccount) throws AccountMatchException, TelusAPIException{
		 IDENAccount newAccount = null;
		 AccountInfo accountInfo = null;

		 if (oldAccount instanceof PCSPostpaidBusinessDealerAccount){
			 accountInfo = PostpaidBusinessDealerAccountInfo.newIDENInstance0();
			 accountInfo.copyFrom(((TMAccount)oldAccount).getDelegate0());
			 accountInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_BUSINESS);
			 accountInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_IDEN_DEALER);
			 accountInfo.setBanId(0);

			 newAccount =  new TMIDENPostpaidBusinessDealerAccount(provider,(PostpaidBusinessDealerAccountInfo) accountInfo);
		 }
		 else if (oldAccount instanceof PCSPostpaidBusinessPersonalAccount){
			 accountInfo = PostpaidBusinessPersonalAccountInfo.newIDENInstance0();
			 accountInfo.copyFrom(((TMAccount)oldAccount).getDelegate0());
			 accountInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_BUSINESS);
			 accountInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL);
			 accountInfo.setBanId(0);

			 newAccount =  new TMIDENPostpaidBusinessPersonalAccount(provider,(PostpaidBusinessPersonalAccountInfo) accountInfo);
		 }
		 else if (oldAccount instanceof PCSPostpaidBusinessRegularAccount){
			 accountInfo = PostpaidBusinessRegularAccountInfo.newIDENInstance();
			 accountInfo.copyFrom(((TMAccount)oldAccount).getDelegate0());
			 accountInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_BUSINESS);
			 accountInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR);
			 accountInfo.setBanId(0);

			 newAccount =  new TMIDENPostpaidBusinessRegularAccount(provider,(PostpaidBusinessRegularAccountInfo) accountInfo);
		 }
		 else if (oldAccount instanceof PCSPostpaidEmployeeAccount){
			 char oldAccountSubtype = ((TMAccount)oldAccount).getDelegate0().getAccountSubType();
			 if (oldAccountSubtype == AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE){
				 accountInfo = PostpaidEmployeeAccountInfo.newIDENInstance0();
				 accountInfo.copyFrom(((TMAccount)oldAccount).getDelegate0());
				 accountInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_CONSUMER);
				 accountInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE);
				 accountInfo.setBanId(0);
			 }
			 else if (oldAccountSubtype == AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE_NEW){
				 accountInfo = PostpaidEmployeeAccountInfo.newIDENInstance1();
				 accountInfo.copyFrom(((TMAccount)oldAccount).getDelegate0());
				 accountInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_CONSUMER);
				 accountInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL);
				 accountInfo.setBanId(0);
			 }


			 newAccount =  new TMIDENPostpaidEmployeeAccount(provider,(PostpaidEmployeeAccountInfo)accountInfo);
		 }
		 else if (oldAccount instanceof PCSPostpaidConsumerAccount){
			 accountInfo = PostpaidConsumerAccountInfo.newIDENInstance();
			 accountInfo.copyFrom(((TMAccount)oldAccount).getDelegate0());
			 accountInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_CONSUMER);
			 accountInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR);
			 accountInfo.setBanId(0);

			 newAccount =  new TMIDENPostpaidConsumerAccount(provider,(PostpaidConsumerAccountInfo) accountInfo);
		 } 
		 else if (oldAccount instanceof PCSPostpaidCorporateRegularAccount){
			 accountInfo = PostpaidCorporateRegularAccountInfo.newInstance(AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR);
			 accountInfo.copyFrom(((TMAccount)oldAccount).getDelegate0());
			 accountInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_CORPORATE);
			 accountInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR);
			 accountInfo.setBanId(0);

			 newAccount =  new TMIDENPostpaidCorporateRegularAccount(provider,(PostpaidCorporateRegularAccountInfo) accountInfo);
		 }
		 else
			 throw new AccountMatchException("PCS Account cannot be created, there is no corresponding pair to the given PCS Account: " + oldAccount.getClass().getName());

		 //populate the original account's BAN id into new account instance
		 accountInfo.setOriginalBanId(((TMAccount)oldAccount).getBanId());

		 return newAccount;
	 }

	 public String[] findPartiallyReservedSubscribersByBan(int ban, int maximum) throws TelusAPIException {
		try {
			Collection c = provider.getSubscriberLifecycleHelper().retrievePartiallyReservedSubscriberListByBan(ban, maximum);
			return (String[])c.toArray(new String[c.size()]);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	 }

	 public PCSPostpaidCorporatePersonalAccount newPCSPostpaidCorporatePersonalAccount(char accountSubType) throws TelusAPIException {
		 return newPCSPostpaidCorporatePersonalAccount (accountSubType, 0 );
	 }

	 public ServicesValidation newServicesValidation() {
		 return new ServicesValidationInfo();
	 }




	 public AccountSummary[] findAccountsByImsi(String imsi)
	 throws TelusAPIException {
		 try {
			 Collection c = null;
			 c = new ArrayList();
			 AccountInfo account = provider.getAccountInformationHelper().retrieveAccountByImsi(imsi);
			 if (account != null ) c.add(account);
			 return decorate((AccountSummary[])c.toArray(new AccountSummary[c.size()]));

		 } catch (Throwable t) {
			 provider.getExceptionHandler().handleException(t);
		 }
		 return null;
	 }


	 public Subscriber[] findSubscribersByImsi(String imsi,
			 boolean includingCancelled) throws TelusAPIException {
		 try {
			 Collection c = provider.getSubscriberLifecycleHelper().retrieveSubscriberListByImsi(imsi, includingCancelled);
			 return decorate((Subscriber[])c.toArray(new Subscriber[c.size()]), true, false);
		 } catch (Throwable t) {
				provider.getExceptionHandler().handleException(t);
			}
		  return null;
	 }

	 @Deprecated
	 public IDENPostpaidCorporateRegularAccount newIDENPostpaidCorporateRegularAccount(
			 char accountSubtype, int billCycle) throws TelusAPIException,
			 InvalidBillCycleException {

		 PostpaidCorporateRegularAccountInfo info = PostpaidCorporateRegularAccountInfo.newInstance0(accountSubtype);
		 info.setEvaluationProductType(PRODUCT_TYPE_IDEN);
		 validateBillCycle( billCycle) ;
		 info.setBillCycle(billCycle);
		 return new TMIDENPostpaidCorporateRegularAccount(provider, info);
	 }


	 public PCSPostpaidCorporateRegularAccount newPCSPostpaidCorporateRegularAccount(
			 char accountSubtype, int billCycle) throws TelusAPIException,
			 InvalidBillCycleException {

		 PostpaidCorporateRegularAccountInfo info = PostpaidCorporateRegularAccountInfo.newInstance0(accountSubtype);
		 info.setEvaluationProductType(PRODUCT_TYPE_PCS);
		 validateBillCycle( billCycle) ;
		 info.setBillCycle(billCycle);
		 return new TMPCSPostpaidCorporateRegularAccount(provider, info);
	 }
	 public PCSPostpaidCorporatePersonalAccount newPCSPostpaidCorporatePersonalAccount(
			 char accountSubtype, int billCycle) throws TelusAPIException,
			 InvalidBillCycleException {

		 PostpaidCorporatePersonalAccountInfo info = PostpaidCorporatePersonalAccountInfo.newInstance0(accountSubtype);
		 info.setEvaluationProductType(PRODUCT_TYPE_PCS);
		 validateBillCycle( billCycle) ;
		 info.setBillCycle(billCycle);
		 return new TMPCSPostpaidCorporatePersonalAccount(provider, info);
	 }

	 private void validateBillCycle( int billCycle ) throws TelusAPIException {
		 if ( billCycle < 0)
			 throw new InvalidBillCycleException( "Bill cycle is not set ");

		 if ( billCycle==PREPAID_BILL_CYCLE ) 
			 throw new InvalidBillCycleException( "Bill cycle is reserved for Prepaid account: " + billCycle);

		 //defect PROD00140681 fix: validate billCycle only when it greater than zero, otherwise treat it system auto assign. 
		 if ( (billCycle>0) && provider.getReferenceDataManager().getBillCycle( String.valueOf(billCycle)) ==null ) {
			 throw new InvalidBillCycleException( "Bill cycle is not avaiable: " + billCycle);
		 }
	 }

	 public PrepaidCallHistory[] convertRateIdToRate(PrepaidCallHistory[] callHistory) throws TelusAPIException {

		 if (callHistory == null || callHistory.length == 0) {
			 return callHistory;
		 }

		 try {
			 for (int i = 0; i < callHistory.length;i ++) {
				 PrepaidRateProfile[] prepaidRateProfiles = null;
				 if (callHistory[i].getOrigin_cd().trim().equalsIgnoreCase(PrepaidCallHistory.CALL_DIRECTION_INBOUND)) {
					 prepaidRateProfiles = provider.getReferenceDataManager().getPrepaidRates(Integer.parseInt(callHistory[i].getRateId()),callHistory[i].getCalledMarketCode());
				 }
				 else if (callHistory[i].getOrigin_cd().trim().equalsIgnoreCase(PrepaidCallHistory.CALL_DIRECTION_OUTBOUND)) {
					 prepaidRateProfiles = provider.getReferenceDataManager().getPrepaidRates(Integer.parseInt(callHistory[i].getRateId()),callHistory[i].getCallingMarketCode());
				 }
				 ((PrepaidCallHistoryInfo) callHistory[i]).setRates(prepaidRateProfiles);

				 for (int k=0; k<prepaidRateProfiles.length; k++){
					 if(prepaidRateProfiles[k].getRateTypeId().equals(PrepaidRateProfile.PREPAID_RATE_TYPE_LOCAL)) {
						 ((PrepaidCallHistoryInfo) callHistory[i]).setRate(prepaidRateProfiles[k].getRate());
						 break;
					 }
				 }
			 }
		 }catch (Throwable t) {
			 throw new TelusAPIException (t);
		 }
		 return callHistory;
	 }


	 public Subscriber newSubscriber(Subscriber subscriber) throws TelusAPIException {
		 Subscriber newSubscriber = new TMSubscriber(provider, (TMSubscriber)subscriber);
		 Equipment equipment = newSubscriber.getEquipment();

		 // Need to assign associated handset from subscriber parameter (which represents a deserialized subscriber object) when equipment is a SIM card (defect # 175090) 
		 if (equipment != null && equipment instanceof USIMCardEquipment)
			 if (subscriber != null && subscriber.getEquipment()!= null) {
				 String associatedHandsetIMEI = ((USIMCardEquipment)subscriber.getEquipment()).getLastAssociatedHandsetIMEI();
				 ((TMEquipment)equipment).getDelegate().setAssociatedHandsetIMEI(associatedHandsetIMEI);
			 }

		 return newSubscriber;
	 }


	 //PCI related changes
	 public AuditHeader newAuditHeader() {
		 return new AuditHeaderInfo();
	 }

	public PCSPostpaidBusinessPersonalAccount newPCSPostpaidBusinessPersonalAccount(
			char accountSubType) throws TelusAPIException {
		PostpaidBusinessPersonalAccountInfo info = PostpaidBusinessPersonalAccountInfo.getNewInstance0(accountSubType);
		info.setEvaluationProductType(PRODUCT_TYPE_PCS);
		info.setFidoConversion(false);
		return new TMPCSPostpaidBusinessPersonalAccount(provider, info);
	}


	public PCSPostpaidBusinessRegularAccount newPCSPostpaidBusinessRegularAccount(
			char accountSubType) throws TelusAPIException {
		PostpaidBusinessRegularAccountInfo info = PostpaidBusinessRegularAccountInfo.getNewInstance(accountSubType);
		info.setEvaluationProductType(PRODUCT_TYPE_PCS);
		info.setFidoConversion(false);
		return new TMPCSPostpaidBusinessRegularAccount(provider, info);
		
	}


	public PCSAccount newPCSAccount(IDENAccount oldAccount, char accountType,
			char accountSubType) throws AccountMatchException,
			TelusAPIException {

		PCSAccount account= newPCSAccount(oldAccount);
		TMAccount tmaccount = (TMAccount)account;
		if((accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL ||
				 accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR)) ||
				(accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE)){	
			tmaccount.getDelegate0().setAccountType(accountType);
			tmaccount.getDelegate0().setAccountSubType(accountSubType);
			return (PCSAccount)tmaccount;
		}else{
			if(tmaccount.getDelegate0().getAccountType() != accountType || tmaccount.getDelegate0().getAccountSubType() != accountSubType)
				throw new AccountMatchException("IDENAccount (" + oldAccount.getClass().getName() + ") " +
						"fields cannot be copied to a PCSAccount object due tounsupported IDENAccount to PCSAccount transition for the input account type: "
						+ accountType + " and subtype: "+ accountSubType);
			return (PCSAccount)tmaccount;
		}
	}


	public EnterpriseAddress newEnterpriseAddress() throws TelusAPIException {
		return new TMEnterpriseAddress(provider, new EnterpriseAddressInfo());
	}


	public Address newAddress() throws TelusAPIException {
		return new TMAddress(provider, new AddressInfo());
	}

	private static int getMaxBanLimit() {
		return AppConfiguration.getMaxBanLimit();
	}
	
	private static int getMaxSubLimit() {
		return AppConfiguration.getMaxSubLimit();
	}

	public SeatData newSeatData() {
		 return new SeatDataInfo();
	 }
}
