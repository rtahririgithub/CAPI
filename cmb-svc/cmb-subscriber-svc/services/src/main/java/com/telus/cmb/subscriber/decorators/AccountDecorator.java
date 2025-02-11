package com.telus.cmb.subscriber.decorators;

import java.util.Date;

import com.telus.api.HistorySearchException;
import com.telus.api.InvalidMultiSubscriberOperationException;
import com.telus.api.LimitExceededException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.Address;
import com.telus.api.account.AddressHistory;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.BillNotificationContact;
import com.telus.api.account.CancellationPenalty;
import com.telus.api.account.Charge;
import com.telus.api.account.Cheque;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.Credit;
import com.telus.api.account.CreditCard;
import com.telus.api.account.CreditCheckResult;
import com.telus.api.account.DepositAssessedHistory;
import com.telus.api.account.DepositHistory;
import com.telus.api.account.Discount;
import com.telus.api.account.FinancialHistory;
import com.telus.api.account.FollowUp;
import com.telus.api.account.FollowUpStatistics;
import com.telus.api.account.FutureStatusChangeRequest;
import com.telus.api.account.InternationalServiceEligibilityCheckResult;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.account.InvoiceHistory;
//import com.telus.api.account.LMSLetterRequest;
import com.telus.api.account.LightWeightSubscriber;
import com.telus.api.account.ManualCreditCheckRequest;
import com.telus.api.account.Memo;
import com.telus.api.account.PaymentFailedException;
import com.telus.api.account.PaymentHistory;
import com.telus.api.account.PaymentMethodChangeHistory;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ProductSubscriberList;
import com.telus.api.account.ProrationMinutes;
import com.telus.api.account.RefundHistory;
import com.telus.api.account.RewardRedemptionResult;
import com.telus.api.account.SearchResults;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.account.StatusChangeHistory;
import com.telus.api.account.Subscriber;
import com.telus.api.account.TaxExemption;
import com.telus.api.account.UnknownBANException;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.api.portability.PRMSystemException;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestException;
import com.telus.api.reference.ClientConsentIndicator;
//import com.telus.api.reference.Letter;
import com.telus.api.reference.SubscriptionRoleType;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.eas.account.info.AccountInfo;

public abstract class AccountDecorator implements Account {
	protected final AccountInfo delegate;
	
	protected AccountDecorator (AccountInfo accountInfo) {
		delegate = accountInfo;
	}

	public AccountInfo getDelegate() {
		return delegate;
	}
	
	@Override
	public boolean isSuspendedDueToNonPayment() {
		return delegate.isSuspendedDueToNonPayment();
	}

	@Override
	public int getBanId() {
		return delegate.getBanId();
	}

	@Override
	public int getCustomerId() {
		return delegate.getCustomerId();
	}

	@Override
	public String getDealerCode() {
		return delegate.getDealerCode();
	}

	@Override
	public String getSalesRepCode() {
		return delegate.getSalesRepCode();
	}

	@Override
	public void setSalesRepCode(String salesRepCode) {
		delegate.setSalesRepCode(salesRepCode);
	}

	@Override
	public void setDealerCode(String dealerCode) {
		delegate.setDealerCode(dealerCode);
	}

	@Override
	public char getStatus() {
		return delegate.getStatus();
	}

	@Override
	public Date getStatusDate() {
		return delegate.getStatusDate();
	}

	@Override
	public String getFullName() {
		return delegate.getFullName();
	}

	@Override
	public String[] getFullAddress() {
		return delegate.getFullAddress();
	}

	@Override
	public boolean isAutotel() {
		return delegate.isAutotel();
	}

	@Override
	public boolean isBrand(int brandId) {
		return delegate.isBrand(brandId);
	}

	@Override
	public boolean isIDEN() {
		return delegate.isIDEN();
	}

	@Override
	public boolean isPCS() {
		return delegate.isPCS();
	}

	@Override
	public boolean isPager() {
		return delegate.isPager();
	}

	@Override
	public boolean isPostpaidEmployee() {
		return delegate.isPostpaidEmployee();
	}

	@Override
	public boolean isPostpaidConsumer() {
		return delegate.isPostpaidConsumer();
	}

	@Override
	public boolean isPostpaidBoxedConsumer() {
		return delegate.isPostpaidBoxedConsumer();
	}

