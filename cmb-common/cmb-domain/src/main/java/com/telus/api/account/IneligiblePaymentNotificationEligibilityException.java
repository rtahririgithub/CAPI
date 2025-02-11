package com.telus.api.account;

import java.util.HashMap;
import java.util.Map;

import com.telus.api.TelusAPIException;

public class IneligiblePaymentNotificationEligibilityException extends TelusAPIException {
   private static final int REASON_OFFSET = 1000;
    
    /** @deprecated this constant is no longer used anywhere in the code */
    public static final int ACCOUNT_NOT_IN_TREATMENT = REASON_OFFSET;

    /** @deprecated this constant is no longer used anywhere in the code */
    public static final int NO_OVERDUE_BALANCE_EXISTS = REASON_OFFSET + 1;

    /** @deprecated this constant is no longer used anywhere in the code */
    public static final int ACTIVE_PAYMENT_ARRANGEMENTS = REASON_OFFSET + 2;

    public static final int ACCOUNT_IS_SUSPENDED = REASON_OFFSET + 3;

    public static final int ACCOUNT_IS_CANCELLED = REASON_OFFSET + 4;

    /** @deprecated this constant is no longer used anywhere in the code */
    public static final int ACCOUNT_IS_HOTLINE = REASON_OFFSET + 5;

    /** @deprecated this constant is no longer used anywhere in the code */
    public static final int CLIENT_TENURE_LESS_THAN_6MONTHS = REASON_OFFSET + 6;

    public static final int NO_PREVIOUS_PAYMENTS = REASON_OFFSET + 7;

    public static final int BACKED_OUT_PAYMENTS = REASON_OFFSET + 8;

    public static final int UNSUPPORTED_CREDIT_CLASS = REASON_OFFSET + 9;

    public static final int ACCOUNT_HAS_AMOUNTS_IN_90DAYS_BUCKET = REASON_OFFSET + 10;

    public static final int PREVIOUSLY_DISHONOURED_SELFSERVE_PAYMENT_NOTIFICATION = REASON_OFFSET + 11;

  
    public static final int ACCOUNT_HAS_BEEN_OVERTURNED_TO_AGENCY = REASON_OFFSET + 13;
    
    public static final int PAYMENT_AMOUNT_LESS_CURRENT_DUE = REASON_OFFSET + 14;
    
    public static final int OPEN_PAYMENT_NOTFICATION_EXIST = REASON_OFFSET + 15;
    
    //Added by Sachin K Sharma on 16-06-2010
    //Add constant if payment amount less than the minimum amount due 
    public static final int PAYMENT_AMOUNT_LESS_MINIMUM_DUE = REASON_OFFSET + 16;
    
    
    private final int reason;
    private final Account account;

    public IneligiblePaymentNotificationEligibilityException(int reason,Account account) {
        super(((String)reasons.get(new Integer(reason))));
        this.reason = reason;
        this.account = account;
    }

  
    public int getReason() {
        return reason;
    }
    
    public  String getReasonMessage(){
        String reasonMessage = (String)reasons.get(new Integer(reason));
        if(reason == ACCOUNT_IS_SUSPENDED || reason == ACCOUNT_IS_CANCELLED){
            reasonMessage  += account.getStatusActivityReasonCode();
        }
        return reasonMessage;
    }

    protected static final Map reasons = new HashMap();

    static {
        reasons.put(new Integer(ACCOUNT_NOT_IN_TREATMENT), "Account is currently not in treatment");
        reasons.put(new Integer(NO_OVERDUE_BALANCE_EXISTS), "No amount is currently overdue on the account");
        reasons.put(new Integer(ACTIVE_PAYMENT_ARRANGEMENTS), "A payment arrangement currently exists on this account.");
        reasons.put(new Integer(ACCOUNT_IS_SUSPENDED), "Account is suspended due to ");
        reasons.put(new Integer(ACCOUNT_IS_CANCELLED), "Account is cancelled due to ");
        reasons.put(new Integer(ACCOUNT_IS_HOTLINE), "Account is currently in hotline status.");
        reasons.put(new Integer(CLIENT_TENURE_LESS_THAN_6MONTHS), "Client tenure is less than 6 months");
        reasons.put(new Integer(NO_PREVIOUS_PAYMENTS), "No previous payment history exists on this account.");
        reasons.put(new Integer(BACKED_OUT_PAYMENTS), "Account has had NSF or backed-out payments");
        reasons.put(new Integer(UNSUPPORTED_CREDIT_CLASS), "No automated payment notification is allowed for this credit class.");
        reasons.put(new Integer(ACCOUNT_HAS_AMOUNTS_IN_90DAYS_BUCKET), "Overdue balance aging exceeds allowable limit.");
        reasons.put(new Integer(PREVIOUSLY_DISHONOURED_SELFSERVE_PAYMENT_NOTIFICATION), "A dishonoured payment notification currently exists on this account.");
        reasons.put(new Integer(ACCOUNT_HAS_BEEN_OVERTURNED_TO_AGENCY), "Account has been referred to an agency for collection.");
        reasons.put(new Integer(PAYMENT_AMOUNT_LESS_CURRENT_DUE), "payment amount is less than overdue charges.");
        reasons.put(new Integer(OPEN_PAYMENT_NOTFICATION_EXIST), "Open payment notfication exist.");
        
        //Added by Sachin K Sharma on 16-06-2010 
        reasons.put(new Integer(PAYMENT_AMOUNT_LESS_MINIMUM_DUE), "payment amount is less than minimum required payment.");
        
    }
    
}    
