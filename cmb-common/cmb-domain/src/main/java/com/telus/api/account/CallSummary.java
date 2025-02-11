package com.telus.api.account;

import java.util.Date;

import com.telus.api.TelusAPIException;

/**
 * <CODE>CallSummary</CODE>
 *
 */
public interface CallSummary {
	
	int CALL_ACTION_CODE_INCOMING = 1;
	int CALL_ACTION_CODE_OUTGOING = 2;

	int MESSAGE_TYPE_HOME = 0;
	int MESSAGE_TYPE_ROAMING = 1;
	
	String getCallTypeFeature();

	String getCallActionCode();

	int getMessageType();
	
	Date getDate();
	
	String getSwitchId();
	
	String getProductType();
	
	String getLocationDescription();
	
	String getLocationProvince();
	
	String getLocationCity();
	
	String getCallToCity();
	
	String getCallToState();
	
	String getCallToNumber();
	
	double getCallDuration();
	
	double getAirtimeChargeAmount();
	
	double getTollChargeAmount();
	
	double getAdditionalChargeAmount();
	
	double getTaxAmount();
	
	double getCreditedAmount();
	
	String getPeriodLevel();
	
	double getRoamingTaxTollAmount();
	
	double getRoamingTaxAirtimeAmount();
	
	double getRoamingTaxAdditionalAmount();
	
	boolean isExtendedHomeArea();
	
	Boolean isLteHspaHandover();
	
	String getBillPresentationNumber();
	
	/**
	 * Retrieves the call details.
	 *
	 * <P>This method may involve a remote method call.
	 */
	Call getCallDetails() throws TelusAPIException;
	
	/**
	 * Adjust the call.
	 *
	 * <P>This method may involve a remote method call.
	 */
	void adjust(double adjustmentAmount, String adjustmentReasonCode, String memoText) throws TelusAPIException;

}
