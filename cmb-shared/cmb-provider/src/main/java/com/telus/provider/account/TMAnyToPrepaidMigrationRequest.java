package com.telus.provider.account;

import java.util.Iterator;
import java.util.Map;

import com.telus.api.InvalidMigrationRequestException;
import com.telus.api.InvalidServiceChangeException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.ActivationTopUpPaymentArrangementException;
import com.telus.api.account.AnyToPrepaidMigrationRequest;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.Contract;
import com.telus.api.account.InvalidActivationAmountException;
import com.telus.api.account.InvalidAirtimeCardPINException;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.account.PrepaidConsumerAccount;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.config0.Configuration;
import com.telus.api.config0.ConfigurationManager;
import com.telus.api.equipment.AirtimeCard;
import com.telus.api.equipment.CellularEquipment;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.USIMCardEquipment;
import com.telus.api.reference.BusinessRole;
import com.telus.api.reference.MigrationType;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.PricePlan;
import com.telus.api.util.SessionUtil;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.eas.account.info.ActivationTopUpInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.config0.TMConfigurationManager;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.Logger;
import com.telus.provider.util.ProviderCreditCardExceptionTranslator;

public class TMAnyToPrepaidMigrationRequest extends TMMigrationRequest implements AnyToPrepaidMigrationRequest{

	protected transient Contract newPrepaidContract;
	
	
	
    public void postMigrationTask() throws InvalidMigrationRequestException, TelusAPIException {
        if(getActivationTopUp() != null){
            creditSubscriberMigration();
        }

        TMActivationTopUpPaymentArrangement topupPaymentArrangement = 
        	(TMActivationTopUpPaymentArrangement) ((PrepaidConsumerAccount) getNewAccount()).getActivationTopUpPaymentArrangement();
        try {
			if (topupPaymentArrangement != null && topupPaymentArrangement.validate()) {
				provider.getAccountLifecycleManager().saveActivationTopUpArrangement(String.valueOf(getNewAccount().getBanId()),
							 getNewSubscriber0().getPhoneNumber(), getNewEquipment().getSerialNumber(), topupPaymentArrangement.getDelegate(), provider.getUser());
			}
		} catch (Throwable e) {
			Logger.debug("TMAnyToPrepaidMigrationRequest.posttMigrationTask ActivationTopUpPaymentArrangement ERROR: " + e.getMessage());
		}
		
		//This is to save new Prepaid feature that being added in the newPrepaidContract 
		((TMPCSSubscriber) newSubscriber).saveActivationFeaturesPurchaseAgreement();
    }

    public void preMigrationTask() throws InvalidMigrationRequestException, TelusAPIException {
        
    	adjustMainMigrationContract();
        
        if(getActivationTopUp() == null)
            performPostAccountCreationPrepaidTasks();
          
    }

