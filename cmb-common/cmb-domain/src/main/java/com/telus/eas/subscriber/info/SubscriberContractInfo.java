package com.telus.eas.subscriber.info;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.telus.api.InvalidCardChangeException;
import com.telus.api.InvalidServiceChangeException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.CallingFeatureCycle;
import com.telus.api.account.Contract;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.account.EquipmentChangeRequest;
import com.telus.api.account.PricePlanValidation;
import com.telus.api.equipment.Card;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.RatedFeature;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServicePromotion;
import com.telus.api.reference.ServiceSummary;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.task.ContractChangeTask;
import com.telus.api.util.ClientApiUtils;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.framework.info.PublicCloneable;
import com.telus.eas.utility.info.FeatureInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.RatedFeatureInfo;
import com.telus.eas.utility.info.ServiceInfo;



/**
 * Title:        Telus - Amdocs Domain Beans
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Telus Mobility
 * @author Michael Lapish
 * @version 1.0
 */

public class SubscriberContractInfo extends BaseAgreementInfo implements Contract {
	
	static final long serialVersionUID = 1L;

  /**
   * @link aggregation
   */
  private HashMap services = new HashMap();


  /**
   * @link aggregation
   */
  private HashMap features = new HashMap();

  private PricePlanInfo pricePlan;
  private transient boolean modified;
  private boolean dispatchOnly;
  private String[] multiRingPhoneNumbers;
  private MultiRingInfo[] multiRingInfos;
  
  //private PricePlanValidation pricePlanValidation; //no in use, no way to initiate it. 
  private PricePlanValidationInfo pricePlanValidationInfo;
  private String pricePlanDealerCode;
  private String pricePlanSalesRepId;
  
  // Attribute Service Type, stores service type associated with with Price Plan code – 
  // this will prevent additional retrieval of price plan reference data for light weight calls which 
  // only require price plan code and associated service type.
  private String pricePlanServiceType;
  
  //CDR confirmation notification - cascade this information from Provider to EJB
  private boolean pricePlanChange;

  // Added this flag to detect contract renewal events ( kafka)
  private boolean contractRenewal;

  /**
   *@link aggregation
   */
  private CommitmentInfo commitment = new CommitmentInfo();
  //private ContractService[] includedServices;
  //private ContractService[] optionalServices;
  
  public SubscriberContractInfo() {
  }

  public void setFeatures(Map pricePlanFeatures) {
    features = (HashMap)pricePlanFeatures;
    setThisAsParent(features);
  }

  public void setServices(Map pricePlanServices) {
    services = (HashMap)pricePlanServices;
    setThisAsParent(services);
  }

  public void setPricePlanInfo(PricePlanInfo pricePlan) {
    this.pricePlan = pricePlan;
    setPricePlan(pricePlan.getCode());
  }

  public PricePlan getPricePlan() {
    return pricePlan;
  }

  public PricePlanInfo getPricePlan0() {
    return pricePlan;
  }

  public void setPricePlan(String newPricePlanCode) {
    setCode(newPricePlanCode, 9);
  }

  public String getPricePlanCode() {
    return getCode();
  }

  public double getRecurringChargeForShareableServices() throws TelusAPIException {
    throw new UnsupportedOperationException("Method is not implemented in server class.");
  }

  public double getRecurringCharge() {
    throw new UnsupportedOperationException("Method is not implemented in server class.");
  }

//  public double getRecurringCharge(boolean suppressPricePlanRecurringCharge) {
//    if(pricePlan == null) {
//      throw new NullPointerException("pricePlan == null, use the API");
//    }
//
//    double charge = (suppressPricePlanRecurringCharge)?0.0:pricePlan.getRecurringCharge();
//
//    Iterator i = services.values().iterator();
//    while(i.hasNext()) {
//      ServiceAgreementInfo info = (ServiceAgreementInfo)i.next();
//      if (info.getTransaction() != DELETE && !(info.getService0().getRecurringChargeFrequency() != ServiceSummary.PAYMENT_FREQUENCY_MONTH)) {
//        charge += info.getService0().getRecurringCharge(); // TODO: handle null ServiceInfo.
//      }
//    }
//
//    return charge;
//  }

  public ContractService addService(Service service) {
    throw new UnsupportedOperationException("Method is not implemented in server class.");
  }

  public ContractService addService(String serviceCode) throws UnknownObjectException, TelusAPIException {
    throw new UnsupportedOperationException("Method is not implemented in server class.");
  }

  public ContractService addService(Service service, Date effectiveDate, Date expiryDate) throws UnknownObjectException, TelusAPIException {
    throw new UnsupportedOperationException("Method is not implemented in server class.");
  }


  public ServiceAgreementInfo addService(ServiceInfo service, Date effectiveDate) {
    return addService(service, effectiveDate, false);
  }
  
  private String findService(ServiceInfo service, Date effectiveDate, boolean addFeatures){
	    Collection infos = services.values();
	    Iterator itr = infos.iterator();
	    while(service.getDurationServiceHours() == 0 && itr.hasNext()) {
	    	ServiceAgreementInfo element = (ServiceAgreementInfo)itr.next();
	        if(service.getCode().trim().equals(element.getCode().trim())){
	        	return ClientApiUtils.getContractServiceMappingKey(service.getCode(), element.getEffectiveDate());
	        }
	     }
	  return  ClientApiUtils.getContractServiceMappingKey(service.getCode(), effectiveDate);
  }

