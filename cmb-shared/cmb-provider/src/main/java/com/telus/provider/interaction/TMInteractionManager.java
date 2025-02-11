/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.interaction;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.EquipmentChangeRequest;
import com.telus.api.interaction.Interaction;
import com.telus.api.interaction.InteractionManager;
import com.telus.eas.config.info.AccountStatusChangeInfo;
import com.telus.eas.config.info.AddressChangeInfo;
import com.telus.eas.config.info.BillPaymentInfo;
import com.telus.eas.config.info.EquipmentChangeInfo;
import com.telus.eas.config.info.InteractionInfo;
import com.telus.eas.config.info.PaymentMethodChangeInfo;
import com.telus.eas.config.info.PhoneNumberChangeInfo;
import com.telus.eas.config.info.PrepaidTopupInfo;
import com.telus.eas.config.info.PricePlanChangeInfo;
import com.telus.eas.config.info.RoleChangeInfo;
import com.telus.eas.config.info.ServiceChangeInfo;
import com.telus.eas.config.info.SubscriberChangeInfo;
import com.telus.eas.config.info.SubscriberChargeInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.account.TMAccount;
import com.telus.provider.account.TMAutoTopUp;
import com.telus.provider.account.TMContract;
import com.telus.provider.account.TMPrepaidConsumerAccount;
import com.telus.provider.account.TMSubscriber;
import com.telus.provider.equipment.TMEquipment;
import com.telus.provider.util.AppConfiguration;


public class TMInteractionManager  extends BaseProvider implements InteractionManager {
	
  private static final long serialVersionUID = 1L;

  public static final String TRANSACTION_TYPE_CHANGE_PRICE_PLAN   = "P";
  public static final String TRANSACTION_TYPE_CHANGE_SERVICE      = "S";
  public static final String TRANSACTION_TYPE_CHANGE_PHONE_NUMBER = "N";
  public static final String TRANSACTION_TYPE_CHANGE_EQUIPMENT    = "E";
  public static final String TRANSACTION_TYPE_CHANGE_SUBSCRIBER   = "R";

  public static final String TRANSACTION_TYPE_CHANGE_ADDRESS          = "A";
  public static final String TRANSACTION_TYPE_CHARGE_SUBSCRIBER       = "C";
  public static final String TRANSACTION_TYPE_CHANGE_PAYMENT_METHOD   = "M";
  public static final String TRANSACTION_TYPE_MAKE_PAYMENT            = "Y";
  public static final String TRANSACTION_TYPE_CHANGE_STATUS           = "B";
  public static final String TRANSACTION_TYPE_CHANGE_ACCOUNT_STATUS   = TRANSACTION_TYPE_CHANGE_STATUS;
  public static final String TRANSACTION_TYPE_PREPAID_TOPUP           = "T";
  public static final String TRANSACTION_TYPE_CHANGE_ROLE             = "O";

  public static final char STATUS_CHANGE_FLAG_ACCOUNT = 'A';
  public static final char STATUS_CHANGE_FLAG_SUBSCRIBER = 'S';

  private String activityCode = null;
  private String reasonCode = null;


  public TMInteractionManager(TMProvider provider) throws Throwable {
	  super(provider);
  }

  //==========================================================================================================

  /**
    * This has the same effect as calling getInteractionsByBan(banId, from, to, null);
    *
    * @param banId -- The account id
    * @param from -- The beggining date range
    * @param to -- The ending date range
    * @exception TelusAPIException -- Contains the error that occured.
    */
  public Interaction[] getInteractionsByBan(int banId, Date from, Date to) throws TelusAPIException {
    return getInteractionsByBan(banId, from, to, null);
  }

