package com.telus.provider.monitoring.aspectj;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Contract;
import com.telus.api.account.CreditCard;
import com.telus.api.account.IDENSubscriber;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.equipment.PagerEquipment;
import com.telus.api.reference.BillCycle;
import com.telus.api.reference.MigrationType;
import com.telus.api.reference.PrepaidEventType;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.ServiceSummary;


public abstract aspect ProviderPointcutAbstractAspect {
	
	pointcut allScope() : 
			subscriberScope() || accountScope() || accountSummaryScope() ||
			accountManagerScope() || referenceScope() || serviceSummaryScope ()
			|| equipmentManagerScope() || portRequestManagerScope() 
			|| queueEventManagerScope() || pcsSubscriberScope() || productSubscriberListScope() 
			|| providerScope() || contactEventScope() || deprecatedMethodScope()
			|| contractScope() || idenSubscriberScope() ||pagerSubscriberScope() ||cdpdSubscriberScope()
			|| tangoSubscriberScope() || addressScope() || configurationManagerScope() || portabilityScope() || 
			paymentArrangementEligibilityScope() || postPaidAccountScope() || chargeScope() || creditScope()
		;
	
	pointcut paymentArrangementEligibilityScope():
		execution(* com.telus.api.account.PaymentArrangementEligibility.applyPaymentArrangement (..))
	;	
	
	pointcut postPaidAccountScope():
		execution(* com.telus.api.account.PostpaidAccount.applyPaymentNotification (..)) ||
		execution(* com.telus.api.account.PostpaidAccount.savePaymentMethod (..)) 
	;
	
	pointcut chargeScope():
		execution(* com.telus.api.account.Charge.adjust (..))
		
	;
	
	pointcut creditScope():
		execution(* com.telus.api.account.Credit.apply (..)) ||
		execution(* com.telus.api.account.Credit.apply ())
	;
	
	pointcut subscriberScope():
		execution(* com.telus.api.account.Subscriber.activate (..)) ||
		execution(* com.telus.api.account.Subscriber.applyCredit (..)) ||
		execution(* com.telus.api.account.Subscriber.cancel (..)) ||
		execution(* com.telus.api.account.Subscriber.changeEquipment (..)) ||
		execution(* com.telus.api.account.Subscriber.changePhoneNumber (..)) ||
		execution(* com.telus.api.account.Subscriber.createDeposit (..)) ||
		execution(* com.telus.api.account.Subscriber.findAvailableActivationCredit (..)) ||
		execution(* com.telus.api.account.Subscriber.findAvailableActivationCredits (..)) ||
		execution(* com.telus.api.account.Subscriber.findAvailablePhoneNumbers (..)) ||
		execution(* com.telus.api.account.Subscriber.getAvailableNumberGroups (..)) ||
		execution(* com.telus.api.account.Subscriber.getAvailableNumberGroupsGivenNumberLocation (..)) ||
		execution(* com.telus.api.account.Subscriber.getAvailablePricePlan (..)) ||
		execution(* com.telus.api.account.Subscriber.getAvailablePricePlans (..)) ||
		execution(* com.telus.api.account.Subscriber.getBilledCalls (..)) ||
		execution(* com.telus.api.account.Subscriber.getCallingCirclePhoneNumberListHistory (..)) ||
		execution(* com.telus.api.account.Subscriber.getCards (..)) ||
		execution(* com.telus.api.account.Subscriber.getContract (..)) ||
		execution(* com.telus.api.account.Subscriber.getContractChangeHistory (..)) ||
		execution(* com.telus.api.account.Subscriber.getCredits (..)) ||
		execution(* com.telus.api.account.Subscriber.getCreditsByReasonCode (..)) ||
		execution(* com.telus.api.account.Subscriber.getDataUsageDetails (..)) ||
		execution(* com.telus.api.account.Subscriber.getDataUsageSummary (..)) ||
		execution(* com.telus.api.account.Subscriber.getDepositHistory (..)) ||
		execution(* com.telus.api.account.Subscriber.getDiscounts (..)) ||
		execution(* com.telus.api.account.Subscriber.getEquipment (..)) ||
		execution(* com.telus.api.account.Subscriber.getEquipmentChangeHistory (..)) ||
		execution(* com.telus.api.account.Subscriber.getFeatureParameterChangeHistory (..)) ||
		execution(* com.telus.api.account.Subscriber.getFeatureParameterHistory (..)) ||
		execution(* com.telus.api.account.Subscriber.getHandsetChangeHistory (..)) ||
		execution(* com.telus.api.account.Subscriber.getHistory (..)) ||
		execution(* com.telus.api.account.Subscriber.getInvoiceCallSortOrder (..)) ||
		execution(* com.telus.api.account.Subscriber.getPricePlanChangeHistory (..)) ||
		execution(* com.telus.api.account.Subscriber.getProvisioningTransactions (..)) ||
		execution(* com.telus.api.account.Subscriber.getProvisioningTransactions (..)) ||
		execution(* com.telus.api.account.Subscriber.getResourceChangeHistory (..)) ||
//		execution(* com.telus.api.account.Subscriber.getSecondarySerialNumbers (..)) ||
//		execution(* com.telus.api.account.Subscriber.getSerialNumber (..)) ||
		execution(* com.telus.api.account.Subscriber.getServiceChangeHistory (..)) ||
		execution(* com.telus.api.account.Subscriber.getSubscriptionRole (..)) ||
		execution(* com.telus.api.account.Subscriber.getUnbilledCalls (..)) ||
		execution(* com.telus.api.account.Subscriber.getUsageProfileListsSummary (..)) ||
		execution(* com.telus.api.account.Subscriber.getVendorServiceChangeHistory (..)) ||
		execution(* com.telus.api.account.Subscriber.getVoiceUsageSummary (..)) ||
		execution(* com.telus.api.account.Subscriber.getWebUsageSummary (..)) ||
		execution(* com.telus.api.account.Subscriber.move (..)) ||
		execution(* com.telus.api.account.Subscriber.refresh (..)) ||
		execution(* com.telus.api.account.Subscriber.refreshSwitch (..)) ||
		execution(* com.telus.api.account.Subscriber.removeFutureDatedPricePlanChange (..)) ||
		execution(* com.telus.api.account.Subscriber.renewContract (..)) ||
		execution(* com.telus.api.account.Subscriber.reserveAdditionalPhoneNumber (..)) ||
		execution(* com.telus.api.account.Subscriber.reservePhoneNumber (..)) ||
		execution(* com.telus.api.account.Subscriber.resetVoiceMailPassword (..)) ||
		execution(* com.telus.api.account.Subscriber.restore (..)) ||
		execution(* com.telus.api.account.Subscriber.retrieveSubscriber (..)) ||
		execution(* com.telus.api.account.Subscriber.save (..)) ||
		execution(* com.telus.api.account.Subscriber.sendEmail (..)) ||
		execution(* com.telus.api.account.Subscriber.sendFax (..)) ||
		execution(* com.telus.api.account.Subscriber.suspend (..)) ||
		execution(* com.telus.api.account.Subscriber.unreserve (..)) ||
		execution(* com.telus.api.account.Subscriber.getCancellationPenalty (..)) ||
		execution(* com.telus.api.account.Subscriber.getContractForEquipmentChange (..))
		
	;
	
	pointcut accountScope(): 
		execution(* com.telus.api.account.Account.applyPayment (..)) ||
		execution(* com.telus.api.account.Account.cancel (..)) ||
		execution(* com.telus.api.account.Account.cancelSubscribers (..)) ||
		execution(* com.telus.api.account.Account.checkInternationalServiceEligibility (..)) ||
		execution(* com.telus.api.account.Account.checkNewSubscriberEligibility (..)) ||
		execution(* com.telus.api.account.Account.checkRewardRedemptionEligibility (..)) ||
		execution(* com.telus.api.account.Account.createDuplicateAccount (..)) ||
		execution(* com.telus.api.account.Account.getActiveSubscribersCount (..)) ||
//		execution(* com.telus.api.account.Account.getAddress (..)) ||
		execution(* com.telus.api.account.Account.getAddressChangeHistory (..)) ||
//		execution(* com.telus.api.account.Account.getAllActiveSubscribersCount (..)) ||
//		execution(* com.telus.api.account.Account.getAllCancelledSubscribersCount (..)) ||
//		execution(* com.telus.api.account.Account.getAllReservedSubscribersCount (..)) ||
//		execution(* com.telus.api.account.Account.getAllSubscriberCount (..)) ||
//		execution(* com.telus.api.account.Account.getAllSuspendedSubscribersCount (..)) ||
		execution(* com.telus.api.account.Account.getBilledCharges (..)) ||
		execution(* com.telus.api.account.Account.getCredits (..)) ||
		execution(* com.telus.api.account.Account.getDepositAssessedHistory (..)) ||
		execution(* com.telus.api.account.Account.getDepositHistory (..)) ||
		execution(* com.telus.api.account.Account.getDuplicateAccountBANs (..)) ||
		execution(* com.telus.api.account.Account.getDuplicateAccounts (..)) ||
		execution(* com.telus.api.account.Account.getInvoiceHistory (..)) ||
		execution(* com.telus.api.account.Account.getPaymentHistory (..)) ||
		execution(* com.telus.api.account.Account.getPaymentMethodChangeHistory (..)) ||
		execution(* com.telus.api.account.Account.getPendingChargeHistory (..)) ||
		execution(* com.telus.api.account.Account.getPortRequests (..)) ||
		execution(* com.telus.api.account.Account.getProductSubscriberLists (..)) ||
		execution(* com.telus.api.account.Account.payBill (..)) ||
		execution(* com.telus.api.account.Account.payDeposit (..)) ||
		execution(* com.telus.api.account.Account.refresh (..)) ||
		execution(* com.telus.api.account.Account.save (..)) ||
		execution(* com.telus.api.account.Account.saveBillNotificationDetails (..)) ||
		execution(* com.telus.api.account.Account.suspend (..)) ||
		execution(* com.telus.api.account.Account.suspendSubscribers (..)) ||
		execution(* com.telus.api.account.Account.hasEPostSubscription (..)) ||
		execution(* com.telus.api.account.Account.getCancellationPenalty (..))
		
	;
	
	pointcut accountSummaryScope(): 
		execution(* com.telus.api.account.AccountSummary.getBillNotificationContacts (..)) ||
		execution(* com.telus.api.account.AccountSummary.getBillNotificationHistory (..)) ||
		execution(* com.telus.api.account.AccountSummary.getLastEBillNotificationSent  (..)) ||
		execution(* com.telus.api.account.AccountSummary.getLastEBillRegistrationReminderSent (..)) ||
		execution(* com.telus.api.account.AccountSummary.isEBillRegistrationReminderExist (..)) ||
		execution(* com.telus.api.account.AccountSummary.saveAuthorizedNames (..)) ||
		execution(* com.telus.api.account.AccountSummary.saveEBillRegistrationReminder (..)) ||
		execution(* com.telus.api.account.AccountSummary.savePin (..)) ||
		execution(* com.telus.api.account.AccountSummary.updatePortRestriction (..)) ||
		execution(* com.telus.api.account.AccountSummary.getPaperBillSupressionAtActivationInd (..))
	;
	
	pointcut accountManagerScope(): 
		execution(* com.telus.api.account.AccountManager.* (..))
	;
	
	pointcut portRequestManagerScope(): 
		execution(* com.telus.api.portability.PortRequestManager.* (..))
	;
	
	pointcut referenceScope():
		execution(* com.telus.api.reference.ReferenceDataManager.findPricePlans (..)) ||
		execution(* com.telus.api.reference.ReferenceDataManager.findPromotionalDiscounts (..)) ||
		execution(* com.telus.api.reference.ReferenceDataManager.getAvailablePhoneNumber (..)) ||
		execution(* com.telus.api.reference.ReferenceDataManager.getPricePlan (..)) ||
		execution(* com.telus.api.reference.ReferenceDataManager.getRegularServices (..)) ||
		execution(* com.telus.api.reference.ReferenceDataManager.getWPSServices (..)) ||
		execution(* com.telus.api.reference.ReferenceDataManager.refresh (..)) ||
		execution(* com.telus.api.reference.ReferenceDataManager.getDataUsageServiceTypes (..)) ||
		execution(* com.telus.api.reference.ReferenceDataManager.getDataUsageUnits (..)) ||
		execution(* com.telus.api.reference.ReferenceDataManager.getMobileCountries (..))		
	;
	
	pointcut serviceSummaryScope():
		execution(* com.telus.api.reference.ServiceSummary.containsPrivilege (..))
	;
	
	pointcut equipmentManagerScope():
		execution(* com.telus.api.equipment.EquipmentManager.* (..))
	;
	pointcut queueEventManagerScope():
		execution(* com.telus.api.queueevent.QueueEventManager.* (..))
	;		
	pointcut pcsSubscriberScope() :
		execution(* com.telus.api.account.PCSSubscriber.* (..))
	;
	pointcut productSubscriberListScope() :
		execution(* com.telus.api.account.ProductSubscriberList.getActiveSubscribers (..)) ||
		execution(* com.telus.api.account.ProductSubscriberList.getCancelledSubscribers (..)) || 
		execution(* com.telus.api.account.ProductSubscriberList.getReservedSubscribers (..)) || 
		execution(* com.telus.api.account.ProductSubscriberList.getSuspendedSubscribers (..)) 
	;
	
	pointcut providerScope() :
		execution(public com.telus.provider.TMProvider.new(String, String, String, String, int[]) throws TelusAPIException)
	;
	
	pointcut contactEventScope() :
		execution(* com.telus.api.contactevent.ContactEventManager.* (..))
	;
	
			
	pointcut deprecatedMethodScope() :
		execution(* com.telus.api.account.Account.getSpecialInstructions ()) ||
		execution(* com.telus.api.account.PostpaidAccount.changeBillCycle (BillCycle)) ||
		execution(* com.telus.api.account.PostpaidAccount.changeBillCycle (BillCycle, boolean)) ||
		execution(* com.telus.api.account.Account.isHotlined ()) ||
		execution(* com.telus.api.account.Account.getInternationalDialingDepositAmount ()) ||
		execution(* com.telus.api.account.Account.getAirtimeMinutePoolingEnabledPricePlanSubscriberCounts ()) ||
		execution(* com.telus.api.account.Account.getLDMinutePoolingEnabledPricePlanSubscriberCounts ()) ||
		execution(* com.telus.api.account.Account.getCancellationPenaltyList (String[])) ||
		execution(* com.telus.api.account.AccountManager.newPCSPrepaidConsumerAccount (String, int, String, CreditCard, String)) ||
		execution(* com.telus.api.account.AccountSummary.newCredit (boolean)) ||		
		execution(* com.telus.api.account.BillNotificationContact.isBillNotificationEnabled ()) ||
		execution(* com.telus.api.account.BillNotificationContact.setBillNotificationEnabled (boolean)) ||
		execution(* com.telus.api.account.BillNotificationContact.getBillNotificationRegistrationId ()) ||
		execution(* com.telus.api.account.BillNotificationContact.setBillNotificationRegistrationId (String)) ||
		execution(* com.telus.api.account.BillNotificationContact.getPortalUserId ()) ||
		execution(* com.telus.api.account.BillNotificationContact.getClientAccountId ()) ||
		execution(* com.telus.api.account.BillNotificationContact.setPortalUserId (int)) ||
		execution(* com.telus.api.account.BillNotificationContact.setBillNotificationType (String)) ||
		execution(* com.telus.api.account.BillNotificationContact.getBillNotificationType ()) ||
		execution(* com.telus.api.account.Contract.isPTTServiceIncluded ()) ||
		execution(* com.telus.api.account.Credit.isTaxable ()) ||
		execution(* com.telus.api.account.FinancialHistory.getCollectionStep ()) ||
		execution(* com.telus.api.account.FinancialHistory.getCollectionAgency ()) ||
		execution(* com.telus.api.account.FinancialHistory.getNextCollectionStep ()) ||
		execution(* com.telus.api.account.FollowUp.getAssignedTo ()) ||
		execution(* com.telus.api.account.FollowUp.setAssignedTo (String)) ||
		execution(* com.telus.api.account.IDENSubscriber.reservePhoneNumber (PhoneNumberReservation, boolean)) ||
		execution(* com.telus.api.account.PagerSubscriber.changeEquipment (PagerEquipment, PagerEquipment[], String, String, String, boolean)) ||
		execution(* com.telus.api.account.PagerSubscriber.testChangeEquipment (PagerEquipment, PagerEquipment[], String, String, String, boolean)) ||
		execution(* com.telus.api.account.PaymentArrangementEligibility.applyPaymentArrangement ()) ||
		execution(* com.telus.api.account.PCSSubscriber.testChangeEquipment (Equipment , Equipment[], String, String, String, String, String, boolean)) ||
		execution(* com.telus.api.account.PrepaidConsumerAccount.changeAirtimeRate (double)) ||
		execution(* com.telus.api.account.PrepaidConsumerAccount.getPrepaidActivationCharge ()) ||
		execution(* com.telus.api.account.Subscriber.testChangeEquipment (Equipment, String, String, String, String, String, boolean)) ||
		execution(* com.telus.api.account.Subscriber.changeEquipment (Equipment, String, String, String, String, String, boolean, boolean)) ||
		execution(* com.telus.api.account.Subscriber.isValidMigrationForPhoneNumber(MigrationType)) ||
		execution(* com.telus.api.account.PCSAccount.getPricePlanSubscriberCount ()) ||
		execution(* com.telus.api.account.PCSAccount.getPricePlanSubscriberCount (String)) ||
		execution(* com.telus.api.account.PostpaidAccount.reportPayment (double)) ||
		execution(* com.telus.api.account.PostpaidBusinessRegularAccount.getFirstName ()) ||
		execution(* com.telus.api.account.PostpaidBusinessRegularAccount.getLastName ()) ||
		execution(* com.telus.api.account.PostpaidConsumerAccount.getAdditionalName ()) ||
		execution(* com.telus.api.account.PostpaidConsumerAccount.setAdditionalName (String)) ||
		execution(* com.telus.api.account.PostpaidConsumerAccount.getCreditInformation ()) ||
		execution(* com.telus.api.account.PostpaidConsumerAccount.getCLMSummary()) ||
		execution(* com.telus.api.account.PrepaidConsumerAccount.applyTopUp (double)) ||
		execution(* com.telus.api.account.PrepaidConsumerAccount.changeAirtimeRate (double)) ||		
		execution(* com.telus.api.account.PrepaidConsumerAccount.getPrepaidActivationCharge ()) ||
		execution(* com.telus.api.account.Subscriber.getFirstName ()) ||
		execution(* com.telus.api.account.Subscriber.getMiddleInitial ()) ||
		execution(* com.telus.api.account.Subscriber.getLastName ()) ||
		execution(* com.telus.api.equipment.Equipment.isPCS ()) ||
		execution(* com.telus.api.equipment.Equipment.updateStatus (long, long)) ||
		execution(* com.telus.api.equipment.MuleEquipment.testTransferWarranty (IDENSubscriber,
				MuleEquipment, String, String, String, String, boolean)) ||
		execution(* com.telus.api.equipment.MuleEquipment.transferWarranty (IDENSubscriber,
				MuleEquipment, String, String, String, String, boolean)) ||
		execution(* com.telus.api.equipment.Warranty.getInitialManufactureDate ()) || 
		execution(* com.telus.api.equipment.Warranty.getLatestPendingDate ()) ||
		execution(* com.telus.api.equipment.Warranty.getLatestPendingModel ()) ||
		execution(* com.telus.api.equipment.Warranty.getWarrantyExtensionDate ()) ||
		execution(* com.telus.api.portability.PortInEligibility.isPCSCoverage ()) ||
		execution(* com.telus.api.portability.PortInEligibility.isPrepaidCoverage ()) ||
		execution(* com.telus.api.portability.PortInEligibility.isPostPaidCoverage ()) ||
		execution(* com.telus.api.portability.PortRequestManager.testEligibility (String, String)) ||
		execution(* com.telus.api.portability.PortRequestManager.getPRMReferenceData (String, String)) ||
		execution(* com.telus.api.reference.PricePlan.getPricePlanFamily (String, String, boolean, int)) ||
		execution(* com.telus.api.reference.PricePlan.getIncludedPromotions (String, String, int)) ||
		execution(* com.telus.api.reference.ReferenceDataManager.getNetworkDesignationId (String)) ||
		execution(* com.telus.api.reference.ReferenceDataManager.getAccountType (String)) ||
		execution(* com.telus.api.reference.ReferenceDataManager.retainByPrivilege (ServiceSummary[], String, String)) ||
		execution(* com.telus.api.reference.ReferenceDataManager.removeByPrivilege (ServiceSummary[], String, String)) ||
		execution(* com.telus.api.reference.ReferenceDataManager.retainServices (ServiceSummary[], String, String)) ||
		execution(* com.telus.api.reference.Service.isRUIM ()) ||
		execution(* com.telus.api.reference.ServiceSummary.getEquipmentTypes ()) ||
		execution(* com.telus.api.reference.ServiceSummary.getEquivalentService (PricePlan)) ||
		execution(* com.telus.api.reference.ServiceSummary.getNetworkType ()) ||
		execution(* com.telus.api.reference.ServiceSummary.getRelations (Contract)) ||
		execution(* com.telus.api.reference.ServiceSummary.getRelations (Contract, String)) ||
	    execution(* com.telus.api.account.AccountManager.getPrepaidCallHistory(String , Date , Date )) ||
	    execution(* com.telus.api.account.AccountManager.getPrepaidEventHistory(String, Date, Date )) ||
	    execution(* com.telus.api.account.AccountManager.getPrepaidEventHistory(String, Date, Date , PrepaidEventType[] )) ||
	    execution(* com.telus.api.account.PrepaidConsumerAccount.getPrepaidCallHistory(Date , Date)) ||
	    execution(* com.telus.api.account.PrepaidConsumerAccount.getPrepaidEventHistory(Date , Date )) ||
	    execution(* com.telus.api.account.PrepaidConsumerAccount.getPrepaidEventHistory(Date , Date , PrepaidEventType[] ))
	;
	
	pointcut contractScope() :	
		execution(* com.telus.api.account.Contract.*(..)) 
	;
	
	pointcut idenSubscriberScope() :	
		execution(* com.telus.api.account.IDENSubscriber.*(..)) 
	;
	
	pointcut pagerSubscriberScope() :	
		execution(* com.telus.api.account.PagerSubscriber.*(..)) 
	;	
	
	pointcut cdpdSubscriberScope() :	
		execution(* com.telus.api.account.CDPDSubscriber.*(..)) 
	;
	
	pointcut tangoSubscriberScope() :	
		execution(* com.telus.api.account.TangoSubscriber.*(..)) 
	;	
	
	pointcut addressScope() :	
		execution(* com.telus.api.account.Address.*(..)) 
	;	
	pointcut configurationManagerScope() :	
		execution(* com.telus.api.config1.ConfigurationManager.*(..)) 
	;
	
	pointcut portabilityScope() :
		execution(* com.telus.api.portability.PortRequest.activate())
	;
	
}