  public ServiceAgreementInfo addService(ServiceInfo service, Date effectiveDate, boolean addFeatures) {
    if(service == null) {
      throw new IllegalArgumentException("service is null");
    }

    String serviceMappingCode = findService(service, effectiveDate, addFeatures);//  ClientApiUtils.getContractServiceMappingKey(service.getCode(), effectiveDate);
    if(services.containsKey(serviceMappingCode)) {
      ServiceAgreementInfo sa = (ServiceAgreementInfo)services.get(serviceMappingCode);
      if(sa.getTransaction() == BaseAgreementInfo.DELETE) {
        sa.setTransaction(BaseAgreementInfo.NO_CHG);
      }
      return sa;
    }

    ServiceAgreementInfo sa = new ServiceAgreementInfo(service);
    services.put(serviceMappingCode, sa);
    sa.setParent(this);


    if(addFeatures) {
      RatedFeatureInfo[] includedFeatures = service.getFeatures0();
      for(int i=0; i < includedFeatures.length; i++) {
        sa.addFeature(includedFeatures[i]);
      }
    }

    modified = true;

    return sa;
  }

  public ServiceAgreementInfo addService(ServiceAgreementInfo service) {
	  String serviceMappingCode = ClientApiUtils.getContractServiceMappingKey(service);
	  services.put(serviceMappingCode, service);
	  service.setParent(this);
	  return service;
  }

  public void removeService(String serviceCode) throws UnknownObjectException {
    if(removeService0(serviceCode) == null) {
      throw new UnknownObjectException("Service does not exist", serviceCode);
    }
  }

  public ServiceAgreementInfo removeService0(String serviceCode) {
    serviceCode = Info.padService(serviceCode);
    return (ServiceAgreementInfo)removeChildOrNull(services, serviceCode);
  }

  public ServiceAgreementInfo undoRemoveService0(String serviceCode) {
    serviceCode = Info.padService(serviceCode);
    return (ServiceAgreementInfo)undoRemoveChildOrNull(services, serviceCode);
  }

  public int getServiceCount() {
    return getServiceCount0(false);
  }

  public int getServiceCount0(boolean includeDeleted) {
    return getChildCount(services, includeDeleted);
  }

  public boolean containsFeature0(String code, Date effectiveDate, Date expiryDate, boolean searchServices, boolean includeDeleted) {
    code = Info.padFeature(code);

    if(containsChild(features, code, effectiveDate, expiryDate, includeDeleted)) {
      return true;
    }

    if(searchServices && getServiceByFeature0(code, effectiveDate, expiryDate, includeDeleted) != null) {
      return true;
    }

    return false;
  }

//  public boolean containsFeature(String code, Date effectiveDate, Date expiryDate) {
//    return containsFeature(code, effectiveDate, expiryDate, false);
//  }

//  public boolean containsFeature(String code) {
//    return containsFeature(code, null, null, false);
//  }


  /**
   * Returns the first service containing the given feature or null.
   *
   */
  public ServiceAgreementInfo getServiceByFeature0(String featureCode, Date effectiveDate, Date expiryDate, boolean includeDeleted) {
    featureCode = Info.padFeature(featureCode);

    ServiceAgreementInfo[] info = getServices0(includeDeleted);
    for(int i=0; i < info.length; i++) {
      if(info[i].containsFeature(featureCode, effectiveDate, expiryDate, includeDeleted)) {
        return info[i];
      }
    }
    return null;
  }

  /**
   * Returns all service containing the given feature.
   *
   */
  public ServiceAgreementInfo[] getServicesByFeature0(String featureCode, Date effectiveDate, Date expiryDate, boolean includeDeleted) {
    featureCode = Info.padFeature(featureCode);

    ServiceAgreementInfo[] info = getServices0(includeDeleted);
    List list = new ArrayList(info.length);
    for(int i=0; i < info.length; i++) {
      if(info[i].containsFeature(featureCode, effectiveDate, expiryDate, includeDeleted)) {
        list.add(info[i]);
      }
    }
    return (ServiceAgreementInfo[])list.toArray(new ServiceAgreementInfo[list.size()]);
  }

  public ServiceFeatureInfo addFeature(RatedFeatureInfo feature) {
    if(feature == null) {
      throw new IllegalArgumentException("feature is null");
    }
    if(features.containsKey(feature.getCode())) {
      ServiceFeatureInfo sa = (ServiceFeatureInfo)features.get(feature.getCode());
      if(sa.getTransaction() == BaseAgreementInfo.DELETE) {
        sa.setTransaction(BaseAgreementInfo.NO_CHG);
      }
      return sa;
    }
    ServiceFeatureInfo sa = new ServiceFeatureInfo(feature);

    features.put(feature.getCode(), sa);
    sa.setParent(this);
    modified = true;

    return sa;
  }

