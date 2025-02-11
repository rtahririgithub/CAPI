package com.telus.api.portability;

public interface PortOutEligibility {

	public static String NDP_DIRECTION_IND_WIRELESS_TO_WIRELESS = "A";
	public static String NDP_DIRECTION_IND_WIRELESS_TO_WIRELINE = "B";
	public static String NDP_DIRECTION_IND_WIRELINE_TO_WIRELESS = "C";
		
	boolean isEligible();
	boolean isTransferBlocking();
	
}