	@Override
	public boolean isPostpaidBusinessRegular() {
		return delegate.isPostpaidBusinessRegular();
	}

	@Override
	public boolean isPostpaidBusinessPersonal() {
		return delegate.isPostpaidBusinessPersonal();
	}

	@Override
	public boolean isPostpaidBusinessDealer() {
		return delegate.isPostpaidBusinessDealer();
	}

	@Override
	public boolean isPostpaidBusinessOfficial() {
		return delegate.isPostpaidBusinessOfficial();
	}

	@Override
	public boolean isPostpaidOfficial() {
		return delegate.isPostpaidOfficial();
	}

	@Override
	public boolean isPrepaidConsumer() {
		return delegate.isPrepaidConsumer();
	}

	@Override
	public boolean isQuebectelPrepaidConsumer() {
		return delegate.isQuebectelPrepaidConsumer();
	}

	@Override
	public boolean isWesternPrepaidConsumer() {
		return delegate.isWesternPrepaidConsumer();
	}

	@Override
	public boolean isCorporateRegular() {
		return delegate.isCorporateRegular();
	}

	@Override
	public boolean isCorporateRegional() {
		return delegate.isCorporateRegional();
	}

	@Override
	public boolean isCorporatePrivateNetworkPlus() {
		return delegate.isCorporatePrivateNetworkPlus();
	}
	
	@Override
	public TaxExemption getTaxExemption() throws TelusAPIException {
		return delegate.getTaxExemption();
	}

	@Override
	public int getBrandId() {
		return delegate.getBrandId();
	}

	@Override
	public boolean isAmpd() {
		return delegate.isAmpd();
	}

	@Override
	public boolean isPostpaid() {
		return delegate.isPostpaid();
	}

	@Override
	public char getAccountType() {
		return delegate.getAccountType();
	}

	@Override
	public char getAccountSubType() {
		return delegate.getAccountSubType();
	}

	@Override
	public String getPin() {
		return delegate.getPin();
	}

	@Override
	public Date getCreateDate() {
		return delegate.getCreateDate();
	}

	@Override
	public Date getStartServiceDate() {
		return delegate.getStartServiceDate();
	}

	@Override
	public String getStatusActivityCode() {
		return delegate.getStatusActivityCode();
	}

	@Override
	public String getStatusActivityReasonCode() {
		return delegate.getStatusActivityReasonCode();
	}

	@Override
	public Account getAccount() throws TelusAPIException {
		return delegate.getAccount();
	}

	@Override
	public Subscriber[] getSubscribers(int maximum) throws TelusAPIException {
		return delegate.getSubscribers(maximum);
	}

	@Override
	public Subscriber[] getPortedSubscribers(int maximum) throws TelusAPIException {
		return delegate.getPortedSubscribers(maximum);
	}

	@Override
	public Subscriber[] getSubscribers(int maximum, boolean includeCancelled) throws TelusAPIException {
		return delegate.getSubscribers(maximum, includeCancelled);
	}

	@Override
	public String[] getActiveSubscriberPhoneNumbers(int maxNumbers) throws TelusAPIException {
		return delegate.getActiveSubscriberPhoneNumbers(maxNumbers);
	}

	@Override
	public String[] getSubscriberPhoneNumbersByStatus(char status, int maximum) throws TelusAPIException {
		return delegate.getSubscriberPhoneNumbersByStatus(status, maximum);
	}

	@Override
	public String[] getSuspendedSubscriberPhoneNumbers(int maxNumbers) throws TelusAPIException {
		return delegate.getSuspendedSubscriberPhoneNumbers(maxNumbers);
	}

	@Override
	public void savePin(String newPin) throws UnknownBANException, TelusAPIException {
		delegate.savePin(newPin);
	}

	@Override
	public Memo getLastCreditCheckMemo() throws TelusAPIException {
		return delegate.getLastCreditCheckMemo();
	}

	@Override
	public Memo newMemo() throws TelusAPIException {
		return delegate.newMemo();
	}

	@Override
	public FollowUp newFollowUp() throws TelusAPIException {
		return delegate.newFollowUp();
	}

	@Override
	public Charge newCharge() throws TelusAPIException {
		return delegate.newCharge();
	}

	@Override
	public Credit newCredit() throws TelusAPIException {
		return delegate.newCredit();
	}

