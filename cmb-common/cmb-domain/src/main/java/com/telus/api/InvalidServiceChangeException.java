/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api;

import com.telus.api.account.*;
import com.telus.api.reference.ServiceSummary;

@SuppressWarnings("unchecked")
public class InvalidServiceChangeException extends InvalidServiceException {

	private static final long serialVersionUID = 1L;

	private static final int REASON_OFFSET = 2000;
	public static final int DUPLICATE_SERVICE = REASON_OFFSET + 1;
	public static final int FEATURE_CONFLICT = REASON_OFFSET + 2;
	public static final int BOUND_SERVICE = REASON_OFFSET + 3;
	public static final int PROMOTIONAL_SERVICE = REASON_OFFSET + 4;
	public static final int UNAVAILABLE_SERVICE = REASON_OFFSET + 5;
	public static final int GRAND_FATHERED_PRICEPLAN = REASON_OFFSET + 6;
	public static final int DUPLICATE_FEATURE = REASON_OFFSET + 7;
	public static final int SERVICE_CONFLICT = REASON_OFFSET + 8;
	public static final int REQUIRED_SERVICE_IS_MISSING = REASON_OFFSET + 9;
	public static final int LADDERED_SERVICE = REASON_OFFSET + 10;
	public static final int ACCOUNT_INELIGIBLE = REASON_OFFSET + 11;
	public static final int SERVICE_ACTIVATIONDATE_CONFLICT = REASON_OFFSET + 12;
	public static final int SERVICE_EXPIRYDATE_CONFLICT = REASON_OFFSET + 13;
	public static final int REQUIRED_SERVICE_IS_MISSING_ONADD = REASON_OFFSET + 14;
	public static final int REQUIRED_SERVICE_IS_MISSING_ONREMOVE = REASON_OFFSET + 15;
	public static final int VOIP_LICENSE_CHANGE_ERROR = REASON_OFFSET + 16;
	public static final int NO_UNASSIGNED_VOIP_LICENSE = REASON_OFFSET + 17;
	public static final int VOIP_SUPPLEMENTARY_DATA_RETRIEVAL_ERROR = REASON_OFFSET + 18;

	/**
	 *@link aggregation
	 */
	private final ContractService contractService;
	private final String featureCode;

	public InvalidServiceChangeException(int reason, String message, Throwable exception, ContractService contractService, String featureCode) {
		super(reason, message, exception);
		this.contractService = contractService;
		this.featureCode = featureCode;
	}

	public InvalidServiceChangeException(int reason, Throwable exception, ContractService contractService, String featureCode) {
		super(reason, exception);
		this.contractService = contractService;
		this.featureCode = featureCode;
	}

	public InvalidServiceChangeException(int reason, String message, ContractService contractService, String featureCode) {
		super(reason, message);
		this.contractService = contractService;
		this.featureCode = featureCode;
	}

	public InvalidServiceChangeException(int reason, String message, ServiceSummary conflictService, ContractService contractService, String featureCode) {
		super(reason, message, conflictService);
		this.contractService = contractService;
		this.featureCode = featureCode;
	}

	public InvalidServiceChangeException(int reason, ContractService contractService, String featureCode) {
		super(reason);
		this.contractService = contractService;
		this.featureCode = featureCode;
	}

	public InvalidServiceChangeException(int reason, String message, Throwable exception) {
		this(reason, message, exception, null, null);
	}

	public InvalidServiceChangeException(int reason, Throwable exception) {
		this(reason, exception, null, null);
	}

	public InvalidServiceChangeException(int reason, String message) {
		this(reason, message, null, null);
	}

	public InvalidServiceChangeException(int reason) {
		this(reason, (ContractService) null, (String) null);
	}

	/**
	 * Returns the existing conflicting service in the case of DUPLICATE_SERVICE,
	 * FEATURE_CONFLICT and LADDERED_SERVICE. The value will be <CODE>null</CODE> if the conflicting
	 * feature is part of the prcaplan.
	 *
	 */
	public ContractService getContractService() {
		return contractService;
	}

	/**
	 * Returns the feature's code in the case of FEATURE_CONFLICT.
	 *
	 */
	public String getFeatureCode() {
		return featureCode;
	}

	static {
		reasons.put(new Integer(DUPLICATE_SERVICE), "DUPLICATE_SERVICE");
		reasons.put(new Integer(FEATURE_CONFLICT), "FEATURE_CONFLICT");
		reasons.put(new Integer(BOUND_SERVICE), "BOUND_SERVICE");
		reasons.put(new Integer(PROMOTIONAL_SERVICE), "PROMOTIONAL_SERVICE");
		reasons.put(new Integer(UNAVAILABLE_SERVICE), "UNAVAILABLE_SERVICE");
		reasons.put(new Integer(GRAND_FATHERED_PRICEPLAN), "GRAND_FATHERED_PRICEPLAN");
		reasons.put(new Integer(DUPLICATE_FEATURE), "DUPLICATE_FEATURE");
		reasons.put(new Integer(SERVICE_CONFLICT), "SERVICE_CONFLICT");
		reasons.put(new Integer(REQUIRED_SERVICE_IS_MISSING), "REQUIRED_SERVICE_IS_MISSING");
		reasons.put(new Integer(ACCOUNT_INELIGIBLE), "ACCOUNT_INELIGIBLE");
		reasons.put(new Integer(LADDERED_SERVICE), "LADDERED_SERVICE");
		reasons.put(new Integer(SERVICE_ACTIVATIONDATE_CONFLICT), "SERVICE_ACTIVATIONDATE_CONFLICT");
		reasons.put(new Integer(SERVICE_EXPIRYDATE_CONFLICT), "SERVICE_EXPIRYDATE_CONFLICT");
		reasons.put(new Integer(REQUIRED_SERVICE_IS_MISSING_ONADD), "REQUIRED_SERVICE_IS_MISSING_ONADD");
		reasons.put(new Integer(REQUIRED_SERVICE_IS_MISSING_ONREMOVE), "REQUIRED_SERVICE_IS_MISSING_ONREMOVE");
		reasons.put(new Integer(VOIP_LICENSE_CHANGE_ERROR), "VOIP_LICENSE_CHANGE_ERROR");
		reasons.put(new Integer(NO_UNASSIGNED_VOIP_LICENSE), "NO_UNASSIGNED_VOIP_LICENSE");
		reasons.put(new Integer(VOIP_SUPPLEMENTARY_DATA_RETRIEVAL_ERROR), "VOIP_SUPPLEMENTARY_DATA_RETRIEVAL_ERROR");
	}
	
}