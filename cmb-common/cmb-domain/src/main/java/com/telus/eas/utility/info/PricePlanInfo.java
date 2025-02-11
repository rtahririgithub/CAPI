
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.eas.utility.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.Brand;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.reference.SeatType;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceSet;
import com.telus.api.reference.ShareablePricePlan;
import com.telus.eas.framework.info.Info;


public class PricePlanInfo extends ServiceInfo implements ShareablePricePlan {

	public static final long serialVersionUID = 7320592405684748689L;

	private Map includedServices = new HashMap();
	private Map optionalServices = new HashMap();
	private int includedMinutesCount;
	private boolean availableForActivation = true;
	private boolean availableForChange = true;
	private boolean availableForChangeByDealer = true;
	private boolean availableForChangeByClient = true;
	private boolean availableToModifyByDealer = true;
	private boolean availableToModifyByClient = true ;
	private boolean availableForCorporateRenewal = true;
	private boolean availableForNonCorporateRenewal = true;
	private int maximumSubscriberCount;
	private String secondarySubscriberService;
	private boolean availableForCorporateStoreActivation = true;
	private boolean availableForRetailStoreActivation = true;
	private boolean waiveActivationFee = false;
	private ServiceSet[] mandatoryServiceSets;
	private boolean suspensionPricePlan;
	private boolean minutePoolingCapable; 
	private boolean dollarPoolingCapable; 
	private boolean EW;
	private boolean XEW;
	private boolean PDA;
	private boolean aomPricePlan;
	private int[] availableTermsInMonths;
	private String seatType;
	private Boolean selectedOffer;
	private String comboGroupId;

	public PricePlanInfo() {
	}

	public ServiceInfo[] getIncludedServices0() {
		return (ServiceInfo[])includedServices.values().toArray(
				new ServiceInfo[includedServices.size()]);
	}

	public Service[] getIncludedServices() {
		return (Service[])includedServices.values().toArray(
				new Service[includedServices.size()]);
	}

	public void setIncludedServices(ServiceInfo[] newIncludedServices) {
		includedServices.clear();
		for (int i = 0; i < newIncludedServices.length; i++) {
			includedServices.put(newIncludedServices[i].getCode(),newIncludedServices[i]);
		}
	}

	public void setOptionalServices(ServiceInfo[] newOptionalServices) {
		optionalServices.clear();
		for (int i = 0; i < newOptionalServices.length; i++) {
			optionalServices.put(newOptionalServices[i].getCode(),newOptionalServices[i]);
		}
	}

	public Service[] getOptionalServices() {
		return (Service[])optionalServices.values().toArray(
				new Service[optionalServices.size()]);
	}

