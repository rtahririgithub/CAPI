package com.telus.cmb.common.kafka;

public enum KafkaEventType {

	// Added below events for cods ( xml payload )
	BILLING_ACCOUNT_ALL, BILLING_ACCOUNT_UPDATED, BILLING_ACCOUNT_STATUS, BILLING_NAME, 
	CONTACT_NAME, CONTACT_NUMBERS, CONTACT_INFO, BILLING_INFO, BILLING_ADDRESS, 
	BILL_CYCLE, BILLING_CONTACT_LANG_PREF, BILLING_CONTACT_EMAIL_ADDRESS, AUTHORIZED_CONTACTS,
	
	//Service agreement change events (json payload )
	SERVICE_AGREEMENT_CHANGE,PRICEPLAN_CHANGE, SERVICE_ADD, SERVICE_REMOVE, SERVICE_MODIFY, EQUIPMENT_CHANGE, COMMITMENT_CHANGE,
	//Payment events (json payload )
	PAYMENT_METHOD_CHANGE,MAKE_PAYMENT,
	//Bill credit events (json payload )
	CREATE_CREDIT,FOLLOWUP_APPROVAL,
	//Credit check result changes (json payload )
	CREDIT_CHECK_CREATE,CREDIT_CHECK_UPDATE,	
	//Subscriber lifeCycle events (json payload )
	SUBSCRIBER_ACTIVATE,SUB_CANCEL,SUB_CANCEL_PORT_OUT,MOVE,
	//Account life cycle events (json payload )
	ACC_CANCEL,ACC_CANCEL_PORT_OUT,
	//Phone number change events (json payload )
	PHONENUMBER_CHANGE_REGULAR, PHONENUMBER_CHANGE_INTER_BRAND, PHONENUMBER_CHANGE_INTER_CARRIER, PHONENUMBER_CHANGE_INTER_MVNE;


	
	public String getAllUpdateEventTypes() {
		String updateEventTypes = BILL_CYCLE + "," + BILLING_NAME + ","+ CONTACT_NAME + "," + BILLING_ADDRESS + "," + CONTACT_NUMBERS+ "," + BILLING_CONTACT_LANG_PREF + ","+ BILLING_CONTACT_EMAIL_ADDRESS;
		return updateEventTypes;
	}

	public String getAllBillingInfoTypes() {
		String contactInfoTypes = BILLING_NAME + "," + BILLING_ADDRESS;
		return contactInfoTypes;
	}

	public String getAllContactInfoTypes() {
		String billingInfoTypes = CONTACT_NAME + "," + CONTACT_NUMBERS;
		return billingInfoTypes;
	}
	
}