  /**
    * Returns a list of interactions for the given account between specific dates.  The returned array
    * can be further filtered by the type.
    *
    * If type is null or an empty string all types are returned.
    *
    * @param banId -- The account id
    * @param from -- The beggining date range
    * @param to -- The ending date range
    * @param type -- The type of interaction to return (or null for all types).
    * @exception TelusAPIException -- Contains the error that occured.
    */
  public Interaction[] getInteractionsByBan(int banId, Date from, Date to, String type) throws TelusAPIException {
	  Interaction[] interactions = null;
    try {
      Interaction[]  valueObjects = provider.getConfigurationManagerNew().getInteractionsByBan(banId, from, to, type);
      interactions= wrapInteractions(valueObjects);
    }
    catch (Throwable t) {
		provider.getExceptionHandler().handleException(t);
    }
    return interactions;
  }

  /**
    * This has the same effect as calling getInteractionsBySubscriber(subscriberId, from, to, null);
    *
    * @param subscriberId -- The subscriber (product) the interactions should be filtered by.
    * @param from -- The beggining date for the date range.
    * @param to -- The ending date for the date range.
    * @exception TelusAPIException -- Contains the error that occured.
    */
  public Interaction[] getInteractionsBySubscriber(String subscriberId, Date from, Date to) throws TelusAPIException {
    return getInteractionsBySubscriber(subscriberId, from, to, null);
  }

  /**
    * Returns a list of interactions filtered by subscriber, a date range, and optionally a type.
    *
    * If null or an empty string is used for the type, then interactions for all types are returned.
    *
    * @param subscriberId -- The subscriber (product) the interactions should be filtered by.
    * @param from -- The beggining date for the date range.
    * @param to -- The ending date for the date range.
    * @param type -- The type of interactions to return. (or null for all types).
    * @exception TelusAPIException -- Contains the error that occured.
    */
  public Interaction[] getInteractionsBySubscriber(String subscriberId, Date from, Date to, String type) throws TelusAPIException {
    Interaction[] interactions = null;
    try {
        Interaction[] valueObjects = provider.getConfigurationManagerNew().getInteractionsBySubscriber(subscriberId, from, to, type);
        interactions = wrapInteractions(valueObjects);
    }
    catch (Throwable t) {
		provider.getExceptionHandler().handleException(t);
    }

    return interactions;
  }


  /**
   * Retrieves the knowbility activity code associated to the interaction
   *
   * <P>This method may return <CODE>null</CODE>
   *
   */
  public String getActivityCode() throws TelusAPIException {
    return activityCode;
  }

  /**
   * Retrieves the knowbility activity reason code associated to the interaction
   *
   * <P>This method may return <CODE>null</CODE>
   *
   */
  public String getReasonCode() throws TelusAPIException{
    return reasonCode;
  }

  /**
   * Stores the knowbility activity code associated to the interaction
   * for later internal use in the provider.
   */
  public void setActivityCode(String activityCode) throws TelusAPIException {
    this.activityCode = activityCode;
  }

  /**
   * Stores the knowbility activity reason code associated to the interaction
   * for later internal use in the provider.
   */
  public void setReasonCode(String reasonCode) throws TelusAPIException{
    this.reasonCode = reasonCode;
  }


  /**
    * Takes the value object array (that comes from a remote call) and wraps each element into
    * a provider implementation of Interaction.
    *
    * @param valueObjects -- The value (info) object list
    * @return Interaction[] -- The provider object list.
    */
  private Interaction[] wrapInteractions(Interaction[] valueObjects) {
    if(valueObjects == null)
      return new Interaction[0];

    Interaction[] interactions = new Interaction[valueObjects.length];
    for(int i = 0;i < valueObjects.length;i++) {
      interactions[i] = new TMInteraction(provider, valueObjects[i]);
    }

    return interactions;
  }

  //==========================================================================================================

  private InteractionInfo newReport(String transactionType,
                               String dealerCode,
                               String salesRepCode,
                               int banId,
                               String subscriberId) throws TelusAPIException {
    try {
      long reasonId = mapActivityReasonCodesToReasonId(this.getActivityCode(), this.getReasonCode() );
      return  provider.getConfigurationManagerNew().newReport(transactionType, provider.getApplication(), Integer.parseInt(provider.getUser()), dealerCode, salesRepCode, banId, subscriberId, reasonId);
    } catch(Throwable e) {
      // Silent failure
      log(e);
    }
    return null;
  }

