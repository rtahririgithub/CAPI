/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.DuplicateObjectException;
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
import com.telus.api.account.BillNotificationHistoryRecord;
import com.telus.api.account.CancellationPenalty;
import com.telus.api.account.Charge;
import com.telus.api.account.Cheque;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.Credit;
import com.telus.api.account.CreditCard;
import com.telus.api.account.CreditCheckResult;
import com.telus.api.account.CreditCheckResultDeposit;
import com.telus.api.account.DepositAssessedHistory;
import com.telus.api.account.DepositHistory;
import com.telus.api.account.Discount;
import com.telus.api.account.EBillRegistrationReminder;
import com.telus.api.account.FinancialHistory;
import com.telus.api.account.FollowUp;
import com.telus.api.account.FollowUpStatistics;
import com.telus.api.account.FutureStatusChangeRequest;
import com.telus.api.account.IDENAccount;
import com.telus.api.account.IDENSubscriber;
import com.telus.api.account.IneligiblePaymentArrangementExtensionException;
import com.telus.api.account.IneligiblePaymentNotificationEligibilityException;
import com.telus.api.account.InternationalServiceEligibilityCheckResult;
import com.telus.api.account.InvalidAddressException;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.account.InvalidFleetException;
import com.telus.api.account.InvalidNetworkException;
import com.telus.api.account.InvalidSerialNumberException;
import com.telus.api.account.InvoiceHistory;
import com.telus.api.account.InvoiceProperties;
//import com.telus.api.account.LMSLetterRequest;
import com.telus.api.account.LightWeightSubscriber;
import com.telus.api.account.ManualCreditCheckRequest;
import com.telus.api.account.Memo;
import com.telus.api.account.MigrationRequest;
import com.telus.api.account.PCSAccount;
import com.telus.api.account.PCSSubscriber;
import com.telus.api.account.PagerAccount;
import com.telus.api.account.PagerSubscriber;
import com.telus.api.account.PaymentFailedException;
import com.telus.api.account.PaymentHistory;
import com.telus.api.account.PaymentMethodChangeHistory;
import com.telus.api.account.PaymentNotification;
import com.telus.api.account.PhoneNumberSearchOption;
import com.telus.api.account.PoolingPricePlanSubscriberCount;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ProductSubscriberList;
import com.telus.api.account.ProrationMinutes;
import com.telus.api.account.RefundHistory;
import com.telus.api.account.RewardRedemptionResult;
import com.telus.api.account.SearchResults;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.account.StatusChangeHistory;
import com.telus.api.account.Subscriber;
import com.telus.api.account.TaxExemption;
import com.telus.api.account.UnknownBANException;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.api.account.UnsupportedEquipmentException;
import com.telus.api.account.VoiceUsageSummary;
import com.telus.api.account.VoiceUsageSummaryException;
import com.telus.api.account.WebUsageSummary;
import com.telus.api.equipment.Equipment;
import com.telus.api.fleet.Fleet;
import com.telus.api.fleet.TalkGroup;
import com.telus.api.portability.PRMSystemException;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestException;
import com.telus.api.reference.Brand;
import com.telus.api.reference.ClientConsentIndicator;
import com.telus.api.reference.CreditCheckDepositChangeReason;
import com.telus.api.reference.DiscountPlan;
//import com.telus.api.reference.Letter;
import com.telus.api.reference.MigrationType;
import com.telus.api.reference.SubscriptionRoleType;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.eas.framework.info.Info;

public class AccountInfo extends Info implements Account, IDENAccount, PCSAccount, PagerAccount {

	public static final long serialVersionUID = -2101023031340832883L;

	// TODO: Move to AccountSummary when we support these types
	//public static final char ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE = '2';
	public static final char ACCOUNT_SUBTYPE_IDEN_PUBLIC_SAFETY = '6';
	public static final short ACCOUNT_CONV_RUN_NO_FIDO = 100;

	private AddressInfo address = new AddressInfo();
	private String additionalLine;
	private String email;
	private String pin;
	private String language;
	private Date startServiceDate;
	private String evaluationProductType;
	private int billCycle;
	private String dealerCode;
	private String salesRepCode;
	private String fullName;
	private int banId;
	private int customerId;
	private char status;
	private char accountType;
	private char accountSubType;
	private Date createDate;
	private FinancialHistoryInfo financialHistory = new FinancialHistoryInfo();
	private CreditCheckResultInfo creditCheckResult = new CreditCheckResultInfo();
	private boolean hotlined;
	private Date lastChangesDate;
	private String ixcCode;
	private String statusActivityReasonCode;
	private String statusActivityCode;
	private int billCycleCloseDay;
	private AddressInfo alternateCreditCheckAddress = new AddressInfo();
	private Fleet[] fleets;
	private TalkGroup[] talkGroups;
	private boolean iDEN;
	private boolean pCS;
	private boolean corporateRegular;
	private boolean corporatePrivateNetworkPlus;
	private byte gstExempt;
	private byte pstExempt;
	private byte hstExempt;
	private Date gstExemptExpiryDate;
	private Date pstExemptExpiryDate;
	private Date hstExemptExpiryDate;
	private boolean corporateHierarchy;
	private String corporateAccountRepCode;
	private String invoiceSuppressionLevel;
	private String homeProvince;
	private String accountCategory;
	private int nextBillCycle;
	private int nextBillCycleCloseDay;
	private Date verifiedDate;
	private String corporateId;
	private boolean handledBySubscriberOnly;
	private ConsumerNameInfo contactName = new ConsumerNameInfo();
	private String contactPhone;
	private String contactPhoneExtension;
	private String contactFax;
	private String otherPhone;
	private String otherPhoneExtension;
	private String otherPhoneType;
	private String[] clientConsentIndicatorCodes;
	private Date gstExemptEffectiveDate;
	private Date pstExemptEffectiveDate;
	private Date hstExemptEffectiveDate;
	private String gstCertificateNumber;
	private String pstCertificateNumber;
	private String hstCertificateNumber;
	private java.util.Date statusDate;
	private boolean fidoConversion;
	private FollowUpStatisticsInfo followUpStatisticsInfo;
	private InvoiceProperties invoiceProperties;
	private String homePhone;
	private String businessPhone;
	private String businessPhoneExtension;
	ProductSubscriberListInfo[] productSubscriberLists;
	private int brandId = Brand.BRAND_ID_TELUS;  //default to Telus
	private char billingNameFormat;
	private String banSegment;
	private String banSubSegment;
    private boolean internalUse = false;

	// during copy BAN process, AccountManager will populate this field
	private int originalBanId;
	
	private int activeSubscriberCount = -1;
	private int suspendedSubscriberCount = -1;
	private int reservedSubscriberCount = -1;
	private int cancelledSubscriberCount = -1;
	private int allActiveSubscriberCount = -1;
	private int allSuspendedSubscriberCount = -1;
	private int allReservedSubscriberCount = -1;
	private int allCancelledSubscriberCount = -1;	

	private boolean isAccountTypeChanged = false;
	private char oldAccountType;
	private char oldAccountSubType;
	private int hierarchyId;
	private boolean noOfInvoiceChanged; //this is not a field for carrying information, but a field hold the change state
	private BillParametersInfo billParamsInfo = new BillParametersInfo();
	private boolean forceZeroBalanceInd;

	private Boolean ePostSubscription;

	private static char[] accountSubtypeCorporateIden = {
		AccountSummary.ACCOUNT_SUBTYPE_IDEN_GOVERNMENT,
		AccountSummary.ACCOUNT_SUBTYPE_IDEN_FEDERAL_GOVENMENT,
		AccountSummary.ACCOUNT_SUBTYPE_IDEN_INDIVIDUAL,
		AccountSummary.ACCOUNT_SUBTYPE_IDEN_ENTERPRISE,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_IDEN_REGULAR,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_IDEN_PUBLIC_SAFETY,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_IDEN_REGIONAL_STRATEGIC,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_IDEN_NATIONAL_STRATEGIC,
		AccountSummary.ACCOUNT_SUBTYPE_IDEN_PRIVATE_NETWORK_PLUS,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_IDEN_DURHAM_POLICE
	};

	private static char[] accountSubtypeCorporatePCS = {
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_FEDERAL_GOVERNMENT,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ENTERPRISE,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ABORIGINAL,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_EMPLOYEE,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_FUSION_EAST_CONV,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_KEY,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_OFFICIAL,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CNBS,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_GOVERNMENT,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_INDIVIDUAL,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_NATIONAL_STRATEGIC,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_REGIONAL_STRATEGIC,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_TMI_AFFILIATE,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_TMI_DIVISION,
		AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE
	};
	
	

	@Override
	public String getPhoneNumberBySubscriberID(String pSubscriberId) throws TelusAPIException {

		throw new UnsupportedOperationException("Method not implemented here");
	}

	public AccountInfo() { }

	public void validate() throws TelusAPIException, InvalidAddressException { }

	public AccountInfo(char accountType, char accountSubType) {
		this.accountType = accountType;
		this.accountSubType = accountSubType;
	}

	@Override
	public Address getAddress() {
		return address;
	}

	public AddressInfo getAddress0() {
		return address;
	}

	public void setAddress0(AddressInfo newAddress0) {
		address = newAddress0;
	}