	public Service[] getOptionalServices (Equipment equipment) {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public Service[] getOptionalServices (Equipment equipment, boolean includePrepaidServices) {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public ServiceInfo[] getOptionalServices0() {
		return (ServiceInfo[])optionalServices.values().toArray(
				new ServiceInfo[optionalServices.size()]);
	}
	
	public Service[] getOptionalServices(Equipment equipment, boolean includePrepaidServices, String overrideEquipmentType) {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public int getIncludedServiceCount() {
		return includedServices.size();
	}

	public int getOptionalServiceCount() {
		return optionalServices.size();
	}

	public Service getOptionalService(String code) throws UnknownObjectException {
		return getOptionalService0(code);
	}

	public ServiceInfo getOptionalService0(String code) throws UnknownObjectException {
		code = Info.padTo(code, ' ', 9);
		//System.err.println("getOptionalService0("+code+")");
		ServiceInfo info = (ServiceInfo)optionalServices.get(code);
		if (info != null) {
			return info;
		}
		//return null;
		throw new UnknownObjectException("Optional Service code=[" + code + "] is not associated with Price Plan code=[" + getCode() + "]");
	}

	public Service getIncludedService(String code) throws UnknownObjectException {
		return getIncludedService0(code);
	}

	public ServiceInfo getIncludedService0(String code) throws UnknownObjectException {
		code = Info.padTo(code, ' ', 9);
		//System.err.println("getIncludedService0("+code+")");

		ServiceInfo info = (ServiceInfo)includedServices.get(code);
		if (info != null) {
			return info;
		}
		//return null;
		throw new UnknownObjectException("Included Service code=[" + code + "] is not associated with Price Plan code=[" + getCode() + "]");
	}

	public Service getService(String code) throws UnknownObjectException {
		return getService1(code);
	}

	public ServiceInfo getService0(String code) throws UnknownObjectException {
		ServiceInfo info = null;

		try{
			info = getIncludedService0(code);
		} catch(UnknownObjectException e) {
			try{
				info = getOptionalService0(code);
			} catch(UnknownObjectException e2) {
				// returns null
			}
		}

		return info;
	}

	public ServiceInfo getService1(String code) throws UnknownObjectException {
		ServiceInfo info = null;

		try{
			info = getIncludedService0(code);
		} catch(UnknownObjectException e) {
			info = getOptionalService0(code);
		}

		return info;
	}

	public ServiceSet[] getMandatoryServiceSets()  {
		return mandatoryServiceSets;
	}

	public ServiceSet[] getMandatoryServiceSets(Equipment equipment) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	public ServiceSet[] getMandatoryServiceSets(String equipmentType, String networkType) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	public ServiceSet[] getMandatoryServiceSets(Equipment equipment, String overrideEquipmentType) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public void setMandatoryServiceSets(ServiceSetInfo[] mandatoryServiceSets) throws TelusAPIException {
		this.mandatoryServiceSets = mandatoryServiceSets;
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer();

		s.append("PricePlanInfo:{\n");
		s.append(super.toString()).append("\n");
		s.append("    availableForActivation=[").append(availableForActivation).append("]\n");
		s.append("    availableForChange=[").append(availableForChange).append("]\n");
		s.append("    availableForChangeByDealer=[").append(availableForChangeByDealer).append("]\n");
		s.append("    availableForChangeByClient=[").append(availableForChangeByClient).append("]\n");
		s.append("    availableToModifyByDealer=[").append(availableToModifyByDealer).append("]\n");
		s.append("    availableToModifyByClient=[").append(availableToModifyByClient).append("]\n");
		s.append("    availableForCorporateRenewal=[").append(availableForCorporateRenewal).append("]\n");
		s.append("    availableForNonCorporateRenewal=[").append(availableForNonCorporateRenewal).append("]\n");

		s.append("    maximumSubscriberCount=[").append(maximumSubscriberCount).append("]\n");
		s.append("    secondarySubscriberService=[").append(secondarySubscriberService).append("]\n");
		s.append("    minutePoolingCapable=[").append(minutePoolingCapable).append("]\n");
		//s.append("    includedServices=[").append(includedServices).append("]\n");
		if (includedServices == null || includedServices.size() == 0) {
			s.append("    includedServices={}\n");
		} else {
			Iterator i = includedServices.values().iterator();
			while (i.hasNext()) {
				s.append("    includedServices[" + i + "]=[").append((ServiceInfo) i.next()).append("]\n");
			}
		}
		//s.append("    optionalServices=[").append(optionalServices).append("]\n");
		if (optionalServices == null || optionalServices.size() == 0) {
			s.append("    optionalServices={}\n");
		} else {
			Iterator i = optionalServices.values().iterator();
			while (i.hasNext()) {
				s.append("    optionalServices[" + i + "]=[").append((ServiceInfo) i.next()).append("]\n");
			}
		}
		//s.append("    service=[").append(service).append("]\n");
		s.append("    EW=[").append(EW).append("]\n");
		s.append("    XEW=[").append(XEW).append("]\n");
		s.append("    PDA=[").append(PDA).append("]\n");
		s.append("    seatType=[").append(seatType).append("]\n");
		s.append("    selectedOffer=[").append(selectedOffer).append("]\n");
		s.append("}");

		return s.toString();
	}

	public Service[] getTelephonyServices() {
		Service[] services = this.getOptionalServices();

		services = (Service[])ReferenceDataManager.Helper.removeDispatchOnly(services);
		//services = (Service[])ReferenceDataManager.Helper.removeTelephonyOnly(services);
		services = (Service[])ReferenceDataManager.Helper.removeWirelessWebOnly(services);

		return services;
	}

	public Service[] getDispatchServices() {
		Service[] services = this.getOptionalServices();

		//services = (Service[])ReferenceDataManager.Helper.removeDispatchOnly(services);
		services = (Service[])ReferenceDataManager.Helper.removeTelephonyOnly(services);
		services = (Service[])ReferenceDataManager.Helper.removeWirelessWebOnly(services);

		return services;
	}

	public Service[] getWirelessWebServices() {
		Service[] services = this.getOptionalServices();

		services = (Service[])ReferenceDataManager.Helper.removeDispatchOnly(services);
		services = (Service[])ReferenceDataManager.Helper.removeTelephonyOnly(services);
		//services = (Service[])ReferenceDataManager.Helper.removeWirelessWebOnly(services);

		return services;
	}

	public PricePlan getPricePlan(String equipmentType, String provinceCode, char accountType, char accountSubType){
		//throw new UnsupportedOperationException("Method not implemented here");
		// This is a hack to allow the API to return the Info or Provider object interchangebly.
		return this;
	}

	public boolean containsIncludedService(String code) {
		code = Info.padTo(code, ' ', 9);
		return includedServices.containsKey(code);
	}

	public boolean containsOptionalService(String code) {
		code = Info.padTo(code, ' ', 9);
		return optionalServices.containsKey(code);
	}

	public boolean containsService(String code) {
		return containsIncludedService(code) || containsOptionalService(code);
	}

	public void addIncludedService(ServiceInfo newIncludedService) {
		includedServices.put(newIncludedService.getCode(),newIncludedService);
	}

	public void addOptionalService(ServiceInfo newOptionalService) {
		optionalServices.put(newOptionalService.getCode(), newOptionalService);
	}

	public void removeIncludedService(ServiceInfo includedService) {
		includedServices.remove(includedService.getCode());
	}

	public void removeOptionalService(ServiceInfo optionalService) {
		optionalServices.remove(optionalService.getCode());
	}
	
	public void setIncludedMinutesCount(int includedMinutesCount) {
		this.includedMinutesCount = includedMinutesCount;
	}
	
	public int getIncludedMinutesCount() {
		return includedMinutesCount;
	}

	public boolean isAvailableForChangeByDealer(){
		return availableForChangeByDealer;
	}

	public boolean isAvailableForChangeByClient() {
		return availableForChangeByClient;
	}

	public boolean isAvailableToModifyByDealer() {
		return availableToModifyByDealer;
	}

	public boolean isAvailableToModifyByClient() {
		return availableToModifyByClient;
	}

	public void setAvailableForChangeByDealer(boolean availableForChangeByDealer) {
		this.availableForChangeByDealer = availableForChangeByDealer;
	}

	public void setAvailableForChangeByClient(boolean availableForChangeByClient) {
		this.availableForChangeByClient = availableForChangeByClient;
	}

	public void setAvailableToModifyByDealer(boolean availableToModifyByDealer) {
		this.availableToModifyByDealer = availableToModifyByDealer;
	}

	public void setAvailableToModifyByClient(boolean availableToModifyByClient) {
		this.availableToModifyByClient = availableToModifyByClient;
	}

	public void setAvailableForActivation(boolean newAvailableForActivation) {
		availableForActivation = newAvailableForActivation;
	}

	public boolean isAvailableForActivation() {
		return availableForActivation;
	}

	public void setAvailableForChange(boolean newAvailableForChange) {
		availableForChange = newAvailableForChange;
	}

	public boolean isAvailableForChange() {
		return availableForChange;
	}
	
	public void setAvailableForCorporateRenewal(boolean availableForCorporateRenewal) {
		this.availableForCorporateRenewal = availableForCorporateRenewal;
	}
	
	public boolean isAvailableForCorporateRenewal() {
		return availableForCorporateRenewal;
	}
	
	public void setAvailableForNonCorporateRenewal(boolean availableForNonCorporateRenewal) {
		this.availableForNonCorporateRenewal = availableForNonCorporateRenewal;
	}
	
	public boolean isAvailableForNonCorporateRenewal() {
		return availableForNonCorporateRenewal;
	}

	public int getMaximumSubscriberCount() {
		return maximumSubscriberCount;
	}

	public void setMaximumSubscriberCount(int maximumSubscriberCount) {
		this.maximumSubscriberCount = maximumSubscriberCount;
	}

	public String getSecondarySubscriberService() {
		return secondarySubscriberService;
	}

	public void setSecondarySubscriberService(String secondarySubscriberService) {
		this.secondarySubscriberService = secondarySubscriberService;
	}

	public PricePlanSummary[] getPricePlanFamily(String provinceCode, String equipmentType, boolean currentPlansOnly, int termInMonths)	{ 
		throw new java.lang.UnsupportedOperationException("Method not implemented.");
	}
	
	public PricePlanSummary[] getPricePlanFamily(String provinceCode, String equipmentType, String networkType, boolean currentPlansOnly, int termInMonths)	{
		throw new java.lang.UnsupportedOperationException("Method not implemented.");
	}

	public void setAvailableTermsInMonths(int[] availableTermsInMonths) {
		this.availableTermsInMonths = availableTermsInMonths;
	}

	public int[] getAvailableTermsInMonths() {
		return availableTermsInMonths;
	}

	public Service[] getIncludedPromotions(String equpmentType, String provinceCode, int termInMonths) throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method not implemented here.");
	}
	
	public Service[] getIncludedPromotions(String networkType, String equpmentType, String provinceCode, int termInMonths) throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method not implemented here.");
	}	
	
	/**
	 * Returns all service containing the given feature.
	 *
	 */
	public ServiceInfo[] getServicesByFeature(String featureCode) {
		featureCode = Info.padFeature(featureCode);

		ServiceInfo[] includedServices = getIncludedServices0();
		ServiceInfo[] optionalServices = getOptionalServices0();

		List list = new ArrayList(includedServices.length + optionalServices.length);

		for(int i=0; i < includedServices.length; i++) {
			if(includedServices[i].containsFeature(featureCode)) {
				list.add(includedServices[i]);
			}
		}

		for(int i=0; i < optionalServices.length; i++) {
			if(optionalServices[i].containsFeature(featureCode)) {
				list.add(optionalServices[i]);
			}
		}

		return (ServiceInfo[])list.toArray(new ServiceInfo[list.size()]);
	}
	
	public void setAvailableForCorporateStoreActivation(boolean availableForCorporateStoreActivation) {
		this.availableForCorporateStoreActivation = availableForCorporateStoreActivation;
	}
	
	public boolean isAvailableForCorporateStoreActivation() {
		return availableForCorporateStoreActivation;
	}
	
	public void setAvailableForRetailStoreActivation(boolean availableForRetailStoreActivation) {
		this.availableForRetailStoreActivation = availableForRetailStoreActivation;
	}
	
	public boolean isAvailableForRetailStoreActivation() {
		return availableForRetailStoreActivation;
	}
	
	public void setWaiveActivationFee(boolean waiveActivationFee) {
		this.waiveActivationFee = waiveActivationFee;
	}
	
	public boolean waiveActivationFee() {
		return waiveActivationFee;
	}
	
	public boolean isSuspensionPricePlan() {
		return suspensionPricePlan;
	}
	
	public void setSuspensionPricePlan(boolean suspensionPricePlan) {
		this.suspensionPricePlan = suspensionPricePlan;
	}
	
	/**
	 * @return Returns the eW.
	 */
	public boolean isEW() {
		return EW;
	}
	
	/**
	 * @param ew The eW to set.
	 */
	public void setEW(boolean ew) {
		EW = ew;
	}
	
	/**
	 * @return Returns the xEW.
	 */
	public boolean isXEW() {
		return XEW;
	}
	
	/**
	 * @param xew The xEW to set.
	 */
	public void setXEW(boolean xew) {
		XEW = xew;
	}
	
	/**
	 * @return Returns the pDA.
	 */
	public boolean isPDA() {
		return PDA;
	}
	
	/**
	 * @param pda The pDA to set.
	 */
	public void setPDA(boolean pda) {
		PDA = pda;
	}

	public boolean isAssociatedService(Service service) throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method not implemented here.");
	}

