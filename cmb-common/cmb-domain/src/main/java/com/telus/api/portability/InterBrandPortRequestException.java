package com.telus.api.portability;

import java.util.HashMap;
import java.util.Map;

import com.telus.api.TelusAPIException;
import com.telus.api.message.ApplicationMessage;

public class InterBrandPortRequestException extends TelusAPIException {

	private static final long serialVersionUID = 1L;
	
	// these error codes correspond to message IDs in the EAS.adapted_messages table
	public static final int ERR001 = 158;
	public static final int ERR002 = 159;
	public static final int ERR003 = 160;
	public static final int ERR004 = 161;
	public static final int ERR005 = 162;
	public static final int ERR006 = 163;
	public static final int ERR007 = 164;
	public static final int ERR008 = 165;
	public static final int ERR009 = 166;

	public InterBrandPortRequestException(ApplicationMessage applicationMessage, int reason) {
		this(null, applicationMessage, reason);
	}

	public InterBrandPortRequestException(Throwable exception, ApplicationMessage applicationMessage, int reason) {
		super(exception, applicationMessage, reason);
	}
	
	public int getReason() {
		return reason;
	}

	public String getReasonText() {
		return getReasonText(reason);
	}

	//---------------------------------------------------------
	// Reason to text translation.
	//---------------------------------------------------------
	protected static final Map reasons = new HashMap();

	static {
		reasons.put(new Integer(ERR001), "unable to cancel source subscriber");
		reasons.put(new Integer(ERR002), "unable to reserve phone number on target subscriber");
		reasons.put(new Integer(ERR003), "unable to activate phone number on target subscriber");
		reasons.put(new Integer(ERR004), "unable to resume subscriber on source account (rollback step)");
		reasons.put(new Integer(ERR005), "unable to release phone number on target account (rollback step)");
		reasons.put(new Integer(ERR006), "unable to retrieve source subscriber");
		reasons.put(new Integer(ERR007), "unable to resume target subscriber");
		reasons.put(new Integer(ERR008), "unable to change phone number to target subscriber");
		reasons.put(new Integer(ERR009), "invalid inter-brand port activity reason code");
	}
	
	protected static String getReasonText(int reason) {
		Integer i = new Integer(reason);
		String text = (String)reasons.get(i);
		if(text == null) {
			return "UNKNOWN";
		}
		return text;
	}	

}