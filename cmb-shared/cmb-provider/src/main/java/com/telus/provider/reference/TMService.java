/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.reference;

import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.RatedFeature;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceSummary;
import com.telus.eas.utility.info.FeatureInfo;
import com.telus.eas.utility.info.ServiceInfo;

public class TMService extends TMServiceSummary implements Service {

	/**
	 * @link aggregation
	 */
	private final ServiceInfo delegate;

	public TMService(TMReferenceDataManager referenceDataManager, ServiceInfo delegate) {
		super(referenceDataManager, delegate);
		this.delegate = delegate;
	}

	// --------------------------------------------------------------------
	// Decorative Methods
	// --------------------------------------------------------------------
	public int getFeatureCount() {
		return delegate.getFeatureCount();
	}

	public RatedFeature[] getFeatures() {
		return delegate.getFeatures();
	}

	public boolean isParameterRequired() {
		return delegate.isParameterRequired();
	}

	public String getCoverageType() {
		return delegate.getCoverageType();
	}

	public String[] getCategoryCodes() {
		return delegate.getCategoryCodes();
	}

	public boolean isAdditionalNumberRequired() {
		return delegate.isAdditionalNumberRequired();
	}

	public boolean isRIM() {
		if (delegate.SERVICE_TYPE_CODE_PRICE_PLAN.equalsIgnoreCase(getServiceType())) {
			return delegate.isRIM();
		} else if (!delegate.SERVICE_TYPE_CODE_PRICE_PLAN.equalsIgnoreCase(getServiceType()) && Equipment.PRODUCT_TYPE_PCS.equals(getProductType())) {
			/*
			 * return ( (getEquipmentTypes().length == 0) ? false : (
			 * (getEquipmentTypes().length == 1) &&
			 * (getEquipmentTypes()[0].equals(Equipment.EQUIPMENT_TYPE_RIM))) ?
			 * true : false);
			 */
			return delegate.isInRIMMandatoryGroup() || containsSwitchCode("RIMBES");
		} else if (!delegate.SERVICE_TYPE_CODE_PRICE_PLAN.equalsIgnoreCase(getServiceType()) && Equipment.PRODUCT_TYPE_IDEN.equals(getProductType())) {

			String[] categoryCodes = getCategoryCodes();
			if (categoryCodes == null || categoryCodes.length == 0) {
				return false;
			}

			String code = null;
			for (int i = 0; i < categoryCodes.length; i++) {
				code = categoryCodes[i];
				if (FeatureInfo.CATEGORY_CODE_RIM.equals(code)) {
					return true;
				}
			}
		}

		return false;

	}

	public boolean isEmailAndWebspaceIncluded() {
		return (delegate.isEmailAndWebspaceIncluded() || isRIM());
	}

	public RatedFeature getFeature(String code) throws UnknownObjectException {
		return delegate.getFeature(code);
	}

	public double getRecurringCharge() {
		return delegate.getRecurringCharge();
	}

	public int getRecurringChargeFrequency() {
		return delegate.getRecurringChargeFrequency();
	}

	/*
	 * public PromoTerm getPromotionTerm() { return delegate.getPromotionTerm();
	 * }
	 */

	public boolean containsFeature(String code) {
		return delegate.containsFeature(code);
	}

	public boolean containsSwitchCode(String switchCode) {
		return delegate.containsSwitchCode(switchCode);
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public boolean isCrossFleetRestricted() {
		return (delegate.getCode().equals(ServiceSummary.HORIZONTAL_CROSSFLEET_CROSSDAP_ZERO_CHARGE) || delegate.getCode().equals(ServiceSummary.HORIZONTAL_CROSSFLEET_CROSSDAP_TWO_CHARGE));
	}

	public boolean hasAlternateRecurringCharge() {
		return delegate.hasAlternateRecurringCharge();
	}

	public String toString() {
		return delegate.toString();
	}

	// --------------------------------------------------------------------
	// Service Methods
	// --------------------------------------------------------------------
	public Service getService() throws TelusAPIException {
		return this;
	}

	public boolean isPTT() {
		return delegate.isPTT();
	}

	public double getAlternateRecurringCharge(Subscriber subscriber) throws TelusAPIException {
		try {
			if (hasAlternateRecurringCharge()) {
				return referenceDataManager.getReferenceDataHelperEJB().retrieveAlternateRecurringCharge(delegate, subscriber.getMarketProvince(), subscriber.getPhoneNumber().substring(0, 3),
						subscriber.getPhoneNumber().substring(3, 6), subscriber.getAccount().getCorporateId());
			} else {
				return getRecurringCharge();
			}
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	/**
	 * Should call isInternationalRoaming().
	 * 
	 * @deprecated
	 */
	public boolean isRUIM() {
		return delegate.isRUIM();
	}

	public boolean isInternationalCalling() {
		return delegate.isInternationalCalling();
	}

	public boolean isInternationalRoaming() {
		return delegate.isInternationalRoaming();
	}

	public boolean isVisto() {
		return delegate.isVisto();
	}

	public boolean is911() {
		return delegate.is911();
	}

	public boolean hasCallHomeFree() {
		return delegate.hasCallHomeFree();
	}

	public boolean hasCallingCircleFeatures() {
		return delegate.hasCallingCircleFeatures();
	}
	
	/**
	 * Should call SCOTT for this information
	 * 
	 * @deprecated
	 */
	public boolean isDeviceProtection() {
		return delegate.isDeviceProtection();
	}
	
	public boolean hasRIMAPN() {
		return delegate.hasRIMAPN();
	}

	public boolean hasMBAPN() {
		return delegate.hasMBAPN();
	}

	public boolean isPPSBundle() {
		return delegate.isPPSBundle();
	}

	public boolean isPPSAddOn() {
		return delegate.isPPSAddOn();
	}

	public int ppsPriority() {
		return delegate.ppsPriority();
	}

	public int ppsLicenses() {
		return delegate.ppsLicenses();
	}

	public int ppsStorage() {
		return delegate.ppsStorage();
	}

	public int getDurationServiceHours() {
		return delegate.getDurationServiceHours();
	}

	public String getServiceMappingCode() {
		return delegate != null ? delegate.getServiceMappingCode() : getCode();
	}
	
	public boolean isRLH() {
		return delegate.isRLH();
	}
	
}