  private long mapActivityReasonCodesToReasonId (String activityCode, String reasonCode ) throws TelusAPIException {
    long reasonId = 0;
    try {
      if ( activityCode != null && reasonCode != null ) {
        String code = activityCode + "_" + reasonCode;
        reasonId = provider.getReferenceDataManager().getClientStateReason(code).
            getReasonId();
      }
    } catch(TelusAPIException e) {
      log(e.getStackTrace0());
    } catch(Throwable e) {
      log(e);
    }

    return reasonId;
  }


  public String getDealerCode(String dealerCode) {
    if (dealerCode != null) {
      return dealerCode;
    } else {
      return provider.getDealerCode();
    }
  }

  public String getSalesRepId(String dealerCode, String salesRepId) {
    if (dealerCode != null || salesRepId != null) {
      return salesRepId;
    } else {
      return provider.getSalesRepId();
    }
  }

  public void contractSave(TMContract contract,
                           String dealerCode,
                           String salesRepCode) throws TelusAPIException {
    try {
      InteractionInfo report = newReport(
        (contract.isPriceplanChange())?TRANSACTION_TYPE_CHANGE_PRICE_PLAN:TRANSACTION_TYPE_CHANGE_SERVICE,
        getDealerCode(dealerCode),
        getSalesRepId(dealerCode, salesRepCode),
        contract.getSubscriber().getBanId(),
        contract.getSubscriber().getSubscriberId());

      long transactionId = report.getId();
      Date transactionDate = report.getDatetime();

      if(contract.isPriceplanChange()) {
        String oldPlan = contract.getSubscriber().getContract0().getPricePlan().getCode();
        String newPlan = contract.getPricePlan().getCode();

//        log("== Price Plan Change ==");
//
//        log("[ transactionId="+transactionId + " ]");
//        log("[ transactionDate="+transactionDate+" ]");
//        log(oldPlan.toString());
//        log(newPlan.toString());

        ServiceAgreementInfo[] services = contract.getDelegate().getModifiedServices();
//        for (int i = 0; i < services.length; i++)
//          log(services[i].toString());
        if(isAsyncInvokeConfigurationManagerService()){
        	PricePlanChangeInfo pricePlanChangeInfo =new PricePlanChangeInfo();
        	pricePlanChangeInfo.setTransactionId(transactionId);
        	pricePlanChangeInfo.setTransactionDate(transactionDate);
        	pricePlanChangeInfo.setOldPricePlanCode(oldPlan);
        	pricePlanChangeInfo.setNewPricePlanCode(newPlan);
        	pricePlanChangeInfo.setServiceAgreementInfo(services);
        	
        	provider.getSubscriberLifecycleFacade().asyncLogChangePricePlan(pricePlanChangeInfo);
        }else{
        	provider.getConfigurationManagerNew().report_changePricePlan(transactionId, transactionDate, oldPlan, newPlan, services);
        }
        
      } else {

//        log("== Service Change ==");
//
//        log("[ transactionId="+transactionId + " ]");
//        log("[ transactionDate="+transactionDate+" ]");

        ServiceAgreementInfo[] services = contract.getDelegate().getModifiedServices();
//        for (int i = 0; i < services.length; i++)
//          log(services[i].toString());
        if(isAsyncInvokeConfigurationManagerService()){
        	ServiceChangeInfo serviceChangeInfo =new ServiceChangeInfo();
        	serviceChangeInfo.setTransactionId(transactionId);
        	serviceChangeInfo.setTransactionDate(transactionDate);
        	serviceChangeInfo.setServices(services);
        	
        	provider.getSubscriberLifecycleFacade().asyncLogChangeService(serviceChangeInfo);
        }else{
        	provider.getConfigurationManagerNew().report_changeService(transactionId, transactionDate, services);
        }
      }
    } catch(TelusAPIException e) {
      // Silent failure
      log(e.getStackTrace0());
    } catch(Throwable e) {
      log(e);
    }
  }

