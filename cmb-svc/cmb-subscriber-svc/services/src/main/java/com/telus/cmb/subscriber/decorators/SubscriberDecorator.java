package com.telus.cmb.subscriber.decorators;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.api.HistorySearchException;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.ActivationCredit;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.Address;
import com.telus.api.account.AddressNotFoundException;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.CallList;
import com.telus.api.account.CallingCirclePhoneList;
import com.telus.api.account.CancellationPenalty;
import com.telus.api.account.Charge;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.ContractChangeHistory;
import com.telus.api.account.Credit;
import com.telus.api.account.DepositHistory;
import com.telus.api.account.Discount;
import com.telus.api.account.EquipmentChangeHistory;
import com.telus.api.account.EquipmentChangeRequest;
import com.telus.api.account.FeatureParameterHistory;
import com.telus.api.account.FollowUp;
import com.telus.api.account.HandsetChangeHistory;
import com.telus.api.account.InvalidEquipmentChangeException;
import com.telus.api.account.InvoiceTax;
//import com.telus.api.account.LMSLetterRequest;
import com.telus.api.account.Memo;
import com.telus.api.account.NumberMatchException;
import com.telus.api.account.PhoneNumberException;
import com.telus.api.account.PhoneNumberInUseException;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.api.account.PricePlanChangeHistory;
import com.telus.api.account.ProductSubscriberList;
import com.telus.api.account.ProvisioningTransaction;
import com.telus.api.account.QueueThresholdEvent;
import com.telus.api.account.ResourceChangeHistory;
import com.telus.api.account.SeatData;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.ServiceChangeHistory;
import com.telus.api.account.Subscriber;
import com.telus.api.account.SubscriberCommitment;
import com.telus.api.account.SubscriberHistory;
import com.telus.api.account.SubscriberIdentifier;
import com.telus.api.account.SubscriptionPreference;
import com.telus.api.account.SubscriptionRole;
import com.telus.api.account.TaxExemption;
import com.telus.api.account.UsageProfileListsSummary;
import com.telus.api.account.VendorServiceChangeHistory;
import com.telus.api.account.VoiceUsageSummary;
import com.telus.api.account.VoiceUsageSummaryException;
import com.telus.api.account.WebUsageSummary;
import com.telus.api.equipment.Card;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentWarrantyNotAvailableException;
import com.telus.api.equipment.IDENEquipment;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.message.ApplicationMessage;
import com.telus.api.reference.DiscountPlan;
import com.telus.api.reference.InvoiceCallSortOrderType;
//import com.telus.api.reference.Letter;
import com.telus.api.reference.MigrationType;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.ReasonType;
import com.telus.api.reference.SeatType;
import com.telus.api.reference.ServiceSummary;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.cmb.subscriber.bo.AccountBo;
import com.telus.cmb.subscriber.bo.ActivationOptionBo;
import com.telus.cmb.subscriber.bo.ContractBo;
import com.telus.cmb.subscriber.bo.EquipmentBo;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

public abstract class SubscriberDecorator {
	
	protected final SubscriberInfo delegate;
	
	boolean lastActiveStarterSeatInd = false;
	boolean lastSuspendedStarterSeatInd = false;
	int activeStarterseatCount = 0;
	int suspendedStarterSeatCount = 0;
	
	public SubscriberDecorator(SubscriberInfo subscriber) {
		delegate = subscriber;
	}
	
	public SubscriberInfo getDelegate() {
		return delegate;
	}
	
	public void save() throws TelusAPIException {
		delegate.save();
	}
	
	public void save(boolean activate) throws TelusAPIException {
		delegate.save(activate);
	}
	
	public void save(Date startServiceDate) throws TelusAPIException {
		delegate.save(startServiceDate);
	}
	
	public void refresh() throws TelusAPIException {
		delegate.refresh();
	}
	
	public void activate() throws TelusAPIException {
		delegate.activate();
	}
	
	public void activate(String reason) throws TelusAPIException {
		delegate.activate(reason);
	}
	
	public void activate(String reason, String memoText) throws TelusAPIException {
		delegate.activate(reason, memoText);
	}

	public void activate(Date startServiceDate) throws TelusAPIException {
		delegate.activate(startServiceDate);
	}
	
	public void activate(String reason, Date startServiceDate) throws TelusAPIException {
		delegate.activate(reason, startServiceDate);
	}

	public void activate(String reason, Date startServiceDate, String memoText) throws TelusAPIException {
		delegate.activate(reason, startServiceDate, memoText);
	}
	
	public void unreserve() throws TelusAPIException {
		delegate.unreserve();
	}
	
	public void reservePhoneNumber(PhoneNumberReservation phoneNumberReservation) throws TelusAPIException, NumberMatchException {
		delegate.reserveMobileNumber(phoneNumberReservation);
	}
	