	@Override
	public Credit newCredit(boolean taxable) throws TelusAPIException {
		return delegate.newCredit(taxable);
	}

	@Override
	public Credit newCredit(char taxOption) throws TelusAPIException {
		return delegate.newCredit(taxOption);
	}

	@Override
	public Discount newDiscount() throws TelusAPIException {
		return delegate.newDiscount();
	}

	@Override
	public Memo getLastMemo(String memoType) throws TelusAPIException {
		return delegate.getLastMemo(memoType);
	}

	@Override
	public boolean isGSTExempt() {
		return delegate.isGSTExempt();
	}

	@Override
	public boolean isPSTExempt() {
		return delegate.isPSTExempt();
	}

	@Override
	public boolean isHSTExempt() {
		return delegate.isHSTExempt();
	}

	@Override
	public boolean isCorporateHierarchy() {
		return delegate.isCorporateHierarchy();
	}

	@Override
	public boolean isPagerPrepaidConsumeraccount() {
		return delegate.isPagerPrepaidConsumeraccount();
	}

	@Override
	public boolean isPCSPostpaidCorporateRegularAccount() {
		return delegate.isPCSPostpaidCorporateRegularAccount();
	}

	@Override
	public String getCorporateAccountRepCode() {
		return delegate.getCorporateAccountRepCode();
	}

	@Override
	public Memo[] getMemos(int count) throws TelusAPIException {
		return delegate.getMemos(count);
	}

	@Override
	public FollowUp[] getFollowUps(int count) throws TelusAPIException {
		return delegate.getFollowUps(count);
	}

	@Override
	public Subscriber getSubscriber(String subscriberId) throws UnknownSubscriberException, TelusAPIException {
		return delegate.getSubscriber(subscriberId);
	}

	@Override
	public Subscriber getSubscriberByPhoneNumber(String phoneNumber) throws UnknownSubscriberException, TelusAPIException {
		return delegate.getSubscriberByPhoneNumber(phoneNumber);
	}

	@Override
	public Memo getSpecialInstructionsMemo() throws TelusAPIException {
		return delegate.getSpecialInstructionsMemo();
	}

	@Override
	public ConsumerName[] getAuthorizedNames() throws TelusAPIException {
		return delegate.getAuthorizedNames();
	}

	@Override
	public void saveAuthorizedNames(ConsumerName[] authorizedNames) throws UnknownBANException, TelusAPIException {
		delegate.saveAuthorizedNames(authorizedNames);
	}

	@Override
	@Deprecated
	public boolean isFidoConversion() {
		return delegate.isFidoConversion();
	}

	@Override
	public SubscriptionRoleType[] getValidSubscriptionRoleTypes() throws TelusAPIException {
		return delegate.getValidSubscriptionRoleTypes();
	}

//	@Override
//	public LMSLetterRequest newLMSLetterRequest(Letter letter) throws TelusAPIException {
//		return delegate.newLMSLetterRequest(letter);
//	}

	@Override
	public String getHotlinedSubscriberPhoneNumber() throws TelusAPIException {
		return delegate.getHotlinedSubscriberPhoneNumber();
	}

	@Override
	public String getCorporateName() throws TelusAPIException {
		return delegate.getCorporateName();
	}

	@Override
	public boolean isPostpaidCorporatePersonal() {
		return delegate.isPostpaidCorporatePersonal();
	}

	@Override
	public boolean isPostpaidCorporateRegular() {
		return delegate.isPostpaidCorporateRegular();
	}

	@Override
	public String getPortProtectionIndicator() throws TelusAPIException {
		return delegate.getPortProtectionIndicator();
	}

	@Override
	public void updatePortRestriction(boolean restrictPort) throws TelusAPIException {
		delegate.updatePortRestriction(restrictPort);
	}

	@Override
	public String getBanSegment() throws TelusAPIException {
		return delegate.getBanSegment();
	}

	@Override
	public String getBanSubSegment() throws TelusAPIException {
		return delegate.getBanSubSegment();
	}

	@Override
	public BillNotificationContact[] getBillNotificationContacts() throws TelusAPIException {
		return delegate.getBillNotificationContacts();
	}

	@Override
	public BillNotificationContact getLastEBillNotificationSent() throws TelusAPIException {
		return delegate.getLastEBillNotificationSent();
	}