  //==========================================================================================================

  public void changeAddress(TMAccount account) throws TelusAPIException {
    try {
	      InteractionInfo report = newReport(TRANSACTION_TYPE_CHANGE_ADDRESS, getDealerCode(null), 
	    		  getSalesRepId(null, null), account.getBanId(), null);
	      if(isAsyncInvokeConfigurationManagerService()){
	    	  AddressChangeInfo addressChangeInfo =new AddressChangeInfo();
	    	  addressChangeInfo.setTransactionId(report.getId());
	    	  addressChangeInfo.setTransactionDate(report.getDatetime());
	    	  addressChangeInfo.setOldAddress(account.getOldAddress().getDelegate());
	    	  addressChangeInfo.setNewAddress(account.getAddress0().getDelegate());
	        	
	         provider.getSubscriberLifecycleFacade().asyncLogChangeAddress(addressChangeInfo);
	        }else{
	        	provider.getConfigurationManagerNew().report_changeAddress(report.getId(), report.getDatetime(), 
	    		  account.getOldAddress().getDelegate(), account.getAddress0().getDelegate());
	        }
    } catch(TelusAPIException e) {
      log(e.getStackTrace0());
    } catch(Throwable e) {
      log(e);
    }
  }

  public void subscriberNewCharge(TMSubscriber subscriber, String chargeCode, String waiverCode) throws TelusAPIException {
    try {
    	InteractionInfo report = newReport(TRANSACTION_TYPE_CHARGE_SUBSCRIBER, getDealerCode(null), 
    		  getSalesRepId(null, null), subscriber.getBanId(), subscriber.getSubscriberId());
    	if(isAsyncInvokeConfigurationManagerService()){
    		SubscriberChargeInfo subscriberChargeInfo =new SubscriberChargeInfo();
    		subscriberChargeInfo.setTransactionId(report.getId());
    		subscriberChargeInfo.setTransactionDate(report.getDatetime());
    		subscriberChargeInfo.setChargeCode(chargeCode);
    		subscriberChargeInfo.setWaiverCode(waiverCode);
	        	
	         provider.getSubscriberLifecycleFacade().asyncLogSubscriberNewCharge(subscriberChargeInfo);
	      }else{
	        	provider.getConfigurationManagerNew().report_subscriberNewCharge(report.getId(), report.getDatetime(), chargeCode, waiverCode);
	      }
    } catch(TelusAPIException e) {
      log(e.getStackTrace0());
    } catch(Throwable e) {
      log(e);
    }
  }

  public void changePaymentMethod(TMAccount account, char oldPaymentMethod, char newPaymentMethod) throws TelusAPIException {
    try {
      InteractionInfo report = newReport(TRANSACTION_TYPE_CHANGE_PAYMENT_METHOD, getDealerCode(null),
        getSalesRepId(null, null), account.getBanId(), null);
      
      if(isAsyncInvokeConfigurationManagerService()){
    	  PaymentMethodChangeInfo paymentMethodChangeInfo =new PaymentMethodChangeInfo();
    	  paymentMethodChangeInfo.setTransactionId(report.getId());
    	  paymentMethodChangeInfo.setTransactionDate(report.getDatetime());
    	  paymentMethodChangeInfo.setOldPaymentMethod(oldPaymentMethod);
    	  paymentMethodChangeInfo.setNewPaymentMethod(newPaymentMethod);
	      provider.getSubscriberLifecycleFacade().asyncLogChangePaymentMethod(paymentMethodChangeInfo);
	   }else{
		   provider.getConfigurationManagerNew().report_changePaymentMethod(report.getId(), report.getDatetime(), oldPaymentMethod, newPaymentMethod);
	   }
    
    } catch(TelusAPIException e) {
      log(e.getStackTrace0());
    } catch(Throwable e) {
      log(e);
    }
  }

