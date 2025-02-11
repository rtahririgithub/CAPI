/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.reference;

import com.telus.api.*;
import com.telus.api.account.*;

public interface Service extends ServiceSummary {

	int getFeatureCount();

	/**
	 * Returns all rated features belonging to this SOC.
	 *
	 * <P>
	 * The array is never <CODE>null</CODE>, and never contains
	 * <CODE>null</CODE> elements, but may contain no (zero) elements.
	 *
	 * @link aggregationByValue
	 */
	RatedFeature[] getFeatures();

	boolean isParameterRequired();

	boolean isAdditionalNumberRequired();

	RatedFeature getFeature(String code) throws UnknownObjectException;

	double getRecurringCharge();

	int getRecurringChargeFrequency();

	// PromoTerm getPromotionTerm();

	/**
	 * Returns the number of hours the service will be active once added to the
	 * subscriber?s profile. The minimum is 24 hours and no specified maximum.
	 * This comes from SOC.soc_duration_hours This attribute is non-zero only
	 * for x-hour SOCs.
	 */
	int getDurationServiceHours();

	/**
	 * Returns <code>true</code> if a feature in this service has the specified
	 * switchCode, otherwise <code>false</code>.
	 *
	 */
	boolean containsFeature(String code);

	boolean containsSwitchCode(String switchCode);

	boolean isCrossFleetRestricted();

	String[] getCategoryCodes();

	String getCoverageType();

	/**
	 * Returns boolean valude to indicate that the service is either a PTT
	 * service or PTT price plan.
	 * 
	 * @return boolean
	 */
	boolean isPTT();

	boolean isRIM();

	boolean hasAlternateRecurringCharge();

	double getAlternateRecurringCharge(Subscriber subscriber) throws TelusAPIException;

	/**
	 * Returns boolean value to indicate that the service is a RUIM service.
	 * 
	 * @return boolean
	 * 
	 *         Should call isInternationalRoaming() instead.
	 * 
	 * @deprecated
	 */
	boolean isRUIM();

	/**
	 * Returns boolean value to indicate that the service is an International
	 * Calling service.
	 * 
	 * @return boolean
	 */
	boolean isInternationalCalling();

	/**
	 * Returns boolean value to indicate that the service is an International
	 * Roaming service.
	 * 
	 * @return boolean
	 */
	boolean isInternationalRoaming();

	/**
	 * Returns boolean value to indicate that the service is a Visto service.
	 * 
	 * @return boolean
	 */
	boolean isVisto();

	/**
	 * Returns a boolean value.
	 * 
	 * @return boolean
	 */
	boolean is911();

	boolean hasCallHomeFree();

	boolean hasCallingCircleFeatures();

	/**
	 * Returns true if this SOC is a device protection SOC. This is determined
	 * by any of the features belong to the corresponding feature category.
	 * 
	 * @return boolean
	 * @deprecated should call SCOTT for this information
	 */
	boolean isDeviceProtection();
	
	/**
	 * Returns true if it contains "RIMAPN" feature
	 * 
	 * @return boolean
	 */
	boolean hasRIMAPN();

	/**
	 * Returns true if it contains "MBAPN" feature
	 * 
	 * @return boolean
	 */
	boolean hasMBAPN();

	/**
	 * Returns true if this service is a PPS bundle
	 * 
	 * @return boolean
	 */
	boolean isPPSBundle();

	/**
	 * Returns true if this service is a PPS add-on
	 * 
	 * @return boolean
	 */
	boolean isPPSAddOn();

	/**
	 * Returns the PPS service priority within the PPS service bundle group for
	 * non PPS bundle service it returns "-1"
	 * 
	 * @return int
	 */
	int ppsPriority();

	/**
	 * Returns the # of licences included in a PPS bundle service for non PPS
	 * bundle service it returns "-1"
	 * 
	 * @return int
	 */
	int ppsLicenses();

	/**
	 * Returns the storage for a PPS Bundle/add-on service in GB for non PPS
	 * service it returns "-1"
	 * 
	 * @return int
	 */
	int ppsStorage();

	/**
	 * Returns true if this service is a RoamLikeHome (RLH) service
	 * 
	 * @return boolean
	 */
	boolean isRLH();

}