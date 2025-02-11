package com.telus.provider.account;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.api.InvalidMigrationRequestException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.ActivationTopUp;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.Contract;
import com.telus.api.account.CreditCard;
import com.telus.api.account.EquipmentChangeRequest;
import com.telus.api.account.MigrationRequest;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.equipment.SIMCardEquipment;
import com.telus.api.fleet.MemberIdentity;
import com.telus.api.portability.PRMSystemException;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestException;
import com.telus.api.reference.AccountType;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.Brand;
import com.telus.api.reference.MigrationType;
import com.telus.api.reference.PricePlan;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.PCSSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.AccountTypeInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.equipment.TMUSIMCardEquipment;
import com.telus.provider.fleet.TMMemberIdentity;
import com.telus.provider.portability.TMPortRequest;
import com.telus.provider.portability.TMPortRequestSO;
import com.telus.provider.util.Logger;

public abstract class TMMigrationRequest implements MigrationRequest {

	protected final TMProvider provider;
	protected final transient Subscriber currentSubscriber;
	protected final transient Subscriber newSubscriber;
	protected final transient TMAccount newAccount;
	protected final transient Equipment equipment;
	//protected   boolean isNewEquipment = true;
	protected transient Contract newContract;
	protected final MigrationRequestInfo migrationRequestInfo;
	protected transient TMCreditCard creditCard;
	protected transient String portRequestId;
	private transient TMPortRequestSO portRequestSO;

	public TMMigrationRequest(TMProvider provider, MigrationType migrationType, Subscriber currentSubscriber, TMAccount newAccount, Equipment equipment, String pricePlanCode)
			throws UnknownBANException, TelusAPIException {

		SubscriberInfo info = null;
		this.provider = provider;
		this.currentSubscriber = currentSubscriber;
		this.newAccount = newAccount;
		this.equipment = equipment;
		((TMAccount) newAccount).assertAccountExists();

		migrationRequestInfo = new MigrationRequestInfo(migrationType);

		setMigrationReasonCode(migrationType.getCode());

		Date activityDate = provider.getReferenceDataManager().getLogicalDate();

		PricePlan pricePlan = provider.getReferenceDataManager().getPricePlan(pricePlanCode, provider.getEquipmentManager0().translateEquipmentType(equipment),
				currentSubscriber.getNumberGroup().getProvinceCode(), newAccount.getAccountType(), newAccount.getAccountSubType(), newAccount.getBrandId());

		if (migrationRequestInfo.isToPCS()) {
			info = new PCSSubscriberInfo();
		} else if (migrationRequestInfo.isToMike()) {
			info = new IDENSubscriberInfo();
		}
		info.setBanId(newAccount.getBanId());
		info.setProductType(equipment.getProductType());
		info.setEquipmentType(equipment.getEquipmentType());
		info.setDealerCode(newAccount.getDealerCode());
		info.setSalesRepId(newAccount.getSalesRepCode());
		info.setLanguage(currentSubscriber.getLanguage());
		info.setNumberGroup(currentSubscriber.getNumberGroup());
		info.setPhoneNumber(currentSubscriber.getPhoneNumber());
		info.setStartServiceDate(activityDate);
		info.setBirthDate(currentSubscriber.getBirthDate());
		info.setSerialNumber(equipment.getSerialNumber());

		if (migrationRequestInfo.isP2P()) {
			newSubscriber = new TMPCSSubscriber(provider, (PCSSubscriberInfo) info, true, null, newAccount, false, equipment);
			if (currentSubscriber.getEquipment().getSerialNumber().equals(equipment.getSerialNumber())) {
				newContract = ((TMPCSSubscriber) currentSubscriber).newContract(pricePlan, PricePlan.CONTRACT_TERM_ALL, false, newAccount);
			} else {
				((TMPCSSubscriber) currentSubscriber).setMigration(true);
				//OOM newMigrationRequest (CR : CPMS/KB Original Dealer of record - Defect# 9850)
				AccountType accType = getAccountTypeDefaults((Account) newAccount);
				EquipmentChangeRequest equipmentChangeRequest = currentSubscriber.newEquipmentChangeRequest(equipment, accType.getDefaultDealer(), accType.getDefaultSalesCode(), provider.getUser(),
						null, Subscriber.SWAP_TYPE_REPLACEMENT, false);
				//End modification
				newContract = ((TMPCSSubscriber) currentSubscriber).newContract(pricePlan, PricePlan.CONTRACT_TERM_ALL, false, equipmentChangeRequest, newAccount);
			}
		} else if (migrationRequestInfo.isM2P()) {
			newSubscriber = new TMPCSSubscriber(provider, (PCSSubscriberInfo) info, true, null, newAccount, false, equipment);
			newContract = ((TMPCSSubscriber) newSubscriber).newContract(pricePlan, PricePlan.CONTRACT_TERM_ALL, false, newAccount);
		} else if (migrationRequestInfo.isP2M()) {
			newSubscriber = new TMIDENSubscriber(provider, (IDENSubscriberInfo) info, true, null, newAccount, false, equipment);
			newContract = ((TMIDENSubscriber) newSubscriber).newContract(pricePlan, PricePlan.CONTRACT_TERM_ALL, false, newAccount);
		} else {
			throw new RuntimeException("Subscriber not supported for Migration[" + migrationRequestInfo.getMigrationType() + "]");
		}

		((TMContract) newContract).setMigrationSubscriber((TMSubscriber) currentSubscriber, (TMSubscriber) newSubscriber);
	}