	@Override
	public String getPaperBillSupressionAtActivationInd() throws TelusAPIException {
		return delegate.getPaperBillSupressionAtActivationInd();
	}

	@Override
	public LightWeightSubscriber[] getLightWeightSubscribers(int maximum, boolean includeCancelled) throws TelusAPIException {
		return delegate.getLightWeightSubscribers(maximum, includeCancelled);
	}

	@Override
	public int getHierarchyId() {
		return delegate.getHierarchyId();
	}

	@Override
	public boolean isBelongToHierarchy() {
		return delegate.isBelongToHierarchy();
	}

	@Override
	public int getNoOfInvoice() throws TelusAPIException {
		return delegate.getNoOfInvoice();
	}

	@Override
	public void setNoOfInvoice(int noOfInvoice) {
		delegate.setNoOfInvoice(noOfInvoice);
	}

	@Override
	public InternationalServiceEligibilityCheckResult checkInternationalServiceEligibility() throws TelusAPIException {
		return delegate.checkInternationalServiceEligibility();
	}

	@Override
	public Address getAddress() {
		return delegate.getAddress();
	}

	@Override
	public String getAdditionalLine() {
		return delegate.getAdditionalLine();
	}

	@Override
	public void setAdditionalLine(String additionalLine) {
		delegate.setAdditionalLine(additionalLine);
	}

	@Override
	public String getEmail() {
		return delegate.getEmail();
	}

	@Override
	public void setEmail(String email) {
		delegate.setEmail(email);
	}

	@Override
	public void setPin(String pin) {
		delegate.setPin(pin);
	}

	@Override
	public String getLanguage() {
		return delegate.getLanguage();
	}

	@Override
	public void setLanguage(String language) {
		delegate.setLanguage(language);
	}

	@Override
	public int getBillCycle() {
		return delegate.getBillCycle();
	}

	@Override
	public int getBillCycleCloseDay() {
		return delegate.getBillCycleCloseDay();
	}

	@Override
	public String getHomeProvince() {
		return delegate.getHomeProvince();
	}

	@Override
	public void setHomeProvince(String homeProvince) {
		delegate.setHomeProvince(homeProvince);
	}

	@Override
	public String getAccountCategory() {
		return delegate.getAccountCategory();
	}

	@Override
	public CreditCheckResult checkNewSubscriberEligibility(int subscriberCount, double thresholdAmount) throws TelusAPIException {
		return delegate.checkNewSubscriberEligibility(subscriberCount, thresholdAmount);
	}

	@Override
	public RewardRedemptionResult checkRewardRedemptionEligibility() throws TelusAPIException {
		return delegate.checkRewardRedemptionEligibility();
	}

	@Override
	public void setAccountCategory(String accountCategory) {
		delegate.setAccountCategory(accountCategory);
	}

	@Override
	public int getNextBillCycle() {
		return delegate.getNextBillCycle();
	}

	@Override
	public int getNextBillCycleCloseDay() {
		return delegate.getNextBillCycleCloseDay();
	}

	@Override
	public Date getVerifiedDate() {
		return delegate.getVerifiedDate();
	}

	@Override
	public void setVerifiedDate(Date date) {
		delegate.setVerifiedDate(date);
	}

	@Override
	public boolean isHandledBySubscriberOnly() {
		return delegate.isHandledBySubscriberOnly();
	}

	@Override
	public String getHomePhone() {
		return delegate.getHomePhone();
	}

	@Override
	public void setHomePhone(String homePhone) {
		delegate.setHomePhone(homePhone);
	}

	@Override
	public String getBusinessPhone() {
		return delegate.getBusinessPhone();
	}

	@Override
	public void setBusinessPhone(String businessPhone) {
		delegate.setBusinessPhone(businessPhone);
	}

	@Override
	public String getBusinessPhoneExtension() {
		return delegate.getBusinessPhoneExtension();
	}

	@Override
	public void setBusinessPhoneExtension(String businessPhoneExtension) {
		delegate.setBusinessPhoneExtension(businessPhoneExtension);
	}

	@Override
	public FinancialHistory getFinancialHistory() {
		return delegate.getFinancialHistory();
	}

	@Override
	public CreditCheckResult getCreditCheckResult() {
		return delegate.getCreditCheckResult();
	}

	@Override
	public int getActiveSubscribersCount() {
		return delegate.getActiveSubscribersCount();
	}