  public void makePayment(TMAccount account, char paymentMethod, double paymentAmount) throws TelusAPIException {
    try {
    	InteractionInfo report = newReport(TRANSACTION_TYPE_MAKE_PAYMENT, getDealerCode(null),
    			getSalesRepId(null, null), account.getBanId(), null);
    	if(isAsyncInvokeConfigurationManagerService()){
    		BillPaymentInfo billPaymentInfo =new BillPaymentInfo();
    		billPaymentInfo.setTransactionId(report.getId());
    		billPaymentInfo.setTransactionDate(report.getDatetime());
    		billPaymentInfo.setPaymentMethod(paymentMethod);
    		billPaymentInfo.setPaymentAmount(paymentAmount);
   	      provider.getSubscriberLifecycleFacade().asyncLogMakePayment(billPaymentInfo);
   	   }else{
   		   provider.getConfigurationManagerNew().report_makePayment(report.getId(), report.getDatetime(), paymentMethod, paymentAmount);
   	   }
    } catch(TelusAPIException e) {
      log(e.getStackTrace0());
    } catch(Throwable e) {
      log(e);
    }
  }

  public void accountStatusChange(TMAccount account, boolean oldHotlined, char oldStatus) throws TelusAPIException {
    try {
    	InteractionInfo report = newReport(TRANSACTION_TYPE_CHANGE_STATUS, getDealerCode(null),
    				getSalesRepId(null, null),account.getBanId(), null);

    	if(isAsyncInvokeConfigurationManagerService()){
    		AccountStatusChangeInfo accountStatusChangeInfo =new AccountStatusChangeInfo();
    		accountStatusChangeInfo.setTransactionId(report.getId());
    		accountStatusChangeInfo.setTransactionDate(report.getDatetime());
    		accountStatusChangeInfo.setOldHotlinedInd(booleanToChar(oldHotlined));
    		accountStatusChangeInfo.setNewHotlinedInd(booleanToChar(account.isHotlined()));
    		accountStatusChangeInfo.setOldStatus(oldStatus);
    		accountStatusChangeInfo.setNewStatus(account.getStatus());
    		accountStatusChangeInfo.setStatusFlag(STATUS_CHANGE_FLAG_ACCOUNT);
   	      	provider.getSubscriberLifecycleFacade().asyncLogAccountStatusChange(accountStatusChangeInfo);
   	   }else{
   		   provider.getConfigurationManagerNew().report_accountStatusChange(report.getId(), report.getDatetime(), 
    			  booleanToChar(oldHotlined), booleanToChar(account.isHotlined()), oldStatus, account.getStatus(), STATUS_CHANGE_FLAG_ACCOUNT );
   	   }
    } catch(TelusAPIException e) {
      log(e.getStackTrace0());
    } catch(Throwable e) {
      log(e);
    }
  }

  public void subscriberStatusChange(TMSubscriber subscriber, boolean oldHotlined, char oldStatus) throws TelusAPIException {
    try {
    	InteractionInfo report = newReport(TRANSACTION_TYPE_CHANGE_STATUS, getDealerCode(null),
    		  getSalesRepId(null, null), subscriber.getBanId(), subscriber.getSubscriberId());
    	if(isAsyncInvokeConfigurationManagerService()){
    		AccountStatusChangeInfo accountStatusChangeInfo =new AccountStatusChangeInfo();
    		accountStatusChangeInfo.setTransactionId(report.getId());
    		accountStatusChangeInfo.setTransactionDate(report.getDatetime());
    		accountStatusChangeInfo.setOldHotlinedInd(booleanToChar(oldHotlined));
    		accountStatusChangeInfo.setNewHotlinedInd(booleanToChar(subscriber.getAccount().getFinancialHistory().isHotlined()));
    		accountStatusChangeInfo.setOldStatus(oldStatus);
    		accountStatusChangeInfo.setNewStatus(subscriber.getStatus());
    		accountStatusChangeInfo.setStatusFlag(STATUS_CHANGE_FLAG_SUBSCRIBER);
   	      	provider.getSubscriberLifecycleFacade().asyncLogAccountStatusChange(accountStatusChangeInfo);
   	   }else{
   		    provider.getConfigurationManagerNew().report_accountStatusChange(report.getId(), report.getDatetime(), booleanToChar(oldHotlined) ,booleanToChar(subscriber.getAccount().getFinancialHistory().isHotlined()), oldStatus, subscriber.getStatus(), STATUS_CHANGE_FLAG_SUBSCRIBER);
   	   }
    } catch(TelusAPIException e) {
      log(e.getStackTrace0());
    } catch(Throwable e) {
      log(e);
    }
  }