	protected void populateNewSubNumberGroup() throws TelusAPIException {
		AvailablePhoneNumber apn = provider.getReferenceDataManager().getAvailablePhoneNumber(currentSubscriber.getPhoneNumber(), equipment.getProductType(), getDealerCode());
		if (apn != null && apn.getNumberGroup() != null && apn.getNumberGroup().getCode() != null) {
			((TMSubscriber) newSubscriber).getDelegate().setNumberGroup(apn.getNumberGroup());
		} else {
			throw new TelusAPIException("Unable to retrieve NumberGroup information for phone:" + currentSubscriber.getPhoneNumber() + "; could be data related.");
		}
	}

	public void validateMandatoryFields() throws InvalidMigrationRequestException, TelusAPIException {
		
		if (getMigrationReasonCode() == null) {
			throw new InvalidMigrationRequestException("Mandatory field missing [MigrationReasonCode].", InvalidMigrationRequestException.REASON_INVALID_MANDATORY_FIELDS);
		}
		if (getRequestorId() == null) {
			throw new InvalidMigrationRequestException("Mandatory field missing [RequestorId].", InvalidMigrationRequestException.REASON_INVALID_MANDATORY_FIELDS);
		}
		if (equipment.isStolen()) {
			throw new InvalidMigrationRequestException("Equipment is stolen.", InvalidMigrationRequestException.REASON_INVALID_EQUIPMENT_TYPE);
		}
		if (!equipment.getSerialNumber().equals(currentSubscriber.getEquipment().getSerialNumber())) {
			if (equipment.isInUse()) {
				throw new InvalidMigrationRequestException("Equipment is already in use.", InvalidMigrationRequestException.REASON_INVALID_EQUIPMENT_TYPE);
			}
		}
		if (newAccount.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS) && !equipment.isCellular()) {
			throw new InvalidMigrationRequestException("Invalid equipment type.", InvalidMigrationRequestException.REASON_INVALID_EQUIPMENT_TYPE);
		}
	}

	public Equipment getNewEquipment() {
		return equipment;
	}

	public Account getNewAccount() {
		return newAccount;
	}

	public Contract getNewContract() throws TelusAPIException {
		return newContract;
	}

	public static final TMMigrationRequest newMigrationRequest(TMProvider provider, Subscriber currentSubscriber, TMAccount newAccount, Equipment equipment, String pricePlanCode)
			throws UnknownBANException, TelusAPIException {
		return newMigrationRequest(provider, currentSubscriber, newAccount, equipment, null, pricePlanCode);
	}

	@Deprecated
	public static final TMMigrationRequest newMigrationRequest(TMProvider provider, Subscriber currentSubscriber, TMAccount newAccount, Equipment equipment, MuleEquipment muleEquipment,
			String pricePlanCode) throws UnknownBANException, TelusAPIException {

		if (equipment == null) {
			equipment = currentSubscriber.getEquipment();
		} else {
			if (equipment.isSIMCard() && muleEquipment != null) { //most likely this is ToMiKE Migration specific
				((SIMCardEquipment) equipment).setLastMule(muleEquipment);
			}
		}

		if (currentSubscriber.isPCS()) {
			if (currentSubscriber.getAccount().isPrepaidConsumer()) { //from PCS prepaid
				if (newAccount.isPCS() && newAccount.isPostpaid()) { //to PCS postpaid
					return new TMPCSPrePaidToPCSPostPaidMigrationRequest(provider, provider.getReferenceDataManager().getMigrationType(MigrationType.PCS_PRE_TO_PCSPOST), currentSubscriber, newAccount,
							equipment, pricePlanCode);
				} else if (newAccount.isIDEN()) { //to mike
					return new TMPCSPrePaidToMikeMigrationRequest(provider, provider.getReferenceDataManager().getMigrationType(MigrationType.PCS_PRE_TO_IDEN), currentSubscriber, newAccount,
							equipment, pricePlanCode);
				}
			} else if (currentSubscriber.getAccount().isPostpaid()) { //from PCS postpaid
				if (newAccount.isPrepaidConsumer()) { //prepaid
					return new TMPCSPostPaidToPrePaidMigrationRequest(provider, provider.getReferenceDataManager().getMigrationType(MigrationType.PCS_POST_TO_PCSPRE), currentSubscriber, newAccount,
							equipment, pricePlanCode);
				} else if (newAccount.isIDEN()) { //to mike
					return new TMPCSPostPaidToMikeMigrationRequest(provider, provider.getReferenceDataManager().getMigrationType(MigrationType.PCS_POST_TO_IDEN), currentSubscriber, newAccount,
							equipment, pricePlanCode);
				}
			}
		} else if (currentSubscriber.isIDEN() && newAccount.isPCS()) { //from mike to PCS
			if (newAccount.isPostpaid()) { //to PCS postpaid
				return new TMMikeToPCSPostMigrationRequest(provider, provider.getReferenceDataManager().getMigrationType(MigrationType.IDEN_TO_PCSPOST), currentSubscriber, newAccount, equipment,
						pricePlanCode);
			} else if (newAccount.isPrepaidConsumer()) { //to PCS prepaid
				return new TMMikeToPCSPreMigrationRequest(provider, provider.getReferenceDataManager().getMigrationType(MigrationType.IDEN_TO_PCSPRE), currentSubscriber, newAccount, equipment,
						pricePlanCode);
			}
		}
		
		throw new java.lang.UnsupportedOperationException("currently Following type of Migration not supported account type/subtype[" + currentSubscriber.getAccount().getAccountType() + "/"
				+ currentSubscriber.getAccount().getAccountSubType() + "] to [" + newAccount.getAccountType() + "/" + newAccount.getAccountSubType() + "]");
	}

	public abstract boolean testMigrationRequest() throws InvalidMigrationRequestException, TelusAPIException;

	public abstract void postMigrationTask() throws InvalidMigrationRequestException, TelusAPIException;

	public abstract void preMigrationTask() throws InvalidMigrationRequestException, TelusAPIException;

	public MigrationRequestInfo getDelegate() {
		return migrationRequestInfo;
	}

	public Subscriber getCurrentSubscriber() {
		return currentSubscriber;
	}

	public String getActivationAirtimeCardNumber() {
		return migrationRequestInfo.getActivationAirtimeCardNumber();
	}

	public CreditCard getActivationCreditCard() {
		return creditCard;
	}

	public ActivationTopUp getActivationTopUp() {
		return migrationRequestInfo.getActivationTopUp();
	}

	public double getActivationCreditAmount() {
		return migrationRequestInfo.getActivationCreditAmount();
	}

	public int getActivationType() {
		return migrationRequestInfo.getActivationType();
	}

	public String getDealerCode() {
		return migrationRequestInfo.getDealerCode();
	}

	public String getMigrationReasonCode() {
		return migrationRequestInfo.getMigrationReasonCode();
	}

	public MigrationType getMigrationType() {
		return migrationRequestInfo.getMigrationType();
	}

	public String getRequestorId() {
		return migrationRequestInfo.getRequestorId();
	}

	public String getSalesRepCode() {
		return migrationRequestInfo.getSalesRepCode();
	}

	public String getUserMemoText() {
		return migrationRequestInfo.getUserMemoText();
	}

	public boolean isDealerAccepteddeposit() {
		return migrationRequestInfo.isDealerAccepteddeposit();
	}

	public void setActivationAirtimeCardNumber(String activationAirtimeCardNumber) {
		migrationRequestInfo.setActivationAirtimeCardNumber(activationAirtimeCardNumber);
	}

	public void setActivationCreditCard(CreditCard activationCreditCard, AuditHeader auditHeader) throws TelusAPIException {
		if (auditHeader == null) {
			throw new TelusAPIException("The required AuditHeader is missing");
		}
		creditCard = (TMCreditCard) activationCreditCard;
		creditCard.setAuditHeader(provider.appendToAuditHeader(auditHeader));
		migrationRequestInfo.setActivationCreditCard(creditCard.getDelegate());
	}

	public void setActivationTopUp(ActivationTopUp activationTopUp) {
		migrationRequestInfo.setActivationTopUp(activationTopUp);
	}

	public void setActivationCreditAmount(double activationCreditAmount) {
		migrationRequestInfo.setActivationCreditAmount(activationCreditAmount);
	}

	public void setActivationType(int activationType) {
		migrationRequestInfo.setActivationType(activationType);
	}

	public void setDealerAccepteddeposit(boolean dealerAccepteddeposit) {
		migrationRequestInfo.setDealerAccepteddeposit(dealerAccepteddeposit);
	}

	public void setDealerCode(String dealerCode) {
		migrationRequestInfo.setDealerCode(dealerCode);
	}

	public void setMigrationReasonCode(String migrationReasonCode) {
		migrationRequestInfo.setMigrationReasonCode(migrationReasonCode);
	}

	public void setRequestorId(String requestorId) {
		migrationRequestInfo.setRequestorId(requestorId);
	}

	public void setSalesRepCode(String salesRepCode) {
		migrationRequestInfo.setSalesRepCode(salesRepCode);
	}

	public void setUserMemoText(String userMemoText) {
		migrationRequestInfo.setUserMemoText(userMemoText);
	}

	public boolean isDepositTransferInd() {
		return migrationRequestInfo.isDepositTransferInd();
	}

	public void setDepositTransferInd(boolean depositTransferInd) {
		migrationRequestInfo.setDepositTransferInd(depositTransferInd);
	}

	public MemberIdentity getMemeberIdentity() {
		return migrationRequestInfo.getMemeberIdentity();
	}

	public boolean isPhoneOnly() {
		return migrationRequestInfo.isPhoneOnly();
	}

	public boolean isPTNBasedFleet() {
		return migrationRequestInfo.isPTNBasedFleet();
	}

	public void setMemberIdentity(MemberIdentity memberIdentity) {
		migrationRequestInfo.setMemberIdentity(((TMMemberIdentity) memberIdentity).getDelegate());
	}

	public void setPhoneOnly(boolean phoneOnly) {
		migrationRequestInfo.setPhoneOnly(phoneOnly);
	}

	public void setPTNBasedFleet(boolean PTNBasedFleet) {
		migrationRequestInfo.setPTNBasedFleet(PTNBasedFleet);
	}

	public Subscriber getNewSubscriber0() {
		return newSubscriber;
	}

	private TMPortRequestSO getPortRequestSO() {
		if (portRequestSO == null) {
			portRequestSO = new TMPortRequestSO(provider);
		}
		return portRequestSO;
	}

	private String migrationContext = null;

	private String getMigrationContext() {
		if (migrationContext == null) {
			migrationContext = "migrate [type:" + getMigrationType().getCode() + ", reason:" + getMigrationReasonCode() + "] for [" + currentSubscriber.getPhoneNumber() + "]";
		}
		return migrationContext;
	}

	public void createPortRequestAsNeeded() throws TelusAPIException {
		
		TMSubscriber sub = (TMSubscriber) newSubscriber;

		boolean needToSubmit = migrationRequestInfo.isM2P() || migrationRequestInfo.isP2M();
		sub.logMessage(getMigrationContext(), "needSubmit port request=" + needToSubmit, null);

		String activity = "create port request";
		if (needToSubmit) {
			try {
				PortRequest portRequest = sub.newPortRequest(sub.getPhoneNumber(), PortInEligibility.PORT_DIRECTION_INDICATOR_WIRELESS_WIRELESS, false);
				portRequest.setAgencyAuthorizationIndicator("Y");
				portRequest.setAgencyAuthorizationDate(newSubscriber.getStartServiceDate());
				portRequest.setAgencyAuthorizationName(portRequest.getPortRequestName().getFirstName() + " " + portRequest.getPortRequestName().getLastName());

				//	        portRequestId = getPortRequestSO().createMigrationPortInRequest( sub, getOldNetworkType(), getNewNetworkType() );

				portRequestId = provider.getSubscriberLifecycleFacade().createPortInRequest(sub.getDelegate(), PortInEligibility.PORT_PROCESS_MIGRATION, Brand.BRAND_ID_NOT_APPLICABLE,
						Brand.BRAND_ID_NOT_APPLICABLE, getOldNetworkType(), getNewNetworkType(), provider.getApplication(), provider.getUser(), ((TMPortRequest) sub.getPortRequest()).getDelegate());
				//make sure the reture is request id
				try {
					Long.parseLong(portRequestId);
				} catch (NumberFormatException nfe) {
					// this error message!!!!
					throw new PortRequestException(portRequestId);
				}
				sub.logSuccess(getMigrationContext(), activity, "portRequestId=" + portRequestId);
			} catch (ApplicationException ae) {
				if (!ae.getErrorCode().equals(""))
					throw new PortRequestException(ae, provider.getApplicationMessage(ApplicationSummary.APP_PRM, ae.getErrorCode(), ae));
				throw new TelusAPIException(ae);
			} catch (TelusAPIException tae) {
				sub.logFailure(getMigrationContext(), activity, tae, "portRequestId=" + portRequestId);
				throw tae;
			} catch (Throwable t) {
				sub.logFailure(getMigrationContext(), activity, t, "portRequestId=" + portRequestId);
				throw new TelusAPIException(t);
			}
		}
	}

	public void submitPortRequestAsNeeded() throws TelusAPIException {
		
		TMSubscriber sub = (TMSubscriber) newSubscriber;
		String activity = "submit port request [" + portRequestId + "]";
		boolean needToSubmit = migrationRequestInfo.isM2P() || migrationRequestInfo.isP2M();
		if (needToSubmit) {
			try {
				//      	    getPortRequestSO().submitPortInRequest(portRequestId);
				provider.getSubscriberLifecycleFacade().submitPortInRequest(portRequestId, provider.getApplication());
				sub.logSuccess(getMigrationContext(), activity, null);
			} catch (Throwable t) {
				sub.logFailure(getMigrationContext(), activity, t, null);
				throw new PRMSystemException("submit port request failed: requestId:" + portRequestId + "cause: " + t);
			}
		}
	}

	private final static String BILLING_SYSTEM_FAILED = "BSTF";

	public void cancelPortRequestAsNeeded() throws PortRequestException, TelusAPIException {
		
		TMSubscriber sub = (TMSubscriber) newSubscriber;
		String activity = "cancel port request [" + portRequestId + "]";
		boolean needToSubmit = migrationRequestInfo.isM2P() || migrationRequestInfo.isP2M();
		if (needToSubmit) {
			try {
				//getPortRequestSO().cancelPortInRequest(portRequestId, BILLING_SYSTEM_FAILED);
				provider.getSubscriberLifecycleFacade().cancelPortInRequest(portRequestId, BILLING_SYSTEM_FAILED, provider.getApplication());
				sub.logSuccess(getMigrationContext(), activity, null);
			} catch (ApplicationException ae) {
				sub.logFailure(getMigrationContext(), activity, ae, null);
			} catch (Throwable t) {
				sub.logFailure(getMigrationContext(), activity, t, null);
				throw new PRMSystemException("cancel PortRequest: falied for requestId:" + portRequestId + "cause: " + t);
			}
		}
	}

	public void setActivationOption(ActivationOption option) {
		migrationRequestInfo.setActivationOption(option);
	}

	public void setNewAssociatedHandset(Equipment newAssociatedHandset) throws InvalidMigrationRequestException {
		//defect PROD00134051 fix: add more check: only when newAssociatedHandset is not null, then we set it.
		if (newAssociatedHandset != null) {
			if (!getNewEquipment().isUSIMCard()) {
				throw new InvalidMigrationRequestException("primaryEquipment is not a USIMCard, serial(" + getNewEquipment().getSerialNumber() + ")", 
						InvalidMigrationRequestException.REASON_INVALID_EQUIPMENT_TYPE);
			}
			if (!newAssociatedHandset.isHSPA()) {
				throw new InvalidMigrationRequestException("newAssociatedHandset is not a HSPA equipment, serial( " + newAssociatedHandset.getSerialNumber() + ")",
						InvalidMigrationRequestException.REASON_INVALID_EQUIPMENT_TYPE);
			}
			((TMUSIMCardEquipment) getNewEquipment()).setLastAssociatedHandset(newAssociatedHandset);
		}
	}

	public Equipment getNewAssociatedHandset() throws TelusAPIException {
		return ((TMUSIMCardEquipment) getNewEquipment()).getLastAssociatedHandset();
	}

	private String getOldNetworkType() throws TelusAPIException {
		return getCurrentSubscriber().getEquipment().getNetworkType();
	}

	private String getNewNetworkType() throws TelusAPIException {
		return equipment.getNetworkType();
	}

	/* 
	 * OOM newMigrationRequest (CR: CPMS/KB Original Dealer of record - Defect# 9850) - Helper method
	 * Given an account object (newAccount object to migrate to) this method will retrieve the default dealer
	 * code and default salesrep code. If the default is not found(ie: return value is null), dealer/salesrep codes are copied from the
	 * object that was passed to the method
	 * 
	 * @param account 	Account object with accountType accountSubType and brandId properties
	 * @return AccountType	account type object with the default Dealer Code and Salesrep code populated
	 */
	private AccountType getAccountTypeDefaults(Account account) {
		
		AccountTypeInfo acctType = null;
		try {
			acctType = (AccountTypeInfo) provider.getReferenceDataManager().getAccountType(account);
		} catch (TelusAPIException e) {
			Logger.debug0("Unable to retrieve default dealer/salesrepcode values for account type [" + account.getAccountType() + "], subtype [" + account.getAccountSubType() + "] and brand ID ["
					+ account.getBrandId() + "].");
		}
		//If we get null for the default codes, we set the values manually
		if (acctType == null) {
			acctType = new AccountTypeInfo();
			acctType.setDefaultDealer(account.getDealerCode());
			acctType.setDefaultSalesCode(account.getSalesRepCode());
		}
		
		return acctType;
	}
	
}