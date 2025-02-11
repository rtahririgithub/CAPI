package com.telus.cmb.subscriber.bo;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.InvalidPricePlanChangeException;
import com.telus.api.InvalidServiceException;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.ContractService;
import com.telus.api.account.CreditCheckResultDeposit;
import com.telus.api.account.EquipmentChangeRequest;
import com.telus.api.account.InvalidEquipmentChangeException;
import com.telus.api.account.InvalidSerialNumberException;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.ServiceChangeHistory;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestManager;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.ServiceSummary;
import com.telus.cmb.subscriber.decorators.PricePlanDecorator;
import com.telus.cmb.subscriber.decorators.SubscriberDecorator;
import com.telus.cmb.subscriber.decorators.migration.MigrateSeatRequestDecorator;
import com.telus.cmb.subscriber.decorators.migration.MigrationRequestDecorator;
import com.telus.cmb.subscriber.utilities.BaseChangeContext;
import com.telus.cmb.subscriber.utilities.ContractChangeContext;
import com.telus.cmb.subscriber.utilities.contract.ContractUtilities;
import com.telus.cmb.subscriber.utilities.migration.ChangeEquipmentHelper;
import com.telus.cmb.subscriber.utilities.migration.MigrationUtilities;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.subscriber.info.BaseChangeInfo;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.EquipmentChangeRequestInfo;
import com.telus.eas.subscriber.info.PricePlanChangeInfo;
import com.telus.eas.subscriber.info.ServiceChangeHistoryInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceInfo;


public class SubscriberBo extends SubscriberDecorator {

	private static final Log logger = LogFactory.getLog(SubscriberBo.class);

	protected BaseChangeContext<? extends BaseChangeInfo> changeContext;
	protected AccountBo account;
	protected ContractBo contract;
	protected boolean activation = false;
	CreditCheckResultDeposit[] creditCheckDeposits = null;
	private boolean isMigration = false;
	private PortRequestInfo portRequest = null;
	
//	private CommunicationSuiteInfo commSuiteInfo;
	private boolean commSuiteCached = false;

	private ActivationOptionBo activationOption;

	public SubscriberBo(SubscriberInfo subscriber, BaseChangeContext<? extends BaseChangeInfo> changeContext) {
		super(subscriber);
		this.changeContext = changeContext;
	}

	@Override
	public EquipmentBo getEquipment() throws ApplicationException {
		return changeContext.getCurrentEquipment();
	}
	
	@Override
	public ActivationOptionBo getActivationOption() throws ApplicationException {
		return (activationOption = activationOption == null ? new ActivationOptionBo(changeContext) : activationOption);
	}

	@Override
	public AccountBo getAccount() throws ApplicationException {
		
		if (account == null) {
			if (getBanId() == changeContext.getChangeInfo().getBan()) {
				account = changeContext.getCurrentAccount();
			} else {
				AccountInfo accountInfo;
				accountInfo = changeContext.getAccountInformationHelper().retrieveAccountByBan(getBanId(), Account.ACCOUNT_LOAD_ALL);
				account = new AccountBo(accountInfo, changeContext);
			}
		}

		return account;
	}

	@Override
	public ContractBo getContract() throws ApplicationException {
		
		if (contract == null) {
			if (getSubscriberId() != null && getSubscriberId().equals(changeContext.getChangeInfo().getSubscriberId())) {
				return changeContext.getCurrentContract();
			} else {
				SubscriberContractInfo contractInfo = changeContext.getSubscriberLifecycleFacade().getServiceAgreement(getDelegate(), getAccount().getDelegate());
				contract = new ContractBo(contractInfo, changeContext);
			}
		}

		return contract;
	}

