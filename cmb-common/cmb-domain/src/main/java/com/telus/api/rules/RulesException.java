package com.telus.api.rules;

import com.telus.api.TelusAPIException;

public class RulesException extends TelusAPIException {
	
	private static final long serialVersionUID = 1L;

	// processor reason constants
	private static final int PROCESSOR_REASON_OFFSET = 0;
	public static final int REASON_INVALID_PROCESSOR_ID = PROCESSOR_REASON_OFFSET + 1;
	public static final int REASON_INVALID_CATEGORY = PROCESSOR_REASON_OFFSET + 2;
	public static final int REASON_PROCESSOR_INITIALIZATION_FAILURE = PROCESSOR_REASON_OFFSET + 3;
	
	// rule reason constants
	private static final int RULE_REASON_OFFSET = 1000;
	public static final int REASON_INVALID_RULE = RULE_REASON_OFFSET + 1;
	public static final int REASON_INVALID_RULE_TEMPLATE = RULE_REASON_OFFSET + 2;
	
	// context reason constants
	private static final int CONTEXT_REASON_OFFSET = 2000;
	public static final int REASON_INVALID_ACCOUNT = CONTEXT_REASON_OFFSET + 1;
	public static final int REASON_INVALID_SUBSCRIBER = CONTEXT_REASON_OFFSET + 2;
	public static final int REASON_INVALID_SUBSCRIBER_CONTEXT = CONTEXT_REASON_OFFSET + 3;
	public static final int REASON_INVALID_TRANSACTION_TYPE = CONTEXT_REASON_OFFSET + 4;
	public static final int REASON_INVALID_SUBCRIBER_CONTEXT_TRANSACTION_TYPE_COMBINATION = CONTEXT_REASON_OFFSET + 5;
	
	// assessor reason constants
	private static final int ASSESSOR_REASON_OFFSET = 3000;
	public static final int REASON_ASSESSOR_CLASS_INSTANTIATION_FAILURE = ASSESSOR_REASON_OFFSET + 1;
    
	private int reason;
	
	public RulesException(String message, int reason, Throwable exception) {
        super(message, exception);
        this.reason = reason;
    }
	
	public RulesException(String message, int reason) {
        this(message, reason, null);
    }
	
	public int getReason() {
		return reason;	
	}
}