  public ContractFeature addFeature(RatedFeature feature) {
    return addFeature((RatedFeatureInfo)feature);
  }

  public void removeFeature(String featureCode) throws UnknownObjectException {
//    featureCode = Info.padFeature(featureCode);
//    removeChild(features, featureCode);

    if(removeFeature0(featureCode) == null) {
      throw new UnknownObjectException("Feature does not exist", featureCode);
    }

  }

  public ServiceFeatureInfo removeFeature0(String featureCode) {
    featureCode = Info.padFeature(featureCode);
    return (ServiceFeatureInfo)removeChildOrNull(features, featureCode);
  }

  public ContractFeature[] getFeatures() {
    return getFeatures0(false);
  }

  public ContractFeature[] getFeatures(boolean includeServices) {
    return getFeatures0(false, includeServices);
  }

  public ServiceFeatureInfo[] getFeatures0(boolean includeDeleted) {
//    return (ServiceFeatureInfo[])getChildren(features, ServiceFeatureInfo.class, includeDeleted);
    return getFeatures0(includeDeleted, false);
  }



  public ServiceFeatureInfo[] getFeatures0(boolean includeDeleted, boolean includeServices) {
    List list = getChildren(features, includeDeleted, new ArrayList());

    if (includeServices) {
      ServiceAgreementInfo[] infos = getServices0(includeDeleted);

      for(int i = 0; i < infos.length; i++) {
        infos[i].getFeatures0(includeDeleted, list);
      }
    }

    return (ServiceFeatureInfo[])list.toArray(new ServiceFeatureInfo[list.size()]);
  }

  public String[] getServiceCodes0(boolean includeDeleted) {
    ServiceAgreementInfo[] infos = getServices0(includeDeleted);
    String[] codes = new String[infos.length];

    for(int i = 0; i < infos.length; i++) {
      codes[i] = infos[i].getServiceCode();
    }

    return codes;
  }

  public ContractService[] getServices() {
    return getServices0(false);
  }
  
  public ServiceAgreementInfo[] getServices0(boolean includeDeleted) {
    return (ServiceAgreementInfo[])getChildren(services, ServiceAgreementInfo.class, includeDeleted);
  }

  public ServiceAgreementInfo[] getModifiedServices() throws TelusAPIException {
    return (ServiceAgreementInfo[])getModifiedChildren(services, ServiceAgreementInfo.class);
  }

  //public boolean containsService(String code, Date effectiveDate, Date expiryDate) {
  public boolean containsService0(String code, boolean includeDeleted) {
    code = Info.padService(code);
    // TODO: respect effectiveDate & expiryDate when they're not null.
    return getChildOrNull(services, code, includeDeleted) != null;
  }

  public boolean containsService(String code) {
    return containsService0(code, false);
  }

  public ContractService getService(String code) throws UnknownObjectException {
    ServiceAgreementInfo o = getService0(code, false);

    if(o == null) {
      throw new UnknownObjectException("code=[" + code + "]");
    }

    return o;
  }

//  public ServiceAgreementInfo getService0(String code) throws UnknownObjectException {
//    ServiceAgreementInfo o = getService1(code);
//
//    if(o == null) {
//      throw new UnknownObjectException("code=[" + code + "]");
//    }
//
//    return o;
//  }
//
//  public ServiceAgreementInfo getService1(String code) {
//    code = Info.padService(code);
//    return (ServiceAgreementInfo)services.get(code);
//  }

  public ServiceAgreementInfo getService0(String code, boolean includeDeleted) {
    return (ServiceAgreementInfo)getChildOrNull(services, code, includeDeleted);
  }

  public void setExpiryDate(Date expiryDate) {
    super.setExpiryDate(expiryDate);
    modified = true;
  }

  public void setEffectiveDate(Date effectiveDate) {
    super.setEffectiveDate(effectiveDate);
    modified = true;
  }

  public void setModified() {
    modified = true;
  }

  public boolean isModified() {
    return modified || isModified(this, services) || isModified(this, features);
  }

  public void commit() {
    modified = false;
    commit(this, services);
    commit(this, features);
  }

  public ServiceAgreementInfo[] getIncludedServices0(boolean includeDeleted) {
    if(pricePlan == null) {
      throw new NullPointerException("pricePlan == null, use the API");
    }

    ServiceAgreementInfo[] services = getServices0(includeDeleted);
    List list = new ArrayList(services.length);
    for(int i = 0; i < services.length; i++) {
      ServiceAgreementInfo s = services[i];
      if(pricePlan.containsIncludedService(s.getServiceCode())) {
        list.add(s);
      }
    }

    return (ServiceAgreementInfo[]) list.toArray(
        new ServiceAgreementInfo[list.size()]);
  }

  public ContractService[] getIncludedServices() {
    return (ContractService[])getIncludedServices0(false);
  }

  public ServiceAgreementInfo[] getOptionalServices0(boolean includeDeleted) {
    if(pricePlan == null) {
      throw new NullPointerException("pricePlan == null, use the API");
    }

    ServiceAgreementInfo[] services = getServices0(includeDeleted);
    List list = new ArrayList(services.length);
    for(int i = 0; i < services.length; i++) {
      ServiceAgreementInfo s = services[i];
      if(!pricePlan.containsIncludedService(s.getServiceCode())) {
        list.add(s);
      }
    }

    return (ServiceAgreementInfo[]) list.toArray(
        new ServiceAgreementInfo[list.size()]);
  }

