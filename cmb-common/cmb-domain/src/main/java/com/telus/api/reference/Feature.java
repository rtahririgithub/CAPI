/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */
package com.telus.api.reference;

public interface Feature extends Reference {

	String SWITCH_CODE_VOICE_MAIL = "VM";
	String SWITCH_CODE_CALL_FORWARDING = "CFW";
	String SWITCH_CODE_CALL_WAITING = "CWT";
	String SWITCH_CODE_FAX_MAIL = "VMFAX";
	String CATEGORY_CODE_VOICE_MAIL = "VM";
	String CATEGORY_CODE_VOICE2TEXT = "VTT";
	//uncomment the if condition for VVM changes
	String CATEGORY_CODE_VISUALVOICEMAIL = "IVV";  
	String CATEGORY_CODE_MESSAGING = "TM";
	String CATEGORY_CODE_EVENINGS_WEEKENDS = "EW";
	String CATEGORY_CODE_CALLING = "CL";
	String CATEGORY_CODE_WEB_WIRELESS = "WW";
	String CATEGORY_CODE_DISPATCH = "DP";
	String CATEGORY_CODE_RIM = "RIM";	
	String CATEGORY_CODE_CORPORATE_HELP_DESK = "CHS";
	String CATEGORY_CODE_ENHANCED_CALL_CENTRE = "ECS";
	String CATEGORY_CODE_BASIC_SERVICE = "BSC";
	String CATEGORY_CODE_BLIN = "BLN";
	String CATEGORY_CODE_CALLING_CIRCLE = "CC";
	//String CATEGORY_CODE_JAVADOWNLOAD = "JVD";
	String CATEGORY_CODE_MOSMS = "MO";
	String CATEGORY_CODE_CALL_HOME_FREE = "CF";

	//PDA Device
	String CATEGORY_CODE_PDA = "PDA";
	String CATEGORY_CODE_EMAIL = "EML";
	String CATEGORY_CODE_EVDO = "DO";
	String CATEGORY_CODE_MULTI_RING = "MR";
	String CATEGORY_CODE_VISTO = "VST";

	// LBS
	String CATEGORY_CODE_LBS_MS_BASED_UPLANE = "MSB";
	String CATEGORY_CODE_LBS_GPS_BASE = "GPS";
	String CATEGORY_CODE_LBS_TRACKER = "TRR";	

	// Mike Data Services
	String CATEGORY_CODE_MMS = "MMS";

	// 911
	String FEATURE_CODE_TELUS_911 = "911";
	String FEATURE_CODE_KOODO_911 = "PT911";
	String FEATURE_CODE_WALMART_911 = "5W911";
	String CATEGORY_CODE_TELUS_911 = "911";
	String CATEGORY_CODE_KOODO_911 = "PT9";
	String CATEGORY_CODE_WALMART_911 = "WM9";

	//SLA
	String CATEGORY_CODE_TTR_SLA = "TTR";

	// SMS
	String FEATURE_CATEGORY_SMS = "SMS";

	// Minute Pooling
	String CATEGORY_CODE_MINUTE_POOLING = "MP";

	// International Services - Telus
	String CATEGORY_CODE_RUIM = "RUM";	
	String CATEGORY_CODE_INTERNATIONAL_CALLING = "ICA";	

	// International Services - Koodo
	String CATEGORY_CODE_INTERNATIONAL_ROAMING_KOODO = "PRM";
	String CATEGORY_CODE_INTERNATIONAL_CALLING_KOODO = "PIC";
	
	// International Services - MIKE
	String CATEGORY_CODE_INTERNATIONAL_CALLING_MIKE = "MIC"; 
	
	//one-number (husky) communication suite
	String CATEGORY_CODE_COMP_SUITE_PRIMARY = "ENP"; //Enablement for Primary
	String CATEGORY_CODE_COMP_SUITE_COMPANION = "ENC"; //Enablement for COMPANION
	
	//standalone (tremblant) communicatino suite
	String CATEGORY_CODE_STANDALONE_COMP_SUITE_PRIMARY = "STP"; //Enablement for Primary
	String CATEGORY_CODE_STANDALONE_COMP_SUITE_COMPANION = "STC"; //Enablement for COMPANION

	// Video Telephony
	String FEATURE_CODE_VT = "VID";
	String FEATURE_CODE_VT_CALL_FORWARD = "VIDCF";
	
	// Device Protection
	String CATEGORY_CODE_DEVICE_PROTECTION = "DEP"; //Arbitrary code for device protection

	// APN
	String FEATURE_CODE_RIMAPN = "RIMAPN";
	String FEATURE_CODE_MBAPN = "MBAPN";

	boolean isParameterRequired();
	boolean isDuplFeatureAllowed();
	boolean isTelephony();
	boolean isDispatch();
	boolean isWirelessWeb();
	boolean isAdditionalNumberRequired();
	boolean isSMSNotification();
	String  getSwitchCode();
	String  getCategoryCode();
	String  getType();
	boolean isCallingCircle();

	/**
	 * Returns pool group ID.
	 *
	 * <P>The pool group ID might be<CODE>null</CODE> and only contains a value when feature type is <CODE>P</CODE>
	 */       
	String getPoolGroupId();
	boolean isPooling();
	boolean isDollarPooling();
}