	public int getBanId() {
		return delegate.getBanId();
	}
	
	public String getSubscriberId() {
		return delegate.getSubscriberId();
	}
	
	public String getPhoneNumber() {
		return delegate.getPhoneNumber();
	}
	
	@SuppressWarnings("deprecation")
	public String getFirstName() {
		return delegate.getFirstName();
	}
	
	@SuppressWarnings("deprecation")
	public void setFirstName(String firstName) {
		delegate.setFirstName(firstName);
	}
	
	@SuppressWarnings("deprecation")
	public String getMiddleInitial() {
		return delegate.getMiddleInitial();
	}
	
	@SuppressWarnings("deprecation")
	public void setMiddleInitial(String middleInitial) {
		delegate.setMiddleInitial(middleInitial);
	}

	@SuppressWarnings("deprecation")
	public String getLastName() {
		return delegate.getLastName();
	}
	
	@SuppressWarnings("deprecation")
	public void setLastName(String lastName) {
		delegate.setLastName(lastName);
	}
	
	public ConsumerName getConsumerName() {
		return delegate.getConsumerName();
	}

	public String getLanguage() {
		return delegate.getLanguage();
	}
	
	public void setLanguage(String language) {
		delegate.setLanguage(language);
	}
	
	public void setVoiceMailLanguage(String voiceMailLanguage) {
		delegate.setVoiceMailLanguage(voiceMailLanguage);
	}
	
	public String getVoiceMailLanguage() {
		return delegate.getVoiceMailLanguage();
	}
	
	public void setDealerHasDeposit(boolean dealerHasDeposit) {
		delegate.setDealerHasDeposit(dealerHasDeposit);
	}
	
	public boolean getDealerHasDeposit() {
		return delegate.getDealerHasDeposit();
	}
	
	public String getSerialNumber() {
		return delegate.getSerialNumber();
	}
	
	public String[] getSecondarySerialNumbers() {
		return delegate.getSecondarySerialNumbers();
	}
	
	public String getUserValueRating() {
		return delegate.getUserValueRating();
	}

	public SubscriptionPreference getSubscriptionPreference(int preferenceTopicId) throws TelusAPIException {
		return delegate.getSubscriptionPreference(preferenceTopicId);
	}
	
	public char getStatus() {
		return delegate.getStatus();
	}
	
	public String getMarketProvince() {
		return delegate.getMarketProvince();
	}
	
	public String getProductType() {
		return delegate.getProductType();
	}
	
	public String getPricePlan() {
		return delegate.getPricePlan();
	}
	
	public String getDealerCode() {
		return delegate.getDealerCode();
	}
	
	public void setDealerCode(String dealerCode) {
		delegate.setDealerCode(dealerCode);
	}

	public String getSalesRepId() {
		return delegate.getSalesRepId();
	}
	
	public void setSalesRepId(String salesRepId) {
		delegate.setSalesRepId(salesRepId);
	}
	
	public Date getBirthDate() {
		return delegate.getBirthDate();
	}

	public void setBirthDate(Date birthDate) {
		delegate.setBirthDate(birthDate);
	}

	public abstract AccountBo getAccount() throws ApplicationException;
	
	public abstract ContractBo newContract(PricePlanDecorator pricePlan, int term) throws ApplicationException;
	
	public abstract ContractBo newContract(PricePlanDecorator pricePlan, int term, EquipmentChangeRequest equipmentChangeRequest) throws ApplicationException;
	
	public abstract ContractBo renewContract(int term) throws ApplicationException;
	
	public abstract ContractBo renewContract(PricePlanDecorator pricePlan, int term) throws ApplicationException;
	
	public abstract ContractBo renewContract(PricePlanDecorator pricePlan, int term, EquipmentChangeRequest equipmentChangeRequest) throws ApplicationException;

	public abstract ContractBo getContract() throws ApplicationException;

	public String getEmailAddress() {
		return delegate.getEmailAddress();
	}

	public void setEmailAddress(String emailAddress) {
		delegate.setEmailAddress(emailAddress);
	}

	public String getFaxNumber() {
		return delegate.getFaxNumber();
	}
	
	public abstract EquipmentBo getEquipment() throws SystemException, ApplicationException;

	public Date getCreateDate() {
		return delegate.getCreateDate();
	}
	
	public Date getStartServiceDate() {
		return delegate.getStartServiceDate();
	}
	
	public void setActivityReasonCode(String activityReasonCode) {
		delegate.setActivityReasonCode(activityReasonCode);
	}

	public String getActivityCode() {
		return delegate.getActivityCode();
	}

	public String getActivityReasonCode() {
		return delegate.getActivityReasonCode();
	}

	public boolean isIDEN() {
		return delegate.isIDEN();
	}

	public boolean isPCS() {
		return delegate.isPCS();
	}
	