  public ContractService[] getOptionalServices() {
    return getOptionalServices0(false);
  }

  public ServiceAgreementInfo[] getOptionalAndIncludedPromotionalServices(boolean includeDeleted) throws TelusAPIException {
    if(pricePlan == null) {
      throw new NullPointerException("pricePlan == null, use the API");
    }

    ServiceAgreementInfo[] services = getServices0(includeDeleted);
    List list = new ArrayList(services.length);
    for(int i = 0; i < services.length; i++) {
      ServiceAgreementInfo s = services[i];
      // we assume it's optional if it's not included
      if(!pricePlan.containsIncludedService(s.getServiceCode()) || pricePlan.getService0(s.getServiceCode()).isIncludedPromotion()) {
        list.add(s);
      }
    }

    return (ServiceAgreementInfo[]) list.toArray(
        new ServiceAgreementInfo[list.size()]);
  }


  public boolean isTelephonyEnabled() {
	  return !containsService0(ServiceSummary.BLOCK_INCOMING_CALLS_IDEN, false) &&
		!containsService0(ServiceSummary.BLOCK_OUTGOING_CALLS_IDEN, false);
  }

  public boolean isDispatchEnabled() throws TelusAPIException {
    throw new UnsupportedOperationException("Method is not implemented in server class.");
  }

  public boolean isWirelessWebEnabled() throws TelusAPIException {
    throw new UnsupportedOperationException("Method is not implemented in server class.");
  }

  public ContractChangeTask[] getCascadingContractChanges() {
    throw new UnsupportedOperationException("Method is not implemented in server class.");
  }

  public void save() throws TelusAPIException {
    throw new UnsupportedOperationException("Method is not implemented in server class.");
  }

  public void save(String dealerCode, String salesRepCode) {
    throw new UnsupportedOperationException("Method is not implemented in server class.");
  }

  public void refresh() throws TelusAPIException {
    throw new UnsupportedOperationException("Method is not implemented in server class.");
  }

  public String toString() {
    StringBuffer s = new StringBuffer(128);

    s.append("SubscriberContractInfo:[\n");
    s.append("    super=[").append(super.toString()).append("]\n");
    s.append("    features=[").append(features).append("]\n");
    s.append("    pricePlan=[").append(pricePlan).append("]\n");
    s.append("    modified=[").append(modified).append("]\n");
    s.append("    dispatchOnly=[").append(dispatchOnly).append("]\n");
//    s.append("    suppressPricePlanRecurringCharge=[").append(suppressPricePlanRecurringCharge).append("]\n");



        /*
        if(includedServices == null)
        {
            s.append("    includedServices=[null]\n");
        }
        else if(includedServices.length == 0)
        {
            s.append("    includedServices={}\n");
        }
        else
        {
            for(int i=0; i<includedServices.length; i++)
            {
                s.append("    includedServices["+i+"]=[").append(includedServices[i]).append("]\n");
            }
        }
        if(optionalServices == null)
        {
            s.append("    optionalServices=[null]\n");
        }
        else if(optionalServices.length == 0)
        {
            s.append("    optionalServices={}\n");
        }
        else
        {
            for(int i=0; i<optionalServices.length; i++)
            {
                s.append("    optionalServices["+i+"]=[").append(optionalServices[i]).append("]\n");
            }
        }
        */
    s.append("]");

    return s.toString();
  }

  public boolean isDispatchOnly() {
    return dispatchOnly;
  }

  public void setDispatchOnly(boolean dispatchOnly) {
    this.dispatchOnly = dispatchOnly;
  }


  public Service testAddition(Service service) throws InvalidServiceChangeException, TelusAPIException{
    throw new UnsupportedOperationException("method not implemented here");
  }

  public Service testAddition(Service service, Date effectiveDate) throws InvalidServiceChangeException, TelusAPIException{
    throw new UnsupportedOperationException("method not implemented here");
  }


  public Service[] testAddition(Service[] service) throws InvalidServiceChangeException, TelusAPIException{
    throw new UnsupportedOperationException("method not implemented here");
  }

  public Service[] testAddition(Card card, boolean autoRenew) throws InvalidCardChangeException, TelusAPIException{
    throw new UnsupportedOperationException("method not implemented here");
  }

  public ContractService testRemoval(ContractService contractService) throws InvalidServiceChangeException, TelusAPIException {
    throw new UnsupportedOperationException("method not implemented here");
  }

  public ContractService[] addCard(Card card){
    throw new UnsupportedOperationException("method not implemented here");
  }

  public ContractService[] addCard(Card card, boolean autoRenew){
    throw new UnsupportedOperationException("method not implemented here");
  }

  public int getCommitmentMonths(){
    return commitment.getMonths();
  }

  public void setCommitmentMonths(int commitmentMonths){
    commitment.setMonths(commitmentMonths);
  }

  public Date getCommitmentStartDate(){
    return commitment.getStartDate();
  }

