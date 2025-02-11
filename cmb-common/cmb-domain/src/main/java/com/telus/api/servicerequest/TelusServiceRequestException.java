package com.telus.api.servicerequest;

import java.util.HashMap;
import java.util.Map;

import com.telus.api.TelusAPIException;

/** 
 * @author W. Kwok
 * @version 1.0, 02-Apr-2008
 */
public class TelusServiceRequestException extends TelusAPIException {
		
	private static final long serialVersionUID = 1L;
	private final int reason;

	public TelusServiceRequestException(int reason, String message, Throwable exception) {
		super(message + ": [reason=" + getReasonText(reason) + "]", exception);
		this.reason = reason;
	}

	public TelusServiceRequestException(int reason, Throwable exception) {
		this(reason, "", exception);
	}

	public TelusServiceRequestException(int reason, String message) {
		super(message + ": [reason=" + getReasonText(reason) + "]");
		this.reason = reason;
	}

	public TelusServiceRequestException(int reason) {
		super("[reason=" + getReasonText(reason) + "]");
		this.reason = reason;
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
	public static final int ERR001 = 0;
	
	protected static final Map reasons = new HashMap();

	static {
		reasons.put(new Integer(ERR001), "InvalidArgumentException");
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
