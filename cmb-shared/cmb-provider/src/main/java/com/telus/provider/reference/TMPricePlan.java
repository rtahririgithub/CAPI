/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.reference;

import java.util.ArrayList;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.equipment.CellularDigitalEquipment;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.equipment.SIMCardEquipment;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceSet;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceSetInfo;
import com.telus.provider.equipment.TMEquipment;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.Logger;

public class TMPricePlan
extends TMPricePlanSummary implements PricePlan {
	/**
	 * @link aggregation
	 */
	private final PricePlanInfo delegate;
	private ServiceSet[] mandatoryServiceSets = null;
	private Service[] cachedOptionalServices = null;
	private Service[] cachedTelephonyServices = null;
	private Service[] cachedDispatchServices = null;
	private Service[] cachedWirelessWebServices = null;

	public TMPricePlan(TMReferenceDataManager referenceDataManager,
			PricePlanInfo delegate) {
		super(referenceDataManager, delegate);
		this.delegate = delegate;
	}

	private long lastCacheTime;

	public long getLastCacheTime() {
		return lastCacheTime;
	} 

	public void setLastCacheTime(long lastCacheTime) {
		this.lastCacheTime = lastCacheTime;
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	public int getOptionalServiceCount() {
		return delegate.getOptionalServiceCount();
	}

	public Service[] getOptionalServices() {
		if (cachedOptionalServices == null) {
			cachedOptionalServices = referenceDataManager.decorate(delegate.getOptionalServices());
		}
		return cachedOptionalServices;
	}

	public Service[] getTelephonyServices() {
		if (cachedTelephonyServices == null) {
			cachedTelephonyServices = referenceDataManager.decorate(delegate.getTelephonyServices());
		}
		return cachedTelephonyServices;
	}

	public Service[] getDispatchServices() {
		if (cachedDispatchServices == null) {
			cachedDispatchServices = referenceDataManager.decorate(delegate.getDispatchServices());
		}
		return cachedDispatchServices;
	}

	public Service[] getWirelessWebServices() {
		if (cachedWirelessWebServices == null) {
			cachedWirelessWebServices = referenceDataManager.decorate(delegate.getWirelessWebServices());
		}
		return cachedWirelessWebServices;
	}

	public Service getOptionalService(String code) throws UnknownObjectException {
		return referenceDataManager.decorate(delegate.getOptionalService(code));
	}

	public Service getService(String code) throws UnknownObjectException {
		return referenceDataManager.decorate(delegate.getService(code));
	}

	public ServiceSet[] getMandatoryServiceSets(String equipmentType, String networkType) throws TelusAPIException {

		if (this.isRIM() || this.isPDA()) {
			return new ServiceSet[] {};
		}

		List serviceSet = new ArrayList();
		ServiceSet sSet = null;
		if (equipmentType.equals(Equipment.EQUIPMENT_TYPE_RIM)) {
			sSet = getMandatoryServiceSet(ServiceSet.CODE_RIM, networkType);
		} else if (equipmentType.equals(Equipment.EQUIPMENT_TYPE_PDA)) {
			sSet = getMandatoryServiceSet(ServiceSet.CODE_PDA, networkType);
		}

		if (sSet != null) {
			serviceSet.add(sSet);
		}

		return (ServiceSet[]) serviceSet.toArray(new ServiceSet[serviceSet.size()]);
	}

	public ServiceSet[] getMandatoryServiceSets(Equipment equipment) throws
	TelusAPIException {

		if (this.isRIM() || this.isPDA()) {
			return new ServiceSet[] {};
		}

		List serviceSet = new ArrayList();
		ServiceSet sSet = null;
		String networkType = getNetworkType(equipment);

		if (equipment.isRIM()) {
			sSet = getMandatoryServiceSet(ServiceSet.CODE_RIM, networkType);
		}
		else if (equipment.isSIMCard()) {
			MuleEquipment mule = ( (SIMCardEquipment) equipment).getLastMule();
			if (mule != null && mule.isIDENRIM()) {
				sSet = getMandatoryServiceSet(ServiceSet.CODE_RIM, networkType);
			}
		}
		else if (equipment.isCellularDigital() && ((CellularDigitalEquipment)equipment).isPDA()) {
			sSet = getMandatoryServiceSet(ServiceSet.CODE_PDA, networkType);
		}

		if (sSet != null) {
			serviceSet.add(sSet);
		}

		return (ServiceSet[]) serviceSet.toArray(new ServiceSet[serviceSet.size()]);
	}

	public ServiceSet[] getMandatoryServiceSets(Equipment equipment, String overrideEquipmentType) throws TelusAPIException {

		if (equipment.isUSIMCard() && !this.isRIM() && !this.isPDA()) {
			ServiceSet sSet = null;
			List serviceSet = new ArrayList();
			String codeSet = "";
			if (Equipment.EQUIPMENT_TYPE_PDA.equals(overrideEquipmentType)) {
				codeSet = ServiceSet.CODE_PDA;
			}else if (Equipment.EQUIPMENT_TYPE_RIM.equals(overrideEquipmentType)) {
				codeSet = ServiceSet.CODE_RIM;
			}else if (overrideEquipmentType == null || "".equals(overrideEquipmentType)){
				throw new TelusAPIException ("getMandatoryServiceSets: overrideEquipmentType cannot be null or empty.");
			}

			if (!"".equals(codeSet)) {
				sSet = getMandatoryServiceSet(codeSet, getNetworkType(equipment));
				if (sSet != null) {
					serviceSet.add(sSet);
				}
			}
			return (ServiceSet[]) serviceSet.toArray(new ServiceSet[serviceSet.size()]);
		} else {
			return getMandatoryServiceSets(equipment);
		}

	}


	private ServiceSet getMandatoryServiceSet(String serviceSetCode, String networkType) throws
	TelusAPIException {
		ServiceSet[] serviceSets = getMandatoryServiceSets0(networkType);

		for (int i = 0; i < serviceSets.length; i++) {
			if (serviceSets[i].getCode().equals(serviceSetCode)) {
				return serviceSets[i];
			}
		}

		return null;
	}

	public ServiceSet[] getMandatoryServiceSets() throws TelusAPIException {
		return getMandatoryServiceSets0(NetworkType.NETWORK_TYPE_ALL);
	}

	private ServiceSet[] getMandatoryServiceSets0(String networkType) {
		if (this.isRIM() || this.isPDA()) {
			return new ServiceSet[] {};
		}

		List ssList = new ArrayList();
		List rimServiceList = new ArrayList();
		List pdaServiceList = new ArrayList();

		Service[] services = getOptionalServices();

		for (int i = 0; i < services.length; i++) {
			if (services[i].isCompatible(networkType)) {
				if (services[i].isRIM() && services[i].isMandatory()) {
					rimServiceList.add(services[i]);
				} else if (services[i].isPDA() && services[i].isMandatory()) {
					pdaServiceList.add(services[i]);
				}
			}
		}

		ServiceSetInfo ss = null;

		if (rimServiceList.size() > 0) {
			ss = new ServiceSetInfo();
			ss.setCode(ServiceSet.CODE_RIM);
			ss.setDescription("RIM Mandatory Services");
			ss.setDescriptionFrench("Services obligatoires liés à l’appareil RIM");
			ss.setServices((Service[]) rimServiceList.toArray(new Service[rimServiceList.size()]));

			ssList.add(ss);
		}

		if (pdaServiceList.size() > 0) {
			ss = new ServiceSetInfo();
			ss.setCode(ServiceSet.CODE_PDA);
			ss.setDescription("PDA Mandatory Services");
			ss.setDescriptionFrench("Services obligatoires liés à l’appareil PDA");
			ss.setServices((Service[]) pdaServiceList.toArray(new Service[pdaServiceList.size()]));

			ssList.add(ss);
		}

		mandatoryServiceSets = referenceDataManager.decorate((ServiceSet[]) ssList.toArray(new ServiceSet[ssList.size()]));

		return mandatoryServiceSets;
	}

	public boolean containsOptionalService(String code) {
		return delegate.containsOptionalService(code);
	}

	public boolean waiveActivationFee() {
		return delegate.waiveActivationFee();
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public String toString() {
		return delegate.toString();
	}

	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------

	public Service[] getOptionalServices(Equipment equipment) {
		//when filtering optional service by Equipment, we want to filter by both equipment's networkType and equipmentType
		String equipmentType = equipment.getEquipmentType();

		/* PROD00187920 fix, move the logic to getOptionalServices(Equipment equipment, String overrideEquipmentType) to avoid duplication. 
		 * 
		if ( equipmentType.equals(Equipment.EQUIPMENT_TYPE_USIM) ) { 
			//use default the equipment type for USIMCard, as in KB, SOC is never associated to equipment type 'U' 
			equipmentType = DEFAULT_HSPA_EQUIPMENT_TYPE;
		}
		*/

		return getOptionalServices (equipment, equipmentType);
	}
	private Service[] getOptionalServices(Equipment equipment, String overrideEquipmentType) {
		// TODO: use better algorithm--this one sucks--it won't remove any dual
		// resource service.
	  
		//PROD00187920 fix begin,  if overrideEquipmentType is null, use equipment object's type
		if (overrideEquipmentType==null ) {
			overrideEquipmentType = equipment.getEquipmentType();
		}
	    
		if ( overrideEquipmentType.equals(Equipment.EQUIPMENT_TYPE_USIM) ) {
			overrideEquipmentType = AppConfiguration.getDefaultHSPAEquipmentType();
		}
		//PROD00187920 fix end

		if (equipment.isSIMCard()) {
			try {
				((SIMCardEquipment) equipment).getLastMule();
			} catch (TelusAPIException e) {
				System.err.println(">>>> Mule Equipment is null for "+equipment.getSerialNumber());
			}
		}

		Service[] services = getOptionalServices();
		try {
			if (!equipment.isHSPA()) {

				if (!equipment.isDispatchEnabled()) {
					services = (Service[]) ReferenceDataManager.Helper.removeDispatchOnly(services);
				}

				if (!equipment.isTelephonyEnabled()) {
					services = (Service[]) ReferenceDataManager.Helper.removeTelephonyOnly(services);
				}

				if (!equipment.isWirelessWebEnabled()) {
					services = (Service[]) ReferenceDataManager.Helper.removeWirelessWebOnly(services);
				}
				/******** Updated for Combo plan CR- Anitha Duraisamy - start ********/

				services = ReferenceDataManager.Helper.retainServicesForPreHSPA(services, equipment);

			}else { //HSPA
				services = ReferenceDataManager.Helper.retainServicesForHSPA(services, equipment);

			}
		}catch (Throwable t) {
			Logger.debug(t);
		}
		/******** Updated for Combo plan CR- Anitha Duraisamy - end ********/

		services = ReferenceDataManager.Helper.retainServicesByNetworkAndEquipmentType(services, getNetworkType(equipment),  overrideEquipmentType );
		return services;
	}

	public Service[] getOptionalServices (Equipment equipment, boolean includePrepaidServices) throws TelusAPIException {

		return getOptionalServices( equipment, includePrepaidServices, null );
	}

	public Service[] getOptionalServices(Equipment equipment, boolean includePrepaidServices, String overrideEquipmentType) throws TelusAPIException {

		Service[] services = null;
		if (equipment.isHSPA())
			services = getOptionalServices(equipment, overrideEquipmentType );
		else
			services = getOptionalServices(equipment);

		if (includePrepaidServices) {
			//add post-paid services first.
			List wholeList = arrayToList(services);
			Service[] servicesPrepaid = getWPSServices(equipment);

			String equipmentType = overrideEquipmentType;
			if ( equipmentType==null) equipmentType = equipment.getEquipmentType();

			//now filter the pre-paid service: check if services' network/equipment configuration matches the equipment's network/equipmentType attribute
			if (servicesPrepaid != null && servicesPrepaid.length > 0) {
				for (int i = 0; i < servicesPrepaid.length; i++) {
					if ( servicesPrepaid[i].isCompatible(equipment.getNetworkType(), equipmentType)) {
						wholeList.add(servicesPrepaid[i]);
					}
				}
			}
			services = (Service[])wholeList.toArray(new Service[wholeList.size()]);
		}
		return services;
	}

	private Service[] getWPSServices(Equipment equipment) {
		Service[] services = null;

		try {
			services = referenceDataManager.getWPSServices();
		} catch (TelusAPIException ex) {
			Logger.debug(ex);
		}

		if (equipment.isCellularDigital()) {
			if (((CellularDigitalEquipment)equipment).isEvDOCapable()) {
				;//OK, we're good.
			} else {
				if (services != null && services.length > 0) {
					services = (Service[])ReferenceDataManager.Helper.removeEvDOServices(services);
				}
			}
		}

		return services;
	}

	/**
	 * @deprecated
	 * @see getPricePlanFamily (String, String, String, boolean, int)
	 */
	public PricePlanSummary[] getPricePlanFamily(String provinceCode,
			String equipmentType,
			boolean currentPlansOnly,
			int termInMonths) throws TelusAPIException {
		return getPricePlanFamily0 (provinceCode, equipmentType, NetworkType.NETWORK_TYPE_ALL, currentPlansOnly, termInMonths);
	}	

	public PricePlanSummary[] getPricePlanFamily(String provinceCode,
			String equipmentType,
			String networkType,
			boolean currentPlansOnly,
			int termInMonths) throws
			TelusAPIException {
		return getPricePlanFamily0 (provinceCode, equipmentType, networkType, currentPlansOnly, termInMonths);
	}

	private PricePlanSummary[] getPricePlanFamily0 (String provinceCode,
			String equipmentType,
			String networkType,
			boolean currentPlansOnly,
			int termInMonths) throws TelusAPIException {
		String[] pricePlansFamilyCodes = new String[0];
		PricePlanSummary[] pricePlansFamily = new PricePlanSummary[0];
		int i;
		try {
			pricePlansFamilyCodes = referenceDataManager.getReferenceDataHelperEJB().
			retrieveServiceFamily(this.getCode(),
					ReferenceDataManager.PRICE_PLAN_FAMILY_TYPE,
					provinceCode, equipmentType, networkType, currentPlansOnly,
					termInMonths);
			pricePlansFamily = new PricePlanSummary[pricePlansFamilyCodes.length];
			for (i = 0; i < pricePlansFamilyCodes.length; i++) {
				pricePlansFamily[i] = referenceDataManager.getPricePlan(
						pricePlansFamilyCodes[i]);
			}
		}
		catch (Throwable e) {
			throw new TelusAPIException(e);
		}
		return pricePlansFamily;
	}

	/**
	 * 
	 * @deprecated
	 * @see com.telus.api.reference.PricePlan#getIncludedPromotions(java.lang.String, java.lang.String, int)
	 */
	public Service[] getIncludedPromotions(String equipmentType, String provinceCode, int termInMonths) throws TelusAPIException {
		return getIncludedPromotions(NetworkType.NETWORK_TYPE_ALL, equipmentType, provinceCode, termInMonths);
	}
	 
	public Service[] getIncludedPromotions(String networkType, String equipmentType, String provinceCode, int termInMonths) throws TelusAPIException {
		Service[] services = new Service[0];
		try {
			services = referenceDataManager.decorate(referenceDataManager.getReferenceDataFacade().getIncludedPromotions(this.getCode(), equipmentType, networkType, provinceCode, termInMonths));
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
		return services;

	}

	private List arrayToList(Object[] array) {
		List list = new ArrayList();
		if (array == null || array.length == 0) {
			return list;
		}

		for (int i=0; i<array.length; i++) {
			list.add(array[i]);
		}

		return list;
	}

	public boolean isAssociatedService(Service service) throws TelusAPIException {
		Service[] services = getOptionalServices();
		for(int i = 0; i < services.length; i++){
			if(services[i].getCode().endsWith(service.getCode())){
				return true;
			}
		}
		return ((TMReferenceDataManager)referenceDataManager).isServiceAssociatedToPricePlan(getCode(), service.getCode());
	}

	public Service[] getIncludedPromotions(Equipment equipment, String provinceCode, int termInMonths) throws TelusAPIException {

		Service[] services = getIncludedPromotions(getNetworkType(equipment), equipment.getEquipmentType(), provinceCode, termInMonths);
		// use ReferenceDataManager.Helper.retainServices() to filter all unrelated SOC's
		return ReferenceDataManager.Helper.retainServices(services, equipment);
	}

	/**
	 * Helper method to return proper network type of equipment
	 * @param equipment
	 * @return
	 */
	private String getNetworkType(Equipment equipment)  {
		String networkType = NetworkType.NETWORK_TYPE_ALL;;

		try {
			networkType = ((TMEquipment) equipment).getNetworkType();
		}catch (ClassCastException e) {
			try {
				networkType = ((EquipmentInfo) equipment).getNetworkType();
			}catch (ClassCastException ex1) {
				Logger.debug("Unknown class:"+equipment.getClass());
			}catch (TelusAPIException ex) {
				Logger.debug ("getNetworkType:"+ex);
			}
		}catch (TelusAPIException e) {
			Logger.debug ("getNetworkType:"+e);
		}

		return networkType;
	}

}