	public Service[] getIncludedPromotions(Equipment equipment, String provinceCode, int termInMonths) throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method not implemented here.");
	}

	public boolean isMinutePoolingCapable() {
		return minutePoolingCapable;
	}

	public void setMinutePoolingCapable(boolean minutePoolingCapable) {
		this.minutePoolingCapable = minutePoolingCapable;
	}

	public boolean isZeroIncludedMinutes() {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Deprecated
	public boolean isAmpd() {
		return (getBrandId() == Brand.BRAND_ID_AMPD); 
	}

	public void setAOMPricePlan(boolean aomPricePlan) {
		this.aomPricePlan = aomPricePlan;
	}

	public boolean isAOMPricePlan() {
		return aomPricePlan;
	}

	public boolean isDollarPoolingCapable() {
		return dollarPoolingCapable;
	}

	public void setDollarPoolingCapable(boolean dollarPoolingCapable) {
		this.dollarPoolingCapable = dollarPoolingCapable;
	}
	
	private static final String FAMILY_GROUP_TYPE_RIM = "R";
	private static final String FAMILY_GROUP_TYPE_PTT = "T";
	private static final String FAMILY_GROUP_TYPE_EW = "N";
	private static final String FAMILY_GROUP_TYPE_XEW = "O";
	private static final String FAMILY_GROUP_TYPE_PDA = "D";
	private static final String FAMILY_GROUP_TYPE_EMAIL_N_WEB = "M";
	
	protected void populateFamilyTypeDerivedProperties(String familyType) {		
		if (FAMILY_GROUP_TYPE_RIM.equals(familyType)) {
			setRIM(true);
			setEmailAndWebspaceIncluded(true);
		} else if (FAMILY_GROUP_TYPE_PTT.equals(familyType)){
			setPTT(true);
		} else if (FAMILY_GROUP_TYPE_EW.equals(familyType)){
			setEW(true);
		} else if (FAMILY_GROUP_TYPE_XEW.equals(familyType)){
			setXEW(true);
		} else if (FAMILY_GROUP_TYPE_PDA.equals(familyType)){
			setPDA(true);
			setEmailAndWebspaceIncluded(true);			
		} else if (FAMILY_GROUP_TYPE_EMAIL_N_WEB.equals(familyType)){
			setEmailAndWebspaceIncluded(true);
		}
	}
	
	public String getSeatType() {
		return seatType;
	}
	
	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public Boolean isSelectedOffer() {
		return selectedOffer;
	}

	public void setSelectedOffer(Boolean selectedOffer) {
		this.selectedOffer = selectedOffer;
	}

	@Deprecated
	@Override
	public boolean isFidoPricePlan() {
		return false;
	}

	public void setComboGroupId(String comboGroupId) {
		this.comboGroupId = comboGroupId;
	}

	@Override
	/**
	 * Returns true if the SOC.COMBO_GROUP_ID is not null.
	 */
	public boolean isComboPlan() {
		return comboGroupId != null;
	}
	
}