	@Override
	public ContractBo newContract(PricePlanDecorator pricePlan, int term) throws ApplicationException {
		
		try {
			if (!activation) {
				return newContract0(pricePlan, term, !changeContext.getCurrentContract().isTelephonyEnabled(), null, false);
			} else {
				return newContract0(pricePlan, term, false, null, false);
			}

		} catch (InvalidPricePlanChangeException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.PRICEPLAN_CHANGE_ERROR, e.getMessage(), "", e);
		} catch (TelusAPIException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.PRICEPLAN_CHANGE_ERROR, e.getMessage(), "", e);
		}
	}

	@Override
	public ContractBo newContract(PricePlanDecorator pricePlan, int term, EquipmentChangeRequest equipmentChangeRequest) throws ApplicationException {
		
		try {
			if (!activation) {
				return newContract0(pricePlan, term, !changeContext.getCurrentContract().isTelephonyEnabled(), equipmentChangeRequest, false);

			} else {
				return newContract0(pricePlan, term, false, equipmentChangeRequest, false);
			}
		} catch (InvalidPricePlanChangeException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.PRICEPLAN_CHANGE_ERROR, e.getMessage(), "", e);
		} catch (TelusAPIException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.PRICEPLAN_CHANGE_ERROR, e.getMessage(), "", e);
		}
	}

	private ContractBo newContract0(PricePlanDecorator pricePlan, int term, boolean dispatchOnly, EquipmentChangeRequest equipmentChangeRequest, boolean contractRenewal)
			throws InvalidPricePlanChangeException, TelusAPIException, ApplicationException {

		EquipmentBo equipment = null;
		if (equipmentChangeRequest != null && equipmentChangeRequest.getNewEquipment() != null) {
			equipment = new EquipmentBo((EquipmentInfo) equipmentChangeRequest.getNewEquipment(), this.changeContext);
		} else {
			equipment = getEquipment();
		}

		testNewContract(pricePlan, dispatchOnly, equipment, contractRenewal);
		boolean priceplanChange = !activation && !(contractRenewal && getPricePlan().equals(pricePlan.getCode()));

		SubscriberContractInfo info;
		if (contractRenewal && !priceplanChange) {
			info = changeContext.getSubscriberLifecycleFacade().getServiceAgreement(getDelegate(), getAccount().getDelegate());
		} else {
			info = new SubscriberContractInfo();
			info.setModified();
			info.setDispatchOnly(dispatchOnly);
			info.setPricePlanInfo(pricePlan.getDelegate());
		}
		info.setPricePlanChange(priceplanChange);
		ContractBo c = new ContractBo(info, changeContext);

		if (priceplanChange) {
			PricePlanInfo oldPlan = getContract().getDelegate().getPricePlan0();
			PricePlanInfo newPlan = null;

			newPlan = ((PricePlanBo) pricePlan).getDelegate();

			if (oldPlan.isXEW() && newPlan.isEW()) {

				// FE SOC should be selected from the list of
				// pricePlan.getOtionalServices()
				// and added to the Contract

				ServiceInfo[] optionalServices = newPlan.getOptionalServices0();
				if (optionalServices != null && optionalServices.length > 0) {
					for (int i = 0; i < optionalServices.length; i++) {
						ServiceInfo service = optionalServices[i];
						if (service.isFlexibleEvenings() && !c.containsService(service.getCode())) {
							c.addService(service);
						}
					}
				}
			} // end-if oldPlan.isXEW() && newPlan.isEW()
		} // end-if priceplanChange

		if (!priceplanChange && !contractRenewal) { //mainly, that means activation
			setContract(c);
		}

		// Multi-Ring
		if (delegate.getMultiRingPhoneNumbers() != null) {
			c.setMultiRingPhoneNumbers(delegate.getMultiRingPhoneNumbers());
		}

		return c;
	}

	public void setContract(ContractBo contract) {
		this.contract = contract;
		delegate.setPricePlan(contract.getPricePlan().getCode().trim());
	}

	private void testNewContract(PricePlanDecorator pricePlan, boolean dispatchOnly, EquipmentBo equipment, boolean contractRenewal)
			throws InvalidPricePlanChangeException, TelusAPIException, ApplicationException {

		boolean priceplanChange = !activation && !(contractRenewal && getPricePlan().equals(pricePlan.getCode()));

		try {
			testService(pricePlan.getDelegate(), equipment);
		} catch (InvalidServiceException e) {
			throw new InvalidPricePlanChangeException(e.getReason(), e);
		}
		
		if (!contractRenewal && priceplanChange && getPricePlan().equals(pricePlan.getCode())) {
			throw new InvalidPricePlanChangeException(InvalidPricePlanChangeException.DUPLICATE_PRICEPLAN, "new priceplan is same as old: " + pricePlan.getCode());
		}

		if (pricePlan.isSharable()) {
			if (pricePlan.getMaximumSubscriberCount() > 0) {
				PricePlanSubscriberCount count = getAccount().getShareablePricePlanSubscriberCount(pricePlan.getCode());

				if (count != null) {
					if (count.isMaximumSubscriberReached()) {
						throw new InvalidPricePlanChangeException(InvalidPricePlanChangeException.SHARED_PLAN_LIMIT, pricePlan.getCode());
					}

					if (count.getActiveSubscribers().length + count.getReservedSubscribers().length > 0) {
						String[] phoneNumber = new String[count.getActiveSubscribers().length + count.getReservedSubscribers().length];
						System.arraycopy(count.getActiveSubscribers(), 0, phoneNumber, 0, count.getActiveSubscribers().length);
						System.arraycopy(count.getReservedSubscribers(), 0, phoneNumber, count.getActiveSubscribers().length, count.getReservedSubscribers().length);

						for (int i = 0; i < phoneNumber.length; i++) {
							Subscriber subscriber = changeContext.getSubscriberLifecycleHelper().retrieveSubscriberByPhoneNumber(phoneNumber[i]);
							// TODO: I can't imagine there being 2 identical PricePlan codes for different terms.
							if (subscriber.getContract().getPricePlan().getTermMonths() != pricePlan.getTermMonths()) {
								throw new InvalidPricePlanChangeException(InvalidPricePlanChangeException.SHARED_PLAN_TERM_MISMATCH, pricePlan.getCode());
							}
						}
					}
				}
			}
		}
	}

	@Override
	public ServiceChangeHistory[] getServiceChangeHistory(Date from, Date to) {

		ServiceChangeHistoryInfo[] historyInfos = null;
		try {
			historyInfos = changeContext.getSubscriberLifecycleHelper().retrieveServiceChangeHistory(delegate.getBanId(), delegate.getSubscriberId(), from, to);
		} catch (ApplicationException e) {
			logger.error("Error calling retrieveServiceChangeHistory. banId=" + delegate.getBanId() + ", subscriberId=" + delegate.getSubscriberId() + ",from=" + from + ",to=" + to, e);
		}

		return historyInfos != null ? historyInfos : new ServiceChangeHistoryInfo[0];
	}

	@Override
	public ServiceChangeHistory[] getServiceChangeHistory(Date from, Date to, boolean includeAllServices) {

		ServiceChangeHistoryInfo[] historyInfos = null;
		try {
			historyInfos = changeContext.getSubscriberLifecycleHelper().retrieveServiceChangeHistory(delegate.getBanId(), delegate.getSubscriberId(), from, to, includeAllServices);
		} catch (ApplicationException e) {
			logger.error("Error calling retrieveServiceChangeHistory. banId=" + delegate.getBanId() + ", subscriberId=" + delegate.getSubscriberId() + ",from=" + from + ",to=" + to, e);
		}

		return historyInfos != null ? historyInfos : new ServiceChangeHistoryInfo[0];
	}

	public String getProvince() throws ApplicationException {

		String province = getMarketProvince();
		if (province == null) {
			logger.warn("subscriber has no privince, using account's: " + getSubscriberId());
			province = getAccount().getAddress().getProvince();
		}

		return province;
	}

	public void setEquipment(EquipmentBo equipment) {
		changeContext.setCurrentEquipment(equipment);
		setSerialNumber(equipment.getSerialNumber());
	}

	public void yieldShareablePricePlanPrimaryStatus(String dealerCode, String salesRepCode, Date newContractDate) throws TelusAPIException, ApplicationException {

		if (getContract().getPricePlan().isSharable()) {
			PricePlanSubscriberCount count = getAccount().getShareablePricePlanSubscriberCount(getContract().getPricePlan().getCode());
			if (count != null) {
				String[] phoneNumbers = new String[count.getActiveSubscribers().length + count.getReservedSubscribers().length];
				System.arraycopy(count.getActiveSubscribers(), 0, phoneNumbers, 0, count.getActiveSubscribers().length);
				System.arraycopy(count.getReservedSubscribers(), 0, phoneNumbers, count.getActiveSubscribers().length, count.getReservedSubscribers().length);

				for (int i = 0; i < phoneNumbers.length; i++) {
					if (!phoneNumbers[i].equals(getPhoneNumber())) {
						SubscriberInfo s = changeContext.getSubscriberLifecycleHelper().retrieveSubscriberByPhoneNumber(phoneNumbers[i]);
						SubscriberBo sub = new SubscriberBo(s, changeContext);
						sub.takeShareablePricePlanPrimaryStatus(dealerCode, salesRepCode, newContractDate);
						return;
					}
				}
			}
		}
	}

	public void takeShareablePricePlanPrimaryStatus(String dealerCode, String salesRepCode, Date newContractDate) throws ApplicationException, TelusAPIException {

		ContractBo c = getContract();
		Date now = changeContext.getLogicalDate();

		if (c.getPricePlan().isSharable()) {
			PricePlanBo pricePlan = c.getPricePlan();
			if (c.containsService(pricePlan.getDelegate().getSecondarySubscriberService())) {
				// Prod Fix:
				ContractService secondarySubSoc = c.getService(pricePlan.getDelegate().getSecondarySubscriberService());

				if (newContractDate != null && now.before(newContractDate)) { // new
					// price plan is future dated
					secondarySubSoc.setExpiryDate(newContractDate);
				} else {
					// price plan is not future dated
					c.removeService(secondarySubSoc.getCode());
				}

				c.save(dealerCode, salesRepCode);
			}
		}
	}

	public void updateShareablePricePlanStatus() throws ApplicationException, TelusAPIException {

		ContractBo contract = getContract();
		if (!contract.getPricePlan().isSharable()) {
			return;
		}

		PricePlanSubscriberCount pricePlanSubscriberCount = getAccount().getShareablePricePlanSubscriberCount(getContract().getPricePlan().getCode());
		String shareableServiceCode = contract.getPricePlan().getSecondarySubscriberService();
		// TODO  getAccount().getServiceSubscriberCounts is an unsupported operation in the delegate - need to implement this in the AccountBo
		ServiceSubscriberCount[] secondaryPricePlanSubscriberCount = getAccount().getServiceSubscriberCounts(new String[] { shareableServiceCode }, false);
		int numberOfSubscribersContainingSecondaryService = secondaryPricePlanSubscriberCount.length > 0 ? secondaryPricePlanSubscriberCount[0].getActiveSubscribers().length : 0;

		if (pricePlanSubscriberCount.getActiveSubscribers().length == numberOfSubscribersContainingSecondaryService) {
			// Shareable service code should be removed
			contract.removeService(shareableServiceCode);
			contract.save();
		} else if ((pricePlanSubscriberCount.getActiveSubscribers().length - numberOfSubscribersContainingSecondaryService) > 1) {
			if (!contract.containsService(shareableServiceCode)) {
				// Shareable service code should be added
				contract.addService(shareableServiceCode);
				contract.save();
			}
		}
	}

	@Override
	public ContractBo renewContract(int term) throws ApplicationException {
		return renewContract(getContract().getPricePlan(), term);
	}

	@Override
	public ContractBo renewContract(PricePlanDecorator pricePlan, int term) throws ApplicationException {
		
		try {
			return newContract0(pricePlan, term, !getContract().isTelephonyEnabled(), null, true);
		} catch (InvalidPricePlanChangeException ippce) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.PRICEPLAN_CHANGE_ERROR, ippce.getMessage(), "", ippce);
		} catch (TelusAPIException te) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, te.getMessage(), "", te);
		}
	}

	@Override
	public ContractBo renewContract(PricePlanDecorator pricePlan, int term, EquipmentChangeRequest equipmentChangeRequest) throws ApplicationException {
		
		try {
			return newContract0(pricePlan, term, !getContract().isTelephonyEnabled(), equipmentChangeRequest, true);
		} catch (InvalidPricePlanChangeException ippce) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.PRICEPLAN_CHANGE_ERROR, ippce.getMessage(), "", ippce);
		} catch (TelusAPIException te) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, te.getMessage(), "", te);
		}
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.subscriber.decorators.SubscriberDecorator#getBrandId()
	 */
	@Override
	public int getBrandId() {

		try {
			if (!(ContractUtilities.validateBrandId(delegate.getBrandId(), changeContext.getRefDataFacade().getBrands()))) {
				PricePlanSummary pricePlanSummary = changeContext.getRefDataFacade().getPricePlan(getPricePlan());
				return (pricePlanSummary != null) ? pricePlanSummary.getBrandId() : getAccount().getBrandId();
			}
		} catch (Throwable t) {
			logger.debug("error retrieving price plan or account brand ID", t);
		}

		return super.getBrandId();
	}

	
	public void testService(ServiceInfo service, EquipmentBo equipment) throws InvalidServiceException, TelusAPIException, ApplicationException {

		if (isSkipSubscriberAndServiceCompatibilityTest()) {
			return;
		}

		for (String familyType : service.getFamilyTypes()) {
			if (StringUtils.equals(familyType, ServiceSummary.FAMILY_TYPE_CODE_BUSINESS_ANYWHERE)) {
				changeContext.getSubscriberLifecycleFacade().testServiceAddToBusinessAnywhereAccount(getAccount().getDelegate(), service);
			}
		}

		// --------------------------------------------------------
		// Network and/or equipment type check
		// --------------------------------------------------------

		// defect PROD00142510fix
		// Check network compatibility on Optional Service (KB term: Regular SOC) only.
		// we only want to stop adding optional service that is not network compatible up front
		// for included service, let it go. we will remove it afterwards : before the contract get saved
		if (StringUtils.equalsIgnoreCase("R", service.getServiceType())
				// PROD00143139 fix, only validate Price plan for non-HSPA
				|| (StringUtils.equalsIgnoreCase("P", service.getServiceType()) && !equipment.isHSPA())) {
			int isNetworkOK = 0;
			isNetworkOK = service.testNetworkEquipmentTypeCompatibility(equipment.getDelegate());
			if (isNetworkOK > 0) {
				throw new InvalidServiceException(isNetworkOK, "service: " + service.getCode() + " - incompatible with equipment[" + equipment.getSerialNumber() + "]'s equipment/network type["
						+ equipment.getEquipmentType() + "/" + equipment.getNetworkType() + "]", service);
			}
		}

		// --------------------------------------------------------
		// /productType check:
		// -- postpaid SOC, compare with SOC's productType with Subscriber
		// -- prepaid SOC, make sure the Account is not postpaid
		// --------------------------------------------------------
		if (service.isWPS()) {
			if (getAccount().isPostpaid()) {
				throw new InvalidServiceException(InvalidServiceException.BILLING_TYPE_MISMATCH, "service: " + service.getCode() + " - Prepaid soc incompatible with postpaid account.", service);
			}
		} else {
			// only for postpaid service we check productType: because prepaid service does not have product type
			if (!service.getProductType().equals(getProductType())) {
				throw new InvalidServiceException(InvalidServiceException.TECHNOLOGY_MISMATCH, "service: " + service.getCode() + " - incompatible with proudctType[" + getProductType() + "]", service);
			}
		}

		if (!equipment.isHSPA()) {
			if (service.getService().isRIM()) {
				if (equipment.isRIM()) {
					// OK
				} else if (equipment.isSIMCard()) {
					MuleEquipment mule = equipment.getLastMule();
					if (mule != null && mule.isIDENRIM()) {
						// OK
					} else {
						throw new InvalidServiceException(InvalidServiceException.TECHNOLOGY_MISMATCH, "service: " + service.getCode() + " RIM service incompatible with non RIM equipment", service);
					}
				} else {
					String[] equipmentTypes = service.getEquipmentTypes(equipment.getNetworkType());
					if (equipmentTypes != null) {
						boolean isEquipmentTypeMatch = false;
						for (int i = 0; i < equipmentTypes.length; i++) {
							if (equipmentTypes[i].equals(equipment.getEquipmentType().trim())) {
								isEquipmentTypeMatch = true;
							}
						}
						if (!isEquipmentTypeMatch) {
							throw new InvalidServiceException(InvalidServiceException.TECHNOLOGY_MISMATCH, "service: " + service.getCode() + " RIM service incompatible with non RIM equipment",
									service);
						}
					}
				}
			}

			// --------------------------------------------------------
			// PDA Device - Cellular Digital Equipment
			// --------------------------------------------------------
			if (service.getService().isPDA()) {
				if (equipment.isCellularDigital() && equipment.getDelegate().isPDA()) {
					// OK
				} else {
					String[] equipmentTypes = service.getEquipmentTypes(equipment.getNetworkType());

					if (equipmentTypes != null) {
						boolean isEquipmentTypeMatch = false;
						for (int i = 0; i < equipmentTypes.length; i++) {
							if (equipmentTypes[i].equals(equipment.getEquipmentType().trim())) {
								isEquipmentTypeMatch = true;
							}
						}
						if (!isEquipmentTypeMatch) {
							throw new InvalidServiceException(InvalidServiceException.TECHNOLOGY_MISMATCH, "service: " + service.getCode() + " PDA service incompatible with non PDA equipment",
									service);
						}
					}
				}
			}
		}
	}

	private boolean isSkipSubscriberAndServiceCompatibilityTest() {
		if (changeContext != null && changeContext instanceof ContractChangeContext) {
			return ((ContractChangeContext) changeContext).isSkipSubscriberAndServiceCompatibilityTest();
		}

		return false;
	}

	public void setMigration(boolean isMigration) {
		this.isMigration = isMigration;
	}

	// Feb 23,2011, HT end state contract CR: 
	// change this method to delegate to the new overloaded version with flag Subscriber.SWAP_DUPLICATESERIALNO_DONOTALLOW
	public EquipmentChangeRequest newEquipmentChangeRequest(EquipmentInfo newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType,
			boolean preserveDigitalServices) throws TelusAPIException, ApplicationException {
		return newEquipmentChangeRequest(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices, Subscriber.SWAP_DUPLICATESERIALNO_DONOTALLOW);
	}

	// Feb 23,2011, HT end state contract CR: 
	// overload above API to support SmartDestkop triangle swap scenario, in which case, the new equipment is in used.
	public EquipmentChangeRequest newEquipmentChangeRequest(EquipmentInfo newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType,
			boolean preserveDigitalServices, char allowDuplicateSerialNo) throws TelusAPIException, ApplicationException {

		EquipmentChangeRequestInfo info = new EquipmentChangeRequestInfo();

		info.setNewEquipment(newEquipment);
		info.setRequestorId(requestorId);
		info.setRepairId(repairId);
		info.setSwapType(swapType);
		info.setDealerCode(dealerCode);
		info.setSalesRepCode(salesRepCode);
		info.setPreserveDigitalServices(preserveDigitalServices);

		// validate
		try {
			new ChangeEquipmentHelper(this).testChangeEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, allowDuplicateSerialNo);
		} catch (InvalidEquipmentChangeException e) {
			if (isMigration && e.getReason() != InvalidEquipmentChangeException.INVALID_SWAP_FOR_PREPAID_ACCOUNT) {
				throw e;
			}
		}

		return info;
	}

	public void testMigrate(MigrationRequestDecorator migrationRequest) throws TelusAPIException, ApplicationException {

		EquipmentInfo equip = getEquipment().getDelegate();
		if (equip.isUSIMCard()) {
			//			USIMCardEquipment usimEquip= (USIMCardEquipment)equip;
			if (equip.isExpired()) {
				throw new InvalidSerialNumberException("USIMCard sn(" + equip.getSerialNumber() + ") is expired.");
			}
			if (equip.isPreviouslyActivated()) {
				String lastAssociatedSubscriptionId = changeContext.getSubscriberLifecycleHelper().retrieveLastAssociatedSubscriptionId(equip.getProfile().getLocalIMSI());
				if (getSubscriptionId() != Long.valueOf(lastAssociatedSubscriptionId).longValue()) {
					throw new SerialNumberInUseException("USIMCard sn(" + equip.getSerialNumber() + ") is previously activated" + " with subscription id: " + lastAssociatedSubscriptionId);
				}
			}
		}

		MigrationUtilities.validateMigrationRequest(migrationRequest);
	}

	public SubscriberInfo migrate(MigrationRequestDecorator migrationRequest, boolean validateMigrate) throws TelusAPIException, ApplicationException {

		String methodName = "migrate(MigrationRequest, String, String, String)";
		String activity = "migrate";

		MigrationUtilities.validateMigrationRequest(migrationRequest);
		migrationRequest.createPortRequestAsNeeded();

		if (!validateMigrate) {
			try {
				migrationRequest.preMigrationTask();

				changeContext.getSubscriberLifecycleFacade().migrateSubscriber(getDelegate(), migrationRequest.getNewSubscriber(), changeContext.getLogicalDate(),
						(SubscriberContractInfo) migrationRequest.getNewContract(), migrationRequest.getNewEquipment0(), null, migrationRequest.getDelegate(),
						changeContext.getSubscriberLifecycleFacadeSessionId());

				migrationRequest.postMigrationTask();
				migrationRequest.submitPortRequestAsNeeded();
			} catch (Throwable e) {
				migrationRequest.cancelPortRequestAsNeeded();
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_FAILURE_ERROR, e.getMessage(), "", e);
			}

			// Holborn changes
			EquipmentInfo newEquipment = migrationRequest.getNewEquipment0();
			EquipmentInfo oldEquipment = changeContext.getCurrentEquipment().getDelegate();

			if (newEquipment.isHSPA() || getEquipment().isHSPA()) {
				try {
					changeContext.getProductEquipmentLifecycleFacade().swapEquipmentForPhoneNumber(getPhoneNumber(), oldEquipment.getSerialNumber(), oldEquipment.getAssociatedHandsetIMEI(),
							oldEquipment.getNetworkType(), newEquipment.getSerialNumber(), newEquipment.getAssociatedHandsetIMEI(), newEquipment.getNetworkType());

				} catch (Throwable thr) {
					// if the SEMS update fails, just log it and continue
					logFailure(methodName, activity, thr,
							"ProductEquipmentLifecycleFacade swapEquipmentForPhoneNumber Equipment to/for Phone failed for phone number [" + getPhoneNumber() + "]; exception ignored");
				}
			}
		}

		SubscriberInfo newSubscriber = changeContext.getSubscriberLifecycleHelper().retrieveSubscriberByPhoneNumber(getPhoneNumber());
		
		return newSubscriber;
	}

	public void testMigrate(MigrateSeatRequestDecorator migrateSeatRequest) throws ApplicationException, TelusAPIException {
		migrateSeatRequest.testMigrationRequest();
	}

	public SubscriberInfo migrate(MigrateSeatRequestDecorator migrateSeatRequest, boolean validateMigrate, boolean notificationSuppressionInd, AuditInfo auditInfo)
			throws TelusAPIException, ApplicationException {

		testMigrate(migrateSeatRequest);

		if (!validateMigrate) {
			// Execute pre-migration tasks, if any
			migrateSeatRequest.preMigrationTask();

			// Put all activities related to moving the subscriber in this try-catch clause
			try {
				// Set this to true, as we need to execute the underlying 'change ownership' logic
				boolean transferOwnership = true;

				// Move the seat (i.e., subscriber) to the target account
				changeContext.getSubscriberLifecycleFacade().moveSubscriber(getDelegate(), migrateSeatRequest.getTargetBan(), changeContext.getLogicalDate(), transferOwnership,
						migrateSeatRequest.getMigrationReasonCode(), migrateSeatRequest.getUserMemoText(), migrateSeatRequest.getDealerCode(), migrateSeatRequest.getSalesRepCode(),
						notificationSuppressionInd, auditInfo, changeContext.getSubscriberLifecycleFacadeSessionId());

				// Remove the secondary service from another active subscriber sharing this plan if this subscriber was primary on a sharable priceplan
				if (getContract().isShareablePricePlanPrimary()) {
					yieldShareablePricePlanPrimaryStatus(migrateSeatRequest.getDealerCode(), migrateSeatRequest.getSalesRepCode(), null);
				}

			} catch (Throwable t) {
				// No fallout email is required at this point, since nothing has completed - just throw the exception
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_SEAT_MOVE_SUBSCRIBER_ERROR, t.getMessage(), "", t);
			}

			// Put all activities related to changing the subscriber's price plan in this try-catch clause
			try {
				// Set the target price plan
				PricePlanChangeInfo pricePlanChangeInfo = new PricePlanChangeInfo();
				pricePlanChangeInfo.setCode(migrateSeatRequest.getTargetPricePlanCode());

				// Set the contract change information object
				ContractChangeInfo contractChangeInfo = new ContractChangeInfo();
				contractChangeInfo.setBan(migrateSeatRequest.getTargetBan());
				contractChangeInfo.setSubscriberId(getDelegate().getSubscriberId());
				contractChangeInfo.setPricePlanChangeInfo(pricePlanChangeInfo);
				contractChangeInfo.setPricePlanChangeInd(true);
				contractChangeInfo.setContractTerm(Subscriber.TERM_PRESERVE_COMMITMENT);
				contractChangeInfo.setDealerCode(migrateSeatRequest.getDealerCode());
				contractChangeInfo.setSalesRepCode(migrateSeatRequest.getSalesRepCode());

				// Change the service agreement to the new price plan	
				changeContext.getSubscriberLifecycleFacade().migrateSeatChangePricePlan(contractChangeInfo, notificationSuppressionInd, auditInfo,
						changeContext.getSubscriberLifecycleFacadeSessionId());

			} catch (Throwable t) {
				// Business decided not to send the fallout email notification
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_SEAT_CHANGE_PRICE_PLAN_ERROR, t.getMessage(), "", t);
			}

			// Execute post-migration tasks, if any
			migrateSeatRequest.postMigrationTask();
		}

		// Retrieve the 'migrated' subscriber (moved to the new account and with an updated service agreement)
		SubscriberInfo migratedSubscriber = changeContext.getSubscriberLifecycleHelper().retrieveSubscriberByPhoneNumber(getPhoneNumber());
		
		return migratedSubscriber;
	}

	public PortRequest getPortRequest() throws ApplicationException {

		if (portRequest == null) {
			PortRequestInfo[] portRequestArray = null;
			//			 PortRequestInfo[] portRequestArray = getPortRequestSO().getCurrentPortRequestsByPhoneNumber(getPhoneNumber(), getBrandId());
			try {
				portRequestArray = changeContext.getSubscriberLifecycleFacade().getCurrentPortRequestsByPhoneNumber(getPhoneNumber(), getBrandId());
			} catch (ApplicationException ae) {
				throw ae;
			}
			if (portRequestArray != null && portRequestArray.length > 0) {
				portRequest = portRequestArray[0];
				portRequest.setSubscriber(this.getDelegate());
				portRequest.setAccount(this.getAccount().getDelegate());
				portRequest.setEquipment(this.getEquipment().getDelegate());
			}
		}

		return portRequest;
	}

	public PortRequest newPortRequest(String phoneNumber, String NPDirectionIndicator, boolean prePopulate) throws TelusAPIException, ApplicationException {

		portRequest = new PortRequestInfo();
		Account account = getAccount();
		if (prePopulate) {
			PortRequest lastPortRequest = getLastPortRequest(account);
			if (lastPortRequest != null) {
				portRequest.setPortRequestName(lastPortRequest.getPortRequestName());
				portRequest.setAgencyAuthorizationName(lastPortRequest.getAgencyAuthorizationName());
				portRequest.setBusinessName(lastPortRequest.getBusinessName());
				portRequest.setPortRequestAddress(lastPortRequest.getPortRequestAddress());
				if (MigrationUtilities.isWithin24Hours(lastPortRequest.getCreationDate())) {
					portRequest.setOSPAccountNumber(lastPortRequest.getOSPAccountNumber());
					portRequest.setOSPPin(lastPortRequest.getOSPPin());
				}
			} else {
				portRequest = (PortRequestInfo) PortRequestManager.Helper.copyName(account, portRequest);
				portRequest = (PortRequestInfo) PortRequestManager.Helper.copyAddress(account, portRequest);
			}
			portRequest.setAlternateContactNumber(account.getOtherPhone());
			portRequest.setPhoneNumber(phoneNumber);
			portRequest.setPortDirectionIndicator(NPDirectionIndicator);
		} else {
			if (NPDirectionIndicator.equals(PortInEligibility.PORT_DIRECTION_INDICATOR_WIRELESS_WIRELESS)) {
				portRequest = (PortRequestInfo) PortRequestManager.Helper.copyName(account, portRequest);
				portRequest = (PortRequestInfo) PortRequestManager.Helper.copyAddress(account, portRequest);
			}
			portRequest.setPhoneNumber(phoneNumber);
			portRequest.setPortDirectionIndicator(NPDirectionIndicator);
		}

		portRequest.setSubscriber(this.getDelegate());
		portRequest.setAccount(this.getAccount().getDelegate());
		portRequest.setEquipment(this.getEquipment().getDelegate());

		return portRequest;
	}

	private PortRequest getLastPortRequest(Account account) throws ApplicationException {

		PortRequest lastPortRequest = null;
		PortRequest[] portRequestArray = null;
		try {
			portRequestArray = changeContext.getSubscriberLifecycleFacade().getCurrentPortRequestsByBan(account.getBanId());
		} catch (ApplicationException ae) {
			if (!ae.getErrorCode().equals("")) {
				return null;
			} else {
				throw ae;
			}
		}
		if (portRequestArray != null && portRequestArray.length != 0) {
			Date lastCreationDate = portRequestArray[0].getCreationDate();
			int index = 0;
			for (int i = 1; i < portRequestArray.length; i++) {
				Date creationDate = portRequestArray[i].getCreationDate();
				if (lastCreationDate.before(creationDate)) {
					lastCreationDate = creationDate;
					index = i;
				}
			}
			lastPortRequest = portRequestArray[index];
		}

		return lastPortRequest;
	}

	@Override
	public NumberGroup getNumberGroup() throws TelusAPIException {

		try {
			if (delegate.getNumberGroup() == null) {
				delegate.setNumberGroup(changeContext.getRefDataHelper().retrieveNumberGroupByPhoneNumberProductType(getPhoneNumber(), getProductType()));
			}

			return delegate.getNumberGroup();

		} catch (TelusAPIException e) {
			throw e;
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	public boolean isActivation() {
		return this.activation;
	}

	public BaseChangeContext<? extends BaseChangeInfo> getChangeContext() {
		return this.changeContext;
	}

	private DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

	private static FieldPosition POSITION = new FieldPosition(0);

	public StringBuffer buildLogEntryHeader(String methodName, String activity) {

		StringBuffer sb = new StringBuffer();
		DATE_FORMAT.format(new Date(), sb, POSITION).append(" ");
		sb.append("[").append(Thread.currentThread().getName()).append("] ");
		sb.append(methodName).append("():").append(activity);

		return sb;
	}

	public StringBuffer appendSubscriberInfo(StringBuffer sb, SubscriberInfo subInfo) {

		if (subInfo != null) {
			sb.append("  subscriber[ban:").append(subInfo.getBanId()).append(", subId:").append(subInfo.getSubscriberId()).append(", phone:").append(subInfo.getPhoneNumber()).append("]");
		}

		return sb;
	}

	protected void logSuccess(String methodName, String activity, SubscriberInfo subInfo, String extraMessage) {
		
		StringBuffer sb = buildLogEntryHeader(methodName, activity);
		sb.append("-succeeded; ");
		if (extraMessage != null) {
			sb.append(extraMessage).append(";");
		}
		appendSubscriberInfo(sb, subInfo);
		logger.debug(sb.toString());
	}

	protected void logFailure(String methodName, String activity, SubscriberInfo subInfo, Throwable t, String extraMessage) {
		
		StringBuffer sb = buildLogEntryHeader(methodName, activity);
		sb.append("-failed; ");
		if (extraMessage != null) {
			sb.append(extraMessage).append(";");
		}
		appendSubscriberInfo(sb, subInfo);
		logger.debug(sb.toString());
		logger.debug(t);
	}

	protected void logMessage(String methodName, String activity, SubscriberInfo subInfo, String extraMessage) {
		StringBuffer sb = buildLogEntryHeader(methodName, activity).append("; ").append(extraMessage);
		appendSubscriberInfo(sb, subInfo);
		logger.debug(sb.toString());
	}

	public void logMessage(String methodName, String activity, String extraMessage) {
		logMessage(methodName, activity, delegate, extraMessage);
	}

	public void logSuccess(String methodName, String activity, String extraMessage) {
		logSuccess(methodName, activity, delegate, extraMessage);
	}

	public void logFailure(String methodName, String activity, Throwable t, String extraMessage) {
		logFailure(methodName, activity, delegate, t, extraMessage);
	}

	public void refresh(boolean refreshAccount) throws ApplicationException {

		SubscriberInfo subscriberInfo = changeContext.getSubscriberLifecycleHelper().retrieveSubscriber(getSubscriberId());
		delegate.copyFrom(subscriberInfo);
		portRequest = null;
		commSuiteCached = false;
//		commSuiteInfo = null;
		if (refreshAccount) {
			refreshAccount();
		}
	}

	private void refreshAccount() throws ApplicationException {
		AccountInfo accountInfo = changeContext.getAccountInformationHelper().retrieveAccountByBan(getBanId(), Account.ACCOUNT_LOAD_ALL);
		account = new AccountBo(accountInfo, changeContext);
	}
	
	public CommunicationSuiteInfo getCommunicationSuite() throws ApplicationException {
		CommunicationSuiteInfo commSuite = null;
		
		boolean isEligibleForCommSuite = changeContext.getAccountLifecycleFacade().validateCommunicationSuiteEligibility(getAccount().getBrandId(), getAccount().getAccountType(), getAccount().getAccountSubType());
		
		if (isEligibleForCommSuite && commSuiteCached == false) {
			try {
				commSuite = changeContext.getSubscriberLifecycleHelper().retrieveCommunicationSuite(getBanId(), getSubscriberId(), CommunicationSuiteInfo.CHECK_LEVEL_ALL);
				this.commSuiteCached = true;
			} catch (Throwable e) {
				logger.error("Error retrieving communication suite in SubscriberBo ban=[" + getBanId() + "], subscriberId=[" + getSubscriberId() + "]");
			}
		}
		return commSuite;
	}

}