	public AvailablePhoneNumber[] findAvailablePhoneNumbers(PhoneNumberReservation phoneNumberReservation, int maximum) throws TelusAPIException, PhoneNumberException {
		return delegate.findAvailablePhoneNumbers(phoneNumberReservation, maximum);
	}

	public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, boolean changeOtherNumbers) throws TelusAPIException, PhoneNumberException, PhoneNumberInUseException {
		delegate.changePhoneNumber(availablePhoneNumber, changeOtherNumbers);
	}
	
	public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, boolean changeOtherNumbers, String dealerCode, String salesRepCode) throws TelusAPIException, PhoneNumberException,
			PhoneNumberInUseException {
		delegate.changePhoneNumber(availablePhoneNumber, changeOtherNumbers, dealerCode, salesRepCode);
	}

	public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, boolean changeOtherNumbers, String dealerCode, String salesRepCode, String reasonCode) throws TelusAPIException,
			PhoneNumberException, PhoneNumberInUseException {
		delegate.changePhoneNumber(availablePhoneNumber, changeOtherNumbers, dealerCode, salesRepCode, reasonCode);
	}

	public void reserveAdditionalPhoneNumber(AvailablePhoneNumber availablePhoneNumber) throws TelusAPIException, PhoneNumberException, PhoneNumberInUseException {
		delegate.reserveAdditionalPhoneNumber(availablePhoneNumber);
	}

	public Memo newMemo() throws TelusAPIException {
		return delegate.newMemo();
	}

	public FollowUp newFollowUp() throws TelusAPIException {
		return delegate.newFollowUp();
	}

	public Charge newCharge() throws TelusAPIException {
		return delegate.newCharge();
	}

	public Credit newCredit() throws TelusAPIException {
		return delegate.newCredit();
	}
	
	public Credit newCredit(boolean taxable) throws TelusAPIException {
		return delegate.newCredit(taxable);
	}

	public Credit newCredit(char taxOption) throws TelusAPIException {
		return delegate.newCredit(taxOption);
	}

	public Discount newDiscount() throws TelusAPIException {
		return delegate.newDiscount();
	}

	public Discount[] getDiscounts() throws TelusAPIException {
		return delegate.getDiscounts();
	}

	public ApplicationMessage[] testChangeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType) throws TelusAPIException,
			SerialNumberInUseException, InvalidEquipmentChangeException {
		return delegate.testChangeEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType);
	}

	public ApplicationMessage[] testChangeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean ignoreSerialNoInUse)
			throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		return delegate.testChangeEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, ignoreSerialNoInUse);
	}

	public ApplicationMessage[] testChangeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, char allowDuplicateSerialNo)
			throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		return delegate.testChangeEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, allowDuplicateSerialNo);
	}

	public void testChangeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment)
			throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		delegate.testChangeEquipment(newIDENEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, associatedMuleEquipment);
	}

	public void testChangeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType,
			MuleEquipment associatedMuleEquipment, boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		delegate.testChangeEquipment(newIDENEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, associatedMuleEquipment, ignoreSerialNoInUse);
	}

	public void testChangeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType,
			MuleEquipment associatedMuleEquipment, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		delegate.testChangeEquipment(newIDENEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, associatedMuleEquipment, allowDuplicateSerialNo);
	}

	public ApplicationMessage[] changeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType) throws TelusAPIException,
			SerialNumberInUseException, InvalidEquipmentChangeException {
		return delegate.changeEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType);
	}

	public ApplicationMessage[] changeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices)
			throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		return delegate.changeEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices);
	}

	public ApplicationMessage[] changeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices,
			boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		return delegate.changeEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices, ignoreSerialNoInUse);
	}

	public ApplicationMessage[] changeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices,
			char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		return delegate.changeEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices, allowDuplicateSerialNo);
	}

	public ApplicationMessage[] changeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices,
			char allowDuplicateSerialNo, ServiceRequestHeader header) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		return delegate.changeEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices, allowDuplicateSerialNo, header);
	}

	public void changeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment)
			throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		delegate.changeEquipment(newIDENEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, associatedMuleEquipment);
	}

	public void changeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment,
			boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		delegate.changeEquipment(newIDENEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, associatedMuleEquipment, ignoreSerialNoInUse);
	}
	
	public void changeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment,
			char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		delegate.changeEquipment(newIDENEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, associatedMuleEquipment, allowDuplicateSerialNo);

	}

	public EquipmentChangeRequest newEquipmentChangeRequest(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType)
			throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		return delegate.newEquipmentChangeRequest(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType);
	}

	public EquipmentChangeRequest newEquipmentChangeRequest(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType,
			boolean preserveDigitalServices) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		return delegate.newEquipmentChangeRequest(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices);
	}

	public EquipmentChangeRequest newEquipmentChangeRequest(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType,
			boolean preserveDigitalServices, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		return delegate.newEquipmentChangeRequest(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices, allowDuplicateSerialNo);
	}

	public void resetVoiceMailPassword() throws TelusAPIException {
		delegate.resetVoiceMailPassword();
	}

	public int getProvisioningPlatformId() throws TelusAPIException {
		return delegate.getProvisioningPlatformId();
	}

	public ProvisioningTransaction[] getProvisioningTransactions(Date startDate, Date endDate) throws TelusAPIException {
		return delegate.getProvisioningTransactions(startDate, endDate);
	}

	public CallList getBilledCalls(int billSeqNo) throws TelusAPIException {
		return delegate.getBilledCalls(billSeqNo);
	}

	public CallList getBilledCalls(int billSeqNo, char callType) throws TelusAPIException {
		return delegate.getBilledCalls(billSeqNo, callType);
	}

	public CallList getBilledCalls(int billSeqNo, char callType, Date from, Date to, boolean getAll) throws TelusAPIException {
		return delegate.getBilledCalls(billSeqNo, callType, from, to, getAll);
	}

	public CallList getBilledCalls(int billSeqNo, Date from, Date to, boolean getAll) throws TelusAPIException {
		return delegate.getBilledCalls(billSeqNo, from, to, getAll);
	}

	public CallList getUnbilledCalls() throws TelusAPIException {
		return delegate.getUnbilledCalls();
	}

	public NumberGroup[] getAvailableNumberGroups() throws TelusAPIException {
		return delegate.getAvailableNumberGroups();
	}

	public NumberGroup[] getAvailableNumberGroups(String marketArea) throws TelusAPIException {
		return delegate.getAvailableNumberGroups(marketArea);
	}

	public NumberGroup[] getAvailableNumberGroupsGivenNumberLocation(String numberLocation) throws TelusAPIException {
		return delegate.getAvailableNumberGroupsGivenNumberLocation(numberLocation);
	}

	public ContractChangeHistory[] getContractChangeHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		return delegate.getContractChangeHistory(from, to);
	}

	public HandsetChangeHistory[] getHandsetChangeHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		return delegate.getHandsetChangeHistory(from, to);
	}

	public PricePlanChangeHistory[] getPricePlanChangeHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		return delegate.getPricePlanChangeHistory(from, to);
	}

	public ServiceChangeHistory[] getServiceChangeHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		return delegate.getServiceChangeHistory(from, to);
	}

	public ServiceChangeHistory[] getServiceChangeHistory(Date from, Date to, boolean includeAllServices) throws TelusAPIException, HistorySearchException {
		return delegate.getServiceChangeHistory(from, to, includeAllServices);
	}
	
	public ResourceChangeHistory[] getResourceChangeHistory(String type, Date from, Date to) throws TelusAPIException, HistorySearchException {
		return delegate.getResourceChangeHistory(type, from, to);
	}
	
	public void applyCredit(Card card) throws TelusAPIException {
		delegate.applyCredit(card);
	}
	
	public Card[] getCards() throws TelusAPIException {
		return delegate.getCards();
	}
	
	public Card[] getCards(String cardType) throws TelusAPIException {
		return delegate.getCards(cardType);
	}
	
	public PricePlanSummary[] getAvailablePricePlans() throws TelusAPIException {
		return delegate.getAvailablePricePlans();
	}
	
	public PricePlanSummary[] getAvailablePricePlans(String equipmentType) throws TelusAPIException {
		return delegate.getAvailablePricePlans(equipmentType);
	}
	
	public PricePlanSummary[] getAvailablePricePlans(boolean getAll, String equipmentType) throws TelusAPIException {
		return delegate.getAvailablePricePlans(getAll, equipmentType);
	}
	
	public PricePlanSummary[] getAvailablePricePlans(boolean telephonyEnabled, boolean dispatchEnabled, boolean webEnabled, int term, boolean isCurrentOnly, boolean isActivationOnly,
			String equipmentType) throws TelusAPIException {
		return delegate.getAvailablePricePlans(telephonyEnabled, dispatchEnabled, webEnabled, term, isCurrentOnly, isActivationOnly, equipmentType);
	}
	
	public DepositHistory[] getDepositHistory() throws TelusAPIException, HistorySearchException {
		return delegate.getDepositHistory();
	}
	
	@SuppressWarnings("deprecation")
	public PricePlanSummary[] getAvailablePricePlans(boolean telephonyEnabled, boolean dispatchEnabled, boolean webEnabled, boolean clientActivation, boolean dealerActivation)
			throws TelusAPIException {
		return delegate.getAvailablePricePlans(telephonyEnabled, dispatchEnabled, webEnabled, clientActivation, dealerActivation);
	}
	
	public PricePlan getAvailablePricePlan(String pricePlanCode) throws TelusAPIException {
		return delegate.getAvailablePricePlan(pricePlanCode);
	}
	
	public PricePlan getAvailablePricePlan(ServiceSummary pricePlan) throws TelusAPIException {
		return delegate.getAvailablePricePlan(pricePlan);
	}
	
	public PricePlanSummary[] getAvailablePricePlans(boolean getAll) throws TelusAPIException {
		return delegate.getAvailablePricePlans(getAll);
	}
	
	public ActivationCredit findAvailableActivationCredit(String creditType) throws TelusAPIException {
		return delegate.findAvailableActivationCredit(creditType);
	}
	
	public ActivationCredit[] findAvailableActivationCredits() throws TelusAPIException {
		return delegate.findAvailableActivationCredits();
	}
	
	public VoiceUsageSummary getVoiceUsageSummary() throws VoiceUsageSummaryException, TelusAPIException {
		return delegate.getVoiceUsageSummary();
	}
	
	public VoiceUsageSummary getVoiceUsageSummary(String featureCode) throws VoiceUsageSummaryException, TelusAPIException {
		return delegate.getVoiceUsageSummary(featureCode);
	}
	
	public WebUsageSummary getWebUsageSummary() throws TelusAPIException {
		return delegate.getWebUsageSummary();
	}
	
	public boolean isPager() {
		return delegate.isPager();
	}
	
	public boolean isTango() {
		return delegate.isTango();
	}
	
	public boolean isCDPD() {
		return delegate.isCDPD();
	}
	
	public SubscriptionRole getSubscriptionRole() throws TelusAPIException {
		return delegate.getSubscriptionRole();
	}
	
	public void setSubscriptionRole(SubscriptionRole subscriptionRole) throws TelusAPIException {
		delegate.setSubscriptionRole(subscriptionRole);
	}

	public SubscriptionRole newSubscriptionRole() throws TelusAPIException {
		return delegate.newSubscriptionRole();
	}
	
	public double getTerminationFee() throws TelusAPIException {
		return delegate.getTerminationFee();
	}
	
	public void cancel(String reason, char depositReturnMethod) throws TelusAPIException {
		delegate.cancel(reason, depositReturnMethod);
	}
	
	public void cancel(String reason, char depositReturnMethod, String waiverReason) throws TelusAPIException {
		delegate.cancel(reason, depositReturnMethod, waiverReason);
	}
	
	public void cancel(Date activityDate, String reason, char depositReturnMethod, String waiverReason, String memoText) throws TelusAPIException {
		delegate.cancel(activityDate, reason, depositReturnMethod, waiverReason, memoText);
	}
	
	public void suspend(String reason) throws TelusAPIException {
		delegate.suspend(reason);
	}
	
	public void suspend(Date activityDate, String reason, String memoText) throws TelusAPIException {
		delegate.suspend(activityDate, reason, memoText);
	}
	
	public void restore(String reason) throws TelusAPIException {
		delegate.restore(reason);
	}

	public void restore(Date activityDate, String reason, String memoText) throws TelusAPIException {
		delegate.restore(activityDate, reason, memoText);
	}

	public NumberGroup getNumberGroup() throws TelusAPIException {
		return delegate.getNumberGroup();
	}

	public ReasonType[] getAvailableCancellationReasons() throws TelusAPIException {
		return delegate.getAvailableCancellationReasons();
	}

	public ReasonType[] getAvailableSuspensionReasons() throws TelusAPIException {
		return delegate.getAvailableSuspensionReasons();
	}

	public ReasonType[] getAvailableResumptionReasons() throws TelusAPIException {
		return delegate.getAvailableResumptionReasons();
	}
	
	public char getAccountStatusChangeAfterSuspend() throws TelusAPIException {
		return delegate.getAccountStatusChangeAfterSuspend();
	}
	
	public boolean isStarterSeat() throws TelusAPIException {
		if (getSeatData()!=null && getSeatData().getSeatType().equalsIgnoreCase(SeatType.SEAT_TYPE_STARTER)) {
			return true;
		}
		return false;
	}

	public char getAccountStatusChangeAfterCancel(CommunicationSuiteInfo commSuiteInfo) throws Exception {
		
		int activeCompanionSubscribersCountNowCancel = 0;
		int suspendCompanionSubscribersCountNowCancel = 0;

		// Begin comm suite Logic to check the active and suspend companion subscribers count.
		if(commSuiteInfo!=null && commSuiteInfo.isRetrievedAsPrimary()==true && commSuiteInfo.getActiveAndSuspendedCompanionPhoneNumberList().isEmpty()==false ){
			activeCompanionSubscribersCountNowCancel += commSuiteInfo.getActiveCompanionCount();
			suspendCompanionSubscribersCountNowCancel += commSuiteInfo.getSuspendedCompanionCount();
		}
		// End comm suite logic
		
		if (getStatus() == Subscriber.STATUS_CANCELED) {
			return Subscriber.ACCOUNT_STATUS_NO_CHANGE;
		}

		/**
		 * Ported from TMSubscriber:
		 * 
		 * The rules are:
		 *
		 * If there is at least one active subscriber, then the account status
		 * should remain the same (the account must already be opened and should
		 * remain opened). If there is at least one suspended subscriber and
		 * none active, the account should be suspended. (or remain the same if
		 * it is already suspended). If there are only canceled subscribers then
		 * the account should be canceled. Reserved subscribers are ignored.
		 */

		/**[Naresh Annabathula] , for business connect subscriber we can't depend on active/suspend subscriber count , all the seats under group will be canceled if the starter seat passed to cancel .
		 * so, using below getBusinessConnectAccountStausAfterCancelStarterSeat to determine the account status. 
		 */
		
		if (getAccount().isPostpaidBusinessConnect() && isStarterSeat()) {
			char status = getBusinessConnectAccountStausAfterCancelStarterSeat(getAccount().getProductSubscriberLists());
			return status;
		}

		int activeCount = getAccount().getAllActiveSubscribersCount();
		if (getStatus() == Subscriber.STATUS_ACTIVE) {
			activeCount--;

		}
		int suspendedCount = getAccount().getAllSuspendedSubscribersCount();
		if (getStatus() == Subscriber.STATUS_SUSPENDED) {
			suspendedCount--;

		}
		
		// Begin comm suite logic , check the companion subscribers count on account
		activeCount = activeCount-activeCompanionSubscribersCountNowCancel;
		suspendedCount = suspendedCount-suspendCompanionSubscribersCountNowCancel;
		//End comm suite logic
		
		if (activeCount > 0) {
			// if there is one active subscriber, then the account must be (and
			// remain) in opened status
			return Subscriber.ACCOUNT_STATUS_NO_CHANGE;
		}
		else if (suspendedCount > 0) {
			if (getAccount().getStatus() == AccountSummary.STATUS_SUSPENDED) {
				return Subscriber.ACCOUNT_STATUS_NO_CHANGE;
			}
			else {
				return AccountSummary.STATUS_SUSPENDED;
			}
		}
		else {
			// both active subscriber count and suspended subscriber count will be 0.
			return AccountSummary.STATUS_CANCELED;
		}
	}
	
	private char getBusinessConnectAccountStausAfterCancelStarterSeat(ProductSubscriberList[] productSubscriberList) throws Exception {	
		
		determineisLastActiveOrLastSupendStarterSeat(productSubscriberList);
		if (lastActiveStarterSeatInd == false) {
			// if there is one active starter subscriber, then the account must be (and remain) in opened status
			return Subscriber.ACCOUNT_STATUS_NO_CHANGE;
		}
		else if (lastSuspendedStarterSeatInd == false ) {
			if (getAccount().getStatus() == AccountSummary.STATUS_SUSPENDED) {
				return Subscriber.ACCOUNT_STATUS_NO_CHANGE;
			}
			else {
				return AccountSummary.STATUS_SUSPENDED;
			}
		}
		else {
			// both active starer seat subscriber count and starter suspended subscriber count will be 0.
			return AccountSummary.STATUS_CANCELED;
		}
	}
	
	private void determineisLastActiveOrLastSupendStarterSeat(ProductSubscriberList[] productSubscriberList)  {

		for (int i = 0; i < productSubscriberList.length; i++) {
			SubscriberIdentifier[] activeSubscriberIdentifiers = productSubscriberList[i].getActiveSubscriberIdentifiers();
			for (int j = 0; j < activeSubscriberIdentifiers.length; j++) {
				if (activeSubscriberIdentifiers[j].getSeatType().equals(SeatType.SEAT_TYPE_STARTER)) {
					activeStarterseatCount++;
				}
			}

			SubscriberIdentifier[] suspendedsubscriberIdentifiers = productSubscriberList[i].getSuspendedSubscriberIdentifiers();
			for (int k = 0; k < suspendedsubscriberIdentifiers.length; k++) {
				if (suspendedsubscriberIdentifiers[k].getSeatType().equals(SeatType.SEAT_TYPE_STARTER)) {
					suspendedStarterSeatCount++;
				}

			}
		}

		if (this.getStatus() == Subscriber.STATUS_ACTIVE) {
			activeStarterseatCount--;
		} else if (this.getStatus() == Subscriber.STATUS_SUSPENDED) {
			suspendedStarterSeatCount--;
		}

		if (activeStarterseatCount == 0) {
			this.lastActiveStarterSeatInd = true;
		}
		if (suspendedStarterSeatCount == 0)
			this.lastSuspendedStarterSeatInd = true;

	}

	public TaxExemption getTaxExemption() throws TelusAPIException {
		return delegate.getTaxExemption();
	}

	public Memo getLastMemo(String memoType) throws TelusAPIException {
		return delegate.getLastMemo(memoType);
	}
	
	public SubscriberHistory[] getHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		return delegate.getHistory(from, to);
	}
	
	public void move(Account account, boolean transferOwnership, String reasonCode, String memoText) throws TelusAPIException {
		delegate.move(account, transferOwnership, reasonCode, memoText);
	}

	public void move(Account account, boolean transferOwnership, String reasonCode, String memoText, String dealerCode, String salesRepCode) throws TelusAPIException {
		delegate.move(account, transferOwnership, reasonCode, memoText, dealerCode, salesRepCode);
	}

	public PricePlanSummary[] getSuspensionPricePlans(String reasonCode) throws TelusAPIException {
		return delegate.getSuspensionPricePlans(reasonCode);
	}

	public Date getStatusDate() {
		return delegate.getStatusDate();
	}

	public void sendFax(int form, String faxNumber, String language) throws EquipmentWarrantyNotAvailableException, TelusAPIException {
		delegate.sendFax(form, faxNumber, language);
	}

	public void sendEmail(int form, String email, String language) throws EquipmentWarrantyNotAvailableException, TelusAPIException {
		delegate.sendEmail(form, email, language);
	}
	
	public void refreshSwitch() throws TelusAPIException {
		delegate.refreshSwitch();
	}

	public String getProvisioningStatus() throws TelusAPIException {
		return delegate.getProvisioningStatus();
	}
	
	public CancellationPenalty getCancellationPenalty() throws TelusAPIException {
		return delegate.getCancellationPenalty();
	}

	public String getSupportLevel() throws TelusAPIException {
		return delegate.getSupportLevel();
	}

	public String getSLALevel() throws TelusAPIException {
		return delegate.getSLALevel();
	}
	
	public InvoiceCallSortOrderType getInvoiceCallSortOrder() throws TelusAPIException {
		return delegate.getInvoiceCallSortOrder();
	}
	
	public void setInvoiceCallSortOrder(String invoiceCallSortOrder) throws TelusAPIException {
		delegate.setInvoiceCallSortOrder(invoiceCallSortOrder);
	}
	
	public void createDeposit(double Amount, String memoText) throws TelusAPIException {
		delegate.createDeposit(Amount, memoText);
	}