  public void prepaidAccountTopUp(TMAutoTopUp autoTopUp, char topUpType) throws TelusAPIException {
    try {
      InteractionInfo report = newReport(TRANSACTION_TYPE_PREPAID_TOPUP,  getDealerCode(null),
    		  getSalesRepId(null, null), autoTopUp.getDelegate().getBan(), null);
      if(isAsyncInvokeConfigurationManagerService()){
    	  PrepaidTopupInfo prepaidTopupInfo =new PrepaidTopupInfo();
    	  prepaidTopupInfo.setTransactionId(report.getId());
    	  prepaidTopupInfo.setTransactionDate(report.getDatetime());
    	  prepaidTopupInfo.setAmount(new Double(autoTopUp.getChargeAmount()));
    	  prepaidTopupInfo.setCardType('C');
    	  prepaidTopupInfo.setTopUpType(topUpType);
  		
 	      provider.getSubscriberLifecycleFacade().asyncLogPrepaidAccountTopUp(prepaidTopupInfo);
 	   }else{
 		   provider.getConfigurationManagerNew().report_prepaidAccountTopUp(report.getId(), report.getDatetime(), 
        		new Double(autoTopUp.getChargeAmount()), 'C', topUpType);
 	   }
    } catch(TelusAPIException e) {
      log(e.getStackTrace0());
    } catch(Throwable e) {
      log(e);
    }
  }

  public void prepaidAccountTopUp(TMPrepaidConsumerAccount account, Double amount, char cardType, char topUpType) throws TelusAPIException {
    try {
      InteractionInfo report = newReport(TRANSACTION_TYPE_PREPAID_TOPUP, getDealerCode(null), 
    		  getSalesRepId(null, null), account.getBanId(), null);
      if(isAsyncInvokeConfigurationManagerService()){
    	  PrepaidTopupInfo prepaidTopupInfo =new PrepaidTopupInfo();
    	  prepaidTopupInfo.setTransactionId(report.getId());
    	  prepaidTopupInfo.setTransactionDate(report.getDatetime());
    	  prepaidTopupInfo.setAmount(amount);
    	  prepaidTopupInfo.setCardType(cardType);
    	  prepaidTopupInfo.setTopUpType(topUpType);
  		
 	      provider.getSubscriberLifecycleFacade().asyncLogPrepaidAccountTopUp(prepaidTopupInfo);
 	   }else{
 		   provider.getConfigurationManagerNew().report_prepaidAccountTopUp(report.getId(), 
    			  report.getDatetime(), amount, cardType, topUpType);
 	   }
    } catch(TelusAPIException e) {
      log(e.getStackTrace0());
    } catch(Throwable e) {
      log(e);
    }
  }

