package com.telus.api.equipment;

import java.util.HashMap;
import java.util.Map;
import com.telus.api.TelusAPIException;

/**
 *  <CODE>MasterLockException</CODE>
 **/
public class MasterLockException extends TelusAPIException {
	
	public static final int ESN_NOT_FOUND = 0;
	public static final int USER_ID_NOT_FOUND = 1;
	public static final int MAX_REQUESTS_EXCEEDED = 2;

	public MasterLockException(String message, Throwable exception, int reason) {
		super(message + ": [reason=" + getReasonText(reason) + "]", exception);
		this.reason = reason;
	}

	public MasterLockException(Throwable exception, int reason) {
		super(exception);
		this.reason = reason;
	}

	public MasterLockException(String message, int reason) {
		super(message + ": [reason=" + getReasonText(reason) + "]");
		this.reason = reason;
	}

	public MasterLockException(int reason) {
		super("[reason=" + getReasonText(reason) + "]");
		this.reason = reason;
	}

	public int getReason() {
		return reason;
	}

	public String getReasonText() {
		return getReasonText(reason);
	}

	// ---------------------------------------------------------
	// Reason to text translation.
	// ---------------------------------------------------------
	protected static final Map reasons = new HashMap();
	static {
		reasons.put(new Integer(ESN_NOT_FOUND), "ESN_NOT_FOUND");
		reasons.put(new Integer(USER_ID_NOT_FOUND), "USER_ID_NOT_FOUND");
		reasons.put(new Integer(MAX_REQUESTS_EXCEEDED),	"MAX_REQUESTS_EXCEEDED");
	}

	protected static String getReasonText(int reason) {
		Integer i = new Integer(reason);
		String text = (String) reasons.get(i);
		if (text == null) {
			return "UNKNOWN";
		}
		return text;
	}
}