//	public LMSLetterRequest newLMSLetterRequest(Letter letter) throws TelusAPIException {
//		return delegate.newLMSLetterRequest(letter);
//	}
	
	public DiscountPlan[] getAvailableDiscountPlans() throws TelusAPIException {
		return delegate.getAvailableDiscountPlans();
	}
	
	public Credit[] getCredits(Date from, Date to, String billState) throws TelusAPIException {
		return delegate.getCredits(from, to, billState);
	}
	
	public Credit[] getCreditsByReasonCode(Date from, Date to, String billState, String reasonCode) throws TelusAPIException {
		return delegate.getCreditsByReasonCode(from, to, billState, reasonCode);
	}

	public Address getAddress() throws TelusAPIException, AddressNotFoundException {
		return delegate.getAddress();
	}

	public Address getAddress(boolean refresh) throws TelusAPIException, AddressNotFoundException {
		return delegate.getAddress(refresh);
	}
	
	public void setAddress(Address newAddress) throws TelusAPIException {
		delegate.setAddress(newAddress);
	}
	
	public void removeFutureDatedPricePlanChange() throws TelusAPIException {
		delegate.removeFutureDatedPricePlanChange();
	}

	@SuppressWarnings("deprecation")
	public EquipmentChangeHistory[] getEquipmentChangeHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		return delegate.getEquipmentChangeHistory(from, to);
	}
	
	public SubscriberCommitment getCommitment() {
		return delegate.getCommitment();
	}
	
	public InvoiceTax getInvoiceTax(int billSeqNo) throws TelusAPIException {
		return delegate.getInvoiceTax(billSeqNo);
	}
	
	public UsageProfileListsSummary getUsageProfileListsSummary(int billSeqNo) throws TelusAPIException {
		return delegate.getUsageProfileListsSummary(billSeqNo);
	}
	
	public boolean isHotlined() {
		return delegate.isHotlined();
	}
	
	@SuppressWarnings("deprecation")
	public boolean isValidMigrationForPhoneNumber(MigrationType migrationType) throws TelusAPIException {
		return delegate.isValidMigrationForPhoneNumber(migrationType);
	}
	
	public boolean isValidMigrationForPhoneNumber(MigrationType migrationType, String sourceNetowrkType, String targetNetworkType) throws TelusAPIException {
		return delegate.isValidMigrationForPhoneNumber(migrationType, sourceNetowrkType, targetNetworkType);
	}
	
	public Date getMigrationDate() {
		return delegate.getMigrationDate();
	}

	public MigrationType getMigrationType() {
		return delegate.getMigrationType();
	}

	public QueueThresholdEvent[] getQueueThresholdEvents(Date from, Date to) throws TelusAPIException {
		return delegate.getQueueThresholdEvents(from, to);
	}
	
	public long getSubscriptionId(){
		return delegate.getSubscriptionId();
	}
	
	public double getRequestedSecurityDeposit() {
		return delegate.getRequestedSecurityDeposit();
	}
	
	public void save(boolean activate, ActivationOption selectedOption) throws TelusAPIException {
		delegate.save(activate, selectedOption);
	}

	public void save(Date startServiceDate, ActivationOption selectedOption) throws TelusAPIException {
		delegate.save(startServiceDate, selectedOption);
	}
	
	public void move(Account account, boolean transferOwnership, String reasonCode, String memoText, ActivationOption selectedOption) throws TelusAPIException {
		delegate.move(account, transferOwnership, reasonCode, memoText, selectedOption);
	}
	
	public void move(Account account, boolean transferOwnership, String reasonCode, String memoText, String dealerCode, String salesRepCode, ActivationOption selectedOption) throws TelusAPIException {
		delegate.move(account, transferOwnership, reasonCode, memoText, dealerCode, salesRepCode, selectedOption);
	}
	
	public double getPaidSecurityDeposit() throws TelusAPIException {
		return delegate.getPaidSecurityDeposit();
	}
	
	public void setSerialNumber(String serialNumber) {
		delegate.setSerialNumber(serialNumber);
	}
	
	public void setSecondarySerialNumbers(String[] secondarySerialNumbers) throws TelusAPIException {
		delegate.setSecondarySerialNumbers(secondarySerialNumbers);
	}
	
	public Subscriber retrieveSubscriber(String phoneNumber) throws TelusAPIException {
		return delegate.retrieveSubscriber(phoneNumber);
	}
	
	public int getBrandId() {
		return delegate.getBrandId();
	}
	
	public CallingCirclePhoneList[] getCallingCirclePhoneNumberListHistory(Date from, Date to) throws TelusAPIException {
		return delegate.getCallingCirclePhoneNumberListHistory(from, to);
	}
	
	public FeatureParameterHistory[] getFeatureParameterHistory(String[] parameterNames, Date from, Date to) throws TelusAPIException {
		return delegate.getFeatureParameterHistory(parameterNames, from, to);
	}

	public FeatureParameterHistory[] getFeatureParameterChangeHistory(Date from, Date to) throws TelusAPIException {
		return delegate.getFeatureParameterChangeHistory(from, to);
	}

	public String[] getSubscriberInterBrandPortActivityReasonCodes() throws TelusAPIException {
		return delegate.getSubscriberInterBrandPortActivityReasonCodes();
	}
	
	public VendorServiceChangeHistory[] getVendorServiceChangeHistory(String SOC) throws TelusAPIException {
		return delegate.getVendorServiceChangeHistory(SOC);
	}
	
	public void changeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment,
			char allowDuplicateSerialNo, ServiceRequestHeader header) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		delegate.changeEquipment(newIDENEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, associatedMuleEquipment, allowDuplicateSerialNo, header);
	}
	
	public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, boolean changeOtherNumbers, String dealerCode, String salesRepCode, String reasonCode, ServiceRequestHeader header)
			throws TelusAPIException, PhoneNumberException, PhoneNumberInUseException {
		delegate.changePhoneNumber(availablePhoneNumber, changeOtherNumbers, dealerCode, salesRepCode, reasonCode, header);
	}
	
	public void cancel(Date activityDate, String reason, char depositReturnMethod, String waiverReason, String memoText, ServiceRequestHeader header) throws TelusAPIException {
		delegate.cancel(activityDate, reason, depositReturnMethod, waiverReason, memoText, header);
	}
	
	public void move(Account account, boolean transferOwnership, String reasonCode, String memoText, String dealerCode, String salesRepCode, ActivationOption selectedOption,
			ServiceRequestHeader header) throws TelusAPIException {
		delegate.move(account, transferOwnership, reasonCode, memoText, dealerCode, salesRepCode, selectedOption, header);
	}
	
	public void activate(String reason, Date startServiceDate, String memoText, ServiceRequestHeader header) throws TelusAPIException {
		delegate.activate(reason, startServiceDate, memoText, header);
	}
	
	public void restore(Date activityDate, String reason, String memoText, ServiceRequestHeader header) throws TelusAPIException {
		delegate.restore(activityDate, reason, memoText, header);
	}

	public SeatData getSeatData() {
		return delegate.getSeatData();
	}

	public boolean isSeatSubscriber() {
		return delegate.isSeatSubscriber();	
	}

	public abstract ActivationOptionBo getActivationOption() throws ApplicationException;

}