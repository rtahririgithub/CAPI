package com.telus.api.reference;

import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;

public interface PricePlanSummary extends Service {

	public static final int USAGE_RATING_FREQUENCY_SECOND = 1;
	public static final int USAGE_RATING_FREQUENCY_MINUTE = 60;

	public static final String PRIVILEGE_PRICEPLAN_ACTIVATE   = "ACTIVATE";
	public static final String PRIVILEGE_PRICEPLAN_MODIFY     = "MODIFY";
	public static final String PRIVILEGE_PRICEPLAN_CHANGE_TO  = "CHANGETO";

	public static final int CONTRACT_TERM_NONE  = 0;
	public static final int CONTRACT_TERM_ONE_YEAR = 12;
	//public static final int CONTRACT_TERM_18_MONTHS = 18;  //not required for zebra anymore
	public static final int CONTRACT_TERM_TWO_YEARS  = 24;
	public static final int CONTRACT_TERM_THREE_YEARS  = 36;
	public static final int CONTRACT_TERM_ALL = 99;

	int getIncludedServiceCount();

	/**
	 * Returns all SOCs included in this plan.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * @link aggregationByValue
	 */
	Service[] getIncludedServices();

	Service getIncludedService(String code) throws UnknownObjectException;
	PricePlan getPricePlan(String equipmentType, String provinceCode, char accountType, char accountSubType) throws TelusAPIException;
	boolean containsIncludedService(String code);
	boolean containsService(String code);

	/**
	 * One of the USAGE_RATING_FREQUENCY_xxx constants.
	 */
	int getUsageRatingFrequency();
	int getIncludedMinutesCount();

	/**
	 * Returns <CODE>true</CODE> if this PricePlan's minutes can be
	 * shared my multiple subscribers on the same acount.
	 *
	 * <P>When <CODE>true</CODE> the result of <CODE>getPricePlan</CODE> can be typed casted to <CODE>ShareablePricePlan</CODE>.
	 *
	 * @see #getPricePlan
	 * @see ShareablePricePlan
	 */
	boolean isSharable();
	boolean isSuspensionPricePlan();
	boolean isAvailableForActivation();
	boolean isAvailableForChange();
	boolean isAvailableForChangeByDealer();
	boolean isAvailableForChangeByClient();
	boolean isAvailableToModifyByDealer();
	boolean isAvailableToModifyByClient();
	boolean isAvailableForNonCorporateRenewal();
	boolean isAvailableForCorporateRenewal();
	boolean isAvailableForCorporateStoreActivation();
	boolean isAvailableForRetailStoreActivation();
	@Deprecated
	boolean isFidoPricePlan();

	/**
	 * Returns the terms (month-to-month, 1 year, 2 years, etc.), in months, supported by this priceplan.
	 *
	 * <P>The array is guaranteed to have at least 1 element.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 */
	int[] getAvailableTermsInMonths() throws TelusAPIException;
	boolean isMinutePoolingCapable();  
	boolean isZeroIncludedMinutes();  
	@Deprecated
	boolean isAmpd();  
	boolean isAOMPricePlan();  
	boolean isDollarPoolingCapable();
	boolean isComboPlan();
	
	String getSeatType();
	
}