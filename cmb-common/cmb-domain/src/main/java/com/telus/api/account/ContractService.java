/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.reference.RatedFeature;
import com.telus.api.reference.Reference;
import com.telus.api.reference.Service;

/**
 * <CODE>ContractService</CODE>
 * 
 */
public interface ContractService extends Reference {

	Service getService() throws TelusAPIException;

	ContractFeature addFeature(RatedFeature feature) throws UnknownObjectException;

	void removeFeature(String featureCode) throws UnknownObjectException;

	int getFeatureCount();

	ContractFeature[] getFeatures();

	ContractFeature getFeature(String code) throws UnknownObjectException;
	
	ContractFeature getFeatureBySwitchCode(String code) throws UnknownObjectException;

	Date getEffectiveDate();

	void setEffectiveDate(Date effectiveDate);
	
	Date getExpiryDate();

	void setExpiryDate(Date expiryDate);

	String[] getAdditionalNumbers();

	boolean getAutoRenew();

	void setAutoRenew(boolean autoRenew);

	PrepaidPromotionDetail getPrepaidPromotionDetail();

	void setAutoRenewFundSource(int autoRenewFundSource);
	
	int getAutoRenewFundSource();

	boolean containsFeature(String code);
	
	/**
	 * Returns the dealer code that's used when this SOC was added.
	 * 
	 * @return String
	 */
	String getDealerCode();

	/**
	 * Returns the sales rep code that's used when this SOC was added.
	 * 
	 * @return String
	 */
	String getSalesRepId();
	
	void setPurchaseFundSource(int purchaseFundSource);
	
	int getPurchaseFundSource();
	
	/**
	 * Returns an effective start date of the underlying duration service.
	 * @return
	 */
	public Calendar getDurationServiceStartTime();
	
	/**
	 * Returns an expiration date of the underlying duration service.
	 * @return
	 */
	public Calendar getDurationServiceEndTime();
	
	/**
	 * Returns true if the current service is durational, i.e. duration time is defined for the service.
	 * 
	 * @return
	 */
	public boolean isDurationService();
	
	/**
	 * Generates service mapping key for in-memory managing contract services. 
	 * @return
	 */
	public String getServiceMappingCode();

}