  public void setCommitmentStartDate(Date commitmentStartDate){
    commitment.setStartDate(commitmentStartDate);
  }

  public Date getCommitmentEndDate(){
    return commitment.getEndDate();
  }

  public void setCommitmentEndDate(Date commitmentEndDate){
    commitment.setEndDate(commitmentEndDate);
  }

  public String getCommitmentReasonCode(){
    return commitment.getReasonCode();
  }

  public void setCommitmentReasonCode(String commitmentReasonCode){
    commitment.setReasonCode(commitmentReasonCode);
  }

  public CommitmentInfo getCommitment(){
    return commitment;
  }

  public boolean isCrossFleetRestricted(){
   throw new UnsupportedOperationException("Method not implemented here");
  }

  public EquipmentChangeRequest getEquipmentChangeRequest() {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void setEquipmentChangeRequest(EquipmentChangeRequest equipmentChangeRequest) throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public boolean isSuppressPricePlanRecurringCharge() {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public boolean isPTTServiceIncluded() {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public boolean isShareablePricePlanPrimary() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public boolean isShareablePricePlanSecondary() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }


//  private boolean suppressPricePlanRecurringCharge;

  public ContractService[] getAddedServices() {
    return (ContractService[])getChildrenByTransaction(services, ADD, ServiceAgreementInfo.class);
  }

  public ContractService[] getChangedServices() {
    return (ContractService[])getChildrenByTransaction(services, UPDATE, ServiceAgreementInfo.class);
  }

  public ContractService[] getDeletedServices() {
    return (ContractService[])getChildrenByTransaction(services, DELETE, ServiceAgreementInfo.class);
  }

  public ContractFeature[] getAddedFeatures() {
    return (ContractFeature[])getChildrenByTransaction(features, ADD, ServiceFeatureInfo.class);
  }

  public ContractFeature[] getChangedFeatures() {
    return (ContractFeature[])getChildrenByTransaction(features, UPDATE, ServiceFeatureInfo.class);
  }

  public ContractFeature[] getDeletedFeatures() {
    return (ContractFeature[])getChildrenByTransaction(features, DELETE, ServiceFeatureInfo.class);
  }

  public boolean getCascadeShareableServiceChanges(){
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void setCascadeShareableServiceChanges(boolean cascadeShareableServiceChanges){
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public boolean containsPricePlanFeature(String featureCode) {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  /**
   * Return <code>ServicePromotion[]</code>
   * @param businessRole String
   * @return ServicePromotion[]
   */
  public ServicePromotion[] getServicePromotionsToAdd(String businessRole) throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  /**
   * Return <code>ServicePromotion[]</code>
   * @param businessRole String
   * @return ServicePromotion[]
   */
  public ServicePromotion[] getServicePromotionsToRemove(String businessRole) throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  /**
   * Returns MultiRingPhoneNumbers.
   */
  public String[] getMultiRingPhoneNumbers() {
    return this.multiRingPhoneNumbers;
  }

  /**
   * Set multi-ring phone numbers.
   * @param phoneNumbers String[]
   */
  public void setMultiRingPhoneNumbers(String[] phoneNumbers) {
    this.multiRingPhoneNumbers = phoneNumbers;
  }

  public MultiRingInfo[] getMultiRingInfos() {
    return multiRingInfos;
  }

  public void setMultiRingInfos(MultiRingInfo[] multiRingInfos) {
    this.multiRingInfos = multiRingInfos;
  }

  public ContractService[] get911Services() {
    throw new java.lang.UnsupportedOperationException("Method get911Services() not implemented here");
  }

  public double get911Charges() {
    throw new java.lang.UnsupportedOperationException("Method not implemented here");
  }
 
  public boolean isAirtimePoolingEnabled() {
	  throw new UnsupportedOperationException("Method not implemented here");
  }
  
  public boolean isLDPoolingEnabled() {
	  throw new UnsupportedOperationException("Method not implemented here");  
  }
  
  public boolean isPoolingEnabled(int poolingGroupId) {
	  throw new UnsupportedOperationException("Method not implemented here");
  }

  public boolean isShareable() throws TelusAPIException {			
	  throw new UnsupportedOperationException("Method not implemented here");
  }

  public boolean isDollarPooling() throws TelusAPIException {	
	  throw new UnsupportedOperationException("Method not implemented here");
  }
 
  public PricePlanValidation getPricePlanValidation(){
	 return getPricePlanValidation0();
  }
  
  public PricePlanValidationInfo getPricePlanValidation0(){
	  if (this.pricePlanValidationInfo == null)
		 this.pricePlanValidationInfo = new PricePlanValidationInfo();

	  return this.pricePlanValidationInfo;
  }
  

	private void synchronizeCallingCircleParameter(Map airtimeFeatures, List nonAirtimeFeatures) {
		// for each non airtime calling circle feature, update the parameter from the matching( by calling circle size) 
		// airtime calling circle feature
		for (int i = 0; i < nonAirtimeFeatures.size(); i++) {
			ServiceFeatureInfo nonAirtimeCallingCircle = (ServiceFeatureInfo) nonAirtimeFeatures.get(i);
			Integer size = new Integer(nonAirtimeCallingCircle.getCallingCircleSize());

			ServiceFeatureInfo airtimeCallingCircle = (ServiceFeatureInfo) airtimeFeatures.get(size);
			if (airtimeCallingCircle != null 
					&&(	airtimeCallingCircle.getTransaction()==ADD ||airtimeCallingCircle.getTransaction()==UPDATE )
					&& ( compare( nonAirtimeCallingCircle.getParameter(), airtimeCallingCircle.getParameter())==false )
					) {
				nonAirtimeCallingCircle.setParameter( airtimeCallingCircle.getParameter() );
				//CDR calling circle BR: CC list effective date need to be in sync as well
				nonAirtimeCallingCircle.setCcParameterChanged(true);
				nonAirtimeCallingCircle.setCallingCircleCommitmentAttributeData(airtimeCallingCircle.getCallingCircleCommitmentAttributeData0());
			}
		}
	}
	
	//this method iterate through the given ServiceFeatureInfo array, put airtime and non-airtime feature into two collections
	private void gatherCallingCircleFeatures( ServiceFeatureInfo[] features, Map airtimeFeatures, List nonAirtimeFeatures) {
		for (int i = 0; i < features.length; i++) {
			ServiceFeatureInfo serviceFeatureInfo = features[i];
			if (serviceFeatureInfo.isNonAirtimeCallingCircle()) {
				nonAirtimeFeatures.add(features[i]);
			}
			else if (serviceFeatureInfo.isCallingCircle()) {
				Integer size = new Integer(serviceFeatureInfo.getCallingCircleSize());
				if (airtimeFeatures.containsKey(size) == false) {
					airtimeFeatures.put(size, features[i]);
				}
			}
		}
	}
	
	private static boolean INCLUDE_DELETED = true;
	private void gatherIncludedCallingCircleFeatures(Map airtimeFeatures, List nonAirtimeFeature) {
		//price plan included feature
		gatherCallingCircleFeatures( 
				getFeatures0(INCLUDE_DELETED), 
				airtimeFeatures, nonAirtimeFeature);
		
		//price plan included SOCs 
		ServiceAgreementInfo[] includedServices = getIncludedServices0(INCLUDE_DELETED);
		for (int i = 0; i < includedServices.length; i++) {
			gatherCallingCircleFeatures(
					includedServices[i].getFeatures0(INCLUDE_DELETED), 
					airtimeFeatures, nonAirtimeFeature);
		}
	}

	private void gatherOptionalCallingCircleFeatures(Map airtimeFeatures, List nonAirtimeFeature) {
		ServiceAgreementInfo[] optionalServices = getOptionalServices0(INCLUDE_DELETED);
		for (int i = 0; i < optionalServices.length; i++) {
			if (optionalServices[i].getServiceType().equals("S")==false) {
				gatherCallingCircleFeatures(
						optionalServices[i].getFeatures0(INCLUDE_DELETED), 
						airtimeFeatures, nonAirtimeFeature);
			}
		}
	}
	
	private void synchronizeIncludedCallingCircleParameter( Map airtimeFeatures, List nonAirtimeFeature ) {

		gatherIncludedCallingCircleFeatures(airtimeFeatures, nonAirtimeFeature);
		synchronizeCallingCircleParameter(airtimeFeatures, nonAirtimeFeature);
	}

	private void synchronizeOptionalCallingCircleParameter( Map airtimeFeatures, List nonAirtimeFeature ) {
		gatherOptionalCallingCircleFeatures(airtimeFeatures, nonAirtimeFeature);
		synchronizeCallingCircleParameter( airtimeFeatures, nonAirtimeFeature );
	}

	public void synchronizeIncludedCallingCircleParameter( ) {
		synchronizeIncludedCallingCircleParameter( new HashMap(), new ArrayList() );
	}

	public void synchronizeCallingCircleParameter() {
		HashMap airtimeFeatures = new HashMap();
		ArrayList nonAirtimeFeature = new ArrayList();
		
		synchronizeIncludedCallingCircleParameter(airtimeFeatures, nonAirtimeFeature );
		
		//clear out the two collection before reuse them
		airtimeFeatures.clear(); 
		nonAirtimeFeature.clear();
		
		synchronizeOptionalCallingCircleParameter(airtimeFeatures, nonAirtimeFeature );
	}

	public CallingFeatureCycle calculatePrepaidFeatureCycleDates()  {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public void save(String dealerCode, String salesRepCode,
			ServiceRequestHeader header)  {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public Service[] getServicesRestrictedByNetworkType(
			Equipment targetEquipment) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	/**
	 * This method is intended to be used with retrieveServiceAgreementBySubscriberId only.
	 * @param dealerCode
	 */
	public void setPricePlanDealerCode(String dealerCode) {
		pricePlanDealerCode = dealerCode;
	}
	
	public String getPricePlanDealerCode() {
		return pricePlanDealerCode;
	}
	
	/**
	 * This method is intended to be used with retrieveServiceAgreementBySubscriberId only.
	 * @param salesRepId
	 */
	public void setPricePlanSalesRepId(String salesRepId) {
		pricePlanSalesRepId = salesRepId;
	}
	
	public String getPricePlanSalesRepId() {
		return pricePlanSalesRepId;
	}
	
	
	/**
	 * This method is intended to be used with pricePlanServiceType only.
	 * @param pricePlanServiceType
	 */
	public void setPricePlanServiceType(String pricePlanServiceType) {
		this.pricePlanServiceType = pricePlanServiceType;
	}

	public String getPricePlanServiceType() {
		return pricePlanServiceType;
	}

	public Object clone() {
		
		SubscriberContractInfo o = (SubscriberContractInfo) super.clone();
		
		o.services = PublicCloneable.Helper.clone(services);
		o.features = PublicCloneable.Helper.clone(features);
		if ( multiRingInfos!=null )
			o.multiRingInfos = (MultiRingInfo[])multiRingInfos.clone();
		if (multiRingPhoneNumbers!=null) 
			o.multiRingPhoneNumbers = (String[]) multiRingPhoneNumbers.clone();
		
		o.pricePlanValidationInfo = (PricePlanValidationInfo) clone( pricePlanValidationInfo );
		
		o.commitment = (CommitmentInfo) clone( commitment );
		
		return  o;
	}

	//CDR calling-circle BR changes begin
	/**
	 * Check the ServiceAgreement and return ServiceFeatureInfo whose switchCode is FeatureInfo.SWITCH_CODE_CALLING_CIRCL
	 * if icnludeCallHomeFree is true, ServiceFeatureInfo with switchCode FeatureInfo.SWITCH_CODE_CALL_HOME_FREE is also returned
	 * 
	 * When checking Prepaid subscriber's contract, includeCallHomeFree shall set to true
	 * 
	 * @param sa
	 * @param includeCallHomeFree,  
	 * @return
	 */
	public static ServiceFeatureInfo getCallingCircleFeature( ServiceAgreementInfo sa, boolean includeCallHomeFree) {
		
		ServiceFeatureInfo[] features = sa.getFeatures0(false);
		for(int i=0; i<features.length; i++ ) {
			if ( FeatureInfo.SWITCH_CODE_CALLING_CIRCLE.equalsIgnoreCase( features[i].getSwitchCode().trim() ) ) {
				return features[i];
			} else if (includeCallHomeFree && FeatureInfo.SWITCH_CODE_CALL_HOME_FREE.equalsIgnoreCase(features[i].getSwitchCode().trim() ) ) {
				return features[i];
			}
		}
		return null;
	}

	/**
	 * This method executes post load  cross reference logic among  services / features . 
	 * Shall be invoked after all service agreements have been loaded from KB and prepaid, and reference data have been set.
	 */
	public void doPostLoadProcess() {
		
		//the following is to cross reference prepaid calling-circle feature with its KB counterpart
		//and sync up the feature parameter from KB feature.
		ServiceAgreementInfo[] agreements = getOptionalServices0( false); 
		for( int i=0; i<agreements.length; i++ ) {
			ServiceAgreementInfo sa= agreements[i];
			ServiceInfo serviceInfo = sa.getService0(); 
			if ( serviceInfo.isWPS() && serviceInfo.hasCallingCircleFeatures() ) {
				
				ServiceAgreementInfo kbSocSa = getService0( serviceInfo.getWPSMappedKBSocCode(), false );
				if ( kbSocSa !=null ) { 
					ServiceFeatureInfo prepaidCCFeature = getCallingCircleFeature( sa, true );
					ServiceFeatureInfo kbSocCCFeature = getCallingCircleFeature( kbSocSa, true );
					prepaidCCFeature.syncParameter(kbSocCCFeature);
				}
			}
		}
	}

	public void prepopulateCallingCircleList() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	/**
	 * Return all 'non-delete' calling circle feature in current contract. 
	 * 
	 * @param consolidate - When this flag is set to true, if contract contain prepaid calling circle feature, then only prepaid 
	 * calling circle feature will be returned, the mapped KB feature will not returned. When it's set to false, all postpaid / prepaid
	 * calling circle features will be returned.
	 *   
	 * @return
	 */
	public List getCallingCircleFeatures(boolean consolidate ) {
		return getCallingCircleFeatures( consolidate, false );
	}
	public List getCallingCircleFeatures(boolean consolidate, boolean includeNonAirtimeCC ) {
		ArrayList result = new ArrayList();
		boolean containPrepaid = false;
		ServiceFeatureInfo[] features = getFeatures0(false, true );
		for( int i=0; i<features.length; i++ ) {
			if ( features[i].isCallingCircle() ) { 
				result.add( features[i]);
				containPrepaid |= features[i].isWPS(); 
			} else if ( includeNonAirtimeCC && features[i].isNonAirtimeCallingCircle() ) {
				result.add( features[i]);
			}
		}
		
		if ( consolidate && containPrepaid ) { //remove non prepaid feature
			Iterator it = result.iterator();
			while( it.hasNext() ) {
				ServiceFeatureInfo feature = (ServiceFeatureInfo) it.next();
				if ( feature.isWPS()==false) it.remove();
			}
		}
		
		return result;
	}
	
	/**
	 * Return all calling circle feature that contain empty calling circle list.
	 *  
	 * @return
	 */
	public ServiceFeatureInfo[] getEmptyCCListFeatures() {
		
		List list = getCallingCircleFeatures(false); 

		Iterator it = list.iterator();
		while( it.hasNext() ) {
			ServiceFeatureInfo feature = (ServiceFeatureInfo) it.next();
			
			//remove non empty CC list feature
			if ( feature.getCallingCirclePhoneNumbersFromParam().length>0) {
				it.remove();
			} else {
				//CC list is empty, 
				if  ( feature.isWPS() ) {
					//this is a prepaid CC feature, now need to double check to see if its mapped KB cc feature contain
					//empty CC list or not.
					
					ServiceAgreementInfo prepaidCCService = getService0( feature.getServiceCode(), false ); 
					ServiceAgreementInfo kbCCService = getService0( prepaidCCService.getWpsMappedKbSoc(), false );
					if ( kbCCService!=null ) { 
						//KB SOC on the contract, and this feature tell us if the prepaid feature has CC list or not 
						ServiceFeatureInfo kbCCFeature = getCallingCircleFeature( kbCCService, true );
						if ( kbCCFeature.getCallingCirclePhoneNumbersFromParam().length>0) {
							list.remove( feature );
						}
					}
				} 
			}
		}
		return (ServiceFeatureInfo[])list.toArray( new ServiceFeatureInfo[list.size()]);
	}
	
	/**
	 * Return all calling circle feature that .
	 * 
	 * @return
	 */
	public ServiceFeatureInfo[] getNullCCCommitmentDataFeatures() {
		List list = getCallingCircleFeatures( false);
		Iterator it = list.iterator();
		while( it.hasNext() ) {
			ServiceFeatureInfo feature = (ServiceFeatureInfo) it.next();
			
			if ( feature.getCallingCircleCommitmentAttributeData()!=null) { 
				it.remove();
			}
		}
		return (ServiceFeatureInfo[])list.toArray( new ServiceFeatureInfo[list.size()]);
	}

	//CDR calling-circle BR changes end
	
	private boolean isBillPresentmentEligible(Service service ) {
		boolean result = true; 
		if (service.getRecurringCharge()==0 && service.isBillingZeroChrgSuppress()==true)
			result = false;
		return result;
	}

	private boolean isNotificationDisplayable(ContractService[] contractServices, boolean isNew) throws TelusAPIException {
		for( int i=0; i<contractServices.length; i++ ) {
			Service service = contractServices[i].getService();
			if ( service!=null ) {
				if ( isBillPresentmentEligible( service ) )
					return true;
				if ( service.hasCallingCircleFeatures() )
					return true;
			}
		}
		return false;
	}
	
	public boolean getNotificationDisplayableInd() {
		try {
			if (isPricePlanChange() ) {
				if (isBillPresentmentEligible( getPricePlan()))
					return true;
				
				
				if( isNotificationDisplayable( getIncludedServices(), true ) )
					return true;
				
			}
		
			if (isNotificationDisplayable(getAddedServices(), true ) )
				return true;
			
			if( isNotificationDisplayable(getDeletedServices(), false )) 
				return true;
			
			
			List ccFeatureList = getCallingCircleFeatures(true);
			for ( int i=0; i<ccFeatureList.size(); i++ ) {
				ServiceFeatureInfo sfi  = (ServiceFeatureInfo) ccFeatureList.get(i);
				if (sfi.isCcParameterChanged()) 
					return true;
			}
		} catch( TelusAPIException e ) {
			
		}
		
		return false;
	}


	public boolean isContractRenewal() {
		return contractRenewal;
	}

	public void setContractRenewal(boolean contractRenewal) {
		this.contractRenewal = contractRenewal;
	}

	public boolean isPricePlanChange() {
		return pricePlanChange;
	}

	public void setPricePlanChange(boolean pricePlanChange) {
		this.pricePlanChange = pricePlanChange;
	}

	public ContractService[] addDurationServices(Service service,
			Calendar effectiveDate, int numberOfReplications)
			throws TelusAPIException {
		throw new RuntimeException("Not supported");
	}

	public void testDurationServicesAddition(Service service,
			Calendar effectiveDate, int numberOfReplications)
			throws InvalidServiceChangeException, TelusAPIException {
		throw new RuntimeException("Not supported");
		
	}

	public void removeService(ContractService service)
			throws UnknownObjectException, TelusAPIException {
		throw new UnsupportedOperationException("Use TMContract.removeService implementation instead");
	}

	public void removeService(String serviceCode, Date effectiveDate)
			throws UnknownObjectException, TelusAPIException {
		throw new UnsupportedOperationException("Use TMContract.removeService implementation instead");
	}

	public ContractService[] getServices(String code)
			throws UnknownObjectException {
		List foundServices = getChildren(services, code);
		return (ContractService[])foundServices.toArray(new ContractService[foundServices.size()]);
	}

	public ContractService addService(ContractService service)
			throws TelusAPIException {
		throw new UnsupportedOperationException("Use TMContract.addService implementation instead");
	}

}

