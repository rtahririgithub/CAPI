package com.telus.cmb.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum CustomerPreferenceUnsubscribeIndicator {
	/*
	* CPUI_CODES, DESCRIPTION
	* "1", "No Direct Mail"
	* "2", "No Restriction Default"
	* "3", "No Email"
	* "4", "No Telemarketing"
	* "5", "No Market Research"
	* "6", "No Virtual Agent"
	* "7", "No SMS"
	* "8", "Future use - Do not use"
	*/
	
	NO_DIRECT_MAIL ("1", "No Direct Mail"),
	NO_RESTRICTION_DEFAULT ("2", "No Restriction Default"),
	NO_EMAIL ("3", "No Email"),
	NO_TELEMARKETING ("4", "No Telemarketing"),
	NO_MARKET_RESEARCH ("5", "No Market Research"),
	NO_VIRTUAL_AGENT ("6", "No Virtual Agent"),
	NO_SMS ("7", "No SMS"),
	RESERVED_DO_NOT_USE ("8", "Future use - Do not use"),
	UNKNOWN ("-1", "Unknown type");
	
	private String code;
	private String description;
	private static Map<String, CustomerPreferenceUnsubscribeIndicator> map = initializeMap();
	private final static String DELETE_MODE = "D";
	private final static String INSERT_MODE = "I";
	
	private CustomerPreferenceUnsubscribeIndicator(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public static CustomerPreferenceUnsubscribeIndicator byCode(String code) {
		CustomerPreferenceUnsubscribeIndicator cpui = map.get(code);
		if (cpui == null) {
			cpui = UNKNOWN;
		}
		
		return cpui;
	}
	
	private static Map<String, CustomerPreferenceUnsubscribeIndicator> initializeMap() {
		Map<String, CustomerPreferenceUnsubscribeIndicator> map = new HashMap<String, CustomerPreferenceUnsubscribeIndicator>();
		
		for (CustomerPreferenceUnsubscribeIndicator cpc : CustomerPreferenceUnsubscribeIndicator.values()) {
			map.put(cpc.getCode(), cpc);
		}
		
		return map;
	}
}
