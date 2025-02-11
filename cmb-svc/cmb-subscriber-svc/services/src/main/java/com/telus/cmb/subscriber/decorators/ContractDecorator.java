package com.telus.cmb.subscriber.decorators;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.api.InvalidCardChangeException;
import com.telus.api.InvalidServiceChangeException;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.CallingFeatureCycle;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.EquipmentChangeRequest;
import com.telus.api.account.PhoneNumberException;
import com.telus.api.account.PricePlanValidation;
import com.telus.api.equipment.Card;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.Service;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.task.ContractChangeTask;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.utility.info.RatedFeatureInfo;
import com.telus.eas.utility.info.ServiceInfo;

public abstract class ContractDecorator {
	protected final SubscriberContractInfo delegate;
	
	public ContractDecorator (SubscriberContractInfo contract) {
		delegate = contract;
	}
	
	public SubscriberContractInfo getDelegate() {
		return delegate;
	}
	
	
	public abstract PricePlanDecorator getPricePlan();

	
	public abstract ServiceAgreementInfo addService(ServiceInfo service) throws InvalidServiceChangeException, TelusAPIException;

	
	public abstract ServiceAgreementInfo addService(String serviceCode) throws InvalidServiceChangeException, ApplicationException, TelusAPIException;

	
	public abstract ServiceAgreementInfo addService(ServiceInfo service, Date effectiveDate, Date expiryDate) throws InvalidServiceChangeException, TelusAPIException, ApplicationException;

	
	public abstract void removeService(String serviceCode) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException, SystemException, ApplicationException;

	
	public int getServiceCount() {
		return delegate.getServiceCount();
	}

	
	public ContractFeature[] getFeatures() {
		return delegate.getFeatures();
	}

	
	public ContractFeature[] getFeatures(boolean includeServices) {
		return delegate.getFeatures(includeServices);
	}

	
	public ServiceAgreementInfo[] getServices() {
		return delegate.getServices0(false);
	}

	
	public ServiceAgreementInfo[] getIncludedServices() {
		return delegate.getIncludedServices0(false);
	}

	
	public ServiceAgreementInfo[] getOptionalServices() {
		return delegate.getOptionalServices0(false);
	}

	
	public ServiceAgreementInfo[] getAddedServices() {
		return (ServiceAgreementInfo[]) delegate.getAddedServices();
	}

	
	public ServiceAgreementInfo[] getChangedServices() {
		return (ServiceAgreementInfo[]) delegate.getChangedServices();
	}

	
	public ServiceAgreementInfo[] getDeletedServices() {
		return (ServiceAgreementInfo[]) delegate.getDeletedServices();
	}

	
	public ContractFeature[] getAddedFeatures() {
		return delegate.getAddedFeatures();
	}

	
	public ContractFeature[] getChangedFeatures() {
		return delegate.getChangedFeatures();
	}

	
	public ContractFeature[] getDeletedFeatures() {
		return delegate.getDeletedFeatures();
	}

	
	public boolean getCascadeShareableServiceChanges() {
		return delegate.getCascadeShareableServiceChanges();
	}

	
	public void setCascadeShareableServiceChanges(boolean cascadeShareableServiceChanges) {
		delegate.setCascadeShareableServiceChanges(cascadeShareableServiceChanges);
	}

	
	public ServiceAgreementInfo getService(String code) throws UnknownObjectException {
		return (ServiceAgreementInfo) delegate.getService(code);
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

	
	public double getRecurringChargeForShareableServices() throws TelusAPIException {
		return delegate.getRecurringChargeForShareableServices();
	}

	
	public double getRecurringCharge() throws TelusAPIException {
		return delegate.getRecurringCharge();
	}

	
	public boolean isTelephonyEnabled() {
		return delegate.isTelephonyEnabled();
	}

	
	public boolean isDispatchEnabled() throws TelusAPIException {
		return delegate.isDispatchEnabled();
	}

	
	public boolean isWirelessWebEnabled() throws TelusAPIException {
		return delegate.isWirelessWebEnabled();
	}

	
	public void save() throws InvalidServiceChangeException, TelusAPIException, PhoneNumberException {
		delegate.save();
	}

	
	public void save(String dealerCode, String salesRepCode) throws ApplicationException {
		delegate.save(dealerCode, salesRepCode);
	}

	
	public void refresh() throws TelusAPIException {
		delegate.refresh();
	}

	
	public Service testAddition(Service service) throws InvalidServiceChangeException, TelusAPIException {
		return delegate.testAddition(service);
	}

	
	public Service testAddition(Service service, Date effectiveDate) throws InvalidServiceChangeException, TelusAPIException {
		return delegate.testAddition(service, effectiveDate);
	}

	
	public abstract Service[] testAddition(ServiceInfo[] service) throws InvalidServiceChangeException, TelusAPIException, ApplicationException;

	
	public Service[] testAddition(Card card, boolean autoRenew) throws InvalidCardChangeException, TelusAPIException {
		return delegate.testAddition(card, autoRenew);
	}

	
	public abstract ServiceAgreementInfo testRemoval(ServiceAgreementInfo contractService) throws InvalidServiceChangeException, TelusAPIException;

	
	public abstract ServiceAgreementInfo[] addCard(Card card);

	
	public abstract ServiceAgreementInfo[] addCard(Card card, boolean autoRenew);

	
	public int getCommitmentMonths() {
		return delegate.getCommitmentMonths();
	}

	
	public void setCommitmentMonths(int commitmentMonths) {
		delegate.setCommitmentMonths(commitmentMonths);

	}

	
	public Date getCommitmentStartDate() {
		return delegate.getCommitmentStartDate();
	}

	
	public void setCommitmentStartDate(Date commitmentStartDate) {
		delegate.setCommitmentStartDate(commitmentStartDate);
	}

	
	public Date getCommitmentEndDate() {
		return delegate.getCommitmentEndDate();
	}

	
	public String getCommitmentReasonCode() {
		return delegate.getCommitmentReasonCode();
	}

	
	public void setCommitmentReasonCode(String commitmentReasonCode) {
		delegate.setCommitmentReasonCode(commitmentReasonCode);
	}

	
	public boolean isCrossFleetRestricted() {
		return delegate.isCrossFleetRestricted();
	}

	
	public EquipmentChangeRequest getEquipmentChangeRequest() {
		return delegate.getEquipmentChangeRequest();
	}

	
	public void setEquipmentChangeRequest(EquipmentChangeRequest equipmentChangeRequest) throws TelusAPIException {
		delegate.setEquipmentChangeRequest(equipmentChangeRequest);
	}

	
	public boolean isSuppressPricePlanRecurringCharge() throws TelusAPIException {
		return delegate.isSuppressPricePlanRecurringCharge();
	}

	
	public void removeFeature(String featureCode) throws UnknownObjectException {
		delegate.removeFeature(featureCode);
	}

	
	public boolean isPTTServiceIncluded() {
		return delegate.isPTTServiceIncluded();
	}

	
	public boolean containsService(String code) {
		return delegate.containsService(code);
	}

	
	public boolean isShareablePricePlanPrimary() throws TelusAPIException {
		return delegate.isShareablePricePlanPrimary();
	}

	
	public boolean isShareablePricePlanSecondary() throws TelusAPIException {
		return delegate.isShareablePricePlanSecondary();
	}

	
	public boolean isShareable() throws TelusAPIException {
		return delegate.isShareable();
	}

	
	public boolean isDollarPooling() throws TelusAPIException {
		return delegate.isDollarPooling();
	}

	
	public ContractChangeTask[] getCascadingContractChanges() {
		return delegate.getCascadingContractChanges();
	}

	
	public boolean isModified() {
		return delegate.isModified();
	}

	
	public ServiceFeatureInfo addFeature(RatedFeatureInfo feature) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException {
		return delegate.addFeature(feature);
	}

	
	public boolean containsPricePlanFeature(String featureCode) {
		return delegate.containsPricePlanFeature(featureCode);
	}

	
	public String[] getMultiRingPhoneNumbers() {
		return delegate.getMultiRingPhoneNumbers();
	}

	
	public void setMultiRingPhoneNumbers(String[] phoneNumbers) {
		delegate.setMultiRingPhoneNumbers(phoneNumbers);
	}

	
	public abstract ServiceAgreementInfo[] get911Services();

	
	public double get911Charges() {
		return delegate.get911Charges();
	}

	
	public boolean isAirtimePoolingEnabled() {
		return delegate.isAirtimePoolingEnabled();
	}

	
	public boolean isLDPoolingEnabled() {
		return delegate.isLDPoolingEnabled();
	}

	
	public boolean isPoolingEnabled(int poolingGroupId) throws TelusAPIException {
		return delegate.isPoolingEnabled(poolingGroupId);
	}

	
	public PricePlanValidation getPricePlanValidation() {
		return delegate.getPricePlanValidation();
	}

	
	public CallingFeatureCycle calculatePrepaidFeatureCycleDates() throws TelusAPIException {
		return delegate.calculatePrepaidFeatureCycleDates();
	}

	
	public void save(String dealerCode, String salesRepCode, ServiceRequestHeader header) throws ApplicationException {
		delegate.save(dealerCode, salesRepCode, header);
	}

	
	public Service[] getServicesRestrictedByNetworkType(Equipment targetEquipment) throws TelusAPIException {
		return delegate.getServicesRestrictedByNetworkType(targetEquipment);
	}

}
