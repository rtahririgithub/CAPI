package com.telus.api.portability;

import com.telus.api.reference.Reference;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 22-Oct-2006
 */
public interface PRMReferenceData extends Reference {
	
	public static final String CATEGORY_PORT_IN_ELIGIBILITY = "PORT_IN_ELIGIBILITY";
	public static final String CATEGORY_CARRIER_INFO = "CARRIER_INFO";
	public static final String CATEGORY_WPRR_REASON_CODE = "WPRR_REASON_CODE";
	public static final String CATEGORY_STATUS_CODE = "STATUS_CODE";
	public static final String CATEGORY_SUP2_REASON_CODE = "SUP2_REASON_CODE";
	public static final String CATEGORY_DELAY_CODE = "DELAY_CODE";
	public static final String[] allCategories = new String[] {
			CATEGORY_PORT_IN_ELIGIBILITY, CATEGORY_CARRIER_INFO,
			CATEGORY_WPRR_REASON_CODE, CATEGORY_STATUS_CODE,
			CATEGORY_SUP2_REASON_CODE, CATEGORY_DELAY_CODE };

	String getCategory();
}
