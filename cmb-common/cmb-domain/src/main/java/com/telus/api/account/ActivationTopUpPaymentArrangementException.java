package com.telus.api.account;

import java.util.HashMap;
import java.util.Map;

import com.telus.api.TelusAPIException;

public class ActivationTopUpPaymentArrangementException extends TelusAPIException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int REASON_CREDITCARD_NOT_SET = 1;
	public static int REASON_INVALID_CREDITCARD = 2;
	public static int REASON_TOPUPAMOUNT_NOT_SET = 3;
	public static int REASON_AIRTIMECARD_NOT_SET = 4;
	public static int REASON_INVALID_CARDTYPE = 5;

    private final int reason;
    private final String reasonMessage;
    private InvalidCreditCardException icce;

    public ActivationTopUpPaymentArrangementException(int reason) {
        super((String)reasons.get(new Integer(reason)));
        this.reason = reason;
        reasonMessage = ((String)reasons.get(new Integer(reason)));
    }

    public ActivationTopUpPaymentArrangementException(int reason, InvalidCreditCardException icce) {
        super(((String)reasons.get(new Integer(reason))));
        this.reason = reason;
        reasonMessage = (String)reasons.get(new Integer(reason));
        this.icce = icce;
    }
  
    public int getReason() {
        return reason;
    }
    
    public  String getReasonMessage(){
        if (icce != null) {
        	return reasonMessage + "\r\nInvalidCreditCardException: reason=" + icce.getReason() + ", reasonText=" + icce.getReasonText();
        }else  {
        	return reasonMessage;
        }
    }
    
    public String getMessage() {
    	return "["+reason+"] "+getReasonMessage();
    }

    protected static final Map reasons = new HashMap();

    static {
        reasons.put(new Integer(REASON_CREDITCARD_NOT_SET), "Valid credit card is not set");
        reasons.put(new Integer(REASON_INVALID_CREDITCARD), "Invalid credit card");
        reasons.put(new Integer(REASON_TOPUPAMOUNT_NOT_SET), " Activation top up amount is not set ");
        reasons.put(new Integer(REASON_AIRTIMECARD_NOT_SET), " Airtime card is not set ");
        reasons.put(new Integer(REASON_INVALID_CARDTYPE), " Invalid activation top up card type ");
    }
}