  public void subscriberChangeRole(TMSubscriber subscriber, String oldRole, String newRole) throws TelusAPIException {
    try {
    	InteractionInfo report = newReport(TRANSACTION_TYPE_CHANGE_ROLE, getDealerCode(null),
    		  getSalesRepId(null, null), subscriber.getBanId(),subscriber.getSubscriberId());
    	 if(isAsyncInvokeConfigurationManagerService()){
    		 RoleChangeInfo roleChangeInfo =new RoleChangeInfo();
    		 roleChangeInfo.setTransactionId(report.getId());
    		 roleChangeInfo.setTransactionDate(report.getDatetime());
    		 roleChangeInfo.setOldRole(oldRole);
    		 roleChangeInfo.setNewRole(newRole);
       	      provider.getSubscriberLifecycleFacade().asyncLogChangeRole(roleChangeInfo);
    	 }else{
    		   provider.getConfigurationManagerNew().report_changeRole(report.getId(), report.getDatetime(), oldRole, newRole);
    	 }
    } catch(TelusAPIException e) {
      log(e.getStackTrace0());
    } catch(Throwable e) {
      log(e);
    }
  }

  //==========================================================================================================

  public void subscriberChangePhoneNumber(TMSubscriber subscriber, AvailablePhoneNumber availablePhoneNumber, 
		  boolean changeOtherNumbers) throws TelusAPIException {
    try {
      InteractionInfo report = newReport(TRANSACTION_TYPE_CHANGE_PHONE_NUMBER, getDealerCode(null),
        getSalesRepId(null, null), subscriber.getBanId(), subscriber.getSubscriberId());
      if(isAsyncInvokeConfigurationManagerService()){
    	  PhoneNumberChangeInfo phoneNumberChangeInfo =new PhoneNumberChangeInfo();
    	  phoneNumberChangeInfo.setTransactionId(report.getId());
    	  phoneNumberChangeInfo.setTransactionDate(report.getDatetime());
    	  phoneNumberChangeInfo.setOldPhoneNumber(subscriber.getPhoneNumber());
    	  phoneNumberChangeInfo.setNewPhoneNumber(availablePhoneNumber.getPhoneNumber());
    	      provider.getSubscriberLifecycleFacade().asyncLogChangePhoneNumber(phoneNumberChangeInfo);
 	 }else{
        provider.getConfigurationManagerNew().report_changePhoneNumber(report.getId(), report.getDatetime(), 
        		subscriber.getPhoneNumber(), availablePhoneNumber.getPhoneNumber());
 	 }
    } catch(TelusAPIException e) {
      log(e.getStackTrace0());
//      throw(TelusAPIException)e.fillInStackTrace();
    } catch(Throwable e) {
      log(e);
//      throw new TelusAPIException(e);
    }
  }

  public void subscriberChangeEquipment(TMSubscriber subscriber, TMEquipment oldEquipment, EquipmentChangeRequest request) throws TelusAPIException {
    subscriberChangeEquipment(subscriber,
                              oldEquipment,
                              (TMEquipment)request.getNewEquipment(),
                              request.getDealerCode(),
                              request.getSalesRepCode(),
                              request.getRequestorId(),
                              request.getRepairId(),
                              request.getSwapType(),
                              (TMEquipment)request.getAssociatedMuleEquipment());
  }