	@Override
	public int getSuspendedSubscribersCount() {
		return delegate.getSuspendedSubscribersCount();
	}

	@Override
	public int getReservedSubscribersCount() {
		return delegate.getReservedSubscribersCount();
	}

	@Override
	public int getCancelledSubscribersCount() {
		return delegate.getCancelledSubscribersCount();
	}

	@Override
	public int getSubscriberCount() {
		return delegate.getSubscriberCount();
	}

	@Override
	public int getAllActiveSubscribersCount() {
		return delegate.getAllActiveSubscribersCount();
	}

	@Override
	public int getAllSuspendedSubscribersCount() {
		return delegate.getAllSuspendedSubscribersCount();
	}

	@Override
	public int getAllReservedSubscribersCount() {
		return delegate.getAllReservedSubscribersCount();
	}

	@Override
	public int getAllCancelledSubscribersCount() {
		return delegate.getAllCancelledSubscribersCount();
	}

	@Override
	public int getAllSubscriberCount() {
		return delegate.getAllSubscriberCount();
	}

	@Override
	public String getSpecialInstructions() {
		return delegate.getSpecialInstructions();
	}

	@Override
	public boolean isHotlined() {
		return delegate.isHotlined();
	}

	@Override
	public void setHotlined(boolean hotlined) {
		delegate.setHotlined(hotlined);
	}

	@Override
	public Date getLastChangesDate() {
		return delegate.getLastChangesDate();
	}

	@Override
	public AccountSummary[] getDuplicateAccounts() throws TelusAPIException {
		return delegate.getDuplicateAccounts();
	}

	@Override
	public AccountSummary[] getDuplicateAccounts(String duplicateSearchLevel) throws TelusAPIException {
		return delegate.getDuplicateAccounts(duplicateSearchLevel);
	}

	@Override
	public int[] getDuplicateAccountBANs() throws TelusAPIException {
		return delegate.getDuplicateAccountBANs();
	}

	@Override
	public int[] getDuplicateAccountBANs(String duplicateSearchLevel) throws TelusAPIException {
		return delegate.getDuplicateAccountBANs(duplicateSearchLevel);
	}

	@Override
	public void save() throws TelusAPIException, InvalidCreditCardException {
		delegate.save();
	}

	@Override
	public void save(boolean saveCreditCheckInfo) throws TelusAPIException, InvalidCreditCardException {
		delegate.save(saveCreditCheckInfo);
	}

	@Override
	public void refresh() throws TelusAPIException {
		delegate.refresh();
	}

	@Override
	public void refreshCreditCheckResult() throws TelusAPIException {
		delegate.refreshCreditCheckResult();
	}

	@Override
	public String payDeposit(int subscriberCount, double amount, CreditCard creditCard, String businessRole, AuditHeader auditHeader) throws TelusAPIException, PaymentFailedException,
			InvalidCreditCardException {
		return delegate.payDeposit(subscriberCount, amount, creditCard, businessRole, auditHeader);
	}

	@Override
	public String payDeposit(int subscriberCount, double amount, CreditCard creditCard, String sourceID, String sourceType, String businessRole, AuditHeader auditHeader) throws TelusAPIException,
			PaymentFailedException, UnknownObjectException, InvalidCreditCardException {
		return delegate.payDeposit(subscriberCount, amount, creditCard, sourceID, sourceType, businessRole, auditHeader);
	}

	@Override
	public String payBill(double amount, CreditCard creditCard, String businessRole, AuditHeader auditHeader) throws TelusAPIException, PaymentFailedException, InvalidCreditCardException {
		return delegate.payBill(amount, creditCard, businessRole, auditHeader);
	}

	@Override
	public String payBill(double amount, CreditCard creditCard, String sourceID, String sourceType, String businessRole, AuditHeader auditHeader) throws TelusAPIException, PaymentFailedException,
			UnknownObjectException, InvalidCreditCardException {
		return delegate.payBill(amount, creditCard, sourceID, sourceType, businessRole, auditHeader);
	}

	@Override
	public String getPhoneNumberBySubscriberID(String pSubscriberId) throws TelusAPIException {
		return delegate.getPhoneNumberBySubscriberID(pSubscriberId);
	}

