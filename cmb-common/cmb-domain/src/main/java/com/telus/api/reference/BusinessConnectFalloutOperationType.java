package com.telus.api.reference;

import java.io.Serializable;

public final class BusinessConnectFalloutOperationType implements Serializable {
	
	private static final long serialVersionUID = -2481381990994619576L;
	
	public static final BusinessConnectFalloutOperationType UNKNOWN = new BusinessConnectFalloutOperationType(0, "UNKNOWN", "Unknown");
	public static final BusinessConnectFalloutOperationType CHANGE_VOIP_WITH_CHARGE = new BusinessConnectFalloutOperationType(1, "CHANGE_VOIP_WITH_CHARGE", "Change VOIP telephone number with Charge");
	public static final BusinessConnectFalloutOperationType MOVE_VOIP = new BusinessConnectFalloutOperationType(2, "MOVE_VOIP", "Move VOIP telephone number between users");
	public static final BusinessConnectFalloutOperationType WLS_MIGRATION_PRICE_PLAN = new BusinessConnectFalloutOperationType(3, "WLS_MIGRATION_PRICE_PLAN", "WLS Migration");
	public static final BusinessConnectFalloutOperationType WLS_MIGRATION_SEAT_GROUP = new BusinessConnectFalloutOperationType(4, "WLS_MIGRATION_SEAT_GROUP", "WLS Migration");
	
	public static final BusinessConnectFalloutOperationType[] values() {
		return VALUES;
	}
	
	private static final BusinessConnectFalloutOperationType[] VALUES = {
		UNKNOWN,
		CHANGE_VOIP_WITH_CHARGE,
		MOVE_VOIP,
		WLS_MIGRATION_PRICE_PLAN,
		WLS_MIGRATION_SEAT_GROUP
	};
	
	private final int code;
	private final String operation;
	private final String description;

	private BusinessConnectFalloutOperationType(int code, String operation, String description) {
		this.code = code;
		this.operation = operation;
		this.description = description;
	}

	public int getCode() {
		return this.code;
	}
	
	public String getOperation() {
		return this.operation;
	}

	public String getDescription() {
		return this.description;
	}
	
	public static BusinessConnectFalloutOperationType valueOf(int code) {
		
		for (int i = 0; i < VALUES.length; i++) {
			if (VALUES[i].getCode() == code) {
				return VALUES[i];
			}
		}
		
		throw new IllegalArgumentException("No BusinessConnectSubscriberFalloutOperation enumerated type found for code: [" + code + "]");
	}
	
	public static BusinessConnectFalloutOperationType valueOf(String operation) {
		
		for (int i = 0; i < VALUES.length; i++) {
			if (VALUES[i].getOperation().equals(operation)) {
				return VALUES[i];
			}
		}
		
		throw new IllegalArgumentException("No BusinessConnectSubscriberFalloutOperation enumerated type found for operation: [" + operation + "]");
	}

	// This method should be used for comparing deserialized versions of BusinessConnectSubscriberFalloutOperation enumerated types rather than the identity
	// comparision operator ('=='), as per the RMI limitations of the typesafe enum pattern.
	public boolean equals(Object operation) {
		return this.code == ((BusinessConnectFalloutOperationType) operation).getCode();
	}
	
}