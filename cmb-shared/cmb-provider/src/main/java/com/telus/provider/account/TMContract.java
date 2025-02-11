/*
 * $Id$
 * %E% %W%
 * Copyright (c) TELUS Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.InvalidCardChangeException;
import com.telus.api.InvalidServiceChangeException;
import com.telus.api.InvalidServiceException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.Account;
import com.telus.api.account.CallingFeatureCycle;
import com.telus.api.account.Contract;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.account.DurationServiceCommitmentAttributeData;
import com.telus.api.account.EquipmentChangeRequest;
import com.telus.api.account.FollowUp;
import com.telus.api.account.IDENSubscriber;
import com.telus.api.account.InvoiceProperties;
import com.telus.api.account.PhoneNumberException;
import com.telus.api.account.PostpaidAccount;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.PricePlanValidation;
import com.telus.api.account.ServiceChangeHistory;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Card;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentManager;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.equipment.SIMCardEquipment;
import com.telus.api.reference.AccountType;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.BillHoldRedirectDestination;
import com.telus.api.reference.Brand;
import com.telus.api.reference.BusinessRole;
import com.telus.api.reference.Dealer;
import com.telus.api.reference.Feature;
import com.telus.api.reference.InvoiceSuppressionLevel;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.PoolingGroup;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.RatedFeature;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceExclusionGroups;
import com.telus.api.reference.ServiceRelation;
import com.telus.api.reference.ServiceSummary;
import com.telus.api.reference.ShareablePricePlan;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.task.ContractChangeTask;
import com.telus.api.util.ClientApiUtils;
import com.telus.api.util.SessionUtil;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.Info;
import com.telus.eas.subscriber.info.BaseAgreementInfo;
import com.telus.eas.subscriber.info.CallingFeatureCycleInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.MultiRingInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.utility.info.AccountTypeInfo;
import com.telus.eas.utility.info.FeatureInfo;
import com.telus.eas.utility.info.RatedFeatureInfo;
import com.telus.eas.utility.info.ServiceExclusionGroupsInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.equipment.TMCard;
import com.telus.provider.equipment.TMEquipment;
import com.telus.provider.equipment.TMMuleEquipment;
import com.telus.provider.equipment.TMSIMCardEquipment;
import com.telus.provider.reference.TMPricePlan;
import com.telus.provider.reference.TMPricePlanSummary;
import com.telus.provider.reference.TMService;
import com.telus.provider.servicerequest.TMServiceRequestManager;
import com.telus.provider.task.TMContractChangeTask;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.DateUtil;
import com.telus.provider.util.Logger;
import com.telus.provider.util.ProviderServiceChangeExceptionTranslator;

public class TMContract extends BaseProvider implements Contract {

	private static final long serialVersionUID = 1L;
	public static final long DAY = 1000L * 60L * 60L * 24L;

	// private constant
	private static final String PRIVILEGE_AUTOADD = ServiceSummary.PRIVILEGE_AUTOADD;
	private static final String ROAMING_PASSES_BOUND_SOC_FAMILY_TYPE = "1";

	/**originalMultiRingPhones
	 * @link aggregation
	 */
	private SubscriberContractInfo delegate;
	private TMPricePlan pricePlan;
	private TMSubscriber subscriber;
	private boolean activation;
	private boolean priceplanChange;
	private boolean contractRenewal;
	private boolean changeInvoiceFormat = false;
	private boolean oldVistoPreserved = false;
	private Date logicalDate = null;
	
	private TMSubscriber migrationTargetSubscriber;
	private TMSubscriber migrationSourceSubscriber;
	
	private SubscriberContractInfo oldContractInfo;
	
	 private Set serviceToAdd = new HashSet();
	 private Set serviceToRemove = new HashSet();


	/**
	 * @link aggregation
	 * @associationAsClass Set
	 * @associates <{TMCard}>
	 */
	public Set cards = new HashSet();

	private EquipmentChangeRequest equipmentChangeRequest;

	public TMContract(TMProvider provider, SubscriberContractInfo delegate, TMPricePlan pricePlan, TMSubscriber subscriber, boolean activation, boolean priceplanChange, boolean contractRenewal, 
			int term, EquipmentChangeRequest equipmentChangeRequest) throws TelusAPIException {

		super(provider);
		this.delegate = delegate;
		this.pricePlan = pricePlan;
		this.subscriber = subscriber;
		this.activation = activation;
		this.priceplanChange = priceplanChange;
		this.contractRenewal = contractRenewal;
		this.equipmentChangeRequest = equipmentChangeRequest;

		if (contractRenewal && subscriber.getStatus() != Subscriber.STATUS_ACTIVE) {
			throw new TelusAPIException("Cannot renew contract on an inactive subscriber");
		}

		//--------------------------------------
		// Set the pricePlan on the delegate.
		//--------------------------------------
		delegate.setPricePlanInfo(((TMPricePlanSummary)pricePlan).getDelegate0());
		delegate.setPricePlanChange(priceplanChange);
		delegate.setContractRenewal(contractRenewal);
		//--------------------------------------
		// Add the existing contract's optional ContractServices in a deleted state.
		//--------------------------------------
		TMContract oldContract = null;
		if (priceplanChange) {
			Date now = provider.getReferenceDataManager().getSystemDate();
			oldContract = subscriber.getContract0(true, false);
			oldContractInfo = oldContract.getDelegate();
			ServiceAgreementInfo[] info = oldContract.getDelegate().getOptionalAndIncludedPromotionalServices(true);

			for (int i = 0; i < info.length; i++) {
				ServiceAgreementInfo s = info[i];
				Date expiryDate = s.getExpiryDate();
				if (!delegate.containsService0(s.getServiceCode(), false) && (expiryDate == null || expiryDate.after(now))) {
					s.setTransaction(BaseAgreementInfo.DELETE);
					delegate.addService(s);
				}
			}
		} else if (contractRenewal) {
			oldContract = subscriber.getContract0(true, false);
		}

		//---------------------------------------------
		// Add included services and features if this is a new contract or a new priceplan.
		//---------------------------------------------
		if (activation || priceplanChange) {
			//ServiceInfo[] includedServices = delegate.getPricePlan0().getIncludedServices0();
			//use the TMPricePlan instead of PricePlanInfo class, so that TMService is properly returned.
			Service[] includedServices = pricePlan.getIncludedServices();   
			for (int i = 0; i < includedServices.length; i++) {
				Logger.debug("----> TMContract.Add_included_services(" + includedServices[i].getCode() + ")");
				ServiceAgreementInfo info = addService0(includedServices[i], null, null, false);

				//-------------------------------------------------
				// Since IncludedPromotions are really optional
				// SOCs to AMDOCS, they should be treated as such
				// by the provider (left in the ADD state).
				//-------------------------------------------------
				if(!info.getService0().isIncludedPromotion()) {
					info.setTransaction(ServiceAgreementInfo.NO_CHG, true, false);
				}

				addBoundServices(pricePlan.getIncludedService(includedServices[i].getCode()), null, null);
			}

			if (priceplanChange) {
				Service newVisto = hasVisto(false);
				Service oldVisto = oldContract.hasVisto(true);
				if (newVisto != null && oldVisto != null && !oldVisto.getCode().equals(newVisto.getCode())
					&& oldVisto.isNetworkEquipmentTypeCompatible(getValidationEquipment())) {
					removeService(newVisto.getCode());
					addService0(oldVisto, null, null, true);
					oldVistoPreserved = true;
				}
			}

			RatedFeatureInfo[] includedFeatures = delegate.getPricePlan0().getFeatures0();
			for (int i = 0; i < includedFeatures.length; i++) {
				Logger.debug("----> TMContract.Add_included_features(" + includedFeatures[i].getCode() + ")");
				ServiceFeatureInfo info = addFeature0(includedFeatures[i]);
				info.setTransaction(ServiceFeatureInfo.NO_CHG);
			}
		}

		if (activation || priceplanChange || contractRenewal) {
			removeNonMatchingServices(getValidationEquipment(), getAssociatedMule(), subscriber.getProvince());
			
			//New method for Holborn R1 project to remove non-matching SOCs (by network type) from the subscriber's contract
			Service[] restrictedSOCsLost = removeNonMatchingRestrictedSOCs(getValidationEquipment().getNetworkType());			
		}

		if (activation || priceplanChange) {
			// remove call detail feature on all postpaid PCS rateplans
			removeCallDetailFeatureOnPostpaidPCS();
		}

		//-----------------------------------------------------------
		// Add telephony blocking services if this is a new dispatch only contract.It's safe to assume this is IDEN.
		//-----------------------------------------------------------
		if ((activation || priceplanChange) && delegate.isDispatchOnly()) {
			addService0(ServiceSummary.BLOCK_INCOMING_CALLS_IDEN, null, null, false);
			addService0(ServiceSummary.BLOCK_OUTGOING_CALLS_IDEN, null, null, false);
		}

		//------------------------------------------------------------
		// Mike data services for activation
		//------------------------------------------------------------
		if (activation) {
			Service[] services = pricePlan.getOptionalServices(subscriber.getEquipment0());
			for (int i = 0; i < services.length; i++) {
				if (services[i].containsPrivilege(BusinessRole.BUSINESS_ROLE_ALL, PRIVILEGE_AUTOADD)) {
					if (services[i].isMOSMS() || services[i].isMMS()) // || services[i].isJavaDownload())
						addService0(services[i].getCode(), null, null, false);
				}
			}
		}

		//-----------------------------------------------------------
		// 1) Add secondary service for non-primary, shareable
		//    priceplan subscribers.
		//
		// 2) Add shearable services that are on other subscribers
		//    with the same priceplan.
		//
		//-----------------------------------------------------------
		if (activation || priceplanChange) {
			// Do not execute shareable priceplan subscriber count query on corporate accounts for performance reasons. 
			if (pricePlan.isSharable() && !subscriber.getAccount0().isCorporate()) {
				ShareablePricePlan shareablePricePlan = (ShareablePricePlan)pricePlan;
				PricePlanSubscriberCount count = subscriber.getAccount0().getShareablePricePlanSubscriberCount(shareablePricePlan);

				//-----------------------------------------------------------
				// Add secondary service
				//-----------------------------------------------------------
				if(count != null && ((count.getActiveSubscribers().length + count.getReservedSubscribers().length + count.getFutureDatedSubscribers().length) > 0)) {
					if (shareablePricePlan.getSecondarySubscriberService() != null) {
						addService0(shareablePricePlan.getSecondarySubscriberService(), null, null, false);
					}
				}

				//-----------------------------------------------------------
				// Add shearable services that are on other subscribers.
				//-----------------------------------------------------------
				addShareableServicesOnOtherSubscribers(count, false);
			}
		}

		if (activation || priceplanChange || contractRenewal) {
			removeDispatchOnlyConflicts();
		}

		//-----------------------------------------------------------
		// Setup commitment for new contracts, new priceplan, or
		// contractRenewals.
		//-----------------------------------------------------------
		if (activation || priceplanChange || contractRenewal) {
			if (term == Subscriber.TERM_PRESERVE_COMMITMENT && oldContract.getDelegate().getCommitment().isValid()) {
				setCommitmentStartDate(oldContract.getCommitmentStartDate());
				setCommitmentMonths(oldContract.getCommitmentMonths());
				setCommitmentEndDate(oldContract.getCommitmentEndDate());
			} else if (term == Subscriber.TERM_MONTH_TO_MONTH) {
				setCommitmentStartDate(null);
				setCommitmentMonths(0);
				setCommitmentEndDate(null);
				setCommitmentReasonCode(null);
			} else if (term != Subscriber.TERM_PRESERVE_COMMITMENT) {
				setCommitmentStartDate(provider.getReferenceDataManager().getSystemDate());
				setCommitmentMonths(term);
			}

			if (!activation && oldContract.getDelegate().getCommitment().isValid()) {
				setCommitmentReasonCode(oldContract.getCommitmentReasonCode());
			}

			if (activation || term == Subscriber.TERM_PRESERVE_COMMITMENT) {
				delegate.getCommitment().setModified(false);
			}
		}
	}

	public SubscriberContractInfo getDelegate() {
		return delegate;
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	public int getServiceCount() {
		return delegate.getServiceCount();
	}

	public ContractFeature[] getFeatures() {
		return decorate(delegate.getFeatures());
	}

	public ContractFeature[] getFeatures(boolean includeServices) {
		return decorate(delegate.getFeatures(includeServices));
	}

	public ContractService[] getServices() {
		return decorate(delegate.getServices());
	}

	public ContractService[] getIncludedServices() {
		return decorate(delegate.getIncludedServices());
	}

	protected List getIncludedServiceList() {
		return Arrays.asList(getIncludedServices());
	}

	public ContractService[] getOptionalServices() {
		return decorate(delegate.getOptionalServices());
	}

	protected List getOptionalServiceList() {
		return Arrays.asList(getOptionalServices());
	}

	public ContractService getService(String code) throws UnknownObjectException {
		code = Info.padService(code);
		return decorate(delegate.getService(code));
	}

	public Date getExpiryDate() {
		return delegate.getExpiryDate();
	}

	public void setExpiryDate(Date expiryDate) {
		delegate.setExpiryDate(expiryDate);
	}

	public Date getEffectiveDate() {
		return delegate.getEffectiveDate();
	}

	public void setEffectiveDate(Date effectiveDate) {
		delegate.setEffectiveDate(effectiveDate);
	}

	public int getCommitmentMonths(){
		return delegate.getCommitmentMonths();
	}

	public void setCommitmentMonths(int commitmentMonths){
		delegate.setCommitmentMonths(commitmentMonths);
	}

	public Date getCommitmentStartDate(){
		return delegate.getCommitmentStartDate();
	}

	public void setCommitmentStartDate(Date commitmentStartDate){
		delegate.setCommitmentStartDate(commitmentStartDate);
	}

	public Date getCommitmentEndDate(){
		return delegate.getCommitmentEndDate();
	}

	public void setCommitmentEndDate(Date commitmentEndDate){
		delegate.setCommitmentEndDate(commitmentEndDate);
	}

	public String getCommitmentReasonCode(){
		return delegate.getCommitmentReasonCode();
	}

	public void setCommitmentReasonCode(String commitmentReasonCode){
		delegate.setCommitmentReasonCode(commitmentReasonCode);
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public boolean isModified() {
		return delegate.isModified();
	}


	public ContractService[] getAddedServices() {
		return decorate(delegate.getAddedServices());
	}

	public TMContractService[] getAddedOrDeletedShareableServices(boolean returnIncludedServices) throws TelusAPIException {
		ContractService[] modifiedServices = decorate(delegate.getModifiedServices());

		List list = new ArrayList(modifiedServices.length);
		for (int i = 0; i < modifiedServices.length; i++) {
			TMContractService cs = (TMContractService)modifiedServices[i];
			if (cs.getService().isSharable()
					&& (cs.getDelegate().getTransaction0() == BaseAgreementInfo.ADD
							|| cs.getDelegate().getTransaction0() == BaseAgreementInfo.DELETE)) {
				if (returnIncludedServices || !pricePlan.containsIncludedService(cs.getCode())) {
					list.add(cs);
				}
			}
		}

		return (TMContractService[])list.toArray(new TMContractService[list.size()]);
	}

	public ContractService[] getChangedServices() {
		return decorate(delegate.getChangedServices());
	}

	public ContractService[] getDeletedServices() {
		return decorate(delegate.getDeletedServices());
	}

	public ContractFeature[] getAddedFeatures() {
		return decorate(delegate.getAddedFeatures());
	}

	public ContractFeature[] getChangedFeatures() {
		return decorate(delegate.getChangedFeatures());
	}

	public ContractFeature[] getDeletedFeatures() {
		return decorate(delegate.getDeletedFeatures());
	}

	public boolean getCascadeShareableServiceChanges(){
		return cascadeShareableServiceChanges;
	}

	public void setCascadeShareableServiceChanges(boolean cascadeShareableServiceChanges){
		this.cascadeShareableServiceChanges = cascadeShareableServiceChanges;
	}


	 //--------------------------------------------------------------------
	 //  Service Methods
	//--------------------------------------------------------------------
	public void commit(String dealerCode, String salesRepCode) throws TelusAPIException {
		commitCascadingContractChanges(dealerCode, salesRepCode);

		activation = false;
		priceplanChange = false;

		//-----------------------------------------
		// After a priceplan change we need to remove references to the old contract and priceplan.
		// This is safe for activations an add/change features.
		//-----------------------------------------
		subscriber.setContract(this);

		delegate.commit();
		cards.clear();
		equipmentChangeRequest = null;
	}

	public EquipmentChangeRequest getEquipmentChangeRequest() {
		return equipmentChangeRequest;
	}

	public void setEquipmentChangeRequest(EquipmentChangeRequest equipmentChangeRequest) throws TelusAPIException {
		if(this.equipmentChangeRequest != null) {
			throw new TelusAPIException("An equipmentChangeRequest is already associated with this contract");
		}

		this.equipmentChangeRequest = equipmentChangeRequest;

		removeNonMatchingServices(getValidationEquipment(), getAssociatedMule(), subscriber.getProvince());
		addIncludedServices(provider.getEquipmentManager0().translateEquipmentType(getValidationEquipment()), subscriber.getProvince());
	}

	private TMEquipment getValidationEquipment() throws TelusAPIException {
		if (equipmentChangeRequest != null) {
			return (TMEquipment) equipmentChangeRequest.getNewEquipment();
		}
		return subscriber.getEquipment0();
	}

	private MuleEquipment getAssociatedMule() throws TelusAPIException {
		MuleEquipment mule = null;
		if (equipmentChangeRequest != null) {
			mule = equipmentChangeRequest.getAssociatedMuleEquipment();
			if (mule != null)
				return mule;

			Equipment newEquipment = equipmentChangeRequest.getNewEquipment();
			if (newEquipment != null && newEquipment.isSIMCard()) {
				mule = ( (SIMCardEquipment) newEquipment).getLastMule();
				if (mule != null)
					return mule;
			}
		}

		if (subscriber.getEquipment().isSIMCard()) {
			mule = ((SIMCardEquipment)subscriber.getEquipment()).getLastMule();
			if (mule != null)
				return mule;
		}

		return null;
	}

	public PricePlan getPricePlan() {
		return pricePlan;
	}

	public boolean isTelephonyEnabled() throws TelusAPIException {
		return !delegate.containsService0(ServiceSummary.BLOCK_INCOMING_CALLS_IDEN, false) &&
		!delegate.containsService0(ServiceSummary.BLOCK_OUTGOING_CALLS_IDEN, false);
	}

	public boolean isDispatchEnabled() throws TelusAPIException {
		if(subscriber.isIDEN()) {
			IDENSubscriber s = (IDENSubscriber)subscriber;
			return s.getMemberIdentity() != null;
		}
		return false;
	}

	public boolean isWirelessWebEnabled() throws TelusAPIException {
		// TODO: this should be based on the equipment's ability.
		return true;
	}

	public boolean canModifyShareableService(String code) throws TelusAPIException {
		try {
			Service service = pricePlan.getService(code);

			if (service == null) {
				throw new UnknownObjectException("couldn't find shearable service (" + code + ") on pricaplan (" +
						pricePlan.getCode() + ")", code);
			}

			return!service.isPromotion()
			&& !service.hasPromotion()
			&& !service.isIncludedPromotion()
			&& !pricePlan.containsIncludedService(code)
			;
		} catch (UnknownObjectException ex) {
			return false;
		}
	}

	public void addShareableServicesOnOtherSubscribers(boolean saveContract) throws TelusAPIException {
		if (pricePlan.isSharable()) {
			ShareablePricePlan shareablePricePlan = (ShareablePricePlan)pricePlan;
			PricePlanSubscriberCount count = subscriber.getAccountSummary().getAccount0().getShareablePricePlanSubscriberCount(shareablePricePlan);
			addShareableServicesOnOtherSubscribers(count, saveContract);
		}
	}

	public void addShareableServicesOnOtherSubscribers(PricePlanSubscriberCount pricePlanSubscriberCount, boolean saveContract) throws TelusAPIException {
		if (pricePlanSubscriberCount == null) {
			return;
		}

		//-----------------------------------------------------------
		// Add shearable services that are on other subscribers.
		//-----------------------------------------------------------
		ServiceSubscriberCount[] serviceSubscriberCount = pricePlanSubscriberCount.getServiceSubscriberCounts();
		for (int i = 0; i < serviceSubscriberCount.length; i++) {
			String code = serviceSubscriberCount[i].getServiceCode();
			if (!containsService(code) && canModifyShareableService(code)) {
				addService(code);
			}
		}

		if (saveContract && isModified()) {
			save();
		}
	}

	//===================================================================================================
	private List cascadingContractChanges = new ArrayList();

	public TMContractChangeTask newContractChangeTask(TMSubscriber anotherSubscriber, String dealerCode, String salesRepCode) throws TelusAPIException {
		TMContractChangeTask task = new TMContractChangeTask(anotherSubscriber, dealerCode, salesRepCode);
		cascadingContractChanges.add(task);
		return task;
	}

	public TMContractChangeTask[] getCascadingContractChanges0() {
		return (TMContractChangeTask[])cascadingContractChanges.toArray(new TMContractChangeTask[cascadingContractChanges.size()]);
	}

	public ContractChangeTask[] getCascadingContractChanges() {
		return getCascadingContractChanges0();
	}

	public void cascadeServiceAddition(Service service) {
		TMContractChangeTask[] tasks = getCascadingContractChanges0();
		for (int i = 0; i < tasks.length; i++) {
			Logger.debug("cascadeServiceAddition::[service="+service+"] [SubscriberId=" + tasks[i].getSubscriber().getSubscriberId() + "]");
			tasks[i].addService(service);
		}
	}

	public void cascadeServiceDeletion(Service service) {
		TMContractChangeTask[] tasks = getCascadingContractChanges0();
		for (int i = 0; i < tasks.length; i++) {
			Logger.debug("cascadeServiceDeletion::[service="+service+"] [SubscriberId=" + tasks[i].getSubscriber().getSubscriberId() + "]");
			tasks[i].removeService(service);
		}
	}

	public void removeCascadingChangesWithNoWork() {
		TMContractChangeTask[] tasks = getCascadingContractChanges0();
		for (int i = tasks.length-1; i >= 0; i--) {
			if (!tasks[i].hasWork()) {
				Logger.debug("removeCascadingChangesWithNoWork::[SubscriberId=" + tasks[i].getSubscriber().getSubscriberId() + "]");
				cascadingContractChanges.remove(i);
			}
		}
	}

	public void runCascadingChanges() {
		TMContractChangeTask[] tasks = getCascadingContractChanges0();
		for (int i = 0; i < tasks.length; i++) {
			Logger.debug("runCascadingChanges::[SubscriberId=" + tasks[i].getSubscriber().getSubscriberId() + "]");
			tasks[i].run();
		}
	}

	public void commitCascadingContractChanges(String dealerCode, String salesRepCode) throws TelusAPIException {

		cascadingContractChanges.clear();
		if (getCascadeShareableServiceChanges()) {
			TMContractService[] addedOrDeletedShareableServices = getAddedOrDeletedShareableServices(false);
			if (pricePlan.isSharable() && addedOrDeletedShareableServices.length > 0) {
				//=======================================================================
				// Create tasks for other subscibers
				//=======================================================================
				ShareablePricePlan shareablePricePlan = (ShareablePricePlan)pricePlan;
				PricePlanSubscriberCount pricePlanSubscriberCount = subscriber.getAccount0().getShareablePricePlanSubscriberCount(shareablePricePlan);
				if(pricePlanSubscriberCount != null) {
					String[] subscribers = pricePlanSubscriberCount.getActiveAndReservedSubscribers();
					for (int i = 0; i < subscribers.length; i++) {
						if (!subscribers[i].equals(subscriber.getSubscriberId())) {
							newContractChangeTask((TMSubscriber)provider.getAccountManager0().findSubscriberByPhoneNumber(subscribers[i]), dealerCode, salesRepCode);
						}
					}

					//=======================================================================
					// Setup tasks
					//=======================================================================
					for (int i = 0; i < addedOrDeletedShareableServices.length; i++) {
						TMContractService cs = addedOrDeletedShareableServices[i];
						if (cs.getDelegate().getTransaction0() == BaseAgreementInfo.ADD) {
							cascadeServiceAddition(cs.getService());
						} else {
							cascadeServiceDeletion(cs.getService());
						}
					}

					removeCascadingChangesWithNoWork();
					runCascadingChanges();
				}
			}
		}
	}

	//===================================================================================================

	public void save() throws TelusAPIException {
		AccountType accountType = getDefaultDealerSaleRepForContractSave();
		if(accountType == null || isDefaultDealerSalesRepNull(accountType.getDefaultDealer(), accountType.getDefaultSalesCode())){
			save(subscriber.getDealerCode(), subscriber.getSalesRepId());
		}
		else{
			save(accountType.getDefaultDealer(), accountType.getDefaultSalesCode());
		}
	}


	private static void printServices(ContractService services[]) {
		Service service;
		for (int i=0;i<services.length;i++) {
			try {
				service = services[i].getService();
				Logger.debug("service["+i+"]="+service.getCode()+" hasPromo="+service.hasPromotion()+" isPromo="+service.isPromotion()+" "+service.getDescription());
			} catch (Throwable t) {
			}
		}
	}

	private void validatePPSServices() throws TelusAPIException {
		// Validate if PP&S services are included in the contract
		// 1) accountType/subAccountType are eligible
		// 2) if the contract has PP&S AddOn it must have a PP&S Bundle and
		// addon expiry is before bundle expiry
		// Since PP&S service require immediate activation the contract should
		// have one bundle and one addon at a time

		ContractService[] includedPPSBundles = getPPSBundleServices();
		ContractService[] includedPPSAddOns = getPPSAddOnServices();
		ContractService removedPPSBundle = null;
		ContractService addedPPSAddOn = null;
		
		
		ContractService[] addedServices = getAddedServices();
		ContractService[] removedServices = getDeletedServices();
		
		for (int i = 0; i < addedServices.length; i++) {
			if (addedServices[i].getService().isPPSAddOn()) {
				addedPPSAddOn = addedServices[i];
				break;
			}
		}

		for (int i = 0; i < removedServices.length; i++) {
			if (removedServices[i].getService().isPPSBundle()) {
				removedPPSBundle = removedServices[i];
				break;
			}
		}
		// In case of migration the contract is still attached to old subscriber, so we check for the target subscriber eligibility
		char accountType = (migrationTargetSubscriber == null) ? subscriber.getAccount().getAccountType() : migrationTargetSubscriber.getAccount().getAccountType();
		char accountSubtype = (migrationTargetSubscriber == null) ? subscriber.getAccount().getAccountSubType() : migrationTargetSubscriber.getAccount().getAccountSubType();
		if ((includedPPSBundles.length + includedPPSAddOns.length) != 0) {
			if (!provider.getReferenceDataManager().isPPSEligible(accountType,accountSubtype)) {
				ContractService service = (includedPPSBundles.length != 0)? includedPPSBundles[0]:includedPPSAddOns[0];
				throw new InvalidServiceChangeException(
						InvalidServiceChangeException.ACCOUNT_INELIGIBLE,
						"Account/SubAccount not eligible for PP&S Services",
						service.getService(), service, null);
			}
		}
		if ((includedPPSAddOns.length > 0)
					&& (includedPPSBundles.length == 0)) {
			if (removedPPSBundle != null) {
				// ContractService service = (removedPPSBundle !=
				// null)?removedPPSBundle:addedPPSAddOn;
				throw new InvalidServiceChangeException(
						InvalidServiceChangeException.REQUIRED_SERVICE_IS_MISSING_ONREMOVE,
						"PP&S bundle is required for the PP&S add-on service in the contract",
						removedPPSBundle.getService(), removedPPSBundle, null);
			} else {
				throw new InvalidServiceChangeException(
						InvalidServiceChangeException.REQUIRED_SERVICE_IS_MISSING_ONADD,
						"PP&S add-on requires a PP&S bundle", addedPPSAddOn.getService(), addedPPSAddOn,
						null);
			}
		}
			//else if (includedPPSAddOns.length > 0) {
		Date addOnExpiryDate = null;
		if (includedPPSAddOns.length > 0) {
			addOnExpiryDate = includedPPSAddOns[0].getExpiryDate();
			if (includedPPSAddOns[0].getService().isPromotion()) {
				// check if the follow up is present
				if (includedPPSAddOns.length > 1) {
					addOnExpiryDate = includedPPSAddOns[1].getExpiryDate();
				} else {
					// Assume Follow up SOCK not added yet by KB and will have
					// null expiry
					addOnExpiryDate = null;
				}

			}
		}
		Date bundleExpiryDate = null;
		if (includedPPSBundles.length > 0) {
			bundleExpiryDate = includedPPSBundles[0].getExpiryDate();
			if (includedPPSBundles[0].getService().isPromotion()) {
				// check if the follow up is present
				if (includedPPSBundles.length > 1) {
					bundleExpiryDate = includedPPSBundles[1].getExpiryDate();
				} else {
					// Assume Follow up SOCK not added yet by KB and will have null expiry
					bundleExpiryDate = null;
				}

			}
		}

		if ((bundleExpiryDate != null) && (addOnExpiryDate != null)) {
			if (!addOnExpiryDate.equals(bundleExpiryDate)) {
				// For PP&S the expiry of the bundle and add-on have to be the same
				// Originallly the add-on expiry date was required to be before bundle expiry date
				throw new InvalidServiceChangeException(
						InvalidServiceChangeException.SERVICE_EXPIRYDATE_CONFLICT,
						"PP&S add-on expiry date must be equal to  the PP&S bundle expiry date",
						includedPPSAddOns[0].getService(), includedPPSAddOns[0], null);
			}
		}
		else if (bundleExpiryDate != addOnExpiryDate) {
			throw new InvalidServiceChangeException(
					InvalidServiceChangeException.SERVICE_EXPIRYDATE_CONFLICT,
					"PP&S add-on expiry date must be equal to  the PP&S bundle expiry date",
					includedPPSAddOns[0].getService(), includedPPSAddOns[0], null);
		}

		// end PP&S validation
	}
	
	public void save(String dealerCode, String salesRepCode) throws TelusAPIException, PhoneNumberException {

		subscriber.assertSubscriberExists();
		Logger.debug("Contract.save() => start ...");
		String methodName = "Contract.save(dealerCode, salesRepCode)";
		String activity = "";
		TMEquipment oldEquipment = null;
		TMEquipment newEquipment = null;
		TMEquipment newhandSet = null;
		
		validatePPSServices();
				
		
		Date contractEffectiveDate = getEffectiveDate();
		if ( contractEffectiveDate!=null ) {
			
			Date targetDate = subscriber.getStartServiceDate();
			if (contractEffectiveDate.before( targetDate ) ) {
				throw new TelusAPIException( "Contract effective date ("+  getEffectiveDate()+ ") must be after subscriber start service date ("+ targetDate+ ").");
			}

			targetDate = subscriber.getMigrationDate();
			if ( targetDate!=null ) {
				if ( contractEffectiveDate.before(targetDate) ) {
					throw new TelusAPIException( "Contract effective date ("+  getEffectiveDate()+ ") must be after subscriber migration date("+ targetDate+ ").");
				}
			}
		}

		try {
			
			if (delegate.isModified() || contractRenewal) {


				checkForVoicemailService();  //Ensure VTT featue is not improperly added
				
				
				Logger.debug("Contract.save() => delegate.isModified() || contractRenewal");
				String[] originalMultiRingPhones = subscriber.getDelegate().getMultiRingPhoneNumbers();
				String[] modifiedMultiRingPhones = getMultiRingPhoneNumbers();

				//----------------------------------------------
				// Make cards pending.
				// --the cards reference cannot be the more specific
				//   TMCard[] type, only its elements.
				//----------------------------------------------
				Card[] cards = (Card[])this.cards.toArray(new Card[this.cards.size()]);
				setCardsStatus(cards, Card.STATUS_PENDING);

				Service[] restrictedSOCsLost = removeNonMatchingRestrictedSOCs(getValidationEquipment().getNetworkType());
				
				// Fixed defect PROD00132009 - all rules pertaining to equipment type/soc relationship should be removed
				//								when swapping to an HSPA equipment or staying on an HSPA equipment
				//								Rules should be applied when swapping to or staying on non-HSPA equipment
				
				if (equipmentChangeRequest != null) { // swapping equipment
					//swapping to HSPA; override equipment type
					if (equipmentChangeRequest.getNewEquipment().isHSPA()) {
						delegate.getPricePlanValidation0().setEquipmentServiceMatch(false);
					}else { //swapping to non-HSPA; validate equipment type
						delegate.getPricePlanValidation0().setEquipmentServiceMatch(true);
					}
				} else { // no swap
					//  staying on HSPA; override equipment type
					if (subscriber.getEquipment().isHSPA()) {
						delegate.getPricePlanValidation0().setEquipmentServiceMatch(false);
					}else {//  staying on non-HSPA; validate equipment type
						delegate.getPricePlanValidation0().setEquipmentServiceMatch(true);
					}
				}

				//----------------------------------------------
				// Save contract.
				//----------------------------------------------
				try {
					if (priceplanChange) {
						Logger.debug("Contract.save() => priceplanChange");
						ContractService services[] = getServices();
						printServices(services);
						Service service;
						for (int i=0;i<services.length;i++) {
							service = services[i].getService();
							if (service.isPromotion()) {
								undoChangeToService0(service.getCode());
							}
						}
						services = getServices();
						printServices(services);

						// TODO: instead of following; change equipment & price plan, if neccesary
						// TODO: report equipment change separately
						if (equipmentChangeRequest != null) {

							StringBuffer eqcBuffer = new StringBuffer();
							
							oldEquipment = subscriber.getEquipment0();
							newEquipment = (TMEquipment)equipmentChangeRequest.getNewEquipment();
							newhandSet  = (TMEquipment)equipmentChangeRequest.getAssociatedHandset();
							
							eqcBuffer.append("Old SN(").append(oldEquipment.getSerialNumber()).append(")")
							.append(" new SN(").append(newEquipment.getSerialNumber()).append(")");
							
							EquipmentInfo[] secondaryEquipmentsInfo = null;
							Equipment[] secondaryEquipments = equipmentChangeRequest.getSecondaryEquipments();

							String newAssociatedHandsetIMEI = null;
							if (newEquipment.isHSPA()) {
								if (equipmentChangeRequest.getAssociatedHandset() != null) {
									newAssociatedHandsetIMEI = equipmentChangeRequest.getAssociatedHandset().getSerialNumber();
									eqcBuffer.append( " handsetIMEI(").append(newAssociatedHandsetIMEI).append(")");
								}
							}
									
							if (secondaryEquipments != null && secondaryEquipments.length>0) {
								secondaryEquipmentsInfo = new EquipmentInfo[secondaryEquipments.length];
								
								eqcBuffer.append( " secondaries[");
								for (int i = 0; i < secondaryEquipments.length; i++) {
									secondaryEquipmentsInfo[i] = ((TMEquipment) secondaryEquipments[i]).getDelegate();
									eqcBuffer.append( secondaryEquipments[i].getSerialNumber()).append(",");
								}
								eqcBuffer.append("]");
							}
							String equipmentContextInfo = eqcBuffer.toString();
							
							if ( newEquipment.isHSPA() && newEquipment.getSerialNumber().equals(oldEquipment.getSerialNumber()) ) {
								activity = "HSPA handset only swap with priceplanChange calling getSubscriberLifecycleFacade().changePricePlan";
								
								//  [ Covent LTE Fix - Naresh Annabathula ] update the subscriber current equipment with associated handset info  , since we require the handset info to validate the Volte logic in subscriber-ejb to add "SVOLTE" soc.
								if(subscriber.getDelegate().getEquipment0() != null && newhandSet !=null){
									subscriber.getDelegate().getEquipment0().setAssociatedHandset(newhandSet.getDelegate());
									
									/*** VOLTE SOC logic for pure handset swap during a PPC (i.e. renewal) **/
									try {
										ServiceInfo volteSocSvcInfo = provider.getSubscriberLifecycleFacade().getVolteSocIfEligible(subscriber.getDelegate(), delegate, newhandSet.getDelegate(),
												priceplanChange);
										if (volteSocSvcInfo != null) {
											addService(provider.getReferenceDataManager0().decorate(volteSocSvcInfo));
										}
									} catch (Throwable t) {
										// silent failure
									}
									/*** VOLTE logic ends **/
								}
								
								
								//HPSA handset only swap
								provider.getSubscriberLifecycleFacade().changePricePlan(
										subscriber.getDelegate(),
										delegate,
										dealerCode,
										salesRepCode,
										delegate.getPricePlanValidation0(),
										provider.getAccountNotificationSuppressionIndicator(subscriber.getBanId()), null,
										oldContractInfo,
										SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
								subscriber.logSuccess(methodName, activity, null);
								
							} else {
								activity = "PCS or IDEN (not mule to mule) swap with priceplanChange -> calling getSubscriberLifecycleFacade().changeEquipment";
								//PCS and IDEN (not mule to mule) swap - call KB to change equipment
							//  [ Covent LTE Fix - Naresh Annabathula ]  update the new equipment with associated handset info  , since we require the handset info to validate the Volte logic in subscriber-ejb to add "SVOLTE" soc.
								if(subscriber.getDelegate().getEquipment0()!=null && newhandSet !=null){
									newEquipment.getDelegate().setAssociatedHandset(newhandSet.getDelegate());
								}
								provider.getSubscriberLifecycleFacade().changeEquipment(subscriber.getDelegate(),
										oldEquipment.getDelegate(),
										newEquipment.getDelegate(),
										secondaryEquipmentsInfo,
										dealerCode,
										salesRepCode,
										equipmentChangeRequest.getRequestorId(),
										equipmentChangeRequest.getSwapType(),
										delegate,
										delegate.getPricePlanValidation0(),
										null, getProvider().getAccountNotificationSuppressionIndicator(subscriber.getBanId()), oldContractInfo,
										SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));

								subscriber.logSuccess(methodName, activity, null);
							}

							if(newEquipment.isHSPA() || oldEquipment.isHSPA()) {
								activity = "Update SEMS swapEquipmentForPhoneNumber";
								try {
										provider.getProductEquipmentLifecycleFacade().swapEquipmentForPhoneNumber(
												subscriber.getPhoneNumber(), 
												oldEquipment.getSerialNumber(),
												oldEquipment.getDelegate().getAssociatedHandsetIMEI(),
												oldEquipment.getNetworkType(),
												newEquipment.getSerialNumber(),
												newAssociatedHandsetIMEI,
												newEquipment.getNetworkType()
												);
										
									subscriber.logSuccess(methodName, activity, equipmentContextInfo);
								} catch(ApplicationException ex) {
									subscriber.logFailure(methodName, activity, ex, equipmentContextInfo);
									if (ex.getErrorCode().equals("VAL18888") || ex.getErrorCode().equals("VAL18889") || ex.getErrorCode().equals("EX"))
										throw new TelusAPIException(ex);
								} catch(SystemException se){
									subscriber.logFailure(methodName, activity, se, equipmentContextInfo);
								} catch(Throwable tr) {
									subscriber.logFailure(methodName, activity, tr, equipmentContextInfo);
									throw tr;
								}
							}
							
							if (newEquipment.isIDEN()) {
								// added logic to update DIST with SIM-Mule relation
								if (newEquipment.isSIMCard()) {
									// associate SIM with new Mule
									MuleEquipment newMule = (TMMuleEquipment) equipmentChangeRequest.getAssociatedMuleEquipment();
									if (newMule == null) {
										newMule = ((TMSIMCardEquipment)newEquipment).getLastMule();
									}
									if (newMule != null) {
											provider.getProductEquipmentManager().setSIMMule(newEquipment.getSerialNumber(),
													newMule.getSerialNumber(), new Date(), EquipmentManager.EVENT_TYPE_SIM_IMEI_ACTIVATE);
										
										//  provider.debug(">>>> Associate new SIM [" + e.getSerialNumber() +
										//                  "] with new Mule [" + newMule.getSerialNumber() + "]");

									} else if (oldEquipment.isSIMCard()) {
										// associate SIM with old Mule
										MuleEquipment oldMule = ((TMSIMCardEquipment) oldEquipment).getLastMule();
										if (oldMule != null) {
											provider.getProductEquipmentManager().setSIMMule(newEquipment.getSerialNumber(),
													oldMule.getSerialNumber(), new Date(), EquipmentManager.EVENT_TYPE_SIM_IMEI_ACTIVATE);
											
											//  provider.debug(">>>> Associate new SIM [" + e.getSerialNumber() +
											//                  "] with old Mule [" + oldMule.getSerialNumber() + "]");
										}
									}
								}
							}

							subscriber.setEquipment(newEquipment);
							provider.getInteractionManager0().subscriberChangeEquipment(subscriber, oldEquipment, equipmentChangeRequest);

						} else {
							if (originalMultiRingPhones != null && originalMultiRingPhones.length > 0) {
								delegate.setMultiRingPhoneNumbers(originalMultiRingPhones);
							}
							
							// Multi Ring
							ServiceAgreementInfo[] allServices = delegate.getServices0(true);
							for(int i=0; i<allServices.length; i++) {
								ServiceAgreementInfo aService = allServices[i];
								if (containsMultiRingFeature(aService)) {
									delegate.setMultiRingInfos(getMultiRingInfos(originalMultiRingPhones, modifiedMultiRingPhones, aService));
								}
							}
							
							
							activity = "priceplanChange only -> calling getSubscriberLifecycleFacade().changePricePlan";
							provider.getSubscriberLifecycleFacade().changePricePlan(
									subscriber.getDelegate(),
									delegate,
									dealerCode,
									salesRepCode,
									delegate.getPricePlanValidation0(),
									provider.getAccountNotificationSuppressionIndicator(subscriber.getBanId()), null,
									oldContractInfo,
									SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
							
							subscriber.logSuccess(methodName, activity, null);
						}
					} else { //if (priceplanChange) 
						// No price plan change: only service change and maybe equipment change as well

						// change equipment before services, if necessary
						if (equipmentChangeRequest != null) {
							subscriber.changeEquipment(equipmentChangeRequest, false);
							subscriber.logSuccess("Contract.save()", "subscriber.changeEquipment", null);
						}

						// Multi Ring
						ServiceAgreementInfo[] allServices = delegate.getServices0(true);
						ServiceAgreementInfo aService = null;
						for(int i=0; i<allServices.length; i++) {
							aService = allServices[i];
							if (containsMultiRingFeature(aService)) {
								Logger.debug("yyyy = Services contains Multi-Ring Feature = " + aService.getCode());
								delegate.setMultiRingInfos(getMultiRingInfos(
										originalMultiRingPhones, modifiedMultiRingPhones, aService));
								printMultiRingInfo(delegate.getMultiRingInfos());
							}
						}
						
						activity ="getSubscriberLifecycleFacade().changeServiceAgreement";
						if (getSubscriber().getAccount().isPrepaidConsumer() && containsNotSavedPrepaidCallingCircleService()){
							ServiceAgreementInfo callingCircleService = getNotSavedPrepaidCallingCircleService();
							changePrepaidCallingCircleService(callingCircleService, dealerCode, salesRepCode);
						} else {						
							provider.getSubscriberLifecycleFacade().changeServiceAgreement(
									subscriber.getDelegate(),
									delegate,
									dealerCode,
									salesRepCode,
									delegate.getPricePlanValidation0(),
									provider.getAccountNotificationSuppressionIndicator(subscriber.getBanId()),	null,
									SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
						}
						subscriber.logSuccess("Contract.save()", "getSubscriberLifecycleFacade().changeServiceAgreement", null );
					}
				} catch (Throwable t) {
					setCardsStatus(cards, Card.STATUS_LIVE);
					subscriber.logFailure(methodName, activity, t, null);
					throw t;
				}

				//----------------------------------------------
				// Activate cards.
				//----------------------------------------------
				activity = "activate Cards";
				for(int i=0; i < cards.length; i++) {
					TMCard c = (TMCard)cards[i];
					provider.getProductEquipmentManager().activateCard(c.getSerialNumber(),subscriber.getBanId(),
							subscriber.getPhoneNumber(), subscriber.getEquipment().getSerialNumber(),
							c.getDelegate().getAutoRenew(), provider.getUser());
				}


				//-----------------------------------------------------------
				// Remove the secondary service from another active subscriber
				// sharing this plan if this subscriber was primary on a shareable
				// priceplan.
				//-----------------------------------------------------------
				// FYI, the following assumes "this.subscriber.contract" has
				// been previously set to the old contract, since calling
				// this.subscriber.getContract0() otherwise would load the
				// new contract information from the database.
				//-----------------------------------------------------------
				if (priceplanChange) {
					if (subscriber.getContract0().isShareablePricePlanPrimary()) {
						subscriber.yeildShareablePricePlanPrimaryStatus(dealerCode, salesRepCode, getEffectiveDate());
					}
				}
			
				provider.getInteractionManager0().contractSave(this, dealerCode, salesRepCode);
				commit(dealerCode, salesRepCode);

				// Switch between paper and e-bills for Ampd accounts.
				if (changeInvoiceFormat) {
					changeInvoiceFormat();
				}

				//----------------------------------------------
				// VoiceMail Bug fix:
				//   Since some voicemail services have no
				//   conflicting feature, the old service
				//   needs to be removed or future dated
				//   manually.
				//----------------------------------------------
				try {
					sendVoiceMailBundleCardFollowUp(cards);
				} catch(Throwable e) {
					Logger.warning(e);
				}

			} else if (equipmentChangeRequest != null) {
				// perform the swap even if there's no contract change
				subscriber.changeEquipment(equipmentChangeRequest, false);
				equipmentChangeRequest = null;
			}
			
			Logger.debug("Contract.save() => saveCommitment");
			saveCommitment(dealerCode, salesRepCode);

			if (serviceToAdd.size() > 0) {
				serviceToAdd.clear();
			}

			if (serviceToRemove.size() > 0) {
				serviceToRemove.clear();
			}
			serviceChangeHistory = null;

			if (oldEquipment != null && newEquipment != null && oldEquipment.isCDMA() && newEquipment.isHSPA()) {
				refreshSocAndFeatures();
			}

		} catch (Throwable t) {
			subscriber.logFailure(methodName, activity, t, null);
			provider.getExceptionHandler().handleException(t, new ProviderServiceChangeExceptionTranslator());
		}

		Logger.debug("Contract.save() => exit ...");
	}

	private void saveCommitment(String dealerCode, String salesRepCode) throws TelusAPIException {
		try {
			CommitmentInfo commitment = delegate.getCommitment();
			if(commitment.isModified()) {
				provider.getSubscriberLifecycleFacade().updateCommitment(subscriber.getDelegate(), commitment, dealerCode, salesRepCode, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
				commitment.setModified(false);
			}
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
	}

	public void setCardsStatus(Card[] cards, int status) throws TelusAPIException {
		try {
			for(int i=0; i < cards.length; i++) {
				provider.getProductEquipmentManager().setCardStatus(cards[i].getSerialNumber(), status, provider.getUser());
			}
		}catch (Throwable t) {
				provider.getExceptionHandler().handleException(t);
		}
	}

	public void refresh() throws TelusAPIException {
		if(!activation) {
			TMContract contract = subscriber.getContract0(true);
			this.delegate = contract.delegate;
			this.pricePlan = contract.pricePlan;
			cards.clear();
			subscriber.refrshCancellationPenalty();
		} else {
			// TODO: create new contract and copy from it
		}
	}

//	public Service testAddition(Service service, Date effectiveDate, Date expiryDate) throws InvalidServiceChangeException, TelusAPIException {
//	return testAddition(service, effectiveDate, expiryDate, true, true);
//	}

	private boolean allowDuplicateService(Service service) throws TelusAPIException {
		//provider.debug("allowDuplicateService(" + service.getCode() + ")");
		if(service.isWPS()) {
			ContractService contractService = getService(service.getCode());
			Date today = provider.getReferenceDataManager().getSystemDate();
			Date expiryDate = contractService.getExpiryDate();

			if(expiryDate == null) {
				return false;
			}

			long daysUntilExpiry = (expiryDate.getTime() - today.getTime() + DAY - 1) / DAY;
			//provider.debug("    [" + service.getCode() + "].daysUntilExpiry=" + daysUntilExpiry);
			//provider.debug("lastExpiryDate=[" + new Date(provider.getReferenceDataManager().getSystemDate().getTime() + (PREPAID_MAXIMUM_EXPIRY_DAYS * DAY)) + "]");


			return daysUntilExpiry < (service.getMaxConsActDays() - 1);
		} else {
			return false;
		}
	}

	private void sendVoiceMailBundleCardFollowUp(Card[] cards) throws TelusAPIException {
		if(cards.length > 0) {
			boolean sendfollowUp = false;

			String[] voiceMailServices = AppConfiguration.getVoiceMailFeatureCardServices();


			//------------------------------------------------------
			// Test Cards
			//------------------------------------------------------
			CARD_LOOP:
				for(int i=0; i < cards.length; i++) {
					TMCard card = (TMCard)cards[i];

					if(card.getProductTypeId().equals(Card.PRODUCT_TYPE_CALLERID_VOICEMAIL25_CALL_FORWARDING_BUNDLE)) {
						for (int j=0; j<voiceMailServices.length; j++) {
							if(delegate.containsService0(voiceMailServices[j], false)) {
								sendfollowUp = true;
								break CARD_LOOP;
							}
						}
					}
				}


			//------------------------------------------------------
			// Send FollowUp
			//------------------------------------------------------
			if (sendfollowUp) {

				FollowUp followUp = subscriber.newFollowUp();

				if(subscriber.getMarketProvince().equals("BC")) {
					followUp.setAssignedToWorkPositionId(AppConfiguration.getVoiceMailFeatureCardFollowUpGroupBC());
				} else {
					followUp.setAssignedToWorkPositionId(AppConfiguration.getVoiceMailFeatureCardFollowUpGroup());
				}

				followUp.setDueDate(provider.getReferenceDataManager().getSystemDate());
				followUp.setText("Feature Card Voice Mail bundle provisioned, but requires future dating of existing Voice Mail services");
				//followUp.setProductType(AccountManager.PRODUCT_TYPE_PCS);
				followUp.setFollowUpType(AppConfiguration.getVoiceMailFeatureCardFollowUpType());

				//provider.debug(followUp);

				followUp.create();
			}
		}

	}

	public TMSubscriber getSubscriber(){
		return subscriber;
	}

	public boolean isActivation(){
		return activation;
	}

	public boolean isPriceplanChange(){
		return priceplanChange;
	}

	public String toString()
	{
		StringBuffer s = new StringBuffer(128);

		s.append("TMContract:[\n");
		s.append("    activation=[").append(activation).append("]\n");
		s.append("    priceplanChange=[").append(priceplanChange).append("]\n");
		s.append("    contractRenewal=[").append(contractRenewal).append("]\n");
		s.append("    equipmentChangeRequest=[").append(equipmentChangeRequest).append("]\n");
		s.append("    delegate=[").append(delegate).append("]\n");
		s.append("]");

		return s.toString();
	}

	//--------------------------------------------------------------------
	//  Decorator Methods
	//--------------------------------------------------------------------
	public TMContractService decorate(ContractService contractService) {
		if(!(contractService instanceof TMContractService)){
			contractService = new TMContractService(provider, (ServiceAgreementInfo)contractService, subscriber);
			((TMContractService)contractService).setContract(this);

		}

		return (TMContractService)contractService;
	}

	public ContractService[] decorate(ContractService[] contractServices) {
		ContractService[] decoratedContractServices = new ContractService[contractServices.length];
		for(int i=0; i<contractServices.length; i++) {
			decoratedContractServices[i] = decorate(contractServices[i]);
		}
		return decoratedContractServices;
	}
	public ContractFeature decorate( ContractFeature contractFeature ) {
		if ( (contractFeature instanceof TMContractFeature)==false ) {
			contractFeature = new TMContractFeature( provider, (ServiceFeatureInfo) contractFeature, subscriber );
			((TMContractFeature)contractFeature).setContract(this);
		}
		return contractFeature;
	}
	public ContractFeature[] decorate( ContractFeature[] contractFeatures ) {
		ContractFeature[] decoratedFeatures = new ContractFeature[contractFeatures.length];
		for( int i=0; i<contractFeatures.length; i++) {
			decoratedFeatures[i] = decorate( contractFeatures[i]);
		}
		return decoratedFeatures;
	}

	//--------------------------------------------------------------------
	//  Undecorator Methods
	//--------------------------------------------------------------------
	public ContractService undecorate(ContractService contractService) {
		if(contractService instanceof TMContractService){
			contractService = ((TMContractService)contractService).getDelegate();
		}

		return contractService;
	}

	public ContractService[] undecorate(ContractService[] contractServices) {
		ContractService[] undecoratedContractServices = new ContractService[contractServices.length];
		for(int i=0; i<contractServices.length; i++) {
			undecoratedContractServices[i] = undecorate(contractServices[i]);
		}
		return undecoratedContractServices;
	}

	public boolean isCrossFleetRestricted(){
		try{
			getService(ServiceSummary.HORIZONTAL_CROSSFLEET_CROSSDAP_ZERO_CHARGE);
			return true;
		}catch(UnknownObjectException e){
			try{
				getService(ServiceSummary.HORIZONTAL_CROSSFLEET_CROSSDAP_TWO_CHARGE);
				return true;
			}catch(UnknownObjectException ex){
				return false;
			}
		}
	}

	/**
	 * @deprecated
	 * @return boolean
	 */
	 public boolean isPTTServiceIncluded() {

		 ContractService[] services = getServices();
		 if (services == null || services.length == 0)
			 return false;

		 ContractService service = null;

		 try {
			 for (int i = 0; i < services.length; i++) {
				 service = services[i];
				 if (service == null || service.getService() == null)
					 return false;
				 if (service.getService().isPTT()) {
					 return true;
				 }
			 }
		 } catch (TelusAPIException e) {

		 }

		 return false;

	 }

	 public boolean containsService0(String code, boolean includeDeleted) {
		 return delegate.containsService0(code, includeDeleted);
	 }

	 public boolean containsService(String code) {
		 return containsService0(code, false);
	 }

	 public boolean isSuppressPricePlanRecurringCharge() throws TelusAPIException {
		 return isShareablePricePlanSecondary();
	 }

	 public double getRecurringChargeForShareableServices() throws TelusAPIException {
		 return getRecurringCharge(true, true, false);
	 }

	 public double getRecurringCharge() throws TelusAPIException {
		 return getRecurringCharge(isSuppressPricePlanRecurringCharge());
	 }

	 public double getRecurringCharge(boolean suppressPricePlanRecurringCharge) throws TelusAPIException {
		 return getRecurringCharge(suppressPricePlanRecurringCharge, true, true);
	 }

	 public double getRecurringCharge(boolean suppressPricePlanRecurringCharge, boolean includeShareableServices, boolean includeNonShareableServices) throws TelusAPIException {
		 if(pricePlan == null) {
			 throw new NullPointerException("pricePlan == null, use the API");
		 }

		 try {
			 Date alternateRecurringChargeStartDate = null;

			 double charge = (suppressPricePlanRecurringCharge) ? 0.0 : pricePlan.getRecurringCharge();

			 ContractService[] services = getServices();
			 for (int i = 0; i < services.length; i++) {
				 Service s = services[i].getService();
				 if (s.getRecurringChargeFrequency() == ServiceSummary.PAYMENT_FREQUENCY_MONTH) {
					 if ((includeShareableServices && s.isSharable()) || (includeNonShareableServices && !s.isSharable())) {
						 if (s.hasAlternateRecurringCharge()) {

							 // get alternateRecurringChargeStartDate lazily
							 if (alternateRecurringChargeStartDate == null) {
								 alternateRecurringChargeStartDate = provider.getReferenceDataHelperEJB().
								 retrieveAlternateRCContractStartDate(subscriber.getMarketProvince());
							 }

							 if (getEffectiveDate() == null || alternateRecurringChargeStartDate == null || !getEffectiveDate().before(alternateRecurringChargeStartDate)) {
								 charge += s.getAlternateRecurringCharge(subscriber);
							 } else {
								 charge += s.getRecurringCharge();
							 }
						 } else {
							 charge += s.getRecurringCharge();
						 }
					 }
				 }

			 }

			 return charge;
		 } catch (TelusException e) {
			 throw new TelusAPIException(e);
		 } catch (Throwable e) {
			 throw new TelusAPIException(e);
		 }
	 }

	 public boolean isShareablePricePlanPrimary() throws TelusAPIException {
		 if(pricePlan.isSharable()) {
			 ShareablePricePlan p = (ShareablePricePlan)pricePlan;

			 return !containsService(p.getSecondarySubscriberService());
		 }
		 return false;
	 }

	 public boolean isShareablePricePlanSecondary() throws TelusAPIException {
		 return pricePlan.isSharable() && !isShareablePricePlanPrimary();
	 }



	 private ServiceAgreementInfo addServiceImpl(Service service, Date effectiveDate, Date expiryDate) throws TelusAPIException {
		 Logger.debug("            +\"" + service.getCode() + "\";    effective " + effectiveDate + " to " + expiryDate);
		 ServiceInfo info;
		 if (service instanceof ServiceInfo) {
			 info = (ServiceInfo) service;
		 }
		 else {
			 info = ( (TMService) service).getDelegate();
		 }

		 ServiceAgreementInfo cs;
		 if (service.isWPS()) {
//			 cs = addPrepaidServiceImpl(info, effectiveDate, expiryDate);
			 cs = addPrepaidServiceImpl(info, effectiveDate);
			 Logger.debug("              (+\"" + service.getCode() + "\";    effective " + cs.getEffectiveDate() + " to " + cs.getExpiryDate() + ";  Prepaid)");
		 }
		 else {
			 cs = delegate.addService(info, effectiveDate, true);
			 if (cs.getTransaction() != ServiceAgreementInfo.NO_CHG) {
				 if (!service.isIncludedPromotion() || effectiveDate != null || expiryDate != null) {
					 cs.setEffectiveDate(effectiveDate);
					 cs.setExpiryDate(expiryDate);
				 }
			 }
		 }

		 if (cs != null) {
			 serviceToAdd.add(cs.getService());
			 serviceToRemove.remove(cs.getService()); // in case user selected to remove before
		 }

		 return cs;
	 }

	 private ServiceAgreementInfo addPrepaidServiceImpl(ServiceInfo service, Date effectiveDate) throws TelusAPIException {
		 return addPrepaidServiceImpl(service, effectiveDate, null);
	 }
	 
	 private ServiceAgreementInfo addPrepaidServiceImpl(ServiceInfo service, Date effectiveDate, Date expiryDate) throws TelusAPIException {
		 Logger.debug("addPrepaidService(" + service.getCode() + ", " + effectiveDate + ", " + expiryDate + ")");
		 // expiryDate is ignored for non-LBM features

		 if(!service.isWPS()) {
			 throw new TelusAPIException("The service is not Prepaid service: [" + service.getCode() + "]");
		 }

		 Date today = provider.getReferenceDataManager().getSystemDate();
		 long lastExpiryDate = today.getTime() + ((service.getMaxConsActDays()-1) * DAY);
		 long newExpiryDate;

		 ServiceAgreementInfo serviceAgreement;

		 //provider.debug("lastExpiryDate=[" + new Date(lastExpiryDate) + "]");

		 if(delegate.containsService0(service.getCode(), false)) {   // Duplicate service
			 serviceAgreement = delegate.getService0(service.getCode(), false);

			 //--------------------------------------------------------------
			 // Bug Fix:  Since wps/prepaid services can have different
			 //           duration based on how they are retrieved, we
			 //           always need to update the serviceAgreement's
			 //           meta information.
			 //--------------------------------------------------------------
			 serviceAgreement.setService(service);
			 serviceAgreement.setTransaction(BaseAgreementInfo.ADD);  // Always set prepaid transaction as ADD
			 
			 newExpiryDate  = serviceAgreement.getExpiryDate().getTime() + (service.getTerm() * DAY);  // Assume termUnits are in days
		 } else {                                            // New service
			 serviceAgreement = delegate.addService(service, effectiveDate, true);
			 newExpiryDate  = today.getTime() + (service.getTerm() * DAY);                             // Assume termUnits are in days
		 }

		 if (service.isPrepaidLBM()) {
			 serviceAgreement.setEffectiveDate(effectiveDate);
			 serviceAgreement.setExpiryDate(expiryDate);
		 }else {
			 newExpiryDate = Math.min(lastExpiryDate, newExpiryDate);	 
			 serviceAgreement.setExpiryDate(new Date(newExpiryDate));
		 }

		 //provider.debug(serviceAgreement);

		 return serviceAgreement;
	 }

	 private ServiceAgreementInfo removeServiceImpl(String serviceCode) {
		 Logger.debug("            -\"" + serviceCode + "\"");
		 return delegate.removeService0(serviceCode);
	 }

	 private ServiceAgreementInfo undoRemoveServiceImpl(String serviceCode) {
		 Logger.debug("            -\"" + serviceCode + "\"");
		 return delegate.undoRemoveService0(serviceCode);
	 }

//	 =========================================================================================================================

	 private Service testAddition0(Service service, Date effectiveDate, Date expiryDate, boolean allowBoundAndPromotional, boolean allowIncludedOptionalConflict) throws InvalidServiceChangeException, TelusAPIException {

		 
		ServiceInfo svcInfo;
		if (service instanceof ServiceInfo)
			svcInfo = (ServiceInfo) service;
		else if (service instanceof TMService)
			svcInfo = ((TMService) service).getDelegate();
		else {
			String serviceClassname = service.getClass().getName();
			throw new SystemException(SystemCodes.PROVIDER, ErrorCodes.GENERIC_THROWABLE_ERROR_CODE, 
				"Client API Coding Error: expecting service input parameter to be an instance of either ServiceInfo or TMService but instead is: " + serviceClassname, 
				"API Client erreur de codage: attendons paramètre d'entrée service être soit une instance de ServiceInfo ou TMService mais est plutôt: " + serviceClassname);
		}
			
	  	String[]  familyTypes= service.getFamilyTypes();
		for (int j = 0; j < familyTypes.length; j++) {
			if (familyTypes[j].equals(ServiceSummary.FAMILY_TYPE_CODE_BUSINESS_ANYWHERE)) {
				try {
					provider.getSubscriberLifecycleFacade().testServiceAddToBusinessAnywhereAccount(((TMAccount) subscriber.getAccount()).getDelegate0(), svcInfo);
				} catch (ApplicationException e) {
					if (e.getErrorCode().equals(ErrorCodes.ERROR_INCOMPATIBLE_SERVICE_ACCOUNT)) {
						throw new InvalidServiceChangeException(InvalidServiceChangeException.UNAVAILABLE_SERVICE, e);
					} else {
						provider.getExceptionHandler().handleException(e);
					}
				}
			}
		}
	     
		 testGrandFatheredPricePlan();
		 boolean isCombinedVoiceMail = isCombinedVoiceMail(service);

		 try {
			 subscriber.testService(service, getValidationEquipment());
		 } catch (InvalidServiceException e) {
			 throw new InvalidServiceChangeException(e.getReason(), e);
		 }

		 if(!allowBoundAndPromotional) {
			 if (service.isPromotion()) {
				 throw new InvalidServiceChangeException(InvalidServiceChangeException.PROMOTIONAL_SERVICE, "service can only be added by system: " + service.getCode());
			 }

			 if (service.isBoundService()) {
				 throw new InvalidServiceChangeException(InvalidServiceChangeException.BOUND_SERVICE, "service can only be added by system: " + service.getCode());
			 }

			 if (service.isSequentiallyBoundService()) {
				 throw new InvalidServiceChangeException(InvalidServiceChangeException.BOUND_SERVICE, "service can only be added by system: " + service.getCode());
			 }

			 /*
      if (!service.isAvailable()) {
        provider.debug(service);
        throw new InvalidServiceChangeException(InvalidServiceChangeException.UNAVAILABLE_SERVICE, service.getCode());
      }
			  */
		 }
		 
		 // RLH fix; need to map services by Code+effectiveDate, if possible
		 String serviceMappingCode = ClientApiUtils.getContractServiceMappingKey(service.getCode(), effectiveDate);
		 if (delegate.containsService0(serviceMappingCode, false) && !allowDuplicateService(service)) {
			 ContractService oldService = delegate.getService(serviceMappingCode);
			 if (isPeriodOverlapping(effectiveDate,expiryDate,oldService.getEffectiveDate(),oldService.getExpiryDate()))
				 throw new InvalidServiceChangeException(InvalidServiceChangeException.DUPLICATE_SERVICE, service.getCode(), getService(serviceMappingCode), null);
		 }

		 // SOC Compatibility, June 1, 2005, by MQ
		 validateServiceCompatibility(service,effectiveDate,expiryDate);

		 RatedFeature[] feature = service.getFeatures();
		 for (int i = 0; i < feature.length; i++) {
			 RatedFeature r = feature[i];

			 if (!r.isDuplFeatureAllowed() && delegate.containsFeature0(r.getCode(), effectiveDate, expiryDate, true, false)) {
				 ServiceAgreementInfo conflictingService = delegate.getServiceByFeature0(r.getCode(), effectiveDate, expiryDate, false);
				 String serviceCode = (conflictingService != null) ? conflictingService.getServiceCode() : null;

				 if (!allowIncludedOptionalConflict) {
					 // Handle conflicts with Combined Voice Mail services and features.
					 if (isCombinedVoiceMail) {
						 // Allow the testAddition0 to proceed - break out of the iteration and continue with the for loop.
						 continue;
					 } else {
						 // Throw the InvalidServiceChangeException only if the service is NOT Combined Voice Mail.
						 throw new InvalidServiceChangeException(InvalidServiceChangeException.FEATURE_CONFLICT, r.getCode(), conflictingService, r.getCode());
					 }
				 }
			 }
		 }

		 return service;
	 }

	 private boolean isPeriodOverlapping(Date firstEffectiveDate, Date firstExpiryDate, Date secondEffectiveDate, Date secondExpiryDate) {
		 if (secondEffectiveDate!=null && firstEffectiveDate!=null && firstExpiryDate!=null) {
			 if (secondExpiryDate==null) {
				 return firstExpiryDate.compareTo(secondEffectiveDate)>0;
			 } else {
				 return (!((firstExpiryDate.compareTo(secondEffectiveDate)<=0)||(firstEffectiveDate.compareTo(secondExpiryDate)>=0)));
			 }
		 }
		 if (firstEffectiveDate!=null) {  //expiryDate==null
			 if (secondExpiryDate==null) {
				 return true;
			 }
			 return firstEffectiveDate.compareTo(secondExpiryDate)<0;
		 }
		 return true;
	 }

	 private void validateServiceCompatibility(Service service, Date effectiveDate, Date expiryDate) throws InvalidServiceChangeException, TelusAPIException {

		 ContractService[] services = getOptionalServices();

		 // DO not check if already on contract - to handle prepaid
		 if (containsService(service.getCode())){
			 return;
		 }

		 String code = null;
		 ServiceExclusionGroups tempSEGInfo = null;
		 HashMap existingSEGInfos = new HashMap();

		 for (int i = 0; i < services.length; i++) {
			 if (!isPeriodOverlapping(effectiveDate,expiryDate,services[i].getEffectiveDate(),services[i].getExpiryDate()))
				 continue;
			 code = services[i].getCode();
			 tempSEGInfo = provider.getReferenceDataManager0().getServiceExclusionGroups(code);
			 if (tempSEGInfo != null) {
				 existingSEGInfos.put(code, tempSEGInfo);
			 }
		 }

		 ServiceExclusionGroups newSEGInfo = provider.getReferenceDataManager0().getServiceExclusionGroups(service.getCode());
		 String[] newExclusionGroups = null;
		 String[] oldExclusionGroups = null;

		 if (newSEGInfo != null && existingSEGInfos.size() > 0) {

			 newExclusionGroups = newSEGInfo.getExclusionGroups();

			 Set keys = existingSEGInfos.keySet();
			 Iterator keyIterator = keys.iterator();
			 Collection values = existingSEGInfos.values();
			 Iterator valueIterator = values.iterator();

			 while(valueIterator.hasNext() && keyIterator.hasNext()) {
				 oldExclusionGroups = ((ServiceExclusionGroupsInfo)valueIterator.next()).getExclusionGroups();
				 code = (String)keyIterator.next();

				 for(int k=0; k<oldExclusionGroups.length; k++) {
					 for(int j=0; j<newExclusionGroups.length; j++) {
						 if (oldExclusionGroups[k].equals(newExclusionGroups[j])) {
							 // int reason, String message, ContractService contractService, String featureCode
							 throw new InvalidServiceChangeException(InvalidServiceChangeException.SERVICE_CONFLICT,
									 "service conflict: [" + code + "] ",
									 getService(code),
									 null);
						 }
					 }
				 }
			 }
		 }
	 }

	 private ContractService testRemoval0(ContractService contractService, boolean allowBoundAndPromotional) throws InvalidServiceChangeException, TelusAPIException {
		 if(!allowBoundAndPromotional) {
			 Service service = contractService.getService();
			 /*
   if (service.isPromotion()) {
        throw new InvalidServiceChangeException(InvalidServiceChangeException.PROMOTIONAL_SERVICE, "service can only be removed by system: " + service.getCode());
      }
			  */
			 if (service.isBoundService() && !service.isVisto()) {
				 throw new InvalidServiceChangeException(InvalidServiceChangeException.BOUND_SERVICE, "service can only be removed by system: " + service.getCode());
			 }

			 if (service.isSequentiallyBoundService() && !service.isVisto()) {
				 throw new InvalidServiceChangeException(InvalidServiceChangeException.BOUND_SERVICE, "service can only be removed by system: " + service.getCode());
			 }
		 }

		 return contractService;
	 }


//	 =========================================================================================================================
	 /**
	  * These change are made to handle included Features conflicting with optional Services.  
	  * In order to handle conflicts we're expiring the optional service feature that has been added.  Included Features
	  * cannot be future dated or expired.
	  */
	 private ArrayList listConflictingFeatures = new ArrayList();
	 private ServiceAgreementInfo addService0(Service service, Date effectiveDate, Date expiryDate, boolean allowBoundAndPromotional) throws InvalidServiceChangeException, TelusAPIException {
		 ServiceAgreementInfo info =  addService00(service, effectiveDate, expiryDate, allowBoundAndPromotional);
		 for(int i = 0; i < listConflictingFeatures.size(); i++){
			 ContractFeature contractFeature =  (ContractFeature)listConflictingFeatures.get(i);
			 info.removeFeature(contractFeature.getCode());
			 addFeature(contractFeature.getFeature());                    
			 Logger.debug(info);
		 }
		 listConflictingFeatures.clear();
		 return info;
	 }


	 private ServiceAgreementInfo addService00(Service service, Date effectiveDate, Date expiryDate, boolean allowBoundAndPromotional) throws InvalidServiceChangeException, TelusAPIException {
		 try{
			 
			 //Check if account is eligible for PP&S and PP&S service activation is immediate 
			 
			 if (service.isPPSAddOn() || service.isPPSBundle()) {
				 // In case of migration the contract is still attached to old subscriber, so we check for the target subscriber eligibility
				 char accountType = (migrationTargetSubscriber == null) ? subscriber.getAccount().getAccountType() : migrationTargetSubscriber.getAccount().getAccountType();
				 char accountSubtype = (migrationTargetSubscriber == null) ? subscriber.getAccount().getAccountSubType() : migrationTargetSubscriber.getAccount().getAccountSubType();
				 if (!provider.getReferenceDataManager().isPPSEligible(accountType, accountSubtype))
						throw new InvalidServiceChangeException(
								InvalidServiceChangeException.ACCOUNT_INELIGIBLE,
								"Account/SubAccount not eligible for PP&S Services",
								service, null, null);
				 
				 if (effectiveDate != null
						&& getLogicalDate().before(effectiveDate)) {				 
					 throw new InvalidServiceChangeException(InvalidServiceChangeException.SERVICE_ACTIVATIONDATE_CONFLICT, "PP&S services require immediate activation [" + service.getCode() + "]",
							   service,
							   null, null);
				 }
			 }
			 
			 //End PP&S check
			 
			 if (priceplanChange && service.isVisto()) {
				 TMContract oldContract = this.subscriber.getContract0();
				 Service oldVisto = oldContract.hasVisto(true);
				 if (oldVisto != null && //old visto exists
						 !service.getCode().equals(oldVisto.getCode()) && //trying to add new visto soc
						 !pricePlan.containsIncludedService(service.getCode()) && //the adding soc is not an included soc. skip because this is handled in TMContract constructor
						 oldVisto.isNetworkEquipmentTypeCompatible(getValidationEquipment())) { //old visto is compatible with current equipment's network
					 //if it is a PPC and a Visto SOC is to be added as optional services, check if there is legacy Visto SOC first.
					 //if there is and is compatible with network, add the old one instead of the new one. (Tsz Chung Tong (tongts), Jul, 2007)
					 Service currentVistoService = hasVisto(false);
					 if (currentVistoService != null) { //remove any previously added Visto service before adding the legacy one
						 removeService(currentVistoService.getCode());
						 Logger.debug("removed Visto SOC "+currentVistoService.getCode());
					 }
					 oldVistoPreserved = true;
					 Logger.debug("preserved Visto SOC "+oldVisto);
					 return addServiceImpl(oldVisto, effectiveDate, expiryDate); //July 2007 Visto project:add the old Visto (if exists) instead of new one and do not add the new one
				 }
			 }
			 testAddition0(service, effectiveDate, expiryDate, allowBoundAndPromotional, false);
			 return addServiceImpl(service, effectiveDate, expiryDate);
		 } catch(InvalidServiceChangeException e) {
			 //-----------------------------------------------------------------
			 // If adding an optional service causes a FEATURE_CONFLICT with
			 // an included service, remove the included service.
			 //-----------------------------------------------------------------
			 if (e.getReason() == InvalidServiceChangeException.FEATURE_CONFLICT) {
				 if(e.getContractService() != null) {  // Service-level conflict
					 String code = e.getContractService().getService().getCode();
             //	  add by sutha to fix optional service getting removed
			 //assumption: if a service is NOT included in a price plan, then we treat it as optional service: M.Liao
					 if(pricePlan.containsIncludedService(code)==false){ 
						 throw e;
					 }
					 try{
						 if (effectiveDate != null
								 && getLogicalDate().before(effectiveDate)) {
							 e.getContractService().setExpiryDate(effectiveDate);
						 }else{    
							 removeService(code);
						 }  
					 }catch(TelusAPIException rm){
						 throw e;
					 }
					 return addService00(service, effectiveDate, expiryDate, allowBoundAndPromotional);
				 } else { // Feature-level conflict
					 String code = e.getFeatureCode();
					 if (effectiveDate != null
							 && getLogicalDate().before(effectiveDate)) {
						 ContractFeature contractFeature = findContractFeature(code);
						 removeFeature(code);
						 listConflictingFeatures.add(contractFeature);
					 } else {
						 removeFeature(code);
					 }
					 return addService00(service, effectiveDate, expiryDate,
							 allowBoundAndPromotional);
				 }
			 }
			 throw e;
		 }
	 }

	 private ServiceAgreementInfo addService0(String serviceCode, Date effectiveDate, Date expiryDate, boolean allowBoundAndPromotional) throws InvalidServiceChangeException, TelusAPIException {
		 serviceCode = Info.padService(serviceCode);

		 Service service;
		 try {
			 service = pricePlan.getService(serviceCode);
		 } catch (UnknownObjectException e) {
			 service = provider.getReferenceDataManager().getRegularService(serviceCode);
		 }

		 return addService0(service, effectiveDate, expiryDate, allowBoundAndPromotional);
	 }


	 private ServiceAgreementInfo removeService0(String serviceCode, boolean allowBoundAndPromotional) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException {
		 ServiceAgreementInfo s = delegate.getService0(serviceCode, false);

		 if(s == null) {
			 throw new UnknownObjectException("Service does not exist", serviceCode);
		 }

		 testRemoval0(s, allowBoundAndPromotional);
		 removeServiceImpl(serviceCode);

		 if (s != null) {
			 serviceToAdd.remove(s.getService()); // in case user selected to add before
			 serviceToRemove.add(s.getService());
		 }

		 return s;
	 }

	 private ServiceAgreementInfo undoChangeToService0(String serviceCode) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException {
		 ServiceAgreementInfo s = delegate.getService0(serviceCode, false);

		 if(s == null) {
			 throw new UnknownObjectException("Service does not exist", serviceCode);
		 }

		 //testRemoval0(s, allowBoundAndPromotional);
		 undoRemoveServiceImpl(serviceCode);

		 /*if (s != null) {
      serviceToAdd.remove(s.getService()); // in case user selected to add before
      serviceToRemove.add(s.getService());
    }*/

		 return s;
	 }

//	 =========================================================================================================================
	 ServiceChangeHistory[] serviceChangeHistory = null;  //don't want retrieve more then one time 
	 
	 private boolean isServiceExistInChangeHistory(String code) throws TelusAPIException {
        if (serviceChangeHistory == null) {
            Calendar calToday = Calendar.getInstance();
            Date to = calToday.getTime();
            Date from = subscriber.getContract().getCommitmentStartDate() != null ? subscriber.getContract().getCommitmentStartDate() : subscriber.getStartServiceDate();
            serviceChangeHistory = subscriber.getServiceChangeHistory(from, to);
        }
        for (int j = 0; j < serviceChangeHistory.length; j++) {
            if (serviceChangeHistory[j].getServiceCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
	 
	 public void addBoundServices(Service service, Date effDate, Date expDate) throws TelusAPIException {
		 Logger.debug("---->     addBoundServices("+service.getCode()+")");

		 if(service.hasBoundService() || service.hasSequentiallyBoundService()) {
			 ServiceRelation[] relations = service.getRelations(getPricePlan());
			 Calendar calToday = Calendar.getInstance();
			 Date today = new Date(calToday.get(Calendar.YEAR) - 1900, calToday.get(Calendar.MONTH), calToday.get(Calendar.DAY_OF_MONTH));
			 int compareDates = 0;
			 boolean valid = false;

			 for(int i=0; i < relations.length; i++) {
				 Service s = relations[i].getService().getService();
				 if ( expDate != null ) {
					 compareDates = today.compareTo(expDate);
				 }
				 //-------------------------------------------------------------
				 // We only handle bound and sequentially-bound services
				 // since AMDOCS handles the promotions.
				 //-------------------------------------------------------------
				 if (s.isBoundService()) {
                    if (expDate == null || compareDates < 0) {
                        valid = testBoundServiceAddition(service, s);
                    }
                    if (valid) {
                        String serviceType = s.getServiceType();
                        if (!activation && (serviceType.equals("G") || serviceType.equals("T"))) {
                            boolean skipLogic = subscriber.getContract().containsService(s.getCode());
                            if (!skipLogic) {
                                if (contractRenewal) {
                                    effDate = today;
                                } else {
                                	//TODO: condition below was added for Roaming Passes CR (Sept 2010) and is intended as a temporary fix.
                                	//	    The condition plus the call to isServiceExistInChangeHistory should be refactored into a permanent solution.
                                	if (s.getFamilyGroupCodes(ROAMING_PASSES_BOUND_SOC_FAMILY_TYPE).length == 0) {
                                		valid = !isServiceExistInChangeHistory(s.getCode());
                                	}
                                	if(valid && effDate != null && getLogicalDate().after(effDate)) {
                                		effDate = today;  
                                	}
                                }
                            }

                        }
                        if(valid) {
                         addService0(s, effDate, expDate, true);
                        }
                    }
				 } else if (s.isSequentiallyBoundService()) {
					 if (expDate == null || compareDates < 0) {
						 valid = testBoundServiceAddition(service, s);
						 if (valid) {

							 // This will only happen once
							 // -------------------------------------------------------------------------------
							 // Calculate the effectiveDate and expiryDate for the first service in
							 // a sequentially-bound service pair. The second
							 // service starts on the same
							 // day the first expires.
							 // -------------------------------------------------------------------------------
							 ServiceAgreementInfo info = delegate.getService0(service.getCode(), false);
							 Date effectiveDate = (info.getEffectiveDate() != null) ? info.getEffectiveDate() : provider.getReferenceDataManager().getSystemDate();
							 Calendar expiryDate = Calendar.getInstance();
							 expiryDate.setTimeInMillis(effectiveDate.getTime());
							 expiryDate.add(Calendar.MONTH, service.getTermMonths());
							 // -------------------------------------------------------------------------------
							 // Setup the effectiveDate and expiryDates for the service pair.
							 // -------------------------------------------------------------------------------
							 // info.setEffectiveDate(effectiveDate); --TODO:
							 // this may cause issues for
							 // futureDatedActivations
							 info.setExpiryDate(expiryDate.getTime());
							 addService0(s, expiryDate.getTime(), null, true);
						 }
					 }
				 }
			 }
		 }
	 }

	 private void removeBoundServices(ContractService contractService) throws TelusAPIException {
		 Logger.debug(">>>> removeBoundServices("+contractService.getCode()+")");
		 // TODO: remove BUGFIX: this is a temporary workaround
		 contractService = decorate(contractService);

		 Service service = contractService.getService();
		 if(service.getService().hasBoundService() || service.getService().hasSequentiallyBoundService()) {
			 ServiceRelation[] relations = service.getRelations(getPricePlan());
			 for(int i=0; i < relations.length; i++) {
				 Service s = relations[i].getService().getService();
				 //if(s.isBoundService()) {  // AMDOCS will remove promotional services
				 if(delegate.containsService0(s.getCode(), false) && (s.isBoundService() || s.isSequentiallyBoundService())) {  // AMDOCS will remove promotional services
//					 ContractService boundContractService = removeService(s.getCode(), false);
					 Logger.debug(">>>> removing bound service [" + s.getCode() + "]");
					 removeService0(s.getCode(), true);
				 }
				 else {
					 Logger.debug(">>>> related service [" + s.getCode() + "]");
				 }
			 }
		 }
	 }

	 private List addRelatedServices(Service service, String relationType, List list, Date effectiveDate, Date expiryDate) throws TelusAPIException {
		 ServiceRelation[] relations = service.getRelations(getPricePlan(),  relationType);
		 for(int i=0; i < relations.length; i++) {
			 ContractService contractService = addService0(relations[i].getService().getService(), effectiveDate, expiryDate);
			 if(list != null) {
				 list.add(contractService);
			 }
		 }

		 return list;
	 }

//	 =========================================================================================================================

	 private void addIncludedServices(String equpmentType, String provinceCode) throws TelusAPIException {
		 Logger.debug("---->     addIncludedServices("+equpmentType+", "+provinceCode+")");
		 Service[] services = pricePlan.getIncludedServices();
		 services = (Service[])ReferenceDataManager.Helper.retainServices(services, equpmentType, provinceCode);

		 for (int i = 0; i < services.length; i++) {
			 if (!delegate.containsService0(services[i].getCode(), false)) {
				 addService0(services[i], null, null, false);
			 }
		 }
	 }

	 private void removeNonMatchingServices(Equipment equipment, MuleEquipment associatedMule, String provinceCode) throws TelusAPIException {
		 Logger.debug(">>>> removeNonMatchingServices(" + equipment.getEquipmentType() + ", " + provinceCode + ")");

		 if (equipment.isSIMCard() && associatedMule != null) {
			 ((SIMCardEquipment)equipment).setLastMule(associatedMule);
		 }

		 ContractService[] services = getServices();
		 ContractService[] matchingServices = (ContractService[])ReferenceDataManager.Helper.retainServices(services, equipment);
		 ContractService[] nonMatchingServices = (ContractService[])ReferenceDataManager.Helper.difference(services, matchingServices);

		 for (int i = 0; i < nonMatchingServices.length; i++) {
			 if ((delegate.containsService0(nonMatchingServices[i].getCode(), false)) &&(!nonMatchingServices[i].getService().isPromotion())
					 && (!nonMatchingServices[i].getService().isBoundService())){
				 removeService0(nonMatchingServices[i].getCode(), false );
				 removeBoundServices(nonMatchingServices[i]);
			 }
		 }
	 }

	 private void addDispatchOnlyConflicts() throws TelusAPIException {
		 Logger.debug("---->     addDispatchOnlyConflicts()");
		 if (isTelephonyEnabled()) {
			 Service[] services = ReferenceDataManager.Helper.retainTelephonyDisabledConflicts(pricePlan.getIncludedServices());
			 Feature[] features = ReferenceDataManager.Helper.retainTelephonyDisabledConflicts(pricePlan.getFeatures());

			 for (int i = 0; i < services.length; i++) {
				 if (!delegate.containsService0(services[i].getCode(), false)) {
					 try {
						 addService0(services[i], null, null, false);
					 } catch (TelusAPIException e) {
						 Logger.debug("            +\"" + services[i].getCode() + "\";     FAILED - " + e.getMessage());
					 }
				 }

			 }

			 for (int i = 0; i < features.length; i++) {
				 if (!delegate.containsFeature0(features[i].getCode(), null, null, false, false)) {
					 try {
						 addFeature0((RatedFeatureInfo)features[i]);
					 } catch (TelusAPIException e) {
						 Logger.debug("            +\"" + features[i].getCode() + "\"  (feature) FAILED - " + e.getMessage());
					 }
				 }
			 }
		 }
	 }

	 private void removeDispatchOnlyConflicts() throws TelusAPIException {
		 Logger.debug("---->     removeDispatchOnlyConflicts()");
		 if (!isTelephonyEnabled()) {
			 ContractService[] services = ReferenceDataManager.Helper.retainTelephonyDisabledConflicts(getServices());
			 ContractFeature[] features = ReferenceDataManager.Helper.retainTelephonyDisabledConflicts(getFeatures());

			 for (int i = 0; i < services.length; i++) {
				 removeService0(services[i].getCode(), false);
			 }

			 for (int i = 0; i < features.length; i++) {
				 removeFeature(features[i].getCode());
			 }
		 }
	 }

	 private void addConflictingIncludedFeatures(Service optionalService) throws TelusAPIException {
		 Logger.debug("---->     addConflictingIncludedFeatures("+optionalService.getCode()+")");
		 if (!pricePlan.containsIncludedService(optionalService.getCode())) {
			 RatedFeature[] features = optionalService.getFeatures();
			 for (int i = 0; i < features.length; i++) {
				 RatedFeatureInfo f = (RatedFeatureInfo)features[i];
				 if (!f.isDuplFeatureAllowed()) {

					 //----------------------------------------------------------------------------
					 // if the feature's in the priceplan, but not this contract; add it
					 //----------------------------------------------------------------------------
					 if (pricePlan.containsFeature(f.getCode()) && !delegate.containsFeature0(f.getCode(), null, null, false, false)) {
						 try {
							 addFeature0(f);
						 } catch (TelusAPIException e) {
							 Logger.debug("            +\"" + f.getCode() + "\"  (feature) FAILED - " + e.getMessage());
						 }

					 }

					 ServiceInfo[] services = delegate.getPricePlan0().getServicesByFeature(f.getCode());
					 for (int j = 0; j < services.length; j++) {
						 //----------------------------------------------------------------------------
						 // if the feature's in an included service, but not this contract; add it
						 //----------------------------------------------------------------------------
						 if (pricePlan.containsIncludedService(services[j].getCode()) && !delegate.containsService0(services[j].getCode(), false)) {
							 try {
								 addService0(services[j], null, null, false);
							 } catch (TelusAPIException e) {
								 Logger.debug("            +\"" + services[j].getCode() + "\";     FAILED - " + e.getMessage());
							 }
						 }
					 }
				 }
			 }
		 }
	 }

	 private static final String CALL_DETAIL_FEATURE = "CD";

	 private void removeCallDetailFeatureOnPostpaidPCS() throws TelusAPIException {
		 Logger.debug(">>>> removeCallDetailFeatureOnPostpaidPCS() ...");

		 if (!delegate.containsFeature0(CALL_DETAIL_FEATURE, null, null, false, false)) {
			 Logger.debug(">>>> CallDetailFeature not found on PricePlan, not removed ...");
			 return;
		 }

		 Date currentTime = new Date();
		 Date expiryTime = AppConfiguration.getCallDetailFeatureExpiryDate();

		 boolean remove = false;

		 if (currentTime.after(expiryTime)) {
			 remove = true;
		 }

		 if (!remove) {
			 return;
		 }

		 char accountType = subscriber.getAccountSummary().getAccountType();
		 boolean corporate = 'C' == accountType ? true : false;
		 boolean pcs = subscriber.isPCS();
		 boolean postpaid = subscriber.getAccountSummary().isPostpaid();

		 if (!corporate && pcs && postpaid) {

			 //---------------------------------------------
			 // Remove price plan feature: detail billing on all postpaid PCS excluding Corporate.
			 // "CD" is a price plan feature, not a feature of a service (SOC).
			 //---------------------------------------------
			 removeFeature(CALL_DETAIL_FEATURE);
			 Logger.debug(">>>> callDetailFeature [" + CALL_DETAIL_FEATURE + "] expired (removed from Contract)");
		 }
	 }

	 //private static final String[] FEATURE_CODE_911 = new String[]{"911", "NS911", "M911"};
	 private boolean cascadeShareableServiceChanges = false;


	 // The following static variables are used for the addOutboundCallerIdDisplay() and removeOutboundCallerIdDisplay() methods.
	 public static final String OUTBOUND_CALLER_ID_PCS_FEATURE = "CNRD";
	 public static final String OUTBOUND_CALLER_ID_KOODO_FEATURE = "PTOCDT";
	 
	 // Add Outbound Caller ID Display service or feature if the removed service is Caller ID Restriction.
	 private void addOutboundCallerIdDisplay(Service service) throws TelusAPIException {
		 
		 Logger.debug("---->     addOutboundCallerIdDisplay(" + service.getCode() + ")");
		 
		 // Check if the removed service is TELUS Caller ID Restriction (SCNIR) and add the 
		 // appropriate feature or service if required.
		 if (service.getCode().trim().equals(ServiceSummary.BLOCK_OUTGOING_CALLER_ID_PCS.trim())) {
			 addFeatureToContract(OUTBOUND_CALLER_ID_PCS_FEATURE);	 
		 }
		 
		 // Check if the removed service is Koodo Caller ID Restriction (3SPXCIDR) and add the 
		 // appropriate feature or service if required.
		 if (service.getCode().trim().equals(ServiceSummary.BLOCK_OUTGOING_CALLER_ID_KOODO.trim())) {
			 addFeatureToContract(OUTBOUND_CALLER_ID_KOODO_FEATURE);
		 }
	 }
	 
	 // Remove Outbound Caller ID Display service or feature if the added service is Caller ID Restriction.
	 private void removeOutboundCallerIdDisplay(Service service) throws TelusAPIException {

		 Logger.debug("---->     removeOutboundCallerIdDisplay(" + service.getCode() + ")");

		 // Check if the added service is TELUS Caller ID Restriction (SCNIR) and remove the 
		 // appropriate feature or service if required..
		 if (service.getCode().trim().equals(ServiceSummary.BLOCK_OUTGOING_CALLER_ID_PCS.trim())) {
			 removeFeatureFromContract(OUTBOUND_CALLER_ID_PCS_FEATURE);
		 }		 
		 
		 // Check if the added service is Koodo Caller ID Restriction (3SPXCIDR) and remove the 
		 // appropriate feature or service if required..
		 if (service.getCode().trim().equals(ServiceSummary.BLOCK_OUTGOING_CALLER_ID_KOODO.trim())) {
			 removeFeatureFromContract(OUTBOUND_CALLER_ID_KOODO_FEATURE);
		 }
	 }
	 
	 private void addFeatureToContract(String feature) throws TelusAPIException {

		 Logger.debug("---->     addFeatureToContract(" + feature + ")");

		 // Check the SubscriberContractInfo to see if the feature already exists.
		 if (delegate.containsFeature0(feature, null, null, true, false)) {
			 Logger.debug(">>>> Feature already found on SubscriberContractInfo, not added...");
			 return;
		 }

		 // If the feature is part of the price plan, add it.
		 if (pricePlan.containsFeature(feature)) {
			 RatedFeature ratedFeature = pricePlan.getFeature(feature);
			 try {
				 addFeature(ratedFeature);
			 } catch (TelusAPIException e) {
				 Logger.debug(e);
			 }

		 // Otherwise, if the feature is part of an included service, add the included service.
		 } else {
			 try {
				 Service[] services = pricePlan.getIncludedServices();
				 for (int i = 0; i < services.length; i++) {
					 if (services[i].containsFeature(feature)) {
						 addService0(services[i], null, null, false);
					 }
				 }
			 } catch (TelusAPIException e) {
				 Logger.debug(e);
			 }
		 }
	 }
	 
	 private void removeFeatureFromContract(String feature) throws TelusAPIException {

		 Logger.debug("---->     removeFeatureFromContract(" + feature + ")");

		 // Get all services which contain the feature and remove them.
		 ServiceAgreementInfo[] services = delegate.getServicesByFeature0(feature, null, null, false);
		 for (int i = 0; i < services.length; i++) {
			 removeService0(services[i].getCode(), false);
		 }
		 
		 // If the price plan included features contains the feature, remove it.
		 if (delegate.containsFeature0(feature, null, null, false, false)) {
			 removeFeature(feature);
		 }
	 }

	 // The following static variable is used for the addCombinedVoiceMailConflicts() and removeCombinedVoiceMailConflicts() methods.
	 public static final String COMBINED_VOICE_MAIL_PREFIX = "CVM";

	 // Add conflicting services or features if the removed service is Combined Voice Mail.
	 private void addCombinedVoiceMailConflicts(Service service) throws TelusAPIException {
		 if (isCombinedVoiceMail(service)) {
			 // Note: check for voice mail AFTER all other conflicts have been added, since it may already be added through a
			 // conflicting feature or service.
			 addCombinedVoiceMailConflicts0(service);
			 addVoiceMail();
		 }
	 }

	 // Remove conflicting services or features if the added service is Combined Voice Mail.
	 private void removeCombinedVoiceMailConflicts(TMContractService service) throws TelusAPIException {
		 if (isCombinedVoiceMail(service.getService())) {
			 // Note: check for voice mail AFTER all other conflicts have been removed, since it may already be removed through a
			 // conflicting feature or service.
			 removeCombinedVoiceMailConflicts0(service);
			 removeVoiceMail();
		 }
	 }

	 // Add Voice Mail service or feature.
	 private void addVoiceMail() throws TelusAPIException {

		 Logger.debug("---->     addVoiceMail()");

		 // Check the subscriber contract to see if the Voice Mail feature (switch code = "VM") already exists.
		 ContractFeature[] contractFeatures = delegate.getFeatures();
		 ContractService[] contractServices = delegate.getServices();

		 // Check the contract features...
		 for (int j = 0; j < contractFeatures.length; j++) {
			 RatedFeature ratedFeature = contractFeatures[j].getFeature();
			 if (ratedFeature.getSwitchCode().trim().equalsIgnoreCase(Feature.SWITCH_CODE_VOICE_MAIL)) {
				 Logger.debug(">>>> Voice Mail already found on SubscriberContractInfo, not added.");
				 return;
			 }
		 }

		 // Check the contract services...
		 for (int k = 0; k < contractServices.length; k++) {
			 ContractFeature[] contractServiceFeatures = contractServices[k].getFeatures();
			 for (int l = 0; l < contractServiceFeatures.length; l++) {
				 RatedFeature ratedServiceFeature = contractServiceFeatures[l].getFeature();
				 if (ratedServiceFeature.getSwitchCode().trim().equalsIgnoreCase(Feature.SWITCH_CODE_VOICE_MAIL)) {
					 Logger.debug(">>>> Voice Mail already found on SubscriberContractInfo, not added.");
					 return;
				 }
			 }
		 }

		 // If the priceplan contains Voice Mail, add the feature or service here.
		 RatedFeature[] pricePlanFeatures = pricePlan.getFeatures();
		 Service[] includedServices = pricePlan.getIncludedServices();

		 // Check priceplan features...
		 for (int x = 0; x < pricePlanFeatures.length; x++) {
			 if (pricePlanFeatures[x].getSwitchCode().trim().equalsIgnoreCase(Feature.SWITCH_CODE_VOICE_MAIL)) {
				 try {
					 addFeature(pricePlanFeatures[x]);
					 Logger.debug(">>>> Voice Mail added to SubscriberContractInfo.");
				 } catch (TelusAPIException e) {
					 Logger.debug("            +\"" + pricePlanFeatures[x].getCode() + "\"  (feature) FAILED - " + e.getMessage());
				 }
			 }
		 }

		 // Check priceplan included services...
		 for (int y = 0; y < includedServices.length; y++) {
			 RatedFeature includedServiceFeatures[] = includedServices[y].getFeatures();
			 for (int z = 0; z < includedServiceFeatures.length; z++) {
				 if (includedServiceFeatures[z].getSwitchCode().trim().equalsIgnoreCase(Feature.SWITCH_CODE_VOICE_MAIL)) {
					 try {
						 addService0(includedServices[y], null, null, false);
						 Logger.debug(">>>> Voice Mail added to SubscriberContractInfo.");
						 break;
					 } catch (TelusAPIException e) {
						 Logger.debug("            +\"" + Feature.SWITCH_CODE_VOICE_MAIL + "\"  (service) FAILED - " + e.getMessage());
					 }
				 }
			 }
		 }
	 }

	 // Remove Voice Mail service or feature.
	 private void removeVoiceMail() throws TelusAPIException {

		 Logger.debug("---->     removeVoiceMail()");

		 // Check the subscriber contract to see if the Voice Mail feature (switch code = "VM") exists.
		 ContractFeature[] contractFeatures = delegate.getFeatures();
		 ContractService[] contractServices = delegate.getServices();

		 // If the contract contains Voice Mail as a feature, remove it.
		 for (int j = 0; j < contractFeatures.length; j++) {
			 RatedFeature ratedFeature = contractFeatures[j].getFeature();
			 if (ratedFeature.getSwitchCode().trim().equalsIgnoreCase(Feature.SWITCH_CODE_VOICE_MAIL)) {
				 removeFeature(contractFeatures[j].getCode());
				 Logger.debug(">>>> Voice Mail removed from SubscriberContractInfo.");
			 }
		 }

		 // Get all services which contain Voice Mail (switch code = "VM") and remove them.
		 for (int k = 0; k < contractServices.length; k++) {
			 ContractFeature[] contractServiceFeatures = contractServices[k].getFeatures();
			 for (int l = 0; l < contractServiceFeatures.length; l++) {
				 RatedFeature ratedServiceFeature = contractServiceFeatures[l].getFeature();
				 if (ratedServiceFeature.getSwitchCode().trim().equalsIgnoreCase(Feature.SWITCH_CODE_VOICE_MAIL)) {
					 removeService0(contractServices[k].getCode(), false);
					 Logger.debug(">>>> Voice Mail removed from SubscriberContractInfo.");
					 break;
				 }
			 }
		 }
	 }

	 // Add conflicting services or features if the removed service is Combined Voice Mail.
	 private void addCombinedVoiceMailConflicts0(Service service) throws TelusAPIException {

		 Logger.debug("---->     addCombinedVoiceMailConflicts0(" + service.getCode() + ")");

		 // Get contract included services.
		 ContractService[] contractServices = delegate.getIncludedServices();

		 // Get priceplan included services.
		 Service[] includedServices = pricePlan.getIncludedServices();

		 // Get features from the Combined Voice Mail service.
		 RatedFeature serviceFeatures[] = service.getFeatures();

		 //---------------------------------------
		 // Conflicting features check.
		 //---------------------------------------
		 serviceFeatureLoop:
			 for (int i = 0; i < serviceFeatures.length; i++) {

				 // Check the subscriber contract features to see if the feature already exists.
				 if (delegate.containsFeature0(serviceFeatures[i].getCode(), null, null, false, false)) {
					 Logger.debug(">>>> Feature [" + serviceFeatures[i].getCode() + "] already found on SubscriberContractInfo, not added.");
					 continue serviceFeatureLoop;
				 }

				 // Check the subscriber contract included services to see if the feature already exists.
				 for (int j = 0; j < contractServices.length; j++) {
					 if (contractServices[j].getService().containsFeature(serviceFeatures[i].getCode())) {
						 Logger.debug(">>>> Service feature [" + serviceFeatures[i].getCode() + "] already found on SubscriberContractInfo, not added.");
						 continue serviceFeatureLoop;
					 }
				 }

				 // Check all priceplan features.  If there is a conflict with any Combined Voice Mail feature,
				 // add the conflicting feature.
				 if (pricePlan.containsFeature(serviceFeatures[i].getCode())) {
					 try {
						 addFeature(pricePlan.getFeature(serviceFeatures[i].getCode()));
						 Logger.debug(">>>> Added conflicting feature from priceplan: [" + serviceFeatures[i].getCode() + "]");
						 continue serviceFeatureLoop;
					 } catch (TelusAPIException e) {
						 Logger.debug("            +\"" + serviceFeatures[i].getCode() + "\"  (feature) FAILED - " + e.getMessage());
					 }
				 }

				 // Loop through all priceplan included services.
				 for (int k = 0; k < includedServices.length; k++) {
					 // Check all priceplan included service features.  If there is a conflict with any Combined Voice Mail feature,
					 // add the conflicting service.
					 if (includedServices[k].containsFeature(serviceFeatures[i].getCode())) {
						 try {
							 addService0(includedServices[k], null, null, false);
							 Logger.debug(">>>> Added conflicting included service from priceplan: [" + includedServices[k].getCode() + "]");
							 continue serviceFeatureLoop;
						 } catch (TelusAPIException e) {
							 Logger.debug("            +\"" + includedServices[k].getCode() + "\"  (service) FAILED - " + e.getMessage());
						 }
					 }
				 }
			 }
	 }

	 // Remove conflicting services or features if the added service is Combined Voice Mail.
	 private void removeCombinedVoiceMailConflicts0(TMContractService service) throws TelusAPIException {

		 Logger.debug("---->     removeCombinedVoiceMailConflicts0(" + service.getCode() + ")");

		 // Get contract services.
		 ContractService[] contractServices = delegate.getIncludedServices();

		 // Get features from the Combined Voice Mail service.
		 RatedFeature serviceFeatures[] = service.getService().getFeatures();

		 for (int i = 0; i < serviceFeatures.length; i++) {

			 // Check the subscriber contract features to see if the feature already exists.
			 // If there is a conflict with any Combined Voice Mail feature, remove the conflicting feature.
			 if (delegate.containsFeature0(serviceFeatures[i].getCode(), null, null, false, false)) {
				 if (service.getEffectiveDate() != null && getLogicalDate().before(service.getEffectiveDate())) {
					 removeFeature(serviceFeatures[i].getCode()); // need to fix when we could expire included feature
					 Logger.debug(">>>> Removed conflicting feature from contract: ["
							 + serviceFeatures[i].getCode()
							 + "] on ["
							 + service.getEffectiveDate() + "]");   
				 }else{
					 removeFeature(serviceFeatures[i].getCode());
					 Logger.debug(">>>> Removed conflicting feature from contract: [" + serviceFeatures[i].getCode() + "]");
				 }  

			 }

			 // Loop through all contract included services.
			 for (int j = 0; j < contractServices.length; j++) {
				 //ContractFeature[] contractServiceFeatures = contractServices[j].getFeatures();

				 // Check the subscriber contract included services to see if the feature already exists.
				 // If there is a conflict with any Combined Voice Mail feature, remove the conflicting service.
				 if (contractServices[j].getService().containsFeature(
						 serviceFeatures[i].getCode())) {
					 if (service.getEffectiveDate() != null
							 && getLogicalDate().before(
									 service.getEffectiveDate())) {
						 contractServices[j].setExpiryDate(service
								 .getEffectiveDate());
						 Logger
						 .debug(">>>> Removed conflicting service from contract: ["
						                                                          + contractServices[j].getCode()
						                                                          + "] on "
						                                                          + service.getEffectiveDate()
						                                                          + "]");

					 } else {
						 removeService0(contractServices[j].getCode(), false);
						 Logger.debug(">>>> Removed conflicting service from contract: ["
								 + contractServices[j].getCode() + "]");
					 } 
					 break;
				 }
			 }
		 }
	 }

	 private boolean isCombinedVoiceMail(Service service) throws TelusAPIException {
		 // Check if the service contains Combined Voice Mail (switch code starts with "CVM").
		 RatedFeature serviceFeatures[] = service.getFeatures();
		 for (int i = 0; i < serviceFeatures.length; i++) {
			 if (serviceFeatures[i].getSwitchCode().startsWith(COMBINED_VOICE_MAIL_PREFIX)) {
				 return true;
			 }
		 }
		 return false;
	 }

//	 =========================================================================================================================
	 private TMContractService addService0(Service service, Date effectiveDate, Date expiryDate) throws InvalidServiceChangeException, TelusAPIException {
	
		 return addService0(service, effectiveDate, expiryDate, false, false);
	 }

	 private TMContractService addService0(Service service, Date effectiveDate, Date expiryDate, boolean allowBoundAndPromotional, boolean preserveDigitalServices) throws InvalidServiceChangeException, TelusAPIException {
		 
		 
		 ServiceAgreementInfo info = addService0(service, effectiveDate, expiryDate, allowBoundAndPromotional);
		
		 TMContractService contractService = decorate(info);
		 if(effectiveDate == null){
		     effectiveDate = contractService.getEffectiveDate(); 
		 }
		 
		

		 ServiceFeatureInfo[] features = info.getFeatures0(true);
		 for (int j = 0; j < features.length; j++) {
			 ServiceFeatureInfo f = features[j];
			 f.setFeature(info.getService0().getFeature0(f.getFeatureCode()));
		 }

		 addBoundServices(service, effectiveDate, expiryDate);

		 if (!preserveDigitalServices) {
			 String applicationCode = provider.getApplication();
			 ApplicationSummary applicationSummary = provider.getReferenceDataManager().getApplicationSummary(applicationCode);
			 if ((applicationSummary != null && !applicationSummary.isBatch()) || applicationSummary == null) {
				 removeDispatchOnlyConflicts();
				 // removed - it should only be done for Activation, Price Plan change and Renewal - R.A. 2007-07-03
				 //removeNonMatchingServices(getValidationEquipment(), getAssociatedMule(), subscriber.getProvince());
			 }
			 //remove911FeaturesInEdmonton();
			 removeOutboundCallerIdDisplay(service);
		 }

		 removeCombinedVoiceMailConflicts(contractService);

		 
		 checkInvoiceFormatChange(service, info.getTransaction() == BaseAgreementInfo.ADD);
		
		 // TODO: check if contractService still exists after removing the above serices.
		 return contractService;
	 }
	 
	 /**
	  * Provides provisioning lag time in minutes which is added to a service duration to
	  * accomodate service provisioning delay related to network and other bottlenecks.
	  * The value is expected to be configurable through LDAP or other configuration source.
	  * @return lag time (minutes)
	  */
	 private int getProvisioningLagTime() {
		 // TODO: get the value from configuration source
		 return 5;
	 }
	 

	 private TMContractService removeService0(boolean allowBoundAndPromotional, String serviceCode) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException {
		 Logger.debug("Before PAD: serverCode =[" + serviceCode + "]");
		 serviceCode = Info.padService(serviceCode);
		 Logger.debug("After PAD: serverCode =[" + serviceCode + "]");
		 ServiceAgreementInfo info = removeService0(serviceCode, allowBoundAndPromotional);

		 TMContractService contractService = decorate(info);

		 removeBoundServices(contractService);
		 addConflictingIncludedFeatures(contractService.getService());
		 String socCode = serviceCode.trim();
		 if (socCode.equals(ServiceSummary.BLOCK_INCOMING_CALLS_IDEN)
				 ||socCode.equals(ServiceSummary.BLOCK_OUTGOING_CALLS_IDEN) ) {
			 addDispatchOnlyConflicts();
		 }
		 removeDispatchOnlyConflicts();
		 // removed - it should only be done for Activation, Price Plan change and Renewal - R.A. 2007-07-03
		 //removeNonMatchingServices(getValidationEquipment(), getAssociatedMule(), subscriber.getProvince());

		 //remove911FeaturesInEdmonton();
		 addOutboundCallerIdDisplay(contractService.getService());
		 addCombinedVoiceMailConflicts(contractService.getService());

		 checkInvoiceFormatChange(contractService.getService(), false);

		 if (contractService.getService().hasPromotion()) {
			 Logger.debug("checking for promo service to remove");
			 ServiceRelation[] promos = contractService.getService().getRelations(subscriber.getContract().getPricePlan(),ServiceRelation.TYPE_PROMOTION);
			 Logger.debug("isPromos.count="+promos.length);
			 if (promos!=null) {
				 for (int i=0;i<promos.length;i++) {
					 if (isServiceInContractService(promos[i],subscriber.getContract().getServices())) {
						 Logger.debug("attempting to remove "+promos[i].getService().getCode());
						 removeService0(true,promos[i].getService().getCode());
					 }
				 }
			 }
		 }

		 return contractService;
	 }

	 private boolean isServiceInContractService(ServiceRelation sr, ContractService[] cs) {
		 if (sr==null||cs==null)
			 return false;

		 for (int i=0;i<cs.length;i++) {
			 if (sr.getService().getCode().equals(cs[i].getCode()))
				 return true;
		 }
		 return false;
	 }

	 /* remove the following method as no one is calling it
	 private TMContractService removeService0(String serviceCode) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException {
		 return removeService0(false,serviceCode);
	 }
	  */

//	 =========================================================================================================================

	 public ContractService addService(Service service) throws InvalidServiceChangeException, TelusAPIException {
		 /*
    provider.debug("----> addService("+service.getCode()+")");
    return addService0(service, null, null);
		  */
		 return addService(service, false);
	 }

	 private ContractService addService(Service service, boolean preserveDigitalServices) throws InvalidServiceChangeException, TelusAPIException {
		 if(service.getCode()!=null)
		   Logger.debug("----> addService("+service.getCode()+")");
		 Logger.debug("----> preserveDigitalServices = " + preserveDigitalServices);
		 return addService0(service, null, null, false, preserveDigitalServices);
	 }

	 public ContractService addService(String serviceCode) throws UnknownObjectException,  InvalidServiceChangeException, TelusAPIException {
		 return addService(serviceCode, false);
	 }

	 public ContractService addService(String serviceCode, boolean preserveDigitalServices) throws UnknownObjectException,  InvalidServiceChangeException, TelusAPIException {
		 serviceCode = Info.padService(serviceCode);
		 Service service;
		 try {
			 service = pricePlan.getService(serviceCode);
		 } catch(UnknownObjectException e) {
			 service = provider.getReferenceDataManager().getRegularService(serviceCode);
		 }
		 
		 if (service == null && subscriber.getAccount().isPrepaidConsumer()) {
			 service = provider.getReferenceDataManager().getWPSService(serviceCode);
		 }
		 if(service==null)
			 throw new TelusAPIException("Invalid service code ["+serviceCode+"] to add to subscriber contract");
		 return addService(service, preserveDigitalServices);
	 }

	 // This Method was added to support The IVR Future Dated
	 public ContractService addService(Service service, Date effectiveDate, Date expiryDate) throws InvalidServiceChangeException, TelusAPIException {
		 if(service.getCode()!=null)
			 Logger.debug("----> addService("+service.getCode()+")");
		 if(effectiveDate !=null)
			 Logger.debug("----> effectiveDate = " + effectiveDate.toString());
		 if(expiryDate !=null)
			 Logger.debug("----> expiryDate = " + expiryDate.toString());
		 return addService0(service, effectiveDate, expiryDate, false, false);
	 }


	 //This only implement the public interface
	 //This is change is for defect PROD00154523
	 public void removeService(String serviceCode) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException {
		 //The original implementation is moved to the method removeService( allBound, serviceCode) , 
		 // so here we only need to delegate to it.
		 removeService( false, serviceCode );
	 }
	 
	 //Refactoring: add one more parameter: allBound, and pass down this information - to allow better reuse of this method's logic
	 //This is change is for defect PROD00154523
	 private void removeService(boolean allowBound, String serviceCode) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException {
		 Logger.debug("Before PAD: serverCode =[" + serviceCode + "]");
		 serviceCode = Info.padService(serviceCode);
		 Logger.debug("After PAD: serverCode =[" + serviceCode + "]");
		 Logger.debug("----> removeService("+serviceCode+")");
		 
		 if (priceplanChange && oldVistoPreserved) {
			 //do not allow removal of Visto service if legacy Visto is already preserved on the contract
			 //this allows execution order of calling removeService after a Visto bundle has been added
			 Service service;
			 try {
				 service = pricePlan.getService(serviceCode);
			 } catch (UnknownObjectException e) {
				 service = provider.getReferenceDataManager().getRegularService(serviceCode);
			 }
			 if (service.isVisto()) {
				 Logger.debug("Visto SOC "+serviceCode+" is preserved-->not removed in removeService.");
				 return; //do nothing if it's trying to remove a Visto SOC
			 }
		 }
		 removeService0(allowBound, serviceCode);
	 }

	 public void removeFeature(String featureCode) throws UnknownObjectException, TelusAPIException {
		 Logger.debug("            -\"" + featureCode + "\"  (feature)");
		 delegate.removeFeature(featureCode);
	 }

	 public ContractService[] addCard(Card card) throws InvalidCardChangeException, TelusAPIException {
		 return addCard(card, false);
	 }

	 public synchronized ContractService[] addCard(Card card, boolean autoRenew) throws InvalidCardChangeException, TelusAPIException {

		 // TODO: use term & termUnits

		 Date today      = provider.getReferenceDataManager().getSystemDate();

		 TMCard c = (TMCard)card;
		 c.getDelegate().setAutoRenew(autoRenew);

		 Service[] cardServices     = testAddition(card, autoRenew);
		 List      contractServices = new ArrayList(cardServices.length * 2);

		 for(int i=0; i < cardServices.length; i++) {
			 Service s = cardServices[i];
			 TMContractService  cs = addService0(s, null, null);
			 cs.getDelegate().setFeatureCard(true);

			 // changeOverDate is when the zero-rated service expires and
			 // the chargeable one can begin.
			 Calendar changeOverCal = Calendar.getInstance();
			 if(s.isWPS()) {
				 changeOverCal.setTime(cs.getExpiryDate());
			 } else {
				 changeOverCal.setTime(today);
				 changeOverCal.add(Calendar.MONTH, s.getTermMonths());
			 }

			 Date changeOverDate = changeOverCal.getTime();

			 if(!s.isWPS()) {
				 cs.setEffectiveDate(today);
			 }
			 cs.setExpiryDate(changeOverDate);

			 contractServices.add(cs);
			 if(autoRenew) {
				 addRelatedServices(cardServices[i], ServiceRelation.TYPE_FEATURE_CARD, contractServices, changeOverDate, null);
			 }
		 }

		 cards.add(card);

		 return (ContractService[])contractServices.toArray(new ContractService[contractServices.size()]);
	 }

//	 =========================================================================================================================

	 private void testGrandFatheredPricePlan() throws InvalidServiceChangeException, TelusAPIException {
//		 if (provider.getReferenceDataManager0().isGrandFathered(getPricePlan())) {
//		 throw new InvalidServiceChangeException(InvalidServiceChangeException.GRAND_FATHERED_PRICEPLAN, getPricePlan().getCode());
//		 }
	 }

	 public Service testAddition(Service service) throws InvalidServiceChangeException, TelusAPIException {
//		 return testAddition(service, null, null);
		 return testAddition0(service, null, null, false, true);
	 }

	 public Service testAddition(Service service,Date effectiveDate) throws InvalidServiceChangeException, TelusAPIException {
		 return testAddition0(service, effectiveDate, null, false, true);
	 }


	 public Service[] testAddition(Service[] service) throws InvalidServiceChangeException, TelusAPIException {
		 for(int i=0; i < service.length; i++) {
			 testAddition(service[i]);
		 }
		 return service;
	 }

	 public ContractService testRemoval(ContractService contractService) throws InvalidServiceChangeException, TelusAPIException {
		 return testRemoval0(contractService, false);
	 }

	 public Service[] testAddition(Card card, boolean autoRenew) throws InvalidCardChangeException, TelusAPIException {
//		 provider.debug("  ====> testAddition(Card card, boolean autoRenew): enter");
		 try {
			 if(card.getStatus() != Card.STATUS_LIVE) {
				 throw new InvalidCardChangeException(InvalidCardChangeException.INVALID_STATUS);
			 }

			 try {
				 Service[] services = card.getServices(subscriber);
				 testAddition(services);

				 Date today      = provider.getReferenceDataManager().getSystemDate();

				 for(int i=0; i < services.length; i++) {
					 Service s = services[i];

					 if(autoRenew) {
						 // changeOverDate is when the zero-rated service expires and
						 // the chargeable one can begin.
						 Calendar changeOverCal = Calendar.getInstance();
						 changeOverCal.setTime(today);
						 changeOverCal.add(Calendar.MONTH, s.getTermMonths());

						 ServiceRelation[] relatedServices = s.getRelations(getPricePlan(), ServiceRelation.TYPE_FEATURE_CARD);

						 for (int j=0; j<relatedServices.length; j++) {
							 testAddition0(relatedServices[j].getService().getService(), changeOverCal.getTime(), null, false, true);
						 }
					 }
				 }



				 return services;
			 } catch(InvalidServiceException e) {
				 throw new InvalidCardChangeException(e.getReason(), e);
			 }
		 }finally {
//			 provider.debug("  ====> testAddition(Card card, boolean autoRenew): leave");
		 }

	 }



	 public ContractFeature addFeature(RatedFeature feature) throws UnknownObjectException, TelusAPIException {
		 return decorate( addFeature0((RatedFeatureInfo)feature) );
	 }

	 private ServiceFeatureInfo addFeature0(RatedFeatureInfo feature) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException {
		 if (!feature.isDuplFeatureAllowed() && delegate.containsFeature0(feature.getCode(), null, null, true, false))
			 throw new InvalidServiceChangeException(InvalidServiceChangeException.DUPLICATE_FEATURE, "", null, null);

		 Logger.debug("            +\"" + feature.getCode() + "\"  (feature)");
		 return delegate.addFeature(feature);
	 }

	 public boolean containsPricePlanFeature(String featureCode) {
		 return delegate.containsFeature0(featureCode, null, null, false, false);
	 }

	 /**
	  * Restricted use. Currently only called by Subscriber.changeEquipment()
	  * @param subscriber TMSubscriber
	  */
	 public void setSubscriber(TMSubscriber subscriber) {
		 this.subscriber = subscriber;
	 }

	 public String[] getMultiRingPhoneNumbers() {
		 return delegate.getMultiRingPhoneNumbers();
	 }

	 /**
	  * Returns multi-ring phone numbers.
	  * @throws TelusAPIException
	  * @return String[]
	  *
	  * @see TMSubscriber
	  */
	 String[] retrieveMultiRingPhoneNumbers() throws TelusAPIException {
		 String[] numbers = null;
		 if (subscriber.isPCS() && containsFeature(Feature.CATEGORY_CODE_MULTI_RING)) {
			 try {
				 
			 	 numbers = provider.getSubscriberLifecycleHelper().
			 			retrieveMultiRingPhoneNumbers(subscriber.getSubscriberId());
				 
				 delegate.setMultiRingPhoneNumbers(numbers);
			 }catch (Throwable t) {
					provider.getExceptionHandler().handleException(t);
				}
		 }

		 return numbers;
	 }

	 private boolean containsFeature(String featureCategoryCode) {

		 ContractFeature[] features = delegate.getFeatures(true);
		 for (int i=0; i<features.length; i++) {
			 if (features[i].getFeature() != null && featureCategoryCode.equalsIgnoreCase(features[i].getFeature().getCategoryCode())) {
				 return true;
			 }
		 }

		 return false;
	 }

	 /**
	  * Set multi-ring phone numbers
	  * @param phoneNumbers String[]
	  */
	 public void setMultiRingPhoneNumbers(String[] phoneNumbers) {
		 delegate.setMultiRingPhoneNumbers(phoneNumbers);
	 }

	 // MultiRing
	 private MultiRingInfo[] getMultiRingInfos(String[] originalSet, String[] modifiedSet, ServiceAgreementInfo service) {
		 byte socMode = service.getTransaction();
		 String socCode = service.getCode();
		 if (BaseAgreementInfo.ADD == socMode) {
			 return getMultiRingInfoArray(modifiedSet, MultiRingInfo.ADD, socCode);
		 }
		 else if (BaseAgreementInfo.DELETE == socMode) {
			 return getMultiRingInfoArray(originalSet, MultiRingInfo.DELETE, socCode);
		 }
		 else if (BaseAgreementInfo.UPDATE == socMode || BaseAgreementInfo.NO_CHG == socMode) {
			 MultiRingInfo[] deleted = getMultiRingInfoArray(originalSet, MultiRingInfo.DELETE, socCode);
			 MultiRingInfo[] added = getMultiRingInfoArray(modifiedSet, MultiRingInfo.ADD, socCode);
			 MultiRingInfo[] sum = new MultiRingInfo[deleted.length + added.length];

			 for (int i=0; i<deleted.length; i++) {
				 sum[i] = deleted[i];
			 }

			 for (int i=0, j=deleted.length; i<added.length; i++, j++) {
				 sum[j] = added[i];
			 }

			 Logger.debug("sum.length = " + sum.length);

			 return sum;
		 }

		 return null;
	 }


	 private MultiRingInfo[] getMultiRingInfoArray(String[] phones, byte mode, String socCode) {
		 MultiRingInfo info = null;
		 List list = new ArrayList();
		 if (phones != null) {
			 for (int i = 0; i < phones.length; i++) {

				 info = new MultiRingInfo();

				 info.setPhone(phones[i]);
				 info.setMode(mode);
				 info.setSocCode(socCode);

				 list.add(info);
				 Logger.debug("phone number added: " + phones[i]);
			 }
		 }

		 return (MultiRingInfo[])list.toArray(new MultiRingInfo[list.size()]);
	 }

	 private void printMultiRingInfo(MultiRingInfo[] infos) {
		 if (infos == null)
			 return;

		 for (int i =0; i<infos.length; i++) {
			 Logger.debug("Multi-ring: " + infos[i].getPhone() + " | " + infos[i].getMode() + " | " + infos[i].getSocCode());
		 }
	 }

	 private boolean containsMultiRingFeature(ServiceAgreementInfo service) {
		 if (subscriber.isPCS() && service != null) {
			 ContractFeature[] features = service.getFeatures0(true);
			 for (int i = 0; i < features.length; i++) {
				 if (Feature.CATEGORY_CODE_MULTI_RING.equalsIgnoreCase(features[i].
						 getFeature().getCategoryCode()))
					 return true;
			 }
		 }
		 return false;
	 }

	 public ContractService[] get911Services() {
		 
		 List _911Services = new ArrayList();
		 ContractService[] allServices = this.getServices();
		 for (int i = 0; i < allServices.length; i++) {
			 try {
				 if (allServices[i].getService().is911()) {
					 _911Services.add(allServices[i]);
				 }
			 } catch (TelusAPIException tae) {
				 Logger.debug(tae);
			 }
		 }

		 return (ContractService[])_911Services.toArray(new ContractService[_911Services.size()]);
	 }

	 public double get911Charges() {

		 double charges = 0.0;
		 ContractService[] services = get911Services();
		 Service s;

		 for (int i = 0; i < services.length; i++) {
			 try {
				 s = services[i].getService();
				 if (s.hasAlternateRecurringCharge()) {
					 charges += s.getAlternateRecurringCharge(subscriber);
				 } else {
					 charges += s.getRecurringCharge();
				 }

			 } catch (TelusAPIException e) {
				 Logger.debug(e);
			 }
		 }

		 return charges;
	 }

	 public boolean isAirtimePoolingEnabled() {
		 try {			 
			 return isPoolingEnabled(PoolingGroup.AIRTIME);
		 } catch (Throwable t) {
			 // Eat the exception for now - returns false in this case.  See TODO note above.
			 Logger.debug("Exception occurred: isAirtimePoolingEnabled method.  Returning false.");
		 }
		 return false;
	 }

	 public boolean isLDPoolingEnabled() {
		 try {
			 return isPoolingEnabled(PoolingGroup.LONG_DISTANCE);
		 } catch (Throwable t) {
			 // Eat the exception for now - returns false in this case.  See TODO note above.
			 Logger.debug("Exception occurred: isLDPoolingEnabled method.  Returning false.");
		 }
		 return false;
	 }
	 
	 public boolean isPoolingEnabled(int poolingGroupId) throws TelusAPIException {
		 
		 if (pricePlan.isMinutePoolingCapable()) {
			 ContractFeature[] features = getFeatures(true);
			 for (int i = 0; i < features.length; i++) {
				 RatedFeature feature = features[i].getFeature();
				 if (feature.isPooling() && feature.getPoolGroupId().equals(String.valueOf(poolingGroupId))) {
					 return true;
				 }
			 }
		 }
		 return false;
	 }
	 
	 public boolean isShareable() throws TelusAPIException {			
		 return isShareablePricePlanPrimary() || isShareablePricePlanSecondary();
	 }
	 
	 public boolean isDollarPooling() throws TelusAPIException {	

		 if (pricePlan.isDollarPoolingCapable()) {
			 if (subscriber.isPCS()) {
				 return true;
			 }else if (subscriber.isIDEN()) {
				 ContractFeature[] features = getFeatures(true);
				 for (int i = 0; i < features.length; i++) {
					 RatedFeature feature = features[i].getFeature();
					 if (feature.isDollarPooling()) {
						 return true;
					 }
				 }
			 }
		 }
		 return false;
	 }

	 private void checkInvoiceFormatChange(Service service, boolean addService) throws TelusAPIException {
		 //  BAN auto -suppress
		 
		 if (pricePlan.getBrandId() == Brand.BRAND_ID_TELUS && service != null && !service.isWPS() && service.containsCategory(Feature.CATEGORY_CODE_BLIN)) {
			if((subscriber.getAccount()) instanceof PostpaidAccount ){	  
				PostpaidAccount account = (PostpaidAccount) subscriber.getAccount();
				InvoiceProperties invoiceProperties = account.getInvoiceProperties();
				if (addService) {
					changeInvoiceFormat = invoiceProperties.getInvoiceSuppressionLevel() != null && invoiceProperties.getInvoiceSuppressionLevel().equals(InvoiceSuppressionLevel.SUPPRESS_ALL);
				}
			}
		 }
	 }

	 private void changeInvoiceFormat() throws TelusAPIException {
		 PostpaidAccount account = (PostpaidAccount) subscriber.getAccount();
		 InvoiceProperties invoiceProperties = account.getInvoiceProperties();

		 if (invoiceProperties.getInvoiceSuppressionLevel() != null && invoiceProperties.getInvoiceSuppressionLevel().equals(InvoiceSuppressionLevel.SUPPRESS_ALL)) {
			 invoiceProperties.setInvoiceSuppressionLevel(InvoiceSuppressionLevel.GROUP_DISPATCH);
			 invoiceProperties.setHoldRedirectDestinationCode(null);
			 invoiceProperties.setHoldRedirectFromDate(null);
			 invoiceProperties.setHoldRedirectToDate(null);
		 }
		 else {
			 invoiceProperties.setInvoiceSuppressionLevel(InvoiceSuppressionLevel.SUPPRESS_ALL);
			 invoiceProperties.setHoldRedirectDestinationCode(BillHoldRedirectDestination.HOLD_BILL);
			 invoiceProperties.setHoldRedirectFromDate(new java.util.GregorianCalendar().getTime());

			 Date holdRedirectToDate = null;
			 try {
				 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				 dateFormat.setLenient(false);
				 holdRedirectToDate = dateFormat.parse("20991231");
			 }
			 catch(Exception e) {
				 holdRedirectToDate = new Date();
			 }

			 invoiceProperties.setHoldRedirectToDate(holdRedirectToDate);
		 }

		 account.setInvoiceProperties(invoiceProperties);
		 account.save();
	 }
	 
	 private boolean testBoundServiceAddition(Service addOnService, Service boundService) throws TelusAPIException {

		 Date addOnSOCExpDate = addOnService.getExpiryDate();
		 Date boundSOCExpDate = boundService.getExpiryDate();
		 Date today = getLogicalDate();


		 if  (( addOnSOCExpDate == null || addOnSOCExpDate.compareTo(today) > 0 )&&
				 ( boundSOCExpDate == null || boundSOCExpDate.compareTo(today) > 0 )) {
			 if ((addOnService.isCurrent() &&  boundService.isCurrent()) || !addOnService.isCurrent()) {
				 if (boundService.isNetworkEquipmentTypeCompatible( getValidationEquipment())) {
					 return true;
				 }
			 }
		 }

		 return false;

	 }

	 /**
	  * Returns the Visto SOC if this contract has any Visto service.
	  * Otherwise, returns null
	  * @return
	  */
	 private Service hasVisto(boolean includeDeleted) throws TelusAPIException {
		 ContractService[] services = delegate.getServices0(includeDeleted);
		 if (services != null) {
			 for (int i = 0; i < services.length; i++) {
				 if (services[i].getService().isVisto()) {
					 return services[i].getService();
				 }
			 }
		 }
		 return null;
	 }

	 private ContractFeature findContractFeature(String featureCode){
		 ContractFeature[] features = getFeatures();
		 for(int i = 0; i < features.length; i++){
			 if(features[i].getFeature().getCode().equals(featureCode)){
				 return features[i];
			 }
		 }
		 return null;   
	 }
	 
	 /**
	  * This method check if this contract contain any voice mail dependent services but lack of of voice mail service itself.
	  * when we find any VM orphan service, this method will remove the service. 
	  * 
	  * @param removeDependant
	  * @throws InvalidServiceChangeException
	  * @throws TelusAPIException
	  */
	void checkForVoicemailService( ) throws InvalidServiceChangeException, TelusAPIException {

		List vmOrphanFeatures = getVoicemailOrphanFeatures();
		 
		if (!vmOrphanFeatures.isEmpty()) {
			for (int i=0; i < vmOrphanFeatures.size(); i++) {
				removeService( true, ((ContractFeature)vmOrphanFeatures.get(i)).getServiceCode());
			}
		}
	}

	/**
	 * Some service / feature depends on voice mail service, when the voice mail itself does not exist on a contract, those service become 
	 * "orphan" service / feature.
	 * 
	 * This method return a list of voice mail dependent feature when voice mail service itself does not exist in this contract
	 * 
	 * @return empty list if the contract has VM service, other return a list of VM orphan ContractFeature if there is any. The return can be 
	 * empty, but will never be null.
	 *   
	 * @throws UnknownObjectException
	 * @throws TelusAPIException
	 */
	List getVoicemailOrphanFeatures() throws UnknownObjectException, TelusAPIException  {

		List featureList = new ArrayList();

		ContractFeature[] features = getFeatures(true);

		//loop through all the features in this contract
		for(int i = 0; i<features.length; i++) {

			String featureCategory = features[i].getFeature().getCategoryCode(); 

			String switchCode = features[i].getFeature().getSwitchCode();
			//trimming switchCode is necessary as the switchCode sometimes has padding space at the end.
			if ( switchCode!=null ) switchCode = switchCode.trim();
			
			//check if the feature is VTT or VVM , both require VM service
			if(Feature.CATEGORY_CODE_VOICE2TEXT.equals(featureCategory) 
				  || Feature.CATEGORY_CODE_VISUALVOICEMAIL.equals(featureCategory)) {
				 
				//The following check is to ensure that the V2T or VVM feature is not on promotion or bound service. 
				//These type of service is not added by customer's request, so it's okay to leave them on the contract.
				
				//The next line itself implies that these VM dependent feature will never be price plan included feature.
				//Otherwise, the call will fail.
				Service theService = delegate.getService(features[i].getServiceCode()).getService();
				if (!theService.isPromotion() && !theService.isBoundService() && !theService.isSequentiallyBoundService()) {
					 featureList.add(features[i]);
				}
			} 
			//check if the feature is VM feature
			else if ( Feature.SWITCH_CODE_VOICE_MAIL.equals( switchCode ) ) {
				
				//The following check is necessary: it's to make sure we are not going to call delegate.getService( pricePlanCode );
				//which will definitely cause Exception.
				if ( features[i].getServiceCode().equals(getPricePlan().getCode())) {

					//this feature is a PricePlan's included feature
					//Now we are sure the contract does have VM feature, no need to continue the checking. We can
					//safely assert that there is no VM orphan service by emptying the return collection.   
					featureList.clear();
					break;
				}
				
				//The following check is strange to me. Per Winnie, the only rational explanation would be: 
				//if an customer want to buy VM dependent service, then he/she has to buy the VM service. 
				//Promotion/Bound services are freebie , so exclude them from the checking.
				Service theService = delegate.getService(features[i].getServiceCode()).getService();
				if (!theService.isPromotion() && !theService.isBoundService() && !theService.isSequentiallyBoundService()) {

					//Now we are sure the contract does have VM feature, no need to continue the checking. We can
					//safely assert that there is no VM orphan service by emptying the return collection.   
					featureList.clear();
					break;
				}
			}
		}
		 
		return featureList;
	 }

	 public PricePlanValidation getPricePlanValidation(){
		 return delegate.getPricePlanValidation(); 
	 }  

	 
	public CallingFeatureCycle calculatePrepaidFeatureCycleDates() throws TelusAPIException {
		CallingFeatureCycleInfo info = null;
		ContractService callingCircleService = getPrepaidCallingCircleService();
		
		if (callingCircleService!=null) {
			Calendar today = getEODToday();
			info = new CallingFeatureCycleInfo();
			Calendar cal = Calendar.getInstance();
			cal.setTime(callingCircleService.getExpiryDate());
			int term = callingCircleService.getService().getTerm();
			while ( cal.after(today) ) {
				cal.add(Calendar.DATE, (0-term) );
			}
			info.setStartDate(cal.getTime());
			
			cal.add(Calendar.DATE, term);
			info.setEndDate( cal.getTime() );
			
			ServiceAgreementInfo kbMappedPrepaidService = getKbMappedPrepaidService(callingCircleService.getService().getWPSMappedKBSocCode());
			if (kbMappedPrepaidService!=null) 
				info.setLastUpdateDate( getCCParameterLastUpdateDate( kbMappedPrepaidService ));
			
		}
		return info;
	}

	public static Calendar getEODToday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal;
	}

	ContractService getPrepaidCallingCircleService() throws TelusAPIException {
		ContractService[] services  = getOptionalServices();
		for( int i=0; i<services.length; i++ ) {
			if (services[i].getService().isWPS() 
					&& (services[i].getService().containsSwitchCode(FeatureInfo.SWITCH_CODE_CALLING_CIRCLE) 
						||services[i].getService().containsSwitchCode(FeatureInfo.SWITCH_CODE_CALL_HOME_FREE))
				) {
				return services[i];
			}
		}
		return null;
	}
	
	// This method returns true if NOT saved contract contains prepaid calling circle SOC 
	// in any status (add, delete or update)
	private boolean containsNotSavedPrepaidCallingCircleService() throws TelusAPIException {
		boolean found = false;
		ServiceAgreementInfo[] allServices = delegate.getServices0(true);
		ServiceAgreementInfo aService = null;
		for(int i=0; i<allServices.length; i++) {
			aService = allServices[i];
			if (aService.getService().isWPS() && 
					(aService.getService().containsSwitchCode(FeatureInfo.SWITCH_CODE_CALLING_CIRCLE) 
							||aService.getService().containsSwitchCode(FeatureInfo.SWITCH_CODE_CALL_HOME_FREE))  
							&& 
					aService.getTransaction() != ServiceFeatureInfo.NO_CHG){
				
				found = true;
				
				break;
			}
		}		
		return found;
	}
	
	// This method returns ServiceAgreementInfo if NOT saved contract contains prepaid calling circle SOC 
	// in any status (add, delete or update)
	ServiceAgreementInfo getNotSavedPrepaidCallingCircleService() throws TelusAPIException {
		ServiceAgreementInfo[] allServices = delegate.getServices0(true);
		ServiceAgreementInfo aService = null;
		for(int i=0; i<allServices.length; i++) {
			aService = allServices[i];
			if (aService.getService().isWPS() && 
					 (aService.getService().containsSwitchCode(FeatureInfo.SWITCH_CODE_CALLING_CIRCLE) 
					  || aService.getService().containsSwitchCode(FeatureInfo.SWITCH_CODE_CALL_HOME_FREE)) 
					  && aService.getTransaction() != ServiceFeatureInfo.NO_CHG){
				return aService;
			}
			
		}		
		return null;
	}
	
	
	ServiceAgreementInfo getKbMappedPrepaidService(String kbMappedPrepaidServiceCode) throws TelusAPIException {
		ServiceAgreementInfo[] allServices = delegate.getServices0(true);
		ServiceAgreementInfo aService = null;
		for(int i=0; i<allServices.length; i++) {
			aService = allServices[i];
			if (aService.getCode().trim().equals(kbMappedPrepaidServiceCode.trim())){
				return aService;
			}
		}		
		return null;
	}
	
	ServiceFeatureInfo getKbCallingCircleFeature(ServiceAgreementInfo kbMappedPrepaidService)	{
		ServiceFeatureInfo [] allfeatures = kbMappedPrepaidService.getFeatures0(false);
		ServiceFeatureInfo feature = null;
		for(int i=0; i<allfeatures.length; i++) {
			feature = allfeatures[i];
			if (feature.getFeature().getSwitchCode().trim().equals(FeatureInfo.SWITCH_CODE_CALLING_CIRCLE) 
				 ||	feature.getFeature().getSwitchCode().trim().equals(FeatureInfo.SWITCH_CODE_CALL_HOME_FREE))
				return feature;
		}
		return null;
	}

	private ServiceFeatureInfo getPrepaidCallingCircleFeature(ServiceAgreementInfo callingCircleService, String featureCode)	{
		ServiceFeatureInfo [] allfeatures = callingCircleService.getFeatures0(false);
		ServiceFeatureInfo feature = null;
		for(int i=0; i<allfeatures.length; i++) {
			feature = allfeatures[i];
			if (feature.getFeature().getCode().trim().equals(featureCode.trim()))
				return feature;
		}
		return null;
	}

	public void save(String dealerCode, String salesRepCode, ServiceRequestHeader header) throws InvalidServiceChangeException,
			TelusAPIException, PhoneNumberException {
		
		TMSubscriber oldSubscriber = (TMSubscriber)provider.getAccountManager0().findSubscriberByPhoneNumber(subscriber.getPhoneNumber());
	
		SubscriberContractInfo oldContractInfo = oldSubscriber.getContract0().getDelegate();		
		
		EquipmentChangeRequest equipChangeRequest = equipmentChangeRequest;
		
		Equipment currentEquipment = null;
		Equipment cuurentMule = null;
		
		if (equipChangeRequest!=null) {
			currentEquipment = subscriber.getEquipment();
			if (  equipChangeRequest.getAssociatedMuleEquipment()!=null 
				&& currentEquipment instanceof SIMCardEquipment ) {
				cuurentMule =((SIMCardEquipment) currentEquipment).getLastMule();
			}
		}

		ContractService[] addedServices = getAddedServices();
		ContractService[] removedServices = getDeletedServices();
		ContractService[] updatedServices = getChangedServices();
		
		List changedFeatures = new ArrayList();
		
		for (int idx = 0; idx < updatedServices.length; idx++) {
			ContractFeature [] changedServiceFeatures = ((TMContractService) updatedServices[idx]).getChangedFeatures();
			getChangedFeatures(changedServiceFeatures, changedFeatures);
		}
		
		ContractFeature [] changedIncludedFeatures = getChangedFeatures();
		getChangedFeatures(changedIncludedFeatures, changedFeatures);
		
		ContractFeature [] allChangedFeatures = (ContractFeature []) changedFeatures.toArray( 
				new ContractFeature[changedFeatures.size()]);
		
		//commit the contract change to KB
		save( dealerCode, salesRepCode );
		

		//try to save the transaction to SRPDS
		if ( header!=null && AppConfiguration.isSRPDSEnabled()==true	) {
			TMServiceRequestManager serviceRequestManager = (TMServiceRequestManager)provider.getServiceRequestManager();
			
			serviceRequestManager.reportChangeContract( 
					subscriber.getBanId(), subscriber.getSubscriberId(),
					dealerCode, salesRepCode, provider.getUser(), 
					this.getDelegate(), oldContractInfo, addedServices, removedServices, 
					updatedServices, allChangedFeatures, header );
			

			if (equipChangeRequest!=null) {
				serviceRequestManager.reportChangeEquipment( 
						subscriber.getBanId(), subscriber.getSubscriberId(),
						dealerCode, salesRepCode, provider.getUser(), 
						currentEquipment, 
						equipChangeRequest.getNewEquipment(), 
						equipChangeRequest.getRepairId(),
						equipChangeRequest.getSwapType(), 
						cuurentMule, 
						equipChangeRequest.getAssociatedMuleEquipment(), 
						header);
			}
		}
	}
	
	private List getChangedFeatures(ContractFeature [] features, List result) {
		if (features != null) {
			result.addAll(Arrays.asList(features));
		}
		return result;
	}
	
	
	/*
	 * New method introduced for Holborn R1 project
	 */
	public Service[] removeNonMatchingRestrictedSOCs(String networkType) throws TelusAPIException {
		
		//If subscriber's equipment is compatible with all network types, then all services are matching (return immediately)
		if (networkType.equalsIgnoreCase(NetworkType.NETWORK_TYPE_ALL)) {
			return null;
		}
		
		Service[] restrictedSOCs = getNonMatchingRestrictedSOCs(networkType);
		
		//defect PROD00154523 background
		// a bound service's network type configuration is subset of the leading SOC ( the service that this 
		// service bound to), this pose issue when swap equipment between network. 
		//  Example: 
		// SOC SRIM40NN support both CDMA and HSPA networks
		//  its bound service: SRIM3M only support CDMA network
		// When a subscriber who has SOC SRIM40NN, and trying the swap from CDMA to HSPA through equipment
		// change. SOC SRIM40NN will remain on profile whereas SOC SRIM3M should be dropped. 
		//
		// But the public API revmoeService( String serviceCode) prevent us from removing 
		// Bound Service (throwing InvalidServiceChangeException: service can only be removed by system: SRIM3MF  : [reason=BOUND_SERVICE])
		//
		// The fix are 
		// 1) make a new private method removeService(boolean allowBound, String serviceCode)
		// 2) move all logic of the current removeService(serviceCode) to new method with slight changes: 
		//     call removeSerivce0( allowBound, serviceCode) instead of removeService0(serviceCode) 
		// 3)  current removeService( serviceCode) just call the new method with allowBound=false
		// 4) remove removeService0( serviceCode )
		// 5) Here, we call new method with allowBound=true;  
		for (int i=0; i < restrictedSOCs.length; i++) {
			removeService(true, restrictedSOCs[i].getCode());
		}
		
		return restrictedSOCs;
	}

	
	/*
	 * New method introduced for Holborn R1 project
	 */
	private Service[] getNonMatchingRestrictedSOCs(String networkType) throws TelusAPIException {
		
		List nonMatchingSOCs = new ArrayList();
		
		//obtain included and optional contract services to validate networkTypes
		ContractService[] includedServices = getIncludedServices();
		ContractService[] optionalServices = getOptionalServices();
		
		for (int i=0; i < includedServices.length; i++) {
			if (!includedServices[i].getService().isCompatible(networkType))
				nonMatchingSOCs.add(includedServices[i].getService());
		}
		
		for (int i=0; i < optionalServices.length; i++) {
			if (!optionalServices[i].getService().isCompatible(networkType))
				nonMatchingSOCs.add(optionalServices[i].getService());
		}
		
		return (Service[])nonMatchingSOCs.toArray(new Service[nonMatchingSOCs.size()]);
	}

	/* (non-Javadoc)
	 * @see com.telus.api.account.Contract#getServicesRestrictedByNetworkType(com.telus.api.equipment.Equipment)
	 */
	public Service[] getServicesRestrictedByNetworkType(
			Equipment targetEquipment) throws TelusAPIException {
		return getNonMatchingRestrictedSOCs( targetEquipment.getNetworkType() );
	}

	/**
	 * This method is intended to fix RIM / MB provisioning for existing RIM/MB CDMA 
	 * subscribers switching to HSPA only.
	 * 
	 * This method was created for fixing CDMA to HSPA equipment change. 
	 * Scenario: An APN SOC needs to be present for RIM/MB (Mobile Broadband). Such SOC needs to be added to subscriber that currently has
	 *           an RIM/MB CDMA equipment, and switching to HSPA. Price plan has been updated with included SOC, and this method would re-add
	 *           this missing included service to subscriber's contract.
	 *           
	 * IMPORTANT: This method should be called at the end of transaction only.              
	 */
	public void refreshSocAndFeatures() {
		String methodName = "refreshSocAndFeatures";
		
		try {
			boolean provisionForRimAndMB = AppConfiguration.isRefreshApn();
			if (provisionForRimAndMB) {
				ArrayList removedOptionalServices = new ArrayList();
				ContractService[] optionalServices = getOptionalServices();
				Service rimAddOnSoc = provider.getReferenceDataManager().getRegularService(getAddOnAPNSoc(Feature.FEATURE_CODE_RIMAPN));
				Service mbAddOnSoc = provider.getReferenceDataManager().getRegularService(getAddOnAPNSoc(Feature.FEATURE_CODE_MBAPN));
				AccountType accountType = provider.getReferenceDataManager0().getAccountType(subscriber.getAccountSummary());
				String defaultDealerCode = accountType.getDefaultDealer();
				String defaultSalesCode = accountType.getDefaultSalesCode();
				boolean addRimAddOn = false;
				boolean addMbAddOn = false;
				Date rimAddOnExpiry = null;
				Date mbAddOnExpiry = null;
				
				// STEP 1: Process optional services first as they can override included services
				for (int i = 0; i < optionalServices.length; i++) {
					Service refOptionalService = optionalServices[i].getService();

					if ((refOptionalService.hasRIMAPN() && !optionalServices[i].containsFeature(Feature.FEATURE_CODE_RIMAPN)) || // if RIM APN SOC and contract doesn't have the latest update
							(refOptionalService.hasMBAPN() && !optionalServices[i].containsFeature(Feature.FEATURE_CODE_MBAPN))) {
						ServiceAgreementInfo removedSoc = null;
						if ((refOptionalService.getExpiryDate() == null || refOptionalService.getExpiryDate().after(getLogicalDate())) 
								&& provider.getDealerManager().findDealer(optionalServices[i].getDealerCode()) != null) { // still for sale and dealer is not expired
							removedOptionalServices.add(optionalServices[i]);
							log(methodName, "Removing Service [" + optionalServices[i].getCode() + "]. Effective="+
											optionalServices[i].getEffectiveDate()+
											",Expiry="+optionalServices[i].getExpiryDate()+
											",DealerCode="+optionalServices[i].getDealerCode()+
											",SalesRep="+optionalServices[i].getSalesRepId());
							removedSoc = removeServiceImpl(optionalServices[i].getCode());
						} 
						
						if (removedSoc == null ){
							log(methodName, "Not dropping SOC [" + refOptionalService.getCode() + "] because it is not for sale or dealer is expired.");
							
							//check if RIM add-on should be added instead
							if (rimAddOnSoc != null && refOptionalService.hasRIMAPN()) {
								if (!addRimAddOn) { //flag not yet true
									addRimAddOn = true;
									rimAddOnExpiry = optionalServices[i].getExpiryDate();
								}else {
									if (rimAddOnExpiry != null) { //determine the expiry date
										if (optionalServices[i].getExpiryDate() == null || 
											rimAddOnExpiry.before(optionalServices[i].getExpiryDate())) {
											rimAddOnExpiry = optionalServices[i].getExpiryDate();
										}
									}
								}
							}
							
							//check if MB add-on should be added instead
							if (mbAddOnSoc != null && refOptionalService.hasMBAPN() ) {
								if (!addMbAddOn) { //flag not yet true
									addMbAddOn = true;
									mbAddOnExpiry = optionalServices[i].getExpiryDate();
								}else {
									if (mbAddOnExpiry != null) { //determine the expiry date
										if (optionalServices[i].getExpiryDate() == null || 
											mbAddOnExpiry.before(optionalServices[i].getExpiryDate())) {
											mbAddOnExpiry = optionalServices[i].getExpiryDate();
										}
									}
								}
							}
						}
					}
				}
				
				addRimAddOn = addRimAddOn && !containsService(rimAddOnSoc.getCode());
				addMbAddOn = addMbAddOn && !containsService(mbAddOnSoc.getCode());
				
				if (addRimAddOn) {
					log(methodName, "Adding " + rimAddOnSoc.getCode());
					try {
						addService(rimAddOnSoc, null, rimAddOnExpiry);
					}catch (Throwable isce) {
						log (methodName, "Cannot add " + rimAddOnSoc.getCode() +". Exception"+isce);
						addRimAddOn = false;
					}
				}
				
				if (addMbAddOn) {
					log(methodName, "Adding " + mbAddOnSoc.getCode());
					try {
						addService(mbAddOnSoc, null, mbAddOnExpiry);
					}catch (Throwable isce) {
						log (methodName, "Cannot add " + mbAddOnSoc.getCode() +". Exception"+isce);
						addMbAddOn = false;
					}
				}
				
				boolean sameDealerCode = haveSameDealerAndSalesRepCode((ContractService[]) removedOptionalServices.toArray(new ContractService[removedOptionalServices.size()]));
				delegate.getPricePlanValidation0().setEquipmentServiceMatch(false);
				if (removedOptionalServices.size() > 0 || addRimAddOn || addMbAddOn) {
					log(methodName, "Saving contract after service removal or add-on add.");
					//commit removal or add-on soc
					provider.getSubscriberLifecycleFacade().changeServiceAgreement(subscriber.getDelegate(), 
							  delegate, 
							  defaultDealerCode, 
							  defaultSalesCode, 
							  delegate.getPricePlanValidation0(), 
							  provider.getAccountNotificationSuppressionIndicator(subscriber.getBanId()), null,
							  SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
					delegate.commit();

					if (removedOptionalServices.size() > 0) {
						log(methodName, "sameDealerCode=" + sameDealerCode);
						
						if (sameDealerCode) {
							// if all the SOCs to be added back had the same dealer code in SERVICE_AGREEMENT, then we can add them altogether
							// otherwise, we have to add them one by one and save them individually
							String socDealerCode = "";
							String socSalesRepId = "";
							boolean wasAnySocAdded = false;
							for (int i = 0; i < removedOptionalServices.size(); i++) {
								ContractService optionalApnSoc = (ContractService) removedOptionalServices.get(i);
								socDealerCode = optionalApnSoc.getDealerCode();
								socSalesRepId = optionalApnSoc.getSalesRepId();
								try {
									log(methodName, "Re-adding Service [" + optionalApnSoc.getCode() + "]");
									ServiceAgreementInfo soc = addService0(optionalApnSoc.getService(), null, optionalApnSoc.getExpiryDate(), true); // add the service back
									soc.setTransaction(BaseAgreementInfo.ADD);
									copyServiceFeatures(soc, optionalApnSoc);
									wasAnySocAdded = true;
								}catch (Throwable t) {
									log(methodName, "Exception when adding service ["+optionalApnSoc.getCode() + "] Exception:"+ t);
								}
							}
							if (wasAnySocAdded) {
								log(methodName, "changeServiceAgreement after adding services.");
								provider.getSubscriberLifecycleFacade().changeServiceAgreement(subscriber.getDelegate(), 
										  delegate, 
										  socDealerCode, 
										  socSalesRepId, 
										  delegate.getPricePlanValidation0(), 
										  provider.getAccountNotificationSuppressionIndicator(subscriber.getBanId()), null,
										  SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
							}
						} else { // add and save one by one
							for (int i = 0; i < removedOptionalServices.size(); i++) {
								ContractService optionalApnSoc = (ContractService) removedOptionalServices.get(i);
								try{
									log(methodName, "Re-adding Service [" + optionalApnSoc.getCode() + "]");
									ServiceAgreementInfo soc = addService0(optionalApnSoc.getService(), null, optionalApnSoc.getExpiryDate(), true); // add the service back
									soc.setTransaction(BaseAgreementInfo.ADD);
									copyServiceFeatures(soc, optionalApnSoc);
									log(methodName, "changeServiceAgreement after adding " + optionalApnSoc.getCode());
									provider.getSubscriberLifecycleFacade().changeServiceAgreement(subscriber.getDelegate(), 
											  delegate, 
											  optionalApnSoc.getDealerCode(), 
											  optionalApnSoc.getSalesRepId(), 
											  delegate.getPricePlanValidation0(), 
											  provider.getAccountNotificationSuppressionIndicator(subscriber.getBanId()), null,
											  SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
									delegate.commit();
								}catch (Throwable t) {
									log(methodName, "Exception when adding service ["+optionalApnSoc.getCode() + "]. Exception:"+ t);
								}
							}
						}
						
						delegate.commit();
					}
				} else {
					log(methodName, "No optional SOC dropped/added.");
				}

				//STEP 2: Process included services
				String[] apnSocs = AppConfiguration.getIncludedApnSocs(); // retrieve list of APN SOCs that are included in PPs
				boolean includedApnSocsAdded = false;
				for (int j = 0; j < apnSocs.length; j++) {
					if (getPricePlan().containsIncludedService(apnSocs[j]) && !delegate.containsService(apnSocs[j])) {
						// 1. if this APN SOC is included with the price plan on contract
						// 2. but it is missing from the contract
						addMissingIncludedService(apnSocs[j]); // add it back
						includedApnSocsAdded = true;
					}
				}

				if (includedApnSocsAdded) {
					log(methodName, "Final contract save.");
					String socDealerCode = delegate.getPricePlanDealerCode();
					String socSalesRepId = delegate.getPricePlanSalesRepId(); 
					Dealer dealer = provider.getDealerManager().findDealer(socDealerCode);
					if (dealer == null) {
						socDealerCode = defaultDealerCode;
						socSalesRepId = defaultSalesCode;
					}
					provider.getSubscriberLifecycleFacade().changeServiceAgreement(subscriber.getDelegate(), 
							  delegate, 
							  socDealerCode, 
							  socSalesRepId, 
							  delegate.getPricePlanValidation0(),
							  provider.getAccountNotificationSuppressionIndicator(subscriber.getBanId()), null,
							  SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
					delegate.commit();
				} else {
					log(methodName, "No PP Included APN SOC has been added to subscriber.");
				}
			}
		} catch (Throwable t) {
			log(methodName, "Exception in refreshSOCOrFeatures " + t);
		}

	}
	
	
	/**
	 * Assumption: getPriceplan().getIncludedService(code) will not return UnknownObjectException
	 * @param code
	 */
	private void addMissingIncludedService(String code) {
		String methodName = "addMissingIncludedService";
		try {
			Service soc = getPricePlan().getIncludedService(code); //Retrieve information about the SOC
			if (soc.hasRIMAPN() || soc.hasMBAPN()) { //logic is applicable for RIM/MB APN SOCs only
				RatedFeature[] ratedFeatures = soc.getFeatures();
				for (int i = 0; i < (ratedFeatures != null ? ratedFeatures.length : 0); i++) {
					if (!ratedFeatures[i].isDuplFeatureAllowed() && 
						delegate.containsFeature0(ratedFeatures[i].getCode(), getLogicalDate(), null, true, false)) {
						//if there exists feature conflict with this SOC, do not add
						return ;
					}
				}
				//add only if there is no feature conflict
				log(methodName, "Adding Missing Inclusive SOC ["+code+"] to PP ["+getPricePlan().getCode()+"]");
				addService(soc, null, getExpiryDate()); //add the included SOC
			}
		}catch (UnknownObjectException uoe) {
			log(methodName, " Unexpected exception. "+uoe);
		}catch (InvalidServiceChangeException isce) {
			log (methodName, " Unable to add the missing included SOC "+code +". "+isce);
		}catch (Throwable t) {
			log (methodName, " Error adding missing included service "+code+". Exception="+t);
		}
	}

	private String getAddOnAPNSoc(String featureCode) {
		return (String) AppConfiguration.getAddOnAPNSocs().get(featureCode);
	}
	
	protected void log (String methodName, String message) {
		Logger.debug (methodName + ": ["+subscriber.getPhoneNumber()+"] "+message);
	}
	
	private boolean haveSameDealerAndSalesRepCode(ContractService[] cs) {
		if (cs != null && cs.length > 0) {
			String dealerCode = null;
			String salesRepId = null;
			
			for (int i = 0 ; i < cs.length; i++) {
				if (dealerCode == null || salesRepId == null) {
					dealerCode = cs[i].getDealerCode();
					salesRepId = cs[i].getSalesRepId();
				}else if (!dealerCode.equals(cs[i].getDealerCode()) || !salesRepId.equals(cs[i].getSalesRepId())) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	private void copyServiceFeatures(ServiceAgreementInfo sa, ContractService srcService) throws TelusAPIException {
		ContractFeature[] unsavedFeatures = sa.getFeatures();
		ContractFeature[] originalFeatures = srcService.getFeatures();
		
		for (int i = 0; i < (unsavedFeatures != null ? unsavedFeatures.length : 0); i++) {
			for (int j = 0; j < (originalFeatures != null ? originalFeatures.length : 0); j++) {
				if (unsavedFeatures[i].getCode().trim().equals(originalFeatures[j].getCode().trim())) {
					unsavedFeatures[i].setParameter(originalFeatures[j].getParameter());
				}
			}
		}
		
	}

	
	private Date getLogicalDate() {
		if (logicalDate == null) {
			try {
				logicalDate = provider.getReferenceDataManager().getLogicalDate();
			}catch (Throwable t) {
				Logger.debug("Error retrieving logical date in TMContract. Returning system date."+t);
				return new Date();
			}
		}
		
		return logicalDate;
	}

	 /**
	  * Restricted use. Currently only called by TMMigrationRequest
	  * @param sourceSubscriber TMSubscriber
	  * @param targetSubscriber TMSubscriber
	  */
	void setMigrationSubscriber(TMSubscriber sourceSubscriber, TMSubscriber targetSubscriber) {
		 this.migrationTargetSubscriber = targetSubscriber;
		 this.migrationSourceSubscriber = sourceSubscriber;
	}

	private ContractChangeInfo extractContractChangeInfo() throws TelusAPIException {
		ContractChangeInfo changeInfo = new ContractChangeInfo();
		
		//contract to be populated
		changeInfo.setCurrentContractInfo(delegate);
		changeInfo.setPricePlanChangeInd(priceplanChange);
		
		if ( migrationSourceSubscriber!=null ) {
			//this contract is created for migraiton , in which case, we need set previous subscriber on which 
			//we search calling circle history
			changeInfo.setPreviousSubscriberInfo(migrationSourceSubscriber.getDelegate());
			
			changeInfo.setCurrentAccountInfo(migrationTargetSubscriber.getAccount0().getDelegate0());
			changeInfo.setCurrentSubscriberInfo (migrationTargetSubscriber.getDelegate() );
			changeInfo.setPricePlanChangeInd(true);
		} else {
			changeInfo.setCurrentAccountInfo(subscriber.getAccount0().getDelegate0());
			changeInfo.setCurrentSubscriberInfo(subscriber.getDelegate() );
		}
		
		return changeInfo;
	}
	
	public void prepopulateCallingCircleList() throws TelusAPIException {
		
		ServiceFeatureInfo[] contractFeatures = delegate.getEmptyCCListFeatures();
		if ( contractFeatures.length>0 ) {
			ContractChangeInfo changeInfo = extractContractChangeInfo();

			try {
				SubscriberContractInfo contractInfo = provider.getSubscriberLifecycleFacade().prepopulateCallingCircleList(changeInfo);

				for( int i=0; i<contractFeatures.length; i++ ) {
					ServiceFeatureInfo thisFeature = contractFeatures[i];
					ServiceFeatureInfo thatFeature = contractInfo.getService0( thisFeature.getServiceCode(),true).getFeature0( thisFeature.getCode(), true );
					thisFeature.copyCallingCircleInfo(thatFeature);
				}
				
			} catch (ApplicationException e) {
				provider.getExceptionHandler().handleException( e);
			}
		}
	}

	public void evaluateCallingCircleCommitmentData() throws TelusAPIException {
		
		ServiceFeatureInfo[] contractFeatures = delegate.getNullCCCommitmentDataFeatures(); 
		
		if (contractFeatures.length>0) {
			ContractChangeInfo changeInfo = extractContractChangeInfo();

			try {
				SubscriberContractInfo contractInfo = provider.getSubscriberLifecycleFacade().evaluateCallingCircleCommitmentAttributeData(changeInfo);
				for( int i=0; i<contractFeatures.length; i++ ) {
					ServiceFeatureInfo thisFeature = contractFeatures[i];
					ServiceFeatureInfo thatFeature = contractInfo.getService0( thisFeature.getServiceCode(),true).getFeature0( thisFeature.getCode(), true );
					thisFeature.setCallingCircleCommitmentAttributeData(thatFeature.getCallingCircleCommitmentAttributeData0());
				}
			} catch (ApplicationException e) {
				provider.getExceptionHandler().handleException( e);
			}
		}
	}

	private Date getCCParameterLastUpdateDate( ServiceAgreementInfo  ccService) throws TelusAPIException {

		Date serviceEffectiveDate = ccService.getEffectiveDate();
		
		ServiceFeatureInfo cf  = SubscriberContractInfo.getCallingCircleFeature(ccService, true);
		FeatureParameterHistoryInfo lastEffectiveParameter=null;
		try {
			lastEffectiveParameter = provider.getSubscriberLifecycleHelper().retrieveLastEffectiveFeatureParameter(
					subscriber.getBanId(),
					subscriber.getSubscriberId(),
					subscriber.getProductType(),
					cf.getServiceCode(),
					cf.getCode()
					);
		} catch (ApplicationException e) {
			provider.getExceptionHandler().handleException( e);
		}
		
		Date paramEffectiveDate = lastEffectiveParameter.getEffectiveDate();
		if ( DateUtil.isSameDay(serviceEffectiveDate, paramEffectiveDate ) ) {
			return lastEffectiveParameter.getUpdateDate();
		} else if ( paramEffectiveDate.after( getLogicalDate() )) {
			return lastEffectiveParameter.getCreationDate();
		} else {
			return lastEffectiveParameter.getEffectiveDate();
		}
	}

	private void changePrepaidCallingCircleService(ServiceAgreementInfo prepaidCCService, String dealerCode, String salesRepCode) throws TelusAPIException {
	
		
/*		ServiceFeatureInfo cf  = SubscriberContractInfo.getCallingCircleFeature(prepaidCCService, false);
		if ( cf==null ) {
			//This is call home free feature!
			//Because setting feature parameter effective date only for works for calling circle feature (not call home free), 
			//we have to use the old way to process call home free
			changePrepaidCallingCircleService_CHF( prepaidCCService, dealerCode, salesRepCode);
			return;
		}
*/		
		String applId = provider.getApplication();
		String userId = provider.getUser();
		String phoneNumber = subscriber.getPhoneNumber();

		StringBuffer sb = new StringBuffer("Contract.changePrepaidCallingCircleService():  subscriber[")
			.append(subscriber.getBanId()).append("/").append( subscriber.getSubscriberId()).append("] ");
		
		try {
			
			String wpsMappedKBSoc = prepaidCCService.getService().getWPSMappedKBSocCode();
			
			if (prepaidCCService.getTransaction()==ServiceFeatureInfo.DELETE){
				
				ServiceAgreementInfo kbCCService = getKbMappedPrepaidService(wpsMappedKBSoc);
				if (kbCCService != null){
					ServiceFeatureInfo kbCCFeature = getKbCallingCircleFeature(kbCCService);
					if (kbCCFeature.getCallingCirclePhoneNumbersFromParam().length>0){
						sb.append(" remove CC list from prepaid;");
						provider.getSubscriberLifecycleManager().updatePrepaidCallingCircleParameters(applId, userId, phoneNumber, prepaidCCService, ServiceFeatureInfo.DELETE);
					}
					sb.append(" remove mapped KB CC SOC;");
					removeService(wpsMappedKBSoc);
				}
			} else if (prepaidCCService.getTransaction() == ServiceFeatureInfo.ADD ) { 
				
				//starting from July 2012 rel. we need to add mapped KB SOC even though prepaid CC feature has empty CC list
				//add KB soc if not presented in the contract
				ServiceAgreementInfo kbCCService = getKbMappedPrepaidService(wpsMappedKBSoc);
				if (kbCCService == null){
					//current contract does not contain KB SOC, add mapped KB SOC
					sb.append(" add mapped KB SOC;");
					kbCCService = ((TMContractService) addService(wpsMappedKBSoc)).getDelegate();
				}
				ServiceFeatureInfo kbCCFeature = getKbCallingCircleFeature(kbCCService);
				
				ServiceFeatureInfo prepaidCCFeature = getPrepaidCallingCircleFeature(prepaidCCService, prepaidCCService.getCode().trim());

				//update CC list in prepaid system
				//if new feature's CC list is not empty, we add the CC list to prepaid system. This does not apply
				//to migration. which the subscriber is first activated in KB, KB send request to switch -> provisioning then add subscriber's phone number
				//to prepaid platform, then provisioning will notifiy prepaid system.
				//So right after subscriber get migrated to prepaid subscriber in KB, the phone number might not be in prepaid platform yet.
				if ( prepaidCCFeature.getCallingCirclePhoneNumbersFromParam().length>0
						&& migrationSourceSubscriber==null ){
					sb.append(" add CC list to prepaid;");
					provider.getSubscriberLifecycleManager().updatePrepaidCallingCircleParameters(applId, userId, phoneNumber, prepaidCCService, ServiceFeatureInfo.ADD);
				}
				
				//sync up KB CC feature parameter from prepaid CC feature.
				kbCCFeature.setParameter(prepaidCCFeature.getParameter());
			} else { //this is UPDATE

				//add KB soc if not presented in the contract
				ServiceAgreementInfo kbCCService = getKbMappedPrepaidService(wpsMappedKBSoc);
				if (kbCCService == null){
					//current contract does not contain KB SOC, add mapped KB SOC
					sb.append(" add mapped KB SOC;");
					kbCCService = ((TMContractService) addService(wpsMappedKBSoc)).getDelegate();
				}
				ServiceFeatureInfo kbCCFeature = getKbCallingCircleFeature(kbCCService);
				
				ServiceFeatureInfo prepaidCCFeature = getPrepaidCallingCircleFeature(prepaidCCService, prepaidCCService.getCode().trim());
				
				//defect 6797 fix:
				//Root cause:
				//  1) a Prepaid CC feature added without CC list possiblly from WS 
				//  2) Later on, when adding other Prepaid feature, along with changing CC prepaid's auto-renew flag without updating CC list.
				//  When above scenario occurs, the execution flow will enter here, which will send empty CC list to prepaid API, causing Prepaid API exception
				// 
				// The fix: add more check, make sure only when CC parameter is change, then we will update prepaid system
				if (prepaidCCFeature.isCcParameterChanged()) {
					
					//update CC list in prepaid system
					
					if (FeatureInfo.SWITCH_CODE_CALL_HOME_FREE.equals( prepaidCCFeature.getFeature0().getSwitchCode() ) ) { 
						//call home free feature
						
						String[] ccList = kbCCFeature.getCallingCirclePhoneNumbersFromParam();
						if ( ccList.length==0
								|| "1111111111".equals(ccList[0]) ) {
							//the existing KB CC feature does not contain CC list, which means preapid system does not have CC list
							sb.append(" add CC list to prepaid;");
							provider.getSubscriberLifecycleManager().updatePrepaidCallingCircleParameters(applId, userId, phoneNumber, prepaidCCService, ServiceFeatureInfo.ADD);
						} else {
							//the KB CC feature contain a number other than default number, which means prepaid systme contains CC list
							sb.append(" update CC list in prepaid;");
							provider.getSubscriberLifecycleManager().updatePrepaidCallingCircleParameters(applId, userId, phoneNumber, prepaidCCService, ServiceFeatureInfo.UPDATE);
						}						
					} else { //real calling circle feature 

						if ( kbCCFeature.getCallingCirclePhoneNumbersFromParam().length==0) {
							//the existing KB CC feature does not contain CC list, which means preapid system does not have CC list
							sb.append(" add CC list to prepaid;");
							provider.getSubscriberLifecycleManager().updatePrepaidCallingCircleParameters(applId, userId, phoneNumber, prepaidCCService, ServiceFeatureInfo.ADD);
						} else {
							//the KB CC feature contain CC list, which means prepaid systme contains CC list
							sb.append(" update CC list in prepaid;");
							provider.getSubscriberLifecycleManager().updatePrepaidCallingCircleParameters(applId, userId, phoneNumber, prepaidCCService, ServiceFeatureInfo.UPDATE);
						}
					}
	
					//sync up KB CC feature parameter from prepaid CC feature.
					kbCCFeature.setParameter(prepaidCCFeature.getParameter());
				}
			} 
			
			sb.append( " SubscriberLifecycleFacade.changeServiceAgreement()");
			
			provider.getSubscriberLifecycleFacade().changeServiceAgreement(
					subscriber.getDelegate(),
					delegate,
					dealerCode,
					salesRepCode,
					delegate.getPricePlanValidation0(),
					provider.getAccountNotificationSuppressionIndicator(subscriber.getBanId()), null,
					SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
			
			Logger.debug( sb.append(" succeeded.").toString() );
			
		}catch (Throwable t) {
			Logger.debug( sb.append(" failed") );
			Logger.debug( t );
			provider.getExceptionHandler().handleException(t);
		}
	}
	
	/*
	 * Retrieves the default dealer and default salesrep for the account associated
	 * with the given subscriber. These values are based on account type, account sub type and brand.
	 * If the ldap flag (rollback) is false, then we do not retrieve the default values and return null.
	 * Also when the initial call to getAccountType returns an empty/null dealer/salesrep code, we use the default AccountSubType = "R" to retrieve the
	 * AccountType object and call the getAccountType method again
	 * 
	 * @return AccountType	account type object consisting the default values
	 */
	private AccountType getDefaultDealerSaleRepForContractSave() throws TelusAPIException{
		//read the flag from ldap to see if we want to get default values or Dealer of Record values (rollback strategy)
		boolean defaultDealerSalesRepInd = AppConfiguration.getDefaultDealerSalesRepInd();
		String defaultSubType = "R";
		
		if(defaultDealerSalesRepInd){
			AccountTypeInfo accountType = null;
			Account account = subscriber.getAccount();
			
			try {
				accountType = (AccountTypeInfo) provider.getReferenceDataManager().getAccountType(account);
				
				if(isDefaultDealerSalesRepNull(accountType.getDefaultDealer(), accountType.getDefaultSalesCode())){
					accountType = (AccountTypeInfo) provider.getReferenceDataManager().getAccountType(String.valueOf(account.getAccountType()) + defaultSubType, account.getBrandId());
				}
				
			} catch (TelusAPIException e) {
				Logger.debug0("unable to retrieve default dealer/salesrepcode values for Account type: "+account.getAccountType() + " ,subtype: " 
								+ account.getAccountSubType() + " and BrandID: " + account.getBrandId() + "For Subscriber: " + subscriber.getSubscriberId());
				throw e;
			}
			return accountType;
		}
		return null;
	}
	
	//Given the default dealerCode/salesRepCode this method returns if either of them is null
	private boolean isDefaultDealerSalesRepNull(String defaultDealerCode, String defaultSalesCode){
		if (defaultDealerCode == null || defaultDealerCode.equals("")
				|| defaultSalesCode == null || defaultSalesCode.equals("")){
			return true;
		}
		return false;
	}

	public boolean getNotificationDisplayableInd() {
		
		return delegate.getNotificationDisplayableInd();
	}
	
	private ContractService[] getPPSBundleServices() throws TelusAPIException {
		ContractService[] optionalServices = getOptionalServices();
		ArrayList ppsBundles = new ArrayList();
		for (int i =0; i < optionalServices.length; i++) {
			if (optionalServices[i].getService().isPPSBundle())
				ppsBundles.add(optionalServices[i])	;
		}
		return (ContractService[])ppsBundles.toArray(new ContractService[ppsBundles.size()]);
	}
	
	private ContractService[] getPPSAddOnServices() throws TelusAPIException {
		ContractService[] optionalServices = getOptionalServices();
		ArrayList ppsAddOns = new ArrayList();
		for (int i =0; i < optionalServices.length; i++) {
			if (optionalServices[i].getService().isPPSAddOn())
				ppsAddOns.add(optionalServices[i])	;
		}
		return (ContractService[])ppsAddOns.toArray(new ContractService[ppsAddOns.size()]);
	}
	
	/**
	 * This method works as a proxy for service addition after PricePlan change ONLY.
	 * When the PricePlan changed - all services from an old contract are deleted
	 * and some are usually added back depending on user's request. Therefore, in this method only 
	 * duration service marked as DELETED are expected. All others will be causing exception suggesting
	 * to use addDurationService interface for adding XHOUR SOCs.
	 * 
	 * @param service
	 * @return
	 * @throws TelusAPIException
	 */
	public ContractService addService(ContractService service) throws TelusAPIException {
		if(!isDurationService(service.getService())) {
			return addService(service.getCode());
		} else if(((TMContractService)service).getDelegate().getTransaction() == BaseAgreementInfo.DELETE) {
			((TMContractService)service).getDelegate().setTransaction(BaseAgreementInfo.NO_CHG, true, false);
			return service;
		} else {
			throw new TelusAPIException("Contract Service with code " + service.getCode() + " doesn't belong to a contract. Use addDurationService method instead.");
		}
	}
	

	/**
	 * [March -2021] , removed the all x-hour logic which is not in use but keeping the method without any implementation to avoid issue on consumer side
	 * Dont implement this method for future code refactor in Rest API code stack.
	 */
	public ContractService[] addDurationServices(Service service, Calendar effectiveDate, int numberOfReplications) throws TelusAPIException {
		if(!this.isDurationService(service)) {
			throw new TelusAPIException("Attempt using a duration servie addition interface for non-duration service with code " + service.getCode());
		}
		List listResult = new ArrayList();
		ContractService[] arrayResult = new ContractService[listResult.size()];
		return arrayResult;
	}
	
	/**
	 * [March -2021] , removed the all x-hour logic which is not in use but keeping the method interface without any implementation to avoid issues on consumer side
	 * Dont implement this method for future code refactor in Rest API code stack.
	 */
	public void testDurationServicesAddition(Service service, Calendar effectiveDate, int numberOfReplications) throws TelusAPIException {
		if(!this.isDurationService(service)) {
			throw new TelusAPIException("Attempt using a duration servie addition interface for non-duration service with code " + service.getCode());
		}
		
	}
	
	/**
	 * Returns true if the service is durational, i.e. its durationServiceHours 
	 * attribute is greater than 0.
	 *  
	 * @param service
	 * @return
	 */
	private boolean isDurationService(Service service) {
		return service != null && service.getDurationServiceHours() > 0;
	}
	
	/**
	 * Removes any type of service from the contract, including durational, if requested.
	 */
	public void removeService(ContractService service)
			throws UnknownObjectException, InvalidServiceChangeException,
			TelusAPIException {
		if(service == null) {
			throw new TelusAPIException("Invalid input parameters for removeService(ContractService) method");
		}
		removeService(false, service.getServiceMappingCode());
	}

	/**
	 * Removes durational service from the contract. If service is not durational, this method will not remove it
	 * properly because the service will be indexed by its Code only, and not by Code + effectiveDate. The mapping
	 * by Code + EffectiveDate is only used for durational services.
	 * To remove a regular (not durational services), use this.removeService(serviceCode) instead.
	 */
	public void removeService(String serviceCode, Date effectiveDate)
			throws UnknownObjectException, InvalidServiceChangeException,
			TelusAPIException {
		if(serviceCode == null || effectiveDate == null) {
			throw new TelusAPIException("Invalid input parameters for removeService(String, Date) method");
		}
		removeService(false, ClientApiUtils.getContractServiceMappingKey(serviceCode, effectiveDate));
	}

	/**
	 * Returns a list of services whose mapping key starts with the prefix
	 */
	public ContractService[] getServices(String prefix) throws UnknownObjectException {
		return delegate.getServices(prefix);
	}
}
