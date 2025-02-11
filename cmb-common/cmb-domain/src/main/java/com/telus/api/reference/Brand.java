/**
 * Title:        Brand<p>
 * Description:  The Brand contains the brand attributes information.<p>
 * Copyright:    Copyright (c) Tsz Chung Tong<p>
 * Company:      Telus Mobility Inc<p>
 * @author Tsz Chung Tong
 * @version 1.0
 */
package com.telus.api.reference;

public interface Brand extends Reference {

	public static final int BRAND_ID_TELUS = 1;
	public static final int BRAND_ID_AMPD = 2;
	public static final int BRAND_ID_KOODO = 3;
	public static final int BRAND_ID_CLEARNET = 4;
	public static final int BRAND_ID_WALMART = 5;
	public static final int BRAND_ID_PC_MOBILE = 6;
	public static final int BRAND_ID_PUBLIC_MOBILE = 7;
	public static final int BRAND_ID_ALL = 255;
	public static final int BRAND_ID_NOT_APPLICABLE = -1;

	String getCode();
	String getDescription();
	String getDescriptionFrench();
	String getShortDescription();
	String getShortDescriptionFrench();
	int getBrandId();
}
