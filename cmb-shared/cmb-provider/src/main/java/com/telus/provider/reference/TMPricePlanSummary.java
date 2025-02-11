/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.reference;

import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.SeatType;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServicePeriod;
import com.telus.api.reference.ServicePeriodIncludedMinutes;
import com.telus.api.reference.ServicePeriodType;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.PricePlanTermInfo;
import com.telus.provider.util.Logger;


public class TMPricePlanSummary extends TMService implements PricePlanSummary {
	
	/**
	 * @link aggregation
	 */
	private final PricePlanInfo delegate;
	private Service[] cachedIncludedServices;

	public TMPricePlanSummary(TMReferenceDataManager referenceDataManager, PricePlanInfo delegate) {
		super(referenceDataManager, delegate);
		this.delegate = delegate;
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	public int getIncludedServiceCount() {
		return delegate.getIncludedServiceCount();
	}

	public Service[] getIncludedServices() {
		if (cachedIncludedServices == null) {
			cachedIncludedServices = referenceDataManager.decorate(delegate.getIncludedServices());
		}
		
		return cachedIncludedServices;
	}

	public Service getIncludedService(String code) throws UnknownObjectException {
		return referenceDataManager.decorate(delegate.getIncludedService(code));
	}

	public boolean containsIncludedService(String code) {
		return delegate.containsIncludedService(code);
	}

	public boolean containsService(String code) {
		return delegate.containsService(code);
	}

	public int getUsageRatingFrequency(){
		return delegate.getUsageRatingFrequency();
	}

	public int getIncludedMinutesCount() {
		return delegate.getIncludedMinutesCount();
	}

	public boolean isAvailableForActivation() {
		return delegate.isAvailableForActivation();
	}

	public boolean isAvailableForChange() {
		return delegate.isAvailableForChange();
	}

	public boolean isAvailableForChangeByDealer(){
		return delegate.isAvailableForChangeByDealer();
	}

	public boolean isAvailableForChangeByClient(){
		return delegate.isAvailableForChangeByClient();
	}

	public boolean isAvailableToModifyByDealer(){
		return delegate.isAvailableToModifyByDealer();
	}

	public boolean isAvailableToModifyByClient(){
		return delegate.isAvailableToModifyByClient();
	}

	public boolean isAvailableForNonCorporateRenewal(){
		return delegate.isAvailableForNonCorporateRenewal();
	}

	public boolean isAvailableForCorporateRenewal(){
		return delegate.isAvailableForCorporateRenewal();
	}

	public boolean isAvailableForCorporateStoreActivation(){
		return delegate.isAvailableForCorporateStoreActivation();
	}

	public boolean isAvailableForRetailStoreActivation(){
		return delegate.isAvailableForRetailStoreActivation();
	}

	public boolean isSuspensionPricePlan() {
		return delegate.isSuspensionPricePlan();
	}

	public boolean isEW() {
		return delegate.isEW();
	}

	public boolean isXEW() {
		return delegate.isXEW();
	}

	public boolean isMinutePoolingCapable(){
		return delegate.isMinutePoolingCapable();
	}

	public boolean isAmpd() {
		return delegate.isAmpd();
	}

	public boolean isAOMPricePlan() {
		return delegate.isAOMPricePlan();
	}
	
	public boolean isDollarPoolingCapable(){
		return delegate.isDollarPoolingCapable();
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
	public PricePlanInfo getDelegate0() {
		return delegate;
	}

	public PricePlan getPricePlan(String equipmentType, String provinceCode, char accountType, char accountSubType)
	throws TelusAPIException {
		return referenceDataManager.getPricePlan(delegate.getCode(), equipmentType, provinceCode, accountType, accountSubType, getBrandId());
	}

	public int[] getAvailableTermsInMonths() throws TelusAPIException {

		int[] availableTermsInMonths = new int[0];

		//M.Liao: May 31, 2010. Defect PROD00170573 fix: underlying ReferenceDataFacade no longer return a object if the data is not in the cache.
		// so need to check if the return is null before using it.
		PricePlanTermInfo termInfo = referenceDataManager.getPricePlanTermInfo(this.getCode());
		if ( termInfo!=null)
			availableTermsInMonths = termInfo.getTermsInMonths();

		if (availableTermsInMonths.length ==0)
		{availableTermsInMonths = new int[1];
		availableTermsInMonths[0] = this.getTermMonths();
		}
		return availableTermsInMonths;
	}

	public boolean isFidoPricePlan() {
		return false;
	}

	public boolean isZeroIncludedMinutes(){
		try {
			TMServiceSummary serviceSummaryProvider = new TMServiceSummary(referenceDataManager, delegate);

			ServicePeriod[ ] sp = serviceSummaryProvider.getServicePeriods();
			if (sp == null) return false;
			for (int i = 0; i < sp.length; i++ ){
				if (sp[i].getCode().trim().equals(ServicePeriodType.COMBINED)){
					ServicePeriodIncludedMinutes[] spim = sp[i].getServicePeriodIncludedMinutes();
					if (spim == null) return false;
					for (int j = 0; j < spim.length; j++ ){
						if (spim[j].getDirection().trim().equals(ServicePeriod.DIRECTION_BOTH)){
							if (spim[j].getIncludedMinutes() == 0 ) return true;
						}
					}
					break;
				}
			}
		} catch (TelusAPIException e){
			Logger.debug(e);
		}

		return false;
	}

	public String[] getFamilyTypes() {
		return delegate.getFamilyTypes();
	}

	public String getSeatType() {
		return delegate.getSeatType();
	}

	@Override
	public boolean isComboPlan() {
		return delegate.isComboPlan();
	}

}