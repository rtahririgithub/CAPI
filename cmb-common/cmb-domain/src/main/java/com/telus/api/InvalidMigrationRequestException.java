package com.telus.api;


public class InvalidMigrationRequestException extends TelusAPIException {
	private static final int REASON_OFFSET = 3000;
	public static final int REASON_INVALID_MANDATORY_FIELDS = REASON_OFFSET + 1;
	public static final int REASON_INVALID_PRODUCT_TYPE = REASON_OFFSET + 2;
	public static final int REASON_EQUIPMENT_STOLEN = REASON_OFFSET + 3;
	public static final int REASON_INVALID_PHONE_NUMBER = REASON_OFFSET + 4;
	public static final int REASON_INVALID_EQUIPMENT_TYPE = REASON_OFFSET + 5;
	public static final int REASON_INVALD_ACTIVATION_METHOD = REASON_OFFSET + 6;
	public static final int REASON_INVAILD_ACTIVATION_AIRTIME_CARD = REASON_OFFSET + 7;
	public static final int REASON_INVAILD_ACTIVATION_CREDIT_CARD = REASON_OFFSET + 8;
	public static final int REASON_INVAILD_ACTIVATION_TOPUP = REASON_OFFSET + 9;
    public static final int REASON_INVAILD_ACCOUNT_STATUS = REASON_OFFSET + 10;
    public static final int REASON_INVALID_DEPOSIT_TRANSFER_IND = REASON_OFFSET + 11;
    public static final int REASON_INVALID_FLEET_FLAG_INFO = REASON_OFFSET + 12;
    public static final int REASON_INVALID_ACTIVATION_OPTION = REASON_OFFSET + 13;
    public static final int REASON_INVALID_ACTIVATION_TOPUP_PAYMENT_ARRANGEMENT = REASON_OFFSET + 14;
    public static final int REASON_INVALID_ACTIVATION_TYPE = REASON_OFFSET + 15;
    
	private int reason;
	public int getReason(){		return reason;	}
	public InvalidMigrationRequestException(String message, Throwable exception, int reason) {
        super(message, exception);
        this.reason = reason;
    }
	public InvalidMigrationRequestException(String message, int reason) {
        this(message, null, reason);
    }
}