    public void performPostAccountCreationPrepaidTasks() throws InvalidCreditCardException, InvalidAirtimeCardPINException, InvalidActivationAmountException, TelusAPIException {

        //----------------------------------------
        // Ensure this is only called on create.
        //----------------------------------------
        TMPCSPrepaidConsumerAccount prepaidConsumerAccount = (TMPCSPrepaidConsumerAccount)getNewAccount();
        
        prepaidConsumerAccount.getDelegate().setActivationType(getActivationType());
        prepaidConsumerAccount.getDelegate().setSerialNumber(getNewEquipment().getSerialNumber());
        
        if (getNewEquipment().isUSIMCard()){
        	if(null != getNewAssociatedHandset()){
        		prepaidConsumerAccount.getDelegate().setAssociatedHandsetIMEI(((USIMCardEquipment)getNewEquipment()).getLastAssociatedHandsetIMEI());
        	}
        }
        
        if(getActivationType() == PrepaidConsumerAccount.ACTIVATION_TYPE_CREDIT_CARD ){
        	if(getActivationCreditAmount() <= 0.0) {
        		throw new InvalidActivationAmountException(getActivationCreditAmount() +" Credit amount is not valid");
        	}
        	prepaidConsumerAccount.getDelegate().setActivationCreditAmount(getActivationCreditAmount());
        	prepaidConsumerAccount.getDelegate().setActivationCreditCard(((TMCreditCard)getActivationCreditCard()).getDelegate());
        	
        }
        else if(getActivationType() == PrepaidConsumerAccount.ACTIVATION_TYPE_AIRTIME_CARD){
        	
        	prepaidConsumerAccount.getDelegate().setActivationCode(getActivationAirtimeCardNumber());
        	prepaidConsumerAccount.getDelegate().setActivationCreditAmount(getActivationCreditAmount());
        }
        else if (getActivationType() == PrepaidConsumerAccount.ACTIVATION_TYPE_NORMAL) {
        	prepaidConsumerAccount.getDelegate().setActivationCode(getActivationAirtimeCardNumber());
        }
        else if(getActivationType() == PrepaidConsumerAccount.ACTIVATION_TYPE_VIRTUAL_WITH_CHARGE) {
        	prepaidConsumerAccount.getDelegate().setActivationCreditCard(((TMCreditCard)getActivationCreditCard()).getDelegate());
        }

	    try {
	    	//PCI changes: grab the auditHeader from CreditCardTransactionInfo, which is supposed to be populated 
	    	// when TMMigrationRequest.setActivationCreditCard( creditCard, auditHeader ) get called
	    	AuditHeader auditHeader = null;
	    	if ( getActivationCreditCard()!=null ) {
	    		TMCreditCard cc=(TMCreditCard)getActivationCreditCard();
	    		if ( cc.hasToken() ) auditHeader = cc.getCreditCardTransactionInfo().getAuditHeader();
	    	}
	    	String creditCardReferenceNumber = provider.getAccountLifecycleManager().performPostAccountCreationPrepaidTasks(prepaidConsumerAccount.getBanId(),
	    				 prepaidConsumerAccount.getDelegate(), auditHeader , SessionUtil.getSessionId( provider.getAccountLifecycleManager()));
	        prepaidConsumerAccount.getActivationCreditCard().setAuthorizationCode(creditCardReferenceNumber);
	    } catch (Throwable t) {
	    	TelusExceptionTranslator telusExceptionTranslator= new ProviderCreditCardExceptionTranslator(creditCard);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);

	    }

      }

    public void creditSubscriberMigration()throws TelusAPIException{
        try{
            TMPCSPrepaidConsumerAccount prepaidConsumerAccount = (TMPCSPrepaidConsumerAccount)getNewAccount();
            if (getNewEquipment().isUSIMCard()){
            	if(null != getNewAssociatedHandset()){
            		prepaidConsumerAccount.getDelegate().setAssociatedHandsetIMEI(((USIMCardEquipment)getNewEquipment()).getLastAssociatedHandsetIMEI());
            	}
            }
	    	provider.getAccountLifecycleManager().creditSubscriberMigration(provider.getApplication(),provider.getUser(),
	    				  (ActivationTopUpInfo)getActivationTopUp(), getCurrentSubscriber().getPhoneNumber(), 
	    				  getNewEquipment().getSerialNumber(), getCurrentSubscriber().getNumberGroup().getProvinceCode(),
	    				  prepaidConsumerAccount.getDelegate());
        } catch (Throwable e) {
        	provider.getExceptionHandler().handleException(e);
        }
    }

    public TMAnyToPrepaidMigrationRequest(TMProvider provider,
            MigrationType migrationType, Subscriber currentSubscriber,
            TMAccount newAccount, Equipment equipment, String pricePlanCode)
            throws UnknownBANException, TelusAPIException {
        super(provider, migrationType, currentSubscriber, newAccount,
                equipment, pricePlanCode);
        PricePlan pricePlan = provider.getReferenceDataManager().getPricePlan(
                pricePlanCode, provider.getEquipmentManager0().translateEquipmentType(equipment),
                currentSubscriber.getNumberGroup().getProvinceCode(),
                newAccount.getAccountType(), newAccount.getAccountSubType(), newAccount.getBrandId());
        newPrepaidContract = ((TMPCSSubscriber)newSubscriber).newContract(pricePlan,PricePlan.CONTRACT_TERM_ALL, false);
        ((TMContract)newPrepaidContract).setMigrationSubscriber( (TMSubscriber)currentSubscriber, (TMSubscriber)newSubscriber);
        
    }

    public boolean testMigrationRequest() throws InvalidMigrationRequestException, TelusAPIException {
    	
    	if(-1 == getActivationType()) 
    		throw new InvalidMigrationRequestException("Invalid activation type", InvalidMigrationRequestException.REASON_INVALID_ACTIVATION_TYPE);
    	
        if(getActivationType() == PrepaidConsumerAccount.ACTIVATION_TYPE_CREDIT_CARD ){
        	if(getActivationCreditAmount() <= 0.0) {
        		throw new InvalidMigrationRequestException(getActivationCreditAmount()+": is invalid credit card Amount", InvalidMigrationRequestException.REASON_INVAILD_ACTIVATION_CREDIT_CARD);
        	}
        	try {
				//PCI: explicit cast to TMCreditCard, to use the validate method
        		((TMCreditCard)getActivationCreditCard()).validate("Purchace-now", BusinessRole.BUSINESS_ROLE_ALL);
			} catch (InvalidCreditCardException e) {
				throw new InvalidMigrationRequestException("Invalid credit card", e, InvalidMigrationRequestException.REASON_INVAILD_ACTIVATION_CREDIT_CARD);
			}
        	
        }
        else if(getActivationType() == PrepaidConsumerAccount.ACTIVATION_TYPE_AIRTIME_CARD){
        	AirtimeCard card = null;

        	try {
        		card = getAirTimeCard();  // validate the card
        	}
        	catch (TelusAPIException ex){
        		throw new InvalidMigrationRequestException("Invalid Activation Air Time Card", InvalidMigrationRequestException.REASON_INVAILD_ACTIVATION_AIRTIME_CARD);
        	}
        	if(!card.isLive())
        		throw new InvalidMigrationRequestException("Status of Air Time Card is not live ", InvalidMigrationRequestException.REASON_INVAILD_ACTIVATION_AIRTIME_CARD);
        	
        	if (!card.isValidAmountForApp())
        		throw new InvalidMigrationRequestException("The amount " +card.getAmount()+" is inavlid amount", InvalidMigrationRequestException.REASON_INVAILD_ACTIVATION_AIRTIME_CARD);
  
        	setActivationCreditAmount(card.getAmount());
        }
        else if (getActivationType() == PrepaidConsumerAccount.ACTIVATION_TYPE_NORMAL) {
        	if (null == getActivationAirtimeCardNumber())
        		throw new InvalidMigrationRequestException("Activation code is null ", InvalidMigrationRequestException.REASON_INVALID_MANDATORY_FIELDS);
        }
        else if(getActivationType() == PrepaidConsumerAccount.ACTIVATION_TYPE_VIRTUAL_WITH_CHARGE) {
        	try {
				//PCI: explicit cast to TMCreditCard, to use the validate method
        		((TMCreditCard)getActivationCreditCard()).validate("Purchace-now", BusinessRole.BUSINESS_ROLE_ALL);
			} catch (InvalidCreditCardException e) {
				throw new InvalidMigrationRequestException("Invalid credit card", e, InvalidMigrationRequestException.REASON_INVAILD_ACTIVATION_CREDIT_CARD);
			}
        }
        
		super.validateMandatoryFields();
		if (getNewAccount().getStatus() != AccountSummary.STATUS_TENTATIVE)
			throw new InvalidMigrationRequestException("Invalid Account Status", InvalidMigrationRequestException.REASON_INVAILD_ACCOUNT_STATUS);
		if (newSubscriber.isValidMigrationForPhoneNumber(getMigrationType(), null, getNewEquipment().isHSPA() ? NetworkType.NETWORK_TYPE_HSPA : NetworkType.NETWORK_TYPE_CDMA) == false)
			throw new InvalidMigrationRequestException("Invalid phone number", InvalidMigrationRequestException.REASON_INVALID_PHONE_NUMBER);
		if (!((CellularEquipment) equipment).isValidForPrepaid())
			throw new InvalidMigrationRequestException("This Equipment type cannot supported for Pre-paid", InvalidMigrationRequestException.REASON_INVALID_EQUIPMENT_TYPE);
		if (getActivationTopUp() != null && !(getActivationTopUp().getExpiryDays() > 0 && getActivationTopUp().getAmount() > 0))
			throw new InvalidMigrationRequestException("Invalid activation topup", InvalidMigrationRequestException.REASON_INVAILD_ACTIVATION_TOPUP);
		

		if (getActivationTopUp() != null) {
			int rateId = loadRate(getActivationTopUp().getRate());
			if (rateId == 0) {
				throw new InvalidMigrationRequestException("Invalid Rate for Top up", InvalidMigrationRequestException.REASON_INVAILD_ACTIVATION_TOPUP);
			}
			((ActivationTopUpInfo) getActivationTopUp()).setRateId(rateId);
		}

		if (((PrepaidConsumerAccount) getNewAccount()).getActivationTopUpPaymentArrangement() != null) {
			try {
				((PrepaidConsumerAccount) getNewAccount()).getActivationTopUpPaymentArrangement().validate();
			} catch (ActivationTopUpPaymentArrangementException e) {
				throw new InvalidMigrationRequestException("Invalid topup payment arrangement", e, InvalidMigrationRequestException.REASON_INVALID_ACTIVATION_TOPUP_PAYMENT_ARRANGEMENT);
			}
		}
		return true;
	}

    public int loadRate(double rate) throws TelusAPIException {
        int rateId = 0;

        try {

            ConfigurationManager configurationManager = new TMConfigurationManager(provider);

            // getting configuration
            Configuration configuration = configurationManager
                    .getConfiguration(new String[] { "telus", "common",
                            "ClientAPI", "prepaidRates" });

            // getting properties
            Map properties = configuration.getProperties();

            // looking for rate ID
            Iterator iterator = properties.keySet().iterator();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();

                if (Double.valueOf(key).doubleValue() == rate)
                    rateId = Integer.parseInt((String) properties.get(key));
            }

        } catch (Throwable t) {
            throw new TelusAPIException(t);
        }
        return rateId;
    }

    public void setActivationOption(ActivationOption option) {
       throw new RuntimeException("This method is not supported in Any To PrePaid Migration");
        
    }
    
	public Contract getNewPrepaidContract() throws TelusAPIException {
		return newPrepaidContract;
	}
	
	public void setActivationCreditAmount(double activationCreditAmount) {
		migrationRequestInfo.setActivationCreditAmount(activationCreditAmount);
	}

	public double getActivationCreditAmount() {
		return migrationRequestInfo.getActivationCreditAmount();
	}
	
	public void setActivationType(int activationType) {
		migrationRequestInfo.setActivationType(activationType);
	}

	public int getActivationType() {
		return migrationRequestInfo.getActivationType();
	}
	public AirtimeCard getAirTimeCard() throws TelusAPIException{
		return (AirtimeCard)provider.getEquipmentManager().getAirCardByCardNumber( getActivationAirtimeCardNumber(), provider.getUser(), getNewEquipment().getSerialNumber());
	}

	void adjustMainMigrationContract() throws TelusAPIException, UnknownObjectException,InvalidServiceChangeException {

		//If newPrepaidContract contains calling circle, then add the KB mapping SOC to
	    //the main newContract inside this MigrationRequest and set the corresponding calling circle list
		//So that the KB CC SOC along with CC list get persisted in KB during migration.
		ServiceAgreementInfo prepaidCCService = ((TMContract) newPrepaidContract).getNotSavedPrepaidCallingCircleService();
		if ( prepaidCCService!=null ) {
			ServiceFeatureInfo prepaidCCFeature = SubscriberContractInfo.getCallingCircleFeature( prepaidCCService, true);
			
			if( prepaidCCFeature.getCallingCirclePhoneNumbersFromParam().length>0 ) { 

				String wpsMappedKBSoc = prepaidCCService.getService().getWPSMappedKBSocCode();
				ServiceAgreementInfo kbCCService = ((TMContract) newContract).getKbMappedPrepaidService(wpsMappedKBSoc);
				if ( kbCCService==null) {
					kbCCService = ((TMContractService) newContract.addService(wpsMappedKBSoc)).getDelegate();
				}
				ServiceFeatureInfo kbCCFeature = SubscriberContractInfo.getCallingCircleFeature( kbCCService, true );
				//sync up KB CC feature parameter from prepaid CC feature.
				kbCCFeature.setParameter(prepaidCCFeature.getParameter());
			}
		}
	}
}