	@Override
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = toUpperCase(fullName);
	}

	@Override
	public String[] getFullAddress() {
		return address.getFullAddress().clone();
	}

	@Override
	public String getAdditionalLine() {
		return additionalLine;
	}

	@Override
	public void setAdditionalLine(String additionalLine) {
		this.additionalLine = toUpperCase(additionalLine);
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public void setPin(String pin) {
		this.pin = toUpperCase(pin);
	}

	@Override
	public String getPin() {
		return pin;
	}

	@Override
	public String getLanguage() {
		return language;
	}

	@Override
	public void setLanguage(String language) {
		this.language = toUpperCase(language);
	}

	@Override
	public Date getStartServiceDate() {
		return startServiceDate;
	}

	public void setStartServiceDate(Date startServiceDate) {
		this.startServiceDate = startServiceDate;
	}

	public String getEvaluationProductType() {
		return evaluationProductType;
	}

	public void setEvaluationProductType(String evaluationProductType) {
		this.evaluationProductType = toUpperCase(evaluationProductType);
	}

	@Override
	public int getBillCycle() {
		return billCycle;
	}

	@Override
	public String getDealerCode() {
		return dealerCode;
	}

	@Override
	public void setDealerCode(String dealerCode) {
		this.dealerCode = toUpperCase(dealerCode);
	}

	@Override
	public FinancialHistory getFinancialHistory() {
		return financialHistory;
	}

	public FinancialHistoryInfo getFinancialHistory0() {
		return financialHistory;
	}

	@Override
	public CreditCheckResult getCreditCheckResult() {
		return creditCheckResult;
	}

	public CreditCheckResultInfo getCreditCheckResult0() {
		return creditCheckResult;
	}

	@Override
	public String getHomeProvince() {
		return homeProvince;
	}

	@Override
	public String getAccountCategory() {
		return accountCategory;
	}

	@Override
	public void setAccountCategory(String accountCategory) {
		this.accountCategory = accountCategory;
	}

	@Override
	public int getNextBillCycle() {
		return nextBillCycle;
	}

	@Override
	public int getNextBillCycleCloseDay() {
		return nextBillCycleCloseDay;
	}

	@Override
	public Date getVerifiedDate() {
		return verifiedDate;
	}

	@Override
	public void setVerifiedDate(Date date) {
		this.verifiedDate = date;
	}

	/**
	 * NO-OP
	 */
	public void save(boolean validateAddress, boolean checkDuplicateBAN) throws
	TelusAPIException,
	InvalidAddressException, DuplicateObjectException,
	InvalidCreditCardException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	@Override
	public void save() throws TelusAPIException, InvalidCreditCardException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	@Override
	public AccountSummary[] getDuplicateAccounts() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	/**
	 * NO-OP
	 */
	@Override
	public AccountSummary[] getDuplicateAccounts(String duplicateSearchLevel) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	/**
	 * NO-OP
	 */
	@Override
	public int[] getDuplicateAccountBANs() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	
	/**
	 * NO-OP
	 */
	@Override
	public int[] getDuplicateAccountBANs(String duplicateSearchLevel) throws TelusAPIException{
		throw new UnsupportedOperationException("Method not implemented here");
	}

	
	/**
	 * NO-OP
	 */
	@Override
	public void refresh() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	@Override
	public void refreshTalkGroups() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	@Override
	public void refreshFleets() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	@Override
	public void refreshCreditCheckResult() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	@Override
	public String payDeposit(int subscriberCount, double amount,
			CreditCard creditCard, String businessRole, AuditHeader auditHeader) throws
			TelusAPIException, PaymentFailedException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	@Override
	public String payDeposit(int subscriberCount, double amount,
			CreditCard creditCard, String sourceID,
			String sourceType, String businessRole, AuditHeader auditHeader) throws
			TelusAPIException, PaymentFailedException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	/**
	 * NO-OP
	 */
	@Override
	public String payBill(double amount, CreditCard creditCard,
			String businessRole, AuditHeader auditHeader) throws TelusAPIException,
			PaymentFailedException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	@Override
	public String payBill(double amount, CreditCard creditCard, String sourceID,
			String sourceType, String businessRole, AuditHeader auditHeader) throws
			TelusAPIException, PaymentFailedException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	public void applyCreditToAccount(Credit credit) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public int getBanId() {
		return banId;
	}

	@Override
	public int getCustomerId() {
		return customerId;
	}

	@Override
	public char getStatus() {
		return status;
	}

	@Override
	public boolean isPostpaidEmployee() {
		return accountType == ACCOUNT_TYPE_CONSUMER &&
		(accountSubType == ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE ||
				accountSubType == ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE ||
				accountSubType == ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE_NEW ||
				accountSubType == ACCOUNT_SUBTYPE_IDEN_PERSONAL  );
	}

	@Override
	public boolean isPostpaidConsumer() {
		return accountType == ACCOUNT_TYPE_CONSUMER &&
		(accountSubType == ACCOUNT_SUBTYPE_PCS_REGULAR ||
				accountSubType == ACCOUNT_SUBTYPE_IDEN_REGULAR ||
				accountSubType == ACCOUNT_SUBTYPE_PAGER_REGULAR ||
				accountSubType == ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE ||
				accountSubType == ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE ||
				accountSubType == ACCOUNT_SUBTYPE_AUTOTEL_REGULAR ||
				accountSubType == ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE_NEW ||
				accountSubType == ACCOUNT_SUBTYPE_IDEN_PERSONAL );
	}

	@Override
	public boolean isPostpaidBoxedConsumer() {
		return accountType == ACCOUNT_TYPE_CONSUMER &&
		(accountSubType == ACCOUNT_SUBTYPE_PAGER_BOXED);
	}

	@Override
	public boolean isPostpaidBusinessRegular() {
		return accountType == ACCOUNT_TYPE_BUSINESS &&
		(accountSubType == ACCOUNT_SUBTYPE_PCS_REGULAR ||
				accountSubType == ACCOUNT_SUBTYPE_IDEN_REGULAR ||
				accountSubType == ACCOUNT_SUBTYPE_PAGER_REGULAR ||
				accountSubType == ACCOUNT_SUBTYPE_IDEN_DEALER ||
				accountSubType == ACCOUNT_SUBTYPE_PCS_DEALER ||
				accountSubType == ACCOUNT_SUBTYPE_PCS_OFFICAL ||
				accountSubType == ACCOUNT_SUBTYPE_AUTOTEL_REGULAR||
				accountSubType == ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR ||
				accountSubType == ACCOUNT_SUBTYPE_REGULAR_MEDIUM||
				accountSubType == ACCOUNT_SUBTYPE_PCS_CONNECT_REGULAR);
	}


	@Override
	public boolean isPostpaidOfficial(){
		return accountSubType == ACCOUNT_SUBTYPE_PCS_OFFICAL;
	}

	@Override
	public boolean isPostpaidBusinessOfficial() {
		return accountType == ACCOUNT_TYPE_BUSINESS &&
		accountSubType == ACCOUNT_SUBTYPE_PCS_OFFICAL;
	}


	@Override
	public boolean isPostpaidBusinessPersonal() {
		return accountType == ACCOUNT_TYPE_BUSINESS &&
		   (accountSubType == ACCOUNT_SUBTYPE_PCS_PERSONAL ||
			accountSubType == ACCOUNT_SUBTYPE_IDEN_PERSONAL || 
			accountSubType ==ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL ||
			accountSubType == ACCOUNT_SUBTYPE_PCS_CONNECT_PERSONAL );
	}

	@Override
	public boolean isPostpaidBusinessDealer() {
		return accountType == ACCOUNT_TYPE_BUSINESS &&
		(accountSubType == ACCOUNT_SUBTYPE_IDEN_DEALER ||
				accountSubType == ACCOUNT_SUBTYPE_PCS_DEALER);
	}

	@Override
	public boolean isPostpaidBusinessConnect() {
		return accountType == ACCOUNT_TYPE_BUSINESS &&
		   (accountSubType == ACCOUNT_SUBTYPE_PCS_CONNECT_REGULAR ||
			accountSubType == ACCOUNT_SUBTYPE_PCS_CONNECT_PERSONAL );
	}
	
	@Override
	public boolean isSuspendedDueToNonPayment() {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public boolean isPrepaidConsumer() {
		return accountType == ACCOUNT_TYPE_CONSUMER &&
		(accountSubType == ACCOUNT_SUBTYPE_PCS_PREPAID||
				accountSubType == ACCOUNT_SUBTYPE_PCS_QUEBECTEL ||
				accountSubType == ACCOUNT_SUBTYPE_PCS_WESTERN_PREPAID);

	}

	@Override
	public boolean isPostpaid() {
		return  !isPrepaidConsumer()
		&& !isQuebectelPrepaidConsumer()
		&& !isWesternPrepaidConsumer()
		;
	}

	@Override
	public boolean isQuebectelPrepaidConsumer() {
		return accountType == ACCOUNT_TYPE_CONSUMER
		&& accountSubType == ACCOUNT_SUBTYPE_PCS_QUEBECTEL
		;
	}

	@Override
	public boolean isWesternPrepaidConsumer() {
		return accountType == ACCOUNT_TYPE_CONSUMER
		&& accountSubType == ACCOUNT_SUBTYPE_PCS_WESTERN_PREPAID
		;
	}

//	public void setWesternPrepaidConsumer(boolean westernPrepaidConsumer) {
//	this.westernPrepaidConsumer = westernPrepaidConsumer;
//	}

	@Override
	public boolean isIDEN() {
//		return accountSubType == ACCOUNT_SUBTYPE_IDEN_REGULAR ||
//		accountSubType == ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE ||
//		accountSubType == ACCOUNT_SUBTYPE_IDEN_PERSONAL ||
//		accountSubType == ACCOUNT_SUBTYPE_IDEN_DEALER ||
//		accountSubType == ACCOUNT_SUBTYPE_IDEN_PRIVATE_NETWORK_PLUS ||
//		accountSubType == ACCOUNT_SUBTYPE_IDEN_PUBLIC_SAFETY;

		if (ACCOUNT_TYPE_CORPORATE == accountType) {
			for (int i = 0; i < accountSubtypeCorporateIden.length; i++) {
				if (accountSubType == accountSubtypeCorporateIden[i])
					return true;
			}
			if (accountSubType == '9') // PCS: Corporate - Autotel EARS
				return false;
		}
		return Character.isDigit(accountSubType);
	}

	@Override
	public boolean isPCS() {
		return !isIDEN() && !isPager() && !isAutotel();
	}

	@Override
	public boolean isPager() {
		return ((accountType == ACCOUNT_TYPE_CONSUMER
				&& (accountSubType == ACCOUNT_SUBTYPE_PAGER_REGULAR || accountSubType == ACCOUNT_SUBTYPE_PAGER_BOXED))
				||
				(accountType == ACCOUNT_TYPE_BUSINESS
						&& accountSubType == ACCOUNT_SUBTYPE_PAGER_REGULAR));
	}

	@Override
	public boolean isAutotel() {
		return ((accountType == ACCOUNT_TYPE_CONSUMER
				&& accountSubType == ACCOUNT_SUBTYPE_AUTOTEL_REGULAR)
				||
				(accountType == ACCOUNT_TYPE_BUSINESS
						&& accountSubType == ACCOUNT_SUBTYPE_AUTOTEL_REGULAR)
						||
						(accountType == ACCOUNT_TYPE_CORPORATE
								&& (accountSubType == ACCOUNT_SUBTYPE_AUTOTEL_REGULAR || accountSubType == ACCOUNT_SUBTYPE_AUTOTEL_EARS)));
	}

	public boolean isCorporate() {
		return accountType == ACCOUNT_TYPE_CORPORATE ;
	}

	@Override
	@Deprecated
	public boolean isFidoConversion() {
		return fidoConversion;
	}

	@Deprecated
	public void setFidoConversion(boolean isFidoConversion) {
		fidoConversion = isFidoConversion;
	}

	@Override
	public boolean isCorporateRegular() {
		return accountType == ACCOUNT_TYPE_CORPORATE &&
		(accountSubType == ACCOUNT_SUBTYPE_IDEN_REGULAR || accountSubType ==ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE);
	}

	@Override
	public boolean isCorporateRegional() {
		return accountType == ACCOUNT_TYPE_CORPORATE &&
		accountSubType == ACCOUNT_SUBTYPE_PCS_REGIONAL;
	}

	@Override
	public boolean isCorporatePrivateNetworkPlus() {
		return accountType == ACCOUNT_TYPE_CORPORATE &&
		accountSubType == ACCOUNT_SUBTYPE_IDEN_PRIVATE_NETWORK_PLUS;
	}
	
	public boolean isCorporateReseller() {
		return accountType == ACCOUNT_TYPE_CORPORATE &&
		accountSubType == ACCOUNT_SUBTYPE_CORP_RESELLER;
	}

	@Override
	public char getAccountType() {
		return accountType;
	}

	@Override
	public char getAccountSubType() {
		return accountSubType;
	}

	@Override
	public String getHomePhone() {
		return homePhone;
	}

	@Override
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	@Override
	public String getBusinessPhone() {
		return businessPhone;
	}

	@Override
	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	@Override
	public String getBusinessPhoneExtension() {
		return businessPhoneExtension;
	}

	@Override
	public void setBusinessPhoneExtension(String businessPhoneExtension) {
		this.businessPhoneExtension = businessPhoneExtension;
	}

	/**
	 * NO-OP
	 */
	@Override
	public Account getAccount() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	public Subscriber[] getSubscribers() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public Subscriber[] getPortedSubscribers(int maximum) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	@Override
	public Subscriber getSubscriber(String subscriberId) throws
	UnknownSubscriberException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public Subscriber getSubscriberByPhoneNumber(String phoneNumber) throws
	UnknownSubscriberException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/* public HashMap getPhoneNumbersForBAN() throws  TelusAPIException {
     return phoneNumbers;
  }*/

	/**
	 * NO-OP
	 */
	@Override
	public Subscriber[] getSubscribers(int maximum) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public Subscriber[] getSubscribers(int maximum, boolean includeCancelled) throws
	TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public IDENSubscriber[] getSubscribers(int urbanId, int fleetId, int maximum) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public String[] getActiveSubscriberPhoneNumbers(int maxNumbers) throws
	TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public String[] getSuspendedSubscriberPhoneNumbers(int maxNumbers) throws
	TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public String[] getSubscriberPhoneNumbersByStatus(char status, int maxNumbers) throws
	TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	public Subscriber newSubscriber(String productType, String serialNumber,
			boolean dealerHasDeposit) throws
			UnknownSerialNumberException,
			SerialNumberInUseException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	public Subscriber newSubscriber(String productType, String serialNumber,
			boolean dealerHasDeposit,
			String activationFeeChargeCode) throws
			UnknownSerialNumberException,
			SerialNumberInUseException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	@Override
	public void savePin(String newPin) throws UnknownBANException,
	TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	public void saveStatus(String newStatus) throws UnknownBANException,
	TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	@Override
	public CreditCheckResult checkNewSubscriberEligibility(int subscriberCount, double thresholdAmount) throws
	TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * NO-OP
	 */
	@Override
	public Memo getLastCreditCheckMemo() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public TaxExemption getTaxExemption() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public void setBillCycle(int billCycle) {
		this.billCycle = billCycle;
	}

	public void setBanId(int banId) {
		this.banId = banId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public void setAccountType(char accountType) {
		this.accountType = accountType;
	}

	public void setAccountSubType(char accountSubType) {
		this.accountSubType = accountSubType;
	}

	public void setCreateDate(Date newCreateDate) {
		createDate = newCreateDate;
	}

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public int getActiveSubscribersCount() {
		/** Moved to TMACcount 
		if (productSubscriberLists == null)
			return 0;

		int count = 0;

		for (int i = 0; i < productSubscriberLists.length; i++)
			if ((productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_PCS ) || productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN))
					|| ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER || getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS) && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR)
					|| (getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_BOXED))
				count += productSubscriberLists[i].getActiveSubscribersCount();

		return count;
		**/
		return activeSubscriberCount;
	}

	@Override
	public int getSubscriberCount() {
		return getActiveSubscribersCount() +
		getSuspendedSubscribersCount() +
		getReservedSubscribersCount() +
		getCancelledSubscribersCount();
	}

	@Override
	public int getReservedSubscribersCount() {
		/** Moved to TMACcount
		if (productSubscriberLists == null)
			return 0;

		int count = 0;

		for (int i = 0; i < productSubscriberLists.length; i++)
			if ((productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_PCS ) || productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN))
					|| ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER || getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS) && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR)
					|| (getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_BOXED))
				count += productSubscriberLists[i].getReservedSubscribersCount();

		return count;
		**/
		return reservedSubscriberCount;
	}

	@Override
	public int getCancelledSubscribersCount() {
		/** Moved to TMAccount
		if (productSubscriberLists == null)
			return 0;

		int count = 0;

		for (int i = 0; i < productSubscriberLists.length; i++)
			if ((productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_PCS ) || productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN))
					|| ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER || getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS) && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR)
					|| (getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_BOXED))
				count += productSubscriberLists[i].getCancelledSubscribersCount();

		return count;
		**/
		return cancelledSubscriberCount;
	}

	@Override
	public int getSuspendedSubscribersCount() {
		/** Moved to TMAccount
		if (productSubscriberLists == null)
			return 0;

		int count = 0;

		for (int i = 0; i < productSubscriberLists.length; i++)
			if ((productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_PCS ) || productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN))
					|| ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER || getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS) && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR)
					|| (getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_BOXED))
				count += productSubscriberLists[i].getSuspendedSubscribersCount();

		return count;
		**/
		return suspendedSubscriberCount;
	}

	@Override
	public int getAllActiveSubscribersCount() {
		/**Moved to TMAccount
		 * 
		if (productSubscriberLists == null)
			return 0;

		int count = 0;

		for (int i = 0; i < productSubscriberLists.length; i++)
			count += productSubscriberLists[i].getActiveSubscribersCount();

		return count;
		**/
		return allActiveSubscriberCount;
	}

	@Override
	public int getAllSubscriberCount() {
		return getAllActiveSubscribersCount() +
		getAllSuspendedSubscribersCount() +
		getAllReservedSubscribersCount() +
		getAllCancelledSubscribersCount();
	}

	@Override
	public int getAllReservedSubscribersCount() {
		/** Moved to TMAccount
		if (productSubscriberLists == null)
			return 0;

		int count = 0;

		for (int i = 0; i < productSubscriberLists.length; i++)
			count += productSubscriberLists[i].getReservedSubscribersCount();

		return count;
		**/
		return allReservedSubscriberCount;
	}

	@Override
	public int getAllCancelledSubscribersCount() {
		/** Moved to TMAccount
		 * 
		 */
		
//		if (productSubscriberLists == null)
//			return 0;
//
//		int count = 0;
//
//		for (int i = 0; i < productSubscriberLists.length; i++)
//			count += productSubscriberLists[i].getCancelledSubscribersCount();
//
//		return count;
		
		return allCancelledSubscriberCount;
	}

	@Override
	public int getAllSuspendedSubscribersCount() {
		/** Moved to TMAccount
		 * 
		 */
		
//		if (productSubscriberLists == null)
//			return 0;
//
//		int count = 0;
//
//		for (int i = 0; i < productSubscriberLists.length; i++)
//			count += productSubscriberLists[i].getSuspendedSubscribersCount();
//
//		return count;
		
		return allSuspendedSubscriberCount;
	}

	@Override
	public String getSpecialInstructions() {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public Memo getSpecialInstructionsMemo() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public void setSalesRepCode(String newSalesRepCode) {
		salesRepCode = toUpperCase(newSalesRepCode);
	}

	@Override
	public String getSalesRepCode() {
		return salesRepCode;
	}

	/**
	 * @deprecated Use getFinancialHistory().isHotlined instead.
	 * since May 29, 2006
	 */
	@Deprecated
	@Override
	public boolean isHotlined() {
		return hotlined;
	}

	@Override
	public void setHotlined(boolean hotlined) {
		this.hotlined = hotlined;
	}

	public String getIxcCode() {
		return ixcCode;
	}

	public void setIxcCode(String ixcCode) {
		this.ixcCode = ixcCode;
	}

	@Override
	public Date getLastChangesDate() {
		return lastChangesDate;
	}

	public void setLastChangesDate(Date newLastChangesDate) {
		lastChangesDate = newLastChangesDate;
	}

	public void setStatusActivityCode(String newStatusActivityCode) {
		statusActivityCode = newStatusActivityCode;
	}

	@Override
	public String getStatusActivityCode() {
		return statusActivityCode;
	}

	public void setStatusActivityReasonCode(String newStatusActivityReasonCode) {
		statusActivityReasonCode = newStatusActivityReasonCode;
	}

	@Override
	public String getStatusActivityReasonCode() {
		return statusActivityReasonCode;
	}

	public void copyFrom(AccountInfo o) {
		
		address.copyFrom(o.address);
		setAdditionalLine(o.additionalLine);
		setEmail(o.email);
		setPin(o.pin);
		setLanguage(o.language);
		startServiceDate = cloneDate(o.startServiceDate);
		setEvaluationProductType(o.evaluationProductType);
		setBillCycle(o.billCycle);
		setBillCycleCloseDay(o.billCycleCloseDay);
		setDealerCode(o.dealerCode);
		setSalesRepCode(o.salesRepCode);
		fullName = o.fullName;
		setBanId(o.banId);
		setCustomerId(o.customerId);
		setStatus(o.status);
		setAccountType(o.accountType);
		setAccountSubType(o.accountSubType);
		setCreateDate(cloneDate(o.createDate));
		setStatusDate(o.statusDate);
		financialHistory.copyFrom(o.financialHistory);
		creditCheckResult.copyFrom(o.creditCheckResult);
		setHotlined(o.hotlined);
		lastChangesDate = cloneDate(o.lastChangesDate);
		setIxcCode(o.ixcCode);
		setStatusActivityCode(o.statusActivityCode);
		setStatusActivityReasonCode(o.statusActivityReasonCode);
		setInvoiceProperties(o.invoiceProperties);
		setCorporateId(o.corporateId);
		setHomeProvince(o.getHomeProvince());
		setAccountCategory(o.getAccountCategory());
		setNextBillCycle(o.getNextBillCycle());
		setNextBillCycleCloseDay(o.getNextBillCycleCloseDay());
		setVerifiedDate(o.getVerifiedDate());
		setHandledBySubscriberOnly(o.isHandledBySubscriberOnly());
		gstExempt = o.getGSTExempt();		
		pstExempt = o.getPSTExempt();
		hstExempt = o.getHSTExempt();
		setGSTExemptEffectiveDate(o.getGSTExemptEffectiveDate());
		setPSTExemptEffectiveDate(o.getPSTExemptEffectiveDate());
		setHSTExemptEffectiveDate(o.getHSTExemptEffectiveDate());
		setGSTExemptExpiryDate(o.getGSTExemptExpiryDate());
		setPSTExemptExpiryDate(o.getPSTExemptExpiryDate());
		setHSTExemptExpiryDate(o.getHSTExemptExpiryDate());
		setContactPhone(o.contactPhone);
		setContactPhoneExtension(o.contactPhoneExtension);
		setContactFax(o.contactFax);
		contactName.copyFrom(o.contactName);
		setOtherPhone(o.otherPhone);
		setOtherPhoneExtension(o.otherPhoneExtension);
		setOtherPhoneType(o.otherPhoneType);
		setClientConsentIndicatorCodes(o.clientConsentIndicatorCodes);
		setProductSubscriberLists((ProductSubscriberListInfo[]) o.getProductSubscriberLists());
		setHomePhone(o.homePhone);
		setBusinessPhone(o.businessPhone);
		setBusinessPhoneExtension(o.businessPhoneExtension);
		setBrandId(o.brandId);
		setActiveSubscribersCount(o.activeSubscriberCount);
		setSuspendedSubscribersCount(o.suspendedSubscriberCount);
		setReservedSubscribersCount(o.reservedSubscriberCount);
		setCancelledSubscribersCount(o.cancelledSubscriberCount);
		setAllActiveSubscribersCount(o.allActiveSubscriberCount);
		setAllSuspendedSubscribersCount(o.allSuspendedSubscriberCount);
		setAllReservedSubscribersCount(o.allReservedSubscriberCount);
		setAllCancelledSubscribersCount(o.allCancelledSubscriberCount);
		ePostSubscription = o.isEPostSubscription();
	}

	public void setBillCycleCloseDay(int billCycleCloseDay) {
		this.billCycleCloseDay = billCycleCloseDay;
	}

	@Override
	public int getBillCycleCloseDay() {
		return billCycleCloseDay;
	}

	public Address getAlternateCreditCheckAddress() {
		return alternateCreditCheckAddress;
	}

	public AddressInfo getAlternateCreditCheckAddress0() {
		return alternateCreditCheckAddress;
	}

	@Override
	public IDENSubscriber newIDENSubscriber(String serialNumber,
			boolean dealerHasDeposit, String voiceMailLanguage) throws
			UnknownSerialNumberException, SerialNumberInUseException,
			TelusAPIException {
		return null;
	}

	@Override
	public IDENSubscriber newIDENSubscriber(String serialNumber,
			String muleNumber,
			boolean dealerHasDeposit,String voiceMailLanguage) throws
			UnknownSerialNumberException, SerialNumberInUseException,
			TelusAPIException {
		return null;
	}

	@Override
	public IDENSubscriber newIDENSubscriber(String serialNumber,
			boolean dealerHasDeposit,
			String activationFeeChargeCode,String voiceMailLanguage) throws
			UnknownSerialNumberException, SerialNumberInUseException,
			TelusAPIException {
		return null;
	}

	@Override
	public IDENSubscriber newIDENSubscriber(String serialNumber,
			String muleNumber,
			boolean dealerHasDeposit,
			String activationFeeChargeCode,String voiceMailLanguage) throws
			UnknownSerialNumberException, SerialNumberInUseException,
			TelusAPIException {
		return null;
	}

	@Override
	public PagerSubscriber newPagerSubscriber(String serialNumber) throws
	UnknownSerialNumberException, SerialNumberInUseException,
	InvalidSerialNumberException, TelusAPIException {
		return null;
	}

	@Override
	public PagerSubscriber newPagerSubscriber(String serialNumber,
			String activationFeeChargeCode) throws
			UnknownSerialNumberException, SerialNumberInUseException,
			InvalidSerialNumberException, TelusAPIException {
		return null;
	}

	@Override
	public Fleet newFleet(int networkId, String name, int numberOfSubscriber) throws
	InvalidNetworkException, UnknownBANException, DuplicateObjectException,
	TelusAPIException {
		return null;
	}

	@Override
	public void addFleets(Fleet[] fleet) throws UnknownBANException,
	InvalidFleetException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public void addFleet(Fleet fleet) throws UnknownBANException,
	InvalidFleetException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public void addTalkGroups(TalkGroup[] talkGroup) throws UnknownBANException,
	InvalidFleetException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public void addTalkGroup(TalkGroup talkGroup) throws UnknownBANException,
	InvalidFleetException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public void removeTalkGroup(TalkGroup talkGroup) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public Fleet[] getFleets() {
		return fleets;
	}

	@Override
	public TalkGroup[] getTalkGroups() {
		return talkGroups;
	}

	@Override
	public TalkGroup[] getTalkGroups(int urbanId, int fleetId) {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public boolean contains(Fleet fleet) throws UnknownBANException,
	TelusAPIException {
		return false;
	}

	@Override
	public boolean contains(TalkGroup talkGroup) throws UnknownBANException,
	TelusAPIException {
		return false;
	}

	@Override
	public PCSSubscriber newPCSSubscriber(String serialNumber,
			boolean dealerHasDeposit, String voiceMailLanguage) throws
			UnknownSerialNumberException, SerialNumberInUseException,
			TelusAPIException {
		return null;
	}

	public PCSSubscriber newPCSPagerSubscriber(String serialNumber,
			boolean dealerHasDeposit) throws
			UnknownSerialNumberException, SerialNumberInUseException,
			TelusAPIException {
		return null;
	}

	@Override
	public PCSSubscriber newPCSSubscriber(String serialNumber,
			boolean dealerHasDeposit,
			String activationFeeChargeCode, String voiceMailLanguage) throws
			UnknownSerialNumberException, SerialNumberInUseException,
			TelusAPIException {
		return null;
	}

	@Override
	public PCSSubscriber newPCSSubscriber(String serialNumber, String[] secondarySerialNumbers, boolean dealerHasDeposit, String activationFeeChargeCode,String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public PCSSubscriber newPCSPagerSubscriber(String serialNumber,
			boolean dealerHasDeposit,
			String activationFeeChargeCode) throws
			UnknownSerialNumberException, SerialNumberInUseException,
			TelusAPIException {
		return null;
	}

	@Override
	public Memo newMemo() throws TelusAPIException {
		return null;
	}

	@Override
	public FollowUp newFollowUp() throws TelusAPIException {
		return null;
	}

	public Fleet newFleet(int networkId, String name) throws
	InvalidNetworkException, UnknownBANException,
	DuplicateObjectException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public Charge newCharge() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public Credit newCredit() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public Credit newCredit(boolean taxable) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public Credit newCredit(char taxOption) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public Memo getLastMemo(String memoType) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public InvoiceHistory[] getInvoiceHistory(Date from, Date to) throws
	TelusAPIException, HistorySearchException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public PaymentHistory[] getPaymentHistory(Date from, Date to) throws
	TelusAPIException, HistorySearchException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public PaymentHistory getLastPaymentActivity() throws
	TelusAPIException, HistorySearchException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public PaymentMethodChangeHistory[] getPaymentMethodChangeHistory(Date from,
			Date to) throws TelusAPIException, HistorySearchException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public StatusChangeHistory[] getStatusChangeHistory(Date from, Date to) throws
	TelusAPIException, HistorySearchException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public AddressHistory[] getAddressChangeHistory(Date from, Date to) throws
	TelusAPIException, HistorySearchException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public ProrationMinutes[] getProrationMinutes(int months, int totalMinutes) throws
	TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public VoiceUsageSummary[] getVoiceUsageSummary() throws
	VoiceUsageSummaryException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public VoiceUsageSummary[] getVoiceUsageSummary(String featureCode) throws
	VoiceUsageSummaryException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public WebUsageSummary[] getWebUsageSummary() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public PricePlanSubscriberCount[] getPricePlanSubscriberCount() 
	throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public PricePlanSubscriberCount getPricePlanSubscriberCount(String pricePlanCode)
	throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	@Override
	public PricePlanSubscriberCount[] getShareablePricePlanSubscriberCount()
	throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	@Override
	public PricePlanSubscriberCount[] getShareablePricePlanSubscriberCount(boolean refresh)
	throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public PricePlanSubscriberCount getShareablePricePlanSubscriberCount(String pricePlanCode)
	throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	@Override
	public ServiceSubscriberCount[ ] getServiceSubscriberCounts(String[] serviceCodes, boolean includeExpired) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public boolean isGSTExempt() {
		if (gstExempt != (byte)0 )
			return gstExempt == (byte)'Y' ? true : false;
		else 
			return false;
	}

	public byte getGSTExempt() {
		return gstExempt;
	}
	
	@Override
	public boolean isPSTExempt() {
		if (pstExempt != (byte)0 )
			return pstExempt == (byte)'Y' ? true : false;
		else
			return false;
	}

	public byte getPSTExempt() {
		return pstExempt;
	}
	
	@Override
	public boolean isHSTExempt() {
		if (hstExempt != (byte)0 )
			return hstExempt == (byte)'Y' ? true : false;
		else
			return false;
	}

	public byte getHSTExempt() {
		return hstExempt;
	}
	
	public Date getGSTExemptExpiryDate() {
		return gstExemptExpiryDate;
	}

	public Date getPSTExemptExpiryDate() {
		return pstExemptExpiryDate;
	}

	public Date getHSTExemptExpiryDate() {
		return hstExemptExpiryDate;
	}

	public void setGstExempt(byte gstExempt) {
		this.gstExempt = gstExempt;
	}

	public void setPstExempt(byte pstExempt) {
		this.pstExempt = pstExempt;
	}

	public void setHstExempt(byte hstExempt) {
		this.hstExempt = hstExempt;
	}

	public void setCorporateHierarchy(boolean corporateHierarchy) {
		this.corporateHierarchy = corporateHierarchy;
	}

	@Override
	public boolean isCorporateHierarchy() {
		return corporateHierarchy;
	}

	@Override
	public boolean isPagerPrepaidConsumeraccount() {
		return false;
	}

	@Override
	public boolean isPCSPostpaidCorporateRegularAccount() {
		return isCorporatePCS() && isPostpaid() && !isPostpaidCorporatePersonal();
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer(128);

		s.append("AccountInfo:[\n");
		s.append("    ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE=[").append(
				ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE).append("]\n");
		s.append("    ACCOUNT_SUBTYPE_IDEN_PUBLIC_SAFETY=[").append(
				ACCOUNT_SUBTYPE_IDEN_PUBLIC_SAFETY).append("]\n");
		s.append("    address=[").append(address).append("]\n");
		s.append("    additionalLine=[").append(additionalLine).append("]\n");
		s.append("    email=[").append(email).append("]\n");
		s.append("    pin=[").append(pin).append("]\n");
		s.append("    language=[").append(language).append("]\n");
		s.append("    startServiceDate=[").append(startServiceDate).append("]\n");
		s.append("    evaluationProductType=[").append(evaluationProductType).
		append("]\n");
		s.append("    billCycle=[").append(billCycle).append("]\n");
		s.append("    dealerCode=[").append(dealerCode).append("]\n");
		s.append("    salesRepCode=[").append(salesRepCode).append("]\n");
		s.append("    fullName=[").append(fullName).append("]\n");
		s.append("    banId=[").append(banId).append("]\n");
		s.append("    customerId=[").append(customerId).append("]\n");
		s.append("    brandId=[").append(brandId).append("]\n");
		s.append("    status=[").append(status).append("]\n");
		s.append("    banSegment=[").append(banSegment).append("]\n");
		s.append("    banSubSegment=[").append(banSubSegment).append("]\n");
		s.append("    accountType=[").append(accountType).append("]\n");
		s.append("    accountSubType=[").append(accountSubType).append("]\n");
		s.append("    createDate=[").append(createDate).append("]\n");
		s.append("    financialHistory=[").append(financialHistory).append("]\n");
		s.append("    creditCheckResult=[").append(creditCheckResult).append("]\n");
		s.append("    hotlined=[").append(hotlined).append("]\n");
		s.append("    lastChangesDate=[").append(lastChangesDate).append("]\n");
		s.append("    ixcCode=[").append(ixcCode).append("]\n");
		s.append("    statusActivityReasonCode=[").append(statusActivityReasonCode).
		append("]\n");
		s.append("    statusActivityCode=[").append(statusActivityCode).append(
		"]\n");
		s.append("    billCycleCloseDay=[").append(billCycleCloseDay).append("]\n");
		s.append("    homeProvince=[").append(homeProvince).append("]\n");
		s.append("    alternateCreditCheckAddress=[").append(
				alternateCreditCheckAddress).append("]\n");
		s.append("    accountCategory=[").append(accountCategory).append("]\n");
		s.append("    nextBillCycle=[").append(nextBillCycle).append("]\n");
		s.append("    verifiedDate=[").append(verifiedDate).append("]\n");
		s.append("    handledBySubscriberOnly=[").append(handledBySubscriberOnly).
		append("]\n");
		s.append("    gstExempt=[").append(gstExempt).append("]\n");
		s.append("    pstExempt=[").append(pstExempt).append("]\n");
		s.append("    hstExempt=[").append(hstExempt).append("]\n");
		s.append("    gstExemptExpiryDate=[").append(gstExemptExpiryDate).append(
		"]\n");
		s.append("    pstExemptExpiryDate=[").append(pstExemptExpiryDate).append(
		"]\n");
		s.append("    hstExemptExpiryDate=[").append(hstExemptExpiryDate).append(
		"]\n");
		s.append("    homeProvince=[").append(homeProvince).append("]\n");
		s.append("    accountCategory=[").append(accountCategory).append("]\n");
		s.append("    nextBillCycle=[").append(nextBillCycle).append("]\n");
		s.append("    nextBillCycleCloseDay=[").append(nextBillCycleCloseDay).append("]\n");
		s.append("    verifiedDate=[").append(verifiedDate).append("]\n");
		s.append("    handledBySubscriberOnly=[").append(handledBySubscriberOnly).append("]\n");
		s.append("    otherPhoneType=[").append(otherPhoneType).append("]\n");
		s.append("    otherPhone=[").append(otherPhone).append("]\n");
		s.append("    otherPhoneExtension=[").append(otherPhoneExtension).append("]\n");
		s.append("    contactPhone=[").append(contactPhone).append("]\n");
		s.append("    contactPhoneExtension=[").append(contactPhoneExtension).append("]\n");
		s.append("    homePhone=[").append(homePhone).append("]\n");
		s.append("    businessPhone=[").append(businessPhone).append("]\n");
		s.append("    businessPhoneExtension=[").append(businessPhoneExtension).append("]\n");
		s.append("    contactFax=[").append(contactFax).append("]\n");
		s.append("    contactName=[").append(contactName).append("]\n");
		if (fleets == null) {
			s.append("    fleets=[null]\n");
		} else if (fleets.length == 0) {
			s.append("    fleets={}\n");
		} else {
			for (int i = 0; i < fleets.length; i++) {
				s.append("    fleets[" + i + "]=[").append(fleets[i]).append("]\n");
			}
		}
		if (talkGroups == null) {
			s.append("    talkGroups=[null]\n");
		} else if (talkGroups.length == 0) {
			s.append("    talkGroups={}\n");
		} else {
			for (int i = 0; i < talkGroups.length; i++) {
				s.append("    talkGroups[" + i +
				"]=[").append(talkGroups[i]).append("]\n");
			}
		}
		s.append("    iDEN=[").append(iDEN).append("]\n");
		s.append("    pCS=[").append(pCS).append("]\n");
		s.append("    corporateRegular=[").append(corporateRegular).append("]\n");
		s.append("    corporatePrivateNetworkPlus=[").append(
				corporatePrivateNetworkPlus).append("]\n");
		s.append("    gstExempt=[").append(gstExempt).append("]\n");
		s.append("    pstExempt=[").append(pstExempt).append("]\n");
		s.append("    hstExempt=[").append(hstExempt).append("]\n");
		s.append("    corporateHierarchy=[").append(corporateHierarchy).append(
		"]\n");
		s.append("    corporateAccountRepCode=[").append(corporateAccountRepCode).
		append("]\n");
		s.append("    invoiceSuppressionLevel=[").append(invoiceSuppressionLevel).
		append("]\n");
		s.append("    corporateId=[").append(corporateId).append("]\n");
		s.append("    fidoConversion=[").append(fidoConversion).append("]\n");

		s.append("    productSubscriberLists=[\n");
		if (productSubscriberLists != null)
			for (int i = 0; i < productSubscriberLists.length; i++)
				s.append(productSubscriberLists[i]);
		s.append("    ]\n");

		s.append("]");

		return s.toString();
	}

	public void setCorporateAccountRepCode(String corporateAccountRepCode) {
		this.corporateAccountRepCode = corporateAccountRepCode;
	}

	@Override
	public String getCorporateAccountRepCode() {
		return corporateAccountRepCode;
	}

	/**
	 *
	 * @param invoiceSuppressionLevel
	 *
	 * @deprecated Use getInvoiceProperties().setInvoiceSuppressionLevel(invoiceSuppressionLevel) instead.
	 */
	@Deprecated
	public void setInvoiceSuppressionLevel(String invoiceSuppressionLevel) {
		getInvoiceProperties().setInvoiceSuppressionLevel(invoiceSuppressionLevel);
	}

	/**
	 *
	 * @return String
	 *
	 * @deprecated Use getInvoiceProperties().getInvoiceSuppressionLevel() instead.
	 */
	@Deprecated
	public String getInvoiceSuppressionLevel() {
		return getInvoiceProperties().getInvoiceSuppressionLevel();
	}

	@Override
	public Memo[] getMemos(int count) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public FollowUp[] getFollowUps(int count) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public ConsumerName[] getAuthorizedNames() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public void saveAuthorizedNames(ConsumerName[] authorizedNames) throws
	UnknownBANException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public DepositHistory[] getDepositHistory(Date from, Date to) throws
	TelusAPIException, HistorySearchException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public RefundHistory[] getRefundHistory(Date from, Date to) throws
	TelusAPIException, HistorySearchException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public SearchResults getPendingChargeHistory(Date from, Date to, char level, String subscriberId, int maximum) throws
	TelusAPIException, HistorySearchException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public SearchResults getCredits (Date from, Date to, String billState, char level, String subscriberId, int maximum) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public SearchResults getCredits (Date from, Date to, String billState, char level, String subscriberId, String knowbilityOperatorId, int maximum) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public String getCorporateId() {
		return corporateId;
	}

	public void setCorporateId(String corporateId) {
		this.corporateId = corporateId;
	}

	public void setPSTExemptExpiryDate(Date pstExemptExpiryDate) {
		this.pstExemptExpiryDate = pstExemptExpiryDate;
	}

	public void setHSTExemptExpiryDate(Date hstExemptExpiryDate) {
		this.hstExemptExpiryDate = hstExemptExpiryDate;
	}

	public void setGSTExemptExpiryDate(Date gstExemptExpiryDate) {
		this.gstExemptExpiryDate = gstExemptExpiryDate;
	}

	@Override
	public void setHomeProvince(String homeProvince) {
		this.homeProvince = homeProvince;
	}

	public void setNextBillCycle(int nextBillCycle) {
		this.nextBillCycle = nextBillCycle;
	}

	public void setNextBillCycleCloseDay(int nextBillCycleCloseDay) {
		this.nextBillCycleCloseDay = nextBillCycleCloseDay;
	}

	public void setHandledBySubscriberOnly(boolean handledBySubscriberOnly) {
		this.handledBySubscriberOnly = handledBySubscriberOnly;
	}

	@Override
	public boolean isHandledBySubscriberOnly() {
		return handledBySubscriberOnly;
	}

	@Override
	public ConsumerName getContactName() {
		return contactName;
	}

	public ConsumerNameInfo getContactName0() {
		return contactName;
	}

	@Override
	public String getContactPhone() {
		return contactPhone;
	}

	@Override
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	@Override
	public String getContactPhoneExtension() {
		return contactPhoneExtension;
	}

	@Override
	public void setContactPhoneExtension(String contactPhoneExtension) {
		this.contactPhoneExtension = contactPhoneExtension;
	}

	public String getContactFax() {
		return contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}

	@Override
	public String getOtherPhoneType() {
		return otherPhoneType;
	}

	@Override
	public void setOtherPhoneType(String newOtherPhoneType) {
		this.otherPhoneType = toUpperCase(newOtherPhoneType);
	}

	@Override
	public String getOtherPhone() {
		return otherPhone;
	}

	@Override
	public void setOtherPhone(String otherPhone) {
		this.otherPhone = otherPhone;
	}

	@Override
	public String getOtherPhoneExtension() {
		return otherPhoneExtension;
	}

	@Override
	public void setOtherPhoneExtension(String otherPhoneExtension) {
		this.otherPhoneExtension = otherPhoneExtension;
	}

	@Override
	public void cancel(Date activityDate, String reason, char depositReturnMethod, String waiver, String memoText) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	

	/**
	 * NO-OP
	 */
	 @Override
	public void cancel(String reason, char depositReturnMethod, String waiver) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 /**
	  * NO-OP
	  */
	 @Override
	public void cancel(String reason, char depositReturnMethod) throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }


	 @Override
	public void suspend(Date activityDate, String reason, String memoText) throws TelusAPIException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 /**
	  * NO-OP
	  */
	 @Override
	public void suspend(String reason) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public Charge[] getBilledCharges(int billSeqNo) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public Charge[] getBilledCharges(int billSeqNo, String phoneNumber) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public Charge[] getBilledCharges(int billSeqNo, Date from, Date to) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public Charge[] getBilledCharges(int billSeqNo, String phoneNumber, Date from, Date to) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public CancellationPenalty getCancellationPenalty() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public String[] getClientConsentIndicatorCodes() {
		 return clientConsentIndicatorCodes;
	 }

	 public void setClientConsentIndicatorCodes(String[] clientConsentIndicatorCodes) {
		 this.clientConsentIndicatorCodes = clientConsentIndicatorCodes;
	 }

	 @Override
	public ClientConsentIndicator[] getClientConsentIndicators() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public void setClientConsentIndicators(ClientConsentIndicator[] clientConsentIndicators) throws TelusAPIException {
		 if (clientConsentIndicators == null) {
			 clientConsentIndicators = new ClientConsentIndicator[0];
		 }

		 String[] codes = new String[clientConsentIndicators.length];
		 for (int i = 0; i < clientConsentIndicators.length; i++) {
			 codes[i] = clientConsentIndicators[i].getCode();
		 }

		 this.clientConsentIndicatorCodes = codes;
	 }


	 public Date getGSTExemptEffectiveDate() {
		 return gstExemptEffectiveDate;
	 }

	 public void setGSTExemptEffectiveDate(Date GSTExemptEffectiveDate){
		 this.gstExemptEffectiveDate = GSTExemptEffectiveDate;
	 }

	 public Date getPSTExemptEffectiveDate() {
		 return pstExemptEffectiveDate;
	 }

	 public void setPSTExemptEffectiveDate(Date PSTExemptEffectiveDate){
		 this.pstExemptEffectiveDate = PSTExemptEffectiveDate;
	 }

	 public Date getHSTExemptEffectiveDate() {
		 return hstExemptEffectiveDate;
	 }

	 public void setHSTExemptEffectiveDate(Date HSTExemptEffectiveDate){
		 this.hstExemptEffectiveDate = HSTExemptEffectiveDate;
	 }

	 public String getGSTCertificateNumber() {
		 return gstCertificateNumber;
	 }

	 public void setGSTCertificateNumber(String GSTCertificateNumber){
		 this.gstCertificateNumber = GSTCertificateNumber;
	 }

	 public String getPSTCertificateNumber() {
		 return pstCertificateNumber;
	 }

	 public void setPSTCertificateNumber(String PSTCertificateNumber){
		 this.pstCertificateNumber = PSTCertificateNumber;
	 }

	 public String getHSTCertificateNumber() {
		 return hstCertificateNumber;
	 }

	 public void setHSTCertificateNumber(String HSTCertificateNumber){
		 this.hstCertificateNumber = HSTCertificateNumber;
	 }
 
	 @Override
	public InternationalServiceEligibilityCheckResult checkInternationalServiceEligibility() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");	 
	 }
	 
	 /**
	  * @deprecated
	  */
	 @Deprecated
	@Override
	public double getInternationalDialingDepositAmount() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public Date getStatusDate() {
		 return statusDate;
	 }
	 public void setStatusDate(Date statusDate) {
		 this.statusDate = statusDate;
	 }

//	 @Override
//	public LMSLetterRequest newLMSLetterRequest(Letter letter) throws TelusAPIException {
//		 throw new UnsupportedOperationException("Method not implemented here");
//	 }
//
//	 @Override
//	public SearchResults getLMSLetterRequests(Date from, Date to, char level, String subscriberId, int maximum) throws TelusAPIException {
//		 throw new UnsupportedOperationException("Method not implemented here");
//	 }

	 @Override
	public Account createDuplicateAccount() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public FutureStatusChangeRequest[] getFutureStatusChangeRequests() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public SubscriptionRoleType[] getValidSubscriptionRoleTypes() throws TelusAPIException{
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public SearchResults getCredits(Date from, Date to, String billState, String reasonCode, char level, String subscriberId, int maximum) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public FollowUpStatistics getFollowUpStatistics() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public FollowUpStatistics getFollowUpStatistics(boolean refresh) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public FollowUpStatisticsInfo getFollowUpStatistics0() {
		 return followUpStatisticsInfo;
	 }

	 public void setFollowUpStatistics0(FollowUpStatisticsInfo followUpStatisticsInfo) {
		 this.followUpStatisticsInfo = followUpStatisticsInfo;
	 }

	 public InvoiceProperties getInvoiceProperties() {
		 if (invoiceProperties == null)
			 invoiceProperties = new InvoicePropertiesInfo();

		 return invoiceProperties;
	 }

	 public void setInvoiceProperties(InvoiceProperties invoiceProperties) {
		 this.invoiceProperties = invoiceProperties;
	 }

	 @Override
	public boolean validTargetAccountType(char accountType, char accountSubType) {
		 return
		 //SmartDesktop CR40, Aug 2008 release: BAN type change: corporate <=> business ban type change
		 (accountType==AccountSummary.ACCOUNT_TYPE_BUSINESS && getAccountType()==AccountSummary.ACCOUNT_TYPE_CORPORATE) 
		 || (accountType==AccountSummary.ACCOUNT_TYPE_CORPORATE && getAccountType()==AccountSummary.ACCOUNT_TYPE_BUSINESS)
		 
		 ||
		 (((getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_PERSONAL)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PCS_PERSONAL)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PCS_PERSONAL)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_PERSONAL)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_REGULAR_MEDIUM)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_REGULAR_MEDIUM)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_REGULAR_MEDIUM)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_REGULAR_MEDIUM)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CORPORATE && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CORPORATE && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CORPORATE && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_CORP_RESELLER)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CORPORATE && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_RESELLER)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CORPORATE && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_INDIVIDUAL)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_EMPLOYEE)
		 )
		 ||
		 ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CORPORATE && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_EMPLOYEE)
				 &&
				 (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_INDIVIDUAL)
		 )
		 );
	 }

	 /**
	  * NO-OP
	  */
	 @Override
	public void sendFax(final int form, String faxNumber, String language) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 /**
	  * NO-OP
	  */
	 @Override
	public void sendEmail(final int form, String email, String language) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 /**
	  * NO-OP
	  */
	 @Override
	public void removeFleet(Fleet fleet) throws InvalidFleetException, TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public ProductSubscriberList[] getProductSubscriberLists() {
		 return productSubscriberLists;
	 }

	 public void setProductSubscriberLists(ProductSubscriberListInfo[] productSubscriberLists) {
		 this.productSubscriberLists = productSubscriberLists;
	 }

	 @Override
	public Discount newDiscount() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public Discount[] getDiscounts() throws TelusAPIException
	 {
		 throw new java.lang.UnsupportedOperationException(
		 "Method not implemented here");
	 }

	 public DiscountPlan[] getAvailableDiscountPlans() throws TelusAPIException
	 {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public void changeCreditCheckDeposits(CreditCheckResultDeposit[] creditCheckResultDeposits, CreditCheckDepositChangeReason reasonCode, String reasonText) throws TelusAPIException{
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 public MigrationRequest newMigration(MigrationType migrationType, Subscriber subscriber, Equipment  newEquipment) {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 @Override
	public Credit[] getCreditByFollowUpId(int followUpId) throws TelusAPIException{
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 @Override
	public CancellationPenalty[] getCancellationPenaltyList(String[] subscriberId) throws TelusAPIException,InvalidMultiSubscriberOperationException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 @Override
	public void cancelSubscribers(Date effectiveDate, String reason,char depMethod, String[] subscriberId, String[] waiverReason,String comment) throws TelusAPIException,InvalidMultiSubscriberOperationException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 @Override
	public void suspendSubscribers(Date effectiveDate, String reason, String[] subscriberId, String comment) throws TelusAPIException,InvalidMultiSubscriberOperationException {
		 throw new UnsupportedOperationException("method not implemented here");
	 }

	 @Override
	public void restoreSuspendedSubscribers(Date effectiveDate, String reason,String[] subscriberId, String comment) throws TelusAPIException,InvalidMultiSubscriberOperationException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public String getHotlinedSubscriberPhoneNumber() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public PricePlanSubscriberCount[] getAirtimeMinutePoolingEnabledPricePlanSubscriberCounts()throws TelusAPIException{
		 throw new UnsupportedOperationException("Method not implemented here");
	 }
	 
	 @Override
	public PricePlanSubscriberCount[] getLDMinutePoolingEnabledPricePlanSubscriberCounts()throws TelusAPIException{
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public PoolingPricePlanSubscriberCount[] getPoolingEnabledPricePlanSubscriberCount(boolean refresh) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public PoolingPricePlanSubscriberCount getPoolingEnabledPricePlanSubscriberCount(int poolingGroupId, boolean refresh) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }
	 
	 public PoolingPricePlanSubscriberCount getZeroMinutePoolingEnabledPricePlanSubscriberCount(int poolingGroupId, boolean refresh) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public PricePlanSubscriberCount[] getDollarPoolingPricePlanSubscriberCount(boolean refresh) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public void refreshPricePlanSubscriberCounts() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }	 
	 
	 @Override
	public DepositAssessedHistory[] getDepositAssessedHistory() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }
	 
	 @Override
	public DepositAssessedHistory[] getOriginalDepositAssessedHistory() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }
	 
	 @Override
	public ActivationOption[] getActivationOptions() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }
	 
	 @Override
	public PortRequest[]  getPortRequests()throws PortRequestException, PRMSystemException, TelusAPIException{
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public String getCorporateName() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	@Override
	public boolean isPostpaidCorporatePersonal() {
		return isPostpaid() && (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE)
				&& (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_INDIVIDUAL || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_EMPLOYEE);
	}

	@Override
	public boolean isPostpaidCorporateRegular() {
		return isPostpaid() && (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE)
				&& ((isCorporatePCS() && !isPostpaidCorporatePersonal()) || isCorporateIDEN() || isAutotel() || isCorporateReseller());
	}

	public boolean isCorporatePCS() {
		if (isPCS() && accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE) {
			for (int i = 0; i < accountSubtypeCorporatePCS.length; i++) {
				if (accountSubType == accountSubtypeCorporatePCS[i]) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isCorporateIDEN() {
		if (isIDEN() && accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE) {
			for (int i = 0; i < accountSubtypeCorporateIden.length; i++) {
				if (accountSubType == accountSubtypeCorporateIden[i]) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isCorporateEmployee() {
		return accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_EMPLOYEE);
	}

	 public PaymentNotification reportPayment (double amount) throws IneligiblePaymentNotificationEligibilityException,TelusAPIException{
		 throw new UnsupportedOperationException("Method not implemented here");   
	 }

	 @Override
	public int getBrandId() {
		 return brandId;
	 }

	 @Override
	public boolean isAmpd() {
		 return isBrand(Brand.BRAND_ID_AMPD);
	 }

	 @Override
	public void setBrandId(int brandId) {
		 this.brandId = brandId;
	 }

	 @Override
	public void updateBrand(int brandId, String memoText) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public void save(boolean saveCreditCheckInfo) throws TelusAPIException, InvalidCreditCardException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public char getBillingNameFormat() {
		 return billingNameFormat;
	 }

	 public void setBillingNameFormat(char billingNameFormat) {
		 this.billingNameFormat = billingNameFormat;
	 }

	 @Override
	public String getPortProtectionIndicator() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");

	 }

	 @Override
	public void updatePortRestriction(boolean restrictPort) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");

	 }

	 @Override
	public String getBanSegment () {
		 return banSegment;
	 }

	 @Override
	public String getBanSubSegment ()  {
		 return banSubSegment;
	 }

	 @Override
	public String setBanSegment (String segment) {
		 banSegment = segment;
		 return banSegment;
	 }

	 @Override
	public String setBanSubSegment (String subsegment) {
		 banSubSegment = subsegment;
		 return banSubSegment;
	 }

	 @Override
	public void applyPayment(double amount, Cheque cheque, String sourceID, String sourceType) throws TelusAPIException, PaymentFailedException, UnknownObjectException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }
	 
	 @Override
	public  boolean isFeatureCategoryExistOnSubscribers(String categoryCode) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public int getOriginalBanId() {
		 return originalBanId;
	 }

	 public void setOriginalBanId(int originalBanId) {
		 this.originalBanId = originalBanId;
	 }

	 @Override
	public BillNotificationContact[] getBillNotificationContacts() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 public void saveBillNotificationDetails(long portalUserID, BillNotificationContact[] billNotificationContact) throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

	 @Override
	public boolean isBrand(int brandId) {
		 return brandId == getBrandId();
	 }

	 @Override
	public RewardRedemptionResult checkRewardRedemptionEligibility() throws TelusAPIException {
		 throw new UnsupportedOperationException("Method not implemented here");
	 }

    public boolean isInternalUse() {
        return internalUse;
    }

    public void setInternalUse(boolean internalUse) {
        this.internalUse = internalUse;
    }
    
    public EBillRegistrationReminder getLastEBillRegistrationReminderSent() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public boolean isEBillRegistrationReminderExist() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public BillNotificationContact getLastEBillNotificationSent() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public String getPaperBillSupressionAtActivationInd() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public void saveEBillRegistrationReminder(Subscriber subscriber, String recipientPhoneNumber) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public void cancel(Date activityDate, String reason,
			char depositReturnMethod, String waiver, String memoText,
			ServiceRequestHeader header) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
		
	}

	@Override
	public void cancelSubscribers(Date effectiveDate, String reason,
			char depMethod, String[] subscriberIds, String[] waiverReason,
			String comment, ServiceRequestHeader header)
			throws TelusAPIException, InvalidMultiSubscriberOperationException {
		throw new UnsupportedOperationException("Method not implemented here");
		
	}

	@Override
	public void save( ServiceRequestHeader header)
			throws TelusAPIException, InvalidCreditCardException {
		throw new UnsupportedOperationException("Method not implemented here");
		
	}

	public boolean isAccountTypeChanged() {
		return isAccountTypeChanged;
	}

	public void setAccountTypeChanged(boolean isAccountTypeChanged) {
		this.isAccountTypeChanged = isAccountTypeChanged;
	}

	public char getOldAccountType() {
		return oldAccountType;
	}

	public void setOldAccountType(char oldAccountType) {
		this.oldAccountType = oldAccountType;
	}

	public char getOldAccountSubType() {
		return oldAccountSubType;
	}

	public void setOldAccountSubType(char oldAccountSubTye) {
		this.oldAccountSubType = oldAccountSubTye;
	}
	
	@Override
	public ManualCreditCheckRequest getManualCreditCheckRequest() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public PCSSubscriber newPCSSubscriber(String serialNumber,
			String associatedHandsetIMEI, boolean dealerHasDeposit,
			String activationFeeChargeCode, String voidMailLanguage)
			throws UnknownSerialNumberException, SerialNumberInUseException,
			InvalidSerialNumberException, UnsupportedEquipmentException,
			TelusAPIException {
		return null;
	}

	@Override
	public int getHierarchyId() {
		return hierarchyId;
	}
	public void setHierarchyId( int hierarchyId ) {
		this.hierarchyId = hierarchyId;
	}

	@Override
	public boolean isBelongToHierarchy() {
		return getHierarchyId()>0 && getAccountType()==AccountSummary.ACCOUNT_TYPE_CORPORATE;
	}

	@Override
	public int getNoOfInvoice() {
		return billParamsInfo.getNoOfInvoice();
	}


	/**
	 * Note: If this setter is invoked upon account retrieval. The setNoOfInvoiceChanged flag should be moved to TMAccount, or
	 * setNoOfInvoiceChanged(false) should be called explicitly after setNoOfInvoice.
	 * Valid range of noOfInvoice must be >= 1 and < 100
	 */
	@Override
	public void setNoOfInvoice(int noOfInvoice) {
		if (noOfInvoice > 0 && noOfInvoice < 100) {
			this.billParamsInfo.setNoOfInvoice((short)noOfInvoice);
			setNoOfInvoiceChanged(true);
		}
	}

	public void setNoOfInvoiceChanged(boolean flag) {
		this.noOfInvoiceChanged= flag;
	}
	public boolean isNoOfInvoiceChanged() {
		return (noOfInvoiceChanged && getNoOfInvoice() > 0);
	}
	@Override
	public boolean hasHSPASubscriberInBAN() throws TelusAPIException{
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	public BillParametersInfo getBillParamsInfo() {
		return billParamsInfo;
	}
	
	public void setBillParamsInfo (BillParametersInfo billParamsInfo) {
		this.billParamsInfo = billParamsInfo;
	}
	
	public void setActiveSubscribersCount(int activeSubscriberCount) {
		this.activeSubscriberCount = activeSubscriberCount;
	}
	
	public void setSuspendedSubscribersCount(int suspendedSubscriberCount) {
		this.suspendedSubscriberCount = suspendedSubscriberCount;
	}
	
	public void setReservedSubscribersCount(int reservedSubscriberCount) {
		this.reservedSubscriberCount = reservedSubscriberCount;
	}
	
	public void setCancelledSubscribersCount(int cancelledSubscriberCount) {
		this.cancelledSubscriberCount = cancelledSubscriberCount;
	}
	
	public void setAllActiveSubscribersCount(int allActiveSubscriberCount) {
		this.allActiveSubscriberCount = allActiveSubscriberCount;
	}
	
	public void setAllSuspendedSubscribersCount(int allSuspendedSubscriberCount) {
		this.allSuspendedSubscriberCount = allSuspendedSubscriberCount;
	}
	
	public void setAllReservedSubscribersCount(int allReservedSubscriberCount) {
		this.allReservedSubscriberCount = allReservedSubscriberCount;
	}
	
	public void setAllCancelledSubscribersCount(int allCancelledSubscriberCount) {
		this.allCancelledSubscriberCount = allCancelledSubscriberCount;
	}

	public boolean hasEPostSubscription() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public Boolean isEPostSubscription() {
		return ePostSubscription;
	}

	public void setEPostSubscription(boolean hasEPostSubscription) throws TelusAPIException {
		this.ePostSubscription = new Boolean(hasEPostSubscription);
	}

	public boolean isForceZeroBalanceInd() {
		return forceZeroBalanceInd;
	}

	public void setForceZeroBalanceInd(boolean forceZeroBalanceInd) {
		this.forceZeroBalanceInd = forceZeroBalanceInd;
	}

	public BillNotificationHistoryRecord[] getBillNotificationHistory(String subscriptionType) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public LightWeightSubscriber[] getLightWeightSubscribers(int maxium, boolean includeCancelled ) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public PCSSubscriber newPCSSubscriber(String phoneNumber,
			String serialNumber, boolean dealerHasDeposit,
			String voiceMailLanguage) throws UnknownSerialNumberException,
			SerialNumberInUseException, InvalidSerialNumberException,
			TelusAPIException {
		return null;
	}

	@Override
	public PCSSubscriber newPCSSubscriber(String phoneNumber,
			String serialNumber, String associatedHandsetIMEI,
			boolean dealerHasDeposit, String activationFeeChargeCode,
			String voiceMailLanguage) throws UnknownSerialNumberException,
			SerialNumberInUseException, InvalidSerialNumberException,
			UnsupportedEquipmentException, TelusAPIException {

		return null;
	}

	@Override
	public PCSSubscriber newPCSSubscriber(String phoneNumber,
			String serialNumber, String[] secondarySerialNumbers,
			boolean dealerHasDeposit, String activationFeeChargeCode,
			String voiceMailLanguage) throws UnknownSerialNumberException,
			SerialNumberInUseException, InvalidSerialNumberException,
			TelusAPIException {

		return null;
	}

	public double getRequiredPaymentForRestoral() throws TelusAPIException{
		
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public Charge[] getCharges(String[] chargeCodes, String billState, char level, String subscriberId, Date from, Date to, int maximum) throws LimitExceededException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	//Implements PostpaidAccount#getUnpaidAirtimeTotal() here
	public 	double getUnpaidAirtimeTotal() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public String[] getSubscriberIdsByServiceGroupFamily(String familyType,Date effectiveDate )
			throws LimitExceededException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public boolean getTransientNotificationSuppressionInd() {
		throw new UnsupportedOperationException("Method is not implemented here");
	}

	@Override
	public void setTransientNotificationSuppressionInd(
			boolean transientNotificaitonSuppressionInd) {
		throw new UnsupportedOperationException("Method is not implemented here");
		
	}

	@Override
	public Subscriber getSubscriberByPhoneNumber(String phoneNumber,
			PhoneNumberSearchOption phoneNumberSearchOption)
			throws UnknownSubscriberException, TelusAPIException {
		throw new UnsupportedOperationException("Method is not implemented here");
	}

	@Override
	public PCSSubscriber newPCSBCSubscriber(String seatType,
			String phoneNumber, String serialNumber, boolean dealerHasDeposit,
			String activationFeeChargeCode, String voiceMailLanguage,
			String associatedHandsetIMEI) throws UnknownSerialNumberException,
			SerialNumberInUseException, InvalidSerialNumberException,
			TelusAPIException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