	@Override
	public InvoiceHistory[] getInvoiceHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		return delegate.getInvoiceHistory(from, to);
	}

	@Override
	public PaymentHistory[] getPaymentHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		return delegate.getPaymentHistory(from, to);
	}

	@Override
	public PaymentHistory getLastPaymentActivity() throws TelusAPIException, HistorySearchException {
		return delegate.getLastPaymentActivity();
	}

	@Override
	public PaymentMethodChangeHistory[] getPaymentMethodChangeHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		return delegate.getPaymentMethodChangeHistory(from, to);
	}

	@Override
	public StatusChangeHistory[] getStatusChangeHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		return delegate.getStatusChangeHistory(from, to);
	}

	@Override
	public AddressHistory[] getAddressChangeHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		return delegate.getAddressChangeHistory(from, to);
	}

	@Override
	public ProrationMinutes[] getProrationMinutes(int months, int totalMinutes) throws TelusAPIException {
		return delegate.getProrationMinutes(months, totalMinutes);
	}

	@Override
	public DepositHistory[] getDepositHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		return delegate.getDepositHistory(from, to);
	}

	@Override
	public RefundHistory[] getRefundHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		return delegate.getRefundHistory(from, to);
	}

	@Override
	public SearchResults getPendingChargeHistory(Date from, Date to, char level, String subscriberId, int maximum) throws TelusAPIException, HistorySearchException {
		return delegate.getPendingChargeHistory(from, to, level, subscriberId, maximum);
	}

	@Override
	public SearchResults getCredits(Date from, Date to, String billState, char level, String subscriberId, int maximum) throws TelusAPIException {
		return delegate.getCredits(from, to, billState, level, subscriberId, maximum);
	}

	@Override
	public SearchResults getCredits(Date from, Date to, String billState, char level, String subscriberId, String knowbilityOperatorId, int maximum) throws TelusAPIException {
		return delegate.getCredits(from, to, billState, level, subscriberId, knowbilityOperatorId, maximum);
	}

	@Override
	public String getCorporateId() {
		return delegate.getCorporateId();
	}

	@Override
	public void cancel(Date activityDate, String reason, char depositReturnMethod, String waiver, String memoText) throws TelusAPIException {
		delegate.cancel(activityDate, reason, depositReturnMethod, waiver, memoText);
	}
	
	@Override
	public void cancel(String reason, char depositReturnMethod, String waiver) throws TelusAPIException {
		delegate.cancel(reason, depositReturnMethod, waiver);
	}

	@Override
	public void cancel(String reason, char depositReturnMethod) throws TelusAPIException {
		delegate.cancel(reason, depositReturnMethod);
	}

	@Override
	public void suspend(Date activityDate, String reason, String memoText) throws TelusAPIException {
		delegate.suspend(activityDate, reason, memoText);
		
	}

	@Override
	public void suspend(String reason) throws TelusAPIException {
		delegate.suspend(reason);
		
	}

	@Override
	public Account createDuplicateAccount() throws TelusAPIException {
		return delegate.createDuplicateAccount();
	}

	@Override
	public String getOtherPhoneType() {
		return delegate.getOtherPhoneType();
	}

	@Override
	public void setOtherPhoneType(String otherPhoneType) {
		delegate.setOtherPhoneType(otherPhoneType);
	}

	@Override
	public String getOtherPhone() {
		return delegate.getOtherPhone();
	}

	@Override
	public void setOtherPhone(String otherPhone) {
		delegate.setOtherPhone(otherPhone);
	}

	@Override
	public String getOtherPhoneExtension() {
		return delegate.getOtherPhoneExtension();
	}

	@Override
	public void setOtherPhoneExtension(String otherPhoneExtension) {
		delegate.setOtherPhoneExtension(otherPhoneExtension);
	}

	@Override
	public String getContactPhone() {
		return delegate.getContactPhone();
	}

	@Override
	public void setContactPhone(String contactPhone) {
		delegate.setContactPhone(contactPhone);
	}

	@Override
	public String getContactPhoneExtension() {
		return delegate.getContactPhoneExtension();
	}

	@Override
	public void setContactPhoneExtension(String contactPhoneExtension) {
		delegate.setContactPhoneExtension(contactPhoneExtension);
	}

	@Override
	public ConsumerName getContactName() {
		return delegate.getContactName();
	}

	@Override
	public Charge[] getBilledCharges(int billSeqNo) throws TelusAPIException {
		return delegate.getBilledCharges(billSeqNo);
	}

	@Override
	public Charge[] getBilledCharges(int billSeqNo, String phoneNumber) throws TelusAPIException {
		return delegate.getBilledCharges(billSeqNo, phoneNumber);
	}

	@Override
	public Charge[] getBilledCharges(int billSeqNo, Date from, Date to) throws TelusAPIException {
		return delegate.getBilledCharges(billSeqNo, from, to);
	}

	@Override
	public Charge[] getBilledCharges(int billSeqNo, String phoneNumber, Date from, Date to) throws TelusAPIException {
		return delegate.getBilledCharges(billSeqNo, phoneNumber, from, to);
	}

	@Override
	public CancellationPenalty getCancellationPenalty() throws TelusAPIException {
		return delegate.getCancellationPenalty();
	}

	@Override
	public ClientConsentIndicator[] getClientConsentIndicators() throws TelusAPIException {
		return delegate.getClientConsentIndicators();
	}

	@Override
	public void setClientConsentIndicators(ClientConsentIndicator[] clientConsentIndicators) throws TelusAPIException {
		delegate.setClientConsentIndicators(clientConsentIndicators);
	}

	@Override
	public double getInternationalDialingDepositAmount() throws TelusAPIException {
		return delegate.getInternationalDialingDepositAmount();
	}