  public void subscriberChangeEquipment(TMSubscriber subscriber,
                                        TMEquipment oldEquipment,
                                        TMEquipment newEquipment,
                                        String dealerCode,
                                        String salesRepCode,
                                        String requestorId,
                                        String repairId,
                                        String swapType,
                                        TMEquipment associatedMuleEquipment) throws TelusAPIException {

    try {
      InteractionInfo report = newReport(
        TRANSACTION_TYPE_CHANGE_EQUIPMENT,
        getDealerCode(null),       
        getSalesRepId(null, null), 
        subscriber.getBanId(),
        subscriber.getSubscriberId());

      SubscriberInfo subscriberInfo = subscriber.getDelegate();
      EquipmentInfo oldEquipmentInfo = oldEquipment.getDelegate();
      EquipmentInfo newEquipmentInfo = newEquipment.getDelegate();
      EquipmentInfo associatedMuleEquipmentInfo = null;

      if (associatedMuleEquipment != null)
        associatedMuleEquipmentInfo = associatedMuleEquipment.getDelegate();

      if(isAsyncInvokeConfigurationManagerService()){
    	  EquipmentChangeInfo equipmentChangeInfo =new EquipmentChangeInfo();
    	  equipmentChangeInfo.setTransactionId(report.getId());
    	  equipmentChangeInfo.setTransactionDate(report.getDatetime());
    	  equipmentChangeInfo.setSubscriberInfo(subscriberInfo);
    	  equipmentChangeInfo.setOldEquipmentInfo(oldEquipmentInfo);
    	  equipmentChangeInfo.setNewEquipmentInfo(newEquipmentInfo);
    	  equipmentChangeInfo.setDealerCode(dealerCode);
    	  equipmentChangeInfo.setSalesRepCode(salesRepCode);
    	  equipmentChangeInfo.setRequestorId(requestorId);
    	  equipmentChangeInfo.setRepairId(repairId);
    	  equipmentChangeInfo.setSwapType(swapType);
    	  equipmentChangeInfo.setAssociatedMuleEquipmentInfo(associatedMuleEquipmentInfo);
    	  equipmentChangeInfo.setApplicationName(subscriber.getProvider().getApplication());
    	  provider.getSubscriberLifecycleFacade().asyncLogSubscriberChangeEquipment(equipmentChangeInfo);
 	 }else{
	      provider.getConfigurationManagerNew().report_subscriberChangeEquipment(
		          report.getId(),
		          report.getDatetime(),
		          subscriberInfo,
		          oldEquipmentInfo,
		          newEquipmentInfo,
		          dealerCode,
		          salesRepCode,
		          requestorId,
		          repairId,
		          swapType,
		          associatedMuleEquipmentInfo,
		          subscriber.getProvider().getApplication());
 	 }
      
    } catch(TelusAPIException e) {
      log(e.getStackTrace0());
    } catch(Throwable e) {
      log(e);
    }

  }

  public void subscriberSave(TMSubscriber subscriber) throws TelusAPIException {
    try {
      SubscriberInfo oldSubscriber = subscriber.getOldDelegate();
      SubscriberInfo newSubscriber = subscriber.getDelegate();

      if(oldSubscriber != null) {
    	  InteractionInfo report = newReport(
    	        TRANSACTION_TYPE_CHANGE_SUBSCRIBER,
    	        getDealerCode(null),
    	        getSalesRepId(null, null),
    	        subscriber.getBanId(),
    	        subscriber.getSubscriberId());
      
    	  if(isAsyncInvokeConfigurationManagerService()){
    		  SubscriberChangeInfo subscriberChangeInfo =new SubscriberChangeInfo();
    		  subscriberChangeInfo.setTransactionId(report.getId());
    		  subscriberChangeInfo.setTransactionDate(report.getDatetime());
    		  subscriberChangeInfo.setOldSubscriber(oldSubscriber);
    		  subscriberChangeInfo.setNewSubscriber(newSubscriber);
        	      provider.getSubscriberLifecycleFacade().asyncLogChangeSubscriber(subscriberChangeInfo);
     	 }else{   
     		 provider.getConfigurationManagerNew().report_changeSubscriber(report.getId(), report.getDatetime(),  oldSubscriber, newSubscriber);
     	 } 
      }
    } catch(TelusAPIException e) {
      log(e.getStackTrace0());
    } catch(Throwable e) {
      log(e);
    }
  }

  public static char booleanToChar(boolean value) {
    return (value)?'Y':'N';
  }

  public static void log(String message) {
    System.err.println(message);
  }

  public static void log(Throwable t) {
    System.err.println("EXCEPTION: " + t.getMessage());
    t.printStackTrace(System.err);
  }
  
  private boolean isAsyncInvokeConfigurationManagerService() {
	  return AppConfiguration.isAsyncInvokeConfigurationManagerService();
  }

}