//	@Override
//	public SearchResults getLMSLetterRequests(Date from, Date to, char level, String subscriberId, int maximum) throws TelusAPIException {
//		return delegate.getLMSLetterRequests(from, to, level, subscriberId, maximum);
//	}

	@Override
	public FutureStatusChangeRequest[] getFutureStatusChangeRequests() throws TelusAPIException {
		return delegate.getFutureStatusChangeRequests();
	}

	@Override
	public SearchResults getCredits(Date from, Date to, String billState, String reasonCode, char level, String subscriberId, int maximum) throws TelusAPIException {
		return delegate.getCredits(from, to, billState, level, subscriberId, maximum);
	}

	@Override
	public FollowUpStatistics getFollowUpStatistics() throws TelusAPIException {
		return delegate.getFollowUpStatistics();
	}

	@Override
	public FollowUpStatistics getFollowUpStatistics(boolean refresh) throws TelusAPIException {
		return delegate.getFollowUpStatistics(refresh);
	}

	@Override
	public boolean validTargetAccountType(char accountType, char accountSubType) {
		return delegate.validTargetAccountType(accountType, accountSubType);
	}

	@Override
	public void sendEmail(int form, String email, String language) throws TelusAPIException {
		delegate.sendEmail(form, email, language);
	}

	@Override
	public void sendFax(int form, String faxNumber, String language) throws TelusAPIException {
		delegate.sendFax(form, faxNumber, language);
	}

	@Override
	public ServiceSubscriberCount[] getServiceSubscriberCounts(String[] serviceCodes, boolean includeExpired) throws TelusAPIException {
		return delegate.getServiceSubscriberCounts(serviceCodes, includeExpired);
	}

	@Override
	public ProductSubscriberList[] getProductSubscriberLists() {
		return delegate.getProductSubscriberLists();
	}

	@Override
	public Credit[] getCreditByFollowUpId(int followUpId) throws TelusAPIException {
		return delegate.getCreditByFollowUpId(followUpId);
	}

	@Override
	public CancellationPenalty[] getCancellationPenaltyList(String[] subscriberId) throws TelusAPIException, InvalidMultiSubscriberOperationException {
		return delegate.getCancellationPenaltyList(subscriberId);
	}

	@Override
	public void cancelSubscribers(Date effectiveDate, String reason, char depMethod, String[] subscriberId, String[] waiverReason, String comment) throws TelusAPIException,
			InvalidMultiSubscriberOperationException {
		delegate.cancelSubscribers(effectiveDate, reason, depMethod, subscriberId, waiverReason, comment);
	}

	@Override
	public void suspendSubscribers(Date effectiveDate, String reason, String[] subscriberId, String comment) throws TelusAPIException, InvalidMultiSubscriberOperationException {
		delegate.suspendSubscribers(effectiveDate, reason, subscriberId, comment);
	}

	@Override
	public void restoreSuspendedSubscribers(Date effectiveDate, String reason, String[] subscriberId, String comment) throws TelusAPIException, InvalidMultiSubscriberOperationException {
		delegate.restoreSuspendedSubscribers(effectiveDate, reason, subscriberId, comment);
	}

	@Override
	public PricePlanSubscriberCount[] getAirtimeMinutePoolingEnabledPricePlanSubscriberCounts() throws TelusAPIException {
		return delegate.getAirtimeMinutePoolingEnabledPricePlanSubscriberCounts();
	}

	@Override
	public PricePlanSubscriberCount[] getLDMinutePoolingEnabledPricePlanSubscriberCounts() throws TelusAPIException {
		return delegate.getLDMinutePoolingEnabledPricePlanSubscriberCounts();
	}

	@Override
	public DepositAssessedHistory[] getDepositAssessedHistory() throws TelusAPIException {
		return delegate.getDepositAssessedHistory();
	}

	@Override
	public DepositAssessedHistory[] getOriginalDepositAssessedHistory() throws TelusAPIException {
		return delegate.getOriginalDepositAssessedHistory();
	}

	@Override
	public ActivationOption[] getActivationOptions() throws TelusAPIException {
		return delegate.getActivationOptions();
	}

	@Override
	public PortRequest[] getPortRequests() throws PortRequestException, PRMSystemException, TelusAPIException {
		return delegate.getPortRequests();
	}

	@Override
	public void setBrandId(int brandId) {
		delegate.setBrandId(brandId);
	}

	@Override
	public void updateBrand(int brandId, String memoText) throws TelusAPIException {
		delegate.updateBrand(brandId, memoText);
	}

	@Override
	public String setBanSegment(String segment) {
		return delegate.setBanSegment(segment);
	}

	@Override
	public String setBanSubSegment(String subsegment) {
		return delegate.setBanSubSegment(subsegment);
	}

	@Override
	public void applyPayment(double amount, Cheque cheque, String sourceID, String sourceType) throws TelusAPIException, PaymentFailedException, UnknownObjectException {
		delegate.applyPayment(amount, cheque, sourceID, sourceType);
	}

	@Override
	public boolean isFeatureCategoryExistOnSubscribers(String categoryCode) throws TelusAPIException {
		return delegate.isFeatureCategoryExistOnSubscribers(categoryCode);
	}

	@Override
	public void save(ServiceRequestHeader header) throws TelusAPIException, InvalidCreditCardException {
		delegate.save(header);
	}

	@Override
	public void cancel(Date activityDate, String reason, char depositReturnMethod, String waiver, String memoText, ServiceRequestHeader header) throws TelusAPIException {
		delegate.cancel(activityDate, reason, depositReturnMethod, waiver, memoText, header);
	}

	@Override
	public void cancelSubscribers(Date effectiveDate, String reason, char depMethod, String[] subscriberIds, String[] waiverReason, String comment, ServiceRequestHeader header)
			throws TelusAPIException, InvalidMultiSubscriberOperationException {
		delegate.cancelSubscribers(effectiveDate, reason, depMethod, subscriberIds, waiverReason, comment, header);
	}

	@Override
	public ManualCreditCheckRequest getManualCreditCheckRequest() throws TelusAPIException {
		return delegate.getManualCreditCheckRequest();
	}


	@Override
	public Charge[] getCharges(String[] chargeCodes, String billState, char level, String subscriberId, Date from, Date to, int maximum) throws LimitExceededException, TelusAPIException {
		return delegate.getCharges(chargeCodes, billState, level, subscriberId, from, to, maximum);
	}
	
	@Override
	public String[] getSubscriberIdsByServiceGroupFamily(String familyType, Date effectiveDate)throws LimitExceededException, TelusAPIException {
		return delegate.getSubscriberIdsByServiceGroupFamily(familyType, effectiveDate);
	}
	@Override
	public boolean getTransientNotificationSuppressionInd(){
		
		return delegate.getTransientNotificationSuppressionInd();
	}
	@Override
	public void setTransientNotificationSuppressionInd(boolean transientNotificaitonSuppressionInd) {
		
		 delegate.setTransientNotificationSuppressionInd(transientNotificaitonSuppressionInd);
		
	}
	
	@Override
	public boolean isPostpaidBusinessConnect() {
		return delegate.isPostpaidBusinessConnect();
